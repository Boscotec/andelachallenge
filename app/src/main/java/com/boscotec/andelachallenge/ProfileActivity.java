package com.boscotec.andelachallenge;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ResolveInfo;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.boscotec.andelachallenge.utility.ConnectivityReceiver;
import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Johnbosco on 26-Jul-17.
 */

public class ProfileActivity extends AppCompatActivity
        implements ConnectivityReceiver.ConnectivityReceiverListener, View.OnClickListener {

    TextView profile_url;
    ImageButton share;
    String username_tv, profile_pix_tv;
    String profile_url_tv;

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        if(getIntent()!=null){
           Bundle bundle = getIntent().getExtras();
           username_tv = bundle.getString("username");
           profile_pix_tv = bundle.getString("profile_pix");
           profile_url_tv = bundle.getString("profile_url");
        }

        ImageView profile_pix = (ImageView) findViewById(R.id.profile_pix);
        TextView username = (TextView) findViewById(R.id.username);
        profile_url =(TextView) findViewById(R.id.profile_url);
        share = (ImageButton) findViewById(R.id.share_button);
        profile_url.setOnClickListener(this);
        share.setOnClickListener(this);

        username.setText(username_tv);
        profile_url.setText(profile_url_tv);
        Glide.with(profile_pix.getContext())
                .load(profile_pix_tv)
                .crossFade()
                .into(profile_pix);
    }

    @Override
    public void onClick(View view){

        if(view == share){
            String message = "Check out this awesome developer @" + username_tv +", " + profile_url_tv;
            share(message);
        }

        if(view == profile_url){

        }
    }


    @Override
    protected void onResume() {
        super.onResume();
        // register connection status listener
        MyApplication.getInstance().setConnectivityListener(this);
    }

    @Override
    public void onNetworkConnectionChanged(boolean isConnected) {
        //showSnack(isConnected);
    }

    public void share(String message) {

            List<Intent> targetShareIntents=new ArrayList<Intent>();
            Intent shareIntent=new Intent();
            shareIntent.setAction(Intent.ACTION_SEND);
            shareIntent.setType("text/plain");

            List<ResolveInfo> resInfor =  this.getPackageManager().queryIntentActivities(shareIntent, 0);

            if(!resInfor.isEmpty()){
                for(ResolveInfo resInfo : resInfor){
                    String packageName=resInfo.activityInfo.packageName;

                    Intent intent=new Intent();
                    intent.setComponent(new ComponentName(packageName, resInfo.activityInfo.name));
                    intent.setAction(Intent.ACTION_SEND);
                    intent.setType("text/*");
                    intent.putExtra(Intent.EXTRA_TEXT, message);
                    intent.setPackage(packageName);
                    targetShareIntents.add(intent);

                }

                if(!targetShareIntents.isEmpty()){
                    Intent intent=Intent.createChooser(targetShareIntents.remove(0), "Share with Friends");
                    intent.putExtra(Intent.EXTRA_INITIAL_INTENTS, targetShareIntents.toArray(new Parcelable[]{}));
                    startActivity(intent);
                }else{
                }
            }
        }

}
