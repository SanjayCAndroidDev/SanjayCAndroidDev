package com.jesvardo.app.ui

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.lifecycle.ViewModelProviders
import com.jesvardo.app.R
import com.jesvardo.app.base.BaseActivity
import com.jesvardo.app.databinding.ActivityChatBinding
import com.jesvardo.app.databinding.ActivityDashboardBinding
import com.jesvardo.app.databinding.ActivityProfileBinding
import com.jesvardo.app.ui.viewmodels.DashboardViewModel
import com.jesvardo.app.utils.AppConstants
import com.jesvardo.app.utils.listeners.setSafeOnClickListener
import kotlinx.android.synthetic.main.activity_profile.*
import kotlinx.android.synthetic.main.base_activity.*

class ActivityProfile : BaseActivity() {

    private lateinit var activityProfileBinding: ActivityProfileBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        activityProfileBinding =  putContentView(R.layout.activity_profile) as ActivityProfileBinding

        initToolbar()
        base_activity_toolbar.visibility = View.GONE
        base_activity_toolbar_imageview_back.visibility = View.GONE


        if (!appPreferences.getAppPrefString(AppConstants.PREF_USER_EMAIL).equals("", false)) {
            activity_profile_txt_logout.visibility = View.VISIBLE
        } else {
            activity_profile_txt_logout.visibility = View.GONE
        }

        activity_profile_txt_logout.setSafeOnClickListener {
            showLogoutDialog()
        }

        activity_profile_txt_trip.setSafeOnClickListener {
            val i = Intent(this@ActivityProfile, ActivityMyTrip::class.java)
            startActivity(i)
        }

        activity_profile_txt_profile.setSafeOnClickListener {
            val i = Intent(this@ActivityProfile, ActivityUpdateProfile::class.java)
            startActivity(i)
        }

        activity_profile_txt_help.setSafeOnClickListener {
            val i = Intent(this@ActivityProfile, ActivityHelpCenter::class.java)
            startActivity(i)
        }

        activity_profile_txt_payment.setSafeOnClickListener {
            val i = Intent(this@ActivityProfile, ActivityPaymentMethods::class.java)
            startActivity(i)
        }

        activity_profile_txt_locations.setSafeOnClickListener {
            val i = Intent(this@ActivityProfile, ActivityMyAddress::class.java)
            startActivity(i)
        }

        activity_profile_txt_notifications.setSafeOnClickListener {
            val i = Intent(this@ActivityProfile, ActivityNotification::class.java)
            startActivity(i)
        }

    }

    @SuppressLint("RestrictedApi")
    override fun onResume() {
        super.onResume()
        selectBottomBarImage(4)
        base_activity_floating_add.visibility = View.VISIBLE
        base_activity_linear_bottom.visibility = View.VISIBLE
    }


    override fun onBackPressed() {
        super.onBackPressed()
    }
}