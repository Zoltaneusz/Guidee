package zollie.travelblogger.guidee.adapters;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;
import com.bumptech.glide.request.RequestOptions;

import java.lang.reflect.Array;
import java.util.ArrayList;

import zollie.travelblogger.guidee.R;
import zollie.travelblogger.guidee.activities.ProfileView;
import zollie.travelblogger.guidee.models.UserModel;

/**
 * Created by FuszeneckerZ on 2017.03.13..
 */

public class FollowedAdapter extends RecyclerView.Adapter<FollowedAdapter.ViewHolder> {

    private ArrayList<UserModel> allFollowers = new ArrayList<UserModel>();
    private Context mContext;

    public static class ViewHolder extends RecyclerView.ViewHolder{

        public ImageView userAvatar;
        public TextView userName;
        public FrameLayout mFollowed;

        public ViewHolder(View itemView){
            super(itemView);

            userAvatar = (ImageView) itemView.findViewById(R.id.mFollowedIMG);
            userName = (TextView) itemView.findViewById(R.id.mFollowedName);
            mFollowed = (FrameLayout) itemView.findViewById(R.id.mFollowedCard);
        }

    }

    public FollowedAdapter(Context context, ArrayList<UserModel> userModels){
        allFollowers = userModels;
        mContext = context;
    }

    @Override
    public FollowedAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.followed_card, parent, false);
        // set the view's size, margins, paddings and layout parameters

        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(FollowedAdapter.ViewHolder holder, int position) {

        final UserModel userModel = allFollowers.get(position);
        TextView textView = holder.userName;
        textView.setText(userModel.userName);
        final ImageView imageView = holder.userAvatar;
        //===================== Adding Image to to Horizontal Slide via Glide =========
        RequestOptions options = new RequestOptions();
        options.centerCrop();
        options.override(80, 80);
        Glide
                .with(mContext)
                .load(userModel.avatarUrl)
                .apply(options)
                .transition(new DrawableTransitionOptions().crossFade())
                .into(imageView);
        //=============================================================================
        //============================= Intent to journey owner ===========================
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent toProfileIntent = new Intent(mContext, ProfileView.class);
                toProfileIntent.putExtra("owner_ID", userModel.userFIRId);
                mContext.startActivity(toProfileIntent);
            }
        });
        //=============================================================================
    }

    @Override
    public int getItemCount()  {
        return allFollowers.size();
    }
}
