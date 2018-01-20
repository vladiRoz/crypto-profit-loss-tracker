package crypro.profit.loss.tracker.service

import android.app.IntentService
import android.content.Intent
import android.util.Log
import com.google.gson.Gson
import crypro.profit.loss.tracker.managers.AlarmsManagerImpl
import crypro.profit.loss.tracker.persistance.AlarmDetails

/**
 * Created by vladi on 19/1/18.
 */
class AlarmService : IntentService {

    constructor() : super("AlarmService")

    override fun onHandleIntent(intent : Intent?) {

        if (intent != null) {

            var json = intent.getStringExtra(AlarmsManagerImpl.EXTRA_DETAILS)
            var details: AlarmDetails = Gson().fromJson<AlarmDetails>(json, AlarmDetails::class.java)

//            val bytes = intent.getByteArrayExtra(CoinAlarmReceiver.EXTRA_DETAILS)

//            val details = AlarmDetails(ParcelableUtil.unmarshal(bytes))

            Log.i("AlarmService", details.toString())

//            CoinsAlarmManager.setAlarm(this, bytes)
        }
    }
}