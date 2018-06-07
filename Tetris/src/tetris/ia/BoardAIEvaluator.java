/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tetris.ia;

/**
 * Define metodos estaticos utilizados pelo agente AI para avaliar determinada
 * configuracao da grade.
 *
 * @author patri
 */
public class BoardAIEvaluator {

    /**
     *
     * @param board Retorna o numero de linhas removidas
     * @return
     */
    public static int clearedRows(int[][] board) {

        int cleared = 0;
        int columns = board[0].length;

        for (int i = 0; i < board.length - 1; i++) {
            boolean ok = true;
            for (int j = 1; j < columns - 1 && ok; j++) {
                if (board[i][j] == 0) {
                    ok = false;
                }
            }

            if (ok) {
                cleared++;
            }

        }
        return cleared;
    }

    /**
     *
     * @param board Retorna a altura max
     * @return
     */
    public static int pileHeight(int[][] board) {

        int columns = board[0].length;
        for (int i = 0; i < board.length - 1; i++) {
            for (int j = 1; j < columns - 1; j++) {
                if (board[i][j] != 0) {
                    return board.length - 1 - j;
                    
                }
            }

        }

        return 0;
    }

    /**
     *
     * @param board Retorna numero de buracos
     * @return
     */
    public static int countSingleHoles(int[][] board) {

        int holes = 0;
        int columns = board[0].length;

        for (int j = 1; j < columns - 1; j++) {
            boolean swap = false;

            for (int i = 0; i < board.length; i++) {
                if (board[i][j] != 0) {
                    swap = true;
                  
                } else if (swap) {
                    holes++;
                }
            }

        }
        return holes;
    }

    /**
     *
     * @param board Retorna o numero de buracos conectados
     * @return
     */
    public static int countConnectedHoles(int[][] board) {
        int holes = 0;
        int columns = board[0].length;

        for (int j = 1; j < columns - 1; j++) {
            boolean swap = false;
            for (int i = 0; i < board.length - 1; i++) {
                if (board[i][j] != 0) {
                    swap = true;
                } else {
                    if (swap) {
                        holes++;
                    }
                    swap = false;
                }
            }
        }
        return holes;

    }

    /**
     * 
     * @param board
     * Retorna a quantidade de blocos acima de buracos
     * @return 
     */
    public static int blocksAboveHoles(int[][] board) {
        int blocks = 0;
        int cols = board[0].length;

        for (int c = 1; c < cols - 1; c++) {
            boolean swap = false;
            for (int r = board.length - 2; r >= 0; r--) {
                if (board[r][c] == 0) {
                    swap = true;
                } else {
                    if (swap) {
                        blocks++;
                    }
                }
            }
        }
        return blocks;
    }

    /**
     * 
     * @param board
     * Retorna o número de poços, regiões onde somente a peça do tipo I pode ser 
     * encaixada sem deixar buracos. 
     * @return 
     */
    public static int countWells(int[][] board) {
        int cols = board[0].length;
        int wells = 0;

        //da segunda até a  penultima coluna
        for (int col = 2; col < cols - 2; col++) {
            for (int row = 0; row < board.length - 1; row++) {
                if ((board[row][col - 1] > 0) && (board[row][col + 1] > 0) && (board[row][col] == 0)) {
                    wells++;
                } else if (board[row][col] > 0) {
                    break;
                }
            }
        }

        //primeira coluna
        for (int row = 0; row < board.length - 1; row++) {
            if ((board[row][1] == 0) && (board[row][2] > 0)) {
                wells++;
            } else if (board[row][1] > 0) {
                break;
            }
        }
        //ultima coluna
        for (int row = 0; row < board.length - 1; row++) {
            if ((board[row][cols - 3] > 0) && (board[row][cols - 2] == 0)) {
                wells++;
            } else if (board[row][cols - 2] > 0) {
                break;
            }
        }
        return wells;
    }
    

    /**
     * Calcula a nivelação da grade,
     * observando as diferenças de altura entre colunas adjacentes
     * @param board
     * @return 
     */
    public static float bumpiness(int[][] board) {
        int cols = board[0].length;
        int[] heights = new int[cols - 2];

        for (int x = 1; x < cols - 1; x++) {
            for (int y = 0; y < board.length - 1; y++) {
                if (board[y][x] != 0) {
                    heights[x - 1] = board.length - 1 - y;
                    break;
                }
            }
        }

        float bmp = 0.0f;
        for (int i = 0; i < cols - 3; i++) {
            float diff = Math.abs(heights[i] - heights[i + 1]);
            bmp += diff;
        }

        return bmp;
    }

}
