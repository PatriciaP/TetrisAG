/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tetris.ia;

import tetris.piece.Point;

/**
 *
 * @author patri
 */
public class TetrisMove {

    //Posicao que a peca sera encaixada
    public Point position = new Point(0, 0);
    //rotacao que a peca sera encaixada
    public int rotation;
    //qualidade do movimento
    public float eval;

    public TetrisMove() {
    }

    public TetrisMove(Point p, int rot) {
        this.position = p;
        this.rotation = rot;
    }

}
