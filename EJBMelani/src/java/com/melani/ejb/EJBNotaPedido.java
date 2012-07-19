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
import javax.annotation.Resource;
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
@SOAPBinding(style=SOAPBinding.Style.RPC)
@WebService(serviceName="ServiceNotaPedido",name="NotaPedidoWs")
public class EJBNotaPedido implements EJBNotaPedidoRemote {
    private static Logger logger = Logger.getLogger(EJBNotaPedido.class);
    @PersistenceContext
    private EntityManager em;
    @Resource(name = "jdbc/_melani")
    
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
                        System.out.println("221");
            List<Itemdetallesnota>lista = notadepedido.getDetallesnota().getDetallesnota();
                                    System.out.println("221");
            for (Iterator<Itemdetallesnota> it = lista.iterator(); it.hasNext();) {
                                        System.out.println("222");
                Itemdetallesnota itemdetallesnota = it.next();

                                        System.out.println("222");
                Productos productos = em.find(Productos.class,itemdetallesnota.getId_producto());
                                        System.out.println("222");

                    DetallesnotadepedidoPK detallespk = new DetallesnotadepedidoPK(notape.getId(), itemdetallesnota.getId_producto());
                                            System.out.println("222");
                    Detallesnotadepedido detalles = new Detallesnotadepedido();
                                            System.out.println("222");
                    detalles.setCancelado(Character.valueOf(itemdetallesnota.getCancelado()));
                                            System.out.println("222");
                    detalles.setCantidad(itemdetallesnota.getCantidad());
                                            System.out.println("222");
                    detalles.setDescuento(BigDecimal.valueOf(itemdetallesnota.getDescuento()));
                                            System.out.println("222");
                    detalles.setEntregado(Character.valueOf(itemdetallesnota.getEntregado()));
                                            System.out.println("222");
                    detalles.setIva(BigDecimal.valueOf(itemdetallesnota.getIva()));
                                            System.out.println("222");
                    detalles.setNotadepedido(notape);
                                            System.out.println("222");
                    detalles.setPendiente(Character.valueOf(itemdetallesnota.getPendiente()));
                                            System.out.println("222");
                    detalles.setPrecio(BigDecimal.valueOf(itemdetallesnota.getPrecio()));
                                            System.out.println("222");
                    detalles.setProductos(productos);
                                            System.out.println("222");
                    detalles.setSubtotal(BigDecimal.valueOf(itemdetallesnota.getSubtotal()));
                                            System.out.println("222");
                    detalles.setDetallesnotadepedidoPK(detallespk);
                                            System.out.println("222");
                    detalles.setAnulado(Character.valueOf(itemdetallesnota.getAnulado()));
                    em.persist(detalles);

                                            System.out.println("222");
                Query consulta = em.createQuery("SELECT d FROM Detallesnotadepedido d WHERE d.detallesnotadepedidoPK.fkIdproducto = :fkIdproducto");
                                        System.out.println("222");
                consulta.setParameter("fkIdproducto", itemdetallesnota.getId_producto());
                                        System.out.println("222");

                productos.setDetallesnotadepedidoList(consulta.getResultList());
                                        System.out.println("222");
            }

                                                            System.out.println("221");
            Query consulta1 = em.createQuery("SELECT d FROM Detallesnotadepedido d WHERE d.detallesnotadepedidoPK.fkIdnota = :fkIdnota");
                                    System.out.println("221");
            consulta1.setParameter("fkIdnota", notape.getId());
                                    System.out.println("221");
            notape.setDetallesnotadepedidoList(consulta1.getResultList());
                                    System.out.println("221");

            em.merge(notape);
            System.out.println("221");

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
            System.out.println("++++");
            List<Itemdetallesnota>lista = notadepedido.getDetallesnota().getDetallesnota();

            System.out.println("++++");
           
