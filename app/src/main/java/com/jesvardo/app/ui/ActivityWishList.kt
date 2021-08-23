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
import com.jesvardo.app.databinding.ActivityWishlistBinding
import com.jesvardo.app.ui.viewmodels.DashboardViewModel
import com.jesvardo.app.utils.listeners.setSafeOnClickListener
import kotlinx.android.synthetic.main.base_activity.*

class ActivityWishList : BaseActivity() {

    private lateinit var activityWishlistBinding: ActivityWishlistBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        activityWishlistBinding =  putContentView(R.layout.activity_wishlist) as ActivityWishlistBinding

        initToolbar()
        base_activity_toolbar.visibility = View.GONE

        activityWishlistBinding.activityWishlistTxtBack.setSafeOnClickListener {
            onBackPressed()
        }
    }

    @SuppressLint("RestrictedApi")
    override fun onResume() {
        super.onResume()
        selectBottomBarImage(3)
        base_activity_floating_add.visibility = View.VISIBLE
        base_activity_linear_bottom.visibility = View.VISIBLE
    }


    override fun onBackPressed() {
        super.onBackPressed()
    }
}