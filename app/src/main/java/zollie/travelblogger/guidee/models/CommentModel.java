package zollie.travelblogger.guidee.models;

import java.util.Map;

/**
 * Created by FuszeneckerZ on 2017.01.12..
 */

public class CommentModel {
    public String author;
    public String avatarURL;
    public String comment;
    public String ID;
    public String journeyID;
    public int toDelete=0;

    public CommentModel(Map<String, Object> rawCommentModel, String commentID, String journeyIdent) {
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
        try {
            this.ID = commentID;
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            this.journeyID = journeyIdent;
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public CommentModel (CommentModel commentModel){
        this.author = commentModel.author;
        this.avatarURL = commentModel.avatarURL;
        this.comment = commentModel.comment;
        this.toDelete = commentModel.toDelete;
        this.ID = commentModel.ID;
        this.journeyID = commentModel.journeyID;
    }

    public CommentModel (String name, String avatar, String com, String jID){
        this.author = name;
        this.avatarURL = avatar;
        this.comment = com;
        this.journeyID = jID;

    }
}
