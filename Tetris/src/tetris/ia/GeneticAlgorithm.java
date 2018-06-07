/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tetris.ia;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;
import java.util.concurrent.CountDownLatch;
import tetris.GamePanel;

/**
 * população randômica de k agentes, cada um será testado em uma série de n
 * games aleatórios; A cada geração, os agentes de maior desempenho terão uma
 * probabilidade maior de serem selecionados para reprodução, o que dará origem
 * aos membros da próxima geração, e assim sucessivamente.
 *
 * @author patri
 */
public class GeneticAlgorithm {

    public static void main(String[] args) {
        int popSize = 20;
        int maxGens = 30;
        float probMutate = 0.2f;
        int gamesPerAgent = 10;
        float eliteRate = 0.2f;

        ArrayList<TetrisAgent[]> hist = runGA(popSize, maxGens, probMutate, gamesPerAgent, eliteRate);
        GeneticAlgorithm.formatResults(hist, "outputGA.csv");
    }

    /**
     * Crossover: combina os genes dos pais em uma nova variavel. Escolhe um
     * ponto de corte aleatorio e a partir do ponto, combina os genes dos pais
     * em um novo valor
     *
     * @param father
     * @param mother
     * @return
     */
    public static TetrisAgent[] blendCrossover(TetrisAgent father, TetrisAgent mother) {
        TetrisAgent copy1 = father.cloneAgent();
        TetrisAgent copy2 = mother.cloneAgent();

        Random random = new Random();
        int N_GENES = father.genes.length;
        int index = random.nextInt(N_GENES);

        for (int i = index; i < N_GENES; i++) {
            float beta = random.nextFloat();
            float var1 = father.genes[i];
            float var2 = mother.genes[i];

            float newVar1 = beta * var1 + (1 - beta) * var2;
            float newVar2 = (1 - beta) * var1 + beta * var2;

            copy1.genes[i] = newVar1;
            copy2.genes[i] = newVar2;
        }

        return new TetrisAgent[]{copy1, copy2};
    }

    /**
     * Ao selecionar e reproduzir os individuos repetidamente, certas
     * combinacoes de genes sao impossiveis, congelando o espaco de busca,
     * fazendo com que a populacao possa convergiar para copias de mesmo
     * individuo; O metodo percorre os genes e introduz ou nao uma pequena
     * variacao.
     *
     * @param agent Esse parametro referencia a probabilidade de um gene sofrer
     * mutacao
     * @param probMutate
     */
    public static void mutateNoise(TetrisAgent agent, float probMutate) {
        int N_GENES = agent.genes.length;
        Random random = new Random();

        for (int i = 0; i < N_GENES; i++) {
            float sort = random.nextFloat();
            if (sort < probMutate) {

                float prob = random.nextFloat();
                if (prob < 0.5f) {
                    float genValue = agent.genes[i];
                    float complement = 1.0f - genValue;
                    float noise = random.nextFloat() * complement;
                    agent.genes[i] += noise;

                } else {
                    float genValue = agent.genes[i];
                    float noise = random.nextFloat() * genValue;
                    agent.genes[i] -= noise;
                }
            }
        }
    }

    /**
     * Agentes com melhor desempenho terao mais chances de serem selecionados
     * para reproducao; Este metodo utiliza o metodo da roleta para selecionar e
     * retornar um agente OBS: valores de fitness nao podem ser negativos
     *
     * @param population
     * @return
     */
    public static TetrisAgent doSelection(TetrisAgent[] population) {
        TetrisAgent sorted = null;

        float totalFitness = 0.0f;
        for (TetrisAgent agent : population) {
            totalFitness += agent.fitness;
        }

        float frac = new Random().nextFloat();
        float cut = frac * totalFitness;

        for (TetrisAgent agent : population) {
            cut -= agent.fitness;
            if (cut <= 0.0f) {
                sorted = agent;
                break;
            }
        }
        return sorted;
    }

