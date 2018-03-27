package me.temoa.autosizetextview;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final AutoSizeTextView autoSizeTextView = findViewById(R.id.auto_size_tv);
        final TextView normalTextView = findViewById(R.id.normal_tv);
        final EditText et = findViewById(R.id.et);
        et.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                autoSizeTextView.setNewText(s.toString());
                normalTextView.setText(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        et.setText(R.string.test_string);
    }
}
