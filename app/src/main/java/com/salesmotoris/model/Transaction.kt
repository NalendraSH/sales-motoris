package com.salesmotoris.model

object Transaction {
    data class TransactionResponse(val meta: Meta, val data: Data)

    data class Data(val day: String, val transaction: MutableList<Item>)

    data class Item(val id: String,
                    val days: String,
                    val store: String,
                    val address: String,
                    val total_income: String,
                    val total_items: String,
                    val visitation_status: String)
}