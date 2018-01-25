package crypro.profit.loss.tracker

import android.content.Context
import android.os.SystemClock
import android.support.test.InstrumentationRegistry
import android.support.test.runner.AndroidJUnit4
import crypro.profit.loss.tracker.alarms.CoinJobServiceTest
import crypro.profit.loss.tracker.managers.CoinNotificationManager
import crypro.profit.loss.tracker.managers.CoinsAlarmJobManager
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

    private val details1 = AlarmDetails("xrp", "btc", 4.0, AlarmDetails.Condition.GreaterThanOrEqualTo)
    private val details2 = AlarmDetails("ltc", "btc", 6.0, AlarmDetails.Condition.LessThanOrEqualTo)

    fun getContext() : Context {
        return InstrumentationRegistry.getTargetContext()
    }

    @Test
    fun testNotifications(){

        CoinNotificationManager.sendNotification(getContext(), details1)

//        SystemClock.sleep(1000)
//        CoinNotificationManager.sendNotification(getContext(), details2)

        SystemClock.sleep(10000)
    }

    @Test
    fun testCoinsAlarmJobManager(){

        val manager = CoinsAlarmJobManager(CoinJobServiceTest::class.java, getContext())
        manager.setAlarm(5000, details1)
        manager.setAlarm(5000, details2)
        SystemClock.sleep(1000000)
    }



}
