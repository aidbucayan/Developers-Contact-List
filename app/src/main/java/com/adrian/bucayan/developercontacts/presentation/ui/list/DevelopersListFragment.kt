package com.adrian.bucayan.developercontacts.presentation.ui.list

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.View
import android.widget.Toast
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.adrian.bucayan.developercontacts.R
import com.adrian.bucayan.developercontacts.common.Resource
import com.adrian.bucayan.developercontacts.databinding.FragmentDevelopersListBinding
import com.adrian.bucayan.developercontacts.domain.model.Developer
import com.google.android.material.appbar.MaterialToolbar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.ExperimentalCoroutinesApi
import timber.log.Timber

@ExperimentalCoroutinesApi
@AndroidEntryPoint
class DevelopersListFragment : Fragment(R.layout.fragment_developers_list) {

    private var _binding: FragmentDevelopersListBinding? = null
    private val binding get() = _binding!!
    private lateinit var toolbar: MaterialToolbar
    private var developerListAdapter : DeveloperListAdapter? = null
    private val viewModel: DevelopersListViewModel by  viewModels()
    var developerList : List<Developer>? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentDevelopersListBinding.bind(view)
        initViews()
        subscribeObservers()
        binding.devListFab.setOnClickListener {
            findNavController().navigate(R.id.action_developersListFragment_to_addDeveloperFragment)
        }
        // TODO viewModel.setGetDevelopersEvent(DeveloperIntent.GetDeveloperIntents)
    }

    private fun subscribeObservers() {
        viewModel.dataStateDeveloper.observe(viewLifecycleOwner) { dataStateGetDev ->
            when (dataStateGetDev) {

                is Resource.Success<List<Developer>> -> {
                    Timber.e("dataStateDeveloper SUCCESS")
                    displayLoading(false)
                    binding.devListRecyclerview.visibility = View.VISIBLE
                    binding.devListTvEmptyMsg.visibility = View.GONE

                    developerList = dataStateGetDev.data
                    if (!developerList.isNullOrEmpty()) {
                        developerListAdapter!!.submitList(developerList!!)
                    } else {
                        binding.devListTvEmptyMsg.visibility = View.VISIBLE
                    }

                }

                is Resource.Error -> {
                    Timber.e("dataStateDeveloper ERROR %s", dataStateGetDev.message)
                    requireContext().toast(dataStateGetDev.message.toString(), true)
                    displayLoading(false)
                }

                is Resource.Loading -> {
                    Timber.e("dataStateDeveloper LOADING")
                    displayLoading(true)
                }
            }
        }
    }

    private fun initViews() {
        setHasOptionsMenu(true)
        displaySearchViewMenuItem(true)
        initRecyclerview()
    }

    private fun displaySearchViewMenuItem(isVisible: Boolean) {
        toolbar = requireActivity().findViewById(R.id.topAppBar)
        toolbar.inflateMenu(R.menu.menu_main)
        toolbar.title = getString(R.string.app_name)
        val searchViewItem = toolbar.menu.findItem(R.id.appbar_search)
        val searchView = searchViewItem?.actionView as SearchView
        if (isVisible) {
            toolbar.setNavigationIcon(R.drawable.baseline_contacts_black_24dp)
            toolbar.setNavigationIconTint(R.color.colorPrimaryVariant)
            searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
                override fun onQueryTextSubmit(query: String?): Boolean {
                    searchView.clearFocus()
                    showSearchResult(query)
                    Timber.d("SUBMIT")
                    return false
                }

                override fun onQueryTextChange(newText: String?): Boolean {
                    //showSearchResult(newText)
                    return false
                }
            })
            searchView.visibility = View.VISIBLE
        }
        else {
            searchView.clearFocus()
            searchView.visibility = View.GONE
            toolbar.menu.clear()
        }

    }


    private fun initRecyclerview() {
        binding.devListRecyclerview.apply {
            layoutManager = LinearLayoutManager(this.context, LinearLayoutManager.VERTICAL, false)

            val itemDecoration: DeveloperListAdapter.TopSpacingDecoration =
                DeveloperListAdapter.TopSpacingDecoration(20)
            addItemDecoration(itemDecoration)

            developerListAdapter = DeveloperListAdapter()
            developerListAdapter!!.toSelectDeveloper = this@DevelopersListFragment::gotoSelectedDeveloper
            adapter = developerListAdapter
        }

    }

    private fun showSearchResult(query: String?) {
        if (query != null) {
            developerList?.filter { it -> it.name!!.startsWith(query, true) }
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

    private fun displayLoading(isDisplayed: Boolean) {
        if (isDisplayed) {
            binding.devListLinearProgressBarLoadMore.visibility = View.VISIBLE
        } else {
            binding.devListLinearProgressBarLoadMore.visibility = View.GONE
        }
    }

    private fun gotoSelectedDeveloper(developer: Developer, position: Int?) {

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}