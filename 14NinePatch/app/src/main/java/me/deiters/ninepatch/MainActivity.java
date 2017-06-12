package me.deiters.ninepatch;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

    public final String TAG = "NinePatch";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button small = (Button) findViewById(R.id.button);
        Button medium = (Button) findViewById(R.id.button2);
        Button big = (Button) findViewById(R.id.button3);
        Button longT = (Button) findViewById(R.id.button4);
        Button big_long = (Button) findViewById(R.id.button5);
        Button small_long = (Button) findViewById(R.id.button6);

        small.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.v(TAG, "Small Button");
            }
        });

        medium.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.v(TAG, "Medium Button");
            }
        });

        big.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.v(TAG, "Big Button");
            }
        });

        longT.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.v(TAG, "Long Button");
            }
        });

        big_long.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.v(TAG, "Big Long Button");
            }
        });

        small_long.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.v(TAG, "Small Long Button");
            }
        });

    }
}
