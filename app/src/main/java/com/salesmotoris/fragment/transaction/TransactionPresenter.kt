package com.salesmotoris.fragment.transaction

import com.salesmotoris.api.ApiManager
import com.salesmotoris.api.ErrorResponseHandler
import com.salesmotoris.mvp.BaseMvpPresenterImpl
import rx.functions.Action1

class TransactionPresenter : BaseMvpPresenterImpl<TransactionContract.View>(), TransactionContract.Presenter {

    override fun getTransactions(accessToken: String, day: String) {
        ApiManager.getTransactions(accessToken, day)
            .doOnError { mView?.showMessage(it.toString()) }
            .subscribe(
                Action1 { mView?.showResponse(it) },
                ErrorResponseHandler(mView, true) {_, _, _ -> mView?.showError()}
            )
    }

}