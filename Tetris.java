/*
* CSC 171
*
* Version 1.0
*
* Copyright Yukako
*
* Course : CSC171 FALL 2014
*
* Assignment : Project 4
*
* Author : Yukako Ito
*
* Lab Session : Tues/Thurs 4:50pm
*
* Lab TA : Grace Heard
*
* Last Revised : December 4, 2014
*
*/

import java.awt.BorderLayout;
import javax.swing.JFrame;
import javax.swing.JLabel;

public class Tetris extends JFrame {

	JLabel counterbar;

    public Tetris() {

        counterbar = new JLabel(" 0    Press 'P' to pause    Press 'Space Bar' to accelerate drop    Press 's' to restart game");
        
        add(counterbar, BorderLayout.SOUTH);
        Board board = new Board(this);
        add(board);
        board.start();

        setSize(550, 700);
        setTitle("Tetris - Yukako Ito");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
   }

   public JLabel getCounterbar() {
       return counterbar;
   }

    public static void main(String[] args) {

        Tetris game = new Tetris();
        game.setLocationRelativeTo(null);
        game.setVisible(true);

    } 
    
}
