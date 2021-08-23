package com.jesvardo.app.base

import android.app.Application
import android.content.pm.PackageManager
import android.graphics.Typeface
import androidx.lifecycle.MutableLiveData
import com.facebook.FacebookSdk
import com.jesvardo.app.injection.component.AppComponent
import com.jesvardo.app.injection.component.DaggerAppComponent
import com.jesvardo.app.injection.modules.AppModule
import com.jesvardo.app.injection.modules.NetworkModule
import com.jesvardo.app.utils.listeners.TypeFactory

class MyApplication : Application() {

    companion object {
        var mTypeFactory: TypeFactory? = null
        lateinit var application: Application
        lateinit var appComponent: AppComponent
        var isInternetAvailable: Boolean = false
        var mStringVersion: String = ""
        var mStringForgotPasswordEmail: String = ""
        var dialogDefaultButtonClick: MutableLiveData<Int> = MutableLiveData(0)
        var dialogForgotPasswordButtonClick: MutableLiveData<Int> = MutableLiveData(0)

        fun getTypeFace(type: Int): Typeface? {
            return when (type) {
                Constants.REGULAR -> mTypeFactory!!.regular
                Constants.MEDIUM -> mTypeFactory!!.medium
                Constants.SEMIBOLD -> mTypeFactory!!.semibold

                else -> mTypeFactory!!.regular
            }
        }
    }

    override fun onCreate() {
        super.onCreate()

        application = this
        mTypeFactory = TypeFactory(this)
        FacebookSdk.sdkInitialize(applicationContext)

        appComponent = DaggerAppComponent.builder()
            .networkModule(NetworkModule)
            .appModule(AppModule(application))
            .build()

        appComponent.inject(this)

        try {
            val pInfo = this.packageManager.getPackageInfo(packageName, 0)
            mStringVersion = pInfo.versionName
        } catch (e: PackageManager.NameNotFoundException) {
            e.printStackTrace()
        }

    }

    interface Constants {
        companion object {
            const val REGULAR = 1
            const val MEDIUM = 2
            const val SEMIBOLD = 3
        }
    }

}