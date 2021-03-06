package com.jesvardo.app.ui.fragments

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.jesvardo.app.R
import com.jesvardo.app.ui.ActivityAddVehicleConfirm
import com.jesvardo.app.ui.ActivityAddVehicleDetails
import com.jesvardo.app.ui.ActivityDashboard
import com.jesvardo.app.ui.ActivityIntro
import com.jesvardo.app.utils.listeners.setSafeOnClickListener
import kotlinx.android.synthetic.main.activity_intro.view.*

class FragmentAddVehicleFifth : Fragment() {

    private lateinit var createdView: View

    private var intPosition : Int = 0

    lateinit var baseActivity: ActivityAddVehicleDetails

    lateinit var textContinue: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        intPosition = arguments!!.getInt("position")
        baseActivity = activity as ActivityAddVehicleDetails

    }

    companion object {
        fun newInstance(position: Int): Fragment {
            val fragment = FragmentAddVehicleFifth()
            val args = Bundle()
            args.putInt("position", position)
            fragment.arguments = args
            return fragment
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        createdView = inflater.inflate(R.layout.fragment_add_vehicle_details_fifth, container, false)

        textContinue = createdView.findViewById<TextView>(R.id.fragment_add_vehicle_detail_fifth_txt_continue)

        textContinue.setSafeOnClickListener {
            val i = Intent(baseActivity, ActivityAddVehicleConfirm::class.java)
            startActivity(i)
        }

        return createdView
    }
}