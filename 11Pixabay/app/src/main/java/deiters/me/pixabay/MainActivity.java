package deiters.me.pixabay;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    public static final String TAG = "Pixabay";
    private static PixabayQueryResult result;
    private ImageView imageView;
    private ConstraintLayout constrainLayout;
    private TextToSpeech tts;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        imageView = (ImageView) findViewById(R.id.imageView);
        constrainLayout = (ConstraintLayout) findViewById(R.id.contraintLayout);

       FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PixabayFetchTask task = new PixabayFetchTask();
                String service = "https://pixabay.com/api/";
                String key = "5555459-b4fbd09dbba0b6b6dd5a296ee";
                String query_params = "&editor_choice=true&safesearch=true&image_type=photo";
                String urlString = service + "?key=" + key + query_params;
                task.execute(urlString);
            }
        });

        tts=new TextToSpeech(getApplicationContext(), new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                if(status != TextToSpeech.ERROR) {
                    tts.setLanguage(Locale.US);
                }
            }
        });
    }

    public void showTags(String str) {
        Snackbar snackbar = Snackbar.make(constrainLayout, str, Snackbar.LENGTH_LONG);
        snackbar.show();
        tts.speak(str, TextToSpeech.QUEUE_FLUSH, null);
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
            startActivity(new Intent(MainActivity.this, About.class));
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public static long getRandomLong(long min, long max) {
        return (long) (Math.random() * (max - min)) + min;
    }

    class PixabayFetchTask extends AsyncTask<String, Void, PixabayQueryResult> {

        @Override
        protected PixabayQueryResult doInBackground(String... params) {

            if (result == null || result.isExpired()) {

                Log.v("Pixabay", "String[0] =" + params[0]);

                try {
                    String line;
                    URL u = new URL(params[0]);
                    HttpURLConnection conn = (HttpURLConnection) u.openConnection();
                    InputStream in = new BufferedInputStream(conn.getInputStream());
                    BufferedReader reader = new BufferedReader(new InputStreamReader(in));
                    StringBuilder json = new StringBuilder();
                    while ((line = reader.readLine()) != null) json.append(line);
                    Log.v("Pixabay", "json =" + json);

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
            imageView.setImageBitmap(bitmap);
            showTags(result.getTags(selected));
        }
    }
}
