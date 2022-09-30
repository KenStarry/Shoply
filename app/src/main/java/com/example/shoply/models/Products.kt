package com.example.shoply.models

data class Products(
    var pid: String?,
    var name: String?,
    var price: String?,
    var description: String?,
    var image: String?,
    var category: String?,
    var date: String?,
    var time: String?


) {

    constructor() : this(
        "",
        "",
        "",
        "",
        "",
        "",
        "",
        ""
    )
}