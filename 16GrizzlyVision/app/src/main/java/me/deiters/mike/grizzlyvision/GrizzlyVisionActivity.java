package me.deiters.mike.grizzlyvision;

import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class GrizzlyVisionActivity extends AppCompatActivity {

    public static final String TAG = "GrizzlyVision";
    private static PixabayQueryResult result;
    public static String PACKAGE_NAME;
    public static PackageManager PACKAGE_MANAGER;

    private TextView mImageDetails;
    private ImageView mMainImage;

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
        mMainImage = (ImageView) findViewById(R.id.main_image);
        PACKAGE_NAME = getPackageName();
        PACKAGE_MANAGER = getPackageManager();
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
        Toast toast = Toast.makeText(getApplicationContext(), str, Toast.LENGTH_LONG);
        toast.show();
//        tts.speak(str, TextToSpeech.QUEUE_FLUSH, null);
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
            int selected = (int)getRandomLong(0, result.size());
            Bitmap bitmap = result.getBitmap(selected);
            mMainImage.setImageBitmap(bitmap);
            mImageDetails.setText(result.getCloudRes(selected));
            showTags(result.getTags(selected));
        }
    }
}


