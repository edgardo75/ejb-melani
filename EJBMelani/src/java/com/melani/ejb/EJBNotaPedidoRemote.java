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

    long cancelarNotaPedido(long idnota,int idusuariocancelo,int estado);

    long entregarNotaPedido(long idnota, int idusuario,int estado);

    String selectNotaEntreFechas(String fecha1, String fecha2,int idvendedor);

    int getRecorCountNotas();

    String selectAllNotas();

    String verNotasPedidoPaginadas(int index, int recordCount);

    long anularNotaPedido(long idnota, long idusuario, int estado);

    long actualizarNotaPedido(String xmlnotapedidomodificada);

    String selecNotaEntreFechasEntrega(String fecha1, String fecha2, int idvendedor);

 

   
    
}
