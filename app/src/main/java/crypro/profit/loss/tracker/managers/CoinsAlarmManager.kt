package crypro.profit.loss.tracker.managers

import android.content.Context
import android.content.Intent
import crypro.profit.loss.tracker.persistance.AlarmDetails
import crypro.profit.loss.tracker.service.AlarmService

/**
 * Created by vladi on 19/1/18.
 */
class CoinsAlarmManager(var context : Context) {

    companion object {
        val EXTRA_DETAILS = "extra_details"
    }

    fun setAlarm(details: AlarmDetails) {
        val intent = Intent(context, AlarmService::class.java)
        intent.putExtra(EXTRA_DETAILS, details)
        context.startService(intent)
    }


}