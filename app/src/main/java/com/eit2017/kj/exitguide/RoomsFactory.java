package com.eit2017.kj.exitguide;

public class RoomsFactory {

    static public Room produceNeighbourWithDoors(Room room, String numberOfNeighbourRoom, Direction direction){
        Room neighbour = new Room(numberOfNeighbourRoom);
        neighbour.id = FloorPlan.getInstance().getRoomMap().size();
        FloorPlan.getInstance().getRoomMap().put(neighbour.id,neighbour);
        room.doors[direction.index] = true;
        room.neighbours[direction.index] = neighbour.id;
        neighbour.doors[direction.getOposite()] = true;
        neighbour.neighbours[direction.getOposite()] = room.id;

        return neighbour;
    }

    static public void produceNeighbourWithoutDoors(Room firstRoom, Room secondRoom, Direction direction) {
        firstRoom.doors[direction.index] = false; //needed?
        firstRoom.neighbours[direction.index] = secondRoom.id;
        secondRoom.doors[direction.getOposite()] = false; // needed?
        secondRoom.neighbours[direction.getOposite()] = firstRoom.id;
    }

    static public void generateSampleMap(){
        Room start = new Room("R1");
        start.id = FloorPlan.getInstance().getRoomMap().size();
        FloorPlan.getInstance().getRoomMap().put(start.id,start);
        Room r1 = produceNeighbourWithDoors(start, "K1", Direction.North);
        Room r2 = produceNeighbourWithDoors(r1,"K2", Direction.North);
        Room r3 = produceNeighbourWithDoors(r2, "R3", Direction.North);
        Room r4 = produceNeighbourWithDoors(r2, "K4", Direction.West);
        Room r5 = produceNeighbourWithDoors(r4, "R5", Direction.South);
        Room r6 = produceNeighbourWithDoors(r4, "R6", Direction.North);
        Room r7 = produceNeighbourWithDoors(r4, "K7", Direction.West);
        Room r8 = produceNeighbourWithDoors(r7, "R8", Direction.South);
        Room r9 = produceNeighbourWithDoors(r7, "R9", Direction.North);
        Room r10 = produceNeighbourWithDoors(r7, "K10", Direction.West);
        Room r11 = produceNeighbourWithDoors(r10,"R11", Direction.West);
        Room r12 = produceNeighbourWithDoors(r10, "K12", Direction.South);
        Room r13 = produceNeighbourWithDoors(r12, "R13", Direction.South);
        Room r14 = produceNeighbourWithDoors(r13, "R14", Direction.East);
        Room r15 = produceNeighbourWithDoors(r12, "K15", Direction.West);
        produceNeighbourWithDoors(r15, "EXIT", Direction.West);

        produceNeighbourWithoutDoors(r1, r5, Direction.West);
        produceNeighbourWithoutDoors(r3, r6, Direction.West);
        produceNeighbourWithoutDoors(r5, r8, Direction.West);
        produceNeighbourWithoutDoors(r6, r9, Direction.West);
        produceNeighbourWithoutDoors(r8, r12, Direction.West);
        produceNeighbourWithoutDoors(r14, r8, Direction.North);
        produceNeighbourWithoutDoors(r15, r11, Direction.North);
    }
}
