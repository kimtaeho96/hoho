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

class CreditMovieAdapter(val context: Context, val item: List<CreditsMovieResult>) :
    RecyclerView.Adapter<CreditMovieAdapter.ViewHolder>() {

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {

        fun binItems(item: CreditsMovieResult) {

            val imgView = itemView.findViewById<ImageView>(R.id.creditImgView)
            val nameTv = itemView.findViewById<TextView>(R.id.creditName)


            nameTv.text = item.name

            Log.d("actorid", item.id.toString())
            Glide.with(itemView)
                .load("https://image.tmdb.org/t/p/w342${item.profile_path}")
                .fitCenter()
                .into(imgView)

            imgView.setOnClickListener {
                val intent = Intent(context, CreditActivity::class.java)
                intent.putExtra("id", item.id)
                itemView.context.startActivity(intent)


            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.credits_movie_item, parent, false)

        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        holder.binItems(item[position])
    }

    override fun getItemCount(): Int {
        return item.size
    }
}