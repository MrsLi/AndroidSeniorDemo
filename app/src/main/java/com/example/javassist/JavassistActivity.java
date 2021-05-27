package com.example.javassist;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.mrslibutterknife.R;

public class JavassistActivity extends AppCompatActivity {

    private Button bt_click;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_javassist);
        bt_click = (Button) findViewById(R.id.bt_click);
        bt_click.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        logCode();
    }
    @MyInterface
    private  void logCode(){
    }
}