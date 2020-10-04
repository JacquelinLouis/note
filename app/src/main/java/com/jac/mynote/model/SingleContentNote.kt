package com.jac.mynote.model

open class SingleContentNote(id: Int, title: String, var content: String) : Note(id, title) {
    constructor(title: String, content: String) : this(NEW_INSTANCE_ID, title, content)
}