    /**
     * Numero de agentes em cada geracao
     *
     * @param popSize Numero max de geracoes
     * @param maxGens Probabilidade de ocorrer mutacao em um gene
     * @param probMutate Numero de jogos por agente
     * @param gamesPerAgent Taxa de elitismo
     * @param eliteRate
     * @return
     */
    public static ArrayList<TetrisAgent[]> runGA(int popSize, int maxGens, float probMutate, int gamesPerAgent, float eliteRate) {

        // cada item representa uma geracao de agentes
        ArrayList<TetrisAgent[]> hist = new ArrayList<>();

        //Forca popsize ser par, para evitar problemas se houver elitismo
        if (popSize % 2 == 1) {
            popSize += 1;
        }

        int elite = (int) (popSize * eliteRate);

        //Forca elite ser par, para evitar problemas se houver elitismo
        if (elite % 2 == 1) {
            elite += 1;
        }

        int currentGeneration = 0;

        Random random = new Random();
        TetrisAgent[] population = new TetrisAgent[popSize];
        TetrisAgent bestAgent = null;
        int rows = 20;
        int cols = 10;
        int blockSize = 1; //qualquer valor diferente de zero

        //cria populacao inicial e inicia fitness
        for (int i = 0; i < population.length; i++) {
            population[i] = TetrisAgent.randomAgent();
        }

        //inteiro ponto de partida sorteado  para semear o gerador aleatorio
        int seed = random.nextInt();

        do {
            CountDownLatch doneSignal = new CountDownLatch(popSize * gamesPerAgent);

            //Para cada agente inicia um jogo passando os parametros necessarios
            for (TetrisAgent agent : population) {
                for (int i = 0; i < gamesPerAgent; i++) {
                    GameAI game = new GameAI(rows, cols, blockSize, i + seed, doneSignal);
                    game.agent = agent;
                    new Thread(game).start();
                }
            }

            //espera todas as threads dos games terminarem
            try {
                doneSignal.await();
            } catch (InterruptedException ex) {
            }

            //calcula fitness
            for (TetrisAgent agent : population) {
                float average = 0.0f;
                //calcula a media de linhas removidas por cada agente e atualiza o fitness e melhor agente
                for (Integer cleared : agent.clearedRowsArray) {
                    average += cleared;
                }
                agent.fitness = 1.0f * average / gamesPerAgent;
                agent.clearedRowsArray.clear();

            }


            //atualiza bestAgent
            for (TetrisAgent agent : population) {
                if (bestAgent == null || agent.fitness > bestAgent.fitness) {
                    bestAgent = agent;
                }
            }

            //array da nova populacao
            TetrisAgent[] newPopulation = new TetrisAgent[popSize];
            //alocados de acordo com o desempenho de fitness
            java.util.Arrays.sort(population);

            //seleciona a elite, que fara parte da nova populacao
            TetrisAgent[] eliteArray = new TetrisAgent[elite];
            for (int i = 0; i < elite; i++) {
                newPopulation[i] = population[popSize - i - 1];
            }

            //restante da populacao preenchido
            int newIndex = elite;
            for (int i = 0; i < (popSize - elite) / 2; i++) {
                TetrisAgent parentA = doSelection(population);
                TetrisAgent parentB = doSelection(population);

                //crossover
                TetrisAgent[] children = blendCrossover(parentA, parentB);
                TetrisAgent childA = children[0];
                TetrisAgent childB = children[1];

                //mutacao
                mutateNoise(childA, probMutate);
                mutateNoise(childB, probMutate);

                //adiciona os filhos a nova populacao
                newPopulation[newIndex] = childA;
                newIndex++;
                newPopulation[newIndex] = childB;
                newIndex++;
            }

            hist.add(population);
            population = newPopulation;
            currentGeneration++;

            System.out.println("Melhor da geracao " + currentGeneration + ": " + bestAgent.fitness);
            for (int i = 0; i < bestAgent.genes.length; i++) {
                System.out.print(bestAgent.genes[i] + "  ");
            }
            System.out.println();

        } while (currentGeneration < maxGens);

        return hist;
    }

    /* formata output em .csv */
    public static void formatResults(ArrayList<TetrisAgent[]> generations, String file) {
        BufferedWriter writer = null;
        try {
            writer = new BufferedWriter(new FileWriter(file, false));
        } catch (IOException ex) {
        }

        int gens = generations.size();

        //header
        for (int i = 0; i < gens; i++) {
            try {
                if (i == gens - 1) {
                    writer.write("g" + i + "\n");
                } else {
                    writer.write("g" + i + ",");
                }
            } catch (IOException ex) {
            }
        }

        int pop = generations.get(0).length;
        for (int indiv = 0; indiv < pop; indiv++) {
            //dados
            for (int i = 0; i < gens; i++) {
                TetrisAgent agent = generations.get(i)[indiv];
                float fitness = agent.fitness;

                try {
                    if (i == gens - 1) {
                        writer.write(fitness + "\n");
                    } else {
                        writer.write(fitness + ",");
                    }
                } catch (IOException ex) {
                }
            }
        }
        try {
            writer.close();
        } catch (IOException ex) {
        }
    }
}
