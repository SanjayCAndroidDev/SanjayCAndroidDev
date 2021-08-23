package com.jesvardo.app.ui

import android.annotation.SuppressLint
import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.*
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager.widget.PagerAdapter
import com.bumptech.glide.Glide
import com.jesvardo.app.R
import com.jesvardo.app.base.BaseActivity
import com.jesvardo.app.databinding.ActivityDashboardBinding
import com.jesvardo.app.injection.modules.AppGlideModule
import com.jesvardo.app.network.model.ModelResponseGetVehicleType
import com.jesvardo.app.network.model.ModelResponseVehicleList
import com.jesvardo.app.network.model.ModelResponseVehicleType
import com.jesvardo.app.network.model.ModelresponseVehicleMakes
import com.jesvardo.app.ui.viewmodels.DashboardViewModel
import com.jesvardo.app.utils.AppConstants
import com.jesvardo.app.utils.listeners.setSafeOnClickListener
import com.jesvardo.app.utils.loadImage
import kotlinx.android.synthetic.main.base_activity.*


class ActivityDashboard : BaseActivity() {

    private lateinit var activityDashboardBinding: ActivityDashboardBinding
    lateinit var dashboardViewModel: DashboardViewModel

    private lateinit var vehicleListAdapter: VehicleListAdapter
    private lateinit var filterAdapter: FilterAdapter

    lateinit var filterType: ArrayList<String>
    lateinit var listVehicleTypeDrivable: ArrayList<ModelResponseGetVehicleType>
    lateinit var listVehicleTypeTowable: ArrayList<ModelResponseGetVehicleType>
    lateinit var listVehicle: ArrayList<ModelResponseVehicleList>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        activityDashboardBinding =
            putContentView(R.layout.activity_dashboard) as ActivityDashboardBinding

        dashboardViewModel = ViewModelProviders.of(this).get(DashboardViewModel::class.java)
        activityDashboardBinding.dashboardViewModel = dashboardViewModel

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

        val adapter = ImageSliderAdapter(this)
        activityDashboardBinding.activityDashboardViewpagerPhoto.adapter = adapter
        activityDashboardBinding.activityDashboardDotsIndicator.setViewPager(
            activityDashboardBinding.activityDashboardViewpagerPhoto
        );

