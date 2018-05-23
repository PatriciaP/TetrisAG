/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tetris;

import java.awt.Color;
import javax.swing.BorderFactory;
import javax.swing.JFrame;
import tetris_game_panel.GamePanel;

/**
 *
 * @author patri
 */
public class Tetris {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        JFrame f = new JFrame("Tetris - GA");

        int rows = 20;
        int cols = 10;
        int blockSize = 50;

        GamePanel gamePanel = new GamePanel(rows, cols, blockSize);
        gamePanel.setBackground(Color.BLACK);
        gamePanel.setBorder(BorderFactory.createLineBorder(Color.white));

        f.add(gamePanel);
        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        f.pack();
        f.setVisible(true);
        f.setResizable(false);

        gamePanel.startNewGame();
    }

}
