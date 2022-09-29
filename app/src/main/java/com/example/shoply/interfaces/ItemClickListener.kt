package com.example.shoply.interfaces

import android.view.View

interface ItemClickListener {
    fun onClick(view: View, pos: Int, isLongClick: Boolean)
}