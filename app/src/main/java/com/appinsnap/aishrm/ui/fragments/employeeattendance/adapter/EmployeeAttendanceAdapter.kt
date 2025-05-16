package com.appinsnap.aishrm.ui.fragments.employeeattendance.adapter

import android.content.Context
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.appinsnap.aishrm.R
import com.appinsnap.aishrm.databinding.EmployeeattendancestatusBinding
import com.appinsnap.aishrm.ui.fragments.employeeattendance.model.BodyX
import com.appinsnap.aishrm.util.MyHelperClass
import com.appinsnap.aishrm.util.clicklistener.setSafeOnClickListener
import java.util.*

class EmployeeAttendanceAdapter(
    val context: Context,
    val list: List<BodyX>,
    val onclick: onItemClick
) : RecyclerView.Adapter<EmployeeAttendanceAdapter.ViewHolder>() {
    class ViewHolder(val binding: EmployeeattendancestatusBinding) :
        RecyclerView.ViewHolder(binding.root) {
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): EmployeeAttendanceAdapter.ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding = EmployeeattendancestatusBinding.inflate(layoutInflater, parent, false)
        return ViewHolder(binding)

    }

    override fun onBindViewHolder(holder: EmployeeAttendanceAdapter.ViewHolder, position: Int) {
        val liststatus = list.get(position).status
        val listposition = list.get(position)
        if (liststatus) {
            if (listposition.value.contains("late")) {
                holder.binding.txtcheckouttimeemployee.visibility = View.VISIBLE
                holder.binding.txtcheckintimeemployee.visibility = View.VISIBLE
                if (listposition.lastCheckOut == null) {
                    holder.binding.txtcheckouttimeemployee.text = ""
                } else {
                    holder.binding.txtcheckouttimeemployee.text =
                        "Check-out" + " " + MyHelperClass.convertToAMPM_24to12(listposition.lastCheckOut)
                }
                if (listposition.firstCheckIn == null) {
                    holder.binding.txtcheckintimeemployee.text = ""
                } else {
                    holder.binding.txtcheckintimeemployee.text =
                        "Check-in" + " " + MyHelperClass.convertToAMPM_24to12(listposition.firstCheckIn)
                }
            }
            if (listposition.value.contains("client")) {
                holder.binding.txtcheckouttimeemployee.visibility = View.VISIBLE
                holder.binding.txtcheckintimeemployee.visibility = View.VISIBLE
                if (listposition.lastCheckOut == null) {
                    holder.binding.txtcheckouttimeemployee.text = ""
                } else {
                    holder.binding.txtcheckouttimeemployee.text =
                        "Check-out" + " " + MyHelperClass.convertToAMPM_24to12(listposition.lastCheckOut)
                }
                if (listposition.firstCheckIn == null) {
                    holder.binding.txtcheckintimeemployee.text = ""
                } else {
                    holder.binding.txtcheckintimeemployee.text =
                        "Check-in" + " " + MyHelperClass.convertToAMPM_24to12(listposition.firstCheckIn)
                }
            }
            if (listposition.value.contains("present")) {
                holder.binding.txtcheckouttimeemployee.visibility = View.VISIBLE
                holder.binding.txtcheckintimeemployee.visibility = View.VISIBLE
                if (listposition.lastCheckOut == null) {
                    holder.binding.txtcheckouttimeemployee.text = ""
                } else {
                    holder.binding.txtcheckouttimeemployee.text =
                        "Check-out" + " " + MyHelperClass.convertToAMPM_24to12(listposition.lastCheckOut)
                }
                if (listposition.firstCheckIn == null) {
                    holder.binding.txtcheckintimeemployee.text = ""
                } else {
                    holder.binding.txtcheckintimeemployee.text =
                        "Check-in" + " " + MyHelperClass.convertToAMPM_24to12(listposition.firstCheckIn)
                }
            }
            if (listposition.value.contains("work from home")) {
                holder.binding.txtcheckouttimeemployee.visibility = View.VISIBLE
                holder.binding.txtcheckintimeemployee.visibility = View.VISIBLE
                if (listposition.lastCheckOut == null) {
                    holder.binding.txtcheckouttimeemployee.text = ""
                } else {
                    holder.binding.txtcheckouttimeemployee.text =
                        "Check-out" + " " + MyHelperClass.convertToAMPM_24to12(listposition.lastCheckOut)
                }
                if (listposition.firstCheckIn == null) {
                    holder.binding.txtcheckintimeemployee.text = ""
                } else {
                    holder.binding.txtcheckintimeemployee.text =
                        "Check-in" + " " + MyHelperClass.convertToAMPM_24to12(listposition.firstCheckIn)
                }
            }
            if (listposition.value.contains("resubmission of attendance")) {
                holder.binding.txtcheckouttimeemployee.visibility = View.VISIBLE
                holder.binding.txtcheckintimeemployee.visibility = View.VISIBLE
                if (listposition.lastCheckOut == null) {
                    holder.binding.txtcheckouttimeemployee.text = ""
                } else {
                    holder.binding.txtcheckouttimeemployee.text =
                        "Check-out" + " " + MyHelperClass.convertToAMPM_24to12(listposition.lastCheckOut)
                }
                if (listposition.firstCheckIn == null) {
                    holder.binding.txtcheckintimeemployee.text = ""
                } else {
                    holder.binding.txtcheckintimeemployee.text =
                        "Check-in" + " " + MyHelperClass.convertToAMPM_24to12(listposition.firstCheckIn)
                }
            }
            if (listposition.value.contains("leave")) {
                holder.binding.txtcheckouttimeemployee.visibility = View.INVISIBLE
                holder.binding.txtcheckintimeemployee.visibility = View.INVISIBLE
                if (listposition.lastCheckOut == null) {
                    holder.binding.txtcheckouttimeemployee.text = ""
                } else {
                    holder.binding.txtcheckouttimeemployee.text =
                        "Check-out" + " " + MyHelperClass.convertToAMPM_24to12(listposition.lastCheckOut)
                }
                if (listposition.firstCheckIn == null) {
                    holder.binding.txtcheckintimeemployee.text = ""
                } else {
                    holder.binding.txtcheckintimeemployee.text =
                        "Check-in" + " " + MyHelperClass.convertToAMPM_24to12(listposition.firstCheckIn)
                }
            }
            if (listposition.value.contains("other")) {
                holder.binding.txtcheckouttimeemployee.visibility = View.VISIBLE
                holder.binding.txtcheckintimeemployee.visibility = View.VISIBLE
                if (listposition.lastCheckOut == null) {
                    holder.binding.txtcheckouttimeemployee.text = ""
                } else {
                    holder.binding.txtcheckouttimeemployee.text =
                        "Check-out" + " " + MyHelperClass.convertToAMPM_24to12(listposition.lastCheckOut)
                }
                if (listposition.firstCheckIn == null) {
                    holder.binding.txtcheckintimeemployee.text = ""
                } else {
                    holder.binding.txtcheckintimeemployee.text =
                        "Check-in" + " " + MyHelperClass.convertToAMPM_24to12(listposition.firstCheckIn)
                }
            }
            if (listposition.value.contains("holiday")) {
                holder.binding.txtcheckouttimeemployee.visibility = View.INVISIBLE
                holder.binding.txtcheckintimeemployee.visibility = View.INVISIBLE
            }
        }

        holder.itemView.setSafeOnClickListener {
            onclick.onitemclick(position)
        }
        val index = 0
        holder.binding.employeename.text = listposition.name

        if (listposition.remarks.lowercase().contains("absent")) {
            holder.binding.employattendancestatus.setTextColor(
                ContextCompat.getColor(
                    context,
                    R.color.absent_red
                )
            )
            holder.binding.employattendancestatus.text = listposition.remarks
            holder.binding.txtcheckouttimeemployee.visibility = View.GONE
            holder.binding.txtcheckintimeemployee.visibility = View.GONE
        } else if (listposition.remarks.lowercase().contains("present")) {
            listposition.status = true
            listposition.value = "client"
            holder.binding.txtcheckouttimeemployee.visibility = View.VISIBLE
            holder.binding.txtcheckintimeemployee.visibility = View.VISIBLE
            holder.binding.employattendancestatus.setTextColor(
                ContextCompat.getColor(
                    context,
                    R.color.present_green
                )
            )
            holder.binding.employattendancestatus.text = listposition.remarks
            if (listposition.lastCheckOut == null) {
                holder.binding.txtcheckouttimeemployee.text = ""
            } else {
                holder.binding.txtcheckouttimeemployee.text =
                    "Check-out" + " " + MyHelperClass.convertToAMPM_24to12(listposition.lastCheckOut)
            }
            if (listposition.firstCheckIn == null) {
                holder.binding.txtcheckintimeemployee.text = ""
            } else {
                holder.binding.txtcheckintimeemployee.text =
                    "Check-in" + " " + MyHelperClass.convertToAMPM_24to12(listposition.firstCheckIn)
            }

        } else if (listposition.remarks.lowercase().contains("late")) {
            listposition.status = true
            listposition.value = "late"
            holder.binding.txtcheckouttimeemployee.visibility = View.VISIBLE
            holder.binding.txtcheckintimeemployee.visibility = View.VISIBLE
            holder.binding.employattendancestatus.setTextColor(
                ContextCompat.getColor(
                    context,
                    R.color.latearrival_yellow
                )
            )
            holder.binding.employattendancestatus.text = listposition.remarks
            if (listposition.lastCheckOut == null) {
                holder.binding.txtcheckouttimeemployee.text = ""
            } else {
                holder.binding.txtcheckouttimeemployee.text =
                    "Check-out" + " " + MyHelperClass.convertToAMPM_24to12(listposition.lastCheckOut)
            }
            if (listposition.firstCheckIn == null) {
                holder.binding.txtcheckintimeemployee.text = ""
            } else {
                holder.binding.txtcheckintimeemployee.text =
                    "Check-in" + " " + MyHelperClass.convertToAMPM_24to12(listposition.firstCheckIn)
            }


        } else if (listposition.remarks.lowercase().contains("work from home")) {
            listposition.status = true
            listposition.value = "work from home"
            holder.binding.txtcheckouttimeemployee.visibility = View.VISIBLE
            holder.binding.txtcheckintimeemployee.visibility = View.VISIBLE
            holder.binding.employattendancestatus.setTextColor(
                ContextCompat.getColor(
                    context,
                    R.color.present_green
                )
            )
            holder.binding.employattendancestatus.text = listposition.remarks
            if (listposition.lastCheckOut == null) {
                holder.binding.txtcheckouttimeemployee.text = ""
            } else {
                holder.binding.txtcheckouttimeemployee.text =
                    "Check-out" + " " + MyHelperClass.convertToAMPM_24to12(listposition.lastCheckOut)
            }
            if (listposition.firstCheckIn == null) {
                holder.binding.txtcheckintimeemployee.text = ""
            } else {
                holder.binding.txtcheckintimeemployee.text =
                    "Check-in" + " " + MyHelperClass.convertToAMPM_24to12(listposition.firstCheckIn)
            }


        } else if (listposition.remarks.lowercase().contains("client")) {
            listposition.status = true
            listposition.value = "client"
            holder.binding.txtcheckouttimeemployee.visibility = View.VISIBLE
            holder.binding.txtcheckintimeemployee.visibility = View.VISIBLE
            holder.binding.employattendancestatus.setTextColor(
                ContextCompat.getColor(
                    context,
                    R.color.present_green
                )
            )


            holder.binding.employattendancestatus.text = listposition.remarks
            if (listposition.lastCheckOut == null) {
                holder.binding.txtcheckouttimeemployee.text = ""
            } else {
                holder.binding.txtcheckouttimeemployee.text =
                    "Check-out" + " " + MyHelperClass.convertToAMPM_24to12(listposition.lastCheckOut)
            }
            if (listposition.firstCheckIn == null) {
                holder.binding.txtcheckintimeemployee.text = "Check-in" + " " + "---"
            } else {
                holder.binding.txtcheckintimeemployee.text =
                    "Check-in" + " " + MyHelperClass.convertToAMPM_24to12(listposition.firstCheckIn)
            }
        } else if(listposition.remarks.lowercase().contains("leave")) {
            listposition.status = true
            holder.binding.txtcheckouttimeemployee.visibility = View.INVISIBLE
            holder.binding.txtcheckintimeemployee.visibility = View.INVISIBLE
            listposition.value = "leave"
            holder.binding.employattendancestatus.setTextColor(Color.parseColor("#0092c7"))
            holder.binding.employattendancestatus.text = listposition.remarks
            if (listposition.lastCheckOut == null) {
                holder.binding.txtcheckouttimeemployee.text = ""
            } else {
                holder.binding.txtcheckouttimeemployee.text =
                    "Check-out" + " " + MyHelperClass.convertToAMPM_24to12(listposition.lastCheckOut)
            }
            if (listposition.firstCheckIn == null) {
                holder.binding.txtcheckintimeemployee.text = ""
            } else {
                holder.binding.txtcheckintimeemployee.text =
                    "Check-in" + " " + MyHelperClass.convertToAMPM_24to12(listposition.firstCheckIn)
            }
        }
        else if(listposition.remarks.lowercase().contains("other")){
            listposition.status = true
            holder.binding.txtcheckouttimeemployee.visibility = View.VISIBLE
            holder.binding.txtcheckintimeemployee.visibility = View.VISIBLE
            listposition.value = "other"
            holder.binding.employattendancestatus.setTextColor(
                ContextCompat.getColor(
                    context,
                    R.color.present_green
                )
            )
            holder.binding.employattendancestatus.text = listposition.remarks
            if (listposition.lastCheckOut == null) {
                holder.binding.txtcheckouttimeemployee.text = ""
            } else {
                holder.binding.txtcheckouttimeemployee.text =
                    "Check-out" + " " + MyHelperClass.convertToAMPM_24to12(listposition.lastCheckOut)
            }
            if (listposition.firstCheckIn == null) {
                holder.binding.txtcheckintimeemployee.text = ""
            } else {
                holder.binding.txtcheckintimeemployee.text = "Check-in" + " " + MyHelperClass.convertToAMPM_24to12(listposition.firstCheckIn)
            }
        }
        else{
            listposition.status = true
            holder.binding.txtcheckouttimeemployee.visibility = View.INVISIBLE
            holder.binding.txtcheckintimeemployee.visibility = View.INVISIBLE
            listposition.value = "holiday"
            holder.binding.employattendancestatus.setTextColor(
                ContextCompat.getColor(
                    context,
                    R.color.white
                )
            )
            holder.binding.employattendancestatus.text = listposition.remarks
            holder.binding.txtcheckouttimeemployee.visibility = View.GONE
            holder.binding.txtcheckintimeemployee.visibility = View.GONE
        }

        if (listposition.designation.lowercase().contains(
                "manager") || listposition.designation.lowercase()
                .contains("team lead") || listposition.designation.lowercase()
                .contains("tl") || listposition.designation.lowercase().contains("lead")
        ) {
            if(listposition.designation.lowercase()
                .contains("team lead") || listposition.designation.lowercase()
                .contains("tl") || listposition.designation.lowercase().contains("lead"))
            {
                holder.binding.mcvemployeedesignation.visibility = View.VISIBLE
                holder.binding.txtdesignation.text = "Team Lead"
            }
            if(listposition.designation.toString().lowercase().contains("manager"))
            {
                holder.binding.mcvemployeedesignation.visibility = View.VISIBLE
                holder.binding.txtdesignation.text = "Manager"
            }
            if(listposition.designation.toString().lowercase()=="project manager")
            {
                holder.binding.mcvemployeedesignation.visibility = View.GONE
            }

        } else {
            holder.binding.mcvemployeedesignation.visibility = View.GONE
        }

        val employeename = listposition.name
        val firstletter = employeename.get(index)
        holder.binding.txtfirstletter.text = firstletter.toString()

        if (firstletter.toString().lowercase() == "a"
        ) {
            val color = Color.argb(200,25, 16, 190)
            holder.binding.mcvemployeename.setCardBackgroundColor(Color.parseColor("#fe2d55"))
        }
       else if ( firstletter.toString()
                .lowercase() == "c"
        ) {
            val color = Color.argb(200,250, 160, 230)
            holder.binding.mcvemployeename.setCardBackgroundColor(color)
        }
      else if ( firstletter.toString()
                .lowercase() == "e"
        ) {
            val color = Color.argb(200,250, 160, 19)
            holder.binding.mcvemployeename.setCardBackgroundColor(Color.parseColor("#FF018786"))
        }


        else
            if (firstletter.toString()
                    .lowercase().equals("p")
            ) {
                val color = Color.argb(255,225, 170, 230)
                holder.binding.mcvemployeename.setCardBackgroundColor(color)
            }

        else
            if (firstletter.toString().lowercase().equals("l")
            ) {
                val color = Color.argb(255,252, 107, 30)
                holder.binding.mcvemployeename.setCardBackgroundColor(color)


            }
            else
                if ( firstletter.toString()
                        .lowercase().equals("n")
                ) {
                    val color = Color.argb(255,25, 167, 230)
                    holder.binding.mcvemployeename.setCardBackgroundColor(Color.parseColor("#0C18F5"))
                }else
            if (firstletter.toString()
                    .lowercase().equals("t")
            ) {
                val color = Color.argb(255,149, 25, 171)
                holder.binding.mcvemployeename.setCardBackgroundColor(Color.parseColor("#0d9a41"))


            }
            else
                if (firstletter.toString()
                        .lowercase().equals("r")
                ) {
                    val color = Color.argb(255,149, 25, 171)
                    holder.binding.mcvemployeename.setCardBackgroundColor(Color.parseColor("#FF018786"))


                }else
            if ( firstletter.toString()
                    .lowercase() == "g" || firstletter.toString()
                    .lowercase() == "i" || firstletter.toString().lowercase() == "k"
            ) {
                val color = Color.argb(255,196, 180, 12)
                holder.binding.mcvemployeename.setCardBackgroundColor(color)


            } else
                if (firstletter.toString().lowercase().equals("v") || firstletter.toString()
                        .lowercase().equals("x")
                ) {
                    val color = Color.argb(255, 52, 53, 23)
                    holder.binding.mcvemployeename.setCardBackgroundColor(color)


                }
                else
                    if ( firstletter.toString()
                            .lowercase().equals("y") || firstletter.toString()
                            .lowercase().equals("z")
                    ) {
                        val color = Color.argb(255, 252, 153, 23)
                        holder.binding.mcvemployeename.setCardBackgroundColor(color)


                    }

                else
                    if (firstletter.toString().lowercase().equals("b") || firstletter.toString()
                            .lowercase().equals("d")
                    ) {
                        val color = Color.argb(255,187, 250, 18)
                        holder.binding.mcvemployeename.setCardBackgroundColor(Color.parseColor("#aaaaaa"))


                    }
                    else
                        if (firstletter.toString()
                                .lowercase().equals("f")
                        ) {
                            val color = Color.argb(255,250, 200, 180)
                            holder.binding.mcvemployeename.setCardBackgroundColor(Color.parseColor("#fd9500"))


                        }
                        else
                            if ( firstletter.toString()
                                    .lowercase().equals("h")
                            ) {
                                val color = Color.argb(255,25, 255, 255)
                                holder.binding.mcvemployeename.setCardBackgroundColor(Color.parseColor("#12AD2B"))


                            }

                    else
                        if (firstletter.toString().lowercase().equals("j")
                        ) {
                            val color = Color.argb(255, 35, 150, 199)
                            holder.binding.mcvemployeename.setCardBackgroundColor(color)


                        }
                        else
                            if (firstletter.toString()
                                    .lowercase().equals("l")
                            ) {
                                val color = Color.argb(255, 35, 150, 199)
                                holder.binding.mcvemployeename.setCardBackgroundColor(Color.parseColor(	"#5C4033"))


                            }
                        else
                            if (firstletter.toString()
                                    .lowercase().equals("m") || firstletter.toString()
                                    .lowercase().equals("o")
                            ) {
                                val color = Color.argb(255, 235, 100, 19)
                                holder.binding.mcvemployeename.setCardBackgroundColor(Color.parseColor("#af52de"))


                            }


                        else
                            if (firstletter.toString().lowercase()
                                    .equals("p")
                            ) {
                                val color = Color.argb(255,217, 156, 171)
                                holder.binding.mcvemployeename.setCardBackgroundColor(color)


                            }
                            else
                                if (firstletter.toString()
                                        .lowercase().equals("s") || firstletter.toString()
                                        .lowercase().equals("v")
                                ) {
                                    val color = Color.argb(255,178, 150, 250)
                                    holder.binding.mcvemployeename.setCardBackgroundColor(color)


                                }


                            else
                            if ( firstletter.toString().lowercase().equals("u")
                            )
                            {
                                val color = Color.argb(255,23, 250, 120)
                                holder.binding.mcvemployeename.setCardBackgroundColor(Color.parseColor("#ffcc01"))
                            }
                            else
                                if (firstletter.toString()
                                        .lowercase().equals("w")
                                )

                            {
                                val color = Color.argb(255,223, 255, 120)
                                holder.binding.mcvemployeename.setCardBackgroundColor(Color.parseColor("#a1835d"))


                            } else {
                                val color = Color.argb(255, 47, 230, 180)
                                holder.binding.mcvemployeename.setCardBackgroundColor(Color.parseColor("#FF5733"))

                            }
    }

    override fun getItemCount(): Int {
        return list.size
    }

    interface onItemClick {
        fun onitemclick(position: Int)
    }
}