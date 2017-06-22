package com.eit2017.kj.exitguide;

import android.app.Activity;

import android.content.ActivityNotFoundException;
import android.content.Intent;

import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.speech.tts.TextToSpeech;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import java.util.Map;

import java.util.ArrayList;
import java.util.Locale;


public class MainActivity extends Activity {

    private TextView textSpoken;
    private Button buttonSpeak;
    private Button buttonNextStep;

    Map<Integer,Room> roomMap;
    String roomNumber;
    public static Integer staticRoomId = -1;

    private final int REQ_CODE_SPEECH_INPUT = 100;
    TextToSpeech tts_test;

    public static Integer getStaticRoomId() {
        return staticRoomId;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //create proper map
        RoomsFactory.generateSampleMap();

        //SpeechRecognition
        buttonSpeak = (Button) findViewById(R.id.buttonSpeak);
        textSpoken = (TextView) findViewById(R.id.textSpoken);

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

        //next step <-- button
        buttonNextStep = (Button) findViewById(R.id.buttonNextStep);

        buttonSpeak.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                speechInput();
            }
        });

        buttonNextStep.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (staticRoomId != -1) {
                    Intent i = new Intent(MainActivity.this, MapActivity.class);
                    startActivity(i);
                }
                else {
                    tts_test.speak("no such room", TextToSpeech.QUEUE_FLUSH, null);
                    Toast.makeText(getApplicationContext(),
                            getString(R.string.no_such_room),
                            Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void speechInput() {
        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,
                RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault());
//        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, "pl-PL");
        intent.putExtra("android.speech.extra.EXTRA_ADDITIONAL_LANGUAGES", new String[]{"pl"});
        intent.putExtra(RecognizerIntent.EXTRA_PROMPT,
                getString(R.string.speech_prompt));
        try {
            startActivityForResult(intent, REQ_CODE_SPEECH_INPUT);
        } catch (ActivityNotFoundException a) {
            Toast.makeText(getApplicationContext(),
                    getString(R.string.speech_not_supported),
                    Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * Receiving speech input
     * */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode) {
            case REQ_CODE_SPEECH_INPUT: {
                if (resultCode == RESULT_OK && null != data) {

                    ArrayList<String> result = data
                            .getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
                    textSpoken.setText(result.get(0));
                    roomNumber = result.get(0).toString();
                    roomNumber = roomNumber.replace(" ", "");
                }
                break;
            }

        }

        //function for finding the proper room
        roomMap = FloorPlan.getInstance().getRoomMap();
        for (Map.Entry<Integer, Room> entry : roomMap.entrySet()) {
            if (entry.getValue().number.equalsIgnoreCase(roomNumber)) {
                staticRoomId = entry.getValue().id;
            }
        }

        if (staticRoomId == -1) {
            tts_test.speak("no such room", TextToSpeech.QUEUE_FLUSH, null);
            Toast.makeText(getApplicationContext(),
                    getString(R.string.no_such_room),
                    Toast.LENGTH_SHORT).show();
        }

        textSpoken.setText(staticRoomId.toString());
    }

    @Override
    protected void onResume() {
        super.onResume();

    }

    @Override
    protected void onPause() {
        super.onPause();
    }


}