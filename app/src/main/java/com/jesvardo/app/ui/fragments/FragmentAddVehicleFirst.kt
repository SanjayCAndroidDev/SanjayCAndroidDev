package com.jesvardo.app.ui.fragments

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.*
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.jesvardo.app.R
import com.jesvardo.app.network.model.ModelResponseGetVehicleType
import com.jesvardo.app.network.model.ModelResponseVehicleModel
import com.jesvardo.app.network.model.ModelresponseVehicleMakes
import com.jesvardo.app.ui.ActivityAddVehicle
import com.jesvardo.app.ui.ActivityAddVehicleDetails
import com.jesvardo.app.ui.ActivityIntro
import com.jesvardo.app.ui.viewmodels.AddVehicleViewModel
import com.jesvardo.app.utils.listeners.setSafeOnClickListener
import kotlinx.android.synthetic.main.activity_intro.view.*

class FragmentAddVehicleFirst : Fragment() {

    private lateinit var createdView: View

    private var intPosition : Int = 0

    lateinit var baseActivity: ActivityAddVehicleDetails

    lateinit var textContinue: TextView

    lateinit var spinnerVehicaleType: Spinner
    lateinit var spinnerManufacture: Spinner
    lateinit var spinnerModel: Spinner
    lateinit var spinnerYear: Spinner

    private lateinit var addVehicleViewModel: AddVehicleViewModel

    var listGetVehicleType: ArrayList<ModelResponseGetVehicleType> = ArrayList()
    var listGetvehicleMakes: ArrayList<ModelresponseVehicleMakes> = ArrayList()
    var listGetvehicleModel: ArrayList<ModelResponseVehicleModel> = ArrayList()

