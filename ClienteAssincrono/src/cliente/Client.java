/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cliente;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.Scanner;
import javax.imageio.IIOException;
import util.Mensagem;

/**
 *
 * @author elder
 */
public class Client implements Observer {

    private String nome;

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    //socket para comunicar com servidor
    private Socket socket;

    public Client(String nome) {
        this.nome = nome;
    }

    private Socket conectaComServidor(String host, int porta) throws IOException {
        try {
            this.socket = new Socket(host, porta);
            return socket;
        } catch (IOException ex) {
            System.out.println("Erro ao conectar com o servidor: " + ex.getMessage());
            throw new IIOException(" Ocorreu um erro ao criar o socket: " + ex.getMessage());
        }
    }

    @Override
    public void update(Mensagem content) {
        System.out.println(content.toString());
        switch (content.getOperacao()) {
            case "OI":
                break;
            case "LOGINRESPONSE":
                break;
        }
    }

    /**
     * @param args the com mand line arguments
     */
    public static void main(String[] args) {
        try {
            Scanner leitura = new Scanner(System.in);
            Client cliente = new Client("Guilherme");
            //conexao com server
            Socket socket = cliente.conectaComServidor("localhost", 5555);
            //streams de saida e entrada
            ObjectOutputStream output = new ObjectOutputStream(socket.getOutputStream());
            output.flush();
            String msg = "";
            MessageHelper<Mensagem> helper = new MessageHelper<>(socket);
            Thread th = new Thread(helper);
            helper.subscribe(cliente);
            th.start();

            //event loop
            do {
                //protocolo
                //envio
                System.out.print("Texto do cliente: ");
                String texto = leitura.nextLine();
                //cliente para estressar servidor

                output.writeUTF(texto);
                output.flush();
                //recebimento

            } while (!msg.equals("pare"));
            //fecha streams e conexão
            output.close();
            socket.close();
        } catch (Exception e) {
            System.out.println("Erro na comunicação: " + e.getLocalizedMessage());
        }
    }

}
