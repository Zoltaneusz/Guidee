package zollie.travelblogger.guidee.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;
import com.bumptech.glide.request.RequestOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.HashMap;

import zollie.travelblogger.guidee.R;
import zollie.travelblogger.guidee.models.UserModel;

/**
 * Created by FuszeneckerZ on 2017. 06. 13..
 */

public class LikeListAdapter extends RecyclerView.Adapter<LikeListAdapter.ViewHolder> {

    private ArrayList<UserModel> allLikers = new ArrayList<UserModel>(30);
    private Context mContext;
    FirebaseUser firUser = FirebaseAuth.getInstance().getCurrentUser(); // Should be in onResume() with almost every other method.

    public static class ViewHolder extends RecyclerView.ViewHolder{


        private final ImageView mAvatarImage;
        private final TextView mName;
        private final ToggleButton mFollowButton;
       // private final TextView mFullName;

        public ViewHolder(View itemView) {
            super(itemView);

            mAvatarImage = (ImageView) itemView.findViewById(R.id.liker_avatar);
            mName = (TextView) itemView.findViewById(R.id.likers_name);
            mFollowButton = (ToggleButton) itemView.findViewById(R.id.likers_button);
            //mFullName = (TextView) itemView.findViewById(R.id.likers_full_name);
        }
    }

    public LikeListAdapter(Context context, ArrayList<UserModel> likeList)
    {
        allLikers = likeList;
        mContext = context;
    }

    private Context GetContext(){return mContext;}
    @Override
    public LikeListAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.like_list_card, parent, false);
        LikeListAdapter.ViewHolder vh = new LikeListAdapter.ViewHolder(v);

        return vh;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        final UserModel likerUser = allLikers.get(position);
     //   TextView fullName = holder.mFullName;
        TextView name = holder.mName;
        ImageView avatarPic = holder.mAvatarImage;

        name.setText(likerUser.userName);
 //       nickname.setText((personHash.get("nickname")));
        //===================== Adding Image to to Horizontal Slide via Glide =========
        RequestOptions options = new RequestOptions();
        options.centerCrop();
        options.override(50, 50);
        Glide
                .with(mContext)
                .load(likerUser.avatarUrl)
                .apply(options)
                .transition(new DrawableTransitionOptions().crossFade())
                .into(avatarPic);
        //=============================================================================
        ToggleButton followButton = holder.mFollowButton;
        followButton.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {

                DataHandler.getInstance().followUserInFIR(likerUser, firUser);
                return false;
            }
        });
    }

    @Override
    public int getItemCount() {
        return allLikers.size();
    }


}
