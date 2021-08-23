package com.jesvardo.app.ui

import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.viewpager.widget.PagerAdapter
import com.google.firebase.events.EventHandler
import com.jesvardo.app.R
import com.jesvardo.app.base.BaseActivity
import com.jesvardo.app.databinding.ActivityVehicleDetailsBinding
import com.jesvardo.app.network.model.ModelResponseVehicleList
import com.jesvardo.app.network.model.ModelResponseVehicleListImages
import com.jesvardo.app.ui.viewmodels.DashboardViewModel
import com.jesvardo.app.utils.CalendarView
import com.jesvardo.app.utils.listeners.setSafeOnClickListener
import com.jesvardo.app.utils.loadImage
import kotlinx.android.synthetic.main.activity_vehicle_details.*
import kotlinx.android.synthetic.main.base_activity.*
import java.text.SimpleDateFormat
import java.util.*

class ActivityVehicleDetails : BaseActivity() {

    private lateinit var activityVehicleDetailsBinding: ActivityVehicleDetailsBinding
    lateinit var dashboardViewModel: DashboardViewModel

    private lateinit var vehicleListAdapter: ActivityDashboard.VehicleListAdapter

    lateinit var mModelResponseVehicleList: ModelResponseVehicleList
    private var vehicleID: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        activityVehicleDetailsBinding =
            putContentView(R.layout.activity_vehicle_details) as ActivityVehicleDetailsBinding

        dashboardViewModel = ViewModelProviders.of(this).get(DashboardViewModel::class.java)
        activityVehicleDetailsBinding.dashboardViewModel = dashboardViewModel

        initToolbar()
        base_activity_toolbar.visibility = View.GONE

        appPermissions.checkPermissions()

        dashboardViewModel.strError.observe(this, Observer {
            if (it != "") {
                showMessage(it)
            }
        })

        dashboardViewModel.booleanAlreadyLoginWithOtherDevice.observe(this, Observer {
            if (it) {
                alreadyLoginWithOtherDevice()
            }
        })

        activity_vehicle_details_txt_buy.setSafeOnClickListener {
            showDateSelectionDialog()
        }

        vehicleID = intent.getIntExtra("id", 0)
        dashboardViewModel.getVehicleDetails(vehicleID.toString())

