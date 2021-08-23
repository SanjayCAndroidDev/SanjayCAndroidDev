package com.jesvardo.app.base

import android.app.Application
import android.app.Dialog
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.net.ConnectivityManager
import android.os.Bundle
import android.view.ViewGroup
import android.view.Window
import androidx.annotation.LayoutRes
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.airbnb.lottie.LottieAnimationView
import com.google.android.material.snackbar.BaseTransientBottomBar
import com.google.android.material.snackbar.Snackbar
import com.google.android.play.core.appupdate.AppUpdateManager
import com.google.android.play.core.appupdate.AppUpdateManagerFactory
import com.jesvardo.app.R
import com.jesvardo.app.base.BaseViewModel
import com.jesvardo.app.base.MyApplication
import com.jesvardo.app.databinding.BaseActivityBinding
import com.jesvardo.app.ui.*
import com.jesvardo.app.utils.*
import com.jesvardo.app.utils.listeners.ConnectivityReceiver
import com.jesvardo.app.utils.listeners.setSafeOnClickListener
import kotlinx.android.synthetic.main.base_activity.*
import javax.inject.Inject

open class BaseActivity : AppCompatActivity(), ConnectivityReceiver.ConnectivityReceiverListener {

    @Inject
    lateinit var applications: Application

    @Inject
    lateinit var context: Context

    @Inject
    lateinit var appUtils: AppUtils

    @Inject
    lateinit var appPreferences: AppPreferences

    @Inject
    lateinit var appDialogUtils: AppDialogUtils

    @Inject
    lateinit var appSocialLoginUtils: AppSocialLoginUtils

    private lateinit var baseActivityBinding: BaseActivityBinding

    lateinit var appPermissions: AppPermissions

    lateinit var baseViewModel: BaseViewModel

    private lateinit var broadcastReceiver: BroadcastReceiver

    private var mSnackBar: Snackbar? = null

    private var dialogProgress: Dialog? = null
    private var dialogForceUpdate: Dialog? = null
    private var dialogExitApplication: Dialog? = null
    private var dialogLogoutFromApplication: Dialog? = null
    private var mDialogCrashLogout: Dialog? = null

    var isActivityAnimationDone = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        MyApplication.appComponent.inject(this)
        overrideAnimationStart()

        baseActivityBinding = DataBindingUtil.setContentView(this, R.layout.base_activity)
        baseViewModel = ViewModelProviders.of(this).get(BaseViewModel::class.java)

        appPermissions = AppPermissions(this)

        // Creates instance of the manager.
        val appUpdateManager: AppUpdateManager? = AppUpdateManagerFactory.create(this)

        broadcastReceiver = ConnectivityReceiver()

