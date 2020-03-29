package com.salesmotoris.model

object Transaction {
    data class TransactionResponse(val meta: Meta, val data: MutableList<Item>)

    data class Item(val id: String,
                    val days: String,
                    val id_store: Int,
                    val store: String,
                    val address: String,
                    val total_income: String,
                    val total_items: String,
                    val id_visitation: Int,
                    val visitation_status: String)
}