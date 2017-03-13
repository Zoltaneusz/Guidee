package zollie.travelblogger.guidee.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.lang.reflect.Array;
import java.util.ArrayList;

import zollie.travelblogger.guidee.R;

/**
 * Created by FuszeneckerZ on 2017.03.13..
 */

public class FollowedAdapter extends RecyclerView.Adapter<FollowedAdapter.ViewHolder> {

    private ArrayList<String> allAvatars = new ArrayList<String>();
    private ArrayList<String> allNames = new ArrayList<String>();
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

    public FollowedAdapter(Context context, ArrayList<String> avatarList, ArrayList<String> nameList){
        allAvatars = avatarList;
        allNames = nameList;
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

        TextView textView = holder.userName;
        textView.setText(allNames.get(position));
        final ImageView imageView = holder.userAvatar;
        //===================== Adding Image to to Horizontal Slide via Glide =========
        Glide
                .with(mContext)
                .load(allAvatars.get(position))
                .centerCrop()
                .override(80, 80)
                .crossFade()
                .into(imageView);
        //=============================================================================

    }

    @Override
    public int getItemCount()  {
        return allAvatars.size();
    }
}
