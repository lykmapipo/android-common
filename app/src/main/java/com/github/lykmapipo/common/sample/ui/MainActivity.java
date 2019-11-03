package com.github.lykmapipo.common.sample.ui;

import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.github.lykmapipo.common.Common;
import com.github.lykmapipo.common.sample.R;

import java.net.SocketException;


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

        // simulate logs
        Button btnLogAction = findViewById(R.id.btnLogAction);
        btnLogAction.setOnClickListener(v -> {
            toast("clicked");
            Log.d(TAG, Common.getApplicationContext().toString());
            Log.d(TAG, Common.isDebug().toString());
            Log.d(TAG, Common.Network.isConnected().toString());
            Log.d(TAG, Common.Network.isNetworkException(new SocketException()).toString());
            Log.d(TAG, Common.Strings.join("a", "b"));
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
