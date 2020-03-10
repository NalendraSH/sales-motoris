package com.salesmotoris.activity.addstore

import com.salesmotoris.model.AddStore
import com.salesmotoris.mvp.BaseMvpPresenter
import com.salesmotoris.mvp.BaseMvpView

interface AddStoreContract {
    interface View: BaseMvpView {
        fun showResponse(response: AddStore.AddStoreResponse)

        fun hideProgress()
    }
    interface Presenter: BaseMvpPresenter<View> {
        fun submitStore(accessToken: String, locationDetail: AddStore.AddStoreJson)
    }
}