package com.hotta.hoho.view.more.paging

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.hotta.hoho.R
import com.hotta.hoho.datamodel.PopularMovieResult
import com.hotta.hoho.network.model.MovieResponse
import com.hotta.hoho.view.detail.MovieDetailActivity
import javax.inject.Inject

class MovieAdapter(val context: Context) :
    PagingDataAdapter<PopularMovieResult, MovieAdapter.ViewHolder>(differCallback) {



    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {

        fun binItems(item: PopularMovieResult) {

            val imgView = itemView.findViewById<ImageView>(R.id.imageView)
            val pop_title = itemView.findViewById<TextView>(R.id.popMovietv)
            val pop_detail = itemView.findViewById<Button>(R.id.moviedetail)

            pop_title.text = item.title

            pop_detail.setOnClickListener {
                val intent = Intent(context, MovieDetailActivity::class.java)
                intent.putExtra("id", item.id.toString())
                itemView.context.startActivity(intent)
            }
            Glide.with(itemView)
                .load("https://image.tmdb.org/t/p/w342${item.poster_path}")
                /* .transform(CenterCrop())*/
                .fitCenter()
                .into(imgView)

        }


    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.popular_movie_item, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.binItems(getItem(position)!!)
        //holder.setIsRecyclable(false)
    }

    companion object {
        private val differCallback = object : DiffUtil.ItemCallback<PopularMovieResult>() {
            override fun areItemsTheSame(
                oldItem: PopularMovieResult,
                newItem: PopularMovieResult
            ): Boolean {
                return oldItem.id == newItem.id

            }

            override fun areContentsTheSame(
                oldItem: PopularMovieResult,
                newItem: PopularMovieResult
            ): Boolean {
                return oldItem == newItem

            }

        }


    }
}