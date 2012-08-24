/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.melani.ejb;

import cm.melani.utils.DatosNotaPedido;

import cm.melani.utils.DetallesNotaPedido;
import cm.melani.utils.Itemdetallesnota;
import com.melani.entity.Detallesnotadepedido;
import com.melani.entity.DetallesnotadepedidoPK;
import com.melani.entity.Historiconotapedido;
import com.melani.entity.Notadepedido;
import com.melani.entity.Personas;
import com.melani.entity.Porcentajes;
import com.melani.entity.Productos;
import com.melani.entity.TarjetasCreditoDebito;
import com.thoughtworks.xstream.XStream;
import java.math.BigDecimal;
import java.util.GregorianCalendar;

import java.util.Iterator;
import java.util.List;
import java.util.Locale;

import javax.ejb.EJB;
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
@Stateless(name="ejb/EJBNotaPedido")
@WebService(serviceName="ServiceNotaPedido",name="NotaPedidoWs")
@SOAPBinding(style=SOAPBinding.Style.RPC)
public class EJBNotaPedido implements EJBNotaPedidoRemote {
    private static Logger logger = Logger.getLogger(EJBNotaPedido.class);
    @PersistenceContext
    private EntityManager em;
    //@Resource(name = "jdbc/_melani")
    
    @EJB
      EJBProductosRemote producto;

    @Override
    public long agregarNotaPedido(String xmlNotaPedido) {
        long retorno =0L;

        try {

            //---------------------------------------------------------------------------------
            
            //---------------------------------------------------------------------------------

            XStream xestream = new XStream();
            xestream.alias("notapedido", DatosNotaPedido.class);
            xestream.alias("personas", DatosNotaPedido.Personas.class);
            xestream.alias("tarjetacredito", DatosNotaPedido.TarjetaCredito.class);
            xestream.alias("porcentajes", DatosNotaPedido.Porcentajes.class);
            xestream.alias("itemdetallesnota", Itemdetallesnota.class);
            xestream.alias("detallesnotapedido", DetallesNotaPedido.class);
            xestream.addImplicitCollection(DetallesNotaPedido.class, "list");

            
            DatosNotaPedido notadepedido = (DatosNotaPedido) xestream.fromXML(xmlNotaPedido);

             retorno = almacenarnota(notadepedido);
           
        } catch (Exception e) {
            logger.error("Error en metodo agregarNotaPedido, verifique", e);
            retorno = -1;
        }finally{
            
            
            return retorno;
        }
        
    }

