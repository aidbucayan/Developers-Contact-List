package com.adrian.bucayan.developercontacts.presentation.ui.edit

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.adrian.bucayan.developercontacts.common.Resource
import com.adrian.bucayan.developercontacts.domain.model.StatusResponse
import com.adrian.bucayan.developercontacts.domain.request.DeveloperRequest
import com.adrian.bucayan.developercontacts.domain.use_case.DeleteDeveloperUseCase
import com.adrian.bucayan.developercontacts.domain.use_case.EditDeveloperUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

@ExperimentalCoroutinesApi
@HiltViewModel
class EditDeveloperViewModel
@Inject constructor(
    private val editDeveloperUseCase: EditDeveloperUseCase,
    private val getDeleteDeveloperUseCase: DeleteDeveloperUseCase
) : ViewModel() {
    private val _dataStateEditDeveloper: MutableLiveData<Resource<StatusResponse>> = MutableLiveData()

    val dataStateEditDeveloper: LiveData<Resource<StatusResponse>> = _dataStateEditDeveloper

    fun setEditDevelopersEvent(addDeveloperIntent: EditDeveloperIntent, developerRequest: DeveloperRequest) {
        viewModelScope.launch {
            when(addDeveloperIntent) {
                is EditDeveloperIntent.GetEditDeveloperIntents -> {
                    editDeveloperUseCase(developerRequest)
                        .onEach { dataStateEditDev ->
                            _dataStateEditDeveloper.value = dataStateEditDev
                        }
                        .launchIn(viewModelScope)
                }
            }
        }
    }

    private val _dataStateDeleteDeveloper: MutableLiveData<Resource<StatusResponse>> = MutableLiveData()

    val dataStateDeleteDeveloper: LiveData<Resource<StatusResponse>> = _dataStateDeleteDeveloper

    fun setGetDeleteDevelopersEvent(deleteDeveloperIntent: DeleteDeveloperIntent, developerRequest: DeveloperRequest) {
        viewModelScope.launch {
            when(deleteDeveloperIntent) {
                is DeleteDeveloperIntent.GetDeleteDeveloperIntents -> {
                    getDeleteDeveloperUseCase(developerRequest)
                        .onEach { dataStateDeleteDev ->
                            _dataStateDeleteDeveloper.value = dataStateDeleteDev
                        }
                        .launchIn(viewModelScope)
                }
            }
        }
    }


}

sealed class EditDeveloperIntent {
    object GetEditDeveloperIntents: EditDeveloperIntent()
}

sealed class DeleteDeveloperIntent {
    object GetDeleteDeveloperIntents: DeleteDeveloperIntent()
}
