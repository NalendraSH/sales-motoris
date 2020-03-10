package com.salesmotoris.model

object DetailTransaction {
    data class DetailTransactionResponse(val meta: Meta, val data: Data)

    data class Data(val detail_transaction: MutableList<DetailTransaction>,
                    val total_income: String,
                    val image: String)

    data class DetailTransaction(val product: String,
                                 val price: String?,
                                 val unit: String?,
                                 val quantity: String,
                                 val sub_total: String?)

    data class DetailTransactionBody(val detail_transaction: MutableList<DataTransactionBody>,
                                     val transaction_id: Int,
                                     val total_income: Long,
                                     val total_items: Int)

    data class DataTransactionBody(val id_product: Int,
                                   var quantity: Int,
                                   val sub_total: Long)
}