package com.appinsnap.aishrm.base

import androidx.lifecycle.ViewModel
import com.eshaafi.patient.util.observer.toObject
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.cancel
import retrofit2.HttpException
import javax.inject.Inject

@HiltViewModel
open class BaseViewModel @Inject constructor() : ViewModel() {

    //kotlin corutines
    private val job = Job()
    protected val coroutinesScope: CoroutineScope = CoroutineScope(job + Dispatchers.IO)


//    protected fun handleResponse(response: Any, event: Int): ClickEvent<NetworkResult<Any>> {
//        response as BaseResponse<*>
//
//        return when (response.statusCode) {
//            200 -> {
//                when (event) {
//                    Constants.LOGIN_REQUEST -> ClickEvent(NetworkResult.Success(response.data as LoginResponse))
//
//                    Constants.VERIFY_OTP_PASSWORD_REQUEST -> ClickEvent(
//                        NetworkResult.Success(
//                            response.data as OtpResponse
//                        )
//                    )
//
//                    Constants.RESEND_OTP_REQUEST -> ClickEvent(NetworkResult.Success(response.data as JSONObject))
//
//                    Constants.REGISTRATION_REQUEST -> ClickEvent(NetworkResult.Success(response.data as RegResponse))
//
//                    Constants.RESET_PASSWORD_REQUEST -> ClickEvent(NetworkResult.Success(response.data as JSONObject))
//
//                    else -> ClickEvent(NetworkResult.Success(response.data as LoginResponse))
//
//                }
//            }
//
//            else -> {
//                ClickEvent(NetworkResult.Error(response.message))
//            }
//        }
//    }
protected fun handleErrorResponse(e: Exception): String {
    return if (e is HttpException) {
        val error = e.response()?.errorBody()?.string()?.toObject<ErrorResponse>()
        if (error?.statusCode == 404)
            error.data.message
        else
            error?.message!!
    } else {
        e.message.toString()
    }

}

    override fun onCleared() {
        coroutinesScope.cancel()
        super.onCleared()

    }
}