    var adapter: CustomAdapter? = null
    var adapterVehicleMakes: CustomAdapterVehicleMake? = null
    var adapterVehicleModel : CustomAdapterVehicleModel? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        intPosition = arguments!!.getInt("position")
        baseActivity = activity as ActivityAddVehicleDetails
        baseActivity.window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);

        addVehicleViewModel = ViewModelProviders.of(this).get(AddVehicleViewModel::class.java)

    }

    companion object {
        fun newInstance(position: Int): Fragment {
            val fragment = FragmentAddVehicleFirst()
            val args = Bundle()
            args.putInt("position", position)
            fragment.arguments = args
            return fragment
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        createdView = inflater.inflate(R.layout.fragment_add_vehicle_details_first, container, false)

        textContinue = createdView.findViewById<TextView>(R.id.fragment_add_vehicle_detail_first_txt_continue)

        spinnerVehicaleType = createdView.findViewById(R.id.fragment_add_vehicle_detail_first_spinner_type)
        spinnerManufacture = createdView.findViewById(R.id.fragment_add_vehicle_detail_first_spinner_manufacturer)
        spinnerModel = createdView.findViewById(R.id.fragment_add_vehicle_detail_first_spinner_model)
        spinnerYear = createdView.findViewById(R.id.fragment_add_vehicle_detail_first_spinner_year)

        textContinue.setSafeOnClickListener {
            baseActivity.activityAddVehicleDetailsBinding.activityAddVehicleDetailViewPager.setCurrentItem(1, true)
        }

        return createdView
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        onSubScripbe()
    }

    fun onSubScripbe() {
        addVehicleViewModel.getVehicleType()


        addVehicleViewModel.vehicleTypeSuccess.observe(viewLifecycleOwner, Observer {
            if (it) {
                listGetVehicleType = addVehicleViewModel.listVehicleType
                adapter = CustomAdapter(baseActivity, listGetVehicleType)
                spinnerVehicaleType.adapter = adapter
            }
        })

        addVehicleViewModel.listVehicleMakes.observe(viewLifecycleOwner, Observer {
            listGetvehicleMakes.clear()
            listGetvehicleMakes.add(it)
            adapterVehicleMakes = CustomAdapterVehicleMake(baseActivity, listGetvehicleMakes)
            spinnerManufacture.adapter = adapterVehicleMakes
        })

        addVehicleViewModel.vehicleModelSuccess.observe(viewLifecycleOwner, Observer {
            if (it) {
                listGetvehicleModel = addVehicleViewModel.listVehicleModel
                adapterVehicleModel = CustomAdapterVehicleModel(baseActivity, listGetvehicleModel)
                spinnerModel.adapter = adapterVehicleModel
            }
        })

        addVehicleViewModel.strError.observe(viewLifecycleOwner, Observer {
            if (it != "") {
                baseActivity.showMessage(it)
            }
        })

        addVehicleViewModel.isShowProgress.observe(viewLifecycleOwner, Observer {
            if (it != null) {
                if (it) {
                    baseActivity.showProgress()
                } else {
                    baseActivity.hideProgress()
                }
            }
        })

        spinnerVehicaleType?.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
            override fun onNothingSelected(parent: AdapterView<*>?) {

            }

            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                addVehicleViewModel.getVehicleMakes(listGetVehicleType[position].id.toString())
            }
        }


        spinnerManufacture?.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
            override fun onNothingSelected(parent: AdapterView<*>?) {

            }

            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                addVehicleViewModel.getVehicleModel(listGetvehicleMakes[position].id.toString())
            }
        }

        val listYears = resources.getStringArray(R.array.years)
        var yearsAdapter: ArrayAdapter<Any?> = ArrayAdapter<Any?>(baseActivity, R.layout.spinner_list, listYears)
        yearsAdapter.setDropDownViewResource(R.layout.spinner_list)
        spinnerYear.adapter = yearsAdapter
    }

    class CustomAdapter(
        private val mContext: Context,
        private val arrayList: ArrayList<ModelResponseGetVehicleType>
    ) : BaseAdapter() {
        override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
            val mLayoutInflater = (mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater)
            @SuppressLint("ViewHolder")
            val rowView = mLayoutInflater.inflate(R.layout.raw_spinner_item, parent, false)
            val mTextViewItem = rowView.findViewById<TextView>(R.id.raw_spinner_item_textview)
            mTextViewItem.text = arrayList[position].label
            return rowView
        }

        override fun getItem(position: Int): String {
            return arrayList[position].label
        }

        override fun getItemId(p0: Int): Long {
            return 0
        }

        override fun getCount(): Int {
            return arrayList.size
        }
    }

    class CustomAdapterVehicleMake(
        private val mContext: Context,
        private val arrayList: ArrayList<ModelresponseVehicleMakes>
    ) : BaseAdapter() {
        override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
            val mLayoutInflater = (mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater)
            @SuppressLint("ViewHolder")
            val rowView = mLayoutInflater.inflate(R.layout.raw_spinner_item, parent, false)
            val mTextViewItem = rowView.findViewById<TextView>(R.id.raw_spinner_item_textview)
            mTextViewItem.text = arrayList[position].name
            return rowView
        }

        override fun getItem(position: Int): String {
            return arrayList[position].name
        }

        override fun getItemId(p0: Int): Long {
            return 0
        }

        override fun getCount(): Int {
            return arrayList.size
        }
    }

    class CustomAdapterVehicleModel(
        private val mContext: Context,
        private val arrayList: ArrayList<ModelResponseVehicleModel>
    ) : BaseAdapter() {
        override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
            val mLayoutInflater = (mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater)
            @SuppressLint("ViewHolder")
            val rowView = mLayoutInflater.inflate(R.layout.raw_spinner_item, parent, false)
            val mTextViewItem = rowView.findViewById<TextView>(R.id.raw_spinner_item_textview)
            mTextViewItem.text = arrayList[position].name
            return rowView
        }

        override fun getItem(position: Int): String {
            return arrayList[position].name
        }

        override fun getItemId(p0: Int): Long {
            return 0
        }

        override fun getCount(): Int {
            return arrayList.size
        }
    }
}