package com.shaidulsoftstudio.roktobindu.adapter;

import android.animation.LayoutTransition;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.transition.AutoTransition;
import android.transition.TransitionManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.AppCompatButton;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.shaidulsoftstudio.roktobindu.R;
import com.shaidulsoftstudio.roktobindu.activities.AllUserOfProfileActivity;
import com.shaidulsoftstudio.roktobindu.model.Users;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class PostforReceiverAdapter extends RecyclerView.Adapter<PostforReceiverAdapter.PostreceievrHolder> {
    Context context;
    List<Users> postRecevrList;

    public PostforReceiverAdapter(Context context, List<Users> postRecevrList) {
        this.context = context;
        this.postRecevrList = postRecevrList;
    }

    @NonNull
    @Override
    public PostreceievrHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.receiver_request_list_item,
                parent, false);
        return new PostreceievrHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PostreceievrHolder holder, int position) {
        Users modelPostRcvr = postRecevrList.get(position);
        String Uid = modelPostRcvr.getUid();
        Glide.with(context).load(modelPostRcvr.getProfilepic())
                .placeholder(R.drawable.profile_image).into(holder.requestlist_imageitem_rcvr);
        holder.patient_blood_group.setText(modelPostRcvr.getPatientBloodgroup());
        holder.rcverBloodgroup.setText(modelPostRcvr.getPatientBloodgroup());
        holder.targetUserType_rcvr.setText(" Blood " + modelPostRcvr.getUsertype());
        holder.targetMobano_rcvr.setText(modelPostRcvr.getMobileno());
        holder.nameDonor_rcvr.setText(modelPostRcvr.getFullname());
        holder.patientAge_rcvr.setText(modelPostRcvr.getPatientAge());
        holder.hospitalname.setText(modelPostRcvr.getHospitalName());
        holder.blood_bag_quantity.setText(modelPostRcvr.getBloodBagQuantity());
        holder.postTimeTv.setTag(modelPostRcvr.getDate() + " , " + modelPostRcvr.getTime());
        holder.fullnamcarv.setText(modelPostRcvr.getFullname());
        holder.patientType_rcvr.setText(modelPostRcvr.getPatientType());
        holder.postTimeTv.setText(modelPostRcvr.getDate() + " , " + modelPostRcvr.getTime());
        holder.from_location_rcvr.setText(modelPostRcvr.getVillage() + "," + modelPostRcvr.getUpzila() + "," + modelPostRcvr.getDistrict());
        Animation animation = AnimationUtils.loadAnimation(holder.itemView.getContext(), R.anim.anim1);
        holder.itemView.setAnimation(animation);

        holder.linlayof_title_carv.getLayoutTransition().enableTransitionType(LayoutTransition.CHANGING);

       holder.bloodImv.setOnClickListener(new View.OnClickListener() {
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

               yourBloodGroup.setText(modelPostRcvr.getPatientBloodgroup());
               if (modelPostRcvr.getPatientBloodgroup().equals("A+")){
                   canGive.setText("A+ \nAB+");
                   canTake.setText("A+ , A- \nO+ , O-");

               }else if (modelPostRcvr.getPatientBloodgroup().equals("A-")){
                   canGive.setText("A+ , A- \nAB+ , AB-");
                   canTake.setText("A- \nO-");

               }else if (modelPostRcvr.getPatientBloodgroup().equals("B+")){
                   canGive.setText("B+ \nAB+");
                   canTake.setText("B+ , B- \nO+ , O-");
               }else if (modelPostRcvr.getPatientBloodgroup().equals("B-")){
                   canGive.setText("B+ , B- \nAB+ , AB-");
                   canTake.setText("B- \nO-");

               }else if (modelPostRcvr.getPatientBloodgroup().equals("AB+")){
                   canGive.setText("AB+");
                   canTake.setText("All Group \n(সকল গ্রুপ)");

               }else if (modelPostRcvr.getPatientBloodgroup().equals("AB-")){
                   canGive.setText("AB+  AB-");
                   canTake.setText("A- , B-\nO- , AB-");

               }else if (modelPostRcvr.getPatientBloodgroup().equals("O+")){
                   canGive.setText("A+ , B+ \nAB+ , O+");
                   canTake.setText("O+ , O-");

               }else if (modelPostRcvr.getPatientBloodgroup().equals("O-")){
                   canGive.setText("All Group \n(সকল গ্রুপ)");
                   canTake.setText("O-");
               }else {

               }
               builder.setView(dialogView);
               builder.setCancelable(true);
               builder.show();
           }
       });
        holder.drop_up_image_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (holder.details_relay.getVisibility() == View.GONE) {
                    TransitionManager.beginDelayedTransition(holder.linlayof_title_carv, new AutoTransition());
                    holder.details_relay.setVisibility(View.VISIBLE);
                    holder.drop_up_image_btn.setBackgroundResource(R.drawable.arrow_up_24);
                } else {
                    TransitionManager.beginDelayedTransition(holder.linlayof_title_carv, new AutoTransition());
                    holder.details_relay.setVisibility(View.GONE);
                    holder.drop_up_image_btn.setBackgroundResource(R.drawable.arrow_down_24);
                }
            }
        });


        holder.listCallbtn_rcvr.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String mobileNo = modelPostRcvr.getMobileno();
                String call = "tel:" + mobileNo.trim();
                Intent intent = new Intent(Intent.ACTION_DIAL);
                intent.setData(Uri.parse(call));
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return postRecevrList.size();
    }

    public class PostreceievrHolder extends RecyclerView.ViewHolder {
        CircleImageView requestlist_imageitem_rcvr;
        TextView patient_blood_group, targetUserType_rcvr, targetMobano_rcvr, patientAge_rcvr, patientType_rcvr,
                nameDonor_rcvr,rcverBloodgroup,hospitalname,blood_bag_quantity, postedtime_rcvr, from_location_rcvr, fullnamcarv, postTimeTv;
        AppCompatButton listCallbtn_rcvr;
        ImageView bloodImv;
        CardView recvr_carV;
        Button drop_up_image_btn;
        LinearLayout linlayof_title_carv;
        RelativeLayout details_relay;

        public PostreceievrHolder(@NonNull View itemView) {
            super(itemView);

            bloodImv = itemView.findViewById(R.id.bloodImv);
            requestlist_imageitem_rcvr = itemView.findViewById(R.id.requestlist_imageitem_rcvr);
            patient_blood_group = itemView.findViewById(R.id.patient_blood_group);
            rcverBloodgroup = itemView.findViewById(R.id.rcverBloodgroup);
            targetUserType_rcvr = itemView.findViewById(R.id.targetUserType_rcvr);
            targetMobano_rcvr = itemView.findViewById(R.id.targetMobano_rcvr);
            patientAge_rcvr = itemView.findViewById(R.id.patientAge_rcvr);
            nameDonor_rcvr = itemView.findViewById(R.id.nameDonor_rcvr);
            blood_bag_quantity = itemView.findViewById(R.id.blood_bag_quantity);
            hospitalname = itemView.findViewById(R.id.hospitalname);
            from_location_rcvr = itemView.findViewById(R.id.from_location_rcvr);
            patientType_rcvr = itemView.findViewById(R.id.patientType_rcvr);
            listCallbtn_rcvr = itemView.findViewById(R.id.listCallbtn_rcvr);
            recvr_carV = itemView.findViewById(R.id.recvr_carV);
            linlayof_title_carv = itemView.findViewById(R.id.linlayof_title_carv);
            drop_up_image_btn = itemView.findViewById(R.id.drop_up_image_btn);
            details_relay = itemView.findViewById(R.id.details_relay);
            fullnamcarv = itemView.findViewById(R.id.fullnamcarv);
            postTimeTv = itemView.findViewById(R.id.postTimeTv);

        }
    }
}
