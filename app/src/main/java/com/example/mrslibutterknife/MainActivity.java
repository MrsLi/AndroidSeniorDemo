package com.example.mrslibutterknife;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.example.annotation.BindView;
import com.example.annotation.OnClick;

public class MainActivity extends AppCompatActivity {
    @BindView(R.id.textView1)
    TextView textView1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        textView1.setText("起作用了");
    }

    @OnClick(R.id.bt1)
    public void OnClick(View view){
        textView1.setText("点击起作用了");
    }
}