package com.salesmotoris.model

object Report {
    data class ReportResponse(val meta: Meta, val data: MutableList<Item>)

    data class Item(val days: String,
                    val date: String,
                    val total_income: String,
                    val completed_visitation: String)
}