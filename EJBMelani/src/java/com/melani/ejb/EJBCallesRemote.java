/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.melani.ejb;



import com.melani.entity.Calles;

import java.util.ArrayList;
import javax.ejb.Remote;

/**
 *
 * @author Edgardo
 */
@Remote
public interface EJBCallesRemote {

    long addCalles(String descripcion,int idUsuario);

    String searchAllCalles();

    Object recorCountCalles();

   // String get_ItemCalles_Paging1(int indice, int cantidadRegistros);

    Calles[] calles_paging(Integer startIndex, Integer numItems);

    Calles[] paginadocalles(int uno, int dos);

    ArrayList    pagingStreet(int page, int recordSize);
    
}
