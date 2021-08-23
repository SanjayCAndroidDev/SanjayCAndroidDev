package com.jesvardo.app.ui

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.view.View
import com.jesvardo.app.R
import com.jesvardo.app.base.BaseActivity
import com.jesvardo.app.databinding.ActivitySplashBinding
import com.jesvardo.app.utils.AppConstants
import kotlinx.android.synthetic.main.base_activity.*

class ActivitySplash : BaseActivity() {

    private lateinit var activitySplashBinding: ActivitySplashBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activitySplashBinding = putContentView(R.layout.activity_splash) as ActivitySplashBinding

        initToolbar()
        base_activity_toolbar.visibility = View.GONE

        Handler().postDelayed({
            if (!appPreferences.getAppPrefString(AppConstants.PREF_USER_EMAIL).equals("", false)) {
                val i = Intent(this@ActivitySplash, ActivityDashboard::class.java)
                startActivity(i)
                finishAffinity()
            } else {
                val i = Intent(this@ActivitySplash, ActivityIntro::class.java)
                startActivity(i)
                finishAffinity()
            }
        }, 2000)

    }

}