        dashboardViewModel.vehicleDetails.observe(this, Observer {
            setData(it)
        })
    }

    fun setData(mModelResponseVehicleList: ModelResponseVehicleList) {
        if (mModelResponseVehicleList != null) {

            if (mModelResponseVehicleList.listing != null && mModelResponseVehicleList.listing.images != null) {
                val adapter = ImageSliderAdapter(this, mModelResponseVehicleList.listing.images)
                activityVehicleDetailsBinding.activityVehicleDetailsRecyclerPhoto.adapter = adapter
                activityVehicleDetailsBinding.activityVehicleDetailsDotsIndicator.setViewPager(
                    activityVehicleDetailsBinding.activityVehicleDetailsRecyclerPhoto
                )
            }

            if (mModelResponseVehicleList.listing != null) {
                if (mModelResponseVehicleList.listing.name != null)
                    activityVehicleDetailsBinding.activityVehicleDetailsTxtTitle.text = mModelResponseVehicleList.listing.name

                if (mModelResponseVehicleList.listing.description != null)
                    activityVehicleDetailsBinding.activityVehicleDetailsTxtDesc.text = mModelResponseVehicleList.listing.description

                if(mModelResponseVehicleList.listing.allow_pets) {
                    activityVehicleDetailsBinding.activityVehicleDetailsTxtPetFriendly.setTextColor(ContextCompat.getColor(this, R.color.color_dark_blue_green))
                } else {
                    activityVehicleDetailsBinding.activityVehicleDetailsTxtPetFriendly.setTextColor(ContextCompat.getColor(this, R.color.color_pale_gray))
                }

                if(mModelResponseVehicleList.listing.allow_festivals) {
                    activityVehicleDetailsBinding.activityVehicleDetailsTxtFestival.setTextColor(ContextCompat.getColor(this, R.color.color_dark_blue_green))
                } else {
                    activityVehicleDetailsBinding.activityVehicleDetailsTxtFestival.setTextColor(ContextCompat.getColor(this, R.color.color_pale_gray))
                }

                if(mModelResponseVehicleList.listing.allow_smoking) {
                    activityVehicleDetailsBinding.activityVehicleDetailsTxtSmoking.setTextColor(ContextCompat.getColor(this, R.color.color_dark_blue_green))
                } else {
                    activityVehicleDetailsBinding.activityVehicleDetailsTxtSmoking.setTextColor(ContextCompat.getColor(this, R.color.color_pale_gray))
                }

                if(mModelResponseVehicleList.listing.country != null && mModelResponseVehicleList.listing.nightly_rate != null) {
                    activityVehicleDetailsBinding.activityVehicleDetailsTxtDailyRate.text = mModelResponseVehicleList.listing.country +" $" +mModelResponseVehicleList.listing.nightly_rate.toString()
                }

                if(mModelResponseVehicleList.listing.country != null && mModelResponseVehicleList.listing.security_deposit != null) {
                    activityVehicleDetailsBinding.activityVehicleDetailsTxtSecurityDeposite.text = mModelResponseVehicleList.listing.country +" $" +mModelResponseVehicleList.listing.security_deposit.toString()
                }

                if(mModelResponseVehicleList.listing.miles_included_per_day != null) {
                    activityVehicleDetailsBinding.activityVehicleDetailsTxtMilePerDay.text = mModelResponseVehicleList.listing.miles_included_per_day.toString() + " miles included per day."
                }

                if(mModelResponseVehicleList.listing.country != null && mModelResponseVehicleList.listing.mileage_overage_charge != null) {
                    activityVehicleDetailsBinding.activityVehicleDetailsTxtMilePerAdditionalUser.text = mModelResponseVehicleList.listing.country +" $" +mModelResponseVehicleList.listing.mileage_overage_charge.toString() + " per mile of additional use."
                }

                if(mModelResponseVehicleList.listing.owner != null) {
                    activityVehicleDetailsBinding.activityVehicleDetailsTxtOwner.text = mModelResponseVehicleList.listing.owner.first_name +" "+mModelResponseVehicleList.listing.owner.last_name + "'s Vehicle Rental"
                }

                if(mModelResponseVehicleList.listing.nightly_rate != null) {
                    activityVehicleDetailsBinding.activityVehicleDetailsTxtBuy.text = mModelResponseVehicleList.listing.country +" $" +mModelResponseVehicleList.listing.nightly_rate.toString() +"/night"
                }
            }


            if (mModelResponseVehicleList.overnight_guests != null) {
                activityVehicleDetailsBinding.activityVehicleDetailsTxtBed.text = mModelResponseVehicleList.overnight_guests.toString()
                activityVehicleDetailsBinding.activityVehicleDetailsTxtFilledBed.text = mModelResponseVehicleList.overnight_guests.toString()
            }

            if (mModelResponseVehicleList.body_length != null) {
                activityVehicleDetailsBinding.activityVehicleDetailsTxtLength.text = mModelResponseVehicleList.body_length.toString() + "ft"
                activityVehicleDetailsBinding.activityVehicleDetailsTxtFilledLength.text = mModelResponseVehicleList.body_length.toString() + "ft"
            }

            if (mModelResponseVehicleList.vehicle_year != null) {
                activityVehicleDetailsBinding.activityVehicleDetailsTxtYear.text = mModelResponseVehicleList.vehicle_year
                activityVehicleDetailsBinding.activityVehicleDetailsTxtFilledYear.text = mModelResponseVehicleList.vehicle_year
            }



        }
    }

    class ImageSliderAdapter(
        private val context: ActivityVehicleDetails,
        var listImages: ArrayList<ModelResponseVehicleListImages>
    ) : PagerAdapter() {
        lateinit var layoutInflater: LayoutInflater

        override fun isViewFromObject(view: View, `object`: Any): Boolean {
            return view === `object` as RelativeLayout
        }

        override fun getCount(): Int {
            return listImages.size
        }

        override fun instantiateItem(container: ViewGroup, position: Int): Any {
            layoutInflater = LayoutInflater.from(context)
            var view = layoutInflater.inflate(R.layout.raw_vehicle_details_list, container, false)

            var imageView = view.findViewById<ImageView>(R.id.raw_dash_vehicle_list_img_close)
            imageView.setSafeOnClickListener {
                context.finish()
            }
            var imageViewMain = view.findViewById<ImageView>(R.id.raw_dash_vehicle_list_img_main)

            loadImage(imageViewMain, listImages[position].url)

            container.addView(view, 0)
            return view
        }

        override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {

            container.removeView(`object` as RelativeLayout)
        }

    }

    override fun onResume() {
        super.onResume()
    }

    override fun onBackPressed() {
        super.onBackPressed()
    }

    private fun showDateSelectionDialog() {
        val dialog = Dialog(this@ActivityVehicleDetails)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.window?.setBackgroundDrawableResource(R.drawable.shape_background_default_popup)
        dialog.setCancelable(false)
        dialog.setContentView(R.layout.dialog_book_date)

        val txtCancel = dialog.findViewById(R.id.dialog_book_date_txt_close) as TextView

        val events = HashSet<Date>()
        events.add(Date())

        val cv = dialog.findViewById(R.id.dialog_book_date_calendar_view) as CalendarView
//        cv.updateCalendar(events)

        // assign event handler

        cv.setEventHandler {
            val df = SimpleDateFormat.getDateInstance()
            Toast.makeText(this@ActivityVehicleDetails, df.format(it), Toast.LENGTH_SHORT).show()
        }

        txtCancel.setOnClickListener { dialog.dismiss() }
        dialog.show()
    }


}