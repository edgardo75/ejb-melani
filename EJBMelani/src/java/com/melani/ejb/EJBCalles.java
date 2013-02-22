/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.melani.ejb;

import com.melani.entity.Calles;

import java.util.ArrayList;
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
 * @return devuelve una lista de calles instanciados
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
 * @return devuelve la cantidad de calles en la tabla de la base de datos
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

  /**
   *
   * @param startIndex indice de pagina
   * @param numItems numero de registro por pagina
   * @return listado de calles paginadas
   */

    public Calles[] calles_paging(Integer startIndex, Integer numItems) {
         List<Calles> lista=null;
        try {

            Query consulta = em.createNativeQuery("SELECT FIRST "+numItems+" SKIP ("+startIndex+"*"+numItems+") c.id_calle,c.DESCRIPCION FROM CALLES c ORDER BY c.id_calle",Calles.class);
            
            lista = consulta.getResultList();
            
        } catch (Exception e) {
            
            e.getMessage();
           
            

        }finally{


                int len = lista.size();
                Calles fCalles[] = new Calles[len];
                fCalles=lista.toArray(fCalles);
             return fCalles;
        }

       
    }

    private ArrayList<com.melani.entity.Calles> marshallToCalles(List<com.melani.entity.Calles> lista) {
        ArrayList<com.melani.entity.Calles>aCalles = new ArrayList<com.melani.entity.Calles>();
        com.melani.entity.Calles lleca;
        try {
          
            for (Iterator<com.melani.entity.Calles> it = lista.iterator(); it.hasNext();) {
                com.melani.entity.Calles calles = it.next();
                        lleca = new com.melani.entity.Calles();
                lleca.setDescripcion(calles.getDescripcion());
                lleca.setId(calles.getId());
                aCalles.add(lleca);

            }
          

        } catch (Exception e) {
            logger.error("Error en metodo marshallToCalles",e.getCause());
        }finally{
        return aCalles;
        }
    }
    //--------------------------------------------------------------------------------


    //--------------------------------------------------------------------------------

    public void persist(Object object) {
        em.persist(object);
    }

    @Override
    public Calles[] paginadocalles(int uno, int dos) {

        Calles []lleca = null;
        Query consulta = em.createQuery("SELECT FIRST dos SKIP (uno*dos) c.id_calle as id,c.descripcion as descripcion FROM CALLES c ORDER BY c.id_calle");
        consulta.setParameter(uno, "uno");
        consulta.setParameter(dos,"dos");
        
        
        List<Calles>lista = consulta.getResultList();
        
        for (Iterator<Calles> it = lista.iterator(); it.hasNext();) {
            Calles calles = it.next();
            
            
        }
        
        return lleca;
    }

    /**
     * Web service operation
     */
    public Object[] paginadoFirebird(Integer startIndex, Integer numItems) {
         com.melani.entity.Calles fCalles[] = null;
      
        try {


            Query consulta = em.createNativeQuery("SELECT FIRST "+numItems+" SKIP ("+startIndex+"*"+numItems+") c.id_calle,c.DESCRIPCION FROM CALLES c ORDER BY c.id_calle",Calles.class);
      
            List<Calles> lista = consulta.getResultList();
      

            Iterator<Calles> iter = lista.iterator();
            
            while(iter.hasNext()){
                Calles lleca = iter.next();
                
            
            }
            
           
      

          
            
                int len = lista.size();
                fCalles = new Calles[len];
                fCalles=lista.toArray(fCalles);
            
            
          
        } catch (Exception e) {
          
            e.getMessage();
          
            

        }finally{
           

             return fCalles;
        }
    }

    public ArrayList pagingStreet(int page, int recordSize) {
        ArrayList arrayStreet=new ArrayList();
        try {

            Query consulta = em.createNativeQuery("SELECT FIRST "+recordSize+" SKIP ("+page+"*"+recordSize+") " +
                    "c.id_calle,c.DESCRIPCION FROM CALLES c ORDER BY c.id_calle");
            List<Calles>lista = consulta.getResultList();
           
                        arrayStreet.add(lista);
            



        } catch (Exception e) {
            logger.error(e.getMessage());
        }finally{
           
               
            return arrayStreet;
        }
    }









    
 
}
