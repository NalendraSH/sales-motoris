package com.salesmotoris.model

object DetailTransaction {

    data class DetailTransactionResponse(val meta: Meta, val data: Data)

    data class Data(val detail_transaction: MutableList<DetailTransaction>,
                    val total_income: Int,
                    val image: String)

    data class DetailTransaction(var product: String?,
                                 var price: Int?,
                                 var unit: String?,
                                 var quantity: Int,
                                 var sub_total: Int?)

    data class DetailTransactionBody(val detail_transaction: MutableList<DataTransactionBody>,
                                     val transaction_id: Int,
                                     val total_income: Long,
                                     val total_items: Int)

    data class DataTransactionBody(val id_product: Int,
                                   var quantity: Int,
                                   val sub_total: Int)

    data class Coordinate(val latCurrent: Double, val lonCurrent: Double)

}