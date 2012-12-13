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
public interface EJBEmpleadosRemote {

    

    long addEmpleadoFullTime(String apellido,String nameuser, String nombre, String password, String passwordre, String email, short idtipoDocu, int numerodocu, String salario,short idGenero,String Observaciones);

    long addEmpleadoParttime(String apellido,String nameuser, String nombre, String password, String paswordre,String email, short idtipo, int numerodocu, float salarioxhora,short idGenero,String Observaciones);

    String selectAllEmpleados();

    int deshabilitarEmpleado(int idEmpleado, int idEmpleadoDesabilito);
    
}
