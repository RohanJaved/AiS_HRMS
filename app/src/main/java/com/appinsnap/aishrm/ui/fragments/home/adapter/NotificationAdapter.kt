package com.appinsnap.aishrm.ui.fragments.home.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.appinsnap.aishrm.R
import com.appinsnap.aishrm.databinding.ApprovedRejectRecyclerLayoutBinding
import com.appinsnap.aishrm.ui.fragments.inappnotification.models.ApprovalRequest
import com.appinsnap.aishrm.ui.fragments.inappnotification.models.Body
import com.appinsnap.aishrm.util.clicklistener.setSafeOnClickListener
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class NotificationAdapter(
    val context: Context,
    var list: List<Body>,
    val btnlistener: BtnClickListener
) : RecyclerView.Adapter<NotificationAdapter.ViewHolder>() {

    class ViewHolder(val binding: ApprovedRejectRecyclerLayoutBinding) :
        RecyclerView.ViewHolder(binding.root) {

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding = ApprovedRejectRecyclerLayoutBinding.inflate(layoutInflater, parent, false)
        return ViewHolder(binding)

    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        holder.binding.notificationdatetime.text = list.get(position).notificationDateTime

        if(list[position].type == "REQUEST_APPROVE"){
            holder.binding.imgApprove.visibility=View.VISIBLE
            holder.binding.imgReject.visibility=View.VISIBLE
        }else{
            holder.binding.imgApprove.visibility=View.GONE
            holder.binding.imgReject.visibility=View.GONE
        }
        holder.binding.imgReject.setImageResource(R.drawable.reject_unselected)
        holder.binding.imgApprove.setImageResource(R.drawable.tick_unselected)

        holder.binding.cardViewApprove.setSafeOnClickListener {

            holder.binding.imgApprove.setImageResource(R.drawable.tick_selected)
            holder.binding.imgReject.setImageResource(R.drawable.reject_unselected)

            holder.binding.cardViewApprove.isEnabled = true
            holder.binding.cardViewReject.isEnabled = true

            CoroutineScope(Dispatchers.Main).launch {
                btnlistener.onBtnClick(
                    true,
                    ApprovalRequest(
                        comment = "",
                        employeeID = list[position].employeeID,
                        notificationID = list[position].notificationID,
                        status = "A"
                    ),
                    position
                )

            }
        }

        holder.binding.cardViewReject.setSafeOnClickListener {

            holder.binding.imgReject.setImageResource(R.drawable.reject_selected)
            holder.binding.imgApprove.setImageResource(R.drawable.tick_unselected)

            holder.binding.cardViewApprove.isEnabled = true
            holder.binding.cardViewReject.isEnabled = true

            CoroutineScope(Dispatchers.Main).launch {
                notifyDataSetChanged()
                btnlistener.onBtnClick(
                    false,

                    ApprovalRequest(
                        comment = "",
                        employeeID = list[position].employeeID,
                        notificationID = list[position].notificationID,
                        status = "R",
                    ),
                    position,
                )
            }
        }
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getItemViewType(position: Int): Int {
        return position
    }



    interface BtnClickListener {
        suspend fun onBtnClick(type: Boolean, approvalRequest: ApprovalRequest, position: Int)
    }
}