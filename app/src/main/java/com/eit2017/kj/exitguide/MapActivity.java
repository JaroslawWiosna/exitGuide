package com.eit2017.kj.exitguide;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Intent;
import android.os.Bundle;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.IntentFilter;

import android.speech.tts.TextToSpeech;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Locale;
import java.util.Map;

import static com.eit2017.kj.exitguide.Direction.East;
import static com.eit2017.kj.exitguide.Direction.North;
import static com.eit2017.kj.exitguide.Direction.South;
import static com.eit2017.kj.exitguide.Direction.West;
import static java.lang.Thread.sleep;

public class MapActivity extends Activity implements SensorEventListener {

    Boolean roomChanged = false;

    //params to monitor current room
    int numberOfCurrentRoomInGraph = 0;
    int numberOfDestinationRoomInGraph = 1;
    Room currentRoom;
    Room destinationRoom;
    Integer roomId;
    Direction doorDirection;
    Integer exitRoomId;

    //sensors
    private SensorManager mSensorManager;
    TextToSpeech tts_test;

    // ### tvDirection - will be deleted in final version
    TextView tvDirection;
    // ### tvDirectionCardinal - will be deleted in final version
    TextView tvDirectionCardinal;
    TextView shortestPathTextView;

    Graph graph = null;
    Map<Integer,Room> fpmap;

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

