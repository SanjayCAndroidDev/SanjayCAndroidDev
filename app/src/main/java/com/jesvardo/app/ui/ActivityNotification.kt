package com.jesvardo.app.ui

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.jesvardo.app.R
import com.jesvardo.app.base.BaseActivity
import com.jesvardo.app.databinding.ActivityChatBinding
import com.jesvardo.app.databinding.ActivityDashboardBinding
import com.jesvardo.app.databinding.ActivityNotificationBinding
import com.jesvardo.app.ui.viewmodels.DashboardViewModel
import com.jesvardo.app.utils.listeners.setSafeOnClickListener
import kotlinx.android.synthetic.main.base_activity.*

class ActivityNotification : BaseActivity() {

    private lateinit var activityNotificationBinding: ActivityNotificationBinding

    private lateinit var notificationAdapter: NotificationAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        activityNotificationBinding =  putContentView(R.layout.activity_notification) as ActivityNotificationBinding

        initToolbar()
        base_activity_toolbar.visibility = View.GONE

        initToolbar()
        base_activity_toolbar.visibility = View.VISIBLE
        base_activity_toolbar_textview_title.text = resources.getString(R.string.text_title_notifications)
        base_activity_toolbar_imageview_back.visibility = View.VISIBLE
        base_activity_toolbar_imageview_right.visibility = View.VISIBLE

        base_activity_toolbar_imageview_right.setImageResource(R.drawable.menu)

        base_activity_toolbar_imageview_back.setSafeOnClickListener {
            onBackPressed()
        }

        var mLayoutManager = LinearLayoutManager(applicationContext)
        mLayoutManager.orientation = LinearLayoutManager.VERTICAL
        notificationAdapter = NotificationAdapter(this@ActivityNotification)
        activityNotificationBinding.activityNotificationRecycler.layoutManager = mLayoutManager
        activityNotificationBinding.activityNotificationRecycler.adapter = notificationAdapter

    }


    class NotificationAdapter(val mContext: ActivityNotification) : RecyclerView.Adapter<NotificationAdapter.MyViewHolder>() {

        class MyViewHolder(view: View) : RecyclerView.ViewHolder(view) {
            val textViewName: TextView = view.findViewById<TextView>(R.id.raw_notification_list_txt_notification)
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
            val itemView = LayoutInflater.from(parent.context)
                .inflate(R.layout.raw_notification_list, parent, false)
            return NotificationAdapter.MyViewHolder(itemView)
        }

        override fun getItemCount(): Int {
            return 12
        }

        override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
            holder.itemView.setSafeOnClickListener {

            }

        }
    }

    @SuppressLint("RestrictedApi")
    override fun onResume() {
        super.onResume()
    }


    override fun onBackPressed() {
        super.onBackPressed()
    }
}