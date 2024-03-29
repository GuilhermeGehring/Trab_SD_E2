/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cliente;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import util.Mensagem;

/**
 *
 * @author elder
 */
public class MessageHelper<T extends Mensagem> implements Runnable, Subject {

    Socket socket;
    ObjectInputStream input;
    ArrayList< Observer<Mensagem> > observers;
    T mensagem;
    
    public MessageHelper(Socket socket) {
        this.socket = socket;
        this.input = null;
        this.observers = new ArrayList<>();
    }

    @Override
    public void run() {
        try {
            input = new ObjectInputStream(socket.getInputStream());
            
        } catch (IOException ex) {
            Logger.getLogger(MessageHelper.class.getName()).log(Level.SEVERE, null, ex);
        }
        //System.out.println("Thread esperando mensagens...");
        String msg;
        try {
            while (true) {
                msg = input.readUTF();
                Mensagem m = Mensagem.clientParseString(msg);
                for (Observer<Mensagem> o : observers) {
                    o.update(m);
                }
            }
        } catch (IOException ex) {
            Logger.getLogger(Client.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public void subscribe(Observer o) {
        observers.add( o );
    }

}
