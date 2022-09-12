package com.example.shoply.models

data class Users(

    val name: String,
    val phone: String,
    val password: String

) {

    constructor() : this("", "", "") {}
}