package com.shaidulsoftstudio.roktobindu.adapter;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.AppCompatButton;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.shaidulsoftstudio.roktobindu.R;
import com.shaidulsoftstudio.roktobindu.activities.AllUserOfProfileActivity;
import com.shaidulsoftstudio.roktobindu.activities.MainActivity;
import com.shaidulsoftstudio.roktobindu.model.Users;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class PostAdapter extends RecyclerView.Adapter<PostAdapter.postViewHolder> {

    Context context;
    List<Users> postList;
    String currentUserID;

    public PostAdapter(Context context, List<Users> postList, String currentUserID) {
        this.context = context;
        this.postList = postList;
        this.currentUserID = currentUserID;
    }

    @NonNull
    @Override
    public postViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.request_list_item, parent
                , false);
        return new postViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull postViewHolder holder, int position) {
        Users modelPost = postList.get(position);
        String Uid = modelPost.getUid();
        holder.targetBloodgroup.setText(modelPost.getBloodGroup());
        holder.targetUserType.setText(modelPost.getUsertype());
        holder.targetMobano.setText(modelPost.getMobileno());
        holder.nameDonor.setText(" " + modelPost.getFullname());
        holder.genderlistpost.setText(modelPost.getGender());
        holder.memberIdno.setText(modelPost.getMemberID());
        holder.postedtime.setText(modelPost.getActiveDate()+" , "+modelPost.getActiveTime());
        holder.from_location.setText(modelPost.getVillage() + "," + modelPost.getUpzila() + "," + modelPost.getDistrict()+ "," + modelPost.getUnion());
        Glide.with(context).load(modelPost.getProfilepic()).placeholder(R.drawable.profile_image)
                .into(holder.requestlist_imageitem);

        Animation animation = AnimationUtils.loadAnimation(holder.itemView.getContext(), R.anim.anim1);
        holder.itemView.setAnimation(animation);
                holder.listCallbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String mobileNo = modelPost.getMobileno();
                String call = "tel:" + mobileNo.trim();
                Intent intent = new Intent(Intent.ACTION_DIAL);
                intent.setData(Uri.parse(call));
                context.startActivity(intent);
            }
        });

        holder.blood_groupIv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(v.getRootView().getContext());
                View dialogView = LayoutInflater.from(v.getRootView().getContext()).inflate(R.layout.blood_group_who_give_donte, null);
                TextView yourBloodGroup;
                TextView canGive;
                TextView canTake;
                yourBloodGroup = dialogView.findViewById(R.id.your_blood_groupTv);
                canGive = dialogView.findViewById(R.id.give_blood_Tv);
                canTake = dialogView.findViewById(R.id.take_blood_Tv);

                yourBloodGroup.setText(modelPost.getBloodGroup());
                if (modelPost.getBloodGroup().equals("A+")){
                    canGive.setText("A+ \nAB+");
                    canTake.setText("A+ , A- \nO+ , O-");

                }else if (modelPost.getBloodGroup().equals("A-")){
                    canGive.setText("A+ , A- \nAB+ , AB-");
                    canTake.setText("A- \nO-");

                }else if (modelPost.getBloodGroup().equals("B+")){
                    canGive.setText("B+ \nAB+");
                    canTake.setText("B+ , B- \nO+ , O-");
                }else if (modelPost.getBloodGroup().equals("B-")){
                    canGive.setText("B+ , B- \nAB+ , AB-");
                    canTake.setText("B- \nO-");

                }else if (modelPost.getBloodGroup().equals("AB+")){
                    canGive.setText("AB+");
                    canTake.setText("All Group \n(সকল গ্রুপ)");

                }else if (modelPost.getBloodGroup().equals("AB-")){
                    canGive.setText("AB+  AB-");
                    canTake.setText("A- , B-\nO- , AB-");

                }else if (modelPost.getBloodGroup().equals("O+")){
                    canGive.setText("A+ , B+ \nAB+ , O+");
                    canTake.setText("O+ , O-");

                }else if (modelPost.getBloodGroup().equals("O-")){
                    canGive.setText("All Group \n(সকল গ্রুপ)");
                    canTake.setText("O-");
                }else {

                }
                builder.setView(dialogView);
                builder.setCancelable(true);
                builder.show();

            }
        });

        holder.requestlist_imageitem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, AllUserOfProfileActivity.class);
                intent.putExtra("userId", Uid);
                context.startActivity(intent);
            }
        });
        holder.list_share_donor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String nameWithmobileNo =modelPost.getUsertype()+" , "+"\n"+modelPost.getFullname()+" , "+"\n"+ modelPost.getMobileno();
                Intent intent=new Intent(Intent.ACTION_SEND);
                intent.setType("text/plain");
                intent.putExtra(Intent.EXTRA_SUBJECT,"Write your subject here");
                intent.putExtra(Intent.EXTRA_TEXT,nameWithmobileNo);
                context.startActivity(Intent.createChooser(intent,"Share by"));

            }
        });


    }


    @Override
    public int getItemCount() {
        return postList.size();
    }

    public class postViewHolder extends RecyclerView.ViewHolder {
        CircleImageView requestlist_imageitem;
        TextView targetBloodgroup, targetUserType, targetMobano,
                nameDonor, postedtime, memberIdno, from_location, genderlistpost;
        AppCompatButton listCallbtn,list_share_donor;
        ImageView blood_groupIv;

        public postViewHolder(@NonNull View itemView) {
            super(itemView);

            blood_groupIv = itemView.findViewById(R.id.blood_groupIv);
            requestlist_imageitem = itemView.findViewById(R.id.requestlist_imageitem);
            targetBloodgroup = itemView.findViewById(R.id.targetBloodgroup);
            targetUserType = itemView.findViewById(R.id.targetUserType);
            targetMobano = itemView.findViewById(R.id.targetMobano);
            memberIdno = itemView.findViewById(R.id.memberIdno);
            nameDonor = itemView.findViewById(R.id.nameDonor);
            postedtime = itemView.findViewById(R.id.postedtime);
            genderlistpost = itemView.findViewById(R.id.genderlistpost);
            from_location = itemView.findViewById(R.id.from_location);
            listCallbtn = itemView.findViewById(R.id.listCallbtn);
            list_share_donor = itemView.findViewById(R.id.list_share_donor);

        }
    }
}
