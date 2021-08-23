package com.jesvardo.app.ui

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.BaseAdapter
import android.widget.TextView
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.jesvardo.app.R
import com.jesvardo.app.base.BaseActivity
import com.jesvardo.app.databinding.ActivityAddVehicleBinding
import com.jesvardo.app.network.model.ModelresponseVehicleMakes
import com.jesvardo.app.ui.viewmodels.AddVehicleViewModel
import com.jesvardo.app.utils.listeners.setSafeOnClickListener
import kotlinx.android.synthetic.main.base_activity.*


class ActivityAddVehicle : BaseActivity() {

    private lateinit var activityAddVehicleBinding: ActivityAddVehicleBinding

    private lateinit var addVehicleViewModel: AddVehicleViewModel

    lateinit var listGetVehicleType: ArrayList<ModelresponseVehicleMakes>

    var adapter: CustomAdapter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        activityAddVehicleBinding = putContentView(R.layout.activity_add_vehicle) as ActivityAddVehicleBinding
        addVehicleViewModel = ViewModelProviders.of(this).get(AddVehicleViewModel::class.java)

        initToolbar()
        base_activity_toolbar.visibility = View.GONE
        base_activity_toolbar_textview_title.text = resources.getString(R.string.text_title_dashboard)
        base_activity_toolbar_imageview_back.visibility = View.GONE

        activityAddVehicleBinding.activityAddVehicleImgBack.setSafeOnClickListener {
            onBackPressed()
        }

        activityAddVehicleBinding.activityAddVehicleTxtGetStarted.setSafeOnClickListener {
            val i = Intent(this@ActivityAddVehicle, ActivityAddVehicleDetails::class.java)
            startActivity(i)
        }

        addVehicleViewModel.getVehicleType()

        addVehicleViewModel.vehicleTypeSuccess.observe(this, Observer {
            if (it) {
                listGetVehicleType = addVehicleViewModel.listVehicleType

                adapter = CustomAdapter(this@ActivityAddVehicle, listGetVehicleType)
                activityAddVehicleBinding.activityAddVehicleSpinner.adapter = adapter
            }
        })

        addVehicleViewModel.strError.observe(this, Observer {
            if (it != "") {
                showMessage(it)
            }
        })

        addVehicleViewModel.isShowProgress.observe(this, Observer {
            if (it != null) {
                if (it) {
                    showProgress()
                } else {
                    hideProgress()
                }
            }
        })
    }



    override fun onResume() {
        super.onResume()
    }

    override fun onBackPressed() {
        super.onBackPressed()
    }


    class CustomAdapter(
        private val mContext: Context,
        private val arrayList: ArrayList<ModelresponseVehicleMakes>
    ) : BaseAdapter() {
        override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
            val mLayoutInflater = (mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater)
            @SuppressLint("ViewHolder")
            val rowView = mLayoutInflater.inflate(R.layout.raw_spinner_item, parent, false)
            val mTextViewItem = rowView.findViewById<TextView>(R.id.raw_spinner_item_textview)
            mTextViewItem.text = arrayList[position].name
            return rowView
        }

        override fun getItem(position: Int): String {
            return arrayList[position].name
        }

        override fun getItemId(p0: Int): Long {
            return 0
        }

        override fun getCount(): Int {
            return arrayList.size
        }


    }

//    class CustomSpinnerCouponAdapter(
//        var mContext: Context,
//        resource: Int,
//        arrayList: ArrayList<ModelresponseVehicleMakes>
//    ) : ArrayAdapter<String>(mContext, resource) {
//
//        override fun getCount(): Int {
//            return arrayList.size
//        }
//
//        override fun getItem(position: Int): String {
//            return list.get(position).getCoupon_name()
//        }
//
//        override fun getView(
//            position: Int,
//            convertView: View?,
//            parent: ViewGroup
//        ): View {
//            val mLayoutInflater =
//                (mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater)
//            @SuppressLint("ViewHolder") val rowView =
//                mLayoutInflater.inflate(R.layout.raw_spinner_item, parent, false)
//            val mTextViewItem =
//                rowView.findViewById<TextView>(R.id.raw_spinner_item_textview)
//            mTextViewItem.setText(list.get(position).getCoupon_name())
//            return rowView
//        }
//
//    }

}