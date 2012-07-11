/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.melani.ejb;

import com.melani.entity.EmpleadoParttime;
import com.melani.entity.FullTimeEmpleado;
import com.melani.entity.Generos;
import com.melani.entity.Tiposdocumento;
import java.math.BigDecimal;
import javax.ejb.Stateless;
import javax.jws.WebService;
import javax.jws.soap.SOAPBinding;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 *
 * @author Edgardo
 */
@Stateless(name="ejb/EJBEmpleados")
@WebService(serviceName="ServicesEmpleados",name="EmpleadosWs")
@SOAPBinding(style=SOAPBinding.Style.RPC)
public class EJBEmpleados implements EJBEmpleadosRemote {
    @PersistenceContext(unitName="EJBMelaniPU2")
    private EntityManager em;

   

    public long addEmpleadoFullTime(String apellido, String nombre, String password, String passwordre, String email, short idtipoDocu, int numerodocu, float salario,
            short idGenero,String Observaciones) {
        long retorno = 0;
        try {
            FullTimeEmpleado empfulltime = new FullTimeEmpleado();
                 empfulltime.setApellido(apellido);
                 empfulltime.setEmail(email);
                 empfulltime.setNombre(nombre);
                 empfulltime.setPassword(password);
                 empfulltime.setGeneros(em.find(Generos.class, idGenero));
                 empfulltime.setObservaciones(Observaciones);
                 empfulltime.setTipodocumento(em.find(Tiposdocumento.class, idtipoDocu));
                 empfulltime.setNrodocumento(numerodocu);
                 empfulltime.setSalario(BigDecimal.valueOf(salario));
               em.persist(empfulltime);
               
               retorno = empfulltime.getIdPersona();


        } catch (Exception e) {
            retorno = -1;
            e.getMessage();
        }finally{
            return retorno;
        }
    }

    public long addEmpleadoParttime(String apellido, String nombre, String password, String paswordre,String email, short idtipo, int numerodocu, float salarioxhora,
            short idGenero,String Observaciones) {
        long retorno =0;
        try {
            EmpleadoParttime empparttime = new EmpleadoParttime();
            empparttime.setApellido(apellido);
            empparttime.setNombre(nombre);
            empparttime.setPassword(password);
            empparttime.setTipodocumento(em.find(Tiposdocumento.class, idtipo));
            empparttime.setNrodocumento(numerodocu);
            empparttime.setEmail(email);
            empparttime.setSalarioporhora(BigDecimal.valueOf(salarioxhora));

            em.persist(empparttime);
            em.flush();
            retorno = empparttime.getIdPersona();

        } catch (Exception e) {
            retorno = -1;
            e.getMessage();
        }finally{
            return retorno;
        }
    }






    
 
}
