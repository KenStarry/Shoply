package com.example.shoply.viewholder

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.shoply.R
import com.example.shoply.interfaces.ItemClickListener
import org.w3c.dom.Text

class ProductsViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView), View.OnClickListener {

    private lateinit var listener: ItemClickListener

    val prodName: TextView = itemView.findViewById(R.id.home_product_name)
    val prodDesc: TextView = itemView.findViewById(R.id.home_product_description)
    val prodPrice: TextView = itemView.findViewById(R.id.home_product_price)
    val prodImageView: ImageView = itemView.findViewById(R.id.home_product_image)

    fun setItemClickListener(listener: ItemClickListener) {
        this.listener = listener
    }

    override fun onClick(view: View?) {
        listener.onClick(view!!, bindingAdapterPosition, false)
    }
}