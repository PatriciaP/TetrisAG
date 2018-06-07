/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tetris;

import tetris.piece.PieceTetris;
import tetris.piece.Point;

/**
 *
 * @author Patricia Pieroni
 */
/*
   Classe representa onde as pecas se movimentam  
 */
public class Board {

    public int rows;
    public int cols;
    public int[][] board;

    /**
     * Inicializar o array board.
     *
     * @param rows
     * @param cols
     */
    public Board(int rows, int cols) {
        this.rows = rows;
        this.cols = cols;

        /*
        Rows + 1 linhas e cols + 2 colunas, linha
        extra para detectar colisão com o fundo da grade, duas colunas extras
        para detectar colisão com as laterais.
         */
        board = new int[rows + 1][cols + 2];

        for (int i = 0; i < rows + 1; i++) {
            board[i][0] = 1;
            board[i][cols + 1] = 1;
        }
        for (int j = 0; j < cols + 2; j++) {
            board[rows][j] = 1;
        }

    }

    /**
     * Remove uma peça da grade, obtendo os índices ocupados por essa peça
     * (através do método pieceIndexes do objeto TetrisPiece) e zerando os
     * mesmos.
     *
     * @param piece
     */
    public void removePiece(PieceTetris piece) {
        Point[] indexes = piece.boardIndexes(piece.position, piece.rotation);

        for (Point p : indexes) {
            int row = p.y;
            if (row < 0) {
                continue;
            }
            int col = p.x + 1;

            board[row][col] = 0;
        }
    }

    /**
     * Resetar a grade
     */
    public void reset() {
        for (int i = 0; i < board.length - 1; i++) {
            for (int j = 1; j < board[i].length - 1; j++) {
                board[i][j] = 0;
            }
        }
    }

    public void print() {
        for (int i = 0; i < board.length - 1; i++) {
            for (int j = 1; j < board[i].length - 1; j++) {
                System.out.print(board[i][j] + " ");
            }
            System.out.println();
        }
        System.out.println();
    }

    /**
     * O método isColliding recebe uma array de índices e verifica se ao menos
     * um desses índices está ocupado, o que resultaria em colisão. OBS: indices
     * negativos e pecas presas (bug) (tratando nos primeiros ifs);
     *
     * @param indexes
     * @return
     */
    public boolean isColliding(Point[] indexes) {
        boolean collide = false;

        for (Point p : indexes) {
            int row = p.y;
            int col = p.x + 1;

            if (col < 1 || col > cols) {
                collide = true;
                break;
            }
            if (row < 0) {
                continue;
            }

            if (board[row][col] > 0) {
                collide = true;
                break;
            }
        }

        return collide;
    }

    /*
     Ao receber input de movimento (do usuário ou da AI), 
    estes métodos serão chamados para decidir se o movimento é possível,
     */
    public boolean canMoveLeft(PieceTetris piece) {
        Point pos = new Point(piece.position.x - piece.blockSize, piece.position.y);
        Point[] indexes = piece.boardIndexes(pos, piece.rotation);

        return !isColliding(indexes);
    }

    public boolean canMoveRight(PieceTetris piece) {
        Point pos = new Point(piece.position.x + piece.blockSize, piece.position.y);
        Point[] indexes = piece.boardIndexes(pos, piece.rotation);

        return !isColliding(indexes);
    }

    public boolean canMoveDown(PieceTetris piece) {
        Point pos = new Point(piece.position.x, piece.position.y + piece.blockSize);
        Point[] indexes = piece.boardIndexes(pos, piece.rotation);

        return !isColliding(indexes);
    }

    public boolean canRotateCW(PieceTetris piece) {
        int rot = piece.rotation;
        rot++;

        if (rot > 3) {
            rot = 0;
        }

        Point[] indexes = piece.boardIndexes(piece.position, rot);

        return !isColliding(indexes);
    }

    public boolean canRotateCCW(PieceTetris piece) {
        int rot = piece.rotation;
        rot--;

        if (rot < 0) {
            rot = 3;
        }

        Point[] indexes = piece.boardIndexes(piece.position, rot);

        return !isColliding(indexes);
    }

    /**
     * Verifica situacao de Game Over, se a prox peca nao possui espaco.
     *
     * @param piece
     * @return
     */
    public boolean willFitNext(PieceTetris piece) {
        Point[] indexes = piece.boardIndexes(piece.position, piece.rotation);

        return !isColliding(indexes);
    }

    /**
     * Verifica situacao de Game Over, quando a ultima peca encaixada fica
     * parcialmente de fora
     *
     * @param piece
     * @return
     */
    public boolean pieceLandOffScreen(PieceTetris piece) {
        Point[] indexes = piece.boardIndexes(piece.position, piece.rotation);
        boolean offscreen = false;

        for (Point p : indexes) {
            if (p.y < 0) {
                offscreen = true;
                break;
            }
        }
        return offscreen;
    }

    /**
     * Atualiza a grade, recebendo a peca como parametro e atualizando os
     * indices da board.
     *
     * @param piece
     */
    public  void updateBoard(PieceTetris piece) {
        Point[] indexes = piece.boardIndexes(piece.position, piece.rotation);

        for (Point p : indexes) {
            int row = p.y;
            if (row < 0) {
                continue;
            }
            int col = p.x + 1;

            board[row][col] = piece.kind + 1;
        }

    }

    /**
     * Remocao das linhas completadas
     *
     * @return
     */
    public int checkCompleteRows() {
        int completeRows = 0;
        for (int y = 0; y < rows; y++) {
            boolean ok = true;
            for (int x = 1; (x < cols + 1) && ok; x++) {
                if (board[y][x] == 0) {
                    ok = false;
                }
            }
            if (ok) {
                completeRows++;
                deleteRow(y);
            }
        }
        return completeRows;
    }

    public void deleteRow(int r) {
        for (int i = r; i > 0; i--) {
            for (int j = 1; j < cols + 1; j++) {
                board[i][j] = board[i - 1][j];
            }
        }
        for (int j = 1; j < cols + 1; j++) {
            board[0][j] = 0;
        }

    }
}
