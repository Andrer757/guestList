package pt.simdea.guestlist.utils.manager

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.provider.Settings
import androidx.fragment.app.Fragment
import pt.simdea.guestlist.ui.base.BaseActivity
import pt.simdea.guestlist.ui.main.MainActivity
import pt.simdea.guestlist.ui.splash.SplashActivity
import pt.simdea.guestlist.ui.widgets.afm.FragmentCall
import javax.inject.Inject
import kotlin.reflect.KClass

class CallManager @Inject constructor() {

    fun splash(context: Context?) = Intent(context, SplashActivity::class.java).apply {
        flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
    }

    fun main(context: Context?) = Intent(context, MainActivity::class.java).apply {
        flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
    }

    private fun replaceFragment(activity: BaseActivity<*, *>, fragmentClass: KClass<out Fragment>, bundle: Bundle) {
        FragmentCall.init(activity, fragmentClass).setTransitionType(FragmentCall.TransitionType.REPLACE).setBundle(bundle).build()
    }

    private fun addFragment(activity: BaseActivity<*, *>, fragmentClass: KClass<out Fragment>, bundle: Bundle) {
        FragmentCall.init(activity, fragmentClass).setTransitionType(FragmentCall.TransitionType.ADD).setBundle(bundle).build()
    }

    fun openSettings() = Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS).apply {
        flags = Intent.FLAG_ACTIVITY_NEW_TASK
    }

}