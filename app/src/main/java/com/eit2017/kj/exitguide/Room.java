package com.eit2017.kj.exitguide;

public class Room {

    String number;
    String bluetoothName;
    Integer id;
    // Array of existing doors at North, East, South and West wall
    boolean doors[];
    // Array of existing neighbours behind North, East, South and West wall
    int neighbours[];

    Room(String inNumber, String inBtName)
    {
        number = inNumber;
        bluetoothName = inBtName;
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
