package com.appinsnap.aishrm.ui.fragments.latesitting.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.appinsnap.aishrm.databinding.LovoptionsBinding
import com.appinsnap.aishrm.databinding.SelectedOptionLayoutBinding
import com.appinsnap.aishrm.ui.fragments.latesitting.models.SelectedProjectData

class SelectedProjectAdapter(val context:Context,val list:ArrayList<SelectedProjectData>?=null,val onimgclick:onProjectCancel,val isItemCancelable:Boolean):RecyclerView.Adapter<SelectedProjectAdapter.ViewHolder>() {

    class ViewHolder(val binding:SelectedOptionLayoutBinding):RecyclerView.ViewHolder(binding.root) {

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding = SelectedOptionLayoutBinding.inflate(layoutInflater, parent, false)
        return ViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return list?.size ?: 0
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.binding.selectedtext.text = list?.get(position)?.name
        holder.binding.canceloptionimg.setOnClickListener {
            if(isItemCancelable)
            {
                list?.removeAt(position)
                notifyDataSetChanged()
                if (list != null) {
                    onimgclick.onSelectedProjectCancel(list)
                }
            }

            else{
        }

        }
    }

    interface onProjectCancel{

        fun onSelectedProjectCancel(list:ArrayList<SelectedProjectData>)
    }


}