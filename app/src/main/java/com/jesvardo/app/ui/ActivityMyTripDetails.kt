package com.jesvardo.app.ui

import android.Manifest
import android.app.Activity
import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.content.IntentSender
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.drawable.GradientDrawable
import android.location.Address
import android.location.Geocoder
import android.location.Location
import android.location.LocationManager
import android.os.Bundle
import android.text.Html
import android.util.DisplayMetrics
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager.widget.PagerAdapter
import androidx.viewpager.widget.ViewPager
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.common.api.ResolvableApiException
import com.google.android.gms.location.*
import com.google.android.gms.tasks.CancellationTokenSource
import com.google.android.gms.tasks.Task
import com.jesvardo.app.R
import com.jesvardo.app.base.BaseActivity
import com.jesvardo.app.databinding.ActivityMyTripBinding
import com.jesvardo.app.databinding.ActivityTripDetailsBinding
import com.jesvardo.app.network.model.ModelResponseMyBookingList
import com.jesvardo.app.ui.fragments.FragmentAddVehicleFourth
import com.jesvardo.app.ui.viewmodels.MyTripViewModel
import com.jesvardo.app.utils.listeners.setSafeOnClickListener
import com.jesvardo.app.utils.loadImage
import com.mindorks.paracamera.Camera
import kotlinx.android.synthetic.main.base_activity.*
import java.io.ByteArrayOutputStream
import java.text.DecimalFormat
import java.text.SimpleDateFormat
import java.util.*

class ActivityMyTripDetails : BaseActivity() {

    private lateinit var activityTripDetailsBinding: ActivityTripDetailsBinding
    private lateinit var myTripViewModel: MyTripViewModel

    private var fusedLocationClient: FusedLocationProviderClient? = null
    private val cancellationTokenSource: CancellationTokenSource = CancellationTokenSource()

    var scale = 0f
    private var DW = 0

    var dFormat: DecimalFormat? = null

    var address_line_1: String = ""
    var city: String = ""
    var state: String = ""
    var country: String = ""
    var Latitude: String = ""
    var arrayListImg = java.util.ArrayList<String>()
    var Longitude: String = ""

    private var tripID: Int = 0

    val dateFormat = SimpleDateFormat("yyyy-MM-dd")
    val dateFormatConvert = SimpleDateFormat("dd MMM yyyy")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        initToolbar()
        base_activity_toolbar.visibility = View.VISIBLE
        base_activity_toolbar_textview_title.text = resources.getString(R.string.text_title_my_trips_details)
        base_activity_toolbar_imageview_back.visibility = View.VISIBLE

        base_activity_toolbar_imageview_back.setSafeOnClickListener {
            onBackPressed()
        }

        activityTripDetailsBinding = putContentView(R.layout.activity_trip_details) as ActivityTripDetailsBinding
        myTripViewModel = ViewModelProviders.of(this).get(MyTripViewModel::class.java)

