package com.hotta.hoho.view.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.hotta.hoho.R
import com.hotta.hoho.datamodel.MovieDto

class DayMovieAdapter(context: Context, private val item: List<MovieDto>) :
    RecyclerView.Adapter<DayMovieAdapter.ViewHolder>() {

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {

        val movietv = view.findViewById<TextView>(R.id.movieName)
        val movieranktv = view.findViewById<TextView>(R.id.movieRank)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.movieday_list_item, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.movietv.text = item[position].movieNm
        holder.movieranktv.text = item[position].rank
    }

    override fun getItemCount(): Int {
        return item.size
    }
}