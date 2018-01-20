package crypro.profit.loss.tracker.managers

import android.app.job.JobParameters
import android.app.job.JobService
import android.os.AsyncTask
import android.util.Log
import com.google.gson.Gson
import crypro.profit.loss.tracker.api.CoinResponse
import crypro.profit.loss.tracker.api.DataCompletion
import crypro.profit.loss.tracker.persistance.AlarmDetails
import crypro.profit.loss.tracker.utils.Utils


/**
 * Created by vladi on 20/1/18.
 */
class CoinJobService : JobService() {

    private val requestManager = CoinRequestManager()

    override fun onStopJob(params: JobParameters?): Boolean {
        return true
    }

    override fun onStartJob(params: JobParameters?): Boolean {

        val task = CoinAlarmTask()
        task.execute(params)

        return true
    }

    inner class CoinAlarmTask : AsyncTask<JobParameters, Void, JobParameters>() {

        private var details: AlarmDetails? = null
        private var jobFinished = false

        override fun doInBackground(vararg params: JobParameters?): JobParameters {

            val jobParameters = params[0]

            val detailsStr = jobParameters?.extras?.getString(AlarmsManagerImpl.EXTRA_DETAILS)
            details = Gson().fromJson<AlarmDetails>(detailsStr, AlarmDetails::class.java)

            val market = Utils.getMarketName(details!!.ticker1, details!!.ticker2)
            requestManager.getCoinStats(market, object : DataCompletion<CoinResponse?> {

                override fun onResponse(response: CoinResponse?) {

                    if (requestManager.checkValidResponse(response?.coinDataResponse?.message) && response?.coinDataResponse?.success!!) {
                        val lastPrice = response.coinDataResponse.result.last
                        val condition = details?.condition
                        when (condition) {
                            AlarmDetails.Condition.LessThanOrEqualTo ->
                                if (lastPrice <= details!!.triggerValue) {
                                    Log.i("CoinAlarmTask", "TRIGGER")
                                    jobFinished = true
                                }
                            AlarmDetails.Condition.GreaterThanOrEqualTo ->
                                if (lastPrice >= details!!.triggerValue) {
                                    Log.i("CoinAlarmTask", "TRIGGER")
                                    jobFinished = true
                                } else {
                                    Log.i("CoinAlarmTask", "NO TRIGGER")
                                }
                        }
                    }
                }

                override fun onError(message: String) {
                    Log.i("CoinAlarmTask", "onError")
                }
            })

            return jobParameters!!
        }

        override fun onPostExecute(result: JobParameters?) {
            super.onPostExecute(result)
            jobFinished(result, jobFinished)
            CoinsAlarmJobManager(applicationContext).setAlarm(5000, details!!)
        }
    }
}