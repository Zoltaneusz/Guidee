package zollie.travelblogger.guidee;

import android.graphics.Bitmap;

import java.util.ArrayList;
import java.util.Map;

/**
 * Created by FuszeneckerZ on 2016.12.24..
 */

class EventModel {
    public String summary;
    public String title;
    public ArrayList<CarouselModel> carouselModels = new ArrayList<CarouselModel>();

    public EventModel(Map<String, Object> rawEventModel) {
        ArrayList<Map<String, Object>> rawCarouselModels = null;
        this.summary = (String) rawEventModel.get("summary");
        this.title = (String) rawEventModel.get("title");
        try {
            rawCarouselModels = (ArrayList<Map<String, Object>>) rawEventModel.get("carouselModels");
            for(Map<String, Object> rawCarouselModel : rawCarouselModels ){
                CarouselModel carouselModel = new CarouselModel(rawCarouselModel);
                this.carouselModels.add(carouselModel);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public ArrayList<CarouselModel> getCarouselModels() {
        return carouselModels;
    }

    public void setCarouselModels(ArrayList<CarouselModel> carouselModels) {
        this.carouselModels = carouselModels;
    }
}