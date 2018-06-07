/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tetris.ia;

import java.util.ArrayList;
import tetris.Board;
import tetris.TetrisBoard;
import tetris.piece.PieceTetris;
import tetris.piece.Point;

/**
 *
 * @author patri
 */
public class MoveSearcher {

    public static ArrayList<TetrisMove> getValidMoves(Board board, PieceTetris piece, TetrisAgent agent) {
        ArrayList<TetrisMove> moves = new ArrayList<TetrisMove>();
        Point originalPosition = new Point(piece.position.x, piece.position.y);

        for (int i = 0; i < 4; i++) {
            piece.rotation = i;
            int startY = piece.position.y;

            while (board.canMoveLeft(piece)) {
                piece.moveLeft();
            }

            do {
                while (board.canMoveDown(piece)) {
                    piece.moveDown();
                }

                board.updateBoard(piece);

                float eval = agent.eval(board.board);
                TetrisMove tempMove = new TetrisMove();
                tempMove.eval = eval;
                tempMove.position.x = piece.position.x;
                tempMove.position.y = piece.position.y;
                tempMove.rotation = piece.rotation;

                moves.add(tempMove);

                board.removePiece(piece);
                piece.position.y = startY;

                if (!board.canMoveRight(piece)) {
                    break;
                } else {
                    piece.moveRight();
                }

            } while (true);

            piece.position = originalPosition;
        }

        return moves;
    }

    /**
     * invoca getValidMoves e retorna o lance com maior eval
     *
     * @param piece
     * @param agent
     * @return
     */
    public static TetrisMove getBestMove(Board board, PieceTetris piece, TetrisAgent agent) {
        TetrisMove best = null;
        ArrayList<TetrisMove> moves = getValidMoves(board, piece, agent);

        for (TetrisMove move : moves) {
            if (best == null) {
                best = move;
            } else if (move.eval > best.eval) {
                best = move;
            }
        }
        return best;
    }

}
