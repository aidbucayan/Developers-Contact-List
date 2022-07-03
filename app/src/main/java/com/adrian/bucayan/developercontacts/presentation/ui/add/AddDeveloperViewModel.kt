package com.adrian.bucayan.developercontacts.presentation.ui.add

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.adrian.bucayan.developercontacts.common.Resource
import com.adrian.bucayan.developercontacts.domain.model.StatusResponse
import com.adrian.bucayan.developercontacts.domain.request.DeveloperRequest
import com.adrian.bucayan.developercontacts.domain.use_case.AddDeveloperUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

@ExperimentalCoroutinesApi
@HiltViewModel
class AddDeveloperViewModel @Inject constructor(
    private val addDeveloperUseCase: AddDeveloperUseCase
) : ViewModel() {

    private val _dataStateAddDeveloper: MutableLiveData<Resource<StatusResponse>> = MutableLiveData()

    val dataStateAddDeveloper: LiveData<Resource<StatusResponse>> = _dataStateAddDeveloper

    fun setAddDevelopersEvent(addDeveloperIntent: AddDeveloperIntent, developerRequest: DeveloperRequest) {
        viewModelScope.launch {
            when(addDeveloperIntent) {
                is AddDeveloperIntent.GetAddDeveloperIntents -> {
                    addDeveloperUseCase(developerRequest)
                        .onEach { dataStateAddDev ->
                            _dataStateAddDeveloper.value = dataStateAddDev
                        }
                        .launchIn(viewModelScope)
                }
            }
        }
    }

}

sealed class AddDeveloperIntent {
    object GetAddDeveloperIntents: AddDeveloperIntent()
}
