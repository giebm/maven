package com.example.maven;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class SubjectsAdapter extends RecyclerView.Adapter<SubjectsAdapter.SubjectViewHolder> {

    private List<String> subjectsList;
    private Map<String, List<LogItem>> logsMap;

    public SubjectsAdapter(Map<String, List<LogItem>> logsMap) {
        this.logsMap = logsMap;
        this.subjectsList = new ArrayList<>(logsMap.keySet());
    }

    @NonNull
    @Override
    public SubjectViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_subject, parent, false);
        return new SubjectViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SubjectViewHolder holder, int position) {
        String subject = subjectsList.get(position);
        List<LogItem> logsList = logsMap.get(subject);
        holder.bind(subject, logsList);
    }

    @Override
    public int getItemCount() {
        return subjectsList.size();
    }

    public static class SubjectViewHolder extends RecyclerView.ViewHolder {
        private TextView subjectTextView;
        private RecyclerView logsRecyclerView;
        private LogsAdapter logsAdapter;

        public SubjectViewHolder(@NonNull View itemView) {
            super(itemView);
            subjectTextView = itemView.findViewById(R.id.subjectTextView);
            logsRecyclerView = itemView.findViewById(R.id.logsRecyclerView);
        }

        public void bind(String subject, List<LogItem> logsList) {
            subjectTextView.setText(subject);

            logsRecyclerView.setLayoutManager(new LinearLayoutManager(itemView.getContext()));
            logsAdapter = new LogsAdapter(logsList);
            logsRecyclerView.setAdapter(logsAdapter);
        }
    }
}
