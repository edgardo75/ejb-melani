/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.melani.ejb;

import com.melani.entity.EmpleadoParttime;
import com.melani.entity.Empleados;
import com.melani.entity.FullTimeEmpleado;
import com.melani.entity.Generos;
import com.melani.entity.Tiposdocumento;
import java.math.BigDecimal;
import java.util.GregorianCalendar;
import java.util.Iterator;
import java.util.List;
import javax.ejb.Stateless;
import javax.jws.WebService;
import javax.jws.soap.SOAPBinding;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import org.apache.commons.lang3.StringEscapeUtils;
import org.apache.log4j.Logger;

/**
 *
 * @author Edgardo
 */
@Stateless(name="ejb/EJBEmpleados")
@WebService(serviceName="ServicesEmpleados",name="EmpleadosWs")
@SOAPBinding(style=SOAPBinding.Style.RPC)
public class EJBEmpleados implements EJBEmpleadosRemote {
     private static Logger logger = Logger.getLogger(EJBEmpleados.class);
    @PersistenceContext(unitName="EJBMelaniPU2")
    private EntityManager em;

   

    public long addEmpleadoFullTime(String apellido,String nameuser, String nombre, String password, String passwordre, String email, short idtipoDocu, int numerodocu, float salario,
            short idGenero,String Observaciones) {
        long retorno = 0;
        try {
            
            FullTimeEmpleado empfulltime = new FullTimeEmpleado();
                 empfulltime.setApellido(apellido);
                 empfulltime.setEmail(email);
                 empfulltime.setNombre(nombre);
                 empfulltime.setNameuser(StringEscapeUtils.escapeXml(nameuser));
                 empfulltime.setPassword(password);
                 empfulltime.setGeneros(em.find(Generos.class, idGenero));
                 empfulltime.setEstado((short)1);
                 empfulltime.setObservaciones(Observaciones);
                 empfulltime.setTipodocumento(em.find(Tiposdocumento.class, idtipoDocu));
                 empfulltime.setNrodocumento(numerodocu);
                 empfulltime.setSalario(BigDecimal.valueOf(salario));
               em.persist(empfulltime);
               
               retorno = empfulltime.getIdPersona();


        } catch (Exception e) {
            retorno = -1;
            logger.error("Error en metodo addEmpleadoFullTime ",e.getCause());
            
        }finally{
            return retorno;
        }
    }

    public long addEmpleadoParttime(String apellido,String nameuser, String nombre, String password, String paswordre,String email, short idtipo, int numerodocu, float salarioxhora,
            short idGenero,String Observaciones) {
        long retorno =0;
        try {
            EmpleadoParttime empparttime = new EmpleadoParttime();
            empparttime.setApellido(apellido);
            empparttime.setNameuser(StringEscapeUtils.escapeXml(nameuser));
            empparttime.setNombre(nombre);
            empparttime.setPassword(password);
            empparttime.setTipodocumento(em.find(Tiposdocumento.class, idtipo));
            empparttime.setNrodocumento(numerodocu);
            empparttime.setEstado((short)1);
            empparttime.setEmail(email);
            empparttime.setSalarioporhora(BigDecimal.valueOf(salarioxhora));

            em.persist(empparttime);
            em.flush();
            retorno = empparttime.getIdPersona();

        } catch (Exception e) {
            retorno = -1;
            logger.error("Error en metodo addEmpleadoParttime ",e.getCause());
        }finally{
            return retorno;
        }
    }

    public String selectAllEmpleados() {
        String xml = "<Lista>\n";
        try {
            Query consulta = em.createQuery("SELECT e FROM Empleados e WHERE e.estado = :estado");
            consulta.setParameter("estado", (short)1);            
            List<Empleados>lista = consulta.getResultList();
            if(lista.size()>0){
                for (Iterator<Empleados> it = lista.iterator(); it.hasNext();) {
                    Empleados empleados = it.next();
                    xml+="<item>\n";
                    xml+="<id>" +empleados.getIdPersona()+"</id>\n"+
                            "<nombre>"+empleados.getNombre()+"</nombre>\n" +
                            "<apellido>"+empleados.getApellido()+"</apellido>\n" ;
                    xml+=empleados.toXMLEmpleado();
                    xml+="</item>\n";


                }
            }else
                xml+="<result>Lista Vacia</result>\n";

        } catch (Exception e) {
            logger.error("error al obtener lista de empleados "+e.getMessage());
            xml+="<error>"+e.getCause()+"</error>\n";
        }finally{
            return xml+="</Lista>\n";
        }

    }

    public int deshabilitarEmpleado(int idEmpleado, int idEmpleadoDesabilito) {
        int retorno =0;
        try {
            GregorianCalendar gc = new GregorianCalendar();
            StringBuilder sb =new StringBuilder();
                Empleados empleadoDesabilitado = em.find(Empleados.class, (long)idEmpleado);
                Empleados empleadoDesabilito = em.find(Empleados.class, (long)idEmpleadoDesabilito);

                empleadoDesabilitado.setEstado((short)0);

                    sb.append(empleadoDesabilito.getNombre());
                    sb.append(" ");
                    sb.append(empleadoDesabilito.getApellido());
                    logger.info("EMPLEADO DESABILITADO EL DIA DE LA FECHA "+gc.getTime()+", EL EMPLEADO QUE REALIZÓ LA OPERACION FUE "+sb.toString());
                    
             retorno=Integer.valueOf(String.valueOf(empleadoDesabilitado.getIdPersona()));


        } catch (Exception e) {
            retorno=-1;
            logger.error("Error en metodo deshabilitarEmpleado", e.getCause());
        }finally{
            return retorno;
        }

    }








    
 
}
