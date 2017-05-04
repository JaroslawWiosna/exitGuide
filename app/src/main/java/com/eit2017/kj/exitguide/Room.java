package com.eit2017.kj.exitguide;

public class Room {

    String number;
    int id;
    // Array of existing doors at North, West, South and East wall
    boolean doors[];
    // Array of existing neighbours behind North, West, South and East wall
    int neighbours[];

    Room()
    {
        number = "";
        doors = new boolean[4];
        neighbours = new int[4];
        for (int i=0; i<4; i++) {
            neighbours[i]=-1;
        }
    }

    public int checkNumberOfDoors() {
        int counter = 0;
        for (boolean door:
             doors) {
            if (door) {
                counter++;
            }
        }
        return counter;
    }
}
