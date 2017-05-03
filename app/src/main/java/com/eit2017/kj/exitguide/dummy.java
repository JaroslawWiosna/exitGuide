package com.eit2017.kj.exitguide;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

public class dummy extends AppCompatActivity {

    private TextView textRoomNumber;
    String RoomNumber;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dummy);

        textRoomNumber = (TextView) findViewById(R.id.textRoomNumber);

        Intent i = getIntent();
        RoomNumber = i.getStringExtra("my_key");
        textRoomNumber.setText(RoomNumber);
    }
}
