package com.example.myclub.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myclub.R;
import com.example.myclub.postAPI.PostModule;
import com.google.firebase.firestore.FirebaseFirestore;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class ViewClubPostAdapter extends RecyclerView.Adapter<ViewClubPostAdapter.ViewHolder> {
    private Context context;
    private ArrayList<PostModule> postModuleArrayList = new ArrayList<>();
    private FirebaseFirestore firebaseFirestore;

    public ViewClubPostAdapter(Context context, ArrayList<PostModule> postModuleArrayList) {
        this.context = context;
        this.postModuleArrayList = postModuleArrayList;
    }

    @NonNull
    @Override
    public ViewClubPostAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.view_clubposts_card, parent, false);
        ViewClubPostAdapter.ViewHolder holder = new ViewClubPostAdapter.ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewClubPostAdapter.ViewHolder holder, int position) {
        System.out.println("Get with position "+ postModuleArrayList.get(position).getPostID());
        System.out.println("Get with holder "+ postModuleArrayList.get(holder.getAdapterPosition()).getAdvisorEmail());

        holder.title.setText(postModuleArrayList.get(holder.getAdapterPosition()).getPostTitle());
        holder.advisorEmail.setText(postModuleArrayList.get(holder.getAdapterPosition()).getAdvisorEmail());
        holder.clubName.setText(postModuleArrayList.get(holder.getAdapterPosition()).getClubID());
        holder.desc.setText(postModuleArrayList.get(holder.getAdapterPosition()).getPostDescription());
        holder.token.setText(postModuleArrayList.get(holder.getAdapterPosition()).getToken());
        //set the image
        Picasso.with(this.context).load(postModuleArrayList.get(position).getPostImgUrl()).fit().into(holder.imageView);
        String tok= postModuleArrayList.get(holder.getAdapterPosition()).getToken();
        //update the status

    }

    @Override
    public int getItemCount() {
        return postModuleArrayList.size();
    }
    public class ViewHolder extends RecyclerView.ViewHolder{
        private TextView title,clubName, advisorEmail, desc, token;
        private ImageView imageView;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.approveTitle3);
            clubName = itemView.findViewById(R.id.approveClubName3);
            advisorEmail = itemView.findViewById(R.id.approveAdvisorEmail3);
            desc = itemView.findViewById(R.id.approveDescription3);
            token = itemView.findViewById(R.id.token1);
            imageView = itemView.findViewById(R.id.approveImage3);
        }
    }
}
