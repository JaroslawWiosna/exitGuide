package com.eit2017.kj.exitguide;

import android.widget.Toast;
import android.content.Context;

import java.util.HashMap;
import java.util.Map;

public class FloorPlan {

    private Map<Integer,Room> roomMap;

    private static FloorPlan instance = null;

    // Assumption that only one Map can be loaded at time
    public static FloorPlan getInstance(){
        if(instance == null){
            instance = new FloorPlan();
        }
        return instance;
    }

    // Function for future use while application will load
    // multiple plans
    public static void resetFloorPlan(){
        instance = null;
    }

    private FloorPlan() {
        this.roomMap = new HashMap<>();
    }

    public Map<Integer, Room> getRoomMap() {
        return roomMap;
    }

    public Integer getRoomIdFromRoomNumber(String roomNumber) {
        for (Map.Entry<Integer, Room> entry : roomMap.entrySet()) {
            if (entry.getValue().number == roomNumber) {
                return entry.getValue().id;
            }
        }
        return -1;
    }
}
