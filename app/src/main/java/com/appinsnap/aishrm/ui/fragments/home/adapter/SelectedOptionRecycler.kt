package com.appinsnap.aishrm.ui.fragments.home.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.appinsnap.aishrm.R
import com.appinsnap.aishrm.databinding.LovoptionsBinding
import com.appinsnap.aishrm.databinding.SelectedOptionLayoutBinding
import com.appinsnap.aishrm.ui.fragments.home.models.filtermodels.Lov

class SelectedOptionRecycler(
    val context: Context,
    val optionlist: ArrayList<String>,
    val onclick: (value: ArrayList<String>) -> Unit
) : RecyclerView.Adapter<SelectedOptionRecycler.ViewHolder>() {
    class ViewHolder(val binding: SelectedOptionLayoutBinding) :
        RecyclerView.ViewHolder(binding.root) {

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding = SelectedOptionLayoutBinding.inflate(layoutInflater, parent, false)
        return ViewHolder(
            binding
        )
    }

    override fun getItemCount(): Int {
        return optionlist.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.binding.selectedtext.text = optionlist[position]

        holder.binding.canceloptionimg.setOnClickListener {
            optionlist.removeAt(position)
            notifyDataSetChanged()
            onclick(optionlist)
        }
    }

}