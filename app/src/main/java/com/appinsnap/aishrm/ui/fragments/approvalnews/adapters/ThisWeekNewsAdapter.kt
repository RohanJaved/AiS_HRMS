package com.appinsnap.aishrm.ui.fragments.approvalnews.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.appinsnap.aishrm.databinding.NewsrecyclerviewitemBinding
import com.appinsnap.aishrm.ui.fragments.approvalnews.models.ThisWeek

class ThisWeekNewsAdapter(val requireContext: Context, val thisweeknewslist: List<ThisWeek>) : RecyclerView.Adapter<ThisWeekNewsAdapter.ViewHolder>() {
    class ViewHolder(val binding: NewsrecyclerviewitemBinding): RecyclerView.ViewHolder(binding.root) {

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding = NewsrecyclerviewitemBinding.inflate(layoutInflater, parent, false)

        return ViewHolder(binding)

    }




    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        holder.binding.tvStatus.text = thisweeknewslist.get(position).title
    }


    override fun getItemCount(): Int {
        return thisweeknewslist.size
    }









}