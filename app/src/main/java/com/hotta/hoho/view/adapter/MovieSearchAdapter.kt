package com.hotta.hoho.view.adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.hotta.hoho.R
import com.hotta.hoho.network.model.MovieSearchResult
import com.hotta.hoho.view.detail.MovieDetailActivity
import com.hotta.hoho.view.search.SearchListActivity
import org.w3c.dom.Text

class MovieSearchAdapter(val context: Context, val item: List<MovieSearchResult>) :
    RecyclerView.Adapter<MovieSearchAdapter.ViewHolder>() {

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {

        val imgView = itemView.findViewById<ImageView>(R.id.search_movie_iv)
        val nameTv = itemView.findViewById<TextView>(R.id.search_name_tv)
        fun binItems(item: MovieSearchResult) {

            Glide.with(itemView)
                .load("https://image.tmdb.org/t/p/w342${item.backdropPath}")
                .fitCenter()
                .centerCrop()
                .into(imgView)

            nameTv.setText(item.title)

            imgView.setOnClickListener {
                val intent = Intent(context, MovieDetailActivity::class.java)
                intent.putExtra("id", item.id.toString())
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                context.startActivity(intent)
            }
        }
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): MovieSearchAdapter.ViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.movie_search_item, parent, false)

        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: MovieSearchAdapter.ViewHolder, position: Int) {
        holder.binItems(item[position])

    }

    override fun getItemCount(): Int {
        return item.size
    }


}