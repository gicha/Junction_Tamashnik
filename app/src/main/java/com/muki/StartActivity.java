package com.muki;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

public class StartActivity extends AppCompatActivity {

    private Api mApi;

    private Core core;
    private EditText mSerialNumberEdit;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.start_activity);

        core = Core.getInstance();
        core.setContext(this);
        core.start();


        mSerialNumberEdit = (EditText) findViewById(R.id.serailNumberText);

        (findViewById(R.id.requestButton)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                init();
            }
        });
        if (ContextCompat.checkSelfPermission(this, "android.permission.ACCESS_COARSE_LOCATION") != PackageManager.PERMISSION_GRANTED)
            ActivityCompat.requestPermissions(this, new String[]{"android.permission.ACCESS_COARSE_LOCATION"}, 0);
        if (ContextCompat.checkSelfPermission(this, "android.permission.ACCESS_FINE_LOCATION") != PackageManager.PERMISSION_GRANTED)
            ActivityCompat.requestPermissions(this, new String[]{"android.permission.ACCESS_FINE_LOCATION"}, 0);
        findViewById(R.id.infoButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mApi.deviceInfo();
            }
        });
        findViewById(R.id.next1).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                core.player.setName(((EditText) findViewById(R.id.edit_name)).getText().toString());
                core.player.takeFood();
                core.player.putHealth();
                startActivity(new Intent(core.mainContext, MainActivity.class));
            }
        });

        String _cup_id = Preferences.getString("cupid");
        String _serial_number = Preferences.getString("serialnumber");
        if (_cup_id == null) {
            return;
        }
        mApi = new Api(this, getApplicationContext(), _serial_number, _cup_id);
        mApi.reset();
        mApi.deviceInfo();

        if (mApi.isOk())
            ((TextView) findViewById(R.id.cupIdText)).setText("OK");
        else
            ((TextView) findViewById(R.id.cupIdText)).setText("Failed...");

        if (!core.player.name.equals(""))
            startActivity(new Intent(getApplicationContext(), MainActivity.class));

    }

    private void init() {
        mApi = new Api(this, getApplicationContext(), mSerialNumberEdit.getText().toString());
        mApi.reset();
        mApi.request();
        if (mApi.isOk())
            ((TextView) findViewById(R.id.cupIdText)).setText("OK");
        else
            ((TextView) findViewById(R.id.cupIdText)).setText("Failed...");
    }
}