    //BLUETOOTH THREAD
    Runnable runnable = new Runnable() {
        private BluetoothAdapter BTAdapter = BluetoothAdapter.getDefaultAdapter();
        public void run() {
            registerReceiver(receiver, new IntentFilter(BluetoothDevice.ACTION_FOUND));

            while(!currentRoom.bluetoothName.equals("BTEXIT")){
                discoverDevices();
                try {
                    sleep(4000);
                }
                catch (Exception e) {
                }
            }
        }

        //Bluetooth functionality
        public void cancelDiscovery() {
            if (BTAdapter.isDiscovering()) BTAdapter.cancelDiscovery();
        }

        public void discoverDevices() {
            if (BTAdapter.isDiscovering()) {
                cancelDiscovery();
                try {
                    sleep(600);
                }
                catch (Exception e) {

                }
                BTAdapter.startDiscovery();
            } else {
                BTAdapter.startDiscovery();
            }
        }

        private final BroadcastReceiver receiver = new BroadcastReceiver(){
            int threshold = -90;
            @Override
            public void onReceive(Context context, Intent intent) {
                String action = intent.getAction();
                if(BluetoothDevice.ACTION_FOUND.equals(action)) {
                    int  rssi = intent.getShortExtra(BluetoothDevice.EXTRA_RSSI,Short.MIN_VALUE);
                    String name = intent.getStringExtra(BluetoothDevice.EXTRA_NAME);
                    Toast.makeText(getApplicationContext(), name + "==>" + rssi + "dBm", Toast.LENGTH_SHORT).show();

                    if (tts_test == null) {
                        tts_test = new TextToSpeech(getApplicationContext(),
                                new TextToSpeech.OnInitListener() {
                                    @Override
                                    public void onInit(int status) {
                                        if(status != TextToSpeech.ERROR){
                                            tts_test.setLanguage(Locale.US);
                                        }
                                    }
                                });
                    }

                    if ((destinationRoom.bluetoothName.equals(name)) && (rssi < threshold)) {
                        roomChanged = true;
                        Toast.makeText(getApplicationContext(), "ROOM CHANGED!", Toast.LENGTH_SHORT).show();
                        tts_test.speak("Well done you are in the next room", TextToSpeech.QUEUE_FLUSH, null);
                    }

                    if (doorDirection == North) {
                        if (tvDirectionCardinal.getText() == "North") {
                            tts_test.speak("forward!", TextToSpeech.QUEUE_FLUSH, null);
                        }
                        if (tvDirectionCardinal.getText() == "East") {
                            tts_test.speak("turn left!", TextToSpeech.QUEUE_FLUSH, null);
                        }
                        if (tvDirectionCardinal.getText() == "West") {
                            tts_test.speak("turn right!", TextToSpeech.QUEUE_FLUSH, null);
                        }
                        if (tvDirectionCardinal.getText() == "South") {
                            tts_test.speak("turn back!", TextToSpeech.QUEUE_FLUSH, null);
                        }
                    }
                    if (doorDirection == East) {
                        if (tvDirectionCardinal.getText() == "North") {
                            tts_test.speak("turn right!", TextToSpeech.QUEUE_FLUSH, null);
                        }
                        if (tvDirectionCardinal.getText() == "East") {
                            tts_test.speak("forward!", TextToSpeech.QUEUE_FLUSH, null);
                        }
                        if (tvDirectionCardinal.getText() == "West") {
                            tts_test.speak("turn back!", TextToSpeech.QUEUE_FLUSH, null);
                        }
                        if (tvDirectionCardinal.getText() == "South") {
                            tts_test.speak("turn left!", TextToSpeech.QUEUE_FLUSH, null);
                        }
                    }
                    if (doorDirection == South) {
                        if (tvDirectionCardinal.getText() == "North") {
                            tts_test.speak("turn back!", TextToSpeech.QUEUE_FLUSH, null);
                        }
                        if (tvDirectionCardinal.getText() == "East") {
                            tts_test.speak("turn right!", TextToSpeech.QUEUE_FLUSH, null);
                        }
                        if (tvDirectionCardinal.getText() == "West") {
                            tts_test.speak("turn left!", TextToSpeech.QUEUE_FLUSH, null);
                        }
                        if (tvDirectionCardinal.getText() == "South") {
                            tts_test.speak("forward!", TextToSpeech.QUEUE_FLUSH, null);
                        }
                    }
                    if (doorDirection == West) {
                        if (tvDirectionCardinal.getText() == "North") {
                            tts_test.speak("turn left!", TextToSpeech.QUEUE_FLUSH, null);
                        }
                        if (tvDirectionCardinal.getText() == "East") {
                            tts_test.speak("turn back!", TextToSpeech.QUEUE_FLUSH, null);
                        }
                        if (tvDirectionCardinal.getText() == "West") {
                            tts_test.speak("forward!", TextToSpeech.QUEUE_FLUSH, null);
                        }
                        if (tvDirectionCardinal.getText() == "South") {
                            tts_test.speak("turn right!", TextToSpeech.QUEUE_FLUSH, null);
                        }
                    }
                }
            }
        };
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);

