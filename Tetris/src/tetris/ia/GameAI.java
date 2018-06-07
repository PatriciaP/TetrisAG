/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tetris.ia;

import java.util.concurrent.CountDownLatch;
import tetris.Board;
import tetris.GamePanel;

/**
 * Game loop da AI
 *
 * @author patri
 */
public class GameAI extends GamePanel {

    //Durante a execucao do AG, cada agente sera compartilhado
    // por n threads diferentes, onde cada thread representa um game 
    //especifico com sequencia fixa de pecas.
    // Qualidade do agente é quantificada pela media de linhas removidas em n jogos
    //sincronia para verificar se todas as threads do agente finalizou
    private CountDownLatch doneSignal;
    private boolean doSleep = true;
    TetrisAgent agent = new TetrisAgent();
    //qtd de linhas removidas ao fim do jogo
    int cleared = 0;

    public GameAI(int row, int cols, int blocksize, int seed, CountDownLatch signal) {
        this(row, cols, blocksize, seed, false);
        this.doneSignal = signal;
    }

    public GameAI(int row, int cols, int blocksize, int seed, boolean doSleep) {
        super(row, cols, blocksize, seed);
        this.doSleep = doSleep;
    }

    public GameAI(int row, int cols, int blocksize, boolean doSleep) {
        super(row, cols, blocksize);
        this.doSleep = doSleep;
    }

    @Override
    public void run() {
        fallingPiece = randomPiece();

        while (!gameOver) {
            if (board.canMoveDown(fallingPiece)) {
                TetrisMove bestMove = MoveSearcher.getBestMove(board, fallingPiece.clonePiece(), agent);

                fallingPiece.position = bestMove.position;
                fallingPiece.rotation = bestMove.rotation;

                board.updateBoard(fallingPiece);

            } else {
                board.updateBoard(fallingPiece);
                cleared += board.checkCompleteRows();
                addNewPiece();
            }

            if (doSleep) {
                try {
                    Thread.sleep(20L);
                } catch (InterruptedException ex) {
                }
            }

            repaint();
        }

        agent.clearedRowsArray.add(cleared);  //acessado por varias threads!
        if (doneSignal != null) {
            doneSignal.countDown();
        }

    }

}
