package com.appinsnap.aishrm.ui.fragments.history.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.appinsnap.aishrm.databinding.HistorydaterecyclerviewitemBinding
import com.appinsnap.aishrm.databinding.NotificationstatusitemBinding
import com.appinsnap.aishrm.ui.fragments.history.models.StausList

class HistoryDateAdapter(val context: Context, val datelist: ArrayList<String>?,val ondateclick:onDateClick): RecyclerView.Adapter<HistoryDateAdapter.ViewHolder>() {
    class ViewHolder(val binding: HistorydaterecyclerviewitemBinding): RecyclerView.ViewHolder(binding.root) {

    }



    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding = HistorydaterecyclerviewitemBinding.inflate(layoutInflater, parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.binding.itemlayout.setOnClickListener {
            ondateclick.ondateitemclick(position)
        }
        holder.binding.txtdate.text = datelist?.get(position)
    }


    override fun getItemCount(): Int {
        return datelist!!.size
    }

    interface onDateClick{
        fun ondateitemclick(position:Int)
    }
}