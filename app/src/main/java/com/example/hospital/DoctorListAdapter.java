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

public class DoctorListAdapter extends RecyclerView.Adapter<DoctorListAdapter.ViewHolder> {
    List<DoctorUserModel> doctorUserModelList;

    public DoctorListAdapter(List<DoctorUserModel> doctorUserModelList) {
        this.doctorUserModelList = doctorUserModelList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.single_dotor,null);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        DoctorUserModel doctorUserModel=doctorUserModelList.get(position);
        String name=doctorUserModel.getName();
        String image=doctorUserModel.getImage();
        String profession=doctorUserModel.getProfession();
        String speciality=doctorUserModel.getSpeciality();
        holder.setData(name,image,profession,speciality);


    }

    @Override
    public int getItemCount() {
        return doctorUserModelList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView Name;
        private CircleImageView circleImageView;
        private TextView Profession;
        private TextView Speciality;
        public ViewHolder(@NonNull final View itemView) {
            super(itemView);

            Name=itemView.findViewById(R.id.main_doctor_name);
            circleImageView=itemView.findViewById(R.id.main_doctor_image);
            Profession=itemView.findViewById(R.id.main_doctor_profession);
            Speciality=itemView.findViewById(R.id.main_doctor_speciality);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    String doctorid=doctorUserModelList.get(getAdapterPosition()).getUserid();
                    Intent intent=new Intent(itemView.getContext(), DoctorDescriptionActivity.class);
                    intent.putExtra("doctorid",doctorid);
                    itemView.getContext().startActivity(intent);
                }
            });
        }

        public void setData(String name, String image, String profession, String speciality) {
            Picasso.get().load(image).into(circleImageView);
            Name.setText(name);
            Speciality.setText(speciality);
            Profession.setText(profession);


        }

    }
}
