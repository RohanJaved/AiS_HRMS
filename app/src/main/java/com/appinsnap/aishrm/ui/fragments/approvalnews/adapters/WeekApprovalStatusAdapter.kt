package com.appinsnap.aishrm.ui.fragments.approvalnews.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.appinsnap.aishrm.R
import com.appinsnap.aishrm.common.hide
import com.appinsnap.aishrm.common.show
import com.appinsnap.aishrm.databinding.NewnotificationstatusitemBinding
import com.appinsnap.aishrm.ui.fragments.approvalnews.models.AcceptRejectRequestModel
import com.appinsnap.aishrm.ui.fragments.approvalnews.models.WeekNotification
import com.appinsnap.aishrm.util.clicklistener.setSafeOnClickListener
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
class WeekApprovalStatusAdapter(
    var isMChecked:Boolean,
    val context: Context,
    var list: ArrayList<WeekNotification>,
    val btnlistener: BtnClickListener,
    val onclick: weekonClick,
    var isLongPressActive:Boolean,
    var showradioButton:Boolean) : RecyclerView.Adapter<WeekApprovalStatusAdapter.ViewHolder>() {
    private val selectedItems = ArrayList<Int>()
    private var longPressedItemPosition = RecyclerView.NO_POSITION
  inner  class ViewHolder(val binding: NewnotificationstatusitemBinding) :
        RecyclerView.ViewHolder(binding.root) {
      fun bind(item: WeekNotification) {
          if(item.typeid==6)
          {
              binding.tvStatus.hide()
              binding.attendance.hide()
              if(item.category!=null)
              {
                  binding.txtReqResub.show()
                  binding.txtReqResub.text = item.category
              }
              else{
                  binding.txtReqResub.hide()

              }
              if(item.name!=null)
              {
                  binding.txtName.show()
                  binding.txtName.text = item.name

              }
              else{
                  binding.txtName.hide()

              }
              if(item.checkin!=null)
              {
                  binding.checkinTime.show()
                  binding.txtcheckinTime.show()
                  binding.txtcheckinTime.text = item.checkin

              }
              else{
                  binding.checkinTime.hide()
                  binding.txtcheckinTime.hide()

              }
              if(item.fromDate!=null)
              {
                  binding.reqDate.show()
                  binding.txtreqDate.show()
                  binding.txtreqDate.text = item.fromDate
              }
              else{
                  binding.reqDate.hide()
                  binding.txtreqDate.hide()
              }
              if(item.checkout!=null)
              {
                  binding.checkoutTime.show()
                  binding.txtcheckoutTime.show()
                  binding.txtcheckoutTime.text = item.checkout
              }
              else{
                  binding.checkoutTime.hide()
                  binding.txtcheckoutTime.hide()
              }

              if(item.leaveType!=null)
              {
                  binding.txtleaveType.show()
                  binding.leaveType.show()
                  binding.txtleaveType.text = item.leaveType
              }
              else{
                  binding.txtleaveType.hide()
                  binding.leaveType.hide()
              }
              if(item.comments!=null)
              {
                  binding.reason.show()
                  binding.txtReason.show()
                  binding.txtReason.text = item.comments
              }
              else{
                  binding.reason.visibility = View.INVISIBLE
              }
              binding.notificationdatetimerequestresubmission.show()
              binding.notificationdatetimerequestresubmission.text=item.notificationDateTime
              binding.notificationdatetimesimplenoti.hide()
              binding.notificationdatetimesimple.hide()
          }
          else if(item.typeid == 10 ){
              binding.tvStatus.show()
              binding.attendance.show()
              binding.txtName.hide()
              binding.txtReqResub.hide()
              binding.checkinTime.hide()
              binding.txtcheckinTime.hide()
              binding.notificationdatetimerequestresubmission.hide()
              binding.checkoutTime.hide()
              binding.txtcheckoutTime.hide()
              binding.reqDate.hide()
              binding.txtreqDate.hide()
              binding.leaveType.hide()
              binding.txtleaveType.hide()
              binding.reason.visibility=View.INVISIBLE
              binding.txtReason.visibility=View.INVISIBLE
              binding.tvStatus.text = item.title
              val notificationText = item.message
              val substringToCheck = "Please review"
              if(substringToCheck in notificationText)
              {
                  val beforeSubstring = notificationText.substringBefore(substringToCheck)
                  val afterSubstring = notificationText.substringAfter(substringToCheck)
                  val finalafterSubString = afterSubstring.trimStart()
                  val formattedString = "$beforeSubstring$substringToCheck\n$finalafterSubString"
                  val finalformattedstring = formattedString.replace(": \"", "")
                  binding.attendance.text = finalformattedstring
              }
              else{
                  binding.attendance.text = item.message
              }
              binding.txtseeMoreLateSittingApproval.show()
              binding.notificationdatetimesimple.hide()
              binding.notificationdatetimesimplenoti.show()
              binding.notificationdatetimesimplenoti.text = item.notificationDateTime
          }
          else{
              binding.tvStatus.show()
              binding.attendance.show()
              binding.txtName.hide()
              binding.txtReqResub.hide()
              binding.checkinTime.hide()
              binding.txtcheckinTime.hide()
              binding.notificationdatetimerequestresubmission.hide()
              binding.checkoutTime.hide()
              binding.txtcheckoutTime.hide()
              binding.reqDate.hide()
              binding.txtreqDate.hide()
              binding.leaveType.hide()
              binding.txtleaveType.hide()
              binding.reason.visibility=View.INVISIBLE
              binding.txtReason.visibility=View.INVISIBLE
              binding.tvStatus.text = item.title
              val notificationText = item.message
              val substringToCheck = "Please review"
              if(substringToCheck in notificationText)
              {
                  val beforeSubstring = notificationText.substringBefore(substringToCheck)
                  val afterSubstring = notificationText.substringAfter(substringToCheck)
                  val finalafterSubString = afterSubstring.trimStart()
                  val formattedString = "$beforeSubstring$substringToCheck\n$finalafterSubString"
                  val finalformattedstring = formattedString.replace(": \"", "")
                  binding.attendance.text = finalformattedstring
              }
              else{
                  binding.attendance.text = item.message
              }
              binding.txtseeMoreLateSittingApproval.hide()
              binding.notificationdatetimesimplenoti.hide()
              binding.notificationdatetimesimple.show()
              binding.notificationdatetimesimplenoti.text = item.notificationDateTime
          }
              // Replace with the specific positions
              val position = adapterPosition
              if (isLongPressActive) {
                  binding.radiobutton.show()
              } else {
                  binding.radiobutton.hide()
              }
              binding.radiobutton.isChecked = selectedItems.contains(adapterPosition)
          }

  }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding = NewnotificationstatusitemBinding.inflate(layoutInflater, parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        if (showradioButton) {
                isLongPressActive = true
        }

        val item = list[position]
        holder.bind(item)

        if(item.attachment == null || item.attachment == ""){
            holder.binding.txtseeMore.hide()
        }else{
            holder.binding.txtseeMore.show()
        }


        holder.itemView.setOnLongClickListener {
            isLongPressActive = true
            if (isLongPressActive) {
                onclick.weekonLongItemclick(selectedItems, true)
            } else {
                onclick.weekonLongItemclick(selectedItems, false)
            }
                longPressedItemPosition = position
                notifyDataSetChanged()
                true
        }

        holder.binding.txtseeMore.setOnClickListener {
            onclick.WeekonItemDetailClick(position)
        }

        holder.binding.imgReject.setImageResource(R.drawable.reject_unselected)
        holder.binding.imgApprove.setImageResource(R.drawable.tick_unselected)

        holder.binding.cardViewApprove.setSafeOnClickListener {

            holder.binding.imgApprove.setImageResource(R.drawable.tick_selected)
            holder.binding.imgReject.setImageResource(R.drawable.reject_unselected)

            holder.binding.cardViewApprove.isEnabled = true
            holder.binding.cardViewReject.isEnabled = true

            CoroutineScope(Dispatchers.Main).launch {
                btnlistener.weekonBtnClick(
                    "Accept",
                    AcceptRejectRequestModel(
                        noticationIDs = list[position].notificationID.toString(),
                        comment = "",
                        status = "A",
                    ),
                    position,
                    false
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
                btnlistener.weekonBtnClick(
                    "Reject",
                    AcceptRejectRequestModel(
                        noticationIDs = list[position].notificationID.toString(),
                        comment = "",
                        status = "R",
                    ),
                    position,
                    false,
                )
            }
        }
        holder.binding.txtseeMoreLateSittingApproval.setOnClickListener {
            holder.binding.txtseeMoreLateSittingApproval.hide()
            holder.binding.change.show()
            holder.binding.approve.show()
        }
        holder.binding.approve.setOnClickListener {
            CoroutineScope(Dispatchers.Main).launch {
                btnlistener.weekonBtnClick(
                    "Accept",
                    AcceptRejectRequestModel(
                        noticationIDs = list[position].notificationID.toString(),
                        comment = "",
                        status = "A",
                    ),
                    position,
                    false
                )
            }

        }
        holder.binding.change.setOnClickListener {
            CoroutineScope(Dispatchers.Main).launch {
                btnlistener.weekonBtnClick(
                    "Accept",
                    AcceptRejectRequestModel(
                        noticationIDs = list[position].notificationID.toString(),
                        comment = "",
                        status = "A",
                    ),
                    position,
                    true
                )
            }

        }

        holder.itemView.setOnClickListener {
            if (isLongPressActive)
            {
                holder.binding.radiobutton.isChecked = !holder.binding.radiobutton.isChecked
                if (holder.binding.radiobutton.isChecked)
                {
                    selectedItems.add(list.get(position).notificationID)
                }else
                {
                    selectedItems.remove(list.get(position).notificationID)

                }
                onclick.weekonitemclick(selectedItems)
            }
        }
        holder.binding.radiobutton.isClickable = false
        if (isMChecked)
        {
            holder.binding.radiobutton.isChecked =true
            selectedItems.add(list.get(position).notificationID)
            if (position==list.size-1)
            {
                onclick.weekonitemclick(selectedItems)

            }
        }
        else{
            selectedItems.clear()
            onclick.weekonitemclick(selectedItems)
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
        suspend fun weekonBtnClick(
            text: String,
            approvalRequest: AcceptRejectRequestModel,
            position: Int,
            b: Boolean
        )
    }
    interface weekonClick{
        fun weekonLongItemclick(list: ArrayList<Int>?, value:Boolean)
        fun weekonitemclick(list: ArrayList<Int>?)

        fun WeekonItemDetailClick(position:Int)
    }
    fun filterList(filterList: ArrayList<WeekNotification>) {
        list = filterList
        notifyDataSetChanged()
    }
}