package com.jesvardo.app.ui

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.jesvardo.app.R
import com.jesvardo.app.base.BaseActivity
import com.jesvardo.app.databinding.ActivityChatBinding
import com.jesvardo.app.databinding.ActivityDashboardBinding
import com.jesvardo.app.databinding.ActivityHelpCenterBinding
import com.jesvardo.app.databinding.ActivityNotificationBinding
import com.jesvardo.app.ui.viewmodels.DashboardViewModel
import com.jesvardo.app.utils.listeners.setSafeOnClickListener
import kotlinx.android.synthetic.main.base_activity.*

class ActivityHelpCenter : BaseActivity() {

    private lateinit var activityHelpCenterBinding: ActivityHelpCenterBinding

    lateinit var filterType: ArrayList<String>

    private lateinit var filterAdapter: FilterAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        activityHelpCenterBinding =  putContentView(R.layout.activity_help_center) as ActivityHelpCenterBinding

        initToolbar()
        base_activity_toolbar.visibility = View.VISIBLE
        base_activity_toolbar_textview_title.text = resources.getString(R.string.text_title_help_center)
        base_activity_toolbar_imageview_back.visibility = View.VISIBLE

        base_activity_toolbar_imageview_back.setSafeOnClickListener {
            onBackPressed()
        }

        filterType = ArrayList<String>()
        filterType.add("General")
        filterType.add("Owner")
        filterType.add("Rental")
        filterType.add("Billing and Cancellation")

        var mLayoutManager = LinearLayoutManager(applicationContext)
        mLayoutManager.orientation = LinearLayoutManager.HORIZONTAL
        filterAdapter = FilterAdapter(this@ActivityHelpCenter, filterType)
        activityHelpCenterBinding.activityHelpCenterRecyclerFilter.layoutManager = mLayoutManager
        activityHelpCenterBinding.activityHelpCenterRecyclerFilter.adapter = filterAdapter

    }

    @SuppressLint("RestrictedApi")
    override fun onResume() {
        super.onResume()
    }

    override fun onBackPressed() {
        super.onBackPressed()
    }

    class FilterAdapter(val mContext: ActivityHelpCenter, val filterType: ArrayList<String>) :
        RecyclerView.Adapter<FilterAdapter.MyViewHolder>() {

        var selectedPotion: Int = 0

        class MyViewHolder(view: View) : RecyclerView.ViewHolder(view) {
            val textViewName: TextView =
                view.findViewById<TextView>(R.id.raw_dash_filter_list_txt_name)
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
            val itemView = LayoutInflater.from(parent.context)
                .inflate(R.layout.raw_dash_filter_list, parent, false)
            return FilterAdapter.MyViewHolder(itemView)
        }

        override fun getItemCount(): Int {
            return filterType.size
        }

        override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
            holder.itemView.setSafeOnClickListener {
                selectedPotion = position
                notifyDataSetChanged()
                mContext.showSelectionView(position)
            }

            holder.textViewName.text = filterType[position]

            if (position == selectedPotion) {
                holder.textViewName.setBackgroundResource(R.drawable.bg_faq_list)
            } else {
                holder.textViewName.setBackgroundResource(android.R.color.transparent)
            }
        }
    }

    fun showSelectionView(intPosition: Int) {

        when (intPosition) {
            0 -> {
                activityHelpCenterBinding.activityHelpCenterLinearGeneral.visibility = View.VISIBLE
                activityHelpCenterBinding.activityHelpCenterLinearOwner.visibility = View.GONE
                activityHelpCenterBinding.activityHelpCenterLinearRental.visibility = View.GONE
                activityHelpCenterBinding.activityHelpCenterLinearBilling.visibility = View.GONE
            }

            1 -> {
                activityHelpCenterBinding.activityHelpCenterLinearGeneral.visibility = View.GONE
                activityHelpCenterBinding.activityHelpCenterLinearOwner.visibility = View.VISIBLE
                activityHelpCenterBinding.activityHelpCenterLinearRental.visibility = View.GONE
                activityHelpCenterBinding.activityHelpCenterLinearBilling.visibility = View.GONE
            }

            2 -> {
                activityHelpCenterBinding.activityHelpCenterLinearGeneral.visibility = View.GONE
                activityHelpCenterBinding.activityHelpCenterLinearOwner.visibility = View.GONE
                activityHelpCenterBinding.activityHelpCenterLinearRental.visibility = View.VISIBLE
                activityHelpCenterBinding.activityHelpCenterLinearBilling.visibility = View.GONE
            }

            3 -> {
                activityHelpCenterBinding.activityHelpCenterLinearGeneral.visibility = View.GONE
                activityHelpCenterBinding.activityHelpCenterLinearOwner.visibility = View.GONE
                activityHelpCenterBinding.activityHelpCenterLinearRental.visibility = View.GONE
                activityHelpCenterBinding.activityHelpCenterLinearBilling.visibility = View.VISIBLE
            }

        }

    }
}