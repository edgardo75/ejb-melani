/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.melani.ejb;

import com.melani.entity.Barrios;
import java.sql.Connection;
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
import org.apache.log4j.Level;
import org.apache.log4j.Logger;


/**
 *
 * @author Edgardo
 */
@Stateless(name="ejb/EJBBarrios")
@WebService(serviceName="ServiceBarrios",name="BarriosWs")
@SOAPBinding(style=SOAPBinding.Style.RPC)
public class EJBBarrios implements EJBBarriosRemote {
   private static Logger logger = Logger.getLogger(EJBBarrios.class);
   @PersistenceContext
   private EntityManager em;
   @Resource(name = "jdbc/_melani")
   private DataSource datasource;
   

       // @SuppressWarnings("CallToThreadDumpStack")
    public long addBarrio(String descripcion,int idUsuario) {
        long retorno = 0;
        try {
            logger.setLevel(Level.WARN);
            //------------------------------------------------------------------------------------------------
            descripcion = descripcion.toLowerCase();
            Query consulta =  em.createNativeQuery("SELECT * FROM BARRIOS WHERE LOWER(BARRIOS.DESCRIPCION) LIKE LOWER('"+descripcion+"%')", Barrios.class);
            List<Barrios> lista = consulta.getResultList();
            //------------------------------------------------------------------------------------------------
          if (lista.isEmpty()) {
                Barrios bario = new Barrios();
                bario.setDescripcion(descripcion.toUpperCase());
                em.persist(bario);
                em.flush();
                logger.info("SE cargó la descripcion del barrio");
                retorno = bario.getId();
            } else {
                retorno = -4;
            }
            //------------------------------------------------------------------------------------------------


        } catch (Exception e) {
            retorno = -1;
            logger.error("Error en metodo addBarrio "+e);
        } finally {
            return retorno;
        }
    }

    public String searchallbarrios() {

        String xml = "<Lista>\n";
        //OracleXMLQuery oxq = null;
        //Connection con = null;
        try {
            Query consulta = em.createNamedQuery("Barrios.findAll");
            List<Barrios>lista = consulta.getResultList();

            if(lista.size()==0)
                xml="LA CONSULTA NO ARROJÓ RESULTADOS";
            else{
                for (Iterator<Barrios> it = lista.iterator(); it.hasNext();) {
                    Barrios barrios = it.next();
                    xml+=barrios.toXML();
                }
                xml += "</Lista>\n";

            }
           /* try {
                
                con = datasource.getConnection();
                
            } catch (Exception e) {
                logger.error("No se pudo Obtener La Conexion con La base de Datos en metodo searchallbarrios"+e);
                xml = "No se pudo Obtener La Conexion con La base de Datos";
            }

            //*******************************************************************
            
            String sql = "SELECT b.ID_BARRIO as id,b.descripcion FROM BARRIOS b ORDER BY b.ID_BARRIO asc";

            oxq = new OracleXMLQuery(con, sql);

            oxq.setRowTag("Item");
            oxq.setRowsetTag("Lista");
           // oxq.setEncoding("ISO-8859-1");
            xml = oxq.getXMLString();
            oxq.close();

            if (xml.contains("<Lista/>")) {
                xml = "La Consulta no arrojó resultados!!!";
            }*/

        //*********************************************************************
        } catch (Exception e) {
            xml="error";
            logger.error("error en metodo searchallbarrios",e.getCause());
        } finally {
            /*try {
            
                if (con != null) {
                    con.close();
                }
                if (oxq != null) {
                    oxq.close();
                }
            } catch (Exception e) {
                logger.error("Error cerrando conexiones metodo searchallbarrios "+e);
            }*/
            
            return xml;

        }
    }

    public int recordCountBarrios() {
        int retorno =0;
      
        OracleXMLQuery oxq = null;
        Connection con = null;
        String xml =null;
        try {
            try {

                con = datasource.getConnection();
         
            } catch (Exception e) {
                logger.error("No se pudo Obtener La Conexion con La base de Datos en metodo recordCountBarrios"+e);
             
            }
             String sql = "SELECT COUNT(*) FROM BARRIOS b";

             oxq = new OracleXMLQuery(con, sql);

            oxq.setRowTag("Item");
            oxq.setRowsetTag("Lista");
            oxq.setEncoding("ISO-8859-1");
            xml = oxq.getXMLString();
            oxq.close();

            if (xml.contains("<Lista/>")) {
                xml = "La Consulta no arrojó resultados!!!";
            }
             

             retorno = Integer.valueOf(xml.substring(xml.indexOf("<COUNT>")+7, xml.indexOf("</COUNT>")));


        } catch (Exception e) {
            retorno = -1;
            logger.error("Error al Obtener la cantidad de registros de la tabla barrios", e);
        }finally{
            try {
         
                if (con != null) {
                    con.close();
                }
                if (oxq != null) {
                    oxq.close();
                }
                
            } catch (Exception e) {
                logger.error("Error cerrando conexiones metodo recordCountBarrios "+e);
            }
            return retorno;
        

        }
        
        

    }

    public String obtenrItemsPaginados(int indiceInicio, int numeroItems) {
        String xml = "<Lista>\n";
        try {
            Query consulta = em.createNativeQuery("SELECT FIRST "+numeroItems+" SKIP ("+indiceInicio+"*"+numeroItems+") * FROM BARRIOS b ORDER BY b.id_barrio", Barrios.class);

            List<Barrios>lista = consulta.getResultList();

            for (Iterator<Barrios> it = lista.iterator(); it.hasNext();) {
                Barrios barrios = it.next();
                xml+=barrios.toXML();
            }
           

        } catch (Exception e) {
            logger.error("Error al obtenerItemsPaginados EJBbarrios "+e);

        }finally{
         
               
            return xml+"</Lista>\n";
        
    }
}

    public Barrios[] barrios_paging(int startindex, int numitems) {
         Barrios[]fBarrios=null;
        try {
            Query consulta = em.createNativeQuery("SELECT FIRST "+numitems+" SKIP ("+startindex+"*"+numitems+") * FROM BARRIOS b ORDER BY b.id_barrio");

            List<Barrios>lista = consulta.getResultList();
            try {
                int len = lista.size();

                fBarrios = new Barrios[len];
                lista.toArray(fBarrios);
            } catch (Exception ee) {
               ee.getMessage();
            }
            
        } catch (Exception e) {
            e.getMessage();
        }finally{
         
        return fBarrios;
        }
    }

   




 
}
