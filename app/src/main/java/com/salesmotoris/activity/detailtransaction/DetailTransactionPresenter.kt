package com.salesmotoris.activity.detailtransaction

import com.salesmotoris.api.ApiManager
import com.salesmotoris.api.ErrorResponseHandler
import com.salesmotoris.mvp.BaseMvpPresenterImpl
import rx.functions.Action1

class DetailTransactionPresenter : BaseMvpPresenterImpl<DetailTransactionContract.View>(), DetailTransactionContract.Presenter {

    override fun getDetailTransactions(accessToken: String, transactionId: String) {
        ApiManager.getDetailTransactions(accessToken, transactionId)
            .doOnError { mView?.showMessage(it.toString()) }
            .subscribe(
                Action1 { mView?.showResponse(it) },
                ErrorResponseHandler(mView, true) {_, _, _ -> mView?.showError()}
            )
    }

}