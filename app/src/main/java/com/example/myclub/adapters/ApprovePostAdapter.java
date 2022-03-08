package com.example.myclub.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myclub.R;
import com.example.myclub.classroomAPI.ClassroomModule;
import com.example.myclub.postAPI.PostModule;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class ApprovePostAdapter extends RecyclerView.Adapter<ApprovePostAdapter.ViewHolder> {
    private Context context;
    private ArrayList<PostModule> postModuleArrayList = new ArrayList<>();
    private FirebaseFirestore firebaseFirestore;

    public ApprovePostAdapter(Context context, ArrayList<PostModule> postModuleArrayList) {
        this.context = context;
        this.postModuleArrayList = postModuleArrayList;
    }

    @NonNull
    @Override
    public ApprovePostAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.manage_post_activity, parent, false);
        ApprovePostAdapter.ViewHolder holder = new ApprovePostAdapter.ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ApprovePostAdapter.ViewHolder holder, int position) {
        System.out.println("Get with position "+ postModuleArrayList.get(position).getPostID());
        System.out.println("Get with holder "+ postModuleArrayList.get(holder.getAdapterPosition()).getAdvisorEmail());

        holder.title.setText(postModuleArrayList.get(holder.getAdapterPosition()).getPostTitle());
        holder.advisorEmail.setText(postModuleArrayList.get(holder.getAdapterPosition()).getAdvisorEmail());
        holder.clubName.setText(postModuleArrayList.get(holder.getAdapterPosition()).getClubID());
        holder.desc.setText(postModuleArrayList.get(holder.getAdapterPosition()).getPostDescription());
        holder.status.setText(postModuleArrayList.get(holder.getAdapterPosition()).getIsApproved());
        //set the image
        Picasso.with(this.context).load(postModuleArrayList.get(position).getPostImgUrl()).fit().into(holder.imageView);
        String tok= postModuleArrayList.get(holder.getAdapterPosition()).getToken();
        //update the status
        holder.edStatus.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                firebaseFirestore = FirebaseFirestore.getInstance();
                DocumentReference record = firebaseFirestore.collection("PostData").document(tok);
                record.update("isApproved", "Published")
                        .addOnSuccessListener(new OnSuccessListener< Void >() {
                            @SuppressLint("ResourceAsColor")
                            @Override
                            public void onSuccess(Void aVoid) {

                                Toast.makeText(v.getContext(), "Post published", Toast.LENGTH_LONG).show();
                                //delete from the ui
                                postModuleArrayList.remove(holder.getAdapterPosition());
                                notifyItemRemoved(holder.getAdapterPosition());
                                notifyItemRangeChanged(holder.getAdapterPosition(), postModuleArrayList.size());

                            }
                        }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(v.getContext(), "Something went wrong, check the internet connection",Toast.LENGTH_LONG).show();
                    }
                });
            }
        });

    }

    @Override
    public int getItemCount() {
        return postModuleArrayList.size();
    }
    public class ViewHolder extends RecyclerView.ViewHolder{
        private TextView title,clubName, advisorEmail, desc, status;
        private Button edStatus;
        private ImageView imageView;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.approveTitle);
            clubName = itemView.findViewById(R.id.approveClubName);
            advisorEmail = itemView.findViewById(R.id.approveAdvisorEmail);
            desc = itemView.findViewById(R.id.approveDescription);
            status = itemView.findViewById(R.id.approveStatus);
            edStatus = itemView.findViewById(R.id.approveEditStatus);
            imageView = itemView.findViewById(R.id.approveImage);
        }
    }
}
