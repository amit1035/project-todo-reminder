package com.example.project_todo;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.text.DateFormat;
import java.util.Date;
import java.util.List;

public class TaskAdapter extends RecyclerView.Adapter<TaskAdapter.TaskViewHolder> {

    private final List<Task> taskList;
    private final OnTaskDeleteListener deleteListener;

    // Interface to handle delete click
    public interface OnTaskDeleteListener {
        void onDeleteClick(int position);
    }

    // Constructor
    public TaskAdapter(List<Task> taskList, OnTaskDeleteListener deleteListener) {
        this.taskList = taskList;
        this.deleteListener = deleteListener;
    }

    @NonNull
    @Override
    public TaskViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_task, parent, false);
        return new TaskViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TaskViewHolder holder, int position) {
        Task currentTask = taskList.get(position);

        // Set task title
        holder.taskName.setText(currentTask.getTitle());

        // Set priority label text
        holder.priorityLabel.setText(currentTask.getPriority());

        // Format and set due date
        String formattedDate = DateFormat.getDateInstance(DateFormat.MEDIUM).format(new Date(currentTask.getTime()));
        holder.taskDate.setText("Due: " + formattedDate);

        // Set task description
        holder.taskDescription.setText(currentTask.getPriority());

        // Delete button click listener
        holder.deleteTask.setOnClickListener(v -> {
            int adapterPosition = holder.getAdapterPosition();
            if (adapterPosition != RecyclerView.NO_POSITION && deleteListener != null) {
                deleteListener.onDeleteClick(adapterPosition);
            }
        });
    }

    @Override
    public int getItemCount() {
        return taskList != null ? taskList.size() : 0;
    }

    // ViewHolder class
    public static class TaskViewHolder extends RecyclerView.ViewHolder {
        TextView taskName, taskDescription, priorityLabel, taskDate;
        ImageView deleteTask;

        public TaskViewHolder(@NonNull View itemView) {
            super(itemView);
            taskName = itemView.findViewById(R.id.taskName);
            taskDescription = itemView.findViewById(R.id.taskDescription);
            priorityLabel = itemView.findViewById(R.id.priorityLabel);
            taskDate = itemView.findViewById(R.id.taskDate);
            deleteTask = itemView.findViewById(R.id.deleteTask);
        }
    }
}
