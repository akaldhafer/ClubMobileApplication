package com.example.myclub.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myclub.R;
import com.example.myclub.classroomAPI.ClassroomModule;

import java.util.ArrayList;

public class BookingListAdapter extends RecyclerView.Adapter<BookingListAdapter.ViewHolder>{

    private Context context;
    private ArrayList<ClassroomModule> classroomModuleArrayList = new ArrayList<>();

    public BookingListAdapter(Context context, ArrayList<ClassroomModule> classroomModuleArrayList) {
        this.context = context;
        this.classroomModuleArrayList = classroomModuleArrayList;
    }

    @NonNull
    @Override
    public BookingListAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.booking_list_card, parent, false);
        BookingListAdapter.ViewHolder holder = new BookingListAdapter.ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull BookingListAdapter.ViewHolder holder, int position) {
        System.out.println("Get with position "+ classroomModuleArrayList.get(position).getBookedDate());
        System.out.println("Get with holder "+ classroomModuleArrayList.get(holder.getAdapterPosition()).getBookedByStudentEmail());

        holder.location.setText(classroomModuleArrayList.get(holder.getAdapterPosition()).getClassLocation());
        holder.advisorEmail.setText(classroomModuleArrayList.get(holder.getAdapterPosition()).getBookedByStudentEmail());
        holder.clubName.setText(classroomModuleArrayList.get(holder.getAdapterPosition()).getBookedForClubName());
        holder.time.setText(classroomModuleArrayList.get(holder.getAdapterPosition()).getBookedTime());
        holder.date.setText(classroomModuleArrayList.get(holder.getAdapterPosition()).getBookedDate());
        holder.status.setText(classroomModuleArrayList.get(holder.getAdapterPosition()).getIsAvailable());
    }

    @Override
    public int getItemCount() {
        return classroomModuleArrayList.size();
    }
    public class ViewHolder extends RecyclerView.ViewHolder{
        private TextView location,clubName, advisorEmail, time, date, status;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            location = itemView.findViewById(R.id.bviewClassLocation);
            clubName = itemView.findViewById(R.id.bviewClubName);
            advisorEmail = itemView.findViewById(R.id.bviewAdvisorEmail);
            time = itemView.findViewById(R.id.bviewBookingTime);
            date = itemView.findViewById(R.id.bviewBookingDate);
            status = itemView.findViewById(R.id.bviewStatus);

        }
    }
}