    private long almacenarnota(DatosNotaPedido notadepedido) {
        long retorno =0L;
        try {
            //---------------------------------------------------------------------------------
            GregorianCalendar gc = new GregorianCalendar();
            //---------------------------------------------------------------------------------
            Notadepedido notape = new Notadepedido();
                            
                            notape.setAnticipo(BigDecimal.valueOf(notadepedido.getAnticipo()));
                            
                            notape.setAnulado(notadepedido.getAnulado());
                            
                            notape.setEnefectivo(notadepedido.getEnefectivo());
                            
                            notape.setEntregado(Character.valueOf(notadepedido.getEntregado()));
                            
                            notape.setFkIdcliente(em.find(Personas.class, notadepedido.getPersonas().getId()));

                            notape.setFkidporcentajenotaId(em.find(Porcentajes.class, notadepedido.getPorcentajes().getId_porcentaje()));

                            notape.setHoracompra(gc.getTime());

                            notape.setIdTarjetaFk(em.find(TarjetasCreditoDebito.class, notadepedido.getTarjetacredito().getId_tarjeta()));

                            notape.setIdUsuarioExpidioNota(notadepedido.getUsuario_expidio_nota());

                            notape.setIdusuarioAnulado(notadepedido.getId_usuario_anulado());

                            notape.setIdusuarioEntregado(notadepedido.getUsuario_entregado());

                            notape.setMontoiva(BigDecimal.valueOf(notadepedido.getMontoiva()));

                            notape.setNumerodecupon(notadepedido.getNumerodecupon());

                            notape.setObservaciones(notadepedido.getObservaciones());

                            notape.setPendiente(Character.valueOf(notadepedido.getPendiente()));

                            notape.setRecargo(BigDecimal.valueOf(notadepedido.getRecargo()));

                            notape.setSaldo(BigDecimal.valueOf(notadepedido.getSaldo()));

                            notape.setStockfuturo(notadepedido.getStockfuturo());

                            notape.setTotal(BigDecimal.valueOf(notadepedido.getMontototal()));

                            notape.setFechadecompra(gc.getTime());

                            notape.setCancelado(Character.valueOf(notadepedido.getCancelado()));
                            
                            notape.setDescuentonota(BigDecimal.valueOf(notadepedido.getDescuentonota()));

                            notape.setIdusuariocancelo(notadepedido.getUsuario_cancelo_nota());

                            notape.setMontototalapagar(BigDecimal.valueOf(notadepedido.getMontototalapagar()));

                            notape.setPorcdesctotal(BigDecimal.valueOf(notadepedido.getPorc_descuento_total()));

                            notape.setPorcrecargo(BigDecimal.valueOf(notadepedido.getPorcentajerecargo()));

                            
                            
                            em.persist(notape);

                            
            
                            
            /*
             * trato la lista de productos de la nota de pedido, a continuación*/
                  
                  long historico =0;
                         switch(notadepedido.getStockfuturo()){
                             case 0:{retorno = almacenardetallenotaconcontrolstock(notadepedido,notape);
                                             /*Almacenar el historico en el método para que quede bien registrada la operacion*/
                                           historico =  almacenarhistorico(notadepedido,notape);
                                       
                                     
                                        
                
                                    }
                             break;
                             default :
                                    {retorno = almacenardetallenota(notadepedido,notape);
                                      /*Almacenar el historico en el método para que quede bien registrada la operacion*/
                                       historico =  almacenarhistorico(notadepedido,notape);
                                       
                                    }

                         }
                 if(historico<0)
                     retorno = historico;
                 else
                     retorno = notape.getId();




        } catch (Exception e) {
            logger.error("Error en metodo almacenarnota, EJBNotaPedido ",e.getCause());
            retorno =-1;
        }finally{
            
            return retorno;
        }
    }

