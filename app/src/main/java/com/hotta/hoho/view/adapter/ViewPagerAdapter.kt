package com.hotta.hoho.view.adapter

import android.content.Context
import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.hotta.hoho.R
import com.hotta.hoho.datamodel.CreditsMovieResult
import com.hotta.hoho.view.credit.CreditActivity
import com.hotta.hoho.view.main.dummy.dummyAdvData

class ViewPagerAdapter(val context: Context, val item: List<dummyAdvData> ) :
    RecyclerView.Adapter<ViewPagerAdapter.ViewHolder>() {

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val imgVie=itemView.findViewById<ImageView>(R.id.imageView_idol)
        fun binItems(item: dummyAdvData) {

            imgVie.setImageResource(item.advImg)


        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewPagerAdapter.ViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.adv_item, parent, false)

        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewPagerAdapter.ViewHolder, position: Int) {
        holder.binItems(item[position])
    }

    override fun getItemCount(): Int {
        return item.size
    }
}