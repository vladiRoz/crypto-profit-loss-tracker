package crypro.profit.loss.tracker

import android.content.Context
import android.os.SystemClock
import android.support.test.InstrumentationRegistry
import android.support.test.runner.AndroidJUnit4
import crypro.profit.loss.tracker.managers.CoinNotificationManager
import crypro.profit.loss.tracker.persistance.AlarmDetails
import org.junit.Test
import org.junit.runner.RunWith

/**
 * Instrumented test, which will execute on an Android device.
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
@RunWith(AndroidJUnit4::class)
class AlarmInstrumentedTest {

    fun getContext() : Context {
        return InstrumentationRegistry.getTargetContext()
    }

    @Test
    fun testNotifications(){

        val details1 = AlarmDetails("xrp", "btc", 0.00014, AlarmDetails.Condition.GreaterThanOrEqualTo)
        val details2 = AlarmDetails("ltc", "btc", 0.00314, AlarmDetails.Condition.LessThanOrEqualTo)

        CoinNotificationManager.sendNotification(getContext(), details1)
//        CoinNotificationManager.sendNotification(getContext(), details2)

        SystemClock.sleep(1000)
        CoinNotificationManager.sendNotification(getContext(), details2)

        SystemClock.sleep(5000)
    }
}
