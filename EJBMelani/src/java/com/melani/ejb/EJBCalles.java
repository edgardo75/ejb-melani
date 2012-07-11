/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.melani.ejb;

import com.melani.entity.Calles;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import javax.annotation.Resource;
import javax.ejb.Stateless;
import javax.jws.WebService;
import javax.jws.soap.SOAPBinding;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.sql.DataSource;
import oracle.xml.sql.query.OracleXMLQuery;
import org.apache.log4j.Logger;


/**
 *
 * @author Edgardo
 */
@Stateless(name="ejb/EJBCalles")

@SOAPBinding(style=SOAPBinding.Style.RPC)
@WebService(serviceName="ServicesCalles",name="CallesWs")
public class EJBCalles implements EJBCallesRemote {
   private static Logger logger = Logger.getLogger(EJBCalles.class);
   @PersistenceContext
   private EntityManager em;
   @Resource(name = "jdbc/_melani")
   private DataSource datasource;

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
                logger.info("Se cargó la descripcion de la calle");
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

    public String searchAllCalles() {
        String xml = "fallo";
        OracleXMLQuery oxq = null;
        
        Connection con = null;
        try {
            try {

                con = datasource.getConnection();
                System.out.println("CONEXION "+con.toString());
            } catch (Exception e) {
                logger.error("No se pudo Obtener La Conexion con La base de Datos en metodo searchAllCalles"+e);
                xml = "No se pudo Obtener La Conexion con La base de Datos";
            }

            String sql = "SELECT * FROM CALLES ORDER BY CALLES.ID_CALLE";
            
                oxq = new OracleXMLQuery(con, sql);
            

            oxq.setRowTag("Item");

            oxq.setRowsetTag("Lista");

            oxq.setEncoding("ISO-8859-1");


            xml = oxq.getXMLString();

            oxq.close();

            if (xml.contains("<Lista/>")) {
                xml = "La Consulta no arrojó resultados!!!";
            }

        } catch (Exception e) {
            e.getMessage();
        } finally {
            try {
                if (con != null) {
                    con.close();
                }
                if (oxq != null) {
                    oxq.close();
                }

            } catch (Exception e) {
                xml = "error en el servidor";
                logger.error("Error cerrando conexiones en metodo searchAllCalles "+e);
            }
            
            return xml;

        }
    }

    public Object recorCountCalles() {
        int retorno =0;
        try {
            Query consulta = em.createNativeQuery("SELECT COUNT(*) FROM CALLES");
            //retorno = Integer.valueOf(consulta.toString());
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

   /* public String get_ItemCalles_Paging1(int indice, int cantidadRegistros) {
        
       String xml = "<Lista>\n";
        try {
           

            Query consulta = em.createNativeQuery("SELECT FIRST "+cantidadRegistros+" SKIP ("+indice+"*"+cantidadRegistros+") * FROM CALLES c ORDER BY c.id_calle",Calles.class);
            List<Calles>lista = consulta.getResultList();

            for (Iterator<Calles> it = lista.iterator(); it.hasNext();) {
                Calles calles = it.next();
              xml+=calles.toXML();
            }
           

        } catch (Exception e) {
            logger.error("Error en metodo get_ItemCalles_Paging de ejbCalles "+e);
        }finally{

        return xml+"</Lista>\n";
        }
    }*/

    public Calles[] calles_paging(Integer startIndex, Integer numItems) {
         List<Calles> lista=null;
        try {

            Query consulta = em.createNativeQuery("SELECT FIRST "+numItems+" SKIP ("+startIndex+"*"+numItems+") c.id_calle,c.DESCRIPCION FROM CALLES c ORDER BY c.id_calle",Calles.class);
            //Query consulta = em.createNativeQuery("SELECT c.id_calle,c.descripcion FROM Calles c ORDER BY c.id_calle ASC",Calles.class).setFirstResult(startIndex).setMaxResults(numItems);
            lista = consulta.getResultList();
            //System.out.println("tamano de la lista "+lista.size());       
           // aCalles = new ArrayList<Calles>(Arrays.asList(fCalles));
        } catch (Exception e) {
                //e.getMessage();
            e.getMessage();
           // logger.error("Error en metodo calles_paging");
            

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
            System.out.println(calles.getDescripcion());
            
        }
        
        return lleca;
    }

    /**
     * Web service operation
     */
    public Object[] paginadoFirebird(Integer startIndex, Integer numItems) {
         com.melani.entity.Calles fCalles[] = null;
        //ArrayList<com.melani.entity.Calles>aCalles = new ArrayList<com.melani.entity.Calles>();
        try {


            Query consulta = em.createNativeQuery("SELECT FIRST "+numItems+" SKIP ("+startIndex+"*"+numItems+") c.id_calle,c.DESCRIPCION FROM CALLES c ORDER BY c.id_calle",Calles.class);
            //Query consulta = em.createNativeQuery("SELECT c.id_calle,c.descripcion FROM Calles c ORDER BY c.id_calle ASC",Calles.class).setFirstResult(startIndex).setMaxResults(numItems);
            List<Calles> lista = consulta.getResultList();
            //System.out.println("tamano de la lista "+lista.size());

            Iterator<Calles> iter = lista.iterator();
            
            while(iter.hasNext()){
                Calles lleca = iter.next();
                System.out.println(lleca.getDescripcion());
            
            }
            
           
            /*Calles lleca = null;
            for (Iterator<Calles> it = lista.iterator(); it.hasNext();) {
                
                Calles calles = it.next();
                lleca = new Calles();

                lleca.setId(calles.getId());
                lleca.setDescripcion(calles.getDescripcion());              

                aCalles.add(lleca);
                
            }*/

          
            
                int len = lista.size();
                fCalles = new Calles[len];
                fCalles=lista.toArray(fCalles);
            
            
           // aCalles = new ArrayList<Calles>(Arrays.asList(fCalles));
        } catch (Exception e) {
                //e.getMessage();
            e.getMessage();
           // logger.error("Error en metodo calles_paging");
            

        }finally{
//           System.out.println("tamano de la lista2 "+fCalles.length);

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
            /*for (Iterator<Calles> it = lista.iterator(); it.hasNext();) {
                Calles calles = it.next();
                arrayStreet.add(it.hashCode(), calles);


            }*/



        } catch (Exception e) {
            logger.error(e.getMessage());
        }finally{
           
               
            return arrayStreet;
        }
    }









    
 
}