        activityDashboardBinding.activityDashboardTxtUserName.text = "Hi ${appPreferences.getAppPrefString(
            AppConstants.PREF_USER_FIRST_NAME)} ${appPreferences.getAppPrefString(AppConstants.PREF_USER_LAST_NAME)}"

        activityDashboardBinding.activityDashboardRlNotification.setSafeOnClickListener {
            val i = Intent(this@ActivityDashboard, ActivityNotification::class.java)
            startActivity(i)
        }

        filterType = ArrayList<String>()
        filterType.add("DATES")
        filterType.add("CITY")
        filterType.add("GUESTS")
        filterType.add("PRICE")
        filterType.add("VEHICLE TYPE")
        filterType.add("MAP")

        var mLayoutManager = LinearLayoutManager(applicationContext)
        mLayoutManager = LinearLayoutManager(applicationContext)
        mLayoutManager.orientation = LinearLayoutManager.HORIZONTAL
        filterAdapter = FilterAdapter(this@ActivityDashboard, filterType)
        activityDashboardBinding.activityDashboardRecyclerFilter.layoutManager = mLayoutManager
        activityDashboardBinding.activityDashboardRecyclerFilter.adapter = filterAdapter

        dashboardViewModel.getVehicleType("drivable")

        dashboardViewModel.vehicleTypeDrivableSuccess.observe(this, Observer {
            if (it) {
                listVehicleTypeDrivable = dashboardViewModel.listVehicleTypeDrivable
                dashboardViewModel.getVehicleType("towable")
                dashboardViewModel.vehicleTypeDrivableSuccess.value = false
            }
        })

        dashboardViewModel.vehicleTypeTowableSuccess.observe(this, Observer {
            if (it) {
                listVehicleTypeTowable = dashboardViewModel.listVehicleTypeTowable
                dashboardViewModel.vehicleTypeTowableSuccess.value = false
            }
        })

        dashboardViewModel.vehicleListSuccess.observe(this, Observer {
            if (it) {
                listVehicle = dashboardViewModel.listVehicle

                var mLayoutManager = LinearLayoutManager(applicationContext)
                mLayoutManager.orientation = LinearLayoutManager.VERTICAL
                vehicleListAdapter = VehicleListAdapter(this@ActivityDashboard, listVehicle)
                activityDashboardBinding.activityDashboardRecyclerVehicleList.layoutManager = mLayoutManager
                activityDashboardBinding.activityDashboardRecyclerVehicleList.adapter = vehicleListAdapter
            }
        })

        dashboardViewModel.isShowProgress.observe(this, Observer {
            if (it != null) {
                if (it) {
                    showProgress()
                } else {
                    hideProgress()
                }
            }
        })

        var hashMap: HashMap<String, String> = HashMap<String, String>()
        hashMap["transmission"] = "automatic"
        hashMap["fuel_type"] = "diesel"
        dashboardViewModel.getVehicleList(hashMap)

    }


    class ImageSliderAdapter(private val context: Context) : PagerAdapter() {

        lateinit var layoutInflater: LayoutInflater

        override fun isViewFromObject(view: View, `object`: Any): Boolean {
            return view === `object` as RelativeLayout
        }

        override fun getCount(): Int {
            return 7
        }

        override fun instantiateItem(container: ViewGroup, position: Int): Any {

            layoutInflater = LayoutInflater.from(context)
            var view = layoutInflater.inflate(R.layout.raw_dash_image_list, container, false)
            val img = view.findViewById<ImageView>(R.id.raw_dash_image_list_img_main)

            container.addView(view, 0)
            return view
        }

        override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
            container.removeView(`object` as RelativeLayout)
        }
    }


    class FilterAdapter(val mContext: ActivityDashboard, val filterType: ArrayList<String>) :
        RecyclerView.Adapter<FilterAdapter.MyViewHolder>() {

        var selectedPotion: Int = -1

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
                mContext.showDialogSelection(selectedPotion)
            }

            holder.textViewName.text = filterType[position]

            if (selectedPotion > -1) {
                if (position == selectedPotion) {
                    holder.textViewName.setBackgroundResource(R.drawable.bd_orage_fill)
                    holder.textViewName.setTextColor(ContextCompat.getColor(mContext, R.color.white))
                } else {
                    holder.textViewName.setBackgroundResource(R.drawable.bd_orage_line)
                    holder.textViewName.setTextColor(ContextCompat.getColor(mContext, R.color.color_red_orange))
                }
            }
        }
    }


    class VehicleListAdapter(
        val mContext: ActivityDashboard,
        var listVehicle: ArrayList<ModelResponseVehicleList>
    ) : RecyclerView.Adapter<VehicleListAdapter.MyViewHolder>() {

        class MyViewHolder(view: View) : RecyclerView.ViewHolder(view) {
            val textViewName: TextView = view.findViewById<TextView>(R.id.raw_dash_vehicle_list_txt_name)
            val textViewPrice: TextView = view.findViewById<TextView>(R.id.raw_dash_vehicle_list_txt_price)
            val imageMian: ImageView = view.findViewById<ImageView>(R.id.raw_dash_vehicle_list_img_main)
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
            val itemView = LayoutInflater.from(parent.context)
                .inflate(R.layout.raw_dash_vehicle_list, parent, false)
            return MyViewHolder(itemView)
        }

        override fun getItemCount(): Int {
            return listVehicle.size
        }

        override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
            holder.itemView.setSafeOnClickListener {
                val intent = Intent(it.context, ActivityVehicleDetails::class.java)
                intent.putExtra("id", listVehicle[position].id)
                it.context.startActivity(intent)
            }

            if (listVehicle[position] != null && listVehicle[position].listing != null && listVehicle[position].listing.name != null)
                holder.textViewName.text = listVehicle[position].listing.name

            if (listVehicle[position] != null && listVehicle[position].listing != null && listVehicle[position].listing.country != null
                && listVehicle[position].listing.miles_included_per_day != null
            ) {
                holder.textViewPrice.text =
                    listVehicle[position].listing.country + " $" + listVehicle[position].listing.miles_included_per_day
            }

            if (listVehicle[position] != null && listVehicle[position].listing != null && listVehicle[position].listing.images != null
                && listVehicle[position].listing.images.isNotEmpty()) {
                loadImage(holder.imageMian, listVehicle[position].listing.images[0].url)
            }

        }

    }

    @SuppressLint("RestrictedApi")
    override fun onResume() {
        super.onResume()
        selectBottomBarImage(1)
        base_activity_floating_add.visibility = View.VISIBLE
        base_activity_linear_bottom.visibility = View.VISIBLE
    }

    override fun onBackPressed() {
        showExitDialog()
    }

    fun showDialogSelection(intPosition: Int) {

        when (intPosition) {
            0 -> {
                showDateSelectionDialog()
            }

            1 -> {
                showCitySelectionDialog()
            }

            2 -> {
                showGuestsSelectionDialog()
            }

            3 -> {
                showPriceSelectionDialog()
            }

            4 -> {
                showVehicleTypeSelectionDialog()
            }

            5 -> {
                val i = Intent(this@ActivityDashboard, ActivityMapVehicle::class.java)
                startActivity(i)
            }
        }

    }

    private fun showDateSelectionDialog() {
        val dialog = Dialog(this@ActivityDashboard)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.window?.setBackgroundDrawableResource(R.drawable.shape_background_default_popup)
        dialog.setCancelable(false)
        dialog.setContentView(R.layout.dialog_filter_date)

        val txtCancel = dialog.findViewById(R.id.dialog_filter_date_txt_cancel) as TextView
        txtCancel.setOnClickListener { dialog.dismiss() }
        dialog.show()
    }

    private fun showCitySelectionDialog() {
        val dialog = Dialog(this@ActivityDashboard)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.window?.setBackgroundDrawableResource(R.drawable.shape_background_default_popup)
        dialog.setCancelable(false)
        dialog.setContentView(R.layout.dialog_filter_city)

        val txtCancel = dialog.findViewById(R.id.dialog_filter_city_txt_cancel) as TextView
        txtCancel.setOnClickListener { dialog.dismiss() }
        dialog.show()
    }

    private fun showGuestsSelectionDialog() {
        val dialog = Dialog(this@ActivityDashboard)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.window?.setBackgroundDrawableResource(R.drawable.shape_background_default_popup)
        dialog.window?.setLayout(
            LinearLayout.LayoutParams.MATCH_PARENT,
            LinearLayout.LayoutParams.WRAP_CONTENT
        )
        dialog.setCancelable(false)
        dialog.setContentView(R.layout.dialog_filter_guests)

        val txtCancel = dialog.findViewById(R.id.dialog_filter_guests_txt_cancel) as TextView
        txtCancel.setOnClickListener { dialog.dismiss() }
        dialog.show()
    }

    private fun showPriceSelectionDialog() {
        val dialog = Dialog(this@ActivityDashboard)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.window?.setBackgroundDrawableResource(R.drawable.shape_background_default_popup)
        dialog.window?.setLayout(
            LinearLayout.LayoutParams.MATCH_PARENT,
            LinearLayout.LayoutParams.WRAP_CONTENT
        )
        dialog.setCancelable(false)
        dialog.setContentView(R.layout.dialog_filter_price)

        val txtCancel = dialog.findViewById(R.id.dialog_filter_price_txt_cancel) as TextView
        txtCancel.setOnClickListener { dialog.dismiss() }
        dialog.show()
    }

    private fun showVehicleTypeSelectionDialog() {
        val dialog = Dialog(this@ActivityDashboard)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.window?.setBackgroundDrawableResource(R.drawable.shape_background_default_popup)
        dialog.window?.setLayout(
            LinearLayout.LayoutParams.MATCH_PARENT,
            LinearLayout.LayoutParams.WRAP_CONTENT
        )
        dialog.setCancelable(false)
        dialog.setContentView(R.layout.dialog_filter_vehicle_type)

        val txtCancel = dialog.findViewById(R.id.dialog_filter_vehicle_type_txt_cancel) as TextView
        val txtDrivable = dialog.findViewById(R.id.dialog_filter_vehicle_type_txt_drivable) as TextView
        val txtTowable = dialog.findViewById(R.id.dialog_filter_vehicle_type_txt_towable) as TextView

        val reclerList = dialog.findViewById(R.id.dialog_filter_vehicle_type_recycler) as RecyclerView

        txtDrivable.setOnClickListener {
            txtDrivable.setBackgroundResource(R.drawable.bg_orange_fill_left)
            txtDrivable.setTextColor(ContextCompat.getColor(this@ActivityDashboard, R.color.white))
            txtTowable.setBackgroundResource(R.drawable.bg_orage_line_right)
            txtTowable.setTextColor(ContextCompat.getColor(this@ActivityDashboard, R.color.color_red_orange))
        }

        txtTowable.setOnClickListener {
            txtDrivable.setBackgroundResource(R.drawable.bg_orage_line_left)
            txtDrivable.setTextColor(ContextCompat.getColor(this@ActivityDashboard, R.color.color_red_orange))
            txtTowable.setBackgroundResource(R.drawable.bg_orange_fill_right)
            txtTowable.setTextColor(ContextCompat.getColor(this@ActivityDashboard, R.color.white))
        }

        txtCancel.setOnClickListener { dialog.dismiss() }
        dialog.show()
    }




}

