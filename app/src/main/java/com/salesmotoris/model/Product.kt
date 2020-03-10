package com.salesmotoris.model

object Product {
    data class ProductResponse(val meta: Meta, val data: MutableList<Data>)

    data class Data(val id: String,
                    val name: String,
                    val price: String,
                    val unit: String)
}