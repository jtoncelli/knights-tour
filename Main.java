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
    public static int[] findMinDist(int[][] moveOptions){
        int min = -1;
        int bestMove[] = new int []{0,0};
        for(int j = 0; j < moveOptions.length; j++){
            if(min == -1 && moveOptions[j][2] != -1 && moveOptions[j][0] !=0){
                bestMove = chooseMove(j);
                min = moveOptions[j][2];

            }
            else if(moveOptions[j][2] != -1 && moveOptions[j][0] !=0 && moveOptions[j][2] < min){
                bestMove = chooseMove(j);
                min = moveOptions[j][2];
            }

        }
        return new int[]{bestMove[0], bestMove[1], min};
    }
    public static int[]decidePathMove(int[][]board, int row, int col, int endRow, int endCol, int dist){
        dist++;
        int amt = board[row][col];
        board[row][col] = 0;
        boolean solution = false;
        for(int i = 0; i< 8; i++){
            int[] possibleMove = chooseMove(i);
            if(row + possibleMove[0] == endRow &&  col + possibleMove[1] == endCol){
//                dist++;
                solution = true;
//                System.out.println("Dist for found solution: " + dist + " --------------------------");
//                System.out.println("Decide successful move: " + possibleMove[0] + ", " + possibleMove[1] + " at coord " + row + ", " + col);
//                System.out.println("Proof of success: " + (row + possibleMove[0]) + ", " + (col + possibleMove[1]) + "---------------------------");

                return new int[]{possibleMove[0], possibleMove[1], dist, solution ? 1 : 0};
            }
        }
        int[][] moveOptions = new int[8][1];
        for(int j = 0; j < moveOptions.length; j++){
            moveOptions[j] = new int[]{-1, -1, -1};
        }
        boolean immediateSolution = false;
        for(int i = 0; i< 8; i++) {
            int[] possibleMove = chooseMove(i);

            if(!immediateSolution && canMove(board, row, col, possibleMove[0], possibleMove[1])){
                moveOptions[i] = decidePathMove(board,row+possibleMove[0], col+possibleMove[1], endRow, endCol, dist);
//                if(moveOptions[i].length > 3){
//                    System.out.println("success " + dist);
//                    immediateSolution = true;
//                }
            }


        }
//        System.out.println("Decide move: " + bestMove[0] + ", " + bestMove[1] + " at coord " + row + ", " + col);
//        System.out.println("result of move: " + (row + bestMove[0]) + ", " + (col + bestMove[1]));
        return findMinDist(moveOptions);
    }
    public static ArrayList<Integer[]> findPath(int [][] board, ArrayList<Integer[]> path, int startRow, int startCol, int endRow, int endCol){

//        board[startRow][startCol] = 0;
//        board = updateBoard(board);
        int[]nextMove = decidePathMove(board, startRow, startCol, endRow, endCol, 0);
        path.add(new Integer[]{nextMove[0], nextMove[1]});
        int row = startRow;
        int col = startCol;
        while(row + nextMove[0] != endRow && col + nextMove[1] != endCol){
//            System.out.println(row + ", " + col);
//            System.out.println("Move=" + nextMove[0] + ", " + nextMove[1]);
            board[row][col] = 0;
            row += nextMove[0];
            col += nextMove[1];
            board[row][col] = 0;
            nextMove = decidePathMove(board, row, col, endRow, endCol, 0);
            path.add(new Integer[]{nextMove[0], nextMove[1]});
            if(nextMove[0] == 0){
                System.out.println("error---------------------");
                break;
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
        System.out.println(Arrays.toString(decidePathMove(board, 0,0, 4, 2, 0)));

////
//
//        ArrayList<Integer[]> path = findPath(board, new ArrayList<Integer[]>() ,0, 0, 1, 4);
//        int startRow = 0;
//        int startCol = 0;
//        System.out.println("Initial position: " + startRow + ", " + startCol);
//        for(int i = 0; i < path.size(); i++){
//            System.out.println("Move " + i + ":" + Arrays.toString(path.get(i)));
//            startRow+= path.get(i)[0];
//            startCol+= path.get(i)[1];
//            System.out.println("After move " + i + ": " + startRow + ", " + startCol);
//        }

    }
}
