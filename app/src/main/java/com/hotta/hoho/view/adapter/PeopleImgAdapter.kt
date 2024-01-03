package com.hotta.hoho.view.adapter

import android.content.Context
import android.graphics.Typeface
import android.util.Log
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.hotta.hoho.R
import com.hotta.hoho.datamodel.PeopleImgResult


class PeopleImgAdapter(
    val context: Context,
    val item: List<PeopleImgResult>,
    ) :
    RecyclerView.Adapter<PeopleImgAdapter.ViewHolder>() {
    private var showAllItems = false

    interface ItemClick {
        fun onClick(view: View, position: Int)
    }

    var itemClick: ItemClick? = null


    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val imgView = itemView.findViewById<ImageView>(R.id.people_img_iv)
        val imgCountTv = TextView(context)

        fun binItems(item: PeopleImgResult) {

            imgView.layoutParams?.apply {
                height = (500..1000).random()
            }
            Glide.with(itemView)
                .load("https://image.tmdb.org/t/p/w342${item.file_path}")
                .fitCenter()
                .centerCrop()
                .into(imgView)

            imgCountTv.setOnClickListener {
                showAllItems()
            }

            imgCountTv.text = "+" + (itemCount - 5).toString()
        }

        fun showMoreImg() {
            // 더보기 버튼을 보이도록 처리하는 로직
            imgCountTv.visibility = View.VISIBLE

            val overlayView = View(context)
            val container = FrameLayout(context)
            val parentView = imgView.parent as ViewGroup


            parentView.addView(overlayView)
            overlayView.layoutParams = imgView.layoutParams
            overlayView.bringToFront()
            overlayView.setBackgroundResource(R.drawable.more_img_rounder)

            imgCountTv.text = "+" + (itemCount - 5)
            imgCountTv.textSize = 30.toFloat()
            imgCountTv.setTypeface(null, Typeface.BOLD)
            imgCountTv.setTextColor(ContextCompat.getColor(context, android.R.color.white))

            container.layoutParams = imgView.layoutParams
            val layoutParams = FrameLayout.LayoutParams(
                FrameLayout.LayoutParams.WRAP_CONTENT,
                FrameLayout.LayoutParams.WRAP_CONTENT
            )
            layoutParams.gravity = Gravity.CENTER
            container.addView(imgCountTv, layoutParams)
            parentView.addView(container)
        }

        fun hideMoreImg() {
            // 더보기 버튼을 숨기도록 처리하는 로직
            imgCountTv.visibility = View.GONE
        }

        fun disableTouch(test: Boolean) {
            Log.d("rlaxogh", "콜백돼냐!")
            imgView.isClickable = test
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.people_img_item, parent, false)

        return ViewHolder(view)

    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        if (showAllItems) {
            holder.binItems(item[position])
            holder.hideMoreImg()

        } else {
            if (position <= 4) {
                holder.binItems(item[position])
            }
            if (position == 4) {
                holder.showMoreImg()
            } else {
                holder.hideMoreImg()
            }
        }

        if (itemClick != null) {
            holder.itemView.setOnClickListener { v ->
                itemClick?.onClick(v, position)
            }
        }

    }

    override fun getItemCount(): Int {
        return item.size

    }

    fun showAllItems() {
        showAllItems = true
        notifyDataSetChanged()
    }
}