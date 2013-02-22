/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.melani.ejb;

import cm.melani.utils.DatosDomicilios;
import com.melani.entity.Barrios;
import com.melani.entity.Calles;
import com.melani.entity.Domicilios;
import com.melani.entity.Localidades;
import com.melani.entity.Orientacion;
import com.thoughtworks.xstream.XStream;
import java.sql.Connection;

import java.util.Locale;
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
@Stateless(name="ejb/EJBDomicilios")
@WebService(serviceName="ServicesDomicilios",name="DomiciliosWs")
@SOAPBinding(style=SOAPBinding.Style.RPC)
public class EJBDomicilios implements EJBDomiciliosRemote {
    private static Logger logger = Logger.getLogger(EJBDomicilios.class);
    @PersistenceContext
    private EntityManager em;
    private long idDomicilio=0;
    @Resource(name = "jdbc/_melani")
   private DataSource datasource;

    public long addDomicilios(DatosDomicilios datosDomici) {
        long retorno =0;
        try {
            //------------------------------------------------------------------
          
            //------------------------------------------------------------------
             idDomicilio= existe(datosDomici);

            switch((int)idDomicilio){
                    case 0:{
                    
                            retorno = procesarAddDomicilio(datosDomici);
                            
                        break;
                    }                   
                    case -1:{
                           retorno=-1;
                    break;
                    }
                default:{
                   
                        retorno = actualizarDomicilio(datosDomici,idDomicilio);
                        
                    break;
                }
            }

        } catch (Exception e) {
            retorno =-2;
            logger.error("Error en Metodo addDomicilios "+e);
        }finally{
            return retorno;
        }
    }
    //------------------------------------------------------------------------------------------

    //------------------------------------------------------------------------------------------
    private long procesarAddDomicilio(DatosDomicilios domiciXML) {
        long retorno = 0;
        Barrios barrios = null;
        Calles calles = null;
        Orientacion orientacion = null;

        
        
        try {
          
                Domicilios domicilioss = new Domicilios();
          
                domicilioss.setPiso(domiciXML.getPiso());
          
          

            if (domiciXML.getEntrecalleycalle().length()>0) 
                  domicilioss.setEntrecalleycalle(domiciXML.getEntrecalleycalle().toUpperCase(Locale.getDefault()));
            else
                  domicilioss.setEntrecalleycalle("NO INGRESADO");


          
                domicilioss.setSector(domiciXML.getSector().toUpperCase(Locale.getDefault()));

                
              
                    domicilioss.setMonoblock(domiciXML.getMonoblock().toUpperCase(Locale.getDefault()));

          
            barrios = em.find(Barrios.class,(long) domiciXML.getBarrio().getBarrioId());

            calles = em.find(Calles.class,(long) domiciXML.getCalle().getCalleId());

            orientacion = em.find(Orientacion.class,(long) domiciXML.getOrientacion().getOrientacion());

       
                domicilioss.setLocalidades(em.find(Localidades.class,(long) domiciXML.getLocalidad().getIdLocalidad()));

                domicilioss.setBarrios(barrios);

                domicilioss.setCalles(calles);

                domicilioss.setOrientacion(orientacion);

                domicilioss.setNumero(domiciXML.getNumero());

              
                domicilioss.setNumdepto(domiciXML.getNumDepto());
               

               
                domicilioss.setArea(domiciXML.getArea().toUpperCase(Locale.getDefault()));
               


                  domicilioss.setManzana(domiciXML.getManzana().toUpperCase(Locale.getDefault()));


               
                domicilioss.setPiso(domiciXML.getPiso().toUpperCase(Locale.getDefault()));

                    domicilioss.setTorre(domiciXML.getTorre().toUpperCase(Locale.getDefault()));


            if(domiciXML.getObservaciones().length()>0){
                domicilioss.setObservaciones(domiciXML.getObservaciones().toUpperCase(Locale.getDefault()));

            }else{
                domicilioss.setObservaciones("NO INGRESADO");

            }
            
                em.persist(domicilioss);
                em.flush();
            
                retorno = domicilioss.getId();



        } catch (Exception xst) {
            logger.error("Error en metodo procesarAddDomicilio  "+xst);
            retorno = -2;
        }  finally {
            
            return retorno;
        }
    }

