package crypro.profit.loss.tracker.service

import android.app.IntentService
import android.content.Intent
import android.util.Log
import crypro.profit.loss.tracker.managers.CoinsAlarmManager
import crypro.profit.loss.tracker.persistance.AlarmDetails

/**
 * Created by vladi on 19/1/18.
 */
class AlarmService : IntentService {

    constructor() : super("AlarmService")

    override fun onHandleIntent(intent : Intent?) {

        val details = intent?.getParcelableExtra<AlarmDetails>(CoinsAlarmManager.EXTRA_DETAILS)
        Log.i("AlarmService", details.toString())


    }
}