/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import util.Estados;
import util.Mensagem;

/**
 *
 * @author elder
 */
public class Server {

    private ServerSocket serverSocket;
    int porta;
    ArrayList<Thread> threads;
    ArrayList<Jogador> jogadores;
    Map<Integer, Integer> ranking;

    public Server() {
        threads = new ArrayList<>();
        jogadores = new ArrayList<>();
        ranking = new HashMap<>();
    }

    protected void avisaServer(String msg) {
        System.out.println("Cliente avisou: " + msg);
    }
    
    public void setRanking(Integer chave, Integer valor) {
        ranking.put(chave, valor);
    }

    public Integer getRanking(Integer chave) {
        return ranking.get(chave);
    }

    private void init() throws IOException {
 
        
        
        try {
            serverSocket = criarServerSocket(5555);
            //2 -Esperar o um pedido de conexÃ£o;
            int id=0;
            do {

                System.out.println("Esperando conexao...");
                Socket socket = esperaConexao(); //bloqueante
                //3 - Criar streams de enechar socket de comunicaÃ§Ã£o entre servidor/cliente
                //Criar outra thread para tratar cliente novo
                
                Jogador tarefa = new Jogador(socket, this, id++);
                setRanking(id-1, 0);
                jogadores.add(tarefa);
                Thread thread = new Thread(tarefa);
                threads.add(thread);
                thread.start();

                System.out.println("Conexão com cliente estabelecida.");

            } while (true);
        } catch (Exception e) {
            System.out.println("Erro no event loop do main(): " + e.getMessage());
            serverSocket.close();
        }
    }
    
    
    protected Jogador getClienteById(int id){
        for (Jogador j : jogadores) {
            if( j.getId() == id )
                return j;
        }
        return null;
    }

    public static void main(String[] args) {
        Server server = new Server();

        try {
            //1 - Criar o servidor de conexÃµes            
            server.init();

        } catch (Exception e) {
            e.printStackTrace();
            //System.out.println("Erro na main do ServerSocket " + e.);
            System.exit(0);
        }
    }

    private ServerSocket criarServerSocket(int porta) {
        try {
            this.serverSocket = new ServerSocket(porta);
        } catch (Exception e) {
            System.out.println("Erro na Criação do server Socket " + e.getMessage());
        }

        return serverSocket;
    }

    private Socket esperaConexao() {
        try {
            return serverSocket.accept();
        } catch (IOException ex) {
            System.out.println("Erro ao criar socket do cliente " + ex.getMessage());
            return null;
        }
    }

    /*Broadcast*/
    protected void broadcast(Mensagem m, Jogador emissor) throws IOException {
        if (emissor == null) {
            //manda para todos
            for (Jogador t : jogadores) {
                t.enviaMsgAoCliente(m);
            }
        } else {
            //manda pra todos, menos pro processo da tarefa
            for (Jogador t : jogadores) {
                if (t != emissor) {
                    t.enviaMsgAoCliente(m);
                }
            }
        }
    }
    
    protected void convidaTodos(Mensagem m, Jogador emissor) throws IOException {
        if (emissor == null) {
            //manda para todos
            for (Jogador t : jogadores) {
                t.enviaMsgAoCliente(m);
            }
        } else {
            //manda pra todos, menos pro processo da tarefa
            for (Jogador t : jogadores) {
                if (t != emissor && t.estado == Estados.AUTENTICADO) {
                    t.enviaMsgAoCliente(m);
                }
            }
        }
    }
    protected Jogador getJogadorById(int id)
    {
        for (Jogador t : jogadores)
            if (t.getId() == id)
                return t;
        
        return null;
    }

}
