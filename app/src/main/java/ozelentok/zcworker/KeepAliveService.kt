package ozelentok.zcworker

import android.app.*
import android.content.Intent
import android.graphics.drawable.Drawable
import android.os.IBinder
import ozelentok.zcworker.Utils.showErrorToast
import ozelentok.zcworker.Utils.toBitmap


class KeepAliveService : Service() {
    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        val notificationManager = getSystemService(NOTIFICATION_SERVICE) as NotificationManager?
        if (notificationManager == null) {
            showErrorToast(this, "Failed to open NotificationManager")
            return START_NOT_STICKY
        }
        val contentIntent = PendingIntent.getActivity(
            this, 0,
            Intent(this, MainActivity::class.java),
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )
        val appIcon: Drawable = resources.getDrawable(R.mipmap.ic_launcher)
        val builder: Notification.Builder = Notification.Builder(this, CHANNEL_ID)
            .setContentTitle("ZCWorker Active")
            .setSmallIcon(R.drawable.ic_launcher_foreground)
            .setLargeIcon(toBitmap(appIcon))
            .setContentIntent(contentIntent)
        val channel =
            NotificationChannel(CHANNEL_ID, CHANNEL_NAME, NotificationManager.IMPORTANCE_DEFAULT)
        channel.setSound(null, null)
        notificationManager.createNotificationChannel(channel)
        val notification: Notification = builder.build()
        notification.flags = notification.flags or Notification.FLAG_NO_CLEAR
        startForeground(NOTIFICATION_ID, notification)
        return START_NOT_STICKY
    }

    override fun onBind(intent: Intent?): IBinder? {
        return null
    }

    companion object {
        private const val NOTIFICATION_ID = 5142
        private const val CHANNEL_ID = "zcworker"
        private const val CHANNEL_NAME = "ZCWorker"
    }
}