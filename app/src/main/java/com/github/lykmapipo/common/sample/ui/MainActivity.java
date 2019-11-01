package com.github.lykmapipo.common.sample.ui;

import android.os.Bundle;
import android.util.Log;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.github.lykmapipo.common.Common;
import com.github.lykmapipo.common.sample.R;


public class MainActivity extends AppCompatActivity {

    private static final String TAG = MainActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // simulate logs
        Button btnLogAction = findViewById(R.id.btnLogAction);
        btnLogAction.setOnClickListener(v -> {
            Log.d(TAG, Common.getApplicationContext().toString());
            Log.d(TAG, Common.isDebug().toString());
            Log.d(TAG, Common.Network.isConnected().toString());
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

}
