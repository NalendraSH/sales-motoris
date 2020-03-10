package com.salesmotoris.activity.inputdetailtransaction

import com.salesmotoris.api.ApiManager
import com.salesmotoris.api.ErrorResponseHandler
import com.salesmotoris.model.DetailTransaction
import com.salesmotoris.mvp.BaseMvpPresenterImpl
import rx.functions.Action1

class InputDetailTransactionPresenter : BaseMvpPresenterImpl<InputDetailTransactionContract.View>(), InputDetailTransactionContract.Presenter {

    override fun getDetailTransactions(accessToken: String, transactionId: String) {
        ApiManager.getDetailTransactions(accessToken, transactionId)
            .doOnError { mView?.showMessage(it.toString()) }
            .subscribe(
                Action1 { mView?.showResponse(it) },
                ErrorResponseHandler(mView, true) {_, _, _ -> mView?.showError()}
            )
    }

    override fun submitDetailTransaction(
        accessToken: String,
        detailTransaction: DetailTransaction.DetailTransactionBody
    ) {
        ApiManager.submitDetailTransaction(accessToken, detailTransaction)
            .doOnError { mView?.showMessage(it.toString()) }
            .subscribe(
                Action1 { mView?.showSubmitResponse(it) },
                ErrorResponseHandler(mView, true) {_, _, _ -> mView?.hideButtonProgress()}
            )
    }

}