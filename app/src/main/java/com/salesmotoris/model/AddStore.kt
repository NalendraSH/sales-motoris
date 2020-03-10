package com.salesmotoris.model

object AddStore {
    data class AddStoreResponse(val code: Int, val message: String)

    data class AddStoreJson(val latCurrent: Double,
                            val latStore: Double,
                            val lonCurrent: Double,
                            val lonStore: Double,
                            val name: String,
                            val address: String)
}