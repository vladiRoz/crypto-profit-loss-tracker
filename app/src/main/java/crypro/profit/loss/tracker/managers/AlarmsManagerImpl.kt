package crypro.profit.loss.tracker.managers

/**
 * Created by vladi on 20/1/18.
 */
abstract class AlarmsManagerImpl : CoinAlarmManager {

    companion object {
        val EXTRA_DETAILS = "extra_details"
        val INTERVAL = 5000L
    }

}