        //sensors init
        mSensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);

        // ### tvDirection - will be deleted in final version
        tvDirection = (TextView) findViewById(R.id.tvDirection);
        // ### tvDirectionCardinal - will be deleted in final version
        tvDirectionCardinal = (TextView) findViewById(R.id.tvDirectionCardinal);
        shortestPathTextView = (TextView) findViewById(R.id.shortestPathTextView);

        //map visualisation
        imageCenter  = (ImageView) findViewById(R.id.imageCenter);
        imageFront = (ImageView) findViewById(R.id.imageFront);
        imageBack = (ImageView) findViewById(R.id.imageBack);
        imageLeft = (ImageView) findViewById(R.id.imageLeft);
        imageRight = (ImageView) findViewById(R.id.imageRight);
        imageFR = (ImageView) findViewById(R.id.imageFR);
        imageFL = (ImageView) findViewById(R.id.imageFL);
        imageBR = (ImageView) findViewById(R.id.imageBR);
        imageBL = (ImageView) findViewById(R.id.imageBL);

        surroundings = new ImageView[8];
        surroundings[0] = imageFront;
        surroundings[1] = imageFR;
        surroundings[2] = imageRight;
        surroundings[3] = imageBR;
        surroundings[4] = imageBack;
        surroundings[5] = imageBL;
        surroundings[6] = imageLeft;
        surroundings[7] = imageFL;

        roomId = MainActivity.getStaticRoomId();
        FloorPlan fp = FloorPlan.getInstance();
        fpmap = fp.getRoomMap();

        graph = new Graph(fpmap);
        graph.DFS(roomId);
        exitRoomId = fpmap.size() - 1;

        setCurrentRoom(fpmap.get(roomId));

        if (currentRoom == null) {
            finish();
            Toast.makeText(getApplicationContext(),
                    "currentRoom is null, and this is a huge problem",
                    Toast.LENGTH_SHORT).show();
        }

        destinationRoom = fpmap.get(graph.shortestPath.get(numberOfDestinationRoomInGraph));

        shortestPathTextView.setText("Shortest path is... " + graph.shortestPath.toString());

        Thread bluetoothThread = new Thread(runnable);
        bluetoothThread.start();
    }

    // Print the nearest neighbours of current location
    private void printMap(Direction dir) {
        int[] centerParams = choosePicture(currentRoom);
        imageCenter.setImageResource(centerParams[0]);
        imageCenter.setRotation(centerParams[1] + dir.index*90);
        for (int i=0 ;i<4; i++) {
            Room neighbour = FloorPlan.getInstance().getRoomMap().get(currentRoom.neighbours[i]);
            int[] params = choosePicture(neighbour);
            surroundings[(i*2 + dir.index*2)%8].setImageResource(params[0]);
            surroundings[(i*2 + dir.index*2)%8].setRotation(params[1] + dir.index*90);

            if(neighbour != null) {
                Room nextNeighbour = FloorPlan.getInstance().getRoomMap().get(neighbour.neighbours[(i+1)%4]);
                int[] nextParams = choosePicture(nextNeighbour);
                surroundings[(i*2+1 + dir.index*2)%8].setImageResource(nextParams[0]);
                surroundings[(i*2+1 + dir.index*2)%8].setRotation(nextParams[1] + dir.index*90);
            }
            else {
                int[] nextParams = choosePicture(null);
                surroundings[(i*2+1 + dir.index*2)%8].setImageResource(nextParams[0]);
                surroundings[(i*2+1 + dir.index*2)%8].setRotation(nextParams[1] + dir.index*90);
            }

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
        try {
            sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        setCurrentRoom(fpmap.get(graph.shortestPath.get(numberOfCurrentRoomInGraph)));
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
            tvDirectionCardinal.setText("North");
        }
        else if (degree >=45 && degree <= 135) {
            printMap(Direction.West);
            tvDirectionCardinal.setText("West");
        }
        else if (degree > 135 && degree <= 225) {
            printMap(Direction.South);
            tvDirectionCardinal.setText("South");
        }
        else if (degree > 225 && degree <= 315) {
            printMap(Direction.East);
            tvDirectionCardinal.setText("East");
        }

        if (roomChanged) {
            roomChanged = false;
            numberOfCurrentRoomInGraph++;
            numberOfDestinationRoomInGraph++;

            if (graph.shortestPath.get(numberOfCurrentRoomInGraph) == exitRoomId)
            {
                tts_test.speak("exit!", TextToSpeech.QUEUE_FLUSH, null);
                onPause();
            }
            else {
                setCurrentRoom(fpmap.get(graph.shortestPath.get(numberOfCurrentRoomInGraph)));
                destinationRoom = fpmap.get(graph.shortestPath.get(numberOfDestinationRoomInGraph));
            }
        }
    }

    public void setCurrentRoom(Room nextRoom) {
        currentRoom = nextRoom;
        for(int direction = 0; direction < 4; direction++) {
            if (currentRoom.neighbours[direction] == graph.shortestPath.get(numberOfDestinationRoomInGraph)) {
                doorDirection = Direction.values()[direction];
            }
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
        //not in use
    }
}
