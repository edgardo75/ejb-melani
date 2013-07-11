/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.melani.ejb;

import com.melani.entity.Calles;

import java.util.Iterator;
import java.util.List;

import javax.ejb.Stateless;
import javax.jws.WebService;
import javax.jws.soap.SOAPBinding;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;


import org.apache.log4j.Logger;


/**
 *
 * @author Edgardo
 * @version 1.0 Build 5600 Feb 20, 2013
 */
@Stateless(name="ejb/EJBCalles")

@SOAPBinding(style=SOAPBinding.Style.RPC)
@WebService(serviceName="ServicesCalles",name="CallesWs")
public class EJBCalles implements EJBCallesRemote {
   private static Logger logger = Logger.getLogger(EJBCalles.class);
   @PersistenceContext
   private EntityManager em;
   
/**
 *
 * @param descripcion nombre propio de calle
 * @param idUsuario usuario que realiza la accion de carga de calle
 * @return retorna el id de la calle insertada
 */
    public long addCalles(String descripcion,int idUsuario) {
        long retorno = 0;
        try {
            //paso a minúsculas las letras de la palabra
            descripcion = descripcion.toLowerCase();
            Query consulta = em.createNativeQuery("SELECT * FROM CALLES WHERE LOWER(CALLES.DESCRIPCION) like LOWER('"+descripcion+"%')",Calles.class);

            
            List<Calles> lista = consulta.getResultList();

                if (lista.isEmpty()) {
                    Calles calle = new Calles();
                    calle.setDescripcion(descripcion.toUpperCase());
                    em.persist(calle);
                    em.flush();

                    retorno = calle.getId();

                } else {
                    retorno = -5;
                }

        } catch (Exception e) {
            retorno = -1;
            logger.error("Error en metodo addCalles "+e);
        } finally {
            return retorno;
        }

    }
/**
 *
 * @return devuelve una lista de calles en la vista actual del contenedor
 */
    public String searchAllCalles() {
        String xml = "<Lista>\n";
       
        try {

            Query consulta =em.createQuery("SELECT c FROM Calles c Order by c.id");

            List<Calles>lista = consulta.getResultList();


            if(lista.size()==0)
                xml="LA CONSULTA NO ARROJÓ RESULTADOS";
            else{

                for (Iterator<Calles> it = lista.iterator(); it.hasNext();) {
                    Calles calles = it.next();
                    xml+=calles.toXML();
                }

                xml+="</Lista>\n";
            }
          

        } catch (Exception e) {
            xml="Error";
            logger.error("Error en metodo searchallcalles", e.getCause());
            e.getMessage();
        } finally {           
            return xml;
        }
    }
/**
 *
 * @return devuelve la cantidad de calles actualmente en la vista del contenedor
 */
    public Object recorCountCalles() {
        int retorno =0;
        try {
            Query consulta = em.createNativeQuery("SELECT COUNT(*) FROM CALLES");

                String resultado = consulta.getResultList().toString();
                    resultado = resultado.replace("[[", "");
                    resultado = resultado.replace("]]", "");
            
                retorno = Integer.valueOf(resultado);

        } catch (Exception e) {
            logger.error("Error en metodo recorCountCalles "+e);
        }finally{
            return retorno;
        }
    }

  









    
 
}
