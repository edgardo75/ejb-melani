/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.melani.ejb;
import java.sql.Connection;
import java.sql.SQLException;
import javax.annotation.Resource;
import javax.ejb.Stateless;
import javax.jws.WebService;
import javax.jws.soap.SOAPBinding;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.sql.DataSource;
import oracle.xml.sql.query.OracleXMLQuery;
import org.apache.log4j.Logger;
/**
 *
 * @author Edgardo
 */
@Stateless(name="ejb/EJBMelaniLoggsBean")
@WebService(serviceName="ServiceMelaniLogs",name="LogsMelaniWs")
@SOAPBinding(style=SOAPBinding.Style.RPC)
public class EJBMelaniLoggsBean implements EJBMelaniLoggsRemote {
   Logger logger = Logger.getLogger(EJBMelaniLoggsBean.class);
    @PersistenceContext
    private EntityManager em;
    @Resource(name="jdbc/_melani")
     private DataSource datasource;
//-----------------------------------------------------------------------------------------
//----------------------------------------------------------------------------------------
    public String searchAllLogs() {
           String xml = "NADA";
        Connection con =null;
        OracleXMLQuery oxq = null;
        try {
           try {
                con = datasource.getConnection();
            } catch (Exception e) {
                logger.error("Error al conectar a base de datos", e);
            }
            String consulta = "SELECT * FROM LOGS l ORDER BY l.DATED DESC";
            oxq = new OracleXMLQuery(con, consulta);
            oxq.setRowTag("itemLog");
            oxq.setRowsetTag("Lista");
            oxq.setEncoding("ISO-8859-1");
            oxq.setDateFormat("dd/MM/yyyy");
            xml = oxq.getXMLString();
            oxq.close();
            if (xml.contains("<Lista/>")) {
                xml = "La Consulta no arrojó resultados!!!";
            }
        } catch (Exception e) {
            logger.error("Error en metodo searchAllLogs en EJBMelaniLogs "+e.getMessage());
        }finally{
            try {
                if (con != null) {
                    con.close();
                }
                if (oxq != null) {
                    oxq.close();
                }
            } catch (SQLException ex) {
               logger.error("Error a cerrar conexiones EJBMelaniLogs", ex.getCause());
            }
            return xml;
        }
    }
    //-------------------------------------------------------------------------------------------------
    public String getRecordCountLog() {
        String xml = "NADA";
        Connection con =null;
        OracleXMLQuery oxq = null;
        try {
           try {
                con = datasource.getConnection();
            } catch (Exception e) {
                logger.error("Error al conectar a base de datos", e);
            }
            String consulta = "SELECT COUNT(*) FROM LOGS";
            oxq = new OracleXMLQuery(con, consulta);
            oxq.setRowTag("itemLog");
            oxq.setRowsetTag("Lista");
            oxq.setEncoding("ISO-8859-1");
            oxq.setDateFormat("dd/MM/yyyy");
            xml = oxq.getXMLString();
            oxq.close();
            if (xml.contains("<Lista/>")) {
                xml = "La Consulta no arrojó resultados!!!";
            }
        } catch (Exception e) {
            logger.error("Error en metodo getRecordCountLog en ejbMelaniLog "+e.getMessage());
        }finally{
        try {
                if (con != null) {
                    con.close();
                }
                if (oxq != null) {
                    oxq.close();
                }
            } catch (SQLException ex) {
               logger.error("Error a cerrar conexiones EJBMelaniLogs", ex.getCause());
            }
        return xml;
        }
    }
    public String getRecordCountPagingLog(int index, int recordCount) {
                String xml = "NADA";
        Connection con =null;
        OracleXMLQuery oxq = null;
        try {
           try {
                con = datasource.getConnection();
            } catch (Exception e) {
                logger.error("Error al conectar a base de datos", e);
            }
            String consulta =  "SELECT FIRST "+index+" SKIP ("+index+"*"+recordCount+") l.MESSAGE,l."+"LEVEL"+",l.DATED,l.HOURS,l.LOGGER FROM LOGS l ORDER BY l.DATED DESC";
            oxq = new OracleXMLQuery(con, consulta);
            oxq.setRowTag("itemLog");
            oxq.setRowsetTag("Lista");
            oxq.setEncoding("ISO-8859-1");
            oxq.setDateFormat("dd/MM/yyyy");
            xml = oxq.getXMLString();
            oxq.close();
            if (xml.contains("<Lista/>")) {
                xml = "La Consulta no arrojó resultados!!!";
            }
        } catch (Exception e) {
            logger.error("Error en metodo getRecordCountPagingLog en ejbMelaniLog "+e.getMessage());
        }finally{
        try {
                if (con != null) {
                    con.close();
                }
                if (oxq != null) {
                    oxq.close();
                }
            } catch (SQLException ex) {
               logger.error("Error a cerrar conexiones EJBMelaniLogs", ex.getCause());
            }
        return xml;
        }
    }
    //--------------------------------------------------------------------------------------------------
}
