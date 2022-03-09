package com.example.myclub.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myclub.R;
import com.example.myclub.activities.CreateClubActivity;
import com.example.myclub.clubAPI.ClubModule;
import com.example.myclub.studentAPI.StudentModule;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class MemberAdpater  extends RecyclerView.Adapter<MemberAdpater.ViewHolder> {
    private Context context;
    private ArrayList<StudentModule> studentModuleArrayList = new ArrayList<>();
    public String studentID, studentEmail,studentPassword,studentName, isAdvisor, token;
    public ArrayList<String> clubList = new ArrayList<>();
    ArrayList<String> clubMember = new ArrayList<>();

    private FirebaseFirestore firebaseFirestore;

    public MemberAdpater(Context context, ArrayList<StudentModule> studentModuleArrayList, String studentID, String studentEmail, String studentPassword, String studentName, String isAdvisor, ArrayList<String> clubList,ArrayList<String> clubMember, String token) {
        this.context = context;
        this.studentModuleArrayList = studentModuleArrayList;
        this.studentID = studentID;
        this.studentEmail = studentEmail;
        this.studentPassword = studentPassword;
        this.studentName = studentName;
        this.isAdvisor = isAdvisor;
        this.clubList = clubList;
        this.clubMember = clubMember;
        this.token =token;
    }

    @NonNull
    @Override
    public MemberAdpater.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.view_members_card, parent, false);
        MemberAdpater.ViewHolder holder = new MemberAdpater.ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull MemberAdpater.ViewHolder holder, int position) {
        System.out.println("Get with position "+ studentModuleArrayList.get(position).getStudentID());
        System.out.println("Get with holder "+ studentModuleArrayList.get(holder.getAdapterPosition()).getStudentEmail());

        holder.email.setText(studentModuleArrayList.get(holder.getAdapterPosition()).getStudentID());
        holder.name.setText(studentModuleArrayList.get(holder.getAdapterPosition()).getStudentName());

        holder.delete.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {

                if(token == null || clubMember.isEmpty() || studentModuleArrayList.get(holder.getAdapterPosition()).getStudentID().equals(studentEmail)){
                    Toast.makeText(v.getContext(), "Could not remove member",Toast.LENGTH_LONG).show();
                }else {
                    ArrayList<String> studentClubs = new ArrayList<>();
                    String sID = studentModuleArrayList.get(holder.getAdapterPosition()).getStudentEmail();
                    System.out.println("Before remove"+clubMember);
                    //update club member list
                    clubMember.remove(sID);
                    System.out.println("after remove"+clubMember);

                    //update student club list
                    studentClubs.addAll(studentModuleArrayList.get(holder.getAdapterPosition()).getClubList());
                    studentClubs.remove(clubList.get(0).toString());

                    firebaseFirestore = FirebaseFirestore.getInstance();
                    DocumentReference record = firebaseFirestore.collection("ClubData").document(token);
                    record.update("clubMemberList", clubMember).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            Toast.makeText(v.getContext(), "Member Removed !", Toast.LENGTH_SHORT).show();
                            clubMember.clear();
                            DocumentReference record = FirebaseFirestore.getInstance().collection("StudentData").document(studentModuleArrayList.get(holder.getAdapterPosition()).getStudentID());
                            record.update("clubList", studentClubs)
                                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void aVoid) {
                                            //delete from the ui
                                            studentModuleArrayList.remove(holder.getAdapterPosition());
                                            notifyItemRemoved(holder.getAdapterPosition());
                                            notifyItemRangeChanged(holder.getAdapterPosition(), studentModuleArrayList.size());

                                        }
                                    }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Toast.makeText(v.getContext(), "Could not remove member", Toast.LENGTH_LONG).show();
                                }
                            });
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(v.getContext(), "Something went wrong, check the internet connection", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }
        });


    }

    @Override
    public int getItemCount() {
        return studentModuleArrayList.size();
    }
    public class ViewHolder extends RecyclerView.ViewHolder{
        private TextView name, email;
        private Button delete;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.cardName);
            email = itemView.findViewById(R.id.cardEmail);
            delete = itemView.findViewById(R.id.cardRemove);
            }
    }
}