            for (Iterator<Itemdetallesnota> it = lista.iterator(); it.hasNext();) {
                System.out.println("++++");
                Itemdetallesnota itemdetallesnota = it.next();

                System.out.println("++++");
                Productos productos = em.find(Productos.class,itemdetallesnota.getId_producto());
                System.out.println("++++");
                DetallesnotadepedidoPK detallespk = new DetallesnotadepedidoPK(notape.getId(), itemdetallesnota.getId_producto());
                System.out.println("++++");

                Detallesnotadepedido detalles = new Detallesnotadepedido();

                System.out.println("++++");
                detalles.setCancelado(Character.valueOf(itemdetallesnota.getCancelado()));
                System.out.println("++++");
                detalles.setCantidad(itemdetallesnota.getCantidad());
                System.out.println("++++");
                detalles.setDescuento(BigDecimal.valueOf(itemdetallesnota.getDescuento()));
                System.out.println("++++");
                detalles.setEntregado(Character.valueOf(itemdetallesnota.getEntregado()));
                System.out.println("++++");
                detalles.setIva(BigDecimal.valueOf(itemdetallesnota.getIva()));
                System.out.println("++++");
                detalles.setNotadepedido(notape);
                System.out.println("++++");
                detalles.setPendiente(Character.valueOf(itemdetallesnota.getPendiente()));
                System.out.println("++++");
                detalles.setPrecio(BigDecimal.valueOf(itemdetallesnota.getPrecio()));
                System.out.println("++++");
                detalles.setProductos(productos);
                System.out.println("++++");
                detalles.setSubtotal(BigDecimal.valueOf(itemdetallesnota.getSubtotal()));
                System.out.println("++++");
                detalles.setDetallesnotadepedidoPK(detallespk);
                System.out.println("++++");
                em.persist(detalles);

                Query consulta = em.createQuery("SELECT d FROM Detallesnotadepedido d WHERE d.detallesnotadepedidoPK.fkIdproducto = :fkIdproducto");
                System.out.println("++++");
                consulta.setParameter("fkIdproducto", itemdetallesnota.getId_producto());
                System.out.println("++++");
                productos.setDetallesnotadepedidoList(consulta.getResultList());
                System.out.println("++++");
                producto.controlStockProducto(itemdetallesnota.getId_producto(), itemdetallesnota.getCantidad(), notadepedido.getUsuario_expidio_nota());
                System.out.println("++++");


            }

                                Query consulta1 = em.createQuery("SELECT d FROM Detallesnotadepedido d WHERE d.detallesnotadepedidoPK.fkIdnota = :fkIdnota");
                                System.out.println("++++");
                                consulta1.setParameter("fkIdnota", notape.getId());
                                System.out.println("++++");
                                notape.setDetallesnotadepedidoList(consulta1.getResultList());
                                System.out.println("++++");
                                em.merge(notape);
                                System.out.println("++++");

                                retorno = notape.getId();
                            System.out.println("++++");

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
            System.out.println("111");
                    Historiconotapedido historico = new Historiconotapedido();
                                System.out.println("111");
                        historico.setAnticipo(BigDecimal.valueOf(notadepedido.getAnticipo()));
                                    System.out.println("111");
                        historico.setEntregado(Character.valueOf(notadepedido.getEntregado()));
                                    System.out.println("111");
                        historico.setFecharegistro(gc.getTime());
                                    System.out.println("111");
                        historico.setFkidnotapedido(em.find(Notadepedido.class, notape.getId()));
                                    System.out.println("111");
                        
                        
                                    System.out.println("111");
                        historico.setObservaciones(notadepedido.getObservaciones());
                                    System.out.println("111");
                        historico.setPendiente(Character.valueOf(notadepedido.getPendiente()));
                                    System.out.println("111");
                        historico.setPorcentajeaplicado(notadepedido.getPorcentajes().getId_porcentaje());
                                    System.out.println("111");
                        historico.setSaldo(BigDecimal.valueOf(notadepedido.getSaldo()));
                                    System.out.println("111");
                        historico.setTotal(BigDecimal.valueOf(notadepedido.getMontototal()));

                        historico.setIdusuarioanulo(notadepedido.getId_usuario_anulado());
                        historico.setIdusuariocancelo(notadepedido.getUsuario_cancelo_nota());
                        historico.setIdusuarioentrega(notadepedido.getUsuario_entregado());
                                    System.out.println("111");
                        historico.setIdusuarioexpidio(notadepedido.getUsuario_expidio_nota());

                        historico.setHoraregistro(gc.getTime());
                                    System.out.println("111");
                        
                                    em.persist(historico);
                                    
                                    
             //-----------------------------------------------------------------------
                                    System.out.println("111");
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
            System.out.println(nota.getId());
            xml =nota.toXML();
            
        } catch (Exception e) {
            logger.error("Error en metodo selectUnaNota "+e.getCause());
            xml = "Error no paso nada";
        }finally{
            
            System.out.println(xml);
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
                nota.setidusuariocancelo(idusuariocancelo);
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
