/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.melani.ejb;


import com.melani.entity.HistPersonasDomicilios;
import java.sql.Connection;
import java.util.GregorianCalendar;

import javax.annotation.Resource;
import javax.ejb.Stateless;
import javax.jws.WebService;
import javax.jws.soap.SOAPBinding;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import oracle.xml.sql.query.OracleXMLQuery;
import org.apache.log4j.Logger;

/**
 *
 * @author Edgardo
 */
@Stateless(mappedName="EJBHistoricoPersonaDomicilioBean")
@WebService(name="HistPersonasDomicilios",serviceName="ServicesHistPD")
@SOAPBinding(style=SOAPBinding.Style.RPC)
public class EJBHistoricoPersonaDomicilioBean implements EJBHistoricoPersonaDomicilioRemote {
    private static Logger logger = Logger.getLogger(EJBHistoricoPersonaDomicilioBean.class);
    @PersistenceContext
    private EntityManager em;
    @Resource(name = "jdbc/_melani")
    private javax.sql.DataSource datasource;

    public long addOneHomePerson(Integer idDomicilio, Integer idPersona,Integer idUsuario) {
        long retorno =0;
        try {

                    GregorianCalendar gc = new GregorianCalendar();
                    HistPersonasDomicilios histpd = new HistPersonasDomicilios();
                    histpd.setFechadecambio(gc.getTime());
                    histpd.setIdDomicilio(Long.valueOf(idDomicilio));
                    histpd.setIdPersona(Long.valueOf(idPersona));
                    histpd.setIdusuario(idUsuario);
                   
                    em.persist(histpd);
                    retorno=histpd.getIdhistperdom();



        } catch (Exception e) {
            retorno = -4;
            logger.error("Error en metodo addOneHomePerson, EJBHistoricoPersonaDomicilioBean "+e.getMessage());
        }finally{
          return retorno;
        }
        
    }

    public String searchAllHistPD() {
        String xml ="";
        OracleXMLQuery oxq=null;
        Connection con = null;
        try {
            //*********************************************************************************
            try {

                con = datasource.getConnection();
                
            } catch (Exception e) {
                logger.error("No se pudo Obtener La Conexion con La base de Datos en metodo searchAllHistPD"+e);
                xml = "No se pudo Obtener La Conexion con La base de Datos";
            }
            //*********************************************************************************
            String sql= "SELECT * FROM HISTPERSONASDOMICILIOS ORDER BY HISTPERSONASDOMICILIOS.IDPERSONA,HISTPERSONASDOMICILIOS.IDDOMICILIO";

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
            logger.error("Error en metodosearchallHistPD en EJBHISTORICOPERSONADOMICILIO "+e.getCause());
        }finally{
            try {

                if (con != null) {
                    con.close();
                }
                if (oxq != null) {
                    oxq.close();
                }
            } catch (Exception e) {
                logger.error("Error cerrando conexiones metodo searchAllHistPD "+e);
            }
            System.out.println(xml);
            return xml;
        }
        
    }
    

    

    
 
}
