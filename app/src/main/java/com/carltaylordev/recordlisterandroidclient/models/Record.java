package com.carltaylordev.recordlisterandroidclient.models;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.UUID;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by carl on 21/05/2017.
 */

public class Record extends RealmObject {
    @PrimaryKey
    private String uuid = UUID.randomUUID().toString();
    private String artist;
    private String title;
    private String label;
    private String comments;
    private String format;
    private String mediaCondition;
    private String coverCondition;
    private String listingTitle;
    private EbayCategory ebayCategory;
    private Double price;

    // todo: sound clip relationship
    // todo: picture relationship

    public String toJson() {
        Gson gson = new GsonBuilder().create();
        return gson.toJson(this);
    }

    public String getUuid() {
        return uuid;
    }

    public String getArtist() {
        return artist;
    }

    public void setArtist(String artist) {
        this.artist = artist;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public String getComments() {
        return comments;
    }

    public void setComments(String comments) {
        this.comments = comments;
    }

    public String getFormat() {
        return format;
    }

    public void setFormat(String format) {
        this.format = format;
    }

    public String getMediaCondition() {
        return mediaCondition;
    }

    public void setMediaCondition(String mediaCondition) {
        this.mediaCondition = mediaCondition;
    }

    public String getCoverCondition() {
        return coverCondition;
    }

    public void setCoverCondition(String coverCondition) {
        this.coverCondition = coverCondition;
    }

    public String getListingTitle() {
        return listingTitle;
    }

    public void setListingTitle(String listingTitle) {
        this.listingTitle = listingTitle;
    }

    public EbayCategory getEbayCategory() {
        return ebayCategory;
    }

    public void setEbayCategory(EbayCategory ebayCategory) {
        this.ebayCategory = ebayCategory;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }
}
