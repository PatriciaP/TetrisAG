/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tetris.ia;

import java.util.List;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

/**
 *
 * @author patri
 */
public class TetrisAgent implements Comparable<Object> {

    /**
     * Habilidade de cada agente expressa por seus genes, cada slot do array
     * corresponde ao peso de determinadas caracteristicas, 1 - numero de linhas
     * feitas; 2 - altura max da pilha; 3 - num de buracos nao conectados; 4 -
     * num de buracos conectados; 5 - num de blocos acima de buracos; 6 - numero
     * de pocos; 7 - nivelacao da grade. OBS: (Valores setados entre 0 e 1 para
     * testes)
     *
     */
    public float[] genes = new float[]{0.9568116f , 0.021857549f ,0.66192174f  ,0.51872456f , 0.07845363f  ,0.1168489f  ,0.22490673f  };

    /**
     * Média de linhas removidas durante os n games. Valores de genes que
     * resultam no maior número médio de linhas removidas (melhor agente) Todos
     * os agentes enfrentarão os mesmo jogos, permitindo balancear o efeito de
     * um agente ruim receber uma sequência boa de peças, ou um agente bom
     * receber uma sequência ruim de peças.
     */
    public float fitness;

    // armazena o num de linhas removidas em cada jogo, cada game sera realizado em uma thread separada
    public List<Integer> clearedRowsArray = Collections.synchronizedList(new ArrayList<>());

    /**
     * Recebe array com a configuracao da grade, e avalia determinada
     * configuracao da grade utilizando metodos da classe BoardEvaluator.
     *
     * @param board
     * @return
     */
    public float eval(int[][] board) {
        float sum = 0.0f;

        sum += genes[0] * BoardAIEvaluator.clearedRows(board);
        sum -= genes[1] * BoardAIEvaluator.pileHeight(board);
        sum -= genes[2] * BoardAIEvaluator.countSingleHoles(board);
        sum -= genes[3] * BoardAIEvaluator.countConnectedHoles(board);
        sum -= genes[4] * BoardAIEvaluator.blocksAboveHoles(board);
        sum -= genes[5] * BoardAIEvaluator.countWells(board);
        sum -= genes[6] * BoardAIEvaluator.bumpiness(board);
        return sum;
    }

    /**
     * Ordenar os agentes.
     *
     * @param obj
     * @return
     */
    @Override
    public int compareTo(Object obj) {
        if (obj instanceof TetrisAgent) {
            TetrisAgent other = (TetrisAgent) obj;
            if (this.fitness > other.fitness) {
                return 1;
            } else if (this.fitness < other.fitness) {
                return -1;
            }
        }
        return 0;
    }

    /**
     * cria um novo agente e seta seus genes com valores randômicos entre 0 e 1.
     *
     * @return
     */
    public static TetrisAgent randomAgent() {
        TetrisAgent agent = new TetrisAgent();
        Random random = new Random();

        for (int i = 0; i < agent.genes.length; i++) {
            agent.genes[i] = random.nextFloat();
        }

        return agent;
    }

    /**
     *
     * @return novo objeto com os mesmos genes do agente que invoca o método
     */
    public TetrisAgent cloneAgent() {
        TetrisAgent cloneAgent = new TetrisAgent();

        System.arraycopy(this.genes, 0, cloneAgent.genes, 0, this.genes.length);

        return cloneAgent;
    }
}
