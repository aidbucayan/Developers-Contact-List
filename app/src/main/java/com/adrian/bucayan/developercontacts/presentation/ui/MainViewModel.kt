package com.adrian.bucayan.developercontacts.presentation.ui

import android.content.Context
import androidx.lifecycle.*
import com.adrian.bucayan.developercontacts.common.Resource
import com.adrian.bucayan.developercontacts.presentation.ui.util.ConnectionLiveData
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.launch
import javax.inject.Inject

@ExperimentalCoroutinesApi
@HiltViewModel
class MainViewModel @Inject constructor(
    @ApplicationContext private val appCtx: Context,
) : ViewModel() {
    private val _dataState: MutableLiveData<Resource<MainDataState>> = MutableLiveData()
    private val _connectionLiveData = ConnectionLiveData(appCtx)

    val dataState: LiveData<Resource<MainDataState>>
        get () = _dataState

    fun setStateEvent(intent: MainIntent) {
        viewModelScope.launch {
            when (intent) {
                is MainIntent.InitHomeIntent -> {
                    _dataState.value = Resource.Success(
                        MainDataState(
                            true,
                            null
                        )
                    )
                }
                is MainIntent.None -> {
                }
            }
        }
    }

    fun registerConnectionObserver(lifecycleOwner: LifecycleOwner){
        _connectionLiveData.observe(owner = lifecycleOwner, onChanged = { isConnected ->
            isConnected.let {
                val newDataState = MainDataState(
                    _dataState.value?.data?.isHomeLoaded, it
                )
                _dataState.value = Resource.Success(newDataState)
            }
        })
    }

}

sealed class MainIntent {
    object InitHomeIntent: MainIntent()
    object None : MainIntent()
}

data class MainDataState(
    var isHomeLoaded: Boolean?,
    var hasNetwork: Boolean?
)