    private long almacenardetallenota(DatosNotaPedido notadepedido, Notadepedido notape) {
        long retorno =0;
        try {
            /* a continuacion "desmenuzo" todo el detalle de la nota de pedido para
             * asi de esa manera poder amancenarla en los lugares correspondiente como lo es en detalles
             * de pedido de la nota y sus ligaduras con la nota de pedido propiamente dicha y los productos
             * para mantener la persistencia de las lista de manera real*/
                        
            List<Itemdetallesnota>lista = notadepedido.getDetallesnota().getDetallesnota();
                        
            for (Iterator<Itemdetallesnota> it = lista.iterator(); it.hasNext();) {
                        
                Itemdetallesnota itemdetallesnota = it.next();

                        
                Productos productos = em.find(Productos.class,itemdetallesnota.getId_producto());
                        

                    DetallesnotadepedidoPK detallespk = new DetallesnotadepedidoPK(notape.getId(), itemdetallesnota.getId_producto());
                        
                    Detallesnotadepedido detalles = new Detallesnotadepedido();
                        
                    detalles.setCancelado(Character.valueOf(itemdetallesnota.getCancelado()));
                        
                    detalles.setCantidad(itemdetallesnota.getCantidad());
                        
                    detalles.setDescuento(BigDecimal.valueOf(itemdetallesnota.getDescuento()));
                        
                    detalles.setEntregado(Character.valueOf(itemdetallesnota.getEntregado()));
                        
                    detalles.setIva(BigDecimal.valueOf(itemdetallesnota.getIva()));
                        
                    detalles.setNotadepedido(notape);
                        
                    detalles.setPendiente(Character.valueOf(itemdetallesnota.getPendiente()));
                        
                    detalles.setPrecio(BigDecimal.valueOf(itemdetallesnota.getPrecio()));
                        
                    detalles.setProductos(productos);
                        
                    detalles.setSubtotal(BigDecimal.valueOf(itemdetallesnota.getSubtotal()));
                        
                    detalles.setDetallesnotadepedidoPK(detallespk);
                        
                    detalles.setAnulado(Character.valueOf(itemdetallesnota.getAnulado()));

                    detalles.setPreciocondescuento(BigDecimal.valueOf(itemdetallesnota.getPreciocondescuento()));

                    em.persist(detalles);

                    
                Query consulta = em.createQuery("SELECT d FROM Detallesnotadepedido d WHERE d.detallesnotadepedidoPK.fkIdproducto = :fkIdproducto");
                    
                consulta.setParameter("fkIdproducto", itemdetallesnota.getId_producto());
                    

                productos.setDetallesnotadepedidoList(consulta.getResultList());
                    
            }

                    
            Query consulta1 = em.createQuery("SELECT d FROM Detallesnotadepedido d WHERE d.detallesnotadepedidoPK.fkIdnota = :fkIdnota");
                    
            consulta1.setParameter("fkIdnota", notape.getId());

            
            notape.setDetallesnotadepedidoList(consulta1.getResultList());
            

            em.merge(notape);
            

            retorno = notape.getId();
            

        } catch (Exception e) {
            logger.error("Error en metdodo almacenardetallenota",e.getCause());
            retorno=-1;
        }finally{
            
            return retorno;
        }
    }

    private long almacenardetallenotaconcontrolstock(DatosNotaPedido notadepedido, Notadepedido notape) {
        long retorno =0;
        try{
/* Idem metodo almacenar nota con la inclusion del metodo para controlar el stock llamando
 a metodo remoto de EJBPRoductosRemote, perdon por no usar reusabilidad de codigo fuente, no se me ocurria otra alternativa
 */
    
            List<Itemdetallesnota>lista = notadepedido.getDetallesnota().getDetallesnota();

    
           
            for (Iterator<Itemdetallesnota> it = lista.iterator(); it.hasNext();) {
    
                Itemdetallesnota itemdetallesnota = it.next();

    
                Productos productos = em.find(Productos.class,itemdetallesnota.getId_producto());
    
                DetallesnotadepedidoPK detallespk = new DetallesnotadepedidoPK(notape.getId(), itemdetallesnota.getId_producto());
    

                Detallesnotadepedido detalles = new Detallesnotadepedido();

    
                detalles.setCancelado(Character.valueOf(itemdetallesnota.getCancelado()));
    
                detalles.setCantidad(itemdetallesnota.getCantidad());
    
                detalles.setDescuento(BigDecimal.valueOf(itemdetallesnota.getDescuento()));

                detalles.setPreciocondescuento(BigDecimal.valueOf(itemdetallesnota.getPreciocondescuento()));
    
                detalles.setEntregado(Character.valueOf(itemdetallesnota.getEntregado()));
    
                detalles.setIva(BigDecimal.valueOf(itemdetallesnota.getIva()));
    
                detalles.setNotadepedido(notape);

    
                detalles.setPendiente(Character.valueOf(itemdetallesnota.getPendiente()));
    
                detalles.setPrecio(BigDecimal.valueOf(itemdetallesnota.getPrecio()));
    
                detalles.setProductos(productos);
    
                detalles.setSubtotal(BigDecimal.valueOf(itemdetallesnota.getSubtotal()));
    
                detalles.setDetallesnotadepedidoPK(detallespk);
    
                em.persist(detalles);

                Query consulta = em.createQuery("SELECT d FROM Detallesnotadepedido d WHERE d.detallesnotadepedidoPK.fkIdproducto = :fkIdproducto");
    
                consulta.setParameter("fkIdproducto", itemdetallesnota.getId_producto());

                productos.setDetallesnotadepedidoList(consulta.getResultList());

                producto.controlStockProducto(itemdetallesnota.getId_producto(), itemdetallesnota.getCantidad(), notadepedido.getUsuario_expidio_nota());



            }

                                Query consulta1 = em.createQuery("SELECT d FROM Detallesnotadepedido d WHERE d.detallesnotadepedidoPK.fkIdnota = :fkIdnota");
                                
                                consulta1.setParameter("fkIdnota", notape.getId());
                                
                                notape.setDetallesnotadepedidoList(consulta1.getResultList());
                                
                                em.merge(notape);
                                

                                retorno = notape.getId();
                            

        }catch(Exception e){
            logger.error("Error en metodo almacenardetallenotaconcontrolstock ",e.getCause().fillInStackTrace());
            retorno =-1;
        }finally{
            
            return retorno;
        }
    }

