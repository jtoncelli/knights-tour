package com.example.knightstour;

import java.util.ArrayList;

public class StackFrontier {
    protected ArrayList<Node> frontier = new ArrayList<Node>();
    protected int len = 0;
    public StackFrontier(){

    }

    public void add(Node node){
        frontier.add(node);
        len++;
    }
    public boolean contains(Node node){
        for(Node element : frontier){
            if (element == node)
                return true;
        }
        return false;
    }
    public boolean empty(){
        if(len == 0)
            return true;
        return false;
    }
    public Node remove(){
        if(this.empty()){
            throw new IllegalStateException("Empty frontier");
        }
        else{
            Node node = frontier.remove(frontier.size() - 1);
            len--;
            return node;
        }
    }
}
