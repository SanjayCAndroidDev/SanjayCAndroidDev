package com.jesvardo.app.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.jesvardo.app.R
import com.jesvardo.app.ui.ActivityIntro
import kotlinx.android.synthetic.main.activity_intro.view.*

class FragmentIntro : Fragment() {

    private lateinit var createdView: View

    private var intPosition : Int = 0

    lateinit var textViewHeader : TextView
    lateinit var textViewDetails : TextView

    lateinit var imageViewMain : ImageView

    lateinit var baseActivity: ActivityIntro

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        intPosition = arguments!!.getInt("position")
        baseActivity = activity as ActivityIntro
    }

    companion object {
        fun newInstance(position: Int): Fragment {
            val fragment = FragmentIntro()
            val args = Bundle()
            args.putInt("position", position)
            fragment.arguments = args
            return fragment
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        createdView = inflater.inflate(R.layout.fragment_intro, container, false)

        textViewHeader = createdView.findViewById<TextView>(R.id.fragment_intro_txt_header)
        textViewDetails = createdView.findViewById<TextView>(R.id.fragment_intro_txt_details)

        imageViewMain = createdView.findViewById<ImageView>(R.id.fragment_intro_img_main)


        when (intPosition) {
            0 -> {
                textViewHeader.text = "Let's Travel"
                textViewDetails.text = "Plan adventures trip with us to make your weekend and holiday more excited and refreshing."
                imageViewMain.background = ContextCompat.getDrawable(baseActivity, R.drawable.onboarding_vector)
            }

            1 -> {
                textViewHeader.text = "Let's Start"
                textViewDetails.text = "The JasVardo was used not to transport goods but, as mobile homes, drawn along by horses. It was an extension of their selves. It became emblematic of a curious, undaunted, and adventurous spirit."
                imageViewMain.background = ContextCompat.getDrawable(baseActivity, R.drawable.onboarding_vector_second)
            }
        }

        return createdView
    }
}