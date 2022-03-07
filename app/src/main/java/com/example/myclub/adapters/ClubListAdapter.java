package com.example.myclub.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.ViewGroup;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myclub.R;
import com.example.myclub.activities.AdminHomeActivity;
import com.example.myclub.activities.ClubListActivity;
import com.example.myclub.activities.ClubPostList;
import com.example.myclub.activities.CreateClubActivity;
import com.example.myclub.clubAPI.ClubModule;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;

public class ClubListAdapter extends RecyclerView.Adapter<ClubListAdapter.ViewHolder> {
    private Context context;
    private ArrayList<ClubModule> clubModuleArrayList = new ArrayList<>();
    public String studentID, studentEmail,studentPassword,studentName, isAdvisor;
    public ArrayList<String> clubList = new ArrayList<>();
    private FirebaseFirestore firebaseFirestore;

    public ClubListAdapter(Context context, ArrayList<ClubModule> clubModuleArrayList, String studentID, String studentEmail, String studentPassword, String studentName, String isAdvisor, ArrayList<String> clubList) {
        this.context = context;
        this.clubModuleArrayList = clubModuleArrayList;
        this.studentID = studentID;
        this.studentEmail = studentEmail;
        this.studentPassword = studentPassword;
        this.studentName = studentName;
        this.isAdvisor = isAdvisor;
        this.clubList = clubList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.club_card, parent, false);
        ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ClubListAdapter.ViewHolder holder, int position) {
        System.out.println("Get with position "+clubModuleArrayList.get(position).getClubAdvisor());
        System.out.println("Get with holder "+clubModuleArrayList.get(holder.getAdapterPosition()).getClubAdvisor());

        holder.email.setText(clubModuleArrayList.get(holder.getAdapterPosition()).getClubAdvisor());
        holder.name.setText(clubModuleArrayList.get(holder.getAdapterPosition()).getClubName());
        holder.desc.setText(clubModuleArrayList.get(holder.getAdapterPosition()).getClubDescription());
        holder.category.setText(clubModuleArrayList.get(holder.getAdapterPosition()).getClubType());
        holder.member.setText(clubModuleArrayList.get(holder.getAdapterPosition()).getClubMemberList().toString());
        String tok= clubModuleArrayList.get(holder.getAdapterPosition()).getToken();

        holder.delete.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                ArrayList<String> AdivsorClub = new ArrayList<>();

                //update advisor role
                AdivsorClub.add("No Club Yet");
                firebaseFirestore = FirebaseFirestore.getInstance();
                DocumentReference record = firebaseFirestore.collection("ClubData").document(tok);
                record.delete().addOnSuccessListener(new OnSuccessListener< Void >() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Toast.makeText(v.getContext(), "Club Deleted !", Toast.LENGTH_SHORT).show();

                        DocumentReference record = FirebaseFirestore.getInstance().collection("StudentData").document(clubModuleArrayList.get(holder.getAdapterPosition()).getClubAdvisor());
                        record.update("isAdvisor", "student","clubList",AdivsorClub)
                                .addOnSuccessListener(new OnSuccessListener< Void >() {
                                    @Override
                                    public void onSuccess(Void aVoid) {
                                        //delete from the ui
                                        clubModuleArrayList.remove(holder.getAdapterPosition());
                                        notifyItemRemoved(holder.getAdapterPosition());
                                        notifyItemRangeChanged(holder.getAdapterPosition(), clubModuleArrayList.size());

                                    }
                                }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(v.getContext(), "Could not update Student role",Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(v.getContext(), "Something went wrong, check the internet connection",Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });

        holder.view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //todo fix the call and variable init
                Intent intent = new Intent(v.getContext(), ClubPostList.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.putExtra("club", tok);
                intent.putExtra("studentID", studentID);
                intent.putExtra("email", studentEmail);
                intent.putExtra("password",studentPassword);
                intent.putExtra("name", studentName);
                intent.putExtra("role", isAdvisor);
                intent.putStringArrayListExtra("clublist", clubList);
                v.getContext().startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return clubModuleArrayList.size();
    }
    public class ViewHolder extends RecyclerView.ViewHolder{
        private TextView name,desc, category, email, member;
        private Button delete,view;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.cardClubName);
            desc = itemView.findViewById(R.id.cardClubDescription);
            category = itemView.findViewById(R.id.cardClubCategory);
            email = itemView.findViewById(R.id.cardClubAdvisorEmail);
            member = itemView.findViewById(R.id.cardClubMember);
            delete = itemView.findViewById(R.id.cardClubDelete);
            view = itemView.findViewById(R.id.cardClubViewActivity);
        }
    }
}
