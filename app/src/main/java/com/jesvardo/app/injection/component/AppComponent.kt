package com.jesvardo.app.injection.component

import com.jesvardo.app.base.BaseActivity
import com.jesvardo.app.base.BaseViewModel
import com.jesvardo.app.base.MyApplication
import com.jesvardo.app.injection.modules.AppModule
import com.jesvardo.app.injection.modules.NetworkModule
import com.jesvardo.app.utils.AppPreferences
import com.jesvardo.app.utils.listeners.GpsStatusListener
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = [NetworkModule::class, AppModule::class])
interface AppComponent {

    fun inject(myApplication: MyApplication)
    fun inject(baseActivity: BaseActivity)
    fun inject(baseViewModel: BaseViewModel)
    fun inject(gpsStatusListener: GpsStatusListener)
    fun inject(appPreferences: AppPreferences)

    @Component.Builder
    interface Builder {
        fun build(): AppComponent
        fun networkModule(networkModule: NetworkModule): Builder
        fun appModule(appModule: AppModule): Builder
    }

}