    private long existe(DatosDomicilios domiciXML) {
        long retorno =0;
        OracleXMLQuery oxq = null;
        Connection con = null;
        String xml =null;
        try {
             try {

                con = datasource.getConnection();

            } catch (Exception e) {
                logger.error("No se pudo Obtener La Conexion con La base de Datos en metodo recordCountBarrios"+e);

            }
               String sql = "SELECT * FROM DOMICILIOS d WHERE d.entrecalleycalle like '"+domiciXML.getEntrecalleycalle()+"' and d.manzana like '"+domiciXML.getManzana()+"' and d.numero = "+domiciXML.getNumero()+" and d.area like '"+domiciXML.getArea()+"' and d.torre like '"+domiciXML.getTorre()+"' and d.piso like '"+domiciXML.getPiso()+"' and d.sector like '"+domiciXML.getSector()+"' and d.monoblock like '"+domiciXML.getMonoblock()+"' and d.numdepto = "+domiciXML.getNumDepto()+" and d.ID_BARRIO = "+domiciXML.getBarrio().getBarrioId()+" and d.ID_CALLE = "+domiciXML.getCalle().getCalleId()+" and d.ID_ORIENTACION = "+domiciXML.getOrientacion().getOrientacion()+" and d.ID_LOCALIDAD = "+domiciXML.getLocalidad().getIdLocalidad();

             oxq = new OracleXMLQuery(con, sql);


            oxq.setRowTag("Item");
            oxq.setRowsetTag("Lista");
            oxq.setEncoding("ISO-8859-1");
            xml = oxq.getXMLString();
            
            oxq.close();
              if (xml.contains("<Lista/>")) {
                
                retorno =0;
            }else{
                
                retorno = Integer.valueOf(xml.substring(xml.indexOf("<ID_DOMICILIO>")+14,xml.indexOf("</ID_DOMICILIO>")));
            }
      


        } catch (Exception e) {
            retorno=-1;
            logger.error("Error en metodo existe de EJBDomicilio "+e);
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
   //----------------------------------------------------------------------------------------

    public long addDomicilio(String xmlDomicilio) {
       long retorno =0;
        try {
            //------------------------------------------------------------------
            XStream xstream = new XStream();
            xstream.alias("Domicilio",DatosDomicilios.class);
            DatosDomicilios domiciXML = (DatosDomicilios) xstream.fromXML(xmlDomicilio);
            //------------------------------------------------------------------
            long idDomicilio1 = existe(domiciXML);

            switch((int)idDomicilio1){
                    case 1:{

                        retorno=actualizarDomicilio(domiciXML,idDomicilio1);
                        
                    }
                    break;
                    case -1:retorno=-1;
                    break;
                default:{
                    retorno = procesarAddDomicilio(domiciXML);
                    
                }
            }
            

        } catch (Exception e) {
            retorno =-2;
            logger.error("Error en Metodo addDomicilio "+e);
        }finally{
            return retorno;
        }
    }

    private long actualizarDomicilio(DatosDomicilios domiciXML,long iddomicilio) {
        long retorno = 0L;
        try {
          
            Domicilios domicilio = em.find(Domicilios.class, iddomicilio);

            domicilio.setArea(domiciXML.getArea());
            domicilio.setBarrios(em.find(Barrios.class,(long) domiciXML.getBarrio().getBarrioId()));
            domicilio.setCalles(em.find(Calles.class,(long) domiciXML.getCalle().getCalleId()));

            if(domiciXML.getObservaciones()!=null)
                domicilio.setObservaciones(domiciXML.getObservaciones().toUpperCase());
            
                domicilio.setLocalidades(em.find(Localidades.class, domiciXML.getLocalidad().getIdLocalidad()));
                domicilio.setManzana(domiciXML.getManzana());
                domicilio.setMonoblock(domiciXML.getMonoblock());
                domicilio.setNumdepto(domiciXML.getNumDepto());
                domicilio.setNumero(domiciXML.getNumero());
                domicilio.setOrientacion(em.find(Orientacion.class, domiciXML.getOrientacion().getOrientacion()));
                domicilio.setPiso(domiciXML.getPiso());
                domicilio.setSector(domiciXML.getSector());

                em.merge(domicilio);

                retorno = domicilio.getId();


        } catch (Exception e) {
            retorno = -3;
            logger.error("Error en metodo actualizarDomicilio, EJBDomicilio "+e);
        }finally{
        return retorno;
        }
    }



    //------------------------------------------------------------------------------------------


   

    //-----------------------------------------------------------------------------------------
   
 
}
