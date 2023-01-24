package com.example.knightstour;

public class QueueFrontier extends StackFrontier{
    public Node remove(){
        if(this.empty()) {
            throw new IllegalStateException("Empty frontier");
        }
        else{
            Node node = frontier.remove(0);
            len--;
            return node;
        }
    }
}
