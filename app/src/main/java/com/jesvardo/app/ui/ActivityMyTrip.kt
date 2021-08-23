package com.jesvardo.app.ui

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.jesvardo.app.R
import com.jesvardo.app.base.BaseActivity
import com.jesvardo.app.databinding.ActivityForgotPasswordBinding
import com.jesvardo.app.databinding.ActivityMyTripBinding
import com.jesvardo.app.network.model.ModelResponseAddress
import com.jesvardo.app.network.model.ModelResponseMyBookingList
import com.jesvardo.app.ui.viewmodels.ForgotPasswordViewModel
import com.jesvardo.app.ui.viewmodels.MyTripViewModel
import com.jesvardo.app.utils.listeners.setSafeOnClickListener
import com.jesvardo.app.utils.loadImage
import kotlinx.android.synthetic.main.base_activity.*
import java.text.SimpleDateFormat

class ActivityMyTrip : BaseActivity() {

    private lateinit var activityMyTripBinding: ActivityMyTripBinding
    private lateinit var myTripViewModel: MyTripViewModel

    private lateinit var vehicleListAdapter: VehicleListAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        initToolbar()
        base_activity_toolbar.visibility = View.VISIBLE
        base_activity_toolbar_textview_title.text = resources.getString(R.string.text_title_my_trips)
        base_activity_toolbar_imageview_back.visibility = View.VISIBLE

        base_activity_toolbar_imageview_back.setSafeOnClickListener {
            onBackPressed()
        }

        activityMyTripBinding = putContentView(R.layout.activity_my_trip) as ActivityMyTripBinding
        myTripViewModel = ViewModelProviders.of(this).get(MyTripViewModel::class.java)

        myTripViewModel.getMyTrips()

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

        myTripViewModel.myTripsList.observe(this, Observer {
            var mLayoutManager = LinearLayoutManager(applicationContext)
            mLayoutManager.orientation = LinearLayoutManager.VERTICAL
            vehicleListAdapter = VehicleListAdapter(this@ActivityMyTrip, it)
            activityMyTripBinding.activityMyTripRecycler.layoutManager = mLayoutManager
            activityMyTripBinding.activityMyTripRecycler.adapter = vehicleListAdapter
        })

    }

    class VehicleListAdapter(val mContext: ActivityMyTrip, var tripList: ArrayList<ModelResponseMyBookingList>) : RecyclerView.Adapter<VehicleListAdapter.MyViewHolder>() {

        val dateFormat = SimpleDateFormat("yyyy-MM-dd")
        val dateFormatConvert = SimpleDateFormat("dd MMM yyyy")

        class MyViewHolder(view: View) : RecyclerView.ViewHolder(view) {
            val textViewName: TextView = view.findViewById<TextView>(R.id.raw_my_trip_list_txt_name)
            val textViewDate: TextView = view.findViewById<TextView>(R.id.raw_my_trip_list_txt_date)
            val textViewStatus: TextView = view.findViewById<TextView>(R.id.raw_my_trip_list_txt_status)
            val textViewPrice: TextView = view.findViewById<TextView>(R.id.raw_my_trip_list_txt_price)

            val imageMain: ImageView = view.findViewById<ImageView>(R.id.raw_my_trip_list_img_main)
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
            val itemView = LayoutInflater.from(parent.context).inflate(R.layout.raw_my_trip_list, parent, false)
            return MyViewHolder(itemView)
        }

        override fun getItemCount(): Int {
            return tripList.size
        }

        override fun onBindViewHolder(holder: MyViewHolder, position: Int) {

            holder.itemView.setSafeOnClickListener {
                val intent = Intent(it.context, ActivityMyTripDetails::class.java)
                intent.putExtra("id", tripList[position].id)
                it.context.startActivity(intent)
            }

            if (tripList[position] != null && tripList[position].listing != null && tripList[position].listing.name != null)
                holder.textViewName.text = tripList[position].listing.name

            if (tripList[position] != null && tripList[position].listing != null && tripList[position].listing.country != null
                && tripList[position].price_quote != null && tripList[position].price_quote.total != null) {
                holder.textViewPrice.text = tripList[position].listing.country + " $" + tripList[position].price_quote.total.toString()
            }

            if(tripList[position].status != null) {
                holder.textViewStatus.text = tripList[position].status
            }

            if (tripList[position] != null && tripList[position].listing != null && tripList[position].listing.images != null
                && tripList[position].listing.images.isNotEmpty()) {
                loadImage(holder.imageMain, tripList[position].listing.images[0].url)
            }

            if(tripList[position] != null && tripList[position].from_date != null && tripList[position].to_date != null) {
                val fromDate = dateFormat.parse(tripList[position].from_date)
                val toDate = dateFormat.parse(tripList[position].to_date)
                holder.textViewDate.text = dateFormatConvert.format(fromDate) +" to " + dateFormatConvert.format(toDate)
            }
        }
    }
}