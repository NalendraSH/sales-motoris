package com.salesmotoris.api

import android.text.TextUtils
import androidx.annotation.StringRes
import com.salesmotoris.R
import com.salesmotoris.mvp.BaseMvpView
import retrofit2.HttpException
import rx.functions.Action1
import java.lang.ref.WeakReference
import java.net.SocketException
import java.net.SocketTimeoutException
import java.net.UnknownHostException

/**
 * Created by andrewkhristyan on 10/31/16.
 */

class ErrorResponseHandler(view: BaseMvpView? = null,
                           private val mShowError: Boolean = false,
                           val onFailure: (Throwable, ErrorResponseBody?, Boolean) -> Unit)
: Action1<Throwable> {

    private val mViewReference: WeakReference<BaseMvpView> = WeakReference<BaseMvpView>(view)

    override fun call(throwable: Throwable) {
        var isNetwork = false
        var errorBody: ErrorResponseBody? = null
        if (isNetworkError(throwable)) {
            isNetwork = true
            showMessage(R.string.internet_connection_error)
        } else if (throwable is HttpException) {
            errorBody = ErrorResponseBody.parseError(throwable.response())
            if (errorBody != null) {
                handleError(errorBody)
            }
        }

        onFailure(throwable, errorBody, isNetwork)
    }

    private fun isNetworkError(throwable: Throwable): Boolean {
        return throwable is SocketException ||
                throwable is UnknownHostException ||
                throwable is SocketTimeoutException
    }

    private fun handleError(errorBody: ErrorResponseBody) {
        if (errorBody.code != ErrorResponseBody.UNKNOWN_ERROR) {
            showErrorIfRequired(R.string.server_error)
        }
    }

    private fun showErrorIfRequired(@StringRes strResId: Int) {
        if (mShowError) {
            mViewReference.get()?.showError(strResId)
        }
    }

    private fun showErrorIfRequired(error: String) {
        if (mShowError && !TextUtils.isEmpty(error)) {
            mViewReference.get()?.showError(error)
        }
    }

    private fun showMessage(@StringRes strResId: Int) {
        mViewReference.get()?.showMessage(strResId)
    }

    private fun showMessage(error: String) {
        if (error.isNotEmpty()) {
            mViewReference.get()?.showError(error)
        }
    }

}