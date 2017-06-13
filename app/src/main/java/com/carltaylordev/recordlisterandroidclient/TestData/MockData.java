package com.carltaylordev.recordlisterandroidclient.TestData;

import com.carltaylordev.recordlisterandroidclient.models.EbayCategory;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import io.realm.Realm;

/**
 * Created by ct on 20/05/17.
 */

public class MockData {

    private ArrayList<ArrayList> catsList = new ArrayList<>();

    private Realm mRealm;

    public MockData(Realm realm) {
        mRealm = realm;
    }

    public void setUp() {
        mRealm.beginTransaction();
        cats();
        mRealm.commitTransaction();
    }

    private void cats() {
        if (EbayCategory.anyExist()) {
            return;
        }

        /**
         * These could be replaced with a server side call at some point, but I don't think they
         * have changed in the history of eBay!
          */

        catsList.add(catArray("Funk 7\"", "45536", EbayCategory.FORMAT_7, true));
        catsList.add(catArray("Motown 7\"", "45538", EbayCategory.FORMAT_7, true));
        catsList.add(catArray("Northern Soul 7\"", "27349", EbayCategory.FORMAT_7, true));
        catsList.add(catArray("R&B 7\"", "45537", EbayCategory.FORMAT_7, true));
        catsList.add(catArray("Jazz 7\"", "20809", EbayCategory.FORMAT_7, false));

        catsList.add(catArray("Funk 12\"", "72422", EbayCategory.FORMAT_12, false));
        catsList.add(catArray("House 12\"", "58642", EbayCategory.FORMAT_12, true));
        catsList.add(catArray("Techno 12\"", "58643", EbayCategory.FORMAT_12, true));
        catsList.add(catArray("Hardcore Rave 12\"", "58641", EbayCategory.FORMAT_12, true));
        catsList.add(catArray("Break Beat 12\"", "58635", EbayCategory.FORMAT_12, true));
        catsList.add(catArray("D&B Jungle 12\"", "58638", EbayCategory.FORMAT_12, true));
        catsList.add(catArray("Big Beat 12\"", "91489", EbayCategory.FORMAT_12, false));
        catsList.add(catArray("Trance 12\"", "58644", EbayCategory.FORMAT_12, false));
        catsList.add(catArray("Old School 12\"", "98913", EbayCategory.FORMAT_12, false));
        catsList.add(catArray("Hard House 12\"", "98912", EbayCategory.FORMAT_12, false));
        catsList.add(catArray("Garage 12\"", "58640", EbayCategory.FORMAT_12, false));
        catsList.add(catArray("Disco 12\"", "58637", EbayCategory.FORMAT_12, false));
        catsList.add(catArray("Electronica 12\"", "58639", EbayCategory.FORMAT_12, false));
        catsList.add(catArray("Down Tempo 12\"", "75552", EbayCategory.FORMAT_12, false));
        catsList.add(catArray("Progressive House 12\"", "68071", EbayCategory.FORMAT_12, false));
        catsList.add(catArray("Chillout / Ambient 12\"", "58636", EbayCategory.FORMAT_12, false));
        catsList.add(catArray("Hip Hop 12\"", "58651", EbayCategory.FORMAT_12, false));

        catsList.add(catArray("Funk LP", "43701", EbayCategory.FORMAT_LP, true));
        catsList.add(catArray("Motown LP", "43703", EbayCategory.FORMAT_LP, true));
        catsList.add(catArray("Northern Soul LP", "58626", EbayCategory.FORMAT_LP, true));
        catsList.add(catArray("R&B LP", "43702", EbayCategory.FORMAT_LP, true));
        catsList.add(catArray("Jazz LP", "43689", EbayCategory.FORMAT_LP, false));
        catsList.add(catArray("Big Beat LP", "91487", EbayCategory.FORMAT_LP, false));
        catsList.add(catArray("Break Beat LP", "25600", EbayCategory.FORMAT_LP, false));
        catsList.add(catArray("Chillout Ambient LP", "43682", EbayCategory.FORMAT_LP, false));
        catsList.add(catArray("Disco LP", "1582", EbayCategory.FORMAT_LP, false));
        catsList.add(catArray("D&B Jungle LP", "14216", EbayCategory.FORMAT_LP, false));
        catsList.add(catArray("Electronica LP", "9993", EbayCategory.FORMAT_LP, false));
        catsList.add(catArray("Garage LP", "22641", EbayCategory.FORMAT_LP, false));
        catsList.add(catArray("Hardcore Rave LP", "43685", EbayCategory.FORMAT_LP, false));
        catsList.add(catArray("Hard House LP", "98898", EbayCategory.FORMAT_LP, false));
        catsList.add(catArray("House LP", "2259", EbayCategory.FORMAT_LP, false));
        catsList.add(catArray("Down Tempo LP", "75551", EbayCategory.FORMAT_LP, false));
        catsList.add(catArray("Old School LP", "98899", EbayCategory.FORMAT_LP, false));
        catsList.add(catArray("Progressive House LP", "68063", EbayCategory.FORMAT_LP, false));
        catsList.add(catArray("Techno LP", "1597", EbayCategory.FORMAT_LP, false));
        catsList.add(catArray("Trance LP", "43688", EbayCategory.FORMAT_LP, false));
        catsList.add(catArray("Hip Hop LP", "64867", EbayCategory.FORMAT_LP, false));

        for (ArrayList list : catsList) {
            EbayCategory newCat = new EbayCategory();
            newCat.setName((String)list.get(0));
            newCat.setNumber((String)list.get(1));
            newCat.setFormat((String)list.get(2));
            String favString = (String)list.get(3);
            if (favString.equals("true")) {
                newCat.setAsFavourite(true);
            }
            mRealm.copyToRealm(newCat);
        }
    }

    private ArrayList<String>catArray(String title, String ebayCat, String format, boolean favourite) {
        ArrayList<String> list = new ArrayList<>();
        list.add(title);
        list.add(ebayCat);
        list.add(format);
        if (favourite) {
            list.add("true");
        } else {
            list.add("false");
        }
        return list;
    }
}
