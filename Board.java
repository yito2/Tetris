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

package tetris;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.Timer;

import tetris.Shape.Tetrominoes;

public class Board extends JPanel implements ActionListener {

    final int BoardWidth = 10;
    final int BoardHeight = 22;
    public static final int tileSize = 24;
	public static final int shadeWidth = 4;
	public static final int panelHeight = 550 * tileSize + 10;

    Timer timer;
    boolean isFallingDone = false;
    boolean Start = false;
    boolean Pause = false;
    int numLinesRemoved = 0;
    int curX = 0;
    int curY = 0;
    JLabel counterbar;
    Shape currentPiece;
    Tetrominoes[] board;

    public Board(Tetris parent) {

       setFocusable(true);
       currentPiece = new Shape();
       timer = new Timer(400, this);
       timer.start(); 

       counterbar =  parent.getCounterbar();
       board = new Tetrominoes[BoardWidth * BoardHeight];
       addKeyListener(new TAdapter());
       clearBoard();  
    }

    public void actionPerformed(ActionEvent e) {
        if (isFallingDone) {
            isFallingDone = false;
            newPiece();
        } else {
            oneLineDown();
        }
    }

    int squareWidth() { return (int) getSize().getWidth() / BoardWidth; }
    int squareHeight() { return (int) getSize().getHeight() / BoardHeight; }
    Tetrominoes shapeAt(int x, int y) { return board[(y * BoardWidth) + x]; }

    public void start() {
        if (Pause)
            return;

        Start = true;
        isFallingDone = false;
        numLinesRemoved = 0;
        clearBoard();

        newPiece();
        timer.start();
    }

    private void pause() {
        if (!Start)
            return;

        Pause = !Pause;
        if (Pause) {
            timer.stop();
            counterbar.setText("PAUSED    Press 'P' to pause    Press 'Space Bar' to accelerate drop    Press 's' to restart game");
        } else {
            timer.start();
            counterbar.setText(String.valueOf(numLinesRemoved + "    Press 'P' to pause    Press 'Space' to accelerate drop    Press 's' to restart game"));
        }
        repaint();
    }

    public void paint(Graphics g)
    { 
        super.paint(g);

        Dimension size = getSize();
        int boardTop = (int) size.getHeight() - BoardHeight * squareHeight();


        for (int i = 0; i < BoardHeight; ++i) {
            for (int j = 0; j < BoardWidth; ++j) {
                Tetrominoes shape = shapeAt(j, BoardHeight - i - 1);
                if (shape != Tetrominoes.NoShape)
                    drawSquare(g, 0 + j * squareWidth(),
                               boardTop + i * squareHeight(), shape);
            }
        }

        if (currentPiece.getShape() != Tetrominoes.NoShape) {
            for (int i = 0; i < 4; ++i) {
                int x = curX + currentPiece.x(i);
                int y = curY - currentPiece.y(i);
                drawSquare(g, 0 + x * squareWidth(),
                           boardTop + (BoardHeight - y - 1) * squareHeight(),
                           currentPiece.getShape());
            }
        }
    }

    private void dropDown()
    {
        int newY = curY;
        while (newY > 0) {
            if (!tryMove(currentPiece, curX, newY - 1))
                break;
            --newY;
        }
        pieceDropped();
    }

    private void oneLineDown()
    {
        if (!tryMove(currentPiece, curX, curY - 1))
            pieceDropped();
    }


    private void clearBoard()
    {
        for (int i = 0; i < BoardHeight * BoardWidth; ++i)
            board[i] = Tetrominoes.NoShape;
    }

    private void pieceDropped()
    {
        for (int i = 0; i < 4; ++i) {
            int x = curX + currentPiece.x(i);
            int y = curY - currentPiece.y(i);
            board[(y * BoardWidth) + x] = currentPiece.getShape();
        }

        removeFullLines();

        if (!isFallingDone)
            newPiece();
    }

