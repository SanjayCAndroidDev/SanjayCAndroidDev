package com.jesvardo.app.ui

import android.annotation.SuppressLint
import android.app.DatePickerDialog
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.view.WindowManager
import android.widget.ArrayAdapter
import android.widget.DatePicker
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.jesvardo.app.R
import com.jesvardo.app.base.BaseActivity
import com.jesvardo.app.databinding.ActivityChatBinding
import com.jesvardo.app.databinding.ActivityDashboardBinding
import com.jesvardo.app.databinding.ActivityProfileBinding
import com.jesvardo.app.databinding.ActivityUpdateProfileBinding
import com.jesvardo.app.ui.viewmodels.DashboardViewModel
import com.jesvardo.app.ui.viewmodels.LoginViewModel
import com.jesvardo.app.ui.viewmodels.ProfileViewModel
import com.jesvardo.app.utils.AppConstants
import com.jesvardo.app.utils.listeners.setSafeOnClickListener
import kotlinx.android.synthetic.main.activity_profile.*
import kotlinx.android.synthetic.main.base_activity.*
import java.util.*

class ActivityUpdateProfile : BaseActivity(), DatePickerDialog.OnDateSetListener  {

    private lateinit var activityUpdateProfileBinding: ActivityUpdateProfileBinding

    private lateinit var profileViewModel: ProfileViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        activityUpdateProfileBinding =  putContentView(R.layout.activity_update_profile) as ActivityUpdateProfileBinding
        profileViewModel = ViewModelProviders.of(this).get(ProfileViewModel::class.java)

        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);

        initToolbar()
        base_activity_toolbar.visibility = View.VISIBLE
        base_activity_toolbar_textview_title.text = resources.getString(R.string.text_title_profile)
        base_activity_toolbar_imageview_back.visibility = View.VISIBLE

        base_activity_toolbar_imageview_back.setSafeOnClickListener {
            onBackPressed()
        }

        profileViewModel.getUserDetails(appPreferences.getAppPrefString(AppConstants.PREF_USER_ID)!!)

        profileViewModel.strError.observe(this, Observer {
            if (it != "") {
                showMessage(it)
            }
        })

        profileViewModel.isShowProgress.observe(this, Observer {
            if (it != null) {
                if (it) {
                    showProgress()
                } else {
                    hideProgress()
                }
            }
        })

        profileViewModel.getUserDetails.observe(this, Observer {
            if(it != null) {
                activityUpdateProfileBinding.activityUpdateProfileEditFirstName.setText(it.first_name)
                activityUpdateProfileBinding.activityUpdateProfileEditLastName.setText(it.last_name)
                activityUpdateProfileBinding.activityUpdateProfileEditEmail.setText(it.email)
                activityUpdateProfileBinding.activityUpdateProfileEditDob.setText(it.dob)
                activityUpdateProfileBinding.activityUpdateProfileEditNumber.setText(it.phone_number)
            }
        })

        activityUpdateProfileBinding.activityUpdateProfileEditDob.setSafeOnClickListener {
            val calendar: Calendar = Calendar.getInstance()

            val days = calendar.get(Calendar.DAY_OF_MONTH)
            val month = calendar.get(Calendar.MONTH)
            val year = calendar.get(Calendar.YEAR)

            val datePickerDialog = DatePickerDialog(this@ActivityUpdateProfile, this@ActivityUpdateProfile, year, month, days)
            datePickerDialog.datePicker.maxDate = System.currentTimeMillis() - 1000
            datePickerDialog.show()
        }

        val listGender = resources.getStringArray(R.array.gender)
        val arrayAdapter: ArrayAdapter<Any?> = ArrayAdapter<Any?>(this@ActivityUpdateProfile, R.layout.spinner_list, listGender)
        arrayAdapter.setDropDownViewResource(R.layout.spinner_list)
        activityUpdateProfileBinding.activityUpdateProfileSpinnerGender.adapter = arrayAdapter


        val listCountryCode = resources.getStringArray(R.array.countryCode)
        var countryCodeAdapter: ArrayAdapter<Any?> = ArrayAdapter<Any?>(this@ActivityUpdateProfile, R.layout.spinner_list, listCountryCode)
        countryCodeAdapter.setDropDownViewResource(R.layout.spinner_list)
        activityUpdateProfileBinding.activityUpdateProfileSpinnerCode.adapter = countryCodeAdapter

    }

    @SuppressLint("RestrictedApi")
    override fun onResume() {
        super.onResume()
    }


    override fun onBackPressed() {
        super.onBackPressed()
    }

    override fun onDateSet(view: DatePicker?, year: Int, month: Int, dayOfMonth: Int) {
        var intMonthFromDate = month + 1
        activityUpdateProfileBinding.activityUpdateProfileEditDob.setText("$year-$intMonthFromDate-$dayOfMonth")
    }
}