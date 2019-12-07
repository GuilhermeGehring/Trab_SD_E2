/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package server;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import util.Estados;
import util.Mensagem;
import util.Status;

/**
 *
 * @author elder
 */
class Jogador implements Runnable {

    Socket socket;
    Server server;
    ObjectOutputStream output;
    ObjectInputStream input;
    int id;
    Jogo jogo;
    Estados estado;

    public Jogador(Socket socket, Server server, int id) {
        this.socket = socket;
        this.server = server;
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Override
    public void run() {
        //trataConexão
        trataConexão(socket);
    }

    protected void enviaMsgAoCliente(Mensagem m) throws IOException {
        output.writeUTF(m.toString());
        output.flush();
    }

    private void trataConexão(Socket socket) {
        //tratamento da comunicação com um cliente (socket)

        try {
            output = new ObjectOutputStream(socket.getOutputStream());
            input = new ObjectInputStream(socket.getInputStream());
            output.flush();
            System.out.println("Conexao recebida, inciando protocolo...");
            //iniciar a conversa --- SINCRONO
            String msgResposta = "";
            String operacao = "";
            //Armazena o estado da comunicação com o cliente
            estado = Estados.CONECTADO;

            boolean primeira = true;
            //event loop
            do {
                //leitura
                String msgCliente = input.readUTF(); //bloqueante
                String response = "";
                System.out.println("Mensagem recebida do cliente: " + msgCliente);
                //escrita

                String[] protocolo = msgCliente.split(";");
                operacao = protocolo[0];
                Mensagem resposta = new Mensagem(operacao.toUpperCase() + "RESPONSE");

                switch (estado) {
                    case CONECTADO:
                        switch (operacao) {
                            case "AVISATODOS":

                                try {
                                    Mensagem broadcast = new Mensagem("AVISO");
                                    Mensagem msg = Mensagem.parseString(msgCliente);

                                    broadcast.setStatus(Status.OK);
                                    broadcast.setParam("msg", msg.getParam("msg"));

                                    resposta.setStatus(Status.OK);

                                    server.broadcast(msg, this);

                                } catch (Exception e) {
                                    resposta.setStatus(Status.ERROR);
                                    resposta.setParam("msg", "Erro ao enviar broadcast");
                                }

                                break;

                            case "OI":
                                try {
                                    Mensagem mCliente = Mensagem.parseString(msgCliente);
                                    if (mCliente != null) {
                                        String nome = mCliente.getParam("nome");
                                        resposta = new Mensagem("OIRESPONSE");
                                        resposta.setStatus(Status.OK);
                                        resposta.setParam("mensagem", "Oi " + nome + ", Bem-vindo!");
                                    }
                                } catch (Exception e) {
                                }

                            break;
                            case "LOGIN":
                                try {

                                    Mensagem mCliente = Mensagem.parseString(msgCliente);
                                    if (mCliente != null) {
                                        String senha = mCliente.getParam("senha");
                                        if (senha.equals("123")) {
                                            resposta = new Mensagem("LOGINRESPONSE");
                                            resposta.setStatus(Status.OK);
                                            resposta.setParam("mensagem", "A senha " + senha + " foi logada com sucesso");
                                            estado = Estados.AUTENTICADO;
                                        }
                                    }

                                    //responde ao cliente
                                } catch (Exception e) {
                                    resposta.setStatus(Status.ERROR);
                                    resposta.setParam("error", "Mensagem Inválida ou não autorizada!");
                                }
                            break;    
                            default:
                                //mensagem inválida
                                resposta.setStatus(Status.ERROR);
                                resposta.setParam("error", "Mensagem Inválida ou não autorizada!");
                            break;
                        }
                        break;
                    case AUTENTICADO:
                        switch (operacao) {
                            //tratamento somente das mensagens possíveis no estado AUTENTICADO
                            case "INVITEONE":
                                /*
                                 CONVIDAR
                                 id:int
                                 */
                                try {
                                    Mensagem m = Mensagem.parseString(msgCliente);
                                    int idAdv = Integer.parseInt(m.getParam("id"));
                                    //notifica convite ao convidado
                                    Jogador guest = server.getClienteById(idAdv);
                                    /*
                                     CONVITE
                                     id:int //id de qm convidou
                                     */
                                    Mensagem convite = new Mensagem("INVITE");
                                    convite.setParam("id", "" + id);
                                    guest.enviaMsgAoCliente(convite);

                                    resposta.setStatus(Status.OK);
                                    estado = Estados.ESPERANDO;
                                    //timeout
                                    //responder mensagem do "convidador"
                                } catch (Exception e) {
                                    resposta.setStatus(Status.ERROR);
                                    resposta.setParam("error", "Mensagem Inválida ou não autorizada!");
                                }
                                break;
                            case "INVITEALL":
                                try {
                                    resposta.setStatus(Status.OK);
                                    //criar convite
                                    Mensagem convite = new Mensagem("INVITE");
                                    convite.setStatus(Status.OK);
                                    convite.setParam("id", id + "");
                                    estado = Estados.ESPERANDO;
                                    server.convidaTodos(convite, this);
                                } catch (Exception e) {
                                    //tratamento de erros
                                        resposta.setStatus(Status.ERROR);
                                        resposta.setParam("Erro", e.getMessage());
                                }
                                break;
                            case "ACEITAR":
                                try{
                                    resposta.setStatus(Status.OK);
                                    Mensagem mCliente = Mensagem.parseString(msgCliente);

                                    if (mCliente != null) {
                                        String idParm = mCliente.getParam("id");
                                        
                                        resposta.setStatus(Status.OK);
                                        
                                        //seta o estado do jogador que aceitou o convite para jogando
                                        Jogador jogador2 = server.getJogadorById(Integer.parseInt(idParm));
                                        jogador2.estado = estado.JOGANDO;
                                        estado = Estados.JOGANDO;
                                        jogador2.output.writeUTF("INVITERESPONSE;OK;msg:Partida aceita por jogador " + id);
                                        jogador2.output.flush();
                                        
                                    }

                                } catch (Exception e) {
                                    resposta.setStatus(Status.ERROR);
                                    resposta.setParam("erro", e.getMessage());
                                }
                                break;
                            case "CHECK":
                                //validando protocolo (parse)
                                try {
                                    //tratamento da mensagem

                                } catch (Exception e) {

                                }
                                break;
                            //exemplo com troca de estados
                            case "LOGOUT":
                                estado = Estados.CONECTADO;
                                resposta.setStatus(Status.OK);
                                break;
                            default:
                                //mensagem inválida
                                resposta.setStatus(Status.ERROR);
                                resposta.setParam("error", "Mensagem Inválida ou não autorizada!");
                                break;
                        }
                        break;
                    case JOGANDO:
                        System.out.println("Jogador " + id + " está jogando");
                        break;
                }
                //enviar a resposta ao cliente
                //output.writeUTF(response);
                output.writeUTF(resposta.toString());
                output.flush();
            } while (!operacao.equals("pare"));
        } catch (Exception e) {
            System.out.println("Erro no loop de tratamento do cliente: " + socket.getInetAddress().getHostAddress());
        } finally {
            try {
                //fechar as conexões
                output.close();
                input.close();
                socket.close();
            } catch (IOException ex) {
                System.out.println("Erro normal ao fechar conexão do cliente..." + ex.getMessage());
            }

        }

    }

}