    private long almacenarhistorico(DatosNotaPedido notadepedido,Notadepedido notape) {
        long resultado =0L;

        //----------------------------------------------------------------------------------------
        try{
            //------------------------------------------------------------------------
            GregorianCalendar gc = new GregorianCalendar(Locale.getDefault());
            //------------------------------------------------------------------------
            
                    Historiconotapedido historico = new Historiconotapedido();
            
                        historico.setAnticipo(BigDecimal.valueOf(notadepedido.getAnticipo()));
            
                        historico.setEntregado(Character.valueOf(notadepedido.getEntregado()));
            
                        historico.setFecharegistro(gc.getTime());
            
                        historico.setFkidnotapedido(em.find(Notadepedido.class, notape.getId()));
            
                        
                        
            
                        historico.setObservaciones(notadepedido.getObservaciones());
            
                        historico.setPendiente(Character.valueOf(notadepedido.getPendiente()));
            
                        historico.setPorcentajeaplicado(notadepedido.getPorcentajes().getId_porcentaje());
            
                        historico.setSaldo(BigDecimal.valueOf(notadepedido.getSaldo()));
            
                        historico.setTotal(BigDecimal.valueOf(notadepedido.getMontototal()));

                        historico.setIdusuarioanulo(notadepedido.getId_usuario_anulado());

                        historico.setIdusuariocancelo(notadepedido.getUsuario_cancelo_nota());

                        historico.setIdusuarioentrega(notadepedido.getUsuario_entregado());
            
                        historico.setIdusuarioexpidio(notadepedido.getUsuario_expidio_nota());

                        historico.setPorcrecargo(BigDecimal.valueOf(notadepedido.getPorcentajerecargo()));

                        historico.setTotalapagar(BigDecimal.valueOf(notadepedido.getMontototalapagar()));

                        historico.setDescuento(BigDecimal.valueOf(notadepedido.getDescuentonota()));
                        
                        historico.setRecargo(BigDecimal.valueOf(notadepedido.getRecargo()));

                        historico.setHoraregistro(gc.getTime());
                        
                        historico.setAccion("Historico Almacenado con exito nota de pedido N° "+notape.getId()+" ");
            
                        
                                    em.persist(historico);
                                    
                                    
             //-----------------------------------------------------------------------
            
                      try {
                            Query consulta = em.createQuery("SELECT h FROM Historiconotapedido h WHERE h.fkidnotapedido.id = :idnota");
                        consulta.setParameter("idnota", notape.getId());
                           notape.setHistoriconotapedidoList(consulta.getResultList());

                      } catch (Exception e) {
                          logger.error("Error en la consulta historiconotadepedido, metodo almacenar historico", e.getCause());
                          resultado = -2;
                      }
                        em.persist(notape);
                        em.flush();

               logger.info("Historico Almacenado con exito nota de pedido N° "+notape.getId()+" ");
               resultado = historico.getIdhistorico();


        }catch(Exception e){
            logger.error("Error en metodo almacenarhistorico", e.getCause());
            resultado =-1;
        }finally{
            
            return resultado;
        }
        //----------------------------------------------------------------------------------------
    }

