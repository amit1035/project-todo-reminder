package com.example.project_todo;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.SearchView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements TaskAdapter.OnTaskDeleteListener {

    private RecyclerView taskRecyclerView;
    private TaskAdapter taskAdapter;
    private final List<Task> taskList = new ArrayList<>();
    private final List<Task> filteredList = new ArrayList<>();
    private SearchView searchView;
    private Button addTaskButton;

    private static final int ADD_TASK_REQUEST = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        taskRecyclerView = findViewById(R.id.taskRecyclerView);
        searchView = findViewById(R.id.searchView);
        addTaskButton = findViewById(R.id.addTaskButton);

        taskAdapter = new TaskAdapter(filteredList, this);
        taskRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        taskRecyclerView.setAdapter(taskAdapter);

        addTaskButton.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, AddTaskActivity.class);
            startActivityForResult(intent, ADD_TASK_REQUEST);
        });

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false; // No action needed
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                filterTasks(newText != null ? newText : "");
                return true;
            }
        });

        // TODO: Load saved tasks here if using persistent storage
    }

    private void filterTasks(String query) {
        filteredList.clear();

        if (query.isEmpty()) {
            filteredList.addAll(taskList);
        } else {
            for (Task task : taskList) {
                if (task.getTitle().toLowerCase().contains(query.toLowerCase())) {
                    filteredList.add(task);
                }
            }
        }

        taskAdapter.notifyDataSetChanged();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == ADD_TASK_REQUEST && resultCode == RESULT_OK && data != null) {
            Task task = (Task) data.getSerializableExtra("task");
            if (task != null) {
                taskList.add(task);
                filterTasks(searchView.getQuery() != null ? searchView.getQuery().toString() : "");
            }
        }
    }

    @Override
    public void onDeleteClick(int position) {
        if (position >= 0 && position < filteredList.size()) {
            Task taskToDelete = filteredList.get(position);
            taskList.remove(taskToDelete);
            filterTasks(searchView.getQuery() != null ? searchView.getQuery().toString() : "");
        }
    }
}
