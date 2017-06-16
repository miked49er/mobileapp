package me.deiters.mike.grizzlyvision;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.media.MediaPlayer;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Vibrator;
import android.speech.tts.TextToSpeech;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

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

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import me.deiters.mike.grizzlyvision.json.Hit;

public class GrizzlyVisionActivity extends AppCompatActivity implements SensorEventListener {

    public static final String TAG = "GrizzlyVision";
    private static PixabayQueryResult result;
    public static String PACKAGE_NAME;
    public static PackageManager PACKAGE_MANAGER;

    private TextView mImageDetails;
    private TextView pixabayTags;
    private ImageView mMainImage;
    private int selected;

    // Sensor Stuff

    private SensorManager manager;
    private boolean down = false;

    // Media

    private MediaPlayer media;
    private TextToSpeech tts;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                fetchPixabay();
            }
        });

        mImageDetails = (TextView) findViewById(R.id.image_details);
        pixabayTags = (TextView) findViewById(R.id.pixabay_details);
        mMainImage = (ImageView) findViewById(R.id.main_image);
        PACKAGE_NAME = getPackageName();
        PACKAGE_MANAGER = getPackageManager();

        manager = (SensorManager) getSystemService(SENSOR_SERVICE);

        manager.registerListener(this, manager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER), SensorManager.SENSOR_DELAY_UI);

        tts=new TextToSpeech(getApplicationContext(), new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                if(status != TextToSpeech.ERROR) {
                    tts.setLanguage(Locale.US);
                }
            }
        });

        int resID = getResources().getIdentifier("mists_of_time", "raw", getPackageName());
        media = MediaPlayer.create(this, resID);
        media.setLooping(true);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_about) {
            startActivity(new Intent(GrizzlyVisionActivity.this, About.class));
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void fetchPixabay() {
        // Switch text to loading
        mImageDetails.setText(R.string.loading_message);

        PixabayFetchTask task = new PixabayFetchTask();
        String service = "https://pixabay.com/api/";
        String key = "5555459-b4fbd09dbba0b6b6dd5a296ee";
        String query_params = "&editor_choice=true&safesearch=true&image_type=photo";
        String urlString = service + "?key=" + key + query_params;
        task.execute(urlString);
    }

    public void showTags(String str) {
//        Toast toast = Toast.makeText(getApplicationContext(), str, Toast.LENGTH_LONG);
//        toast.show();
        pixabayTags.setText(getString(R.string.pixabayTags) + " " + str);
        tts.speak(str, TextToSpeech.QUEUE_FLUSH, null);
    }

    @Override
    protected void onResume() {
        super.onResume();
        manager.registerListener(this, manager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER), SensorManager.SENSOR_DELAY_UI);
        media.start();
        Log.v(TAG, "Resume music");
    }

    @Override
    protected void onPause() {
        super.onPause();
        manager.unregisterListener(this);
        media.pause();
        Log.v(TAG, "Pause Music");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        media.stop();
        media.release();
        Log.v(TAG, "Release Music");
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        float zAxis = event.values[2];
        if (!down && zAxis < -9.75) {
            down = true;
            Log.v(TAG, "Face Down");
            tts.speak("beep", TextToSpeech.QUEUE_FLUSH, null);
            Vibrator v = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
            v.vibrate(200);
        }
        else if (down && zAxis > 9.75) {
            down = false;
            Log.v(TAG, "Face Up");
            fetchPixabay();
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
    }

    public static long getRandomLong(long min, long max) {
        return (long) (Math.random() * (max - min)) + min;
    }

    class PixabayFetchTask extends AsyncTask<String, Void, PixabayQueryResult> {

        @Override
        protected PixabayQueryResult doInBackground(String... params) {

            if (result == null || result.isExpired()) {

                Log.v(TAG, "String[0] =" + params[0]);

                try {
                    String line;
                    URL u = new URL(params[0]);
                    HttpURLConnection conn = (HttpURLConnection) u.openConnection();
                    InputStream in = new BufferedInputStream(conn.getInputStream());
                    BufferedReader reader = new BufferedReader(new InputStreamReader(in));
                    StringBuilder json = new StringBuilder();
                    while ((line = reader.readLine()) != null) json.append(line);
                    Log.v(TAG, "json =" + json);

                    result = new PixabayQueryResult(json.toString());
                    result.setResponseCode(conn.getResponseCode());

                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            return result;
        }

        @Override
        protected void onPostExecute(PixabayQueryResult result) {
            super.onPostExecute(result);
            selected = (int)getRandomLong(0, result.size());
            Bitmap bitmap = result.getBitmap(selected);
            mMainImage.setImageBitmap(bitmap);

            if (result.isCloudResCache(selected)) {
                String res = result.getCloudRes(selected);
                mImageDetails.setText(res);
            }
            else {
                CloudVision task = new CloudVision();
                Hit hit = result.getHit(selected);
                String url = hit.getWebformatURL();
                task.execute(Integer.toString(selected), url);
            }
            showTags(result.getTags(selected));
        }
    }

    class CloudVision extends AsyncTask<String, String, String> {

        private static final String CLOUD_VISION_API_KEY = "AIzaSyCPSp6zMLI1PViqtLk6RlsNSoepjTYJMrc";
        private static final String ANDROID_CERT_HEADER = "X-Android-Cert";
        private static final String ANDROID_PACKAGE_HEADER = "X-Android-Package";

        @Override
        protected String doInBackground(String... params) {

            final int index = Integer.parseInt(params[0]);
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
                String res = convertResponseToString(response);
                Log.v(TAG, res);

                result.cacheCloudRes(index, res);

                return res;

            } catch (GoogleJsonResponseException e) {
                Log.d(TAG, "failed to make API request because " + e.getContent());
            } catch (IOException e) {
                Log.d(TAG, "failed to make API request because of other IOException " +
                        e.getMessage());
            }
            Log.d(TAG, "Cloud Vision API request failed. Check logs for details.");
            return "Cloud Vision API request failed. Check logs for details.";
        }

        @Override
        protected void onProgressUpdate(String... values) {
            super.onProgressUpdate(values);
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            mImageDetails.setText(s);
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
    }
}


