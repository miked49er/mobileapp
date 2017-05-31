package me.deiters.transparencyandcolors;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "TransparencyColor";
    private TextView swatch;
    private SeekBar seek;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        seek = (SeekBar) findViewById(R.id.sbTransparency);

        Spinner colorPicker = (Spinner) findViewById(R.id.spColor);
        swatch = (TextView) findViewById(R.id.tvSwatch);

        seek.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                Log.i(TAG, "seek=" + seek.getProgress());
                int currColor = ((ColorDrawable) swatch.getBackground()).getColor();
                int nextColor = Color.argb(seek.getProgress(), Color.red(currColor), Color.green(currColor), Color.blue(currColor));
                swatch.setBackgroundColor(nextColor);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        colorPicker.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Log.i(TAG, "item index i=" + position);
                Log.i(TAG, "item selected=" + parent.getItemAtPosition(position));

                switch (parent.getItemAtPosition(position).toString()) {
                    case "Red":
                        swatch.setBackgroundColor(Color.RED);
                        break;
                    case "Green":
                        swatch.setBackgroundColor(Color.GREEN);
                        break;
                    case "Blue":
                        swatch.setBackgroundColor(Color.BLUE);
                        break;
                }
                swatch.getBackground().setAlpha(seek.getProgress());
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }
}
