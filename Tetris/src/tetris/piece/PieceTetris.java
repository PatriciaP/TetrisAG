/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tetris.piece;

import java.awt.Graphics;

/**
 * @author Patricia Pieroni Metodos para movimentacao , rotacao, e desenhar;
 *
 */
public class PieceTetris {

    /*Objetos desta classe sabem se mover, porem a decisao e a logica para lidar
     com o movimento e colisao sao implementadas em outra classe*/

 /*Cada peca possui um tipo, rotacao e posicao*/
    public int kind;
    public int rotation;
    //Inicializando a posicao upper-left
    public Point position = new Point(0, 0);
    //BlockSize, especifica o tamanho dos blocos que formam a peça
    public int blockSize = 20;

    //facilita a fazer o uso de simetria, bloco pivo de rotacao fixado
    static final int PIECE_SIZE = 5;
    //BLOCKS_PIECE informa quantos blocos serao desenhados por peca
    static final int BLOCKS_PIECE = 4;


    /*Metodos para movimentacao , rotacao, e desenhar*/
    public void draw(Graphics g) {
        g.setColor(PieceColor.COLORS[this.kind]);

        for (int i = 0; i < PIECE_SIZE; i++) {
            for (int j = 0; j < PIECE_SIZE; j++) {
                if (PieceStruct.PIECES[kind][rotation][i][j] == 1) {
                    g.fillRect(position.x + j * blockSize,
                            position.y + i * blockSize, blockSize, blockSize);
                }
            }
        }
    }

    public void moveLeft() {
        position.x -= blockSize;
    }

    public void moveRight() {
        position.x += blockSize;
    }

    public void moveDown() {
        position.y += blockSize;
    }

    // rotacao sentido horario
    public void rotateClockWise() {
        rotation++;
        if (rotation > 3) {
            rotation = 0;
        }
    }

    //rotacao contrario ao sentido horario
    public void rotateCounterClockWise() {
        rotation--;
        if (rotation < 0) {
            rotation = 3;
        }
    }

    /*Retorna índices (linha, coluna) ocupados por determinada peça,
    dado a rotação e a posição. Ao mover ou rotacionar uma peça,
    calcula-se a nova rotação ou posição, e chama pieceIndexes passando estes valores,
    obtendo assim os novos índices ocupados. Caso estes índices estejam livres o movimento é possível,
    e as variáveis de instância rotação e posição são atualizadas.
    Caso contrário, a colisão é detectada e a peça não se move. 
     */
    public Point[] boardIndexes(Point pos, int rot) {
        Point[] indexes = new Point[BLOCKS_PIECE];

        int n = 0;
        for (int i = 0; i < PIECE_SIZE; i++) {
            for (int j = 0; j < PIECE_SIZE; j++) {
                if (PieceStruct.PIECES[kind][rot][i][j] == 1) {
                    int col = j + pos.x / blockSize;
                    int row = i + pos.y / blockSize;
                    indexes[n] = new Point(col, row);
                    n++;
                }
            }
        }

        return indexes;
    }

    public PieceTetris clonePiece() {
        PieceTetris p = new PieceTetris();
        p.kind = this.kind;
        p.rotation = this.rotation;
        p.position = new Point(this.position.x, this.position.y);
        p.blockSize = this.blockSize;

        return p;

    }
}
