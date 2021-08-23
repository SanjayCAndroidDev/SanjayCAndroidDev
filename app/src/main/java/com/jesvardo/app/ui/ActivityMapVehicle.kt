package com.jesvardo.app.ui

import android.app.Activity
import android.content.Intent
import android.content.IntentSender
import android.content.pm.PackageManager
import android.location.Location
import android.os.Bundle
import android.view.View
import androidx.core.app.ActivityCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.google.android.gms.common.api.ResolvableApiException
import com.google.android.gms.location.*
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.*
import com.jesvardo.app.R
import com.jesvardo.app.base.BaseActivity
import com.jesvardo.app.databinding.ActivityMapVehicleBinding
import com.jesvardo.app.network.model.ModelResponseVehicleList
import com.jesvardo.app.ui.viewmodels.DashboardViewModel
import com.jesvardo.app.utils.listeners.setSafeOnClickListener
import com.jesvardo.app.utils.loadImage
import kotlinx.android.synthetic.main.activity_map_vehicle.*
import kotlinx.android.synthetic.main.base_activity.*


class ActivityMapVehicle : BaseActivity() , OnMapReadyCallback{

    private lateinit var activityMapVehicleBinding: ActivityMapVehicleBinding

    private lateinit var mGoogleMap: GoogleMap
    private lateinit var fusedLocationClient: FusedLocationProviderClient

    private lateinit var locationCallback: LocationCallback
    private lateinit var locationRequest: LocationRequest
    private var locationUpdateState = false

    private lateinit var lastLocation: Location

    lateinit var dashboardViewModel: DashboardViewModel

    lateinit var listVehicle: ArrayList<ModelResponseVehicleList>

    val builder = LatLngBounds.Builder()

    companion object {
        private const val LOCATION_PERMISSION_REQUEST_CODE = 1
        private const val REQUEST_CHECK_SETTINGS = 2
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activityMapVehicleBinding = putContentView(R.layout.activity_map_vehicle) as ActivityMapVehicleBinding

        dashboardViewModel = ViewModelProviders.of(this).get(DashboardViewModel::class.java)

        initToolbar()
        base_activity_toolbar.visibility = View.GONE

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this@ActivityMapVehicle)

        var supportMapFragment: SupportMapFragment = supportFragmentManager.findFragmentById(R.id.activity_map_vehicle_map) as SupportMapFragment
        supportMapFragment.getMapAsync(this)

        locationCallback = object : LocationCallback() {
            override fun onLocationResult(location: LocationResult) {
                super.onLocationResult(location)
            }
        }

        activity_map_vehicle_img_back.setSafeOnClickListener {
            onBackPressed()
        }

        createLocationRequest()

        dashboardViewModel.vehicleListSuccess.observe(this, Observer {
            if (it) {
                listVehicle = dashboardViewModel.listVehicle

                for ((itemPostion, vehicalDetail) in listVehicle.withIndex()) {
                    val currentLatLng = LatLng(vehicalDetail.listing.lat, vehicalDetail.listing.long)
                    putMarkerOnMap(currentLatLng, itemPostion, vehicalDetail)
                }

                val bounds = builder.build()
                val padding = 0 // offset from edges of the map in pixels
                val cu = CameraUpdateFactory.newLatLngBounds(bounds, padding)
                mGoogleMap.moveCamera(cu)
                mGoogleMap.animateCamera(cu)
            }
        })

        dashboardViewModel.isShowProgress.observe(this, Observer {
            if (it != null) {
                if (it) {
                    showProgress()
                } else {
                    hideProgress()
                }
            }
        })

        var hashMap: HashMap<String, String> = HashMap<String, String>()
        hashMap["transmission"] = "automatic"
        hashMap["fuel_type"] = "diesel"
        dashboardViewModel.getVehicleList(hashMap)

    }

    private fun startLocationUpdates() {
        if (ActivityCompat.checkSelfPermission(this,
                android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION),
                LOCATION_PERMISSION_REQUEST_CODE)
            return
        }

        fusedLocationClient.requestLocationUpdates(locationRequest, locationCallback, null /* Looper */)

        fusedLocationClient.lastLocation.addOnSuccessListener(this) { location ->
            // Got last known location. In some rare situations this can be null.
            if (location != null) {
                lastLocation = location
            }
        }
    }

    private fun createLocationRequest() {
        locationRequest = LocationRequest()
        locationRequest.interval = 10000
        locationRequest.fastestInterval = 5000
        locationRequest.priority = LocationRequest.PRIORITY_HIGH_ACCURACY

        val builder = LocationSettingsRequest.Builder().addLocationRequest(locationRequest)

        val client = LocationServices.getSettingsClient(this)
        val task = client.checkLocationSettings(builder.build())

        task.addOnSuccessListener {
            locationUpdateState = true
            startLocationUpdates()
        }
        task.addOnFailureListener { e ->
            if (e is ResolvableApiException) {
                // Location settings are not satisfied, but this can be fixed
                // by showing the user a dialog.
                try {
                    // Show the dialog by calling startResolutionForResult(),
                    // and check the result in onActivityResult().
                    e.startResolutionForResult(this@ActivityMapVehicle, REQUEST_CHECK_SETTINGS)
                } catch (sendEx: IntentSender.SendIntentException) {
                    // Ignore the error.
                }
            }
        }
    }

    override fun onMapReady(googleMap: GoogleMap?) {

        this.mGoogleMap = googleMap!!

        try {
            mGoogleMap.isMyLocationEnabled = true
        } catch (e: SecurityException) {
            e.printStackTrace()
        }

        mGoogleMap.setOnMarkerClickListener { marker ->
            val position = marker.tag as Int
            showBottomView(position)
            true
        }
    }

    private fun putMarkerOnMap(mLatLng: LatLng, itemPosition : Int, vehicalDetail: ModelResponseVehicleList) {

        val markerOptions = MarkerOptions()
        markerOptions.position(mLatLng)
        markerOptions.title(vehicalDetail.listing.name)

        var icon: BitmapDescriptor? = null
        icon = BitmapDescriptorFactory.fromResource(R.drawable.pin)

        markerOptions.icon(icon)
        markerOptions.snippet("" + itemPosition)

        val marker = mGoogleMap.addMarker(markerOptions)
        marker.tag = itemPosition
        builder.include(marker.position);
    }

    private fun showBottomView(intPosition: Int) {
        activity_map_vehicle_card_bottom.visibility = View.VISIBLE

        loadImage(activityMapVehicleBinding.activityMapVehicleImgMain, listVehicle[intPosition].listing.images[0].url)

        activityMapVehicleBinding.activityMapVehicleTxtName.text = listVehicle[intPosition].listing.name
        activityMapVehicleBinding.activityMapVehicleTxtPrice.text = listVehicle[intPosition].listing.country +" $"+listVehicle[intPosition].listing.nightly_rate.toString()

        activity_map_vehicle_card_bottom.setSafeOnClickListener {
            val intent = Intent(it.context, ActivityVehicleDetails::class.java)
            intent.putExtra("id", listVehicle[intPosition].id)
            it.context.startActivity(intent)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_CHECK_SETTINGS) {
            if (resultCode == Activity.RESULT_OK) {
                locationUpdateState = true
                startLocationUpdates()
            }
        }
    }

    override fun onPause() {
        super.onPause()
        fusedLocationClient.removeLocationUpdates(locationCallback)
    }

    public override fun onResume() {
        super.onResume()
        if (!locationUpdateState) {
            startLocationUpdates()
        }
    }


}