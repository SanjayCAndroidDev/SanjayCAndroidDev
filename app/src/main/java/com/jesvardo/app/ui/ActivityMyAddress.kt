package com.jesvardo.app.ui

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.jesvardo.app.R
import com.jesvardo.app.base.BaseActivity
import com.jesvardo.app.databinding.ActivityChatBinding
import com.jesvardo.app.databinding.ActivityDashboardBinding
import com.jesvardo.app.databinding.ActivityNotificationBinding
import com.jesvardo.app.databinding.ActivityPaymentMethodBinding
import com.jesvardo.app.network.model.ModelResponseAddress
import com.jesvardo.app.network.model.ModelResponseVehicleList
import com.jesvardo.app.ui.viewmodels.DashboardViewModel
import com.jesvardo.app.ui.viewmodels.MyTripViewModel
import com.jesvardo.app.utils.listeners.setSafeOnClickListener
import kotlinx.android.synthetic.main.base_activity.*

class ActivityMyAddress : BaseActivity() {

    private lateinit var activityPaymentMethodBinding: ActivityPaymentMethodBinding

    private lateinit var myAddressAdapter: MyAddressAdapter

    lateinit var myTripViewModel: MyTripViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        activityPaymentMethodBinding =  putContentView(R.layout.activity_payment_method) as ActivityPaymentMethodBinding

        myTripViewModel = ViewModelProviders.of(this).get(MyTripViewModel::class.java)

        initToolbar()
        base_activity_toolbar.visibility = View.VISIBLE
        base_activity_toolbar_textview_title.text = resources.getString(R.string.text_title_my_addresses)
        base_activity_toolbar_imageview_back.visibility = View.VISIBLE

        base_activity_toolbar_imageview_back.setSafeOnClickListener {
            onBackPressed()
        }

        activityPaymentMethodBinding.activityPaymentMethodTxtAdd.text = "ADD NEW ADDRESS"

        myTripViewModel.getAddress()

        myTripViewModel.addressSucess.observe(this, Observer {

            var mLayoutManager = LinearLayoutManager(applicationContext)
            mLayoutManager.orientation = LinearLayoutManager.VERTICAL
            myAddressAdapter = MyAddressAdapter(this@ActivityMyAddress, it)
            activityPaymentMethodBinding.activityPaymentMethodRecycler.layoutManager = mLayoutManager
            activityPaymentMethodBinding.activityPaymentMethodRecycler.adapter = myAddressAdapter
        })

        myTripViewModel.strError.observe(this, Observer {
            if (it != "") {
                showMessage(it)
            }
        })

        myTripViewModel.isShowProgress.observe(this, Observer {
            if (it != null) {
                if (it) {
                    showProgress()
                } else {
                    hideProgress()
                }
            }
        })



    }

    @SuppressLint("RestrictedApi")
    override fun onResume() {
        super.onResume()
    }


    override fun onBackPressed() {
        super.onBackPressed()
    }

    class MyAddressAdapter(val mContext: ActivityMyAddress, var addressList: ArrayList<ModelResponseAddress>) : RecyclerView.Adapter<MyAddressAdapter.MyViewHolder>() {

        class MyViewHolder(view: View) : RecyclerView.ViewHolder(view) {
            val textViewName: TextView = view.findViewById<TextView>(R.id.raw_my_address_list_txt_address)
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
            val itemView = LayoutInflater.from(parent.context)
                .inflate(R.layout.raw_my_address_list, parent, false)
            return MyAddressAdapter.MyViewHolder(itemView)
        }

        override fun getItemCount(): Int {
            return addressList.size
        }

        override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
            holder.itemView.setSafeOnClickListener {

            }

            holder.textViewName.text = addressList[position].street_1 +", " + addressList[position].street_2 + ", " + addressList[position].city + ", " +  addressList[position].state + ", " + addressList[position].country +", " + addressList[position].zipcode
        }
    }
}