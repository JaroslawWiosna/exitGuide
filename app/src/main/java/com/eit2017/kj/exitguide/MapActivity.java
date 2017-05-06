package com.eit2017.kj.exitguide;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;

import android.widget.ImageView;
import android.widget.TextView;

public class MapActivity extends Activity implements SensorEventListener {

    private SensorManager mSensorManager;
    String RoomNumber;
    Room currentRoom;

    // ### tvDirection - will be deleted in final version
    TextView tvDirection;

    ImageView imageCenter;
    ImageView imageFront;
    ImageView imageBack;
    ImageView imageLeft;
    ImageView imageRight;
    ImageView imageFR;
    ImageView imageFL;
    ImageView imageBR;
    ImageView imageBL;

    // Array of displayed rooms around of current room
    ImageView[] surroundings;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);

        mSensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);

        // ### tvDirection - will be deleted in final version
        tvDirection = (TextView) findViewById(R.id.tvDirection);

        surroundings = new ImageView[8];

        imageCenter  = (ImageView) findViewById(R.id.imageCenter);
        imageFront = (ImageView) findViewById(R.id.imageFront);
        imageBack = (ImageView) findViewById(R.id.imageBack);
        imageLeft = (ImageView) findViewById(R.id.imageLeft);
        imageRight = (ImageView) findViewById(R.id.imageRight);
        imageFR = (ImageView) findViewById(R.id.imageFR);
        imageFL = (ImageView) findViewById(R.id.imageFL);
        imageBR = (ImageView) findViewById(R.id.imageBR);
        imageBL = (ImageView) findViewById(R.id.imageBL);

        surroundings[0] = imageFront;
        surroundings[1] = imageFR;
        surroundings[2] = imageRight;
        surroundings[3] = imageBR;
        surroundings[4] = imageBack;
        surroundings[5] = imageBL;
        surroundings[6] = imageLeft;
        surroundings[7] = imageFL;

        Intent i = getIntent();
        RoomNumber = i.getStringExtra("my_key");

        RoomsFactory.generateSampleMap();
        //loop for finding the proper room


        // ### pass id of current room
        currentRoom = FloorPlan.getInstance().getRoomMap().get(12);

    }

    // Prototyped function for printing the nearest neighbours of current location
    private void printMap(Direction dir) {
        int[] centerParams = choosePicture(currentRoom);
        imageCenter.setImageResource(centerParams[0]);
        imageCenter.setRotation(centerParams[1] + dir.index*90);
        for (int i=0 ;i<4; i++) {
            Room neighbour = FloorPlan.getInstance().getRoomMap().get(currentRoom.neighbours[i]);
            int[] params = choosePicture(neighbour);
            surroundings[(i*2 + dir.index*2)%8].setImageResource(params[0]);
            surroundings[(i*2 + dir.index*2)%8].setRotation(params[1] + dir.index*90);

            Room nextNeighbour = FloorPlan.getInstance().getRoomMap().get(neighbour.neighbours[(i+1)%4]);
            int[] nextParams = choosePicture(nextNeighbour);
            surroundings[(i*2+1 + dir.index*2)%8].setImageResource(nextParams[0]);
            surroundings[(i*2+1 + dir.index*2)%8].setRotation(nextParams[1] + dir.index*90);
        }
    }

    // Choose a proper rotated room image
    private int[] choosePicture(Room room) {
        int[] result = new int[2];
        if (room==null) {
            result[0]=R.drawable.empty;
            result[1]=0;
        }
        else {
            int numberOfDoors = room.checkNumberOfDoors();

            switch (numberOfDoors) {
                case 1:
                    result[0] = R.drawable.room1;
                    result[1] = checkSingleDoorRotation(room);
                    break;
                case 2:
                    result[0] = checkDoubleDoorPosition(room);
                    result[1] = checkDoubleDoorRotation(room, result[0]);
                    break;
                case 3:
                    result[0] = R.drawable.room3;
                    result[1] = checkTripleDoorRotation(room);
                    break;
                case 4:
                    result[0] = R.drawable.room4;
                    result[1] = 0;
                    break;
            }
        }
        return result;
    }

    private int checkSingleDoorRotation(Room room) {
        for (int i = 0; i<4;i++) {
            if (room.doors[i]==true) {
                return i*90;
            }
        }
        return 0;
    }

    // There are two variants of double doors rooms (tunnel position
    // and corner position)
    private int checkDoubleDoorPosition(Room room) {
        if (room.doors[0]==room.doors[2]) {
            return R.drawable.room22;
        }
        else {
            return R.drawable.room2;
        }
    }

    private int checkDoubleDoorRotation(Room room, int type) {
        if (type == R.drawable.room22) {
            if(room.doors[0]){
                return 0;
            }else{
                return 90;
            }
        }
        else{
            if(room.doors[0] && room.doors[1]) {
                return 0;
            }
            else if(room.doors[1] && room.doors[2]) {
                return 90;
            }
            else if(room.doors[2] && room.doors[3]) {
                return 180;
            }
            else if(room.doors[3] && room.doors[0]) {
                return 270;
            }
        }
        return 0;
    }

    private int checkTripleDoorRotation(Room room) {
        for (int i=0; i<4; i++) {
            if (room.doors[i]==false) {
                return i*90;
            }
        }
        return 0;
    }

    @Override
    protected void onResume() {
        super.onResume();

        mSensorManager.registerListener(this, mSensorManager.getDefaultSensor(Sensor.TYPE_ORIENTATION),
                SensorManager.SENSOR_DELAY_GAME);
    }

    @Override
    protected void onPause() {
        super.onPause();

        // to stop the listener and save battery
        mSensorManager.unregisterListener(this);
    }

    @Override
    public void onSensorChanged(SensorEvent event) {

        // get the angle around the z-axis rotated:
        // 0 - North, 90 - East, 180 - South, 270 - West
        float degree = Math.round(event.values[0]);

        // ### tvDirection - will be deleted in final version
        tvDirection.setText(Float.toString(degree));

        if (degree > 315 || degree < 45) {
            printMap(Direction.North);
        }
        else if (degree >=45 && degree <= 135) {
            printMap(Direction.West);
        }
        else if (degree > 135 && degree <= 225) {
            printMap(Direction.South);
        }
        else if (degree > 225 && degree <= 315) {
            printMap(Direction.East);
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
        //not in use
    }
}
