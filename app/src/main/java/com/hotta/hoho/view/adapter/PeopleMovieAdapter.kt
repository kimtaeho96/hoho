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
import com.hotta.hoho.datamodel.PeopleMovieResult
import com.hotta.hoho.view.credit.CreditActivity
import com.hotta.hoho.view.detail.MovieDetailActivity

class PeopleMovieAdapter(
    val context: Context,
    val item: List<PeopleMovieResult>,
    val creditActivity: CreditActivity
) :
    RecyclerView.Adapter<PeopleMovieAdapter.ViewHolder>() {

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {

        // 추가된 초기화 코드

        fun binItems(item: PeopleMovieResult) {

            val imgView = itemView.findViewById<ImageView>(R.id.people_movie_iv)
            val nullImgView = itemView.findViewById<ImageView>(R.id.people_movie_null_iv)
            val nameTv = itemView.findViewById<TextView>(R.id.people_movie_name_tv)


            if (item.posterPath?.isEmpty() == false) {
                Glide.with(itemView)
                    .load("https://image.tmdb.org/t/p/w342${item.posterPath}")
                    .fitCenter()
                    .centerCrop()
                    .into(imgView)
            } else {
                nullImgView.visibility = View.VISIBLE
            }
            nameTv.text = item.title
            imgView.setOnClickListener {
                val intent = Intent(context, MovieDetailActivity::class.java)
                intent.putExtra("id", item.id.toString())
                //intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
                itemView.context.startActivity(intent)
                creditActivity.finish()
            }


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