package me.deiters.palindrome;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import static android.R.attr.editable;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "PalindromeApp";
    public static final String isPalindrome = "Is a Palindrome";
    public static final String notPalindrome = "Is NOT a Palindrome";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final EditText input = (EditText) findViewById(R.id.etInput);
        final ImageView image = (ImageView) findViewById(R.id.ivResult);
        final TextView result = (TextView) findViewById(R.id.tvResult);

        input.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                String current = s.toString();
                boolean is = Palindrome.check(current);
                result.setText(is ? isPalindrome : notPalindrome);
                image.setImageResource(is ? R.drawable.check : R.drawable.x);
            }
        });
    }
}
