package com.jesvardo.app.injection.modules

import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import com.jesvardo.app.R
import com.jesvardo.app.utils.AppDialogUtils
import com.jesvardo.app.utils.AppPreferences
import com.jesvardo.app.utils.AppSocialLoginUtils
import com.jesvardo.app.utils.AppUtils
import dagger.Module
import dagger.Provides
import dagger.Reusable

@Module
class AppModule(private val application: Application) {

    @Provides
    @Reusable
    internal fun providerApplication(): Application {
        return application
    }

    @Provides
    @Reusable
    internal fun providerContext(): Context {
        return application
    }

    @Provides
    @Reusable
    internal fun providesSharedPrefereces(): SharedPreferences {
        return application.getSharedPreferences(
            application.resources.getString(R.string.app_name),
            0
        )
    }

    @Provides
    @Reusable
    internal fun providesSharedPreferencesEditor(): SharedPreferences.Editor {
        return providesSharedPrefereces().edit()
    }

    @Provides
    @Reusable
    internal fun providerUtils(): AppUtils {
        return AppUtils.newInstance(application)
    }

    @Provides
    @Reusable
    internal fun providerPreferences(): AppPreferences {
        return AppPreferences.newInstance(application)
    }

    @Provides
    @Reusable
    internal fun providerDialogUtils(): AppDialogUtils {
        return AppDialogUtils.newInstance(providerContext())
    }

    @Provides
    @Reusable
    internal fun providerSocialLoginUtils(): AppSocialLoginUtils {
        return AppSocialLoginUtils.newInstance(application)
    }

}