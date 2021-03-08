package com.jac.note.model

class Note constructor(var id: Int, var title: String, var content: String, var type: Int) {
    companion object {
        const val NEW_INSTANCE_ID : Int = -1
        const val OLD_INSTANCE_ID : Int = -2
    }

    interface Type {
        companion object {
            const val TEXT : Int = 0
            const val PASSWORD : Int = 1
        }
    }

    constructor(title: String, content: String) : this(NEW_INSTANCE_ID, title, content, Type.TEXT)
    constructor() : this("", "")
}