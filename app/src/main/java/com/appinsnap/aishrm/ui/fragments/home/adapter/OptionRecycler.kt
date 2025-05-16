package com.appinsnap.aishrm.ui.fragments.home.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.appinsnap.aishrm.R
import com.appinsnap.aishrm.databinding.LovoptionsBinding
import com.appinsnap.aishrm.ui.fragments.home.models.filtermodels.Lov

class OptionRecycler(
    val context: Context,
    val optionlist: List<Lov>,
    val onclick:(position: Int, st: String, check:Boolean) -> Unit
) : RecyclerView.Adapter<OptionRecycler.ViewHolder>() {
    class ViewHolder(val binding: LovoptionsBinding) :
        RecyclerView.ViewHolder(binding.root) {

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding = LovoptionsBinding.inflate(layoutInflater, parent, false)
        return ViewHolder(
            binding
        )
    }

    override fun getItemCount(): Int {
        return optionlist.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.binding.checkboxtext.text = optionlist.get(position).name
        val item = optionlist.get(position)
        if(item.check)
            holder.binding.checkboximage.setImageDrawable(context.getDrawable(R.drawable.filled_checkbox))
        else
            holder.binding.checkboximage.setImageDrawable(context.getDrawable(R.drawable.empty_checkbox))
//        holder.binding.checkboximage.setImageResource = item.check
        holder.binding.opotionitemlayout.setOnClickListener {
            if(!item.check)
                holder.binding.checkboximage.setImageDrawable(context.getDrawable(R.drawable.filled_checkbox))
            else
                holder.binding.checkboximage.setImageDrawable(context.getDrawable(R.drawable.empty_checkbox))
            onclick(position, optionlist[position].name,!item.check)
        }
        holder.binding.checkboximage.setOnClickListener {
            if(!item.check)
                holder.binding.checkboximage.setImageDrawable(context.getDrawable(R.drawable.filled_checkbox))
            else
                holder.binding.checkboximage.setImageDrawable(context.getDrawable(R.drawable.empty_checkbox))

            onclick(position, optionlist[position].name,!item.check)
        }
    }

}