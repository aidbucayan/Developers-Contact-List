package com.adrian.bucayan.developercontacts.presentation.ui.list

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.adrian.bucayan.developercontacts.common.Resource
import com.adrian.bucayan.developercontacts.domain.model.Developer
import com.adrian.bucayan.developercontacts.domain.use_case.GetDevelopersListUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

@ExperimentalCoroutinesApi
@HiltViewModel
class DevelopersListViewModel @Inject constructor(
    private val getDevelopersListUseCase: GetDevelopersListUseCase) : ViewModel() {

    private val _dataStateDeveloper: MutableLiveData<Resource<List<Developer>>> = MutableLiveData()

    val dataStateDeveloper: LiveData<Resource<List<Developer>>> = _dataStateDeveloper

    fun setGetDevelopersEvent(developerIntent: DeveloperIntent) {
        viewModelScope.launch {
            when(developerIntent) {
                is DeveloperIntent.GetDeveloperIntents -> {
                    getDevelopersListUseCase()
                        .onEach { dataStateGetDev ->
                            _dataStateDeveloper.value = dataStateGetDev
                        }
                        .launchIn(viewModelScope)
                }

                is DeveloperIntent.None -> {

                }
            }
        }
    }
}

sealed class DeveloperIntent {

    object GetDeveloperIntents: DeveloperIntent()

    object None: DeveloperIntent()
}
