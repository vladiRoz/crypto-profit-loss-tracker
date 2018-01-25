package crypro.profit.loss.tracker.managers

import android.app.job.JobInfo
import android.app.job.JobScheduler
import android.app.job.JobService
import android.content.ComponentName
import android.content.Context
import android.os.PersistableBundle
import com.google.gson.Gson
import crypro.profit.loss.tracker.persistance.AlarmDetails


/**
 * cto'r should receive XXX::class for JobInfo ComponentName
 *
 * Created by vladi on 20/1/18.
 */

class CoinsAlarmJobManager<T : JobService> (val jobServiceClass : Class<T>, var context: Context) : AlarmsManagerImpl() {

    override fun setAlarm(interval : Int, details: AlarmDetails) {

        val serviceComponent = ComponentName(context, jobServiceClass)

        val builder = JobInfo.Builder(System.currentTimeMillis().toInt(), serviceComponent)

        val bundle = PersistableBundle()
        bundle.putString(AlarmsManagerImpl.EXTRA_DETAILS, Gson().toJson(details))
        builder.setExtras(bundle)

        builder.setMinimumLatency((interval * 1000).toLong())
        builder.setOverrideDeadline(10000)
        builder.setPersisted(true)

        builder.setRequiredNetworkType(JobInfo.NETWORK_TYPE_ANY)
//        builder.setRequiresDeviceIdle(true); // device should be idle

        val jobScheduler = context?.getSystemService(Context.JOB_SCHEDULER_SERVICE) as JobScheduler?
        jobScheduler?.schedule(builder.build())
    }


}


