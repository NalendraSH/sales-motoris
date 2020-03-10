package com.salesmotoris.activity.takestock

import com.salesmotoris.model.Meta
import com.salesmotoris.model.Product
import com.salesmotoris.mvp.BaseMvpPresenter
import com.salesmotoris.mvp.BaseMvpView

interface TakeStockContract {
    interface View: BaseMvpView {
        fun showProducts(response: Product.ProductResponse)

        fun showSubmitResponse(response: Meta)

        fun hideMainProgress()

        fun hideButtonProgress()
    }
    interface Presenter: BaseMvpPresenter<View> {
        fun getProducts(accessToken: String)

        fun submitTakeStock(accessToken: String, productId: String, quantity: String)
    }
}