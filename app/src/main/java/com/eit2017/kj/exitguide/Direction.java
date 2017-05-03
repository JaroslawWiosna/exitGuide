package com.eit2017.kj.exitguide;

public enum Direction {
    North(0),West(1),South(2),East(3);

    int index;

    Direction(int index) {
        this.index = index;
    }

    public int getOposite(){
        int result = this.index - 2;
        if (result < 0) {
            result += 4;
        }
        return result;
    }
}
