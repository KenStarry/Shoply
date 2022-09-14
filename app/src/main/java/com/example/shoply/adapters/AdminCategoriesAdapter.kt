package com.example.shoply.adapters

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.shoply.AdminNewProductActivity
import com.example.shoply.R
import kotlinx.android.synthetic.main.admin_category_item.view.*

class AdminCategoriesAdapter(
    private val context: Context,
    private val catImageArray: Array<Int>,
    private val catTitleArray: Array<String>

) : RecyclerView.Adapter<AdminCategoriesAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(LayoutInflater.from(parent.context).inflate(
            R.layout.admin_category_item,
            parent,
            false
        ))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        catImageArray[position].also { holder.image.setImageResource(it) }
        catTitleArray[position].also { holder.title.text = it }

        holder.itemView.setOnClickListener {

            val intent = Intent(context, AdminNewProductActivity::class.java).apply {
                putExtra("CAT_TITLE_EXTRA", catTitleArray[position])
                putExtra("CAT_IMAGE_EXTRA", catImageArray[position])
            }

            context.startActivity(intent)
        }
    }

    override fun getItemCount(): Int {
        return catTitleArray.size
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        val image: ImageView = itemView.adminCatTshirts
        val title: TextView = itemView.adminCatTitle
    }
}