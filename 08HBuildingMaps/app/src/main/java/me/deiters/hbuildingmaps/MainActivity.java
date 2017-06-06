package me.deiters.hbuildingmaps;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button campus = (Button) findViewById(R.id.campus);
        Button firstFloor = (Button) findViewById(R.id.firstFloor);
        Button secondFloor = (Button) findViewById(R.id.secondFloor);
        Button thirdFloor = (Button) findViewById(R.id.thirdFloor);
        Button about = (Button) findViewById(R.id.about);

        campus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, CampusMap.class));
            }
        });

        firstFloor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, FirstFloor.class));
            }
        });

        secondFloor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, SecondFloor.class));
            }
        });

        thirdFloor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, ThirdFloor.class));
            }
        });

        about.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, About.class));
            }
        });
    }
}
