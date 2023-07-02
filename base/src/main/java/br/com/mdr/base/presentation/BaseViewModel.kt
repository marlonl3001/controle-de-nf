package br.com.mdr.base.presentation

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import br.com.mdr.base.ApiError
import br.com.mdr.base.extension.toErrorWrapper
import com.google.firebase.auth.FirebaseAuthException
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import retrofit2.HttpException

abstract class BaseViewModel : ViewModel() {
    private var _loading = MutableLiveData(true)
    val loading: LiveData<Boolean> = _loading

    private var _apiError = MutableLiveData<ApiError>()
    val apiError: LiveData<ApiError> = _apiError

    protected var _successMessage = MutableLiveData<Int?>()
    val successMessage: LiveData<Int?> = _successMessage

    protected fun launch(
        enableLoading: Boolean = true,
        errorBlock: ((Throwable) -> Unit?)? = null,
        successBlock: (() -> Unit?)? = null,
        block: suspend CoroutineScope.() -> Unit
    ) =
        viewModelScope.launch {
            showLoading(enableLoading, true)
            runCatching {
                block()
            }
            .onSuccess {
                showLoading(enableLoading, false)
                successBlock?.invoke()
            }
            .onFailure { error ->
                showLoading(enableLoading, false)
                if (errorBlock != null) errorBlock.invoke(error)
                else postErrorValue(error)
            }
        }

    private fun showLoading(isLoadingEnabled: Boolean, showLoading: Boolean) {
        if (isLoadingEnabled) _loading.postValue(showLoading)
    }

    private fun postErrorValue(throwable: Throwable) {
        this.postErrorValue(_apiError, throwable)
    }

    private fun postErrorValue(dispatcher: MutableLiveData<ApiError>, throwable: Throwable) {
        if (throwable is HttpException) {
            dispatcher.postValue(throwable.toErrorWrapper())
        } else if (throwable is FirebaseAuthException) {
            dispatcher.postValue(throwable.toErrorWrapper())
        }
    }
}