    private void newPiece()
    {
        currentPiece.setRandomShape();
        curX = BoardWidth / 2 + 1;
        curY = BoardHeight - 1 + currentPiece.minY();

        if (!tryMove(currentPiece, curX, curY)) {
            currentPiece.setShape(Tetrominoes.NoShape);
            timer.stop();
            Start = false;
            counterbar.setText("game over    Press 's' to restart game");
        }
    }

    private boolean tryMove(Shape newPiece, int newX, int newY)
    {
        for (int i = 0; i < 4; ++i) {
            int x = newX + newPiece.x(i);
            int y = newY - newPiece.y(i);
            if (x < 0 || x >= BoardWidth || y < 0 || y >= BoardHeight)
                return false;
            if (shapeAt(x, y) != Tetrominoes.NoShape)
                return false;
        }

        currentPiece = newPiece;
        curX = newX;
        curY = newY;
        repaint();
        return true;
    }

    private void removeFullLines()
    {
        int numFullLines = 0;

        for (int i = BoardHeight - 1; i >= 0; --i) {
            boolean lineIsFull = true;

            for (int j = 0; j < BoardWidth; ++j) {
                if (shapeAt(j, i) == Tetrominoes.NoShape) {
                    lineIsFull = false;
                    break;
                }
            }

            if (lineIsFull) {
                ++numFullLines;
                for (int k = i; k < BoardHeight - 1; ++k) {
                    for (int j = 0; j < BoardWidth; ++j)
                         board[(k * BoardWidth) + j] = shapeAt(j, k + 1);
                }
            }
        }

        if (numFullLines > 0) {
            numLinesRemoved += numFullLines;
            counterbar.setText(String.valueOf(numLinesRemoved + "    Press 'P' to pause    Press 'Space Bar' to accelerate drop    Press 's' to restart game"));
            isFallingDone = true;
            currentPiece.setShape(Tetrominoes.NoShape);
            repaint();
        }
     }

    private void drawSquare(Graphics g, int x, int y, Tetrominoes shape)
    {
        Color colors[] = { new Color(0, 0, 0), new Color(204, 102, 102), 
            new Color(102, 204, 102), new Color(102, 102, 204), 
            new Color(204, 204, 102), new Color(204, 102, 204), 
            new Color(102, 204, 204), new Color(218, 170, 0)
        };


        Color color = colors[shape.ordinal()];

        g.setColor(color);
        g.fillRect(x + 1, y + 1, squareWidth() - 2, squareHeight() - 2);

        g.setColor(color.brighter());
        g.drawLine(x, y + squareHeight() - 1, x, y);
        g.drawLine(x, y, x + squareWidth() - 1, y);

        g.setColor(color.darker());
        g.drawLine(x + 1, y + squareHeight() - 1,
                         x + squareWidth() - 1, y + squareHeight() - 1);
        g.drawLine(x + squareWidth() - 1, y + squareHeight() - 1,
                         x + squareWidth() - 1, y + 1);
    }

    class TAdapter extends KeyAdapter {
         public void keyPressed(KeyEvent e) {

             if (!Start || currentPiece.getShape() == Tetrominoes.NoShape) {  
                 return;
             }

             int keycode = e.getKeyCode();

             if (keycode == 'p' || keycode == 'P') {
                 pause();
                 return;
             }

             if (Pause)
                 return;

             switch (keycode) {
             case KeyEvent.VK_LEFT:
                 tryMove(currentPiece, curX - 1, curY);
                 break;
             case KeyEvent.VK_RIGHT:
                 tryMove(currentPiece, curX + 1, curY);
                 break;
             case KeyEvent.VK_DOWN:
                 tryMove(currentPiece.rotateRight(), curX, curY);
                 break;
             case KeyEvent.VK_UP:
                 tryMove(currentPiece.rotateLeft(), curX, curY);
                 break;
             case KeyEvent.VK_SPACE:
                 dropDown();
                 break;
             case 'S':
                 clearBoard();
                 start();
                 break;
             }
         }
     }
}
