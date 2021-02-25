package com.example.hospital;

import android.telephony.mbms.StreamingServiceInfo;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class CommentAdapter extends RecyclerView.Adapter<CommentAdapter.ViewHolder> {
    List<CommentModel> commentModelList;
    private DatabaseReference ref;
    public CommentAdapter(List<CommentModel> commentModelList) {
        this.commentModelList = commentModelList;
    }

    @NonNull
    @Override
    public CommentAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.single_comment,null);

        ref= FirebaseDatabase.getInstance().getReference();
        return  new ViewHolder(view);

    }

    @Override
    public void onBindViewHolder(@NonNull final CommentAdapter.ViewHolder holder, int position) {

        final CommentModel commentModel=commentModelList.get(position);
        String senderid=commentModel.getSenderid();
        String receiverid=commentModel.getReceiverid();
        String comments=commentModel.getComments();
        final String ratingBarValues=commentModel.getRating();
        holder.ratingBar.setEnabled(false);
        ref.child("Users").child(senderid).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                PatientUserModel patientUserModel=snapshot.getValue(PatientUserModel.class);
                String names=patientUserModel.getName();
                String image=patientUserModel.getImage();
                String comments=commentModel.getComments();
                holder.ratingBarValue.setText(ratingBarValues);
                holder.ratingBar.setRating(Float.parseFloat(ratingBarValues));
                Picasso.get().load(image).into(holder.circleImageView);
                holder.name.setText(names);
                holder.comments.setText(comments);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });



    }

    @Override
    public int getItemCount() {
        return commentModelList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private CircleImageView circleImageView;
        private TextView name;
        private TextView comments;
        private RatingBar ratingBar;
        private TextView ratingBarValue;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            circleImageView=itemView.findViewById(R.id.single_comment_profile_image);
            name=itemView.findViewById(R.id.single_comment_profile_name);
            comments=itemView.findViewById(R.id.single_comment);
            ratingBar=itemView.findViewById(R.id.single_comment_rating_bar);
            ratingBarValue=itemView.findViewById(R.id.single_rating_bar_value);


        }


    }
}
