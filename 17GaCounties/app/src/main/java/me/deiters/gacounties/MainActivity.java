package me.deiters.gacounties;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button about = (Button) findViewById(R.id.about);
        Button gwinnett = (Button) findViewById(R.id.gwinnett);
        Button fulton = (Button) findViewById(R.id.fulton);
        Button cobb = (Button) findViewById(R.id.cobb);
        Button clarke = (Button) findViewById(R.id.clarke);

        gwinnett.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showCounty(getString(R.string.gwinnettC));
                goToUrl("https://www.google.com/maps/place/Gwinnett+County,+GA/@33.9574595,-84.318151,10z/data=!3m1!4b1!4m5!3m4!1s0x88f59d45cf8d985d:0x5aae88810e065f7d!8m2!3d33.9190653!4d-84.0167423");
            }
        });

        fulton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showCounty(getString(R.string.fultonC));
                goToUrl("https://www.google.com/maps/place/Fulton+County,+GA/data=!4m2!3m1!1s0x88f5ab709867d3d1:0xd7d2418b0ed7c513?sa=X&ved=0ahUKEwj7vMf5wpDTAhUHiVQKHQ4fAqIQ8gEIpwEwGg");
            }
        });

        cobb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showCounty(getString(R.string.cobbC));
                goToUrl("https://www.google.com/maps/place/Cobb+County,+GA/data=!4m2!3m1!1s0x88f51a7f2ffdc425:0x48c404325b99dffa?sa=X&sqi=2&ved=0ahUKEwj7r-mKw5DTAhVF7SYKHTMADhoQ8gEIfzAN");
            }
        });

        clarke.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showCounty(getString(R.string.clarkeC));
                goToUrl("https://www.google.com/maps/place/Clarke+County,+GA/data=!4m2!3m1!1s0x88f67369c3e5a9a5:0x281bbc3db2a4ca0a?sa=X&ved=0ahUKEwjhqc6kw5DTAhXIz1QKHZuGChMQ8gEIggEwDQ");
            }
        });

        about.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, About.class));
            }
        });
    }

    private void goToUrl (String url) {
        Uri uriUrl = Uri.parse(url);
        Intent launchBrowser = new Intent(Intent.ACTION_VIEW, uriUrl);
        startActivity(launchBrowser);
    }

    public void showCounty(String str) {
        Toast toast = Toast.makeText(getApplicationContext(), str, Toast.LENGTH_SHORT);
        toast.show();
    }
}
