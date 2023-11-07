package com.shaidulsoftstudio.roktobindu.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
;
import com.github.marlonlom.utilities.timeago.TimeAgo;
import com.google.firebase.auth.FirebaseAuth;
import com.shaidulsoftstudio.roktobindu.R;
import com.shaidulsoftstudio.roktobindu.activities.AllUserOfProfileActivity;
import com.shaidulsoftstudio.roktobindu.model.Users;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class UserAdapter extends RecyclerView.Adapter<UserAdapter.viewHolder> {

    Context context;
    List<Users> usersList;

    public UserAdapter(Context context, List<Users> usersList) {
        this.context = context;
        this.usersList = usersList;
    }

    @NonNull
    @Override
    public viewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_user_lay, parent, false);
        return new viewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull viewHolder holder, int position) {
        Users model = usersList.get(position);
        String Uid = model.getUid();
        String memberID = String.valueOf(model.getMemberID());
        holder.memberIDno.setText(memberID);
        holder.grouptv.setText(model.getBloodGroup());
        holder.nametv.setText(model.getFullname());
        holder.districttv.setText(model.getDistrict());
        holder.typetv.setText(model.getUsertype());
        String totalDonation = String.valueOf(model.getTotalDonation());
        holder.total_donation_Tv.setText(totalDonation);
        String nextDonateDay = String.valueOf(model.getNextDonate());
        holder.nextDateTv.setText(nextDonateDay);
        Glide.with(context).load(model.getProfilepic()).placeholder(R.drawable.profile_image)
                .into(holder.profile_image);

        Animation animation = AnimationUtils.loadAnimation(holder.itemView.getContext(), R.anim.anim1);
        holder.itemView.setAnimation(animation);

        if (model.getUsertype().equals("Donor")) {

            holder.availabeTv.setVisibility(View.VISIBLE);
            holder.availabeTv.setText(R.string.available);
            holder.activeLightgreen.setVisibility(View.VISIBLE);
            holder.activeLightred.setVisibility(View.GONE);
            holder.postDateTv.setVisibility(View.GONE);
            holder.nextDateTv.setVisibility(View.GONE);

        } else {
            Toast.makeText(context, "ok Donor", Toast.LENGTH_SHORT).show();
        }

        if (model.getActiveStatus().equals("Yes")) {
            holder.availabeTv.setVisibility(View.VISIBLE);
            holder.availabeTv.setText(R.string.notavailable);
            holder.availabeTv.setTextColor(Color.RED);
            holder.activeLightgreen.setVisibility(View.GONE);
            holder.activeLightred.setVisibility(View.VISIBLE);
            holder.postedTv.setVisibility(View.VISIBLE);
            holder.postDateTv.setVisibility(View.VISIBLE);
            holder.nextDonateLinlay.setVisibility(View.VISIBLE);
            holder.nextdayTv.setVisibility(View.VISIBLE);
            holder.postDateTv.setText(TimeAgo.using(model.getInactiveTime()));
            holder.nextDateTv.setVisibility(View.VISIBLE);

        } else {

        }
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, AllUserOfProfileActivity.class);
                intent.putExtra("userId", Uid);
                context.startActivity(intent);
            }
        });
        holder.blood_group_Iv.setOnClickListener(new View.OnClickListener() {
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

                yourBloodGroup.setText(model.getBloodGroup());
                if (model.getBloodGroup().equals("A+")){
                    canGive.setText("A+ \nAB+");
                    canTake.setText("A+ , A- \nO+ , O-");

                }else if (model.getBloodGroup().equals("A-")){
                    canGive.setText("A+ , A- \nAB+ , AB-");
                    canTake.setText("A- \nO-");

                }else if (model.getBloodGroup().equals("B+")){
                    canGive.setText("B+ \nAB+");
                    canTake.setText("B+ , B- \nO+ , O-");
                }else if (model.getBloodGroup().equals("B-")){
                    canGive.setText("B+ , B- \nAB+ , AB-");
                    canTake.setText("B- \nO-");

                }else if (model.getBloodGroup().equals("AB+")){
                    canGive.setText("AB+");
                    canTake.setText("All Group \n(সকল গ্রুপ)");

                }else if (model.getBloodGroup().equals("AB-")){
                    canGive.setText("AB+  AB-");
                    canTake.setText("A- , B-\nO- , AB-");

                }else if (model.getBloodGroup().equals("O+")){
                    canGive.setText("A+ , B+ \nAB+ , O+");
                    canTake.setText("O+ , O-");

                }else if (model.getBloodGroup().equals("O-")){
                    canGive.setText("All Group \n(সকল গ্রুপ)");
                    canTake.setText("O-");
                }else {

                }
                builder.setView(dialogView);
                builder.setCancelable(true);
                builder.show();

            }
        });
        holder.favouriteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, AllUserOfProfileActivity.class);
                intent.putExtra("userId", Uid);
                context.startActivity(intent);
            }
        });
        holder.share_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String nameWithmobileNo = model.getUsertype() + " , " + "\n" + model.getFullname() + " , " + "\n" + model.getMobileno();
                Intent intent = new Intent(Intent.ACTION_SEND);
                intent.setType("text/plain");
                intent.putExtra(Intent.EXTRA_SUBJECT, "Write your subject here");
                intent.putExtra(Intent.EXTRA_TEXT, nameWithmobileNo);
                context.startActivity(Intent.createChooser(intent, "Share by"));
            }
        });
        holder.callbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String mobileNo = model.getMobileno();
                String call = "tel:" + mobileNo.trim();
                Intent intent = new Intent(Intent.ACTION_DIAL);
                intent.setData(Uri.parse(call));
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return usersList.size();
    }

    public class viewHolder extends RecyclerView.ViewHolder {
        CircleImageView profile_image;
        LinearLayout nextDonateLinlay;
        ImageView blood_group_Iv, favouriteBtn, callbtn, share_btn, activeLightgreen, activeLightred;
        TextView memberIDno, postedTv, total_donation_Tv, typetv, grouptv, nametv, districttv, availabeTv, nextTimeTv, nextdayTv, postDateTv, nextDateTv;
        RelativeLayout favourite_relay;

        public viewHolder(@NonNull View itemView) {
            super(itemView);
            blood_group_Iv = itemView.findViewById(R.id.blood_group_Iv);
            nextDonateLinlay = itemView.findViewById(R.id.nextDonateLinlay);
            postedTv = itemView.findViewById(R.id.postedTv);
            favourite_relay = itemView.findViewById(R.id.favourite_relay);
            profile_image = itemView.findViewById(R.id.profile_imageitem);
            typetv = itemView.findViewById(R.id.typeTv);
            total_donation_Tv = itemView.findViewById(R.id.total_donation_Tv);
            availabeTv = itemView.findViewById(R.id.availabeTv);
            nametv = itemView.findViewById(R.id.detailFullName);
            nextDateTv = itemView.findViewById(R.id.nextDateTxtv);
            nextdayTv = itemView.findViewById(R.id.nextdayTv);
            postDateTv = itemView.findViewById(R.id.postDateTv);
            nextTimeTv = itemView.findViewById(R.id.nextTimeTv);
            grouptv = itemView.findViewById(R.id.detailBloodGroup);
            districttv = itemView.findViewById(R.id.detaildist);
            callbtn = itemView.findViewById(R.id.callbtn);
            favouriteBtn = itemView.findViewById(R.id.favouriteBtn);
            share_btn = itemView.findViewById(R.id.share_btn);
            activeLightgreen = itemView.findViewById(R.id.activeLight);
            activeLightred = itemView.findViewById(R.id.activeLightred);
            memberIDno = itemView.findViewById(R.id.memberIDno);
        }
    }
}
