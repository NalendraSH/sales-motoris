package com.salesmotoris.model

object Stock {
    data class StockResponse(val meta: Meta, val data: MutableList<Data>)

    data class Data(val product: String,
                    val price: String,
                    val quantity: String)
}