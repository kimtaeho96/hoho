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
import com.hotta.hoho.datamodel.PeopleMovieResult
import com.hotta.hoho.view.credit.CreditActivity

class PeopleMovieAdapter(val context: Context, val item: List<PeopleMovieResult>) :
    RecyclerView.Adapter<PeopleMovieAdapter.ViewHolder>() {

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        fun binItems(item: PeopleMovieResult) {

            val imgView = itemView.findViewById<ImageView>(R.id.people_movie_iv)
            val nameTv = itemView.findViewById<TextView>(R.id.people_movie_name_tv)

            Glide.with(itemView)
                .load("https://image.tmdb.org/t/p/w342${item.posterPath}")
                /* .transform(CenterCrop())*/
                .fitCenter()
                .into(imgView)
            nameTv.text = item.title

        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.people_movie_item, parent, false)

        return ViewHolder(view)

    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.binItems(item[position])
    }

    override fun getItemCount(): Int {
        return item.size
    }


}