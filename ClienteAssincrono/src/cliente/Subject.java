/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cliente;

import util.Mensagem;

/**
 *
 * @author elder
 */
public interface Subject<T extends Mensagem> {
    public void subscribe( Observer<T> o );
    
}
