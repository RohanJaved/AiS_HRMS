package com.appinsnap.aishrm.ui.fragments.Policies.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.appinsnap.aishrm.common.hide
import com.appinsnap.aishrm.common.show
import com.appinsnap.aishrm.databinding.PoliciesitemlayoutBinding
import com.appinsnap.aishrm.ui.fragments.Policies.models.Attachmentlist
import com.appinsnap.aishrm.ui.fragments.Policies.models.GetPoliciesresponse

class PolicyAdapter(
    val context: Context,
    var list:ArrayList<GetPoliciesresponse>,
    var onItemClick:onPolicyClick
): RecyclerView.Adapter<PolicyAdapter.ViewHolder>() {
    var itemPosition:Int = -1
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

        val adapter = InnerPolicyAdapter(context,
            list.get(position).attachmentlist as ArrayList<Attachmentlist>
        )
        {url,name ->
            onItemClick.onItemPolicyClick(url,name)
        }
        holder.binding.pdfRecyclerview.adapter = adapter
    }


    override fun getItemCount(): Int {
        return list.size
    }
    interface onPolicyClick{
        fun onItemPolicyClick(url : String,name:String)
    }

    }