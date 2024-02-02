package com.example.knightstour;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;
import java.util.Scanner;

public class Main {
    public static boolean canMove(int[][] board, int row, int col, int hMove, int vMove){
        //given current coord, board, and move, checks if it is possible to make that move
        try{
            if(board[row+hMove][col+vMove] == 0)
                return false;
        }
        catch(IndexOutOfBoundsException e){
            return false;
        }
        return true;
    }
    public static int[] chooseMove(int index){
        //returns one of eight possible moves
        int [][]moves = new int[][]{{1, 2}, {1, -2}, {-1, 2}, {-1, -2}, {2, 1}, {2, -1}, {-2, 1}, {-2, -1}};
        if(index<8)
            return moves[index];
        return new int[]{0, 0};
    }
    public static int[][] makeMove(int[][] board, int row, int col, int hMove, int vMove){
        //assumes move can be made
        if(hMove == 0 && vMove == 0){
            return board;
        }
        board[row+hMove][col+vMove] = 0;
        return board;
    }
    public static Integer[]tryMove(int row, int col, int hMove, int vMove){
        return new Integer[]{row + hMove, col+vMove};
    }
    public static int[][] updateBoard(int[][] board){
        //updates accessibility index for each square after making a move
        for(int row=0; row<board.length; row++){
            for(int col=0; col<board[row].length; col++){
                if(board[row][col] == 0)
                    continue;
                else{
                    int num = 0;
                    int move[];
                    for(int i=0; i<8;i++){
                        move = chooseMove(i);
                        if(canMove(board, row, col, move[0], move[1]))
                            num++;
                    }
                    board[row][col] = num;
                }

            }
        }
        return board;
    }
    public static int[]helperDecideMove(int[][]board, int row, int col){
        //will decide a next move based on the move with the lowest possible accessibility index
        int []move;
        int min = 100;
        int bestMove[] = new int[]{0,0};

        for(int i = 0; i<8; i++){
            move = chooseMove(i);
            if(canMove(board, row, col, move[0], move[1]) ){

                if(board[row+move[0]][col+move[1]] < min){
                    min = board[row+move[0]][col+move[1]];
                    bestMove = move;
                }

            }
        }
        return new int[]{bestMove[0], bestMove[1], min};
    }
    public static int[]decideMove(int[][]board, int row, int col){
        //will decide a next move based on the move with the lowest possible accessibility index
        int []move;
        int min = 100;
        int bestMove[] = new int[]{0,0};

        for(int i = 0; i<8; i++){
            move = chooseMove(i);
            if(canMove(board, row, col, move[0], move[1]) ){
                int []tempMin = helperDecideMove(board,row,col);
                if(board[row+move[0]][col+move[1]] < min){
                    min = board[row+move[0]][col+move[1]];
                    bestMove = move;
                }
                else if(board[row+move[0]][col+move[1]] == min && !Arrays.equals(bestMove, new int[]{tempMin[0], tempMin[1]})){
                    if(tempMin[2] < decideMove(board,row + bestMove[0],col+bestMove[1])[2]){
                        bestMove[0] = tempMin[0];
                        bestMove[1] = tempMin[1];
                    }
                }
            }
        }
        return new int[]{bestMove[0], bestMove[1], min};
    }
    public static void printBoard(int[][]board){
        //displays board in friendly manner
        for(int i=0; i<board.length; i++){
            for(int j=0; j<board[i].length; j++){
                System.out.print(board[i][j] + " ");
            }
            System.out.println("\n");
        }
    }
    public static void knightsTour(){
        int board [][] = new int[][]{{2, 3, 4, 4, 4, 4, 3, 2},{3, 4, 6, 6, 6, 6, 4, 3}, {4, 6, 8, 8, 8, 8, 6, 4}, {4, 6, 8, 8, 8, 8, 6, 4}, {4, 6, 8, 8, 8, 8, 6, 4}, {4, 6, 8, 8, 8, 8, 6, 4},{3, 4, 6, 6, 6, 6, 4, 3}, {2, 3, 4, 4, 4, 4, 3, 2}};
        //initialized and filled board with accessibility index; i.e; how many moves are possible from any given square
        Scanner scan = new Scanner(System.in);
        int row = 0; int col = 0;
        try{
            System.out.println("Enter initial row(1-8): ");
            row = scan.nextInt()-1;
            System.out.println("Enter initial column(1-8): ");
            col = scan.nextInt()-1;
        }catch(NumberFormatException e){
            row = 0; col = 0;
        }
        if(row > 7 || row < 0 || col > 7 || col < 0){
            row = 0; col = 0;
        }
        int counter = 0;
        int [][] temp;
        System.out.println("Initial:");
        printBoard(board);
        for(int i = 0; i < 64; i++){
            temp = board;
            int[]move = helperDecideMove(board, row, col);
            if(!Arrays.equals(move, new int[]{0,0})){
                board = makeMove(board, row, col, move[0], move[1]);
                board = updateBoard(board);
                counter++;
                System.out.println("Move " +counter + ", from row " + (row+1) + " and col " + (col+1) + " to row "+  (row+move[0]+1) + " and col " + (col + move[1]+1));
                row += move[0];
                col += move[1];
                printBoard(board);
                System.out.println("\n\n");
            }
            else{
                break;
            }
        }
        System.out.println(counter + " moves made");
    }

    public static ArrayList<Integer[]> possibleMoves(int[][] board, int row, int col){
        ArrayList<Integer[]> possibleMoves = new ArrayList<Integer[]>();
        for(int i = 0; i < 8; i++){
            if(canMove(board, row, col, chooseMove(i)[0], chooseMove(i)[1])){
                possibleMoves.add(new Integer[]{chooseMove(i)[0], chooseMove(i)[1]});
            }
        }
        return possibleMoves;
    }
    public static ArrayList<Integer[]> shortestPath(int[][] board, Integer[] source, Integer[] target){

        Node startNode = new Node(source, null, possibleMoves(board, source[0], source[1]));

        QueueFrontier frontier = new QueueFrontier();
        frontier.add(startNode);
        ArrayList<Integer[]> path = new ArrayList<Integer[]>();
        ArrayList<Integer[]> exploredNodes = new ArrayList<Integer[]>();

        while(true){
            if(frontier.empty()) break;

            Node node = frontier.remove();;

            exploredNodes.add(node.state);

            for(Integer[] move : node.action){
                if(canMove(board, node.state[0], node.state[1], move[0], move[1])) {
                    Integer[] result = tryMove(node.state[0], node.state[1], move[0], move[1]);
                    if (Arrays.equals(result, target)) {
                        path.add(new Integer[]{move[0], move[1]});

                        while (node.parent != null) {
                            path.add(0, new Integer[]{node.state[0], node.state[1]});
                            node = node.parent;
                        }
                    } else {
                        if (!exploredNodes.contains(move)) {
                            frontier.add(new Node(result, node, possibleMoves(makeMove(board, node.state[0], node.state[1], move[0], move[1]), result[0], result[1])));
                            exploredNodes.add(move);
                        }
                    }
                }

            }
        }

        return path;
    }
    public static void main(String[] args){

//        knightsTour();
        int board[][] = new int[8][8];
        for(int i = 0; i < board.length; i++){
            for(int j = 0; j < board[i].length; j++){
                board[i][j] = 1;
            }
        }
        ArrayList<Integer[]> path = shortestPath(board, new Integer[]{0, 0}, new Integer[]{1, 2});
        int counter = 0;
        for(Integer[] move : path){
            System.out.println("Move " +  (counter++) + ": " + move[0] + " ," + move[1]);
        }
