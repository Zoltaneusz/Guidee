package zollie.travelblogger.guidee;

/**
 * Created by FuszeneckerZ on 2016.12.03..
 */

public class DataHandler {
    private static DataHandler myInstance = null;

    private String myProfPic;

    private DataHandler(){
        myProfPic = "Default";
    }

    public static DataHandler getInstance(){

        if(myInstance == null){
            myInstance = new DataHandler();
        }
        return myInstance;
    }

    public String getProfPic(){
        return this.myProfPic;
    }

    public void setProfPic(String value){
        myProfPic = value;
    }

}
