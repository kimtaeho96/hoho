package com.hotta.hoho.view.adapter

import android.content.Context
import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.hotta.hoho.R
import com.hotta.hoho.datamodel.PopularMovieResult
import com.hotta.hoho.datamodel.PopularTvResult
import com.hotta.hoho.view.detail.MovieDetailActivity

class CurMovieAdapter(
    val context: Context,
    val item: List<PopularMovieResult>

) :
    RecyclerView.Adapter<CurMovieAdapter.ViewHolder>() {

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

        /* //클릭이벤트
         if (itemClick != null) {
             holder.pop_detail.setOnClickListener { v ->
                 itemClick?.onClick(v, position)
             }`
         }*/

            holder.binItems(item[position])



    }

    override fun getItemCount(): Int {
        return item.size

    }

    interface ItemClick {
        fun onClick(view: View, position: Int)
    }

    var itemClick: ItemClick? = null


}