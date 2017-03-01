package zollie.travelblogger.guidee.adapters;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.os.AsyncTask;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.Target;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import org.w3c.dom.Comment;

import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

import zollie.travelblogger.guidee.R;
import zollie.travelblogger.guidee.models.CommentModel;
import zollie.travelblogger.guidee.utils.ImageProcessor;

/**
 * Created by FuszeneckerZ on 2017.01.12..
 */

public class CommentAdapter extends RecyclerView.Adapter<CommentAdapter.ViewHolder> {

    private ArrayList<CommentModel> allComments = new ArrayList<CommentModel>();
    private Context mContext;
    Bitmap userAvatarGlobal = null;
    ImageProcessor imageProcessor = new ImageProcessor();
    FirebaseUser firUser = FirebaseAuth.getInstance().getCurrentUser();
    String firUserID = firUser.getUid();

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
    public void onBindViewHolder(final CommentAdapter.ViewHolder holder, int position) {
        final CommentModel mComment = allComments.get(position);

        final TextView commentText = holder.mComment;
        commentText.setText(mComment.comment);
        //===================== Adding Image to to Horizontal Slide via Glide =========
        new AsyncUserPic().execute(mComment, holder);
        //=============================================================================

        commentText.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {


                if(mComment.author.equals(firUserID)){
                    if(mComment.toDelete == 1) { // Not needed??
                        mComment.toDelete = 2;
                        commentText.setBackgroundColor(ContextCompat.getColor(mContext, R.color.PressedTint));
                        //!!!!!!!!!! Implement a floating window here, where the user can click to EDIT, DELETE or LEAVE the selected comment. Also invoke DataHandler.getInstance().deleteCommentInFIR !!!!!!!!
                    }
                    else if(mComment.toDelete == 2){
                        mComment.toDelete = 1;
                        commentText.setBackgroundColor(ContextCompat.getColor(mContext, R.color.LightGreen));

                    }

                }

                return true;
            }
        });

    }

    @Override
    public int getItemCount() {
        return allComments.size();
    }

    class AsyncUserPic extends AsyncTask<Object, Void, CommentAdapter.ViewHolder> {


        @Override
        protected CommentAdapter.ViewHolder doInBackground(Object... params) {
            CommentModel mComment = new CommentModel((CommentModel)(params[0]));
            CommentAdapter.ViewHolder holder = (CommentAdapter.ViewHolder) params[1];
            //===================== Adding Image to to Horizontal Slide via Glide =========
            try {
                userAvatarGlobal= Glide.with(mContext)
                        .load(mComment.avatarURL)
                        .asBitmap()
                        .into(Target.SIZE_ORIGINAL,Target.SIZE_ORIGINAL).get();
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (ExecutionException e) {
                e.printStackTrace();
            }
            //=============================================================================
            userAvatarGlobal = imageProcessor.resizeMarkerImage(userAvatarGlobal, 2);
            return holder;
        }

        @Override
        protected void onPostExecute(CommentAdapter.ViewHolder holder) {
            ImageView imageView = holder.mUserAvatar;
            final float scale = mContext.getResources().getDisplayMetrics().density;
            final Bitmap.Config conf = Bitmap.Config.ARGB_8888;
            Bitmap bmp = Bitmap.createBitmap((int)(120*scale),(int) (120*scale), conf);
            Canvas canvas1 = new Canvas(bmp);
            Bitmap circleBitmap = imageProcessor.pulseMarker(4, bmp, canvas1, scale*2, userAvatarGlobal);
            circleBitmap = imageProcessor.pulseMarker(4, userAvatarGlobal, canvas1, scale*2, circleBitmap);
            imageView.setImageBitmap(circleBitmap);
        }
    }

}
