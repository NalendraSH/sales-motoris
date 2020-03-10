package com.salesmotoris.model

object Visitation {
    data class VisitationResponse(val meta: Meta, val data: Data)

    data class Data(val Senin: MutableList<String>,
                    val Selasa: MutableList<String>,
                    val Rabu: MutableList<String>,
                    val Kamis: MutableList<String>,
                    val Jumat: MutableList<String>)
}