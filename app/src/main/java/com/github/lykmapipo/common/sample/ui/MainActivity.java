package com.github.lykmapipo.common.sample.ui;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.github.lykmapipo.common.Common;
import com.github.lykmapipo.common.sample.R;

import static android.Manifest.permission.ACCESS_FINE_LOCATION;


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

        View netSettings = findViewById(R.id.btnNetSettings);
        netSettings.setOnClickListener(v -> {
            Common.Intents.openWirelessSettings();
        });

        View btnAppSettings = findViewById(R.id.btnAppSettings);
        btnAppSettings.setOnClickListener(v -> {
            Common.Intents.openApplicationSettings();
        });

        View btnPrompt = findViewById(R.id.btnPrompt);
        btnPrompt.setOnClickListener(v -> {
            Common.Prompt.show(this, R.string.prompt_title, R.string.prompt_message, accepted -> {
                if (accepted) {
                    toast("Accepted.");
                } else {
                    toast("Cancelled");
                }
            });
        });

        View btnPermissions = findViewById(R.id.btnPermissions);
        btnPermissions.setOnClickListener(v -> {
            Common.Permissions.request(this, granted -> {
                if (granted) {
                    toast("Granted.");
                } else {
                    toast("Denied");
                }
            }, ACCESS_FINE_LOCATION);
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
