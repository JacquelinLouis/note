package com.jac.mynote.model

open class Note constructor(var id: Int, var title: String) {
    companion object {
        const val NEW_INSTANCE_ID : Int = -1
        const val OLD_INSTANCE_ID : Int = -2
    }

    constructor(title: String) : this(NEW_INSTANCE_ID, title)
}