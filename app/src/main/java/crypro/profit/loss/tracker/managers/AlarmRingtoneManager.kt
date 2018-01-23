package crypro.profit.loss.tracker.managers

import android.content.Context
import android.media.RingtoneManager
import android.net.Uri


/**
 * Created by vladi on 21/1/18.
 */
object AlarmRingtoneManager {

    fun soundAlarm(context : Context) {

        var alarmUri: Uri? = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM)
        if (alarmUri == null) {
            alarmUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)
        }
        val ringtone = RingtoneManager.getRingtone(context, alarmUri)
        ringtone.play()
    }
}