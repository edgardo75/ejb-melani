package com.melani.ejb;
import com.melani.entity.Barrios;
import java.sql.Connection;
import java.sql.SQLException;
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
 * @version 1.0 Build 5600 Feb 20, 2013
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

   /**
    * 
    * @param descripcion nombre propio del barrio
    * @param idUsuario operario que raliza la accion
    * @return retorna el id del barrio 
    */

    public long addBarrio(String descripcion,int idUsuario) {
        long retorno = 0;
        try {
            logger.setLevel(Level.WARN);
            //------------------------------------------------------------------------------------------------
            descripcion = descripcion.toLowerCase();
            Query consulta =  em.createNativeQuery("SELECT BARRIOS.DESCRIPCION FROM BARRIOS WHERE LOWER(BARRIOS.DESCRIPCION) LIKE LOWER('"+descripcion+"%')", Barrios.class);
            List<Barrios> lista = consulta.getResultList();
            //------------------------------------------------------------------------------------------------
                  if (lista.isEmpty()) {
                        Barrios bario = new Barrios();
                        bario.setDescripcion(descripcion.toUpperCase());
                        em.persist(bario);
                        em.flush();

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

/**
 *
 * @return devuelve la lista de barrios
 */

    public String searchAllBarrios() {
        String xml ="<Lista>\n";        
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
                    xml +="</Lista>";
                }            
        //*********************************************************************
        } catch (Exception e) {
            xml="error en metodo searchAllBarrios";
            logger.error("error en metodo searchallbarrios",e.getCause());
        } finally {
            return xml;
        }
    }

/**
 *
 * @return devuelve la cantidad actual de barrios instanciados por el contenedor
 */
    public int recordCountBarrios() {
       int retorno =0;      
       try {
                       Query consulta = em.createQuery("Select b From Barrios b");
                       retorno = consulta.getResultList().size();
            } catch (Exception e) {
                retorno = -1;
                logger.error("Error al Obtener la cantidad de registros de la tabla barrios", e);
            }finally{
              return retorno;
            }
    }
/**
 *
 * @param indiceInicio indica la pagina en al cual se establece la consulta paginada
 * @param numeroItems indica los numeros de registros que retorna cada consulta paginada
 * @return devuelve una lista de barrios instanciados
 */
public String obtenrItemsPaginados(int indiceInicio, int numeroItems) {
        String xml = "<?xml version = '1.0' encoding = 'iso-8859-1'?>\n" +
                "<Lista>\n";
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
/**
 *
 * @param startindex indice de pagina
 * @param numitems cantidad de registro por pagina
 * @return devuelve la lista de barrios instanciados
 */
public Barrios[] barrios_Paging(int startindex, int numitems) {
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
/**
 *
 * @return devuelve la lista de barrios en la tabla correspondiente de la base de datos
 */
    public String selectAllBarrios() {
        //variables locales
        String xml = "";
        OracleXMLQuery oxq = null;
        Connection con = null;
        try{
                    try {
                        //me conecto a la base de datos
                        con = datasource.getConnection();
                    } catch (Exception e) {
                        logger.error("No se pudo Obtener La Conexion con La base de Datos en metodo searchallbarrios"+e);
                        xml = "No se pudo Obtener La Conexion con La base de Datos";
                    }
                        String sql =   "SELECT * FROM BARRIOS";
             //----------------------------------------------------------------------------
                        oxq = new OracleXMLQuery(con, sql);
                        oxq.setRowTag("Item");
                        oxq.setRowsetTag("Lista");
                        oxq.setEncoding("ISO-8859-1");
                        xml = oxq.getXMLString();
                        oxq.close();
             //----------------------------------------------------------------------------
        }catch(Exception e) {
            logger.error("Error en metodo selectAllBarrios", e.getCause());

        }finally{
            //Cerramos la conexiones correspondientes
                try {
                    if (con != null) {
                        con.close();
                    }
                    if (oxq != null) {
                        oxq.close();
                    }

                } catch (SQLException ex) {
                    logger.error("Error al cerrar las conexiones en método selectAllBarrios de EJBBarrios",ex.getCause());
                }
                
           return xml;
        }
    } 
}
