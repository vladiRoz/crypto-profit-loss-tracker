package crypro.profit.loss.tracker.alarms

import android.app.job.JobParameters
import android.app.job.JobService
import android.content.Context
import android.os.AsyncTask
import android.util.Log
import com.google.gson.Gson
import crypro.profit.loss.tracker.api.CoinResponse
import crypro.profit.loss.tracker.api.DataCompletion
import crypro.profit.loss.tracker.managers.*
import crypro.profit.loss.tracker.persistance.AlarmDetails
import crypro.profit.loss.tracker.utils.Utils
import java.lang.ref.WeakReference


/**
 * Created by vladi on 20/1/18.
 */
open class CoinJobService(protected var requestManager : CoinsApiManager = CoinRequestManager()) : JobService() {

    override fun onStopJob(params: JobParameters?): Boolean {
        return true
    }

    override fun onStartJob(params: JobParameters?): Boolean {

        val task = CoinAlarmTask(WeakReference(this), WeakReference(applicationContext), requestManager)
        task.execute(params)

        return true
    }

    class CoinAlarmTask(private val service : WeakReference<JobService>, val context : WeakReference<Context>, val requestManager : CoinsApiManager) : AsyncTask<JobParameters, Void, JobParameters>() {

        private var details: AlarmDetails? = null
        private var jobFinished = false

        override fun doInBackground(vararg params: JobParameters?): JobParameters {

            val jobParameters = params[0]

            val detailsStr = jobParameters?.extras?.getString(AlarmsManagerImpl.EXTRA_DETAILS)
            details = Gson().fromJson<AlarmDetails>(detailsStr, AlarmDetails::class.java)

            Log.i("CoinJobService", "trigger " + details?.condition?.name + " " + details?.triggerValue)

            val market = Utils.getMarketName(details!!.ticker1, details!!.ticker2)
            requestManager.getCoinStats(market, object : DataCompletion<CoinResponse?> {

                override fun onResponse(response: CoinResponse?) {

                    Log.i("CoinJobService", "exchange last price: " + response?.coinDataResponse?.result?.last)

                    if (requestManager.checkValidResponse(response?.coinDataResponse?.message) && response?.coinDataResponse?.success!!) {
                        val lastPrice = response.coinDataResponse.result.last
                        when (details?.condition) {
                            AlarmDetails.Condition.LessThanOrEqualTo ->
                                if (lastPrice <= details!!.triggerValue) {
                                    Log.i("CoinJobService", "TRIGGER")
                                    onTriggered(details!!)
                                    jobFinished = true
                                } else {
                                    Log.i("CoinJobService", "NO TRIGGER")
                                }
                            AlarmDetails.Condition.GreaterThanOrEqualTo ->
                                if (lastPrice >= details!!.triggerValue) {
                                    Log.i("CoinJobService", "TRIGGER")
                                    onTriggered(details!!)
                                    jobFinished = true
                                } else {
                                    Log.i("CoinJobService", "NO TRIGGER")
                                }
                        }
                    }
                }

                override fun onError(message: String) {
                    Log.i("CoinJobService", "onError")
                }
            })

            return jobParameters!!
        }

        override fun onPostExecute(result: JobParameters?) {
            super.onPostExecute(result)
            Log.i("CoinJobService", "onPostExecute jobFinished: " + jobFinished)
            if (jobFinished == true) {
                service.get()?.jobFinished(result, false)
            } else {
                val contextTmp = context.get()
                if (contextTmp != null) {
                    CoinsAlarmJobManager(CoinJobServiceTest::class.java, contextTmp).setAlarm(5000, details!!)
                }
            }
        }

        fun onTriggered(details : AlarmDetails){
            val contextTmp = context.get()
            if (contextTmp != null) {
                CoinNotificationManager.sendNotification(contextTmp, details)
//                AlarmRingtoneManager.soundAlarm(contextTmp)
            }
        }
    }


}