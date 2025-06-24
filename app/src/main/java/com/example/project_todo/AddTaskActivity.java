package com.example.project_todo;

import android.annotation.SuppressLint;
import android.app.AlarmManager;
import android.app.DatePickerDialog;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.widget.*;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import java.util.Calendar;

public class AddTaskActivity extends AppCompatActivity {

    private EditText taskInput;
    private Spinner prioritySpinner;
    private Button dateButton, timeButton, setReminderButton;

    private final Calendar calendar = Calendar.getInstance();

    private static final int NOTIFICATION_PERMISSION_CODE = 101;

    @SuppressLint("ScheduleExactAlarm")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_task);

        taskInput = findViewById(R.id.taskInput);
        prioritySpinner = findViewById(R.id.prioritySpinner);
        dateButton = findViewById(R.id.dueDateButton);
        timeButton = findViewById(R.id.timeButton);
        setReminderButton = findViewById(R.id.setReminderButton);

        // 🛡️ Request POST_NOTIFICATIONS for Android 13+
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.POST_NOTIFICATIONS)
                    != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this,
                        new String[]{android.Manifest.permission.POST_NOTIFICATIONS},
                        NOTIFICATION_PERMISSION_CODE);
            }
        }

        // 🎚️ Spinner setup
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(
                this,
                R.array.priority_levels,
                android.R.layout.simple_spinner_item
        );
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        prioritySpinner.setAdapter(adapter);

        // 📅 Date Picker
        dateButton.setOnClickListener(v -> {
            new DatePickerDialog(this, (view, year, month, day) -> {
                calendar.set(Calendar.YEAR, year);
                calendar.set(Calendar.MONTH, month);
                calendar.set(Calendar.DAY_OF_MONTH, day);
                dateButton.setText(day + "/" + (month + 1) + "/" + year);
            }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH)).show();
        });

        // ⏰ Time Picker
        timeButton.setOnClickListener(v -> {
            new TimePickerDialog(this, (view, hour, minute) -> {
                calendar.set(Calendar.HOUR_OF_DAY, hour);
                calendar.set(Calendar.MINUTE, minute);
                calendar.set(Calendar.SECOND, 0);
                timeButton.setText(String.format("%02d:%02d", hour, minute));
            }, calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE), true).show();
        });

        // ✅ Set Reminder Button Click
        setReminderButton.setOnClickListener(v -> {
            String title = taskInput.getText().toString().trim();
            String priority = (prioritySpinner.getSelectedItem() != null)
                    ? prioritySpinner.getSelectedItem().toString()
                    : "";

            long time = calendar.getTimeInMillis();

            if (title.isEmpty()) {
                taskInput.setError("Enter task title");
                return;
            }

            if (priority.equals("Select Priority") || priority.isEmpty()) {
                Toast.makeText(this, "Please select a valid priority", Toast.LENGTH_SHORT).show();
                return;
            }

            if (time < System.currentTimeMillis()) {
                Toast.makeText(this, "Choose a future time", Toast.LENGTH_SHORT).show();
                return;
            }

            // 🔄 Pass task back to MainActivity (if needed)
            Task task = new Task(title, priority, time);
            Intent result = new Intent();
            result.putExtra("task", task);
            setResult(RESULT_OK, result);

            // ⏰ Create Intent for AlarmReceiver
            Intent intent = new Intent(this, AlarmReceiver.class);
            intent.putExtra("task", title);
            intent.putExtra("priority", priority);

            int requestCode = (int) System.currentTimeMillis();
            PendingIntent pendingIntent = PendingIntent.getBroadcast(
                    this,
                    requestCode,
                    intent,
                    PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE
            );

            AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
            if (alarmManager != null) {
                alarmManager.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, time, pendingIntent);
                Toast.makeText(this, "⏰ Reminder set for task!", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "❌ AlarmManager not found!", Toast.LENGTH_SHORT).show();
            }

            finish();
        });
    }
}
