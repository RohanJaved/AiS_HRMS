package com.appinsnap.aishrm.ui.fragments.EmployeeHistory.adapter
import android.content.Context
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.TextView
import com.appinsnap.aishrm.R
import com.appinsnap.aishrm.ui.fragments.home.models.BodyXXX
import com.appinsnap.aishrm.util.MyHelperClass
import com.appinsnap.aishrm.util.SessionManagement
import com.appinsnap.aishrm.util.clicklistener.setSafeOnClickListener


class CalendarAdapter(
    private val context: Context,
    private val monthdayonePosition: Int,
    private val days: ArrayList<Int>,
    private val responselist: BodyXXX?,
    private val onCalendarClick: onCalendarDate,
    private val empID: Int,
    private val month: String,
    private val year: String
) :
    BaseAdapter() {
    var adapterposition = -1
    var selectedPosition = -1
    var lastSelectedPosition = -1
    var phSelectedposition=-1
    private val inflater: LayoutInflater = LayoutInflater.from(context)

    override fun getCount(): Int {
        return days.size
    }

    override fun getItem(position: Int): Any {
        return days[position]
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        adapterposition = position
        var view = convertView
        if (view == null) {
            view = inflater.inflate(R.layout.customcalendaritem, parent, false)
        }

        val dayTextView = view?.findViewById<TextView>(R.id.dayTextView)
//        if ((position + 1) % 7 == 1) {
//            dayTextView?.setTextColor(Color.parseColor("#666666"))
//        }
//        if ((position + 1) % 7 == 0) {
//            dayTextView?.setTextColor(Color.parseColor("#666666"))
//        }

        var monthTemp = ""
        if (month.toInt() > 9) {
            monthTemp = month
        } else {
            monthTemp = "0" + month
        }

        if (responselist != null) {
            var response = responselist.weeklyAttendanceresponce
            for (i in response) {
                if(i.date!=null){
                    if (i.date.toInt() == days[position]) {
                        markingAttendanceColor(i.remarks, dayTextView)
                    if (MyHelperClass.getCurrentDateYYYYMMDD().split("/")[1] == (monthTemp).toString()) {
                        val crday = MyHelperClass.getCurrentDateYYYYMMDD().split("/")[2]
                        if ((position + 1) >= crday.toInt() + monthdayonePosition) {
                            if (i.remarks.lowercase() == "l") {
                                dayTextView?.setTextColor(Color.parseColor("#00a3e5"))
                            }
                            else if(i.remarks.lowercase().contains("p.h"))
                            {
                                dayTextView?.setTextColor(Color.parseColor("#d4d4d4"))
                            }
                            else {
                                dayTextView?.setTextColor(Color.parseColor("#666666"))
                            }
                        }
                        break
                    }
                }
                }
            }
        }
        dayTextView?.setSafeOnClickListener {

            var employeeid = SessionManagement(context)?.getEmpId()?.toInt()
            if (employeeid == empID) {
                val crday = MyHelperClass.getCurrentDateYYYYMMDD().split("/")[2]
                val currday = crday.toInt() - 1
                if (MyHelperClass.getCurrentDateYYYYMMDD()
                        .split("/")[1] == (monthTemp).toString()
                ) {
                    if (days[position] != 0 && (position + 1) < currday.toInt() + monthdayonePosition) {

                        lastSelectedPosition = selectedPosition;
                        selectedPosition = position;
                        /*notifyItemChanged(lastSelectedPosition);
                        notifyItemChanged(selectedPosition);*/
                        notifyDataSetChanged()



                        onCalendarClick.onDateClick(
                            position,
                            month + "/" + days[position] + "/" + year, true
                        )


                    } else if (days[position] != 0 && (position + 1) < crday.toInt() + monthdayonePosition) {


                        lastSelectedPosition = selectedPosition;
                        selectedPosition = position;
                        /*notifyItemChanged(lastSelectedPosition);
                        notifyItemChanged(selectedPosition);*/
                        notifyDataSetChanged()
                        onCalendarClick.onDateClick(
                            position,
                            month + "/" + days[position] + "/" + year, false
                        )
                    }

                } else {
                    if (days[position] != 0) {

                        lastSelectedPosition = selectedPosition;
                        selectedPosition = position;
                        /*notifyItemChanged(lastSelectedPosition);
                        notifyItemChanged(selectedPosition);*/
                        notifyDataSetChanged()
                        onCalendarClick.onDateClick(
                            position,
                            month + "/" + days[position] + "/" + year,
                            true
                        )
                    }
                }
            } else {
                val crday = MyHelperClass.getCurrentDateYYYYMMDD().split("/")[2]
                val currday = crday.toInt() - 1
                if (MyHelperClass.getCurrentDateYYYYMMDD()
                        .split("/")[1] == (monthTemp).toString()
                ) {
                    if (days[position] != 0 && (position + 1) < currday + monthdayonePosition) {

                        lastSelectedPosition = selectedPosition;
                        selectedPosition = position;
                        /*notifyItemChanged(lastSelectedPosition);
                        notifyItemChanged(selectedPosition);*/
                        notifyDataSetChanged()
                        onCalendarClick.onDateClick(
                            position,
                            month + "/" + days[position] + "/" + year, false
                        )
                    } else if (days[position] != 0 && (position + 1) < crday.toInt() + monthdayonePosition) {


                        lastSelectedPosition = selectedPosition;
                        selectedPosition = position;

                        notifyDataSetChanged()
                        onCalendarClick.onDateClick(
                            position,
                            month + "/" + days[position] + "/" + year, false
                        )
                    }

                } else {
                    if (days[position] != 0) {

                        lastSelectedPosition = selectedPosition;
                        selectedPosition = position;
                        /*notifyItemChanged(lastSelectedPosition);
                        notifyItemChanged(selectedPosition);*/

                        notifyDataSetChanged()
                        onCalendarClick.onDateClick(
                            position,
                            month + "/" + days[position] + "/" + year,
                            false)
                    }
                }
            }


        }


        if (selectedPosition == position) {
            dayTextView?.setBackgroundResource(R.drawable.selecteddatebg)
        }

        else {
            dayTextView?.setBackgroundResource(R.drawable.notificationcountbackground_trans)
            }

            if (days[position] != 0) {
                dayTextView?.text = days[position].toString()
            }
            return view!!

        }
    private fun markingAttendanceColor(remarks: String, dayTextView: TextView?) {
        if(remarks!=null)
        {
            when (remarks.lowercase()) {
                "p" -> {
                    dayTextView?.setTextColor(Color.parseColor("#0D894F"))
                }
                "a" -> {
                    dayTextView?.setTextColor(Color.parseColor("#F04438"))
                }
                "l" -> {
                    dayTextView?.setTextColor(Color.parseColor("#1FB6FF"))
                }
                "la" -> {
                    dayTextView?.setTextColor(Color.parseColor("#F7A91D"))
                }
                "p.h" ->{
                    dayTextView?.setTextColor(Color.parseColor("#d4d4d4"))
                }
                else->
                {
                    dayTextView?.setTextColor(Color.parseColor("#666666"))
                }
            }
        }

    }

    interface onCalendarDate {
        fun onDateClick(position: Int, datefromadapter: String,onDateClick: Boolean)
    }
}
