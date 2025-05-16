package com.appinsnap.aishrm.ui.fragments.Policies.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.appinsnap.aishrm.common.hide
import com.appinsnap.aishrm.common.show
import com.appinsnap.aishrm.databinding.PoliciesitemlayoutBinding
import com.appinsnap.aishrm.ui.fragments.Policies.models.GetSOPresponse
import com.appinsnap.aishrm.ui.fragments.Policies.models.SopAttachmentlist

class SOPAdapter(
    val context: Context,
    var list:ArrayList<GetSOPresponse>,
    var onItemClick:onSOPClick
): RecyclerView.Adapter<SOPAdapter.ViewHolder>() {
    class ViewHolder(val binding: PoliciesitemlayoutBinding): RecyclerView.ViewHolder(binding.root) {

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding = PoliciesitemlayoutBinding.inflate(layoutInflater, parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        if(list.get(position).name.isNullOrEmpty())
        {
            holder.binding.txtpolicies.hide()
        }
        else{
            holder.binding.txtpolicies.show()
            holder.binding.txtpolicies.text = list.get(position).name
        }
        val adapter = InnerSOPAdapter(context,
            list.get(position).sopAttachmentlist as ArrayList<SopAttachmentlist>
        )
        {url,name ->
            onItemClick.onItemSOPClick(url,name)
        }
        holder.binding.pdfRecyclerview.adapter = adapter
    }


    override fun getItemCount(): Int {
        return list.size
    }
    interface onSOPClick{
       fun onItemSOPClick(url:String,name:String)
    }

}