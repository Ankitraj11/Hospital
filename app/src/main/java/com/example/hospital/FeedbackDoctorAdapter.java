package com.example.hospital;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class FeedbackDoctorAdapter extends RecyclerView.Adapter<FeedbackDoctorAdapter.ViewHolder> {
    List<MedicineFeedbackModel> medicineFeedbackModelList;

    public FeedbackDoctorAdapter(List<MedicineFeedbackModel> medicineFeedbackModelList) {
        this.medicineFeedbackModelList = medicineFeedbackModelList;
    }

    @NonNull
    @Override
    public FeedbackDoctorAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.single_doctor_feedback,null);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull FeedbackDoctorAdapter.ViewHolder holder, int position) {
        MedicineFeedbackModel medicineFeedbackModel=medicineFeedbackModelList.get(position);
        String feedback=medicineFeedbackModel.getFeedback();
        holder.feedback.setText(feedback);
        holder.feedbacksign.setText(medicineFeedbackModel.getFeedbacksign());
    }

    @Override
    public int getItemCount() {
        return medicineFeedbackModelList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private TextView feedback;
        private TextView feedbacksign;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            feedback=itemView.findViewById(R.id.single_doctor_feedback);
            feedbacksign=itemView.findViewById(R.id.single_doctor_feedbacksign);

        }
    }
}

