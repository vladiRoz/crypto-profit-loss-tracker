package crypro.profit.loss.tracker.managers

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.media.RingtoneManager
import android.support.v4.app.NotificationCompat
import android.support.v4.content.res.ResourcesCompat
import crypro.profit.loss.tracker.R
import crypro.profit.loss.tracker.persistance.AlarmDetails
import crypro.profit.loss.tracker.utils.Utils






/**
 * Created by vladi on 21/1/18.
 */
object CoinNotificationManager {


    fun sendNotification(context: Context, details : AlarmDetails) {

        val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        val marketName = Utils.getMarketName(details.ticker1, details.ticker2).toUpperCase()

        val channelId = (System.currentTimeMillis() / 1000).toString()
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {

            val notificationChannel = NotificationChannel(channelId, marketName, NotificationManager.IMPORTANCE_HIGH)
            notificationChannel.enableLights(true)
            notificationChannel.enableVibration(true)
            notificationChannel.setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM), null )
            notificationManager.createNotificationChannel(notificationChannel)
        }

        val builder = NotificationCompat.Builder(context, channelId)
                .setContentTitle(context.getString(R.string.launcher_app_name))
                .setContentText(String.format(context.resources.getString(R.string.alarm_trigger), marketName))
                .setVibrate(longArrayOf(100, 200, 300, 400, 500, 400, 300, 200, 400))
                .setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM))

            val color = ResourcesCompat.getColor(context.resources, R.color.colorAccent, null)
            builder.color = color
            builder.setSmallIcon(R.drawable.ic_add_alarm_black_18dp)

        notificationManager.notify((System.currentTimeMillis() / 1000).toInt(), builder.build())
    }

}