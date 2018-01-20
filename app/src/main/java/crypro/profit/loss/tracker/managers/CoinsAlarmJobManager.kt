package crypro.profit.loss.tracker.managers

import android.app.job.JobInfo
import android.app.job.JobScheduler
import android.content.ComponentName
import android.content.Context
import android.os.PersistableBundle
import com.google.gson.Gson
import crypro.profit.loss.tracker.persistance.AlarmDetails


/**
 * Created by vladi on 20/1/18.
 */
class CoinsAlarmJobManager : AlarmsManagerImpl {

    private var context : Context? = null

    constructor(context: Context){
        this.context = context
    }

    override fun setAlarm(interval : Int, details: AlarmDetails) {

        val serviceComponent = ComponentName(context, CoinJobService::class.java)

        val builder = JobInfo.Builder(0, serviceComponent)

        val bundle = PersistableBundle()
        bundle.putString(AlarmsManagerImpl.EXTRA_DETAILS, Gson().toJson(details))
        builder.setExtras(bundle)

        builder.setMinimumLatency((interval * 1000).toLong())
        builder.setOverrideDeadline(10 * 1000)
        builder.setPersisted(true)

        builder.setRequiredNetworkType(JobInfo.NETWORK_TYPE_ANY)
//        builder.setRequiresDeviceIdle(true); // device should be idle

        val jobScheduler = context?.getSystemService(Context.JOB_SCHEDULER_SERVICE) as JobScheduler?
        jobScheduler?.schedule(builder.build())
    }

}


