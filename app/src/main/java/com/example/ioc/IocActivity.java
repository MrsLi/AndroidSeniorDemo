package com.example.ioc;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.ioc.annotations.ContentView;
import com.example.ioc.annotations.InjectOnClick;
import com.example.ioc.annotations.InjectView;
import com.example.mrslibutterknife.R;

@ContentView(R.layout.activity_ioc)
public class IocActivity extends BaseActivity {
    @InjectView(R.id.tv1)
    TextView view;

    @Override
    protected void onResume() {
        super.onResume();
        view.setText("我被ioc注入了findViewById");
    }


    @InjectOnClick(value = {R.id.tv1})
    public void OnClick(){
        Toast.makeText(this, "Proxy起作用了!!!", Toast.LENGTH_SHORT).show();
    }
}
