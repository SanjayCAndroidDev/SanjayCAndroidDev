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
import com.jesvardo.app.databinding.ActivityUpdateProfileBinding
import com.jesvardo.app.ui.viewmodels.DashboardViewModel
import com.jesvardo.app.utils.AppConstants
import com.jesvardo.app.utils.listeners.setSafeOnClickListener
import kotlinx.android.synthetic.main.activity_profile.*
import kotlinx.android.synthetic.main.base_activity.*

class ActivityUpdateProfile : BaseActivity() {

    private lateinit var activityUpdateProfileBinding: ActivityUpdateProfileBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        activityUpdateProfileBinding =  putContentView(R.layout.activity_update_profile) as ActivityUpdateProfileBinding

        initToolbar()
        base_activity_toolbar.visibility = View.VISIBLE
        base_activity_toolbar_textview_title.text = resources.getString(R.string.text_title_profile)
        base_activity_toolbar_imageview_back.visibility = View.VISIBLE

        base_activity_toolbar_imageview_back.setSafeOnClickListener {
            onBackPressed()
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