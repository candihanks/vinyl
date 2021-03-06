package com.carltaylordev.recordlisterandroidclient.models;

import com.carltaylordev.recordlisterandroidclient.Logger;
import com.carltaylordev.recordlisterandroidclient.Media.Base64Helpers;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.UUID;

import io.realm.RealmList;
import io.realm.RealmObject;
import io.realm.annotations.Ignore;
import io.realm.annotations.PrimaryKey;

/**
 * Created by carl on 21/05/2017.
 */

public class RealmRecord extends RealmObject {

    @Ignore
    public static final String PRIMARY_KEY = "uuid";

    @Ignore
    private boolean checked = false;

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
    private String price;
    private String numberOfRecords;
    private Boolean uploaded = false;

    private RealmList<RealmImage>images;
    private RealmList<RealmAudioClip> audioClips;

    public JSONObject toJSON() throws JSONException{
        JSONObject json = new JSONObject();
        json.put("artist", artist);
        json.put("title", title);
        json.put("label", label);
        json.put("record_condition", mediaCondition);
        json.put("cover_condition", coverCondition);
        json.put("notes", comments);
        json.put("price", price);
        json.put("listing_category", ebayCategory.getNumber());
        json.put("number_of_12s", numberOfRecords); // this translates to 'number of records' server side
        json.put("format", ebayCategory.getFormat());
        json.put("listing_title", listingTitle);

        // TODO: 09/06/2017 these are not in our UI / DB yet may keep them hardcoded
        json.put("condition_id", "3000"); // Used
        json.put("auction_type", "BIN");
        json.put("listing_duration", "Days_30");
        json.put("reserve_price", "0");
        json.put("quantity", "1");

        // Images
        JSONArray base64Images = new JSONArray();
        for (RealmImage image: getImages()) {
            try {
                base64Images.put(Base64Helpers.getFileAsBase64(image.getPath()));
            } catch (IOException e) {
                Logger.logMessage("hmmm should not be images missing");
            }
        }
        json.put("pictures", base64Images);

        try {
            // SoundClips
            JSONArray base64SoundClips = new JSONArray();
            for (RealmAudioClip audioClip : getAudioClips()) {
                try {
                    base64SoundClips.put(Base64Helpers.getFileAsBase64(audioClip.getPath()));
                } catch (IOException e) {}
            }
            json.put("sound_clips", base64SoundClips);
        } catch (NullPointerException e) {}
        return json;
    }

    public String getNumberOfRecords() {
        return numberOfRecords;
    }

    public void setNumberOfRecords(String numberOfRecords) {
        this.numberOfRecords = numberOfRecords;
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

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public RealmList<RealmImage> getImages() {
        return images;
    }

    public void setImages(RealmList<RealmImage> images) {
        this.images = images;
    }

    public RealmList<RealmAudioClip> getAudioClips() {
        return audioClips;
    }

    public void setAudioClips(RealmList<RealmAudioClip> audioClips) {
        this.audioClips = audioClips;
    }

    public Boolean hasBeenUploaded() {
        return uploaded;
    }

    public void setUploaded(Boolean uploaded) {
        this.uploaded = uploaded;
    }

    public boolean isChecked() {
        return checked;
    }

    public void setChecked(boolean checked) {
        this.checked = checked;
    }
}
