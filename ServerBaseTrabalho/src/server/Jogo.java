/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package server;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 *
 * @author elder
 */
public class Jogo {
    
    Jogador p1;
    Jogador p2;
    int pontosp1;
    int pontosp2;
    boolean turn = true;
    Carta deckp1[] = new Carta[3];
    Carta deckp2[] = new Carta[3];
    Carta cartas[] = new Carta[6];
    
    public Jogo(Jogador p1, Jogador p2) {
        this.p1 = p1;
        this.p2 = p2;
        int cont = 0;
        darCartas();
        System.out.println("DECK PLAYER 1: \n");
        for (int i = 0; i < 3; i++) {
            System.out.println(deckp1[i].getNome());
        }

        System.out.println("DECK PLAYER 2: \n");
        for (int i = 0; i < 3; i++) {
            System.out.println(deckp2[i].getNome());
        }
    }

//
//    
//    public void mesa() {
//    
//         darCartas();
//        System.out.println("DECK PLAYER 1: \n");
//        for (int i = 0; i < 3; i++) {
//            System.out.println(deckp1[i].getNome());
//        }
//
//        System.out.println("DECK PLAYER 2: \n");
//        for (int i = 0; i < 3; i++) {
//            System.out.println(deckp2[i].getNome());
//        }
//
//        for (int i = 0; i < 3; i++) {
//
//            if (turn == true) {
//                JOptionPane.showMessageDialog(null, "Carta player one: \n" + deckp1[i].getNome() + " \n" + deckp1[i].getSkill1() + " \n" + deckp1[i].getSkill2() + "\n" + deckp1[i].getElemento());
//                String[] options = {"atk1", "atk2"};
//
//                int x = JOptionPane.showOptionDialog(null, "Escolha o seu ataque",
//                        "Click a button",
//                        JOptionPane.DEFAULT_OPTION, JOptionPane.INFORMATION_MESSAGE, null, options, options[0]);
//
//                if (deckp1[i].getElemento().equals("agua") && deckp2[i].getElemento().equals("fogo")) {
//                    deckp1[i].setSkill1(deckp1[i].getSkill1() + 100);
//                    deckp1[i].setSkill2(deckp1[i].getSkill2() + 100);
//                }
//
//                if (deckp2[i].getElemento().equals("agua") && deckp1[i].getElemento().equals("fogo")) {
//                    deckp2[i].setSkill1(deckp2[i].getSkill1() + 100);
//                    deckp2[i].setSkill2(deckp2[i].getSkill2() + 100);
//                }
//
//                if (x == 0) {
//
//                    if (deckp1[i].getSkill1() > deckp2[i].getSkill1()) {
//                        pontosp1++;
//                        System.out.println("Vitoria player one");
//                    } else {
//                        pontosp2++;
//                        System.out.println("Vitoria player two");
//                        turn = false;
//                    }
//                }
//
//                if (x == 1) {
//
//                    if (deckp1[i].getSkill2() > deckp2[i].getSkill2()) {
//                        pontosp1++;
//                        System.out.println("Vitoria player one");
//                    } else {
//                        pontosp2++;
//                        System.out.println("Vitoria player two");
//                        turn = false;
//                    }
//                }
//            } else {
//                JOptionPane.showMessageDialog(null, "Carta player two: \n" + deckp2[i].getNome() + " \n" + deckp2[i].getSkill1() + " \n" + deckp2[i].getSkill2() + "\n" + deckp2[i].getElemento());
//                String[] options = {"atk1", "atk2"};
//
//                int x = JOptionPane.showOptionDialog(null, "Escolha o seu ataque",
//                        "Click a button",
//                        JOptionPane.DEFAULT_OPTION, JOptionPane.INFORMATION_MESSAGE, null, options, options[0]);
//
//                if (deckp2[i].getElemento().equals("agua") && deckp1[i].getElemento().equals("fogo")) {
//                    deckp2[i].setSkill1(deckp2[i].getSkill1() + 100);
//                    deckp2[i].setSkill2(deckp2[i].getSkill2() + 100);
//                }
//
//                if (deckp1[i].getElemento().equals("agua") && deckp2[i].getElemento().equals("fogo")) {
//                    deckp1[i].setSkill1(deckp1[i].getSkill1() + 100);
//                    deckp1[i].setSkill2(deckp1[i].getSkill2() + 100);
//                }
//
//                if (x == 0) {
//
//                    if (deckp2[i].getSkill1() > deckp1[i].getSkill1()) {
//                        pontosp2++;
//                        System.out.println("Vitoria player two");
//                    } else {
//                        pontosp1++;
//                        System.out.println("Vitoria player one");
//                        turn = true;
//                    }
//                }
//
//                if (x == 1) {
//
//                    if (deckp2[i].getSkill2() > deckp1[i].getSkill2()) {
//                        pontosp2++;
//                        System.out.println("Vitoria player two");
//                    } else {
//                        pontosp1++;
//                        System.out.println("Vitoria player one");
//                        turn = true;
//                    }
//                }
//            }
//
//        }
//        if (pontosp1 > pontosp2) {
//            JOptionPane.showMessageDialog(null, "VITÓRIA DO PLAYER ONE");
//            
//            
//
//        } else {
//            JOptionPane.showMessageDialog(null, "VITÓRIA DO PLAYER ONE");
//        }
//        
//    }
//    
    public void darCartas() {
        carregarCartas();
        for (int i = 0; i < 3; i++) {
            deckp1[i] = cartas[i];
            deckp2[i] = cartas[i + 3];
        }

    }
    
    public void carregarCartas() {
        Path caminho = Paths.get("C:\\Users\\20171pf.cc0224\\Desktop\\cartas.txt");

        try {

            byte[] texto = Files.readAllBytes(caminho);
            String leitura = new String(texto);

            String leituraCartas[] = leitura.split("-");
            String carta = new String();

            for (int i = 0; i < leituraCartas.length; i++) {
                carta = leituraCartas[i];
                String lei[] = carta.split(";");

                Carta c = new Carta();
                c.setNome(lei[0]);
                c.setSkill1(Integer.parseInt(lei[1]));
                c.setSkill2(Integer.parseInt(lei[2]));
                c.setElemento(lei[3]);
                cartas[i] = c;

            }
        } catch (Exception e) {
            System.out.println("ERRO");
        }

    }
}
