package com.salesmotoris.activity.login

import com.salesmotoris.api.ApiManager
import com.salesmotoris.api.ErrorResponseHandler
import com.salesmotoris.library.SalesMotorisPref
import com.salesmotoris.mvp.BaseMvpPresenterImpl
import rx.functions.Action1

class LoginPresenter : BaseMvpPresenterImpl<LoginContract.View>(), LoginContract.Presenter{

    override fun submitLoginData(username: String, password: String) {
        ApiManager.submitLoginData(username, password)
            .doOnError { mView?.showMessage(it.toString()) }
            .subscribe(
                Action1 { mView?.showResponseMessage(it) },
                ErrorResponseHandler(mView, true) {_, _, _ -> mView?.hideProgress()}
            )
    }

    override fun saveUserData(accessToken: String, data: MutableMap<String, String>) {
        val preferences = mView?.getContext()?.let { SalesMotorisPref(it) }
        preferences?.accessToken = accessToken
        preferences?.id = data["id"]
        preferences?.username = data["username"]
        preferences?.email = data["email"]
    }

}