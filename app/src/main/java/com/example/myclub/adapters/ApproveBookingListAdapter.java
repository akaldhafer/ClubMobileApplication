package com.example.myclub.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myclub.R;
import com.example.myclub.activities.ClubListActivity;
import com.example.myclub.activities.ClubPostList;
import com.example.myclub.classroomAPI.ClassroomModule;
import com.example.myclub.clubAPI.ClubModule;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;

public class ApproveBookingListAdapter extends RecyclerView.Adapter<ApproveBookingListAdapter.ViewHolder> {
    private Context context;
    private ArrayList<ClassroomModule> classroomModuleArrayList = new ArrayList<>();
    private FirebaseFirestore firebaseFirestore;

    public ApproveBookingListAdapter(Context context, ArrayList<ClassroomModule> classroomModuleArrayList) {
        this.context = context;
        this.classroomModuleArrayList = classroomModuleArrayList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.booking_request_card, parent, false);
        ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ApproveBookingListAdapter.ViewHolder holder, int position) {
        System.out.println("Get with position "+ classroomModuleArrayList.get(position).getBookedDate());
        System.out.println("Get with holder "+ classroomModuleArrayList.get(holder.getAdapterPosition()).getBookedByStudentEmail());

        holder.location.setText(classroomModuleArrayList.get(holder.getAdapterPosition()).getClassLocation());
        holder.advisorEmail.setText(classroomModuleArrayList.get(holder.getAdapterPosition()).getBookedByStudentEmail());
        holder.clubName.setText(classroomModuleArrayList.get(holder.getAdapterPosition()).getBookedForClubName());
        holder.time.setText(classroomModuleArrayList.get(holder.getAdapterPosition()).getBookedTime());
        holder.date.setText(classroomModuleArrayList.get(holder.getAdapterPosition()).getBookedDate());
        holder.status.setText(classroomModuleArrayList.get(holder.getAdapterPosition()).getIsAvailable());

        String tok= classroomModuleArrayList.get(holder.getAdapterPosition()).getToken();

        holder.edStatus.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                firebaseFirestore = FirebaseFirestore.getInstance();
                DocumentReference record = firebaseFirestore.collection("ClassroomData").document(tok);
                record.update("isAvailable", "Accepted")
                        .addOnSuccessListener(new OnSuccessListener< Void >() {
                            @SuppressLint("ResourceAsColor")
                            @Override
                            public void onSuccess(Void aVoid) {

                                Toast.makeText(v.getContext(), "Booking accepted", Toast.LENGTH_LONG).show();
                                //delete from the ui
                                classroomModuleArrayList.remove(holder.getAdapterPosition());
                                notifyItemRemoved(holder.getAdapterPosition());
                                notifyItemRangeChanged(holder.getAdapterPosition(), classroomModuleArrayList.size());

                            }
                        }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(v.getContext(), "Something went wrong, check the internet connection",Toast.LENGTH_LONG).show();
                    }
                });            }
        });

    }

    @Override
    public int getItemCount() {
        return classroomModuleArrayList.size();
    }
    public class ViewHolder extends RecyclerView.ViewHolder{
        private TextView location,clubName, advisorEmail, time, date, status;
        private Button edStatus;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            location = itemView.findViewById(R.id.viewClassLocation);
            clubName = itemView.findViewById(R.id.viewClubName);
            advisorEmail = itemView.findViewById(R.id.viewAdvisorEmailb);
            time = itemView.findViewById(R.id.viewBookingTime);
            date = itemView.findViewById(R.id.viewBookingDate);
            status = itemView.findViewById(R.id.viewStatus);
            edStatus = itemView.findViewById(R.id.EditStatus);
        }
    }
}
