package com.salesmotoris.model

object Report {
    data class ReportResponse(val meta: Meta, val data: Data)

    data class Data(val daily: MutableList<Item>, val target: Target)

    data class Item(val days: String,
                    val date: String,
                    val total_income: String,
                    val completed_visitation: String)

    data class Target(val eff_call: Int, val income: Int)
}