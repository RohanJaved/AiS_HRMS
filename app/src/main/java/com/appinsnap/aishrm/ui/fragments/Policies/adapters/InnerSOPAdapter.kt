package com.appinsnap.aishrm.ui.fragments.Policies.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.appinsnap.aishrm.databinding.FilesitemlayoutBinding
import com.appinsnap.aishrm.ui.fragments.Policies.models.Attachmentlist
import com.appinsnap.aishrm.ui.fragments.Policies.models.SopAttachmentlist

class InnerSOPAdapter(val context: Context, val list:ArrayList<SopAttachmentlist>, val onSOPClick:(Url:String, name:String)->Unit):
    RecyclerView.Adapter<InnerSOPAdapter.ViewHolder>(){
    class ViewHolder(val binding: FilesitemlayoutBinding): RecyclerView.ViewHolder(binding.root) {

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding = FilesitemlayoutBinding.inflate(layoutInflater, parent, false)
        return InnerSOPAdapter.ViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.binding.txtpolicies.text = list.get(position).title
        holder.itemView.setOnClickListener{
            onSOPClick(list.get(position).attachments,list.get(position).title)
        }
    }

}