package com.salesmotoris.fragment.stock

import com.salesmotoris.model.Stock
import com.salesmotoris.mvp.BaseMvpPresenter
import com.salesmotoris.mvp.BaseMvpView

interface StockContract {
    interface View: BaseMvpView {
        fun showResponse(response: Stock.StockResponse)

        fun showError()
    }
    interface Presenter:BaseMvpPresenter<View> {
        fun getStock(accessToken: String)
    }
}