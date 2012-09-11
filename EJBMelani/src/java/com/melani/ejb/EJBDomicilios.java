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
              
          

            if (domiciXML.getEntrecalleycalle().length()>0) 
                  domicilioss.setEntrecalleycalle(domiciXML.getEntrecalleycalle().toUpperCase());
            else
                  domicilioss.setEntrecalleycalle("NO INGRESADO");



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
               

               
                domicilioss.setArea(domiciXML.getArea());
               


                  domicilioss.setManzana(domiciXML.getManzana());


               
                domicilioss.setPiso(domiciXML.getPiso());
               
                    domicilioss.setTorre(domiciXML.getTorre());
                

            if(domiciXML.getObservaciones().length()>0)
                domicilioss.setObservaciones(domiciXML.getObservaciones());
            else
                domicilioss.setObservaciones("NO INGRESADO");
                
            
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
         /*   String xml="";
        int numero=666;
        int numdepto=1;
        String manzana ="0";
        int idbarrio=500;
        int idcalle =1100;
        int idlocalidad =14000;
        int idorientacion=3;
        short idprovincia=12;*/

        System.out.println("RESULTADO DE LOS DOMICI "+domiciXML.getArea()+" "+domiciXML.getEntrecalleycalle()+" "+domiciXML.getManzana()+" "+domiciXML.getMonoblock()+" "+domiciXML.getObservaciones()
                +" "+domiciXML.getPiso()+" "+domiciXML.getSector()+" "+domiciXML.getTorre()+" "+domiciXML.getNumDepto()+" "+domiciXML.getNumero()+" "+domiciXML.getOrientacion().getOrientacion());
            
             //select * from domicilios where DOMICILIOS.NUMERO = 123 AND LOWER(DOMICILIOS.MANZANA) LIKE LOWER('NO INGRESADO') AND LOWER(DOMICILIOS.SECTOR) LIKE LOWER('0') AND LOWER(DOMICILIOS.MONOBLOCK) LIKE LOWER('0') AND DOMICILIOS.AREA LIKE LOWER('0') AND LOWER(DOMICILIOS.NUMDEPTO) LIKE LOWER('0') AND LOWER(DOMICILIOS.TORRE) LIKE LOWER('0') AND LOWER(DOMICILIOS.PISO) LIKE LOWER('0') AND LOWER(DOMICILIOS.ENTRECALLEYCALLE) LIKE LOWER('no ingresado')
            /*String sql = "SELECT * FROM DOMICILIOS WHERE DOMICILIOS.NUMERO = numero AND DOMICILIOS.ID_BARRIO = barrioid";/* AND DOMICILIOS.ID_BARRIO = 2 AND DOMICILIOS.ID_CALLE = 3" AND DOMICILIOS.ID_LOCALIDAD = idlocalidad AND DOMICILIOS.ID_ORIENTACION = idorientacion AND LOWER(DOMICILIOS.MANZANA) " +
                    "LIKE LOWER('manzana') AND LOWER(DOMICILIOS.SECTOR) LIKE LOWER('sector') AND LOWER(DOMICILIOS.MONOBLOCK) LIKE LOWER('monoblock') AND LOWER(DOMICILIOS.AREA) LIKE LOWER('area') AND LOWER(DOMICILIOS.NUMDEPTO) LIKE LOWER('numdepto') AND LOWER(DOMICILIOS.TORRE) LIKE LOWER('torre') " +
                    "AND LOWER(DOMICILIOS.PISO) LIKE LOWER('piso') AND LOWER(DOMICILIOS.ENTRECALLEYCALLE) LIKE LOWER('entrecalleycalle')";*/

           /* String jpql = "SELECT d FROM Domicilios d WHERE "
                    //+ "d.piso = :piso and "
                    //+ "d.entrecalleycalle = :entrecalleycalle "
                    + "d.numero = :numero "
                    //+ "and d.area LIKE :area "
                    + "and d.idbarrio.id = :idbarrio "
                    + "and d.monoblock = :monoblock "
                    + "and d.idcalle.id = :idcalle "
                    + "and d.sector = :sector "
                    + "and d.area = :area "
                    //+ "and d.monoblock LIKE :monoblock ";
                    //+ "d.piso = :piso and "
                    //+ "and d.numdepto = :numdepto ";
                    + "and d.idorientacion.id = :idorientacion "
                   // + "and d.manzana = :manzana";
                    + "and d.localidades.idLocalidad = :idlocalidad "
                  //  + "and d.numdepto LIKE :numdepto ";
                    + "and d.localidades.codigopostal = :codigopostal "
                    + "and d.localidades.provincias.idProvincia = :idprovincia ";
                    //+ "and d.torre LIKE :torre";

            Query consulta = em.createQuery(jpql);
           // Query consulta =em.createNativeQuery(sql);

            
            //consulta.setParameter("piso", domiciXML.getPiso());
            //
            consulta.setParameter("numero", domiciXML.getNumero());
            consulta.setParameter("idbarrio", domiciXML.getBarrio().getBarrioId());
            //consulta.setParameter("area", domiciXML.getArea());
             consulta.setParameter("monoblock", domiciXML.getMonoblock());
             consulta.setParameter("idcalle", domiciXML.getCalle().getCalleId());
             consulta.setParameter("sector", domiciXML.getSector());
             consulta.setParameter("area", domiciXML.getArea());
             //consulta.setParameter("monoblock", domiciXML.getMonoblock());

             //consulta.setParameter("piso", domiciXML.getPiso());
             //consulta.setParameter("numdepto", domiciXML.getNumDepto());
                consulta.setParameter("idorientacion", domiciXML.getOrientacion().getOrientacion());
            //consulta.setParameter("3", domiciXML.getCalle().getCalleId());
            consulta.setParameter("idlocalidad", domiciXML.getLocalidad().getIdLocalidad());
           // consulta.setParameter("idorientacion", domiciXML.getOrientacion().getOrientacion());
           // consulta.setParameter("manzana", domiciXML.getManzana());
            //consulta.setParameter("sector", domiciXML.getSector());
            //consulta.setParameter("monoblock", domiciXML.getMonoblock());
           //consulta.setParameter("area", domiciXML.getArea());
          // consulta.setParameter("numdepto", "NO INGRESADO");
           //consulta.setParameter("torre", domiciXML.getTorre());
           /*consulta.setParameter("entrecalleycalle", domiciXML.getEntrecalleycalle());
           

          /*  consulta.setParameter("idbarrio", domiciXML.getBarrio().getBarrioId());
            consulta.setParameter("monoblock", domiciXML.getMonoblock().toUpperCase());
            consulta.setParameter("idcalle", domiciXML.getCalle().getCalleId());
            consulta.setParameter("sector", domiciXML.getSector().toUpperCase());
            consulta.setParameter("idorientacion", domiciXML.getOrientacion().getOrientacion());
            //consulta.setParameter("manzana", domiciXML.getManzana().toUpperCase());
           // consulta.setParameter("idlocalidad", domiciXML.getLocalidad().getIdLocalidad());
            //consulta.setParameter("numdepto", domiciXML.getNumDepto().toUpperCase());
            consulta.setParameter("codigopostal", domiciXML.getLocalidad().getCodigoPostal());
            consulta.setParameter("idprovincia", domiciXML.getLocalidad().getIdProvincia());
            /*consulta.setParameter("torre", domiciXML.getTorre().toUpperCase());*/

             Query consulta=em.createQuery("SELECT d FROM Domicilios d WHERE d.entrecalleycalle = :entrecalleycalle and " +
                "d.manzana = :manzana and d.numero = :numero and d.area = :area and d.torre = :torre and d.piso = :piso and d.sector = :sector and " +
                "d.monoblock = :monoblock and d.numdepto = :numdepto and d.idbarrio.id = :idbarrio and d.idcalle.id = :idcalle and d.localidades.idLocalidad = :idlocalidad and " +
                "d.idorientacion.id = :idorientacion and d.localidades.provincias.idProvincia = :idprovincia");
        consulta.setParameter("entrecalleycalle", domiciXML.getEntrecalleycalle());

        consulta.setParameter("manzana", domiciXML.getManzana().toUpperCase());
        consulta.setParameter("numero", domiciXML.getNumero());
        consulta.setParameter("area", domiciXML.getArea().toUpperCase());
        consulta.setParameter("torre", domiciXML.getTorre().toUpperCase());
        consulta.setParameter("piso", domiciXML.getPiso().toUpperCase());
        consulta.setParameter("sector", domiciXML.getSector().toUpperCase());
        consulta.setParameter("monoblock", domiciXML.getMonoblock().toUpperCase());
        consulta.setParameter("numdepto", domiciXML.getNumDepto());
        consulta.setParameter("idbarrio", domiciXML.getBarrio().getBarrioId());
        consulta.setParameter("idcalle", domiciXML.getCalle().getCalleId());
        consulta.setParameter("idlocalidad", domiciXML.getLocalidad().getIdLocalidad());
        consulta.setParameter("idorientacion", domiciXML.getOrientacion().getOrientacion());
        consulta.setParameter("idprovincia", domiciXML.getLocalidad().getIdProvincia());

            System.out.println("********************************+RESULTADO DE LA CONSULTA*************************+ "+consulta.getResultList().size());
            List<Domicilios>lista = consulta.getResultList();
            if(lista.size()==1){
                
                
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
            long idDomicilio1 = existe(domiciXML);

            switch((int)idDomicilio1){
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
