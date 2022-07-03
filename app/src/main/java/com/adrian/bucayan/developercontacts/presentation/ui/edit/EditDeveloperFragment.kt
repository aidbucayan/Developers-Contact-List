package com.adrian.bucayan.developercontacts.presentation.ui.edit

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import coil.load
import coil.size.Scale
import com.adrian.bucayan.developercontacts.R
import com.adrian.bucayan.developercontacts.common.Constants
import com.adrian.bucayan.developercontacts.common.Resource
import com.adrian.bucayan.developercontacts.databinding.FragmentEditDeveloperBinding
import com.adrian.bucayan.developercontacts.domain.model.Developer
import com.adrian.bucayan.developercontacts.domain.model.StatusResponse
import com.adrian.bucayan.developercontacts.domain.request.DeveloperRequest
import com.adrian.bucayan.developercontacts.presentation.ui.util.PermissionUtility
import com.google.android.material.appbar.MaterialToolbar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.ExperimentalCoroutinesApi
import timber.log.Timber

@ExperimentalCoroutinesApi
@AndroidEntryPoint
class EditDeveloperFragment : Fragment(R.layout.fragment_edit_developer) {

    companion object {
        const val PHOTO_PICKER_REQUEST_CODE = 20
    }

    private var _binding: FragmentEditDeveloperBinding? = null
    private val binding get() = _binding!!
    private lateinit var toolbar: MaterialToolbar
    private val viewModel: EditDeveloperViewModel by  viewModels()
    private var selectedImage : Uri? = null
    private lateinit var developer: Developer

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentEditDeveloperBinding.bind(view)
        developer = arguments?.getParcelable(Constants.SELECTED_DEVELOPER)!!
        Timber.e("developer = ${developer.name}")
        initDevData(developer)
        initViews()
        subscribeObservers()

        binding.ivEditPhoto.setOnClickListener {
            requestPermission()
        }

        binding.btnEdit.setOnClickListener {
            viewModel.setEditDevelopersEvent(EditDeveloperIntent.GetEditDeveloperIntents,
                DeveloperRequest(
                    selectedImage.toString(),
                    binding.tvEditName.text.toString(),
                    binding.tvEditEmail.text.toString(),
                    binding.tvEditPhone.text.toString(),
                    binding.tvEditCompany.text.toString()
                )
            )

            findNavController().navigate(R.id.action_editDeveloperFragment_to_developersListFragment)
        }
    }

    private fun initDevData(developer: Developer) {
        binding.ivEditPhoto.load(developer.photo) {
            crossfade(true)
            placeholder(R.drawable.img)
            scale(Scale.FILL)
        }
        binding.tvEditName.setText(developer.name)
        binding.tvEditEmail.setText(developer.email)
        binding.tvEditPhone.setText(developer.phoneNumber)
        binding.tvEditCompany.setText(developer.companyName)
    }

    private fun subscribeObservers() {
        viewModel.dataStateEditDeveloper.observe(viewLifecycleOwner) { dataStateEdit ->
            when (dataStateEdit) {

                is Resource.Success<StatusResponse> -> {
                    Timber.e("dataStateEdit SUCCESS")
                    dataStateEdit.data?.status?.let { requireContext().applicationContext.toast(it, true) }
                    findNavController().navigate(R.id.action_editDeveloperFragment_to_developersListFragment)
                }

                is Resource.Error -> {
                    Timber.e("dataStateEdit ERROR %s", dataStateEdit.message)
                    requireContext().applicationContext.toast(dataStateEdit.message.toString(), true)
                }

                is Resource.Loading -> {
                    Timber.e("dataStateEdit LOADING")
                }
            }
        }
    }

    private fun requestPermission() {
        if (!PermissionUtility.hasPermissionsFile(requireContext())) {
            requestPermissions(PermissionUtility.PERMISSIONS_REQUIRED_FILE, PermissionUtility.PERMISSIONS_REQUEST_CODE)
        } else {
            pickFromGallery()
        }
    }

    private fun pickFromGallery() {
        //Create an Intent with action as ACTION_PICK
        val intent = Intent(Intent.ACTION_PICK)
        // Sets the type as image/*. This ensures only components of type image are selected
        intent.type = "image/*"
        //We pass an extra array with the accepted mime types. This will ensure only components with these MIME types as targeted.
        val mimeTypes = arrayOf("image/jpeg", "image/png")
        intent.putExtra(Intent.EXTRA_MIME_TYPES, mimeTypes)
        // Launching the Intent
        startActivityForResult(intent, PHOTO_PICKER_REQUEST_CODE)
    }

    override fun onRequestPermissionsResult(
        requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        if (requestCode == PermissionUtility.PERMISSIONS_REQUEST_CODE) {
            if (PackageManager.PERMISSION_GRANTED == grantResults.firstOrNull()) {
                // Take the user to the success fragment when permission is granted
                Toast.makeText(context, "Permission request granted", Toast.LENGTH_LONG).show()
                pickFromGallery()
            } else {
                Toast.makeText(context, "Permission request denied", Toast.LENGTH_LONG).show()
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (resultCode == Activity.RESULT_OK) when (requestCode) {
            PHOTO_PICKER_REQUEST_CODE -> {
                //data.getData returns the content URI for the selected Image
                val selectedImage: Uri? = data?.data
                Timber.e("selectedImage = %s", selectedImage)

                if (selectedImage != null) {
                    binding.ivEditPhoto.load(selectedImage) {
                        crossfade(true)
                        placeholder(R.drawable.img)
                        scale(Scale.FILL)
                    }
                }

            }
        }
    }

    private fun initViews() {
        setHasOptionsMenu(true)
        displaySearchViewMenuItem()
    }

    private fun displaySearchViewMenuItem() {
        toolbar = requireActivity().findViewById(R.id.topAppBar)
        toolbar.inflateMenu(R.menu.menu_main)
        toolbar.title = getString(R.string.update_contact)
        toolbar.menu.clear()
    }
    private fun Context.toast(message: CharSequence, isLengthLong: Boolean = true) =
        Toast.makeText(
            this, message, if (isLengthLong) {
                Toast.LENGTH_LONG
            } else {
                Toast.LENGTH_SHORT
            }
        ).show()

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}