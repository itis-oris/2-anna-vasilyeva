package ru.itis.client.utils;

import lombok.Data;

import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Data
public class Game {
    private int[][] board;
    private int prev;
    private int prevRow;
    private int prevCol;
    private int cntPoint;
    // Предположим, что у вас есть структура данных для хранения состояния ячеек
    private boolean[][] openedCells; // true, если ячейка открыта
    private boolean[][] matchedCells; // true, если ячейка совпала

    // Метод для проверки, открыта ли ячейка
//    public boolean isOpened(int row, int col) {
//        return openedCells[row][col];
//    }

    // Метод для проверки, совпала ли ячейка
    public boolean isMatched(int row, int col) {
        return matchedCells[row][col];
    }

    // Метод для обновления состояния ячейки
    public void setCellState(int row, int col, boolean opened, boolean matched) {
        openedCells[row][col] = opened;
        matchedCells[row][col] = matched;
    }

    public Game() {
        board = new int[10][10];
    }

    public boolean isOpened(int row, int col) {
        return board[row][col] != 0;
    }

    private void open(int row, int col) {
        board[row][col] = 1;
    }

    private void close(int row, int col) {
        board[row][col] = 0;
    }

    public int check(int i, int row, int col){
        if(prev == 0){
            open(row, col);
            prev = i;
            prevRow = row;
            prevCol = col;
            return 1;
        }else{
            if(prev == i){
                cntPoint++;
                prev = 0;
                open(prevRow, prevCol);
                open(row, col);
                return 2;
            }
            prev = 0;
            close(prevRow, prevCol);
            close(row, col);

            return 3;
        }
    }
}
