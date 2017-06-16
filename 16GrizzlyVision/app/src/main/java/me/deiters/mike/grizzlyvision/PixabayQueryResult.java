package me.deiters.mike.grizzlyvision;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

import com.google.gson.Gson;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.HashMap;
import java.util.concurrent.TimeUnit;

import me.deiters.mike.grizzlyvision.json.Hit;
import me.deiters.mike.grizzlyvision.json.PixabayHttpResponse;

import static me.deiters.mike.grizzlyvision.GrizzlyVisionActivity.TAG;

public class PixabayQueryResult {

    private static long DAY_MILLIS;
    private static final Gson gson;

    private int responseCode;
    private long created;
    private PixabayHttpResponse response; // taken from jsonschema2pojo without mod
    private HashMap<Long,Bitmap> bitmaps;
    private HashMap<Long,String> cloudRes;
    private String responseMethod;

    static {
        gson = new Gson();
        long DAY_MILLIS = TimeUnit.DAYS.toMillis(1);
        Log.v(TAG, "DAY_MILLIS=" + DAY_MILLIS);
    }

    public PixabayQueryResult(String json) throws IOException {

        this.response= gson.fromJson(json, PixabayHttpResponse.class);
        this.created = System.currentTimeMillis(); // birthday!
        cacheBitmaps();
        this.cloudRes = new HashMap<>();
    }

    private void cacheBitmaps() throws IOException {
        this.bitmaps = new HashMap<>();
        for (Hit hit : response.getHits()) {
            InputStream stream = new URL(hit.getWebformatURL()).openStream();
            bitmaps.put(hit.getId(), BitmapFactory.decodeStream(stream));
        }
    }

    public void cacheCloudRes(int index, String str) throws IOException {
        Hit hit = getHit(index);
        cloudRes.put(hit.getId(), str);
    }

    public boolean isExpired() {

        return created - System.currentTimeMillis() > DAY_MILLIS;
    }

    public long size() {
        return response.getHits().size();
    }

    public String getTags(int index) {
        return getHit(index).getTags();
    }

    public void setResponseCode(int responseCode) {
        this.responseCode = responseCode;
        Log.v(TAG, "responseCode = " + this.responseCode);
    }

    public void setResponseMethod(String responseMethod) {
        this.responseMethod = responseMethod;
        Log.v(TAG, "responseMethod = " + this.responseMethod);
    }

    public Hit getHit(int index) {
        return response.getHits().get(index);
    }

    public Bitmap getBitmap(int index) {
        long id = getHit(index).getId(); // covert response index to Pixabay's id
        return bitmaps.get(id);
    }

    public String getCloudRes(int index) {
        long id = getHit(index).getId();

        return cloudRes.get(id);
    }

    public boolean isCloudResCache(int index) {
        long id = getHit(index).getId();

        if (cloudRes.get(id) != null) {
            return true;
        }
        else {
            return false;
        }
    }
}
