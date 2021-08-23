package com.jesvardo.app.ui

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter
import androidx.lifecycle.ViewModelProviders
import com.google.android.material.tabs.TabLayout
import com.jesvardo.app.R
import com.jesvardo.app.base.BaseActivity
import com.jesvardo.app.databinding.ActivityChatBinding
import com.jesvardo.app.databinding.ActivityDashboardBinding
import com.jesvardo.app.ui.fragments.FragmentIntro
import com.jesvardo.app.ui.fragments.FragmentMsgAll
import com.jesvardo.app.ui.fragments.FragmentMsgBuying
import com.jesvardo.app.ui.fragments.FragmentMsgSelling
import com.jesvardo.app.ui.viewmodels.DashboardViewModel
import com.jesvardo.app.utils.listeners.setSafeOnClickListener
import kotlinx.android.synthetic.main.base_activity.*

class ActivityChat : BaseActivity() {

    private lateinit var activityChatBinding: ActivityChatBinding

    private lateinit var messagePagerAdapter: MessagePagerAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        activityChatBinding =  putContentView(R.layout.activity_chat) as ActivityChatBinding

        initToolbar()
        base_activity_toolbar.visibility = View.GONE

        activityChatBinding.activityChatTxtBack.setSafeOnClickListener {
            onBackPressed()
        }

        activityChatBinding.activityChatTabMessage.addTab(activityChatBinding.activityChatTabMessage.newTab().setText("ALL"))
        activityChatBinding.activityChatTabMessage.addTab(activityChatBinding.activityChatTabMessage.newTab().setText("BUYING"))
        activityChatBinding.activityChatTabMessage.addTab(activityChatBinding.activityChatTabMessage.newTab().setText("SELLING"))

        activityChatBinding.activityChatTabMessage.tabGravity = TabLayout.GRAVITY_FILL

        messagePagerAdapter = MessagePagerAdapter(supportFragmentManager)
        activityChatBinding.activityChatViewPagerMessage.adapter = messagePagerAdapter

        activityChatBinding.activityChatViewPagerMessage.addOnPageChangeListener(TabLayout.TabLayoutOnPageChangeListener(activityChatBinding.activityChatTabMessage))

        activityChatBinding.activityChatTabMessage.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab) {
                activityChatBinding.activityChatViewPagerMessage.currentItem = tab.position
            }
            override fun onTabUnselected(tab: TabLayout.Tab) {}
            override fun onTabReselected(tab: TabLayout.Tab) {}
        })


        activityChatBinding.activityChatRelativeNoMessage.visibility = View.GONE

    }

    @SuppressLint("RestrictedApi")
    override fun onResume() {
        super.onResume()
        selectBottomBarImage(2)
        base_activity_floating_add.visibility = View.VISIBLE
        base_activity_linear_bottom.visibility = View.VISIBLE
    }


    override fun onBackPressed() {
        super.onBackPressed()
    }

    class MessagePagerAdapter(supportFragmentManager: FragmentManager) : FragmentStatePagerAdapter(supportFragmentManager, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {

        override fun getItem(position: Int): Fragment {

            return when (position) {

                0 -> {
                    return FragmentMsgAll.newInstance(position)
                }

                1 -> {
                    return FragmentMsgBuying.newInstance(position)
                }

                2 -> {
                    return FragmentMsgSelling.newInstance(position)
                }

                else -> {
                    return FragmentMsgAll.newInstance(position)
                }
            }

        }
        override fun getCount(): Int {
            return 3
        }
    }
}