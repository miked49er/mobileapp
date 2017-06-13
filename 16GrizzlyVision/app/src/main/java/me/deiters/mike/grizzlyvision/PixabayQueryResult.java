package me.deiters.mike.grizzlyvision;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;

import com.google.api.client.extensions.android.http.AndroidHttp;
import com.google.api.client.googleapis.json.GoogleJsonResponseException;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.gson.GsonFactory;
import com.google.api.services.vision.v1.Vision;
import com.google.api.services.vision.v1.VisionRequest;
import com.google.api.services.vision.v1.VisionRequestInitializer;
import com.google.api.services.vision.v1.model.AnnotateImageRequest;
import com.google.api.services.vision.v1.model.BatchAnnotateImagesRequest;
import com.google.api.services.vision.v1.model.BatchAnnotateImagesResponse;
import com.google.api.services.vision.v1.model.EntityAnnotation;
import com.google.api.services.vision.v1.model.Feature;
import com.google.api.services.vision.v1.model.Image;
import com.google.gson.Gson;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

import me.deiters.mike.grizzlyvision.json.Hit;
import me.deiters.mike.grizzlyvision.json.PixabayHttpResponse;

import static me.deiters.mike.grizzlyvision.GrizzlyVisionActivity.PACKAGE_MANAGER;
import static me.deiters.mike.grizzlyvision.GrizzlyVisionActivity.PACKAGE_NAME;
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

    private void cacheCloudRes(long index) throws IOException {
        CloudVision task = new CloudVision();
        String url = hit.getWebformatURL();
        task.execute(Long.toString(hit.getId()), url);
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

    class CloudVision extends AsyncTask<String, Void, HashMap<Long, String>> {

        private static final String CLOUD_VISION_API_KEY = "AIzaSyCPSp6zMLI1PViqtLk6RlsNSoepjTYJMrc";
        private static final String ANDROID_CERT_HEADER = "X-Android-Cert";
        private static final String ANDROID_PACKAGE_HEADER = "X-Android-Package";
        private String result;

        @Override
        protected HashMap<Long, String> doInBackground(String... params) {

            final long index = Long.parseLong(params[0]);
            final String loc = params[1];

            Log.v(TAG, loc);

            try {
                HttpTransport httpTransport = AndroidHttp.newCompatibleTransport();
                JsonFactory jsonFactory = GsonFactory.getDefaultInstance();

                VisionRequestInitializer requestInitializer =
                        new VisionRequestInitializer(CLOUD_VISION_API_KEY) {
                            /**
                             * We override this so we can inject important identifying fields into the HTTP
                             * headers. This enables use of a restricted cloud platform API key.
                             */
                            @Override
                            protected void initializeVisionRequest(VisionRequest<?> visionRequest)
                                    throws IOException {
                                super.initializeVisionRequest(visionRequest);

                                String packageName = PACKAGE_NAME;
                                visionRequest.getRequestHeaders().set(ANDROID_PACKAGE_HEADER, packageName);

                                String sig = PackageManagerUtils.getSignature(PACKAGE_MANAGER, packageName);

                                visionRequest.getRequestHeaders().set(ANDROID_CERT_HEADER, sig);
                            }
                        };

                Vision.Builder builder = new Vision.Builder(httpTransport, jsonFactory, null);
                builder.setVisionRequestInitializer(requestInitializer);

                Vision vision = builder.build();

                BatchAnnotateImagesRequest batchAnnotateImagesRequest =
                        new BatchAnnotateImagesRequest();
                batchAnnotateImagesRequest.setRequests(new ArrayList<AnnotateImageRequest>() {{
                    AnnotateImageRequest annotateImageRequest = new AnnotateImageRequest();

                    Bitmap bitmap = null;
                    try {
                        InputStream stream = new URL(loc).openStream();
                        bitmap = BitmapFactory.decodeStream(stream);
                        publishProgress();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                    // Add the image
                    Image base64EncodedImage = new Image();
                    // Convert the bitmap to a JPEG
                    // Just in case it's a format that Android understands but Cloud Vision
                    ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                    bitmap.compress(Bitmap.CompressFormat.JPEG, 90, byteArrayOutputStream);
                    byte[] imageBytes = byteArrayOutputStream.toByteArray();

                    // Base64 encode the JPEG
                    base64EncodedImage.encodeContent(imageBytes);
                    annotateImageRequest.setImage(base64EncodedImage);

                    // add the features we want
                    annotateImageRequest.setFeatures(new ArrayList<Feature>() {{
                        Feature labelDetection = new Feature();
                        labelDetection.setType("LABEL_DETECTION");
                        labelDetection.setMaxResults(10);
                        add(labelDetection);
                    }});

                    // Add the list of one thing to the request
                    add(annotateImageRequest);
                }});

                Vision.Images.Annotate annotateRequest =
                        vision.images().annotate(batchAnnotateImagesRequest);
                // Due to a bug: requests to Vision API containing large images fail when GZipped.
                annotateRequest.setDisableGZipContent(true);
                Log.d(TAG, "created Cloud Vision request object, sending request");

                BatchAnnotateImagesResponse response = annotateRequest.execute();
                result = convertResponseToString(response);
                Log.v(TAG, result);

                cloudRes.put(index, result);

                return cloudRes;

            } catch (GoogleJsonResponseException e) {
                Log.d(TAG, "failed to make API request because " + e.getContent());
            } catch (IOException e) {
                Log.d(TAG, "failed to make API request because of other IOException " +
                        e.getMessage());
            }
            Log.d(TAG, "Cloud Vision API request failed. Check logs for details.");
            return cloudRes;
        }

        @Override
        protected void onProgressUpdate(Void... values) {
            super.onProgressUpdate(values);
        }

        @Override
        protected void onPostExecute(HashMap<Long, String> longStringHashMap) {
            super.onPostExecute(longStringHashMap);
        }

        private String convertResponseToString(BatchAnnotateImagesResponse response) {
            String message = "I found these things:\n\n";

            List<EntityAnnotation> labels = response.getResponses().get(0).getLabelAnnotations();
            if (labels != null) {
                for (EntityAnnotation label : labels) {
                    message += String.format(Locale.US, "%.3f: %s", label.getScore(), label.getDescription());
                    message += "\n";
                }
            } else {
                message += "nothing";
            }

            return message;
        }

        public String getResult() {
            return result;
        }
    }

}