        dFormat = DecimalFormat("#.######")
        val displayMetrics = DisplayMetrics()
        windowManager.defaultDisplay.getMetrics(displayMetrics)
        DW = displayMetrics.heightPixels
        scale = displayMetrics.scaledDensity
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this@ActivityMyTripDetails);
        requestCurrentLocation()

        activityTripDetailsBinding.rlAddPhotos.setSafeOnClickListener {
            startgps()
        }

        activityTripDetailsBinding.activityTripDetailsTxtViewImages.setSafeOnClickListener {
            if (arrayListImg.size > 0) {
                showDateSelectionDialog()
            } else {
                Toast.makeText(
                    this@ActivityMyTripDetails,
                    "Please capture photo first",
                    Toast.LENGTH_LONG
                ).show()
            }
        }

        tripID = intent.getIntExtra("id", 0)
        myTripViewModel.getMyTripDetails(tripID.toString())

        myTripViewModel.strError.observe(this, androidx.lifecycle.Observer {
            if (it != "") {
                showMessage(it)
            }
        })

        myTripViewModel.isShowProgress.observe(this, androidx.lifecycle.Observer {
            if (it != null) {
                if (it) {
                    showProgress()
                } else {
                    hideProgress()
                }
            }
        })

        myTripViewModel.myTripsDetails.observe(this, androidx.lifecycle.Observer {
            setData(it)
        })
    }

    private fun setData(myTripsDetails: ModelResponseMyBookingList) {

        if(myTripsDetails != null) {
            if (myTripsDetails.listing != null && myTripsDetails.listing.images != null
                && myTripsDetails.listing.images.isNotEmpty()) {
                loadImage(activityTripDetailsBinding.activityTripDetailsImgMain, myTripsDetails.listing.images[0].url)
            }

            if (myTripsDetails.listing != null && myTripsDetails.listing.name != null)
                activityTripDetailsBinding.activityTripDetailsTxtName.text = myTripsDetails.listing.name

            if (myTripsDetails.listing != null && myTripsDetails.listing.country != null
                && myTripsDetails.price_quote != null && myTripsDetails.price_quote.total != null) {
                activityTripDetailsBinding.activityTripDetailsTxtPrice.text = myTripsDetails.listing.country + " $" + myTripsDetails.price_quote.total.toString()
            }

            if(myTripsDetails.status != null) {
                activityTripDetailsBinding.activityTripDetailsTxtStatus.text = myTripsDetails.status
            }

            if(myTripsDetails.from_date != null && myTripsDetails.to_date != null) {
                val fromDate = dateFormat.parse(myTripsDetails.from_date)
                val toDate = dateFormat.parse(myTripsDetails.to_date)
                activityTripDetailsBinding.activityTripDetailsTxtDate.text = dateFormatConvert.format(fromDate) +" to " + dateFormatConvert.format(toDate)
            }

            if(myTripsDetails.price_quote != null && myTripsDetails.price_quote.min_rental_duration_days != null) {
                activityTripDetailsBinding.activityTripDetailsTxtDuration.text = myTripsDetails.price_quote.duration_in_days.toString() + " days"
            }

            if(myTripsDetails.price_quote != null && myTripsDetails.price_quote.min_rental_duration_days != null) {
                activityTripDetailsBinding.activityTripDetailsTxtDuration.text = myTripsDetails.price_quote.duration_in_days.toString() + " days"
            }

            if(myTripsDetails.price_quote != null && myTripsDetails.price_quote.nightly_rate != null) {
                activityTripDetailsBinding.activityTripDetailsTxtNightlyRate.text = "$"+myTripsDetails.price_quote.nightly_rate.toString()
            }

            if(myTripsDetails.price_quote != null && myTripsDetails.price_quote.security_deposit != null) {
                activityTripDetailsBinding.activityTripDetailsTxtSecurityDeposit.text = "$"+myTripsDetails.price_quote.security_deposit.toString()
            }

            if(myTripsDetails.price_quote != null && myTripsDetails.price_quote.service_fee != null) {
                activityTripDetailsBinding.activityTripDetailsTxtServiceCharge.text = "$"+myTripsDetails.price_quote.service_fee.toString()
            }

            if(myTripsDetails.price_quote != null && myTripsDetails.price_quote.total != null) {
                activityTripDetailsBinding.activityTripDetailsTxtBookingAmount.text = "$"+myTripsDetails.price_quote.total.toString()
            }
        }
    }

    private fun requestCurrentLocation() {

        // Request permission
        if (ActivityCompat.checkSelfPermission(
                this@ActivityMyTripDetails,
                Manifest.permission.ACCESS_FINE_LOCATION
            )
            == PackageManager.PERMISSION_GRANTED
        ) {
            // Main code
            val currentLocationTask: Task<Location> = fusedLocationClient!!.getCurrentLocation(
                LocationRequest.PRIORITY_HIGH_ACCURACY,
                cancellationTokenSource.token
            )

            currentLocationTask.addOnCompleteListener { task ->

                var result = ""
                if (task.isSuccessful) {
                    // Task completed successfully
                    val location: Location = task.result
                    if (location != null) {
                        val mLatitude = location.latitude
                        val mLongitude = location.longitude


                        Latitude = mLatitude.toString()
                        Longitude = mLongitude.toString()

                        val geocoder = Geocoder(this@ActivityMyTripDetails, Locale.getDefault())
                        try {
                            val addresses: List<Address>? =
                                geocoder.getFromLocation(mLatitude, mLongitude, 1)
                            if (addresses != null && addresses.isNotEmpty() && addresses.isNotEmpty()) {
                                val mAddress_line_1: String =
                                    addresses[0].getAddressLine(0) // If any additional address line present than only, check with max available address lines by getMaxAddressLineIndex()
                                val mCity: String = addresses[0].locality
                                val mState: String = addresses[0].adminArea
                                val mCountry: String = addresses[0].countryName

                                if (mAddress_line_1 != null) {
                                    address_line_1 = mAddress_line_1
                                }
                                if (mCity != null) {
                                    city = mCity
                                }
                                if (mState != null) {
                                    state = mState
                                }
                                if (mCountry != null) {
                                    country = mCountry
                                }
                            }
                        } catch (e: java.lang.Exception) {
                            e.printStackTrace()
                        }
                    }
                } else {
                    // Task failed with an exception
                    val exception = task.exception
                    "Exception thrown: $exception"
                }
            }
        } else {
        }
    }

    private fun drawStamp(src: Bitmap?): Bitmap? {
        if (src != null) {
            val locationManager =
                context!!.getSystemService(AppCompatActivity.LOCATION_SERVICE) as LocationManager
            val statusOfGPS = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)
            if (!statusOfGPS) {
            }
            val p = Paint(Paint.ANTI_ALIAS_FLAG)
            val w = src.width
            val h = src.height
            var bitmap: Bitmap? = null
            bitmap?.recycle()
            bitmap = Bitmap.createBitmap(w, h, src.config)
            val canvas = Canvas(bitmap)
            canvas.drawBitmap(src, 0f, 0f, null)
            System.gc()
            src.recycle()
            return setbitmap1(canvas, p, bitmap)
        }
        return null
    }

    private fun setbitmap1(canvas: Canvas, p: Paint, bitmap: Bitmap?): Bitmap? {
        try {
            val mInflater = getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater?
            val viewLayout: View = mInflater!!.inflate(R.layout.stamp_view, null)
            val tv_address_line_1 = viewLayout.findViewById<TextView>(R.id.tv_address_line_1)
            val tv_address = viewLayout.findViewById<TextView>(R.id.tv_address)
            val li_main_stamp_lay = viewLayout.findViewById<RelativeLayout>(R.id.li_main_stamp_lay)
            val li_stamp = viewLayout.findViewById<LinearLayout>(R.id.li_stamp)

            val size_24 = (bitmap!!.width * (12 * scale) / DW).toInt()
            val size_36 = (bitmap.width * (18 * scale) / DW).toInt()
            val size_32 = (bitmap.width * (16 * scale) / DW).toInt()
            val size_8 = (bitmap.width * (4 * scale) / DW).toInt()
            var size_400 = (bitmap.width * (200 * scale) / DW).toInt()
            var size_230 = (bitmap.width * (180 * scale) / DW).toInt()
            if (bitmap.width > bitmap.height) {
                size_400 = ((bitmap.width * (170 * scale) / DW).toInt())
                size_230 = ((bitmap.width * (120 * scale) / DW).toInt())
            }

            val img_lp = LinearLayout.LayoutParams(size_400, size_400)
            img_lp.setMargins(0, 0, size_32, 0)
            li_main_stamp_lay.setPadding(size_36, size_36, size_36, size_36)

            tv_address_line_1.maxLines = 10
            val width: Int

            width = canvas.getWidth() - 2 * size_36
            li_stamp.layoutParams.height = size_230
            li_stamp.setPadding(size_24, size_24, size_24, size_24)

            val bg_background = li_stamp.background.current as GradientDrawable
            bg_background.cornerRadius = size_8.toFloat()
            val lp = LinearLayout.LayoutParams(width, li_stamp.layoutParams.height)
            li_stamp.layoutParams = lp

            var address = ""

            if (city != null && !city.isEmpty()) {
                address += "$city, "
            }

            if (state != null && !state.isEmpty()) {
                address += "$state, "
            }

            if (country != null && !country.isEmpty()) {
                address += country
            }

            if (address != null) {
                if (address.endsWith(", ")) {
                    address = address.substring(0, address.length - 2)
                }
            }

            var final_text = address_line_1


            var latLong: String = converlatLog(Latitude, Longitude)
            if (Latitude != null) {
                if (latLong.length > 55) {
                    val str = latLong.split(" Long".toRegex()).toTypedArray()
                    latLong = (str[0] + "<br/>"
                            + "Long " + str[1])
                }
            }

            final_text = (final_text + "<br/>" + latLong)

            val currentDateFormat: String = setDateTimeFormat("dd/MM/YYYY hh:mm:ss a").toString()
            final_text = "$final_text<br/>$currentDateFormat"

            tv_address_line_1.setText(Html.fromHtml(final_text))
            tv_address.setText(address)

            val stampBitmap: Bitmap = loadBitmapFromView(viewLayout)!!
            var location_y = 0f
            var location_x = 0f


            //location_x = (canvas.getWidth() / 2) - (stampBitmap.getWidth() / 2);
            if (bitmap != null && stampBitmap != null) {
                location_y = canvas.getHeight() - (location_y + stampBitmap.getHeight())
                location_x = (canvas.getWidth() / 2 - stampBitmap.getWidth() / 2).toFloat()
                canvas.drawBitmap(stampBitmap, location_x, location_y, p)
                stampBitmap.recycle()
                System.gc()
            }
            return bitmap
        } catch (e: OutOfMemoryError) {
            e.printStackTrace()
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return null
    }

    private fun converlatLog(latitude: String, longitude: String): String {
        var d_lat: Double = latitude.toDouble()
        var d_lon: Double = longitude.toDouble()

        var latitude = Location.convert(d_lat, Location.FORMAT_SECONDS)
        latitude = replaceDelimiters(latitude, 8)
        val latCardinal = if (d_lat >= 0) 'N' else 'S'
        latitude = "$latCardinal $latitude"

        var longitude = Location.convert(d_lon, Location.FORMAT_SECONDS)
        longitude = replaceDelimiters(longitude.toString(), 8)
        val lonCardinal = if (d_lon >= 0) 'E' else 'W'
        longitude = "$lonCardinal $longitude"
        var output_string = "Lat $latitude Long $longitude"
        return output_string
    }

    private fun replaceDelimiters(str: String, decimalPlace: Int): String {
        var str = str
        str = str.replaceFirst(":".toRegex(), "° ")
        str = str.replaceFirst(":".toRegex(), "' ")
        val pointIndex = str.indexOf(".")
        val endIndex = pointIndex + 1 + decimalPlace
        if (endIndex < str.length) {
            str = str.substring(0, endIndex)
        }
        str = str + "\""
        return str
    }

    fun loadBitmapFromView(viewLayout: View): Bitmap? {
        try {
            viewLayout.measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED)
            val bitmap = Bitmap.createBitmap(
                viewLayout.measuredWidth,
                viewLayout.measuredHeight,
                Bitmap.Config.ARGB_8888
            )
            val canvas_1 = Canvas(bitmap)
            viewLayout.layout(0, 0, viewLayout.measuredWidth, viewLayout.measuredHeight)
            viewLayout.draw(canvas_1)
            System.gc()
            return bitmap
        } catch (e: OutOfMemoryError) {
            e.printStackTrace()
        }
        return null
    }

    fun setDateTimeFormat(format: String): String? {
        val c = Calendar.getInstance()
        val result: String
        val df = SimpleDateFormat(format, Locale.getDefault())
        result = df.format(c.time)

        return result.replace("am", "AM").replace("pm", "PM")
    }

    companion object {
        @JvmStatic
        var PARA_CAM: Camera? = null
    }

    private fun captureParaImage(activity: ActivityMyTripDetails?): Camera? {
        return Camera.Builder()
            .resetToCorrectOrientation(true) // it will rotate the camera bitmap to the correct orientation from meta data
            .setTakePhotoRequestCode(1)
            .setDirectory("pics")
            .setName("temp_photo")
            .setImageFormat(Camera.IMAGE_JPEG)
            .setCompression(75)
            .setTakePhotoRequestCode(Camera.REQUEST_TAKE_PHOTO)
            .setImageHeight(1000) // it will try to achieve this height as close as possible maintaining the aspect ratio;
            .build(activity)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK && requestCode == Camera.REQUEST_TAKE_PHOTO) {

            var bitmap = PARA_CAM!!.cameraBitmap

            if (bitmap != null) {
                val bitmap1: Bitmap = drawStamp(bitmap)!!
                val byteArrayOutputStream = ByteArrayOutputStream()
                bitmap1.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream)
                val byteArray: ByteArray = byteArrayOutputStream.toByteArray()
                val encoded: String =
                    android.util.Base64.encodeToString(byteArray, android.util.Base64.DEFAULT)
                arrayListImg.add(encoded)

                activityTripDetailsBinding.txtTotalPhotos.text = arrayListImg.size.toString()
            }
        }
    }

    private fun startgps() {
        val locationManager =
            context?.getSystemService(AppCompatActivity.LOCATION_SERVICE) as LocationManager
        val statusOfGPS = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)

        if (!statusOfGPS) {
            val locationRequest = LocationRequest.create()
            locationRequest.priority = LocationRequest.PRIORITY_HIGH_ACCURACY
            locationRequest.interval = (30 * 1000).toLong()
            locationRequest.fastestInterval = (5 * 1000).toLong()
            val builder = LocationSettingsRequest.Builder()
                .addLocationRequest(locationRequest)
            builder.setAlwaysShow(true)

            val result =
                LocationServices.getSettingsClient(context).checkLocationSettings(builder.build())
            result.addOnCompleteListener { task ->
                try {
                    val response = task.getResult(ApiException::class.java)
                } catch (exception: ApiException) {
                    when (exception.statusCode) {
                        LocationSettingsStatusCodes.RESOLUTION_REQUIRED -> try {
                            val resolvable = exception as ResolvableApiException
                            resolvable.startResolutionForResult(
                                this@ActivityMyTripDetails,
                                LocationRequest.PRIORITY_HIGH_ACCURACY
                            )
                        } catch (e: IntentSender.SendIntentException) {
                        } catch (e: ClassCastException) {
                        }
                        LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE -> {
                        }
                    }
                }
            }
        } else {
            PARA_CAM = captureParaImage(this@ActivityMyTripDetails)
            PARA_CAM?.takePicture()
        }
    }

    private fun showDateSelectionDialog() {
        val dialog = Dialog(this@ActivityMyTripDetails)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.window?.setBackgroundDrawableResource(R.drawable.shape_background_default_popup)
        dialog.setCancelable(false)
        dialog.setContentView(R.layout.dialog_image_view)

        val viewPagerImages = dialog.findViewById(R.id.dialog_image_viewer_viewpager) as ViewPager

        val adapter = ImageSliderAdapter(this@ActivityMyTripDetails, arrayListImg)
        viewPagerImages.adapter = adapter

        val imgClose = dialog.findViewById(R.id.dialog_image_viewer_img_close) as ImageView

        imgClose.setSafeOnClickListener {
            dialog.dismiss()
        }

        dialog.show()
    }

    class ImageSliderAdapter(private val context: Context, val arrayListImg: ArrayList<String>) :
        PagerAdapter() {

        lateinit var layoutInflater: LayoutInflater

        override fun isViewFromObject(view: View, `object`: Any): Boolean {
            return view === `object` as RelativeLayout
        }

        override fun getCount(): Int {
            return arrayListImg.size
        }

        override fun instantiateItem(container: ViewGroup, position: Int): Any {

            layoutInflater = LayoutInflater.from(context)
            var view = layoutInflater.inflate(R.layout.raw_image_list, container, false)
            val img = view.findViewById<ImageView>(R.id.raw_dash_image_list_img_main)

            img.setImageBitmap(convertBase64ToBitmap(arrayListImg[position]))

            container.addView(view, 0)
            return view
        }

        override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {

            container.removeView(`object` as RelativeLayout)
        }

        private fun convertBase64ToBitmap(b64: String): Bitmap? {
            val imageAsBytes: ByteArray =
                android.util.Base64.decode(b64.toByteArray(), android.util.Base64.DEFAULT)
            return BitmapFactory.decodeByteArray(imageAsBytes, 0, imageAsBytes.size)
        }

    }
}