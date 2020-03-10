package com.salesmotoris.fragment.home

import com.salesmotoris.api.ApiManager
import com.salesmotoris.api.ErrorResponseHandler
import com.salesmotoris.mvp.BaseMvpPresenterImpl
import rx.functions.Action1

class HomePresenter : BaseMvpPresenterImpl<HomeContract.View>(), HomeContract.Presenter{

    override fun getTwoDaysReport(accessToken: String) {
        ApiManager.getTwoDaysReport(accessToken)
            .doOnError { mView?.showMessage(it.toString()) }
            .subscribe(
                Action1 { mView?.showResponse(it) },
                ErrorResponseHandler(mView, true) {_, _, _ -> mView?.showError()}
            )
    }

}