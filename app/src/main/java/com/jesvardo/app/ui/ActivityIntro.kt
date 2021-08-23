package com.jesvardo.app.ui

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter
import androidx.viewpager.widget.ViewPager
import com.jesvardo.app.R
import com.jesvardo.app.base.BaseActivity
import com.jesvardo.app.databinding.ActivityIntroBinding
import com.jesvardo.app.databinding.ActivityLoginBinding
import com.jesvardo.app.ui.fragments.FragmentIntro
import com.jesvardo.app.utils.listeners.setSafeOnClickListener
import kotlinx.android.synthetic.main.base_activity.*
import java.util.*

class ActivityIntro : BaseActivity() {

    private lateinit var activityIntroBinding: ActivityIntroBinding

    private lateinit var introViewPagerAdapter : IntroViewPagerAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        initToolbar()
        base_activity_toolbar.visibility = View.GONE

        activityIntroBinding = putContentView(R.layout.activity_intro) as ActivityIntroBinding

        introViewPagerAdapter = IntroViewPagerAdapter(supportFragmentManager)
        activityIntroBinding.activityInfoViewPager.adapter = introViewPagerAdapter
        activityIntroBinding.activityInfoTabLayout.setupWithViewPager(activityIntroBinding.activityInfoViewPager, false)

        activityIntroBinding.activityInfoViewPager?.addOnPageChangeListener(object : ViewPager.OnPageChangeListener {

            override fun onPageScrollStateChanged(state: Int) {
            }

            override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {

            }
            override fun onPageSelected(position: Int) {

                when (position) {
                    0 -> {
                        activityIntroBinding.activityInfoTxtNext.text = "NEXT"
                    }
                    1 -> {
                        activityIntroBinding.activityInfoTxtNext.text = "GRT STARTED"
                    }
                }
            }
        })


        activityIntroBinding.activityInfoTxtNext.setSafeOnClickListener {
            if(activityIntroBinding.activityInfoViewPager.currentItem == 0) {
                activityIntroBinding.activityInfoViewPager.currentItem = 1
            } else {
                val i = Intent(this@ActivityIntro, ActivityLoginSelection::class.java)
                startActivity(i)
                finishAffinity()
            }
        }

    }

    class IntroViewPagerAdapter(supportFragmentManager: FragmentManager) : FragmentStatePagerAdapter(supportFragmentManager, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {
        override fun getItem(position: Int): Fragment {
            return FragmentIntro.newInstance(position)
        }
        override fun getCount(): Int {
            return 2
        }
    }
}