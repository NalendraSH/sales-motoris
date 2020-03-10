package com.salesmotoris.fragment.visitation

import com.salesmotoris.api.ApiManager
import com.salesmotoris.api.ErrorResponseHandler
import com.salesmotoris.mvp.BaseMvpPresenterImpl
import rx.functions.Action1

class VisitationPresenter : BaseMvpPresenterImpl<VisitationContract.View>(), VisitationContract.Presenter {

    override fun getVisitation(accessToken: String, idSales: String) {
        ApiManager.getVisitation(accessToken, idSales)
            .doOnError { mView?.showMessage(it.toString()) }
            .subscribe(
                Action1 { mView?.showResponse(it) },
                ErrorResponseHandler(mView, true) {_, _, _ -> mView?.showError()}
            )
    }

}