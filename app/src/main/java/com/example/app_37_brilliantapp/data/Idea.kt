package com.example.app_37_brilliantapp.data

data class Idea(var title: String, var done: Boolean = false, var createTime: String) {

    @Suppress("unused")
    constructor() : this("", false, "")

}