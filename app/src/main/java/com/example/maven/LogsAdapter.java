package com.example.maven;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TableRow;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class LogsAdapter extends RecyclerView.Adapter<LogsAdapter.LogViewHolder> {

    private List<LogItem> logItems;

    public LogsAdapter(List<LogItem> logItems) {
        this.logItems = logItems;
    }

    @NonNull
    @Override
    public LogViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_log, parent, false);
        return new LogViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull LogViewHolder holder, int position) {
        LogItem logItem = logItems.get(position);
        holder.bind(logItem);
    }

    @Override
    public int getItemCount() {
        return logItems.size();
    }

    public static class LogViewHolder extends RecyclerView.ViewHolder {
        private TextView subjectTextView;
        private TextView activityTextView;
        private TextView scorePercentageTextView;
        private TextView activityPercentageTextView;

        public LogViewHolder(@NonNull View itemView) {
            super(itemView);
            subjectTextView = itemView.findViewById(R.id.subjectTextView);
            activityTextView = itemView.findViewById(R.id.activityTextView);
            scorePercentageTextView = itemView.findViewById(R.id.scorePercentageTextView);
            activityPercentageTextView = itemView.findViewById(R.id.activityPercentageTextView);
        }

        public void bind(LogItem logItem) {
            subjectTextView.setText(logItem.getSubject());
            activityTextView.setText(logItem.getActivity());
            scorePercentageTextView.setText(String.valueOf(logItem.getScorePercentage()));
            activityPercentageTextView.setText(String.valueOf(logItem.getActivityPercentage()));
        }
    }
}
