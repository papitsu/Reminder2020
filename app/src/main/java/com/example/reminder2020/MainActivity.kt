package com.example.reminder2020

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Message
import androidx.core.app.NotificationCompat
import androidx.room.Room
import kotlinx.android.synthetic.main.activity_main.*
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.toast
import org.jetbrains.anko.uiThread
import kotlin.random.Random


class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        var fabOpened = false

        fab.setOnClickListener {
            if (!fabOpened) {
                fabOpened = true
                fab_map.animate().translationY(-resources.getDimension((R.dimen.standard_66)))
                fab_time.animate().translationY(-resources.getDimension((R.dimen.standard_116)))
            } else {
                fabOpened = false
                fab_map.animate().translationY(0f)
                fab_time.animate().translationY(0f)
            }
        }

        fab_time.setOnClickListener {
            startActivity(Intent(applicationContext, TimeActivity::class.java))
        }

        fab_map.setOnClickListener {
            startActivity(Intent(applicationContext, MapActivity::class.java))
        }
    }

    override fun onResume() {
        super.onResume()
        refreshList()
    }

    private fun refreshList() {
        doAsync {
            val db = Room.databaseBuilder(applicationContext, AppDatabase::class.java, "reminders")
                .build()
            val reminders = db.reminderDao().getReminders()
            db.close()
            uiThread {

                if (reminders.isNotEmpty()) {
                    val adapter = ReminderAdapter(applicationContext, reminders)
                    list.adapter = adapter
                } else {
                    toast(getString(R.string.noreminders))
                }

            }
        }
    }

    companion object {
        fun showNotification(context: Context, message: String) {
            val CHANNEL_ID = "REMINDER_CHANNEL_ID"
            var notificationId = 1337
            notificationId += Random(notificationId).nextInt(1, 30)

            val notificationBuilder = NotificationCompat.Builder(context, CHANNEL_ID)
                .setSmallIcon(R.drawable.ic_notification_important_24px)
                .setContentTitle(context.getString(R.string.app_name))
                .setStyle(NotificationCompat.BigTextStyle().bigText(message))
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)

            val notificationManager =
                context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager


            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                val channel = NotificationChannel(
                    CHANNEL_ID,
                    context.getString(R.string.app_name),
                    NotificationManager.IMPORTANCE_DEFAULT
                ).apply { description = context.getString(R.string.app_name) }

                notificationManager.createNotificationChannel(channel)
            }

            notificationManager.notify(notificationId, notificationBuilder.build())
        }
    }
}
