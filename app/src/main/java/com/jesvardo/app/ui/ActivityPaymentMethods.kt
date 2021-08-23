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
import com.jesvardo.app.databinding.ActivityPaymentMethodBinding
import com.jesvardo.app.ui.viewmodels.DashboardViewModel
import com.jesvardo.app.utils.listeners.setSafeOnClickListener
import kotlinx.android.synthetic.main.base_activity.*

class ActivityPaymentMethods : BaseActivity() {

    private lateinit var activityPaymentMethodBinding: ActivityPaymentMethodBinding

    private lateinit var paymentMethodAdapter: PaymentMethodAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        activityPaymentMethodBinding =  putContentView(R.layout.activity_payment_method) as ActivityPaymentMethodBinding

        initToolbar()
        base_activity_toolbar.visibility = View.VISIBLE
        base_activity_toolbar_textview_title.text = resources.getString(R.string.text_title_payment_method)
        base_activity_toolbar_imageview_back.visibility = View.VISIBLE
        base_activity_toolbar_imageview_right.visibility = View.VISIBLE

        base_activity_toolbar_imageview_back.setSafeOnClickListener {
            onBackPressed()
        }

        var mLayoutManager = LinearLayoutManager(applicationContext)
        mLayoutManager.orientation = LinearLayoutManager.VERTICAL
        paymentMethodAdapter = PaymentMethodAdapter(this@ActivityPaymentMethods)
        activityPaymentMethodBinding.activityPaymentMethodRecycler.layoutManager = mLayoutManager
        activityPaymentMethodBinding.activityPaymentMethodRecycler.adapter = paymentMethodAdapter

    }

    class PaymentMethodAdapter(val mContext: ActivityPaymentMethods) : RecyclerView.Adapter<PaymentMethodAdapter.MyViewHolder>() {

        class MyViewHolder(view: View) : RecyclerView.ViewHolder(view) {
            val textViewName: TextView = view.findViewById<TextView>(R.id.raw_payment_method_list_txt_card_no)
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
            val itemView = LayoutInflater.from(parent.context)
                .inflate(R.layout.raw_payment_mehod_list, parent, false)
            return PaymentMethodAdapter.MyViewHolder(itemView)
        }

        override fun getItemCount(): Int {
            return 4
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