        registerReceiver(
            broadcastReceiver,
            IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION)
        )

        baseViewModel.strErrorBase.observe(this, Observer {
            if (it != "") {
                showMessage(it)
            }
        })

        baseViewModel.modelResponseForceUpdate.observe(this, Observer {
            if (it != null) {
                if (it.success == "1") {
                    if (it.data.update == "1") {
                        if (it.data.flag == "1") {
                            //Toast.makeText(context, "Show Force Update!", Toast.LENGTH_LONG).show()
                        } else {
                            //Toast.makeText(context, "Don't Show Force Update!", Toast.LENGTH_LONG) .show()
                        }
                    }
                }
            }
        })

        baseViewModel.booleanLogoutSuccess.observe(this, Observer {
            if (it) {
                appPreferences.clearAllPrefData()
                val myIntent = Intent(this@BaseActivity, ActivityLogin::class.java)
                startActivity(myIntent)
                finishAffinity()
            }
        })

        base_activity_img_home.setSafeOnClickListener {

            val i = Intent(this@BaseActivity, ActivityDashboard::class.java)
            finishAffinity()
            startActivity(i)

            selectBottomBarImage(1)
        }

        base_activity_img_msg.setSafeOnClickListener {

            val i = Intent(this@BaseActivity, ActivityChat::class.java)
            startActivity(i)

            selectBottomBarImage(2)
        }

        base_activity_img_wishlist.setSafeOnClickListener {
            val i = Intent(this@BaseActivity, ActivityWishList::class.java)
            startActivity(i)

            selectBottomBarImage(3)
        }
        base_activity_img_profile.setSafeOnClickListener {
            val i = Intent(this@BaseActivity, ActivityProfile::class.java)
            startActivity(i)

            selectBottomBarImage(4)
        }

        base_activity_floating_add.setSafeOnClickListener {
            val i = Intent(this@BaseActivity, ActivityAddVehicle::class.java)
            startActivity(i)
        }

    }

    override fun onResume() {
        super.onResume()
        ConnectivityReceiver.connectivityReceiverListener = this

        if (MyApplication.isInternetAvailable) {
            if (this !is ActivitySplash) {
//                baseViewModel.checkForceUpdate()
            }
        }

    }

    override fun onPause() {
        super.onPause()
        if (dialogProgress != null && dialogProgress!!.isShowing) {
            dialogProgress!!.dismiss()
        }

        if (dialogForceUpdate != null && dialogForceUpdate!!.isShowing) {
            dialogForceUpdate!!.dismiss()
        }

        if (dialogExitApplication != null && dialogExitApplication!!.isShowing) {
            dialogExitApplication!!.dismiss()
        }

        if (mDialogCrashLogout != null && mDialogCrashLogout!!.isShowing) {
            mDialogCrashLogout!!.dismiss()
        }

    }

    override fun onDestroy() {
        super.onDestroy()
        unregisterReceiver(broadcastReceiver)
    }

    override fun onBackPressed() {
        super.onBackPressed()
        overrideAnimationEnd()
    }

    fun initToolbar() {
        val toolbar = findViewById<Toolbar>(R.id.base_activity_toolbar)

        base_activity_toolbar_imageview_back.setSafeOnClickListener {
            onBackPressed()
        }

    }

    protected open fun <T : ViewDataBinding?> putContentView(@LayoutRes resId: Int): T {
        val frameLayout = findViewById<ViewGroup?>(R.id.base_activity_fl_content)

        return DataBindingUtil.inflate<T>(
            layoutInflater,
            resId,
            frameLayout,
            true
        )
    }

    fun showMessage(strError: String) {
        mSnackBar = Snackbar.make(
            findViewById(R.id.base_activity_rl_container),
            strError,
            Snackbar.LENGTH_LONG
        ) //Assume "rootLayout" as the root layout of every activity.
        mSnackBar?.duration = BaseTransientBottomBar.LENGTH_LONG
        mSnackBar?.show()
    }

    override fun onNetworkConnectionChanged(isConnected: Boolean) {
        MyApplication.isInternetAvailable = isConnected
    }

    fun showProgress() {
        if (!this.isFinishing && !this.isDestroyed) {
            dialogProgress = Dialog(this)

            dialogProgress!!.requestWindowFeature(Window.FEATURE_NO_TITLE)
            dialogProgress!!.setCancelable(false)
            dialogProgress!!.setContentView(R.layout.raw_progress_layout)
            dialogProgress!!.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

            val mLottieAnimationView: LottieAnimationView = dialogProgress!!.findViewById(
                R.id.raw_progress_layout_animationview
            )

            mLottieAnimationView.imageAssetsFolder = "images/"
            mLottieAnimationView.setAnimation("animation_loader.json")
            mLottieAnimationView.loop(true)
            mLottieAnimationView.playAnimation()

            if (!dialogProgress!!.isShowing) dialogProgress!!.show()
        }

    }

    fun hideProgress() {
        if (!this.isFinishing && !this.isDestroyed) {
            if (dialogProgress != null && dialogProgress!!.isShowing) {
                dialogProgress!!.dismiss()
            }
        }
    }

    fun showExitDialog() {
        val mAlertDialogBuilder = AlertDialog.Builder(this)

        mAlertDialogBuilder.setTitle(resources.getString(R.string.text_exit_title))
            .setMessage(resources.getString(R.string.text_exit_information))
            .setCancelable(false)
            .setPositiveButton(resources.getString(R.string.text_yes)) { dialog, _ ->
                dialog.dismiss()
                this.finishAffinity()
            }
            .setNegativeButton(resources.getString(R.string.text_no)) { dialog, _ ->
                dialog.dismiss()
            }

        if (dialogExitApplication == null) {
            dialogExitApplication = mAlertDialogBuilder.create()
        }

        if (!dialogExitApplication!!.isShowing) {
            dialogExitApplication!!.show()
        }
    }

    fun showLogoutDialog() {
        val mAlertDialogBuilder = AlertDialog.Builder(this)

        mAlertDialogBuilder.setTitle(resources.getString(R.string.text_logout_title))
            .setMessage(resources.getString(R.string.text_logout_information))
            .setCancelable(false)
            .setPositiveButton(resources.getString(R.string.text_yes)) { dialog, _ ->
                dialog.dismiss()
//                baseViewModel.doUserLogout()

                appPreferences.clearAllPrefData()
                val myIntent = Intent(this@BaseActivity, ActivityLoginSelection::class.java)
                startActivity(myIntent)
                finishAffinity()
            }
            .setNegativeButton(resources.getString(R.string.text_no)) { dialog, _ ->
                dialog.dismiss()
            }

        if (dialogLogoutFromApplication == null) {
            dialogLogoutFromApplication = mAlertDialogBuilder.create()
        }

        if (!dialogLogoutFromApplication!!.isShowing) {
            dialogLogoutFromApplication!!.show()
        }
    }

    open fun alreadyLoginWithOtherDevice() {
        val builder = AlertDialog.Builder(this, R.style.AlertDialogStyleAppCompat)
        builder.setTitle(getString(R.string.app_name))
        builder.setCancelable(false)
        builder.setMessage(getString(R.string.text_already_login_with_other_device))
        builder.setPositiveButton(getString(R.string.text_yes)) { dialog, _ ->
            dialog.dismiss()
            appPreferences.clearAllPrefData()
            val i = Intent(this@BaseActivity, ActivityLogin::class.java)
            finishAffinity()
            startActivity(i)
        }
        if (mDialogCrashLogout == null) {
            mDialogCrashLogout = builder.create()
        }

        if (!mDialogCrashLogout!!.isShowing) {
            mDialogCrashLogout!!.show()
        }

    }

    //Check If Animation Is Running When Sliding Activity
    open fun checkIsAnimationDone() {
        val thread: Thread = object : Thread() {
            override fun run() {
                try {
                    sleep(resources.getInteger(android.R.integer.config_longAnimTime).toLong())
                } catch (e: InterruptedException) {
                    e.printStackTrace()
                } finally {
                    isActivityAnimationDone = true
                }
            }
        }
        thread.start()
    }

    //Override Animation On Going to Next Screen
    open fun overrideAnimationStart() {
        if (isActivityAnimationDone) {
            isActivityAnimationDone = false
            checkIsAnimationDone()

            if (this !is ActivityDashboard && this !is ActivitySplash) {
                overridePendingTransition(R.anim.trans_left_in, R.anim.trans_left_out)
            }
        }
    }

    //Override Animation On Going to Previous Screen
    open fun overrideAnimationEnd() {
        if (isActivityAnimationDone) {
            isActivityAnimationDone = false
            checkIsAnimationDone()

            if (this !is ActivityDashboard) {
                overridePendingTransition(R.anim.trans_right_in, R.anim.trans_right_out)
            }
        }
    }

    fun selectBottomBarImage(position: Int) {

        when (position) {
            1 -> {
                base_activity_img_home.setImageResource(R.drawable.home_selected)
                base_activity_img_msg.setImageResource(R.drawable.message_deselect)
                base_activity_img_wishlist.setImageResource(R.drawable.wishlist_deselect)
                base_activity_img_profile.setImageResource(R.drawable.profile_unselect)
            }

            2 -> {
                base_activity_img_home.setImageResource(R.drawable.home_deselect)
                base_activity_img_msg.setImageResource(R.drawable.message_select)
                base_activity_img_wishlist.setImageResource(R.drawable.wishlist_deselect)
                base_activity_img_profile.setImageResource(R.drawable.profile_unselect)
            }

            3 -> {
                base_activity_img_home.setImageResource(R.drawable.home_deselect)
                base_activity_img_msg.setImageResource(R.drawable.message_deselect)
                base_activity_img_wishlist.setImageResource(R.drawable.wishlist_select)
                base_activity_img_profile.setImageResource(R.drawable.profile_unselect)
            }

            4 -> {
                base_activity_img_home.setImageResource(R.drawable.home_deselect)
                base_activity_img_msg.setImageResource(R.drawable.message_deselect)
                base_activity_img_wishlist.setImageResource(R.drawable.wishlist_deselect)
                base_activity_img_profile.setImageResource(R.drawable.profile_select)
            }
        }
    }

}