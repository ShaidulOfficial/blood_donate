package com.shaidulsoftstudio.roktobindu.normalClass;

import android.app.Application;
import android.content.Context;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;


public class MyApplication extends Application {



    public  static void addToFavorite(Context context, String uid){

        //we can only add login user
        FirebaseAuth fauth=FirebaseAuth.getInstance();
        if (fauth.getCurrentUser()==null){
            Toast.makeText(context, "You are not logged in", Toast.LENGTH_SHORT).show();
        }else {

            long timestamp= System.currentTimeMillis();
            //setup data to add Fb to RDB of current user for favourite book
            HashMap<String ,Object> favouriteMap=new HashMap<>();
            favouriteMap.put("uid",uid);
            favouriteMap.put("timestamp",timestamp);
      //      favouriteMap.put("OkFavourite","Good");
//            favouriteMap.put("ActiveStatus",ActiveStatus);
//            favouriteMap.put("ActiveDate",ActiveDate);

            //save to RDB
            DatabaseReference dbreferadd= FirebaseDatabase.getInstance().getReference("Users");

            dbreferadd.child(fauth.getUid()).child("Favorites").child(uid)
                    .setValue(favouriteMap)
                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void unused) {

                            Toast.makeText(context, "added to your favourite list", Toast.LENGTH_SHORT).show();
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(context, "failed to add favourite"+e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

    public  static void removeFromFavorite(Context context, String uid){

        //we can only add login user
        long timestamp= System.currentTimeMillis();
        FirebaseAuth fauth=FirebaseAuth.getInstance();
        if (fauth.getCurrentUser()==null){
            Toast.makeText(context, "You are not logged in", Toast.LENGTH_SHORT).show();
        }else {

            //remove favourite to RDB
            DatabaseReference dbrefer=FirebaseDatabase.getInstance().getReference("Users");

            dbrefer.child(fauth.getUid()).child("Favorites").child(uid)
                    .removeValue()
                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void unused) {

                            Toast.makeText(context, "Remove your favourite list", Toast.LENGTH_SHORT).show();
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(context, "failed to remove favourite"+e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

}
