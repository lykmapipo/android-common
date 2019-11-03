package com.github.lykmapipo.common.sample.ui;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.github.lykmapipo.common.Common;
import com.github.lykmapipo.common.sample.R;


public class MainActivity extends AppCompatActivity {

    private static final String TAG = MainActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        toast("Opened");

        Common.Network.observe(this, isConnected -> {
            Log.d(TAG, String.valueOf(isConnected));
            String state = isConnected ? "Connected" : "Disconnected";
            String message = "Change Network State: " + state;
            toast(message);
        });

        View browse = findViewById(R.id.btnBrowse);
        browse.setOnClickListener(v -> {
            Common.Intents.browse("https://google.com");
        });

        View dial = findViewById(R.id.btnDial);
        dial.setOnClickListener(v -> {
            Common.Intents.dial("0714000999");
        });

        View locateAddress = findViewById(R.id.btnLocateAddr);
        locateAddress.setOnClickListener(v -> {
            Common.Intents.navigateTo("Mirambo,Dar es salaam,Tanzania");
        });

        View locatePoint = findViewById(R.id.btnLocatePoint);
        locatePoint.setOnClickListener(v -> {
            Common.Intents.navigateTo(-6.812810f, 39.273933f);
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    private void toast(String message) {
        Toast.makeText(getBaseContext(), message, Toast.LENGTH_SHORT).show();
    }

}
