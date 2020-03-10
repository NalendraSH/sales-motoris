package com.salesmotoris.activity.login

import com.salesmotoris.model.Login
import com.salesmotoris.mvp.BaseMvpPresenter
import com.salesmotoris.mvp.BaseMvpView

interface LoginContract {
    interface View: BaseMvpView{
        fun showResponseMessage(response: Login.LoginResponse)

        fun hideProgress()
    }
    interface Presenter: BaseMvpPresenter<View> {
        fun submitLoginData(username: String, password: String)

        fun saveUserData(accessToken: String, data: MutableMap<String, String>)
    }
}