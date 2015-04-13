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
import java.util.Random;

public class Shape {

    enum Tetrominoes { NoShape, ZShape, SShape, LineShape, 
               TShape, SquareShape, LShape, MirroredLShape };

    private Tetrominoes pieceShape;
    private int coordinates[][];
    private int[][][] coordinatesTable;


    public Shape() {
        coordinates = new int[4][2];
        setShape(Tetrominoes.NoShape);
    }

    public void setShape(Tetrominoes shape) {
         coordinatesTable = new int[][][] {
            { { 0, 0 },   { 0, 0 },   { 0, 0 },   { 0, 0 } },
            { { 0, -1 },  { 0, 0 },   { -1, 0 },  { -1, 1 } },
            { { 0, -1 },  { 0, 0 },   { 1, 0 },   { 1, 1 } },
            { { 0, -1 },  { 0, 0 },   { 0, 1 },   { 0, 2 } },
            { { -1, 0 },  { 0, 0 },   { 1, 0 },   { 0, 1 } },
            { { 0, 0 },   { 1, 0 },   { 0, 1 },   { 1, 1 } },
            { { -1, -1 }, { 0, -1 },  { 0, 0 },   { 0, 1 } },
            { { 1, -1 },  { 0, -1 },  { 0, 0 },   { 0, 1 } }
        };

        for (int i = 0; i < 4 ; i++) {
            for (int j = 0; j < 2; ++j) {
                coordinates[i][j] = coordinatesTable[shape.ordinal()][i][j];
            }
        }
        pieceShape = shape;
    }

    private void setX(int index, int x) { coordinates[index][0] = x; }
    private void setY(int index, int y) { coordinates[index][1] = y; }
    public int x(int index) { return coordinates[index][0]; }
    public int y(int index) { return coordinates[index][1]; }
    public Tetrominoes getShape()  { return pieceShape; }

    public void setRandomShape() {
        Random r = new Random();
        int x = Math.abs(r.nextInt()) % 7 + 1;
        Tetrominoes[] values = Tetrominoes.values(); 
        setShape(values[x]);
    }

    public int minX() {
      int m = coordinates[0][0];
      for (int i=0; i < 4; i++) {
          m = Math.min(m, coordinates[i][0]);
      }
      return m;
    }


    public int minY() {
      int m = coordinates[0][1];
      for (int i=0; i < 4; i++) {
          m = Math.min(m, coordinates[i][1]);
      }
      return m;
    }

    public Shape rotateLeft() {
        if (pieceShape == Tetrominoes.SquareShape)
            return this;

        Shape result = new Shape();
        result.pieceShape = pieceShape;

        for (int i = 0; i < 4; ++i) {
            result.setX(i, y(i));
            result.setY(i, -x(i));
        }
        return result;
    }

    public Shape rotateRight() {
        if (pieceShape == Tetrominoes.SquareShape)
            return this;

        Shape result = new Shape();
        result.pieceShape = pieceShape;

        for (int i = 0; i < 4; ++i) {
            result.setX(i, -y(i));
            result.setY(i, x(i));
        }
        return result;
    }
}
