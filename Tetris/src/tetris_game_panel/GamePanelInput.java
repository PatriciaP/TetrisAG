/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tetris_game_panel;

import static com.sun.java.accessibility.util.AWTEventMonitor.addKeyListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import tetris_board.BoardTetris;


/**
 *
 * @author Patricia Pieroni
 */
public class GamePanelInput {

    public static void inputKey() {

        addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (GamePanel.gameover) {
                    return;
                }

                int key = e.getKeyCode();
                switch (key) {
                    case KeyEvent.VK_SPACE:
                        break;
                    case KeyEvent.VK_LEFT:
                        if (BoardTetris.canMoveLeft(GamePanel.fallingpiece)) {
                            GamePanel.fallingpiece.moveLeft();
                        }   break;
                    case KeyEvent.VK_RIGHT:
                        if (BoardTetris.canMoveRight(GamePanel.fallingpiece)) {
                            GamePanel.fallingpiece.moveRight();
                        }   break;
                    case KeyEvent.VK_UP:
                    case KeyEvent.VK_NUMPAD3:
                        if (BoardTetris.canRotateCW(GamePanel.fallingpiece)) {
                            GamePanel.fallingpiece.rotateClockWise();
                        }   break;
                    case KeyEvent.VK_DOWN:
                    case KeyEvent.VK_NUMPAD1:
                        if (BoardTetris.canRotateCCW(GamePanel.fallingpiece)) {
                            GamePanel.fallingpiece.rotateCounterClockWise();
                        }   break;
                    default:
                        break;
                }

                new GamePanel().repaint();
            }
        });
    }
}
