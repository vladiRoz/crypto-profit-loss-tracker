package crypro.profit.loss.tracker.managers

import crypro.profit.loss.tracker.persistance.AlarmDetails

/**
 * Created by vladi on 20/1/18.
 */
interface CoinAlarmManager {
    fun setAlarm(interval : Int, details : AlarmDetails)
}