package com.adrian.bucayan.developercontacts.presentation.ui.add

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.View
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import coil.load
import coil.size.Scale
import com.adrian.bucayan.developercontacts.R
import com.adrian.bucayan.developercontacts.common.Constants
import com.adrian.bucayan.developercontacts.common.Resource
import com.adrian.bucayan.developercontacts.databinding.FragmentAddDeveloperBinding
import com.adrian.bucayan.developercontacts.domain.model.AddDeveloper
import com.adrian.bucayan.developercontacts.domain.model.Developer
import com.adrian.bucayan.developercontacts.presentation.ui.list.DevelopersListViewModel
import com.adrian.bucayan.developercontacts.presentation.ui.util.PermissionUtility
import com.google.android.material.appbar.MaterialToolbar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.ExperimentalCoroutinesApi
import timber.log.Timber

@ExperimentalCoroutinesApi
@AndroidEntryPoint
class AddDeveloperFragment : Fragment(R.layout.fragment_add_developer) {

    private var _binding: FragmentAddDeveloperBinding? = null
    private val binding get() = _binding!!
    private lateinit var toolbar: MaterialToolbar
    private val viewModel: AddDeveloperViewModel by  viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentAddDeveloperBinding.bind(view)
        initViews()
        subscribeObservers()

        requestPermission()

        binding.ivAddDevPhoto.setOnClickListener {
            pickFromGallery()
        }

        binding.btnAddDev.setOnClickListener {
            viewModel.setAddDevelopersEvent(AddDeveloperIntent.GetAddDeveloperIntents)
        }
    }

    private fun initViews() {
        setHasOptionsMenu(true)
        displaySearchViewMenuItem()
    }

    private fun displaySearchViewMenuItem() {
        toolbar = requireActivity().findViewById(R.id.topAppBar)
        toolbar.inflateMenu(R.menu.menu_main)
        toolbar.title = getString(R.string.add)
        toolbar.menu.clear()
    }

    private fun subscribeObservers() {
        viewModel.dataStateAddDeveloper.observe(viewLifecycleOwner) { dataStateAdd ->
            when (dataStateAdd) {

                is Resource.Success<AddDeveloper> -> {
                    Timber.e("dataStateDeveloper SUCCESS")
                    dataStateAdd.data?.status?.let { requireContext().applicationContext.toast(it, true) }
                    findNavController().navigate(R.id.action_addDeveloperFragment_to_developersListFragment)
                }

                is Resource.Error -> {
                    Timber.e("dataStateAdd ERROR %s", dataStateAdd.message)
                    requireContext().applicationContext.toast(dataStateAdd.message.toString(), true)
                }

                is Resource.Loading -> {
                    Timber.e("dataStateDeveloper LOADING")
                }
            }
        }
    }

    private fun requestPermission() {
        if (!PermissionUtility.hasPermissionsFile(requireContext())) {
            requestPermissions(PermissionUtility.PERMISSIONS_REQUIRED_FILE, PermissionUtility.PERMISSIONS_REQUEST_CODE)
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
        startActivityForResult(intent, Companion.PHOTO_PICKER_REQUEST_CODE)
    }

    override fun onRequestPermissionsResult(
        requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
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
        // Result code is RESULT_OK only if the user selects an Image
        if (resultCode == Activity.RESULT_OK) when (requestCode) {
            PHOTO_PICKER_REQUEST_CODE -> {
                //data.getData returns the content URI for the selected Image
                val selectedImage: Uri? = data?.data
                Timber.e("selectedImage = %s", selectedImage)

                if (selectedImage != null) {
                    binding.ivAddDevPhoto.load(selectedImage) {
                        crossfade(true)
                        placeholder(R.drawable.img)
                        scale(Scale.FILL)
                    }
                }

            }
        }
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

    companion object {
        const val PHOTO_PICKER_REQUEST_CODE = 20
    }

}