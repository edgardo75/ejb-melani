/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.melani.ejb;
import com.melani.entity.Barrios;
import javax.ejb.Remote;
/**
 *
 * @author Edgardo
 */
@Remote
public interface EJBBarriosRemote {
    long addBarrio(String descripcion,int idUsuario);
    String searchAllBarrios();
    int recordCountBarrios();
    String obtenrItemsPaginados(int indiceInicio, int numeroItems);
    Barrios[] barrios_Paging(int startindex, int numitems);
    String selectAllBarrios();
}