    public String selectUnaNota(long idnta) {
        String xml = "";
        try {
            Notadepedido nota = em.find(Notadepedido.class, idnta);
           
            xml =nota.toXML();
            
        } catch (Exception e) {
            logger.error("Error en metodo selectUnaNota "+e.getCause());
            xml = "Error no paso nada";
        }finally{
            
           
            return xml;
        }
    }

    public long modificarSaldoNota(long idnota, double saldo, int idusuario) {
        long result =0L;
        try {
            //--------------------------------------------------------------------------
            GregorianCalendar gc = new GregorianCalendar(Locale.getDefault());
            //--------------------------------------------------------------------------
                        Notadepedido nota = em.find(Notadepedido.class, idnota);
                    //------------------------------------------------------------------
                        nota.setSaldo(nota.getSaldo().subtract(BigDecimal.valueOf(saldo)));
                    //------------------------------------------------------------------
                        Historiconotapedido historico = new Historiconotapedido();
                    //------------------------------------------------------------------
                        historico.setAnticipo(BigDecimal.valueOf(saldo));
                        historico.setEntregado(Character.valueOf(nota.getEntregado()));
                        historico.setFecharegistro(gc.getTime());
                        historico.setFkidnotapedido(nota);
                        historico.setHoraregistro(gc.getTime());
                        historico.setIdusuarioanulo(nota.getIdusuarioAnulado());
                        historico.setIdusuarioentrega(nota.getIdusuarioEntregado());
                        historico.setIdusuarioexpidio(idusuario);
                        historico.setIdusuariocancelo(0);
                        historico.setPendiente(Character.valueOf(nota.getPendiente()));
                        historico.setPorcentajeaplicado(nota.getFkidporcentajenotaId().getIdPorcentajes());
                        historico.setSaldo(nota.getSaldo());
                        historico.setTotal(nota.getTotal());
               //----------------------------------------------------------------------------------
                        long notaID = procesaListNotaHistorico(nota,historico);
               //----------------------------------------------------------------------------------

                    em.flush();

                result = notaID;

        } catch (Exception e) {
            logger.error("Error en metodo modificarSaldoNota "+e.getCause());
            result = -1;
        }finally{
            
            return result;
        }
    }

    public long cancelarNotaPedido(long idnota,int idusuariocancelo) {
        long result = 0L;
        try {
            //--------------------------------------------------------------------------
                       GregorianCalendar gc = new GregorianCalendar(Locale.getDefault());
            //--------------------------------------------------------------------------
            //--------------------------------------------------------------------------
            Notadepedido nota = em.find(Notadepedido.class,idnota);
                nota.setCancelado(Character.valueOf('1'));
                nota.setPendiente(Character.valueOf('0'));
                nota.setIdusuariocancelo(idusuariocancelo);
                nota.setFecancelado(gc.getTime());
            //--------------------------------------------------------------------------
            List<Detallesnotadepedido>lista = nota.getDetallesnotadepedidoList();
            //--------------------------------------------------------------------------
            for (Iterator<Detallesnotadepedido> it = lista.iterator(); it.hasNext();) {
                Detallesnotadepedido detallesnotadepedido = it.next();
                detallesnotadepedido.setCancelado(Character.valueOf('1'));
            }
            //--------------------------------------------------------------------------
                            Historiconotapedido historico = new Historiconotapedido();
                                historico.setAccion("NOTA DE PEDIDO N° "+nota.getId()+" CANCELADA POR EL USUARIO "+idusuariocancelo);
                                historico.setAnticipo(BigDecimal.ZERO);
                                historico.setEntregado(Character.valueOf(nota.getEntregado()));
                                historico.setFecharegistro(gc.getTime());
                                historico.setFkidnotapedido(nota);
                                historico.setHoraregistro(gc.getTime());
                                historico.setIdusuarioanulo(0);
                                historico.setIdusuarioentrega(0);
                                historico.setIdusuarioexpidio(0);
                                historico.setPendiente(Character.valueOf(nota.getPendiente()));
                                historico.setPorcentajeaplicado(Short.valueOf("0"));
                                historico.setSaldo(BigDecimal.ZERO);
                                historico.setTotal(BigDecimal.ZERO);
          //----------------------------------------------------------------------------------
                            long notaID = procesaListNotaHistorico(nota,historico);
          //----------------------------------------------------------------------------------

            em.flush();

                result = notaID;

        } catch (Exception e) {
            logger.error("Error en metodo cancelaNotaPedido", e.getCause());
            result = -1;
        }finally{
        return result;
        }
    }

