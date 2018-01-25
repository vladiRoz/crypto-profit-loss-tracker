package crypro.profit.loss.tracker.managers

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.SystemClock
import crypro.profit.loss.tracker.persistance.AlarmDetails
import crypro.profit.loss.tracker.receivers.CoinAlarmReceiver


/**
 * Created by vladi on 19/1/18.
 */
@Deprecated ("use CoinsAlarmJobManager")
class CoinsAlarmManager : AlarmsManagerImpl() {

    override fun setAlarm(interval : Int, details: AlarmDetails) {
//        val intent = Intent(context, CoinAlarmReceiver::class.java)
//        val bytes = ParcelableUtil.marshall(details)
//        intent.putExtra(AlarmsManagerImpl.EXTRA_DETAILS, bytes)
//        createAlarm(context, intent)
    }

    fun setAlarm(context : Context, bytes: ByteArray) {
        val intent = Intent(context, CoinAlarmReceiver::class.java)
        intent.putExtra(AlarmsManagerImpl.EXTRA_DETAILS, bytes)
        createAlarm(context, intent)
    }

    private fun createAlarm(context: Context, intent: Intent) {
        val pendingIntent = PendingIntent.getBroadcast(context, System.currentTimeMillis().toInt(), intent, PendingIntent.FLAG_ONE_SHOT)
        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        alarmManager.setRepeating(AlarmManager.ELAPSED_REALTIME_WAKEUP, SystemClock.elapsedRealtime(), INTERVAL, pendingIntent)
    }



}