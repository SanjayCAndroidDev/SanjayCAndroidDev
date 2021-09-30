package com.jesvardo.app.ui

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter
import androidx.viewpager.widget.ViewPager
import com.jesvardo.app.R
import com.jesvardo.app.base.BaseActivity
import com.jesvardo.app.databinding.ActivityAddVehicleBinding
import com.jesvardo.app.databinding.ActivityAddVehicleDetailsBinding
import com.jesvardo.app.ui.fragments.*
import com.jesvardo.app.utils.listeners.setSafeOnClickListener
import kotlinx.android.synthetic.main.base_activity.*

class ActivityAddVehicleDetails : BaseActivity() {

    lateinit var activityAddVehicleDetailsBinding: ActivityAddVehicleDetailsBinding

    private lateinit var introViewPagerAdapter : IntroViewPagerAdapter

    var intPosition:Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


        intPosition = intent.getIntExtra("intPosition", 0);

        activityAddVehicleDetailsBinding =  putContentView(R.layout.activity_add_vehicle_details) as ActivityAddVehicleDetailsBinding

        initToolbar()
        base_activity_toolbar.visibility = View.GONE

        activityAddVehicleDetailsBinding.activityAddVehicleDetailImgBack.setSafeOnClickListener {
            onBackPressed()
        }

        introViewPagerAdapter = IntroViewPagerAdapter(supportFragmentManager)
        activityAddVehicleDetailsBinding.activityAddVehicleDetailViewPager.adapter = introViewPagerAdapter


        activityAddVehicleDetailsBinding.activityAddVehicleDetailViewPager.setOnTouchListener { v, event ->
            return@setOnTouchListener true
        }

        activityAddVehicleDetailsBinding.activityAddVehicleDetailSeekBar.setOnTouchListener { v, event ->
            return@setOnTouchListener true
        }


        activityAddVehicleDetailsBinding.activityAddVehicleDetailViewPager.addOnPageChangeListener(object :
            ViewPager.OnPageChangeListener {

            override fun onPageScrollStateChanged(state: Int) {

            }

            override fun onPageScrolled(
                position: Int,
                positionOffset: Float,
                positionOffsetPixels: Int
            ) {

            }

            override fun onPageSelected(position: Int) {
                when (position) {
                    0 -> {
                        activityAddVehicleDetailsBinding.activityAddVehicleDetailSeekBar.progress = 20
                    }

                    1 -> {
                        activityAddVehicleDetailsBinding.activityAddVehicleDetailSeekBar.progress = 40
                    }

                    2 -> {
                        activityAddVehicleDetailsBinding.activityAddVehicleDetailSeekBar.progress = 60
                    }

                    3 -> {
                        activityAddVehicleDetailsBinding.activityAddVehicleDetailSeekBar.progress = 80
                    }

                    4 -> {
                        activityAddVehicleDetailsBinding.activityAddVehicleDetailSeekBar.progress = 100
                    }

                }
            }
        })

        activityAddVehicleDetailsBinding.activityAddVehicleDetailSeekBar.progress = 20

        activityAddVehicleDetailsBinding.activityAddVehicleDetailViewPager.currentItem = intPosition

    }

    override fun onResume() {
        super.onResume()
    }

    override fun onBackPressed() {
        super.onBackPressed()
    }

    class IntroViewPagerAdapter(supportFragmentManager: FragmentManager) : FragmentStatePagerAdapter(supportFragmentManager, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {
        override fun getItem(position: Int): Fragment {
            return when (position) {
                0 -> {
                    return FragmentAddVehicleFirst.newInstance(position)
                }

                1 -> {
                    return FragmentAddVehicleSecond.newInstance(position)
                }

                2 -> {
                    return FragmentAddVehicleThird.newInstance(position)
                }

                3 -> {
                    return FragmentAddVehicleFourth.newInstance(position)
                }

                4 -> {
                    return FragmentAddVehicleFifth.newInstance(position)
                }


                else -> {
                    return FragmentAddVehicleFirst.newInstance(position)
                }
            }

        }
        override fun getCount(): Int {
            return 5
        }
    }
}