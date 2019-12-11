/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package server;

/**
 *
 * @author 20171pf.cc0178
 */
public class Carta {
    private String nome;
    private String elemento;
    private int skill1;
    private int skill2;

    public Carta(String nome, String elemento, int skill1, int skill2) {
        this.nome = nome;
        this.elemento = elemento;
        this.skill1 = skill1;
        this.skill2 = skill2;
    }

    public Carta() {
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getElemento() {
        return elemento;
    }

    public void setElemento(String elemento) {
        this.elemento = elemento;
    }

    public int getSkill1() {
        return skill1;
    }

    public void setSkill1(int skill1) {
        this.skill1 = skill1;
    }

    public int getSkill2() {
        return skill2;
    }

    public void setSkill2(int skill2) {
        this.skill2 = skill2;
    }
    
    @Override
    public String toString() {
        String s = "Carta :" + nome + "\n";
        s+= "elemento: "+ elemento+"\n";
        s+= "Ataque 1: " + skill1+ "\n";
        s+= "Ataque 2: " + skill2 +"\n";
        
        return s;
    }

}
