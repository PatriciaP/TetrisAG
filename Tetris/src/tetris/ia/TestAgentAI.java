/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tetris.ia;

import java.awt.Color;
import javax.swing.JFrame;

/**
 *
 * @author patri
 */
public class TestAgentAI {

    //Para alterar os valores do agente TetrisAgent.randomAgent()
    public static void main(String[] args) {
        JFrame f = new JFrame();
        int rows = 20, cols = 10, block = 32;
        boolean sleep = true;  //para atualizar o panel
        GameAI ai = new GameAI(rows, cols, block, sleep);
        ai.setBackground(Color.BLACK);
        f.add(ai);
        f.pack();
        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        f.setResizable(false);
        f.setVisible(true);
        new Thread(ai).start();
    }

}
