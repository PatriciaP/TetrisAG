/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tetris.piece;

/**
 *
 * @author Patricia Pieroni
 */
public class PieceTranslation {

    /*
    Há sete  tipos  de  peças,  que  são  todas  as  combinações  possíveis  de  4  
    blocos ortogonalmente  adjacentes. Atribui-se os nomes  das  peças,
    pelas suas formas: O, I, S, Z, L, J, T 
     */

 /*Posicionamento inicial, e indica o offset (medido em blockSize) 
    necessário para centralizar a peça, usa type e rotation como indices de acesso.*/
    public static final int[][][] TRANSLATIONS
            = {
                /* T */
                {
                    {-2, -3},
                    {-2, -3},
                    {-2, -3},
                    {-2, -2}
                },
                /* I */
                {
                    {-2, -2},
                    {-2, -4},
                    {-2, -2},
                    {-2, -3}
                },
                /* S  */
                {
                    {-2, -3},
                    {-2, -3},
                    {-2, -3},
                    {-2, -2}
                },
                /* Z */
                {
                    {-2, -3},
                    {-2, -3},
                    {-2, -3},
                    {-2, -2}
                },
                /* O */
                {
                    {-2, -3},
                    {-2, -3},
                    {-2, -3},
                    {-2, -3}
                },
                /* J */
                {
                    {-2, -3},
                    {-2, -2},
                    {-2, -3},
                    {-2, -3}
                },
                /* L */
                {
                    {-2, -3},
                    {-2, -3},
                    {-2, -3},
                    {-2, -2}
                }

            };

}
