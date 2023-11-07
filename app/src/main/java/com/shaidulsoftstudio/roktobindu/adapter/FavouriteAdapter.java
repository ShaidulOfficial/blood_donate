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
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.shaidulsoftstudio.roktobindu.normalClass.MyApplication;
import com.shaidulsoftstudio.roktobindu.R;
import com.shaidulsoftstudio.roktobindu.activities.AllUserOfProfileActivity;
import com.shaidulsoftstudio.roktobindu.model.Users;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class FavouriteAdapter extends RecyclerView.Adapter<FavouriteAdapter.viewHolder>  {

    Context context;
    List<Users>usersList;

    public FavouriteAdapter(Context context, List<Users> usersList) {
        this.context = context;
        this.usersList = usersList;
    }

    @NonNull
    @Override
    public viewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
       View view=LayoutInflater.from(context).inflate(R.layout.favourite_item_user_lay,parent,false);
               return new viewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull viewHolder holder, int position) {

        Users model = usersList.get(position);
        loadfavouriteDetails(model, holder);
        String Uid = model.getUid();

        Animation animation = AnimationUtils.loadAnimation(holder.itemView.getContext(), R.anim.anim2);
        holder.itemView.setAnimation(animation);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, AllUserOfProfileActivity.class);
                intent.putExtra("userId", Uid);
                context.startActivity(intent);
            }
        });
        holder.sharebtnfav.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String nameWithmobileNo =model.getUsertype()+" , "+"\n"+ model.getFullname()+" , "+"\n"+ model.getMobileno();
                Intent intent=new Intent(Intent.ACTION_SEND);
                intent.setType("text/plain");
                intent.putExtra(Intent.EXTRA_SUBJECT,"Write your subject here");
                intent.putExtra(Intent.EXTRA_TEXT,nameWithmobileNo);
                context.startActivity(Intent.createChooser(intent,"Share by"));

            }
        });
        holder.callbtnfav.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String mobileNo = model.getMobileno();
                String call1 = "tel:" + mobileNo;
                Intent intent = new Intent(Intent.ACTION_DIAL);
                intent.setData(Uri.parse(call1));
                context.startActivity(intent);
            }
        });
        holder.deletebtnfav.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MyApplication.removeFromFavorite(context,model.getUid());

            }
        });

    }

    private void loadfavouriteDetails(Users model, viewHolder holder) {

            String uId = model.getUid();
            DatabaseReference dbref = FirebaseDatabase.getInstance().getReference("Users");
            dbref.child(uId)
                    .addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {

                            String name = snapshot.child("fullname").getValue().toString();
                            String type =  snapshot.child("Usertype").getValue().toString();
                            String bloodgroup =  snapshot.child("bloodGroup").getValue().toString();
                            String district = snapshot.child("District").getValue().toString();
                            String uid = snapshot.child("uid").getValue().toString();
                            String profilePic =  snapshot.child("Profilepic").getValue().toString();
                            String activeDate =  snapshot.child("ActiveDate").getValue().toString();
                            String activeStatus =  snapshot.child("ActiveStatus").getValue().toString();
                            String memberId_fav =  snapshot.child("memberID").getValue().toString();

                            model.setMemberID(memberId_fav);
                            model.setFullname(name);
                            model.setUsertype(type);
                            model.setBloodGroup(bloodgroup);
                            model.setProfilepic(profilePic);
                            model.setDistrict(district);
                            model.setActiveStatus(activeStatus);
                            model.setActiveDate(activeDate);
                            model.setUid(uid);
                            Glide.with(context).load(profilePic).placeholder(R.drawable.profile_image).into(holder.profile_image);
                            holder.nametvfav.setText(name);
                            holder.typetvfav.setText(type);
                            holder.grouptvfav.setText(bloodgroup);
                            holder.districttvfav.setText(district);
                            holder.memberId_fav.setText(memberId_fav);

                            if (model.getUsertype().equals("Donor")){
                                holder.availabeTvfav.setVisibility(View.VISIBLE);
                                holder.availabeTvfav.setText(R.string.available);
                                holder.activeLightgreenfav.setVisibility(View.VISIBLE);
                                holder.activeLightredfav.setVisibility(View.GONE);
                                holder.nextDateTvfav.setVisibility(View.GONE);
                                holder.nextTimeTvfav.setVisibility(View.GONE);
                            }else {

                                Toast.makeText(context, "ok Donor", Toast.LENGTH_SHORT).show();
                            }
                            if (model.getActiveStatus().equals("Yes")){
                                holder.availabeTvfav.setVisibility(View.VISIBLE);
                                holder.availabeTvfav.setText(R.string.available);
                                holder.activeLightgreenfav.setVisibility(View.VISIBLE);
                                holder.activeLightredfav.setVisibility(View.GONE);
                                holder.nextDateTvfav.setVisibility(View.GONE);
                                holder.nextTimeTvfav.setVisibility(View.GONE);

                            }
                            else {
                                Toast.makeText(context, "ok Donor", Toast.LENGTH_SHORT).show();
                            }

                            if (model.getActiveStatus().equals("No")){
                                holder.availabeTvfav.setVisibility(View.VISIBLE);
                                holder.availabeTvfav.setText(R.string.notavailable);
                                holder.availabeTvfav.setTextColor(Color.RED);
                                holder.activeLightgreenfav.setVisibility(View.GONE);
                                holder.activeLightredfav.setVisibility(View.VISIBLE);
                                holder.nextDateTvfav.setVisibility(View.VISIBLE);
                                holder.nextDateTvfav.setText(model.getActiveDate());
                                holder.nextTimeTvfav.setVisibility(View.VISIBLE);
                            }

                            else {
                                Toast.makeText(context, "ok Donor", Toast.LENGTH_SHORT).show();
                            }



                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {
                            Toast.makeText(context, "failed", Toast.LENGTH_SHORT).show();
                        }
                    });

    }

    @Override
    public int getItemCount() {
        return usersList.size();
    }

    public class viewHolder extends RecyclerView.ViewHolder{

        CircleImageView profile_image;
        ImageView sharebtnfav, callbtnfav,deletebtnfav,activeLightgreenfav,activeLightredfav;
        TextView memberId_fav,typetvfav, grouptvfav, nametvfav, districttvfav,availabeTvfav,nextTimeTvfav,nextDateTvfav;

        public viewHolder(@NonNull View itemView) {
            super(itemView);

            sharebtnfav = itemView.findViewById(R.id.sharebtnfav);
            memberId_fav = itemView.findViewById(R.id.memberId_fav);
            profile_image = itemView.findViewById(R.id.profile_imagefav);
            typetvfav = itemView.findViewById(R.id.typeTvfav);
            nametvfav = itemView.findViewById(R.id.detailFullNamefav);
            grouptvfav = itemView.findViewById(R.id.detailBloodGroupfav);
            districttvfav = itemView.findViewById(R.id.detaildistfav);
            callbtnfav = itemView.findViewById(R.id.callbtnfav);
            deletebtnfav = itemView.findViewById(R.id.deletebtn);
            activeLightgreenfav = itemView.findViewById(R.id.activeLightgreenfav);
            activeLightredfav = itemView.findViewById(R.id.activeLightredfav);
            availabeTvfav = itemView.findViewById(R.id.availabeTvfav);
            nextTimeTvfav = itemView.findViewById(R.id.nextTimeTvfav);
            nextDateTvfav = itemView.findViewById(R.id.nextDateTvfav);
        }
    }
    }

