package com.salesmotoris.model

object Product {
    data class ProductResponse(val meta: Meta, val data: MutableList<Data>)

    data class Data(val id: Int,
                    val name: String,
                    val price: Int,
                    val unit: String)
}