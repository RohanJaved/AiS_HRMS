package com.appinsnap.aishrm.ui.fragments.history.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.appinsnap.aishrm.R
import com.appinsnap.aishrm.common.hide
import com.appinsnap.aishrm.common.show
import com.appinsnap.aishrm.databinding.NotificationstatusitemBinding
import com.appinsnap.aishrm.ui.fragments.approvalnews.models.WeekNotification
import com.appinsnap.aishrm.ui.fragments.history.models.Body
import com.appinsnap.aishrm.util.clicklistener.setSafeOnClickListener

class NotificationStatusAdapter(val context: Context, var notificationlist: ArrayList<Body>?, val requestitemclick:onRequestClick): RecyclerView.Adapter<NotificationStatusAdapter.ViewHolder>() {
    var lastselectedposition:Int=-1

  inner  class ViewHolder(val binding: NotificationstatusitemBinding): RecyclerView.ViewHolder(binding.root) {
        fun bind(item:Body) {
            if(item.typeid==6)
            {
                binding.tvStatus.hide()
                binding.attendance.hide()
                if(item.category.isNotEmpty())
                {
                    binding.txtReqResub.show()
                    binding.txtReqResub.text = item.category
                }
                else{
                    binding.txtReqResub.hide()
                }
                if(item.name.isNotEmpty())
                {
                    binding.txtName.show()
                    binding.txtName.text = item.name
                }
                else{
                    binding.txtName.hide()

                }
                if(item.checkin.isNotEmpty())
                {
                    binding.checkinTime.show()
                    binding.txtcheckinTime.show()
                    binding.txtcheckinTime.text = item.checkin
                }
                else{
                    binding.checkinTime.hide()
                    binding.txtcheckinTime.hide()
                }
                if(item.checkout.isNotEmpty())
                {
                    binding.checkoutTime.show()
                    binding.txtcheckoutTime.show()
                    binding.txtcheckoutTime.text = item.checkout
                }
                else{
                    binding.checkoutTime.hide()
                    binding.txtcheckoutTime.hide()
                }

                if(item.leaveType.isNotEmpty())
                {
                    binding.txtleaveType.show()
                    binding.leaveType.show()
                    binding.txtleaveType.text = item.leaveType
                }
                else{
                    binding.txtleaveType.hide()
                    binding.leaveType.hide()
                }
                if(item.fromDate.isNotEmpty())
                {
                    binding.reqDate.show()
                    binding.txtreqDate.show()
                    binding.txtreqDate.text = item.fromDate
                }
                else{
                    binding.reqDate.hide()
                    binding.txtreqDate.hide()
                }

                if(item.comments.isNotEmpty())
                {
                    binding.reason.show()
                    binding.txtReason.text = item.comments
                }
                else{
                    binding.txtReason.visibility = View.INVISIBLE
                    binding.reason.visibility = View.INVISIBLE
                }
                binding.txtnotificationdatetimereqresub.show()
                binding.txtnotificationdatetimereqresub.text = item.notificationDateTime
                binding.txtnotificationdatetimesimple.hide()
            }
            else{
                binding.tvStatus.show()
                binding.attendance.show()
                binding.txtName.hide()
                binding.txtReqResub.hide()
                binding.checkinTime.hide()
                binding.txtcheckinTime.hide()
                binding.checkoutTime.hide()
                binding.txtcheckoutTime.hide()
                binding.reqDate.hide()
                binding.txtreqDate.hide()
                binding.leaveType.hide()
                binding.txtleaveType.hide()
                binding.reason.visibility = View.INVISIBLE
                binding.txtReason.visibility = View.INVISIBLE
                binding.tvStatus.text = item.title
                val notificationText = item.message
                val substringToCheck = "Please review"
                if(substringToCheck in notificationText)
                {
                    val beforeSubstring = notificationText.substringBefore(substringToCheck)
                    val afterSubstring = notificationText.substringAfter(substringToCheck)
                    val finalafterSubString = afterSubstring.trimStart()
                    val formattedString = "$beforeSubstring$substringToCheck\n$finalafterSubString"
                    val finalformattedstring = formattedString.replace(": \"", "" )
                    binding.attendance.text = finalformattedstring
                }
                else{
                    binding.attendance.text = item.message
                }
                binding.txtnotificationdatetimesimple.show()
                binding.txtnotificationdatetimereqresub.hide()
                binding.txtnotificationdatetimesimple.text = item.notificationDateTime

            } // Replace with the specific positions
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding = NotificationstatusitemBinding.inflate(layoutInflater, parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        if(notificationlist?.get(position)?.status.toString().lowercase().contains("pending"))///||notificationlist?.get(position)?.status.toString().lowercase().contains("accept")
        {
            holder.binding.imgCancel.visibility = View.VISIBLE
        }
        else{
            holder.binding.imgCancel.visibility = View.GONE
        }

        holder.binding.imgCancel.setSafeOnClickListener {
            lastselectedposition = position // Update the selected position
//            selectedImageHeight = 50
//            selectedImageWidth = 50
//            settingSelectedImageSize(holder)
            holder.binding.imgCancel.setImageResource(R.drawable.reject_selected)
            requestitemclick.onRequestItemClick(position,notificationlist?.get(position)?.notificationID.toString())
        }


        if (lastselectedposition == position) {
//            selectedImageHeight = 33
//            selectedImageWidth = 33
//            settingSelectedImageSize(holder)
            holder.binding.imgCancel.setImageResource(R.drawable.reject_unselected)
        }

        val item = notificationlist?.get(position)
        if (item != null) {
            holder.bind(item)
        }

    }
   /* fun settingSelectedImageSize(holder: ViewHolder)
    {
        val newWidth = selectedImageWidth// in pixels
        val newHeight = selectedImageHeight // in pixels

        val layoutParams = holder.binding.imgCancel.layoutParams
        layoutParams.width = newWidth
        layoutParams.height = newHeight
        holder.binding.imgCancel.layoutParams = layoutParams
    }
*/

    override fun getItemCount(): Int {
        return notificationlist!!.size
    }

    interface onRequestClick{
        fun onRequestItemClick(position: Int,notificationid:String)
    }
}