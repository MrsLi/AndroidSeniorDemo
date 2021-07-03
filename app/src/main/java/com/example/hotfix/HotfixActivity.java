package com.example.hotfix;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;

import com.example.mrslibutterknife.R;

public class HotfixActivity extends AppCompatActivity {

    String textValue="";
    private TextView tv1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hotfix);
        tv1 = (TextView) findViewById(R.id.tv1);
        tv1.setText(textValue);
    }
}