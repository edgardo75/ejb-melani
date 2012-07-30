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
 */
@Stateless(name="ejb/EJBDomicilios")
@WebService(serviceName="ServicesDomicilios",name="DomiciliosWs")
@SOAPBinding(style=SOAPBinding.Style.RPC)
public class EJBDomicilios implements EJBDomiciliosRemote {
    private static Logger logger = Logger.getLogger(EJBDomicilios.class);
    @PersistenceContext
    private EntityManager em;
    private long idDomicilio=0;

    public long addDomicilios(DatosDomicilios datosDomici) {
        long retorno =0;
        try {
            //------------------------------------------------------------------
          /*  XStream xstream = new XStream();
            xstream.alias("Domicilio",DatosDomicilios.class);
            DatosDomicilios domiciXML = (DatosDomicilios) xstream.fromXML(datosDomici);*/
            //------------------------------------------------------------------
             idDomicilio= existe(datosDomici);

            switch((int)idDomicilio){
                    case 0:{
                            retorno = procesarAddDomicilio(datosDomici);
                            logger.info("Valor devuelto por retorno en addDomicilio "+retorno);

                    }
                    break;
                    case -1:
                           retorno=-1;
                    break;
                default:{
                    retorno=idDomicilio;
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
                domicilioss.setManzana(domiciXML.getManzana());
          

            if (domiciXML.getEntrecalleycalle() != null) {          
                domicilioss.setEntrecalleycalle(domiciXML.getEntrecalleycalle().toUpperCase());
            }
          
                domicilioss.setSector(domiciXML.getSector());
          
                domicilioss.setMonoblock(domiciXML.getMonoblock());        
                    
            barrios = em.find(Barrios.class,(long) domiciXML.getBarrio().getBarrioId());
            calles = em.find(Calles.class,(long) domiciXML.getCalle().getCalleId());
            orientacion = em.find(Orientacion.class,(long) domiciXML.getOrientacion().getOrientacion());
       
                domicilioss.setLocalidades(em.find(Localidades.class,(long) domiciXML.getLocalidad().getIdLocalidad()));
            
                domicilioss.setBarrios(barrios);
            
                domicilioss.setCalles(calles);
            
                domicilioss.setOrientacion(orientacion);
            
                domicilioss.setNumero(domiciXML.getNumero());
            
                domicilioss.setNumdepto(domiciXML.getNumDepto());
                
                domicilioss.setObservaciones(domiciXML.getObservaciones().toUpperCase());
            
                domicilioss.setArea(domiciXML.getArea());
            
                em.persist(domicilioss);
                em.flush();
            
                retorno = domicilioss.getId();



        } catch (Exception xst) {
            logger.warn("Error en la libreria Xstream metodo procesaAdd "+xst);
            retorno = -2;
        }  finally {
            
            return retorno;
        }
    }

    private long existe(DatosDomicilios domiciXML) {
        long retorno =0;
        try {
            String jpql = "SELECT d FROM Domicilios d WHERE "
                    + "d.piso = :piso and "
                    + "d.entrecalleycalle = :entrecalleycalle "
                    + "and d.numero = :numero "                    
                    + "and d.area = :area "
                    + "and d.idbarrio.id = :idbarrio "
                    + "and d.monoblock = :monoblock "
                    + "and d.idcalle.id = :idcalle "
                    + "and d.sector = :sector "
                    + "and d.idorientacion.id = :idorientacion "
                    + "and d.manzana = :manzana "
                    + "and d.localidades.idLocalidad = :idlocalidad "
                    + "and d.numdepto = :numdepto "
                    + "and d.localidades.codigopostal = :codigopostal "
                    + "and d.localidades.provincias.idProvincia = :idprovincia";

            Query consulta = em.createQuery(jpql);

            
            consulta.setParameter("piso", domiciXML.getPiso());
            consulta.setParameter("entrecalleycalle", domiciXML.getEntrecalleycalle().toUpperCase());
            consulta.setParameter("numero", domiciXML.getNumero());            
            consulta.setParameter("area", domiciXML.getArea());            
            consulta.setParameter("idbarrio", domiciXML.getBarrio().getBarrioId());
            consulta.setParameter("monoblock", domiciXML.getMonoblock());
            consulta.setParameter("idcalle", domiciXML.getCalle().getCalleId());
            consulta.setParameter("sector", domiciXML.getSector());
            consulta.setParameter("idorientacion", domiciXML.getOrientacion().getOrientacion());
            consulta.setParameter("manzana", domiciXML.getManzana());
            consulta.setParameter("idlocalidad", domiciXML.getLocalidad().getIdLocalidad());
            consulta.setParameter("numdepto", domiciXML.getNumDepto());
            consulta.setParameter("codigopostal", domiciXML.getLocalidad().getCodigoPostal());
            consulta.setParameter("idprovincia", domiciXML.getLocalidad().getIdProvincia());

            if(consulta.getResultList().size()==1){
                List<Domicilios>lista = consulta.getResultList();
                
                for (Iterator<Domicilios> it = lista.iterator(); it.hasNext();) {
                    Domicilios domicilios = it.next();
                    retorno = domicilios.getId();

                }
                //retorno =1;
                logger.info("Domicilio Encontrado!!!");
            }



        } catch (Exception e) {
            retorno=-1;
            logger.error("Error en metodo existe de EJBDomicilio "+e);
        }finally{
            logger.info("Valor devuelto de existe "+retorno);

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
            long idDomicilio = existe(domiciXML);

            switch((int)idDomicilio){
                    case 1:{

                        retorno=actualizarDomicilio(domiciXML);
                        logger.info("Domicilio Actualizado "+retorno);
                    }
                    break;
                    case -1:retorno=-1;
                    break;
                default:{
                    retorno = procesarAddDomicilio(domiciXML);
                    logger.info("Domicilio Insertado Nº "+retorno);
                }
            }
            

        } catch (Exception e) {
            retorno =-2;
            logger.error("Error en Metodo addDomicilios "+e);
        }finally{
            return retorno;
        }
    }

    private long actualizarDomicilio(DatosDomicilios domiciXML) {
        long retorno = 0L;
        try {

            Domicilios domicilio = em.find(Domicilios.class, domiciXML.getDomicilioId());

            domicilio.setArea(domiciXML.getArea());
            domicilio.setBarrios(em.find(Barrios.class,(long) domiciXML.getBarrio().getBarrioId()));
            domicilio.setCalles(em.find(Calles.class,(long) domiciXML.getCalle().getCalleId()));

            if(domiciXML.getObservaciones()!=null)
                domicilio.setObservaciones(domicilio.getObservaciones()+" \n"+domiciXML.getObservaciones());
            
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
