package com.example.hospital;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class PatientListAdapter extends RecyclerView.Adapter<PatientListAdapter.ViewHolder>
{
    List<PatientUserModel> patientUserModelList;

    public PatientListAdapter(List<PatientUserModel> patientUserModelList) {
        this.patientUserModelList = patientUserModelList;
    }

    @NonNull
    @Override
    public PatientListAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.single_patient,null);
        return  new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PatientListAdapter.ViewHolder holder, int position) {

        PatientUserModel patientUserModel=patientUserModelList.get(position);
        String name=patientUserModel.getName();
        String image=patientUserModel.getImage();
        String profession=patientUserModel.getProfession();
        holder.setData(name,image,profession);
    }

    @Override
    public int getItemCount() {
        return patientUserModelList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView Name;
        private CircleImageView circleImageView;
        private  TextView professoin;
        public ViewHolder(@NonNull final View itemView) {
            super(itemView);
            Name=itemView.findViewById(R.id.main_name);
            circleImageView=itemView.findViewById(R.id.main_image);
            professoin=itemView.findViewById(R.id.main_profession);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    String patientid=patientUserModelList.get(getAdapterPosition()).getUserid();
                    Intent intent=new Intent(itemView.getContext(),PatientDescriptionActivity.class);
                    intent.putExtra("patientid",patientid);
                    itemView.getContext().startActivity(intent);
                }
            });

        }

        public void setData(String name, String image, String profession) {


            Name.setText(name);
            Picasso.get().load(image).into(circleImageView);
            professoin.setText(profession);


        }
    }
}
