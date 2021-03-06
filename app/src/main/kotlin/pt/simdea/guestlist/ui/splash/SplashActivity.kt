package pt.simdea.guestlist.ui.splash

import android.os.Handler
import pt.simdea.guestlist.R
import pt.simdea.guestlist.databinding.ActivitySplashBinding
import pt.simdea.guestlist.ui.base.BaseActivity
import pt.simdea.guestlist.utils.manager.CallManager
import pt.simdea.guestlist.utils.manager.PreferencesManager
import javax.inject.Inject

/**
 * Activity to apply the splash screen.
 */
class SplashActivity : BaseActivity<ActivitySplashBinding, SplashViewModel>() {

    companion object {
        private const val SPLASH_TIME_OUT: Long = 2000
    }

    @Inject
    lateinit var callManager: CallManager
    @Inject
    lateinit var preferencesManager: PreferencesManager

    private var handler: Handler? = null
    private var startTime: Long = 0

    /**
     * Gets the timeout for the splash finishes.
     *
     * @return the timeout.
     */
    private val timeout: Long
        get() {
            val diff = System.currentTimeMillis() - startTime
            return if (diff > SPLASH_TIME_OUT) 0 else SPLASH_TIME_OUT - diff
        }

    override fun layoutToInflate() = R.layout.activity_splash

    override fun getViewModelClass() = SplashViewModel::class.java

    override fun doOnCreated() { }

    public override fun onStart() {
        super.onStart()
        handler = Handler()
    }

    public override fun onResume() {
        super.onResume()

        startTime = System.currentTimeMillis()
        if (handler == null) return

        handler!!.postDelayed({
            startActivity(callManager.main(this))
            finishAffinity()
        }, timeout)
    }

    public override fun onPause() {
        super.onPause()
        handler!!.removeCallbacksAndMessages(null)
    }

}
