package zollie.travelblogger.guidee.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.HashMap;

import zollie.travelblogger.guidee.R;

/**
 * Created by FuszeneckerZ on 2017. 06. 13..
 */

public class LikeListAdapter extends RecyclerView.Adapter<LikeListAdapter.ViewHolder> {

    private ArrayList<HashMap<String, String>> allLikers = new ArrayList<HashMap<String, String>>(30);
    private Context mContext;

    public static class ViewHolder extends RecyclerView.ViewHolder{


        private final ImageView mAvatarImage;
        private final TextView mNickName;
        private final TextView mFullName;

        public ViewHolder(View itemView) {
            super(itemView);

            mAvatarImage = (ImageView) itemView.findViewById(R.id.liker_avatar);
            mNickName = (TextView) itemView.findViewById(R.id.likers_nickname);
            mFullName = (TextView) itemView.findViewById(R.id.likers_full_name);
        }
    }

    public LikeListAdapter(Context context, ArrayList<HashMap<String, String>> likeList)
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
        final HashMap<String, String> personHash = allLikers.get(0);
        TextView fullName = holder.mFullName;
        TextView nickname = holder.mNickName;
        ImageView avatarPic = holder.mAvatarImage;

        fullName.setText(personHash.get("fullname"));
        nickname.setText((personHash.get("nickname")));
        //===================== Adding Image to to Horizontal Slide via Glide =========
        Glide
                .with(mContext)
                .load(personHash.get("imgURL"))
                .centerCrop()
                .override(50, 50)
                .crossFade()
                .into(avatarPic);
        //=============================================================================

    }

    @Override
    public int getItemCount() {
        return allLikers.size();
    }


}
