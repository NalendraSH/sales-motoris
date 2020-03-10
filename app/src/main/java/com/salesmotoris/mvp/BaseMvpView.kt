package com.salesmotoris.mvp

import android.content.Context
import androidx.annotation.StringRes

/**
 * Created by andrewkhristyan on 10/2/16.
 */
interface BaseMvpView {

    fun getContext(): Context

    fun showError(error: String?)

    fun showError(@StringRes stringResId: Int)

    fun showMessage(@StringRes srtResId: Int)

    fun showMessage(message: String)

}