    private long procesaListNotaHistorico(Notadepedido nota, Historiconotapedido historico) {
        long result = 0L;
        try {
            //-----------------------------------------------------------------
                     try {
                            Query consulta = em.createQuery("SELECT h FROM Historiconotapedido h WHERE h.fkidnotapedido.id = :idnota");
                        consulta.setParameter("idnota", nota.getId());
                           nota.setHistoriconotapedidoList(consulta.getResultList());

                      } catch (Exception e) {
                          logger.error("Error en la consulta historiconotadepedido, metodo almacenar historico", e.getCause());
                          result = -2;
                      }
                     //-----------------------------------------------------------------

                        em.persist(nota);
                        em.persist(historico);
                 result = nota.getId();

        } catch (Exception e) {
            logger.error("Error en metodo procesaListNotaHistorico", e.getCause());
            result = -1;
        }finally{
            return result;
        }

    }

    public long entregarNotaPedido(long idnota, int idusuarioentrega) {
        long result = 0L;
        try {
            //--------------------------------------------------------------------
                GregorianCalendar gc = new GregorianCalendar(Locale.getDefault());
            //--------------------------------------------------------------------

                Notadepedido nota = em.find(Notadepedido.class, idnota);
                    nota.setEntregado(Character.valueOf('1'));
                    nota.setFechaentrega(gc.getTime());
                    nota.setPendiente(Character.valueOf('0'));
                    nota.setIdusuarioEntregado(idusuarioentrega);

                List<Detallesnotadepedido>lista = nota.getDetallesnotadepedidoList();

                    for (Iterator<Detallesnotadepedido> it = lista.iterator(); it.hasNext();){
                            Detallesnotadepedido detallesnotadepedido = it.next();
                            detallesnotadepedido.setEntregado(Character.valueOf('1'));
                    }
                Historiconotapedido historico = new Historiconotapedido();
                                historico.setAccion("NOTA DE PEDIDO N° "+nota.getId()+" ENTREGADA POR EL USUARIO "+idusuarioentrega);
                                historico.setAnticipo(BigDecimal.ZERO);
                                historico.setEntregado(Character.valueOf('1'));
                                historico.setFecharegistro(gc.getTime());
                                historico.setFkidnotapedido(nota);
                                historico.setHoraregistro(gc.getTime());
                                historico.setIdusuarioanulo(0);
                                historico.setIdusuarioentrega(idusuarioentrega);
                                historico.setIdusuarioexpidio(0);
                                historico.setPendiente(Character.valueOf('1'));
                                historico.setPorcentajeaplicado(Short.valueOf("0"));
                                historico.setSaldo(BigDecimal.ZERO);
                                historico.setTotal(BigDecimal.ZERO);
                //----------------------------------------------------------------------------------
                            long notaID = procesaListNotaHistorico(nota,historico);
          //----------------------------------------------------------------------------------
                            em.flush();

                       result = notaID;


        } catch (Exception e) {
            logger.error("Error en metodo entregarNotaPedido "+e.getCause());
            result = -1;
        }finally{
            return result;
        }
    }
   
//-----------------------------------------------------------------------------------------------
    




   
    
    
 
}
