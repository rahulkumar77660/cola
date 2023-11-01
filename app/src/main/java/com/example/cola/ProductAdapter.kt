package com.example.cola

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide

class ProductAdapter(private val pview: List<DataModel>,val context: Context): RecyclerView.Adapter<ProductAdapter.MyHolder>(){
    class MyHolder(itemView: View): RecyclerView.ViewHolder(itemView){


        val p_name = itemView.findViewById<TextView>(R.id.list_name)
        val descriptor = itemView.findViewById<TextView>(R.id.list_desciption)
        val price = itemView.findViewById<TextView>(R.id.list_price)
        val image1 = itemView.findViewById<ImageView>(R.id.p_image)

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyHolder {

        val layout = LayoutInflater.from(parent.context).inflate(R.layout.item,parent,false)
        return MyHolder(layout)
    }

    override fun getItemCount(): Int {

        return pview.size
    }

    override fun onBindViewHolder(holder: MyHolder, position: Int) {

        holder.p_name.text = pview[position].name
        holder.descriptor.text = pview[position].desciption
        holder.price.text = pview[position].price.toString()

        Glide.with(context).load(pview[position].image).into(holder.image1)
    }
}


