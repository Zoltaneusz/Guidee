package zollie.travelblogger.guidee.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import org.w3c.dom.Comment;

import java.util.ArrayList;

import zollie.travelblogger.guidee.R;
import zollie.travelblogger.guidee.models.CommentModel;
import zollie.travelblogger.guidee.models.EventModel;

/**
 * Created by FuszeneckerZ on 2017.01.12..
 */

public class CommentAdapter extends RecyclerView.Adapter<CommentAdapter.ViewHolder> {

    private ArrayList<CommentModel> allComments = new ArrayList<CommentModel>();
    private Context mContext;

    public static class ViewHolder extends RecyclerView.ViewHolder{

        public ImageView mUserAvatar;
        public TextView mComment;
        public LinearLayout mCommentView;

        public ViewHolder(View itemView){
            super(itemView);

            mUserAvatar = (ImageView) itemView.findViewById(R.id.comment_user_avatar);
            mComment = (TextView) itemView.findViewById(R.id.comment_content);
            mCommentView = (LinearLayout) itemView.findViewById(R.id.comment_card_placeholder);
        }
    }

    public CommentAdapter(Context context, ArrayList<CommentModel> allCommentList) {
        allComments = allCommentList;
        mContext = context;

    }

    private Context getContext() {
        return mContext;
    }

    @Override
    public CommentAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.comment_card, parent, false);
        // set the view's size, margins, paddings and layout parameters

        CommentAdapter.ViewHolder vh = new CommentAdapter.ViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(CommentAdapter.ViewHolder holder, int position) {
        final CommentModel mComment = allComments.get(position);

        TextView commentText = holder.mComment;
        commentText.setText(mComment.comment);
        ImageView commentUserAvatar = holder.mUserAvatar;
        //===================== Adding Image to to Horizontal Slide via Glide =========
        Glide
                .with(mContext)
                .load(mComment.avatarURL)
                .centerCrop()
                .override(160, 160)
                .crossFade()
                .into(commentUserAvatar);
        //=============================================================================
    }

    @Override
    public int getItemCount() {
        return allComments.size();
    }
}
