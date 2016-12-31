package zollie.travelblogger.guidee.models;

import java.util.ArrayList;
import java.util.Map;

/**
 * Created by FuszeneckerZ on 2016.12.24..
 */

public class EventModel {
    public String summary;
    public String title;
    public ArrayList<CarouselModel> carouselModels = new ArrayList<CarouselModel>();

    public EventModel(Map<String, Object> rawEventModel) {
        ArrayList<Map<String, Object>> rawCarouselModels = null;
        try {
            this.summary = (String) rawEventModel.get("summary");
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            this.title = (String) rawEventModel.get("title");
        } catch (Exception e) {
            e.printStackTrace();
        }
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