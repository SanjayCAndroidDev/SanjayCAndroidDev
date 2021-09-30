package com.jesvardo.app.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.jesvardo.app.R
import com.jesvardo.app.ui.ActivityAddVehicleDetails
import com.jesvardo.app.ui.ActivityIntro
import com.jesvardo.app.utils.listeners.setSafeOnClickListener
import kotlinx.android.synthetic.main.activity_intro.view.*

class FragmentAddVehicleSecond : Fragment() {

    private lateinit var createdView: View

    private var intPosition : Int = 0

    lateinit var baseActivity: ActivityAddVehicleDetails

    lateinit var textContinue: TextView


    var intSleeperSeat: Int = 14
    var intSeatWithSeatBelt: Int = 14
    var intKing: Int = 14
    var intQueen: Int = 14
    var intTwin: Int = 14
    var intFull: Int = 14
    var intDinette: Int = 14
    var intBunkBed: Int = 14
    var intSoldOutSofa: Int = 14
    var intOther: Int = 14


    lateinit var textSleepSeat: TextView
    lateinit var textSleepSeatPlus: TextView
    lateinit var textSleepSeatMinus: TextView
    lateinit var textSeatWithSeatBelt: TextView
    lateinit var textSeatWithSeatBeltPlus: TextView
    lateinit var textSeatWithSeatBeltMinus: TextView
    lateinit var textKing: TextView
    lateinit var textKingPlus: TextView
    lateinit var textKingMinus: TextView
    lateinit var textQueen: TextView
    lateinit var textQueenPlus: TextView
    lateinit var textQueenMinus: TextView
    lateinit var textTwin: TextView
    lateinit var textTwinPlus: TextView
    lateinit var textTwinMinus: TextView
    lateinit var textFull: TextView
    lateinit var textFullPlus: TextView
    lateinit var textFullMinus: TextView
    lateinit var textDinette: TextView
    lateinit var textDinettePlus: TextView
    lateinit var textDinetteMinus: TextView
    lateinit var textBunkBed: TextView
    lateinit var textBunkBedPlus: TextView
    lateinit var textBunkBedMinus: TextView
    lateinit var textSoldOutSofa: TextView
    lateinit var textSoldOutSofaPlus: TextView
    lateinit var textSoldOutSofaMinus: TextView
    lateinit var textOther: TextView
    lateinit var textOtherPlus: TextView
    lateinit var textOtherMinus: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        intPosition = arguments!!.getInt("position")
        baseActivity = activity as ActivityAddVehicleDetails
        baseActivity.window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE)

    }

    companion object {
        fun newInstance(position: Int): Fragment {
            val fragment = FragmentAddVehicleSecond()
            val args = Bundle()
            args.putInt("position", position)
            fragment.arguments = args
            return fragment
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        createdView = inflater.inflate(R.layout.fragment_add_vehicle_details_second, container, false)

        textContinue = createdView.findViewById<TextView>(R.id.fragment_add_vehicle_detail_second_txt_continue)

        textSleepSeat = createdView.findViewById<TextView>(R.id.fragment_add_vehicle_detail_second_txt_sleeper_seat)
        textSleepSeatPlus = createdView.findViewById<TextView>(R.id.fragment_add_vehicle_detail_second_txt_add_sleeper_seat)
        textSleepSeatMinus = createdView.findViewById<TextView>(R.id.fragment_add_vehicle_detail_second_txt_minus_sleeper_seat)

        textSleepSeatPlus.setSafeOnClickListener {
            intSleeperSeat++
            textSleepSeat.text = intSleeperSeat.toString()
        }

        textSleepSeatMinus.setSafeOnClickListener {
            if(intSleeperSeat > 0) {
                intSleeperSeat--
                textSleepSeat.text = intSleeperSeat.toString()
            }
        }


        textSeatWithSeatBelt = createdView.findViewById<TextView>(R.id.fragment_add_vehicle_detail_second_txt_passenger)
        textSeatWithSeatBeltPlus = createdView.findViewById<TextView>(R.id.fragment_add_vehicle_detail_second_txt_add_passenger)
        textSeatWithSeatBeltMinus = createdView.findViewById<TextView>(R.id.fragment_add_vehicle_detail_second_txt_minus_passenger)

        textSeatWithSeatBeltPlus.setSafeOnClickListener {
            intSeatWithSeatBelt++
            textSeatWithSeatBelt.text = intSeatWithSeatBelt.toString()
        }

        textSeatWithSeatBeltMinus.setSafeOnClickListener {
            if(intSeatWithSeatBelt > 0) {
                intSeatWithSeatBelt--
                textSeatWithSeatBelt.text = intSeatWithSeatBelt.toString()
            }
        }

       //King
        textKing = createdView.findViewById<TextView>(R.id.fragment_add_vehicle_detail_second_txt_king)
        textKingPlus = createdView.findViewById<TextView>(R.id.fragment_add_vehicle_detail_second_txt_add_king)
        textKingMinus = createdView.findViewById<TextView>(R.id.fragment_add_vehicle_detail_second_txt_minus_king)

        textKingPlus.setSafeOnClickListener {
            intKing++
            textKing.text = intKing.toString()
        }

        textKingMinus.setSafeOnClickListener {
            if(intKing > 0) {
                intKing--
                textKing.text = intKing.toString()
            }
        }

        //Queen
        textQueen = createdView.findViewById<TextView>(R.id.fragment_add_vehicle_detail_second_txt_queen)
        textQueenPlus = createdView.findViewById<TextView>(R.id.fragment_add_vehicle_detail_second_txt_add_queen)
        textQueenMinus = createdView.findViewById<TextView>(R.id.fragment_add_vehicle_detail_second_txt_minus_queen)

        textQueenPlus.setSafeOnClickListener {
            intQueen++
            textQueen.text = intQueen.toString()
        }

        textQueenMinus.setSafeOnClickListener {
            if(intQueen > 0) {
                intQueen--
                textQueen.text = intQueen.toString()
            }
        }

        //Twin
        textTwin = createdView.findViewById<TextView>(R.id.fragment_add_vehicle_detail_second_txt_twin)
        textTwinPlus = createdView.findViewById<TextView>(R.id.fragment_add_vehicle_detail_second_txt_add_twin)
        textTwinMinus = createdView.findViewById<TextView>(R.id.fragment_add_vehicle_detail_second_txt_minus_twin)

        textTwinPlus.setSafeOnClickListener {
            intTwin++
            textTwin.text = intTwin.toString()
        }

        textTwinMinus.setSafeOnClickListener {
            if(intTwin > 0) {
                intTwin--
                textTwin.text = intTwin.toString()
            }
        }


        //Full
        textFull = createdView.findViewById<TextView>(R.id.fragment_add_vehicle_detail_second_txt_full)
        textFullPlus = createdView.findViewById<TextView>(R.id.fragment_add_vehicle_detail_second_txt_add_full)
        textFullMinus = createdView.findViewById<TextView>(R.id.fragment_add_vehicle_detail_second_txt_minus_full)

        textFullPlus.setSafeOnClickListener {
            intFull++
            textFull.text = intFull.toString()
        }

        textFullMinus.setSafeOnClickListener {
            if(intFull > 0) {
                intFull--
                textFull.text = intFull.toString()
            }
        }


        //Dinette
        textDinette = createdView.findViewById<TextView>(R.id.fragment_add_vehicle_detail_second_txt_dinette)
        textDinettePlus = createdView.findViewById<TextView>(R.id.fragment_add_vehicle_detail_second_txt_add_dinette)
        textDinetteMinus = createdView.findViewById<TextView>(R.id.fragment_add_vehicle_detail_second_txt_minus_dinette)

        textDinettePlus.setSafeOnClickListener {
            intDinette++
            textDinette.text = intDinette.toString()
        }

        textDinetteMinus.setSafeOnClickListener {
            if(intDinette > 0) {
                intDinette--
                textDinette.text = intDinette.toString()
            }
        }


        //BunkBed
        textBunkBed = createdView.findViewById<TextView>(R.id.fragment_add_vehicle_detail_second_txt_bunk)
        textBunkBedPlus = createdView.findViewById<TextView>(R.id.fragment_add_vehicle_detail_second_txt_add_bunk)
        textBunkBedMinus = createdView.findViewById<TextView>(R.id.fragment_add_vehicle_detail_second_txt_minus_bunk)

        textBunkBedPlus.setSafeOnClickListener {
            intBunkBed++
            textBunkBed.text = intBunkBed.toString()
        }

        textBunkBedMinus.setSafeOnClickListener {
            if(intBunkBed > 0) {
                intBunkBed--
                textBunkBed.text = intBunkBed.toString()
            }
        }

        //SoldOutSofa
        textSoldOutSofa = createdView.findViewById<TextView>(R.id.fragment_add_vehicle_detail_second_txt_fold_sofa)
        textSoldOutSofaPlus = createdView.findViewById<TextView>(R.id.fragment_add_vehicle_detail_second_txt_add_fold_sofa)
        textSoldOutSofaMinus = createdView.findViewById<TextView>(R.id.fragment_add_vehicle_detail_second_txt_minus_fold_sofa)

        textSoldOutSofaPlus.setSafeOnClickListener {
            intSoldOutSofa++
            textSoldOutSofa.text = intSoldOutSofa.toString()
        }

        textSoldOutSofaMinus.setSafeOnClickListener {
            if(intSoldOutSofa > 0) {
                intSoldOutSofa--
                textSoldOutSofa.text = intSoldOutSofa.toString()
            }
        }


        //Other
        textOther = createdView.findViewById<TextView>(R.id.fragment_add_vehicle_detail_second_txt_other)
        textOtherPlus = createdView.findViewById<TextView>(R.id.fragment_add_vehicle_detail_second_txt_add_other)
        textOtherMinus = createdView.findViewById<TextView>(R.id.fragment_add_vehicle_detail_second_txt_minus_other)

        textOtherPlus.setSafeOnClickListener {
            intOther++
            textOther.text = intOther.toString()
        }

        textOtherMinus.setSafeOnClickListener {
            if(intOther > 0) {
                intOther--
                textOther.text = intOther.toString()
            }
        }



        textContinue.setSafeOnClickListener {
            baseActivity.activityAddVehicleDetailsBinding.activityAddVehicleDetailViewPager.setCurrentItem(2, true)
        }

        return createdView
    }
}