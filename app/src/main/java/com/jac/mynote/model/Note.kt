package com.jac.mynote.model

open class Note constructor(val id: Int, var title: String) {
    companion object {
        const val NEW_INSTANCE_ID : Int = -1
    }

    constructor(title: String) : this(NEW_INSTANCE_ID, title)
}