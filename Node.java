package com.example.knightstour;

import java.util.ArrayList;
import java.util.Arrays;

public class Node {
    public Integer[] state;
    //current coordinate on the board
    public Node parent;
    //previous node on the board
    public ArrayList<Integer[]> action;
    //arraylist of possible moves on the board
    public Node(Integer[] state, Node parent, ArrayList<Integer[]> action){
        this.state = state;
        this.parent = parent;
        // "null" value of parent is {-1, -1}
        this.action = action;
    }
//    public boolean equals(Node other){
//        return (Arrays.equals(state, other.state) && Arrays.equals(parent, other.parent));
//    }
}
