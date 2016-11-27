package com.muki;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {
    public ImageView pimage;
    public TextView satietyStatus;
    public TextView healthStatus;
    private Core core;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        core = Core.getInstance();
        new TimeViewer(this);
        TextView name = (TextView) findViewById(R.id.name_view);
        name.setText(core.player.name);
        pimage = (ImageView) findViewById(R.id.imageSrc);
        satietyStatus = (TextView) findViewById(R.id.satiety_view);
        healthStatus = (TextView) findViewById(R.id.health_view);
        findViewById(R.id.take_food_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                core.player.putHealth();
            }
        });
        findViewById(R.id.take_health_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                core.player.takeFood();
            }
        });

        healthStatus.setText(core.player.health + " ");
        satietyStatus.setText(core.player.satiety + " ");
    }


}
