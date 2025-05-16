package com.appinsnap.aishrm.ui.fragments.approvalnews.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.appinsnap.aishrm.databinding.NewsrecyclerviewitemBinding
import com.appinsnap.aishrm.ui.fragments.approvalnews.models.ThisDay

class TodayNewsAdapter(val requireContext: Context, val todaynewslist: List<ThisDay>) : RecyclerView.Adapter<TodayNewsAdapter.ViewHolder>() {
    class ViewHolder(val binding: NewsrecyclerviewitemBinding): RecyclerView.ViewHolder(binding.root) {

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding = NewsrecyclerviewitemBinding.inflate(layoutInflater, parent, false)

        return ViewHolder(binding)

    }




    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
holder.binding.tvStatus.text = todaynewslist.get(position).title
    }


    override fun getItemCount(): Int {
        return todaynewslist.size
    }









}