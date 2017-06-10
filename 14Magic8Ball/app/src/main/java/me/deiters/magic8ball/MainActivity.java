package me.deiters.magic8ball;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
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
import android.widget.Toast;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Locale;

public class MainActivity extends AppCompatActivity implements SensorEventListener {

    public final String TAG = "Magic8";

    // Sensor Stuff

    private SensorManager manager;
    private boolean down = false;

    // Pixabay Stuff

    private static PixabayQueryResult result;
    private ImageView imageView;
    private TextToSpeech tts;

    // Media

    private MediaPlayer media;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        manager = (SensorManager) getSystemService(SENSOR_SERVICE);
        imageView = (ImageView) findViewById(R.id.imageView);

        manager.registerListener(this, manager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER), SensorManager.SENSOR_DELAY_UI);

        tts=new TextToSpeech(getApplicationContext(), new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                if(status != TextToSpeech.ERROR) {
                    tts.setLanguage(Locale.US);
                }
            }
        });

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                fetchPixabay();
            }
        });

        int resID = getResources().getIdentifier("mists_of_time", "raw", getPackageName());
        media = MediaPlayer.create(this, resID);
        media.setLooping(true);

    }

    public void showTags(String str) {
        Toast toast = Toast.makeText(getApplicationContext(), str, Toast.LENGTH_LONG);
        toast.show();
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

    private void fetchPixabay() {
        PixabayFetchTask task = new PixabayFetchTask();
        String service = "https://pixabay.com/api/";
        String key = "5555459-b4fbd09dbba0b6b6dd5a296ee";
        String query_params = "&editor_choice=true&safesearch=true&image_type=photo";
        String urlString = service + "?key=" + key + query_params;
        task.execute(urlString);
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
