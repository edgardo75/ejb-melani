/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.melani.ejb;

import javax.ejb.Remote;

/**
 *
 * @author Edgardo
 */
@Remote
public interface EJBNotaPedidoRemote {

    long agregarNotaPedido(String xmlNotaPedido);

    String selectUnaNota(long idnta);

    long modificarSaldoNota(long idnota, double saldo, int idusuario);

    long cancelarNotaPedido(long idnota,int idusuariocancelo);

    long entregarNotaPedido(long idnota, int idusuario);

   
    
}
