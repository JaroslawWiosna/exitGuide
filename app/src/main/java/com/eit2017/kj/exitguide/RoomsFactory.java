package com.eit2017.kj.exitguide;

public class RoomsFactory {

    static public Room produceNeighbour(Room room, Direction direction){
        Room neighbour = new Room();
        neighbour.id = FloorPlan.getInstance().getRoomMap().size();
        FloorPlan.getInstance().getRoomMap().put(neighbour.id,neighbour);
        room.doors[direction.index] = true;
        room.neighbours[direction.index] = neighbour.id;
        neighbour.doors[direction.getOposite()] = true;
        neighbour.neighbours[direction.getOposite()] = room.id;

        return neighbour;
    }

    static public void generateSampleMap(){
        Room start = new Room();
        start.id = FloorPlan.getInstance().getRoomMap().size();
        FloorPlan.getInstance().getRoomMap().put(start.id,start);
        Room r1 = produceNeighbour(start,Direction.North);
        Room r2 = produceNeighbour(r1,Direction.North);
        produceNeighbour(r2,Direction.North);
        Room r4 = produceNeighbour(r2, Direction.West);
        produceNeighbour(r4, Direction.South);
        produceNeighbour(r4, Direction.North);
        Room r7 = produceNeighbour(r4, Direction.West);
        produceNeighbour(r7, Direction.South);
        produceNeighbour(r7, Direction.North);
        Room r10 = produceNeighbour(r7,Direction.West);
        produceNeighbour(r10,Direction.West);
        Room r12 = produceNeighbour(r10,Direction.South);
        Room r13 = produceNeighbour(r12,Direction.South);
        produceNeighbour(r13,Direction.East);
        Room r15 = produceNeighbour(r12,Direction.West);
        produceNeighbour(r15,Direction.West);
    }
}
