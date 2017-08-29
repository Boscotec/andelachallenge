package com.boscotec.andelachallenge;

import android.content.ComponentName;
import android.content.Intent;
import android.content.pm.ResolveInfo;
import android.net.Uri;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.boscotec.andelachallenge.utility.CircleImageView;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Johnbosco on 26-Jul-17.
 */

public class ProfileActivity extends AppCompatActivity
        implements View.OnClickListener {

    TextView tv_profile_url, tv_username;
    FloatingActionButton fab_share;
    String username, profile_pix, profile_url;
    Toolbar toolbar;
    ImageView back;

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        initToolbar();

        if(getIntent()!=null){
           Bundle bundle = getIntent().getExtras();
           username = bundle.getString("username");
           profile_pix = bundle.getString("profile_pix");
           profile_url = bundle.getString("html_url");
        }

        CircleImageView civ_profile_pix = (CircleImageView) findViewById(R.id.profile_pix);
        tv_username = (TextView) findViewById(R.id.username);
        tv_profile_url =(TextView) findViewById(R.id.profile_url);
        fab_share = (FloatingActionButton) findViewById(R.id.share_button);

        tv_profile_url.setOnClickListener(this);
        fab_share.setOnClickListener(this);

        tv_username.setText(username);
        tv_profile_url.setText(profile_url);
        Glide.with(this).load(profile_pix)
                .crossFade()
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .error(R.drawable.ic_person_white_36dp)
                .into(civ_profile_pix);
    }

    private void initToolbar(){
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        TextView title = (TextView) findViewById(R.id.toolbarTitle);
        back = (ImageView) findViewById(R.id.back_arrow);
        back.setOnClickListener(this);
        title.setText(getResources().getString(R.string.mainactivity_toolbar_title));
        setSupportActionBar(toolbar);
    }

    @Override
    public void onClick(View view){
        if(view==back){
            ProfileActivity.super.onBackPressed();
        }
        if(view == fab_share){
            String message = "Check out this awesome developer @" + username +", " + profile_url;
            share(message);
        }

        if(view == tv_profile_url){
            Intent web = new Intent(Intent.ACTION_VIEW);
            web.setData(Uri.parse(profile_url));
            web.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY|Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET);
            web.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_MULTIPLE_TASK);
            startActivity(web);
        }
    }


    @Override
    protected void onResume() {
        super.onResume();
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
