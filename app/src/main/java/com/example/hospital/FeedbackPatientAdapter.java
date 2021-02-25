package com.example.hospital;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class FeedbackPatientAdapter extends RecyclerView.Adapter<FeedbackPatientAdapter.ViewHolder> {
    List<MedicineFeedbackModel> medicineFeedbackModelList;

    public FeedbackPatientAdapter(List<MedicineFeedbackModel> medicineFeedbackModelList) {
        this.medicineFeedbackModelList = medicineFeedbackModelList;
    }

    @NonNull
    @Override
    public FeedbackPatientAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.single_patient_feedback,null);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull FeedbackPatientAdapter.ViewHolder holder, int position) {

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

            feedbacksign=itemView.findViewById(R.id.single_patient_feedbacksign);
            feedback=itemView.findViewById(R.id.single_patient_feedback);


        }
    }
}
