package com.example.project_todo;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.util.Log;
import android.widget.Toast;

import androidx.core.app.NotificationCompat;

public class AlarmReceiver extends BroadcastReceiver {

    private static final String CHANNEL_ID = "task_channel";
    private static final String CHANNEL_NAME = "Task Reminders";
    private static final String CHANNEL_DESC = "Used for task notifications";

    @Override
    public void onReceive(Context context, Intent intent) {
        if (context == null || intent == null) {
            Log.e("AlarmReceiver", "Context or Intent is null");
            return;
        }

        String task = intent.getStringExtra("task");
        String priority = intent.getStringExtra("priority");

        if (task == null || priority == null) {
            Log.e("AlarmReceiver", "Missing extras: task or priority");
            return;
        }

        Log.d("AlarmReceiver", "🔥 Alarm Triggered: " + task);
        Toast.makeText(context, "🔔 Reminder: " + task, Toast.LENGTH_SHORT).show();

        NotificationManager notificationManager = (NotificationManager)
                context.getSystemService(Context.NOTIFICATION_SERVICE);

        if (notificationManager == null) {
            Log.e("AlarmReceiver", "❌ NotificationManager is null");
            return;
        }


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(
                    CHANNEL_ID, CHANNEL_NAME, NotificationManager.IMPORTANCE_HIGH
            );
            channel.setDescription(CHANNEL_DESC);
            channel.enableLights(true);
            channel.enableVibration(true);
            notificationManager.createNotificationChannel(channel);
        }

        // Build the notification
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, CHANNEL_ID)
                .setSmallIcon(android.R.drawable.ic_dialog_info) // Replace with your icon if needed
                .setContentTitle("🔔 Task Reminder")
                .setContentText(task + " (" + priority + ")")
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setAutoCancel(true)
                .setDefaults(NotificationCompat.DEFAULT_ALL);

        // Show the notification
        int notificationId = (int) System.currentTimeMillis();
        notificationManager.notify(notificationId, builder.build());
        Log.d("AlarmReceiver", "✅ Notification shown with ID: " + notificationId);
    }
}
