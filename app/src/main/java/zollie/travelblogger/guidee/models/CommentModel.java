package zollie.travelblogger.guidee.models;

import java.util.Map;

/**
 * Created by FuszeneckerZ on 2017.01.12..
 */

public class CommentModel {
    public String author;
    public String avatarURL;
    public String comment;

    public CommentModel(Map<String, Object> rawCommentModel) {
        try {
            this.author = (String) rawCommentModel.get("author");
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            this.avatarURL = (String) rawCommentModel.get("avatarURL");
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            this.comment = (String) rawCommentModel.get("comment");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
