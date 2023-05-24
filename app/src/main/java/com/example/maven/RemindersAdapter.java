package com.example.maven;


import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.maven.Reminder;

import java.util.List;

public class RemindersAdapter extends RecyclerView.Adapter<RemindersAdapter.ReminderViewHolder> {

    private List<Reminder> reminders;

    public void setReminders(List<Reminder> reminders) {
        this.reminders = reminders;
        notifyDataSetChanged();
    }

    public void clearReminders() {
        this.reminders.clear();
        notifyDataSetChanged();
    }


    public RemindersAdapter(List<Reminder> reminders) {
        this.reminders = reminders;
    }

    @NonNull
    @Override
    public ReminderViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_reminder, parent, false);
        return new ReminderViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ReminderViewHolder holder, int position) {
        Reminder reminder = reminders.get(position);
        holder.bind(reminder);
    }

    @Override
    public int getItemCount() {
        return reminders.size();
    }

    public static class ReminderViewHolder extends RecyclerView.ViewHolder {
        private TextView reminderTextView;
        private TextView dateTimeTextView;

        public ReminderViewHolder(@NonNull View itemView) {
            super(itemView);
            reminderTextView = itemView.findViewById(R.id.reminderTextView);
            dateTimeTextView = itemView.findViewById(R.id.dateTimeTextView);
        }

        public void bind(Reminder reminder) {
            reminderTextView.setText(reminder.getText());
            dateTimeTextView.setText(reminder.getDateTime());
        }
    }
}
