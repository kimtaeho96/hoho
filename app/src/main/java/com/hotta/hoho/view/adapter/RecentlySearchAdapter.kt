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
import androidx.activity.viewModels
import androidx.recyclerview.widget.RecyclerView
import com.hotta.hoho.R
import com.hotta.hoho.db.entity.SearchListEntity
import com.hotta.hoho.view.search.SearchActivity
import com.hotta.hoho.view.search.SearchListActivity
import com.hotta.hoho.view.search.SearchViewModel

class RecentlySearchAdapter(
    val context: Context,
    val item: List<SearchListEntity>,
    viewModel: SearchViewModel
) :
    RecyclerView.Adapter<RecentlySearchAdapter.ViewHolder>() {

    val viewModel = viewModel
    val TAG = "!!@@" + RecentlySearchAdapter::class.java.simpleName

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        fun bind(item: SearchListEntity) {
            val textTv = itemView.findViewById<TextView>(R.id.searchName)
            val dataTv = itemView.findViewById<TextView>(R.id.searchData)
            val deleteBtn = itemView.findViewById<ImageView>(R.id.delete_btn)


            textTv.setText(item.searchName)
            dataTv.setText(item.data)

            textTv.setOnClickListener {
                    val intent = Intent(context, SearchListActivity::class.java)
                    intent.putExtra("search", item.searchName.toString())
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                    context.startActivity(intent)
            }
            deleteBtn.setOnClickListener {
                viewModel.getSelectDeleteData(item.searchName)
                notifyDataSetChanged()
            }


        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.recently_search_item, parent, false)

        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return item.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(item[position])
    }

}