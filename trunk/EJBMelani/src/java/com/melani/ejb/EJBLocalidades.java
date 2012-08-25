/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.melani.ejb;

import com.melani.entity.Localidades;
import com.melani.entity.Provincias;
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


/**
 *
 * @author Edgardo
 */
@Stateless(name="ejb/EJBLocalidades")
@WebService(serviceName="ServicesLocalidades",name="LocalidadesWs")
@SOAPBinding(style=SOAPBinding.Style.RPC)
public class EJBLocalidades implements EJBLocalidadesRemote {
    org.apache.log4j.Logger logger = org.apache.log4j.Logger.getLogger(EJBLocalidades.class);
     @PersistenceContext(unitName="EJBMelaniPU2")
     private EntityManager em;

     @Resource(name="jdbc/_melani")
     private DataSource datasource;
     
    public String searchAllLocalidades() {
        String xml ="<Lista>\n";
        Query jpql=null;
        try {
            int maxRecord = 20;
            int startPosition = 0;

            
            while(true){
            jpql =em.createQuery("SELECT l FROM Localidades l");
            jpql.setMaxResults(maxRecord);
            jpql.setFirstResult(startPosition);
                    

            List<Localidades>localidad = jpql.getResultList();
           

            if(localidad.isEmpty())
                break;
            else{
                for (Iterator<Localidades> it = localidad.iterator(); it.hasNext();) {
                Localidades localidades = it.next();
                xml += localidades.toXML();

            }

            }
            
            em.clear();
            startPosition = startPosition + localidad.size();
            }
        } catch (Exception e) {
            e.getMessage();
        }finally{
            
            return xml+="</Lista>\n";
        }
    }

    public String searchLocXProvincia(short provincia) {
        String xml ="<Lista>\n";
        Query jpql = null;
        try {
            jpql = em.createQuery("SELECT l FROM Localidades l WHERE l.provincias.idProvincia = :provincia");
            
            jpql.setParameter("provincia",provincia);
           
            List<Localidades>localidad = jpql.getResultList();

            for (Iterator<Localidades> it = localidad.iterator(); it.hasNext();) {
                Localidades localidades = it.next();
                xml += localidades.toXML();

            }

        } catch (Exception e) {
            e.getMessage();
        }finally{
          
            return xml+="</Lista>\n";
        }
    }

    public String searchAllLocalidadesSQL() {
        String resultado = "";
        OracleXMLQuery oxq = null;
        Connection con = null;
        try {
            try {
               con = datasource.getConnection();


            } catch (Exception e) {
                resultado = "No SE PUDO CONECTAR";
                e.getMessage();
            }
            String sql = "SELECT l.ID_LOCALIDAD as ID,l.descripcion as DESCRIPCION,l.CODIGOPOSTAL from localidades l order by l.ID_LOCALIDAD ASC;";

             oxq = new OracleXMLQuery(con, sql);

            oxq.setRowTag("Item");
            oxq.setRowsetTag("Lista");
            oxq.setEncoding("ISO-8859-1");
            resultado = oxq.getXMLString();
            oxq.close();

            if (resultado.contains("<Lista/>")) {
                resultado = "La Consulta no arrojó resultados!!!";
            }


        } catch (Exception e) {
            resultado = "FALLO";
            e.getCause();
        }finally{
            try {
                if (con != null) {
                    con.close();
                }
                if (oxq != null) {
                    oxq.close();
                }

            } catch (SQLException ex) {
                logger.error("Error en metodo searchAllLocalidadesSQL, ejblocalidades");
            }
            return resultado;
        }
    }

    public long addLocalidadCompleto(String descripcion, short idprovincia, int codigopostal) {
        long retorno = 0;
        try {

            descripcion = descripcion.toLowerCase();
            
            Query consulta = em.createQuery("SELECT l FROM Localidades l WHERE lower(l.descripcion) like :descripcion and l.codigopostal = :codigopostal and " +
                    "l.provincias.idProvincia = :idProvincia");
            
            consulta.setParameter("descripcion", descripcion);
            consulta.setParameter("codigopostal", codigopostal);
            consulta.setParameter("idProvincia", idprovincia);


            List<Localidades> lista = consulta.getResultList();

            if (lista.isEmpty()) {

                Localidades depto = new Localidades();

                depto.setDescripcion(descripcion.toUpperCase());
                
                depto.setProvincias(em.find(Provincias.class, idprovincia));
                depto.setCodigopostal(codigopostal);

                em.persist(depto);
                em.flush();


                retorno = depto.getIdLocalidad();
            } else {
                retorno = -6;
            }

        } catch (Exception e) {
            e.getMessage();
        }finally{
            return retorno;
        }
    }
    //--------------------------------------------------------------------------------
    
    //--------------------------------------------------------------------------------

    public String searchAllLocalidadesbyidprov(Short idprovincia) {
         String resultado = "<Lista>\n";
         try {
             Query consulta = em.createQuery("SELECT l FROM Localidades l WHERE l.provincias.idProvincia = :idprovincia order by l.descripcion asc");
             consulta.setParameter("idprovincia", idprovincia);
             List<Localidades>lista = consulta.getResultList();
             if(lista.size()==0)
                 resultado="NO HAY LOCALIDADES CARGADAS en "+em.find(Provincias.class, idprovincia).getProvincia();
             else{
                 for (Iterator<Localidades> it = lista.iterator(); it.hasNext();) {
                     Localidades localidades = it.next();
                     resultado+=localidades.toXML();

                 }
             resultado+="</Lista>\n";
             }


        } catch (Exception e) {
            logger.error("Error en metodo searchAllLocalidadesbyidprov ",e);
        }finally{
        return resultado;

        }
        /*OracleXMLQuery oxq = null;
        Connection con = null;
        try {
            con = datasource.getConnection();
            
            String sql = "SELECT l.ID_LOCALIDAD as ID,l.descripcion as DESCRIPCION,l.CODIGOPOSTAL FROM LOCALIDADES l WHERE l.id_provincia = "+idprovincia+" order by l.id_localidad";
            
            oxq = new OracleXMLQuery(con, sql);
            
              oxq.setRowTag("Item");
              oxq.setRowsetTag("Lista");
              oxq.setEncoding("ISO-8859-1");
              resultado = oxq.getXMLString();
              oxq.close();

            if (resultado.contains("<Lista/>")) {
                resultado = "La Consulta no arrojó resultados!!!";
            }
            
        } catch (Exception e) {
            logger.error("Error en metodo searchalllocalidadesbyidprov "+e);
        }finally{
            try {
                
                if(con!=null)
                    con.close();
                if(oxq!=null)
                    oxq.close();
               
            } catch (SQLException ex) {
                    logger.error("Error cerrando conexiones en metodo searchalllocalidadesbyidprov "+ex);
            }finally{
             return resultado;
            }
        }*/
    }
    
    




   
 
}
