package com.jac.mynote.model

class PasswordContentNote(id: Int, title: String, content: String) : SingleContentNote(id, title, content) {
    constructor(title: String, content: String) : this(NEW_INSTANCE_ID, title, content)
    constructor() : this(NEW_INSTANCE_ID, "", "")
}