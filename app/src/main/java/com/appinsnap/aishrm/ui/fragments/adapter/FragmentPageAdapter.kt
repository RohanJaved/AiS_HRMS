package com.appinsnap.aishrm.ui.fragments.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.appinsnap.aishrm.ui.fragments.approval.approval.Approval
import com.appinsnap.aishrm.ui.fragments.approval.pending.Pending
import com.appinsnap.aishrm.ui.fragments.approval.rejected.Rejected
import com.appinsnap.aishrm.ui.fragments.history.models.Body
import com.appinsnap.aishrm.ui.fragments.history.models.StausList

class FragmentPageAdapter(
    fragmentManager: FragmentManager,
    lifecycle: Lifecycle,
    val allnotificationstatuslist: List<Body>,
    val notificationlist:notificationlistdata
):
    FragmentStateAdapter(fragmentManager,lifecycle) {
    override fun getItemCount(): Int {
        return 3
    }

    override fun createFragment(position: Int): Fragment {
         if(position==0)
        {

          val pendingfragment =   Pending(allnotificationstatuslist)
            return pendingfragment
        }
        else if(position == 1)
        {


         val rejectedfragment =    Rejected(allnotificationstatuslist)
            return rejectedfragment
        }
        else{
          val approvalfragment =   Approval(allnotificationstatuslist)
             return approvalfragment

        }

    }
    interface notificationlistdata{
        fun notificationstatuslist(allnotificationstatuslist: ArrayList<StausList>)
    }
}