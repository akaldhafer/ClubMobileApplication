package com.example.myclub.adapters;

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
import com.example.myclub.activities.ClubPostList;
import com.example.myclub.activities.ViewMyClubPosts;
import com.example.myclub.clubAPI.ClubModule;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;

public class JoinClubAdapter extends RecyclerView.Adapter<JoinClubAdapter.ViewHolder>{
    private Context context;
    private ArrayList<ClubModule> clubModuleArrayList = new ArrayList<>();
    public String studentID, studentEmail,studentPassword,studentName, isAdvisor;
    public ArrayList<String> clubList;
    public ArrayList<String> clubMembers =new ArrayList<>();
    private FirebaseFirestore firebaseFirestore;

    public JoinClubAdapter(Context context, ArrayList<ClubModule> clubModuleArrayList, String studentID, String studentEmail, String studentPassword, String studentName, String isAdvisor, ArrayList<String> clubList) {
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
    public JoinClubAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.join_club_card, parent, false);
        JoinClubAdapter.ViewHolder holder = new JoinClubAdapter.ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull JoinClubAdapter.ViewHolder holder, int position) {
        System.out.println("Get with position "+clubModuleArrayList.get(position).getClubAdvisor());
        System.out.println("Get with member list: "+clubModuleArrayList.get(holder.getAdapterPosition()).getClubMemberList());

        holder.email.setText(clubModuleArrayList.get(holder.getAdapterPosition()).getClubAdvisor());
        holder.name.setText(clubModuleArrayList.get(holder.getAdapterPosition()).getClubName());
        holder.desc.setText(clubModuleArrayList.get(holder.getAdapterPosition()).getClubDescription());
        holder.category.setText(clubModuleArrayList.get(holder.getAdapterPosition()).getClubType());
        String tok= clubModuleArrayList.get(holder.getAdapterPosition()).getToken();
        String advisorEmail = clubModuleArrayList.get(holder.getAdapterPosition()).getClubAdvisor();
        holder.join.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                clubMembers.addAll(clubModuleArrayList.get(holder.getAdapterPosition()).getClubMemberList());
                clubMembers.add(studentEmail);
                System.out.println("updated member list: "+clubMembers);

                DocumentReference record = FirebaseFirestore.getInstance().collection("ClubData").document(tok);
                record.update("clubMemberList",clubMembers).addOnSuccessListener(new OnSuccessListener< Void >() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        //update advisor role
                        clubList.add(clubModuleArrayList.get(holder.getAdapterPosition()).getClubName());
                        DocumentReference record = FirebaseFirestore.getInstance().collection("StudentData").document(studentEmail);
                        record.update("clubList",clubList)
                                .addOnSuccessListener(new OnSuccessListener< Void >() {
                                    @Override
                                    public void onSuccess(Void aVoid) {
                                        Toast.makeText(v.getContext(), "You Joined the Club", Toast.LENGTH_LONG).show();
                                        holder.join.setText("Joined");
                                        holder.join.setClickable(false);


                                    }
                                }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(v.getContext(), "Could not join the club",Toast.LENGTH_SHORT).show();
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
                Intent intent = new Intent(v.getContext(), ViewMyClubPosts.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.putExtra("club", tok);
                intent.putExtra("studentID", studentID);
                intent.putExtra("email", studentEmail);
                intent.putExtra("password",studentPassword);
                intent.putExtra("name", studentName);
                intent.putExtra("role", isAdvisor);
                intent.putExtra("advisorEmail",advisorEmail);
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
        private TextView name,desc, category, email;
        private Button join,view;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.cardClubName2);
            desc = itemView.findViewById(R.id.cardClubDescription2);
            category = itemView.findViewById(R.id.cardClubCategory2);
            email = itemView.findViewById(R.id.cardClubAdvisorEmail2);
            join = itemView.findViewById(R.id.cardJoinClub);
            view = itemView.findViewById(R.id.cardClubViewActivity2);
        }
    }

}
