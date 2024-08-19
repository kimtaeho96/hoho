package com.hotta.hoho.view.adapter

import android.content.Context
import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import com.hotta.hoho.R
import com.hotta.hoho.utils.FireBaseAuthUtils
import com.hotta.hoho.utils.FireBaseRef
import com.hotta.hoho.view.detail.MovieDetailActivity
import com.hotta.hoho.view.detail.ReviewActivity
import com.hotta.hoho.view.detail.ReviewLkeModel
import com.hotta.hoho.view.detail.ReviewModel
import com.hotta.hoho.view.myreview.MyReviewModel

class MyReviewAdapter(
    val context: Context,
    val item: List<MyReviewModel>,
) :
    RecyclerView.Adapter<MyReviewAdapter.ViewHolder>() {
    interface ItemClick {
        fun onClick(view: View, position: Int)
    }

    var itemClick: ItemClick? = null


    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {

        val movieName = itemView.findViewById<TextView>(R.id.my_review_movie_name)

        val moveImg = itemView.findViewById<ImageView>(R.id.my_review_movie_img)
        val reviewGoodBad = itemView.findViewById<TextView>(R.id.my_review_good_bad)
        val reviewContent = itemView.findViewById<TextView>(R.id.my_review_movie_content)
        val reviewTime = itemView.findViewById<TextView>(R.id.my_review_movie_time)

        val reviewDelte = itemView.findViewById<TextView>(R.id.my_review_movie_delete)
        val reviewModify = itemView.findViewById<TextView>(R.id.my_review_movie_modify)


        fun bind(item: MyReviewModel) {
            movieName.text = item.movieName
            reviewContent.text = item.text
            reviewGoodBad.text = item.goodBad
            reviewTime.text = item.time

            Glide.with(itemView)
                .load("https://image.tmdb.org/t/p/w342${item.posterPath}")
                /* .transform(CenterCrop())*/
                .fitCenter()
                .into(moveImg)

            moveImg.setOnClickListener {
                val intent = Intent(context, MovieDetailActivity::class.java)
                intent.putExtra("id", item.moveId)
                context.startActivity(intent)
            }

          /*  reviewModify.setOnClickListener {
                Toast.makeText(context, "수정 클릭", Toast.LENGTH_SHORT).show()
                val intent = Intent(context, ReviewActivity::class.java)
                intent.putExtra("수정", item.moveId)
                context.startActivity(intent)
            }

            reviewDelte.setOnClickListener {
                var storage = Firebase.storage
                val storageRef = storage.reference
                Toast.makeText(context, "삭제 클릭.", Toast.LENGTH_SHORT).show()

                FireBaseRef.movieReview.child(getId)
                    .child(FireBaseAuthUtils.getUid())
                    .removeValue()
                FireBaseRef.reviewLike.child(getId).child(FireBaseAuthUtils.getUid())
                    .child(allReviewKeyList[position])
                    .removeValue()


                val mountainsRef =
                    storageRef.child(getId)
                        .child(FireBaseAuthUtils.getUid() + ".png")
                mountainsRef.delete().addOnSuccessListener {
                }.addOnFailureListener {
                }
                check = true

            }*/

        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.my_review_item, parent, false)

        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return item.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(item[position])

        if (itemClick != null) {
            holder.itemView.setOnClickListener { v ->
                itemClick?.onClick(v, position)
            }
        }


    }


}