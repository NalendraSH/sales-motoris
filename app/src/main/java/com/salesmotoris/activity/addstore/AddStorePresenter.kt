package com.salesmotoris.activity.addstore

import com.salesmotoris.api.ApiManager
import com.salesmotoris.api.ErrorResponseHandler
import com.salesmotoris.model.AddStore
import com.salesmotoris.mvp.BaseMvpPresenterImpl
import rx.functions.Action1

class AddStorePresenter: BaseMvpPresenterImpl<AddStoreContract.View>(), AddStoreContract.Presenter {
    override fun submitStore(accessToken: String, locationDetail: AddStore.AddStoreJson) {
        ApiManager.submitStore(accessToken, locationDetail)
            .doOnError { mView?.showMessage(it.toString()) }
            .subscribe(
                Action1 { mView?.showResponse(it) },
                ErrorResponseHandler(mView, true) {_, _, _ -> mView?.hideProgress()}
            )
    }

}