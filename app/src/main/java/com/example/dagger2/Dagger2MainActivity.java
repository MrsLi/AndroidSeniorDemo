package com.example.dagger2;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.example.mrslibutterknife.R;

import javax.inject.Inject;

public class Dagger2MainActivity extends AppCompatActivity {

    @Inject
    Cat cat;

    @Inject
    Cat cat2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dagger2_main);

        DaggerMyComponents.builder().build().injectCat(this);
        Log.i("Dagger2MainActivity", "onCreate: cat:"+ cat.hashCode());
        Log.i("Dagger2MainActivity", "onCreate: cat:"+ cat2.hashCode());
    }
}