/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.melani.ejb;

import cm.melani.utils.DatosNotaPedido;
import cm.melani.utils.DetallesNotaPedido;
import cm.melani.utils.Itemdetallesnota;
import com.melani.entity.Clientes;
import com.melani.entity.Detallesnotadepedido;
import com.melani.entity.DetallesnotadepedidoPK;
import com.melani.entity.Empleados;
import com.melani.entity.Historiconotapedido;
import com.melani.entity.Notadepedido;
import com.melani.entity.Personas;
import com.melani.entity.Porcentajes;
import com.melani.entity.Productos;
import com.melani.entity.TarjetasCreditoDebito;
import com.thoughtworks.xstream.XStream;
import java.math.BigDecimal;


import java.text.SimpleDateFormat;

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


import org.apache.commons.lang3.StringEscapeUtils;
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
    
    
    @EJB
      EJBProductosRemote producto;

    @EJB
    EJBPresupuestosRemote ejbpresupuesto;

    DatosNotaPedido notadepedido;

    

    private DatosNotaPedido xestreaNotapedido(String xmlNotapedido){
            XStream xestream = new XStream();

            xestream.alias("notapedido", DatosNotaPedido.class);

            xestream.alias("personas", DatosNotaPedido.Personas.class);

            xestream.alias("tarjetacredito", DatosNotaPedido.TarjetaCredito.class);

            xestream.alias("porcentajes", DatosNotaPedido.Porcentajes.class);

            xestream.alias("itemdetallesnota", Itemdetallesnota.class);

            xestream.alias("detallesnotapedido", DetallesNotaPedido.class);

            xestream.addImplicitCollection(DetallesNotaPedido.class, "list");

            return notadepedido = (DatosNotaPedido) xestream.fromXML(parsearCaracteresEspecialesXML(xmlNotapedido).toString());
    }
    @Override
    public long agregarNotaPedido(String xmlNotaPedido) {
        long retorno =0L;

        try {
             //---------------------------------------------------------------------------------
            
            //---------------------------------------------------------------------------------     

            
             
            
             retorno = almacenarnota(xestreaNotapedido(xmlNotaPedido));
           
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
            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
            //---------------------------------------------------------------------------------


                            Clientes cliente = em.find(Clientes.class, notadepedido.getPersonas().getId());

                            Notadepedido notape = new Notadepedido();

                                            notape.setAnticipo(BigDecimal.valueOf(notadepedido.getAnticipo()));

                                            notape.setAnulado(notadepedido.getAnulado());

                                            notape.setEnefectivo(notadepedido.getEnefectivo());
                                           
                                            notape.setPendiente(Character.valueOf(notadepedido.getPendiente()));

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

                                            

                                            notape.setRecargo(BigDecimal.valueOf(notadepedido.getRecargo()));

                                            notape.setSaldo(BigDecimal.valueOf(notadepedido.getSaldo()));

                                            notape.setStockfuturo(notadepedido.getStockfuturo());

                                            notape.setTotal(BigDecimal.valueOf(notadepedido.getMontototal()));

                                            notape.setFechadecompra(gc.getTime());


                                            
                                            notape.setFechaentrega(sdf.parse(notadepedido.getFechaentrega()));



                                            notape.setCancelado(Character.valueOf(notadepedido.getCancelado()));

                                            notape.setDescuentonota(BigDecimal.valueOf(notadepedido.getDescuentonota()));

                                            notape.setDescuentoPesos(BigDecimal.valueOf(notadepedido.getDescuentopesos()));

                                            notape.setIdusuariocancelo(notadepedido.getUsuario_cancelo_nota());

                                            try {
                                                notape.setMontototalapagar(BigDecimal.valueOf(notadepedido.getMontototalapagar()));
                                            } catch (Exception e) {
                                                e.printStackTrace();
                                            }


                                            notape.setPorcdesctotal(BigDecimal.valueOf(notadepedido.getPorc_descuento_total()));

                                            notape.setPorcrecargo(BigDecimal.valueOf(notadepedido.getPorcentajerecargo()));

                                            if(notadepedido.getCancelado()=='1')
                                                notape.setFecancelado(gc.getTime());

                                            if(notadepedido.getAnulado()=='1')
                                                notape.setFechaAnulado(gc.getTime());

                                            em.persist(notape);




                            /*
                             * trato la lista de productos de la nota de pedido, a continuación*/

                                  long historico =0;
                                         switch(notadepedido.getStockfuturo()){
                                             case 0:{

                                                 retorno = almacenardetallenotaconcontrolstock(notadepedido,notape);
                                                             /*Almacenar el historico en el método para que quede bien registrada la operacion*/
                                                           historico =  almacenarhistorico(notadepedido,notape);




                                                    }
                                             break;
                                             default :
                                                    {
                                                        retorno = almacenardetallenota(notadepedido,notape);
                                                      /*Almacenar el historico en el método para que quede bien registrada la operacion*/
                                                       historico =  almacenarhistorico(notadepedido,notape);

                                                    }

                                         }

                                         Query consulta =em.createQuery("SELECT n FROM Notadepedido n WHERE n.fkIdcliente.idPersona = :id");
                                         consulta.setParameter("id", cliente.getIdPersona());
                                         List<Notadepedido>lista=consulta.getResultList();
                                         cliente.setNotadepedidoList(lista);
                                         Double totalCompras = cliente.getTotalCompras().doubleValue()+notape.getMontototalapagar().doubleValue();
                                         cliente.setTotalCompras(BigDecimal.valueOf(totalCompras));
                                         em.persist(cliente);
                                         em.flush();

                                 if(historico<0)
                                     retorno = historico;
                                 else{

                                             retorno = notape.getId();
                                             logger.info("NOTA DE PEDIDO "+retorno+" ACCIONADA POR VENDEDOR "+notadepedido.getVendedor());

                                 }



        } catch (Exception e) {
            logger.error("Error en metodo almacenarnota, EJBNotaPedido ",e.getCause());
            retorno =-2;
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
             
            List<Itemdetallesnota>lista = notadepedido.getDetallesnotapedido().getDetallesnota();
                        
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
            retorno=-3;
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
    
            List<Itemdetallesnota>lista = notadepedido.getDetallesnotapedido().getDetallesnota();



    
           
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

                        historico.setPorcentajedesc(BigDecimal.valueOf(notadepedido.getPorc_descuento_total()));

                        
            
                        historico.setIdusuarioexpidio(notadepedido.getUsuario_expidio_nota());

                        historico.setPorcrecargo(BigDecimal.valueOf(notadepedido.getPorcentajerecargo()));

                        historico.setTotalapagar(BigDecimal.valueOf(notadepedido.getMontototalapagar()));



                        historico.setDescuento(BigDecimal.valueOf(notadepedido.getDescuentonota()));
                        
                        historico.setRecargo(BigDecimal.valueOf(notadepedido.getRecargo()));

                        historico.setHoraregistro(gc.getTime());

                        historico.setCancelado(notadepedido.getCancelado());

                        historico.setAnulado(notadepedido.getAnulado());
                       
                        
                        historico.setAccion("Historico Almacenado con exito nota de pedido N "+notape.getId());
            
                        
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

               
               resultado = historico.getIdhistorico();


        }catch(Exception e){
            logger.error("Error en metodo almacenarhistorico", e.getCause());
            resultado =-4;
        }finally{
            
            return resultado;
        }
        //----------------------------------------------------------------------------------------
    }

    public String selectUnaNota(long idnta) {
        String xml = "<Lista>\n";

        try {
            Notadepedido nota = em.find(Notadepedido.class, idnta);



                            

                    

            
            xml+=devolverNotaProcesadaSB(nota).toString();
           
            
            
                
           
            
            
            


            
        } catch (Exception e) {
            logger.error("Error en metodo selectUnaNota "+e.getCause());
            xml += "Error no paso nada";
        }finally{
            
           
            return xml+="</Lista>\n";
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

    public long cancelarNotaPedido(long idnota,int idusuariocancelo,int estado) {
        long result = 0L;
        
        char cancelado ='0';
        try {
            //--------------------------------------------------------------------------
                       GregorianCalendar gc = new GregorianCalendar(Locale.getDefault());
            //--------------------------------------------------------------------------
            //--------------------------------------------------------------------------
            Notadepedido nota = em.find(Notadepedido.class,idnota);
            Empleados empleado = em.find(Empleados.class,(long) idusuariocancelo);
            if(estado==1){
                cancelado ='1';
                nota.setCancelado(Character.valueOf(cancelado));
                
                nota.setIdusuariocancelo(idusuariocancelo);
                nota.setFecancelado(gc.getTime());
            }else{
                nota.setCancelado(Character.valueOf(cancelado));
        
                nota.setIdusuariocancelo(idusuariocancelo);
                nota.setFecancelado(null);
            }
            //--------------------------------------------------------------------------
            List<Detallesnotadepedido>lista = nota.getDetallesnotadepedidoList();
            //--------------------------------------------------------------------------
            for (Iterator<Detallesnotadepedido> it = lista.iterator(); it.hasNext();) {
                Detallesnotadepedido detallesnotadepedido = it.next();
                detallesnotadepedido.setCancelado(Character.valueOf(cancelado));
                
            }
            //--------------------------------------------------------------------------
                            Historiconotapedido historico = new Historiconotapedido();
                            if(estado==1){
                                historico.setAccion("Cancelada por"+empleado.getNameuser());
                               
                                historico.setCancelado(Character.valueOf(cancelado));
                                
                            }else{
                                historico.setAccion("No cancelada por"+empleado.getNameuser());
                               
                                historico.setCancelado(Character.valueOf(cancelado));
                                

                            }

                                historico.setAnticipo(BigDecimal.ZERO);                                
                                historico.setFecharegistro(gc.getTime());
                                historico.setFkidnotapedido(nota);
                                
                                historico.setHoraregistro(gc.getTime());
                                historico.setPendiente(Character.valueOf('0'));
                                historico.setEntregado(Character.valueOf('0'));
                                historico.setIdusuarioanulo(0);
                                historico.setIdusuarioentrega(0);
                                historico.setIdusuarioexpidio(0);
                                historico.setIdusuariocancelo(idusuariocancelo);
                                historico.setPorcentajeaplicado(Short.valueOf("0"));
                                historico.setSaldo(BigDecimal.ZERO);
                                historico.setTotal(BigDecimal.ZERO);
                                historico.setTotalapagar(BigDecimal.ZERO);
                                historico.setRecargo(BigDecimal.ZERO);
                                historico.setPorcrecargo(BigDecimal.ZERO);
                                historico.setPorcentajedesc(BigDecimal.ZERO);
                                historico.setDescuento(BigDecimal.ZERO);
                                historico.setAnulado('0');
                                


                                 em.persist(historico);
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
                        
                 result = nota.getId();

        } catch (Exception e) {
            logger.error("Error en metodo procesaListNotaHistorico", e.getCause());
            result = -1;
        }finally{
            return result;
        }

    }

    public long entregarNotaPedido(long idnota, int idusuarioentrega, int estado) {
        long result = 0L;
        char pendiente = '0';
        char entregado ='0';
        try {
            //--------------------------------------------------------------------
                GregorianCalendar gc = new GregorianCalendar(Locale.getDefault());
            //--------------------------------------------------------------------

                Notadepedido nota = em.find(Notadepedido.class,(long) idnota);
                Empleados empleado = em.find(Empleados.class,(long) idusuarioentrega);
                if(estado==1){
                    entregado ='1';
                    nota.setEntregado(Character.valueOf(entregado));
                    nota.setFechaentrega(gc.getTime());

                    nota.setPendiente(Character.valueOf(pendiente));
                    nota.setIdusuarioEntregado(idusuarioentrega);
                }else{
                    pendiente ='1';
                     nota.setEntregado(Character.valueOf(entregado));
                     
                     nota.setPendiente(pendiente);
                     nota.setIdusuarioEntregado(idusuarioentrega);
                }

                List<Detallesnotadepedido>lista = nota.getDetallesnotadepedidoList();

                    for (Iterator<Detallesnotadepedido> it = lista.iterator(); it.hasNext();){
                            Detallesnotadepedido detallesnotadepedido = it.next();
                            detallesnotadepedido.setEntregado(Character.valueOf(entregado));
                            detallesnotadepedido.setPendiente(Character.valueOf(pendiente));
                    }
               
                Historiconotapedido historico = new Historiconotapedido();
                
                        if(estado==1){
                          
                                historico.setAccion("Entregado por"+empleado.getNameuser());
                             
                                historico.setEntregado('1');
                             
                                historico.setPendiente('0');
                             

                        }else{
                          
                                historico.setAccion("No entregada por "+empleado.getNameuser());
                              
                                historico.setEntregado('0');
                             
                                historico.setPendiente('1');
                              
                        }




                                historico.setAnticipo(BigDecimal.ZERO);
                                historico.setFecharegistro(gc.getTime());
                                historico.setFkidnotapedido(nota);

                                historico.setHoraregistro(gc.getTime());

                                historico.setIdusuarioanulo(0);
                                historico.setIdusuarioentrega(0);
                                historico.setIdusuarioexpidio(0);
                                historico.setIdusuariocancelo(0);
                                historico.setPorcentajeaplicado(Short.valueOf("0"));
                                historico.setSaldo(BigDecimal.ZERO);
                                historico.setTotal(BigDecimal.ZERO);
                                historico.setTotalapagar(BigDecimal.ZERO);
                                historico.setRecargo(BigDecimal.ZERO);
                                historico.setPorcrecargo(BigDecimal.ZERO);
                                historico.setPorcentajedesc(BigDecimal.ZERO);
                                historico.setDescuento(BigDecimal.ZERO);
                                historico.setAnulado('0');
                                historico.setCancelado('0');


                                 em.persist(historico);

                                
                               
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
public StringBuilder parsearCaracteresEspecialesXML(String xmlNota){
    String xml = "No paso Nada";
    StringBuilder sb=null;
    try {
        sb=new StringBuilder(xmlNota);

            xml=StringEscapeUtils.escapeXml(xmlNota.substring(xmlNota.indexOf("es>")+3,xmlNota.indexOf("</ob")));


            sb.replace(sb.indexOf("es>")+3, sb.indexOf("</ob"), xml);

    } catch (Exception e) {
        xml = "Error";
        logger.error("Error en metodo parsearCaracteresEspecialesXML ",e.getCause());
    }finally{

    return sb;
    }

}


    public String selectNotaEntreFechas(String fecha1, String fecha2,int idvendedor) {
        String xml="<Lista>\n";
        List<Notadepedido>lista = null;
        
        try {

            

            Query jpasql=em.createNativeQuery("SELECT * FROM NOTADEPEDIDO n where CAST(n.FECHADECOMPRA as date)  between CAST('"+fecha1+"' as DATE) and cast('"+fecha2+"' as date) and n.entregado=0 and n.pendiente=1 order by n.id desc", Notadepedido.class);
                lista= jpasql.getResultList();

                if(lista.size()>0){
                    for (Iterator<Notadepedido> it = lista.iterator(); it.hasNext();) {
                             Notadepedido notadepedido1 = it.next();
                             xml+=notadepedido1.toXML();
                    }
                    fecha1=fecha1.substring(3, 5)+"/"+fecha1.substring(0, 2)+"/"+fecha1.substring(6, 10);
                    fecha2=fecha2.substring(3, 5)+"/"+fecha2.substring(0, 2)+"/"+fecha2.substring(6, 10);
                    StringBuilder sb =new StringBuilder(xml);
                    
                    String periodoconsultado = "<fechainicio>"+fecha1+"</fechainicio>\n" +
                            "<fechafinal>"+fecha2+"</fechafinal>\n";
                    
                     sb.replace(sb.indexOf("</numerocupon>")+14, sb.indexOf("<observaciones>"), "\n"+periodoconsultado);
                     xml=sb.toString();
         
                    
                }else
                    xml+="<result>lista vacia</result>\n";

        
            

                
        } catch (Exception e) {
            xml+="<error>Error</error>\n";
            logger.error("Error en metodo selectnotaEntreFechas",e.fillInStackTrace());
        }finally{


           
                return xml+="</Lista>\n";
        }

    }

    public int getRecorCountNotas() {
        int retorno =0;
        try {
            Query notas = em.createQuery("SELECT n FROM Notadepedido n");

            retorno =notas.getResultList().size();

        } catch (Exception e) {
            retorno =-1;
            logger.error("Error en metodo getRecorCountNotas");
        }finally{
            return retorno;
        }
        
    }


    public String selectAllNotas() {
        String lista = "<Lista>\n";
        try {
            Query consulta = em.createNativeQuery("SELECT * FROM NOTADEPEDIDO n ORDER BY n.ID DESC, n.FECHADECOMPRA DESC",Notadepedido.class);
            List<Notadepedido>result = consulta.getResultList();

            if(result.size()==0)
                lista="LA CONSULTA NO ARROJÓ RESULTADOS";
            else{
                    for (Iterator<Notadepedido> it = result.iterator(); it.hasNext();) {
                    Notadepedido notape = it.next();
                    lista+=devolverNotaProcesadaSB(notape).toString();

                }

            }

        } catch (Exception e) {
            lista ="ERROR EN METODO selectAllNotas";
            logger.error("Error en metodo selecAllNotas");
        }finally{
        return lista+"</Lista>\n";
        }
    }

    private StringBuilder devolverNotaProcesadaSB(Notadepedido nota) {
                long idusuarioexpidio=0L;
                String usuarioexpidio ="";
                long idusuarioanulonota=0L;
                String usuarioanulonota ="";
                long idusuarioentregonota=0L;
                String usuarioentregonota ="";
                long idusuariocancelonota =0L;
                String usuariocancelonota="";
                StringBuilder sb=null;
        try {
                         sb=new StringBuilder(nota.toXML());

            if(nota.getIdUsuarioExpidioNota()>0){
                 idusuarioexpidio = nota.getIdUsuarioExpidioNota();
                Personas persona = em.find(Personas.class, idusuarioexpidio);
                usuarioexpidio=persona.getApellido().toUpperCase()+" "+persona.getNombre().toUpperCase();
                sb.replace(sb.indexOf("dionota>")+8, sb.indexOf("</usuarioex"), usuarioexpidio);
            }
                    if(nota.getIdusuarioAnulado()>0){
                        idusuarioanulonota = nota.getIdusuarioAnulado();
                        Personas persona = em.find(Personas.class, idusuarioanulonota);
                        usuarioanulonota=persona.getApellido().toUpperCase()+" "+persona.getNombre().toUpperCase();
                        sb.replace(sb.indexOf("anulonota>")+10, sb.indexOf("</usuarioanul"), usuarioanulonota.toUpperCase());

                    }
                            if(nota.getIdusuarioEntregado()>0){
                                idusuarioentregonota=nota.getIdusuarioEntregado();
                                Personas persona = em.find(Personas.class, idusuarioentregonota);
                                usuarioentregonota=persona.getApellido().toUpperCase()+" "+persona.getNombre().toUpperCase();
                                sb.replace(sb.indexOf("oentregonota>")+13, sb.indexOf("</usuarioent"), usuarioentregonota.toUpperCase());
                            }
                                if(nota.getIdusuariocancelo()>0){
                                        idusuariocancelonota=nota.getIdusuariocancelo();
                                        Personas persona = em.find(Personas.class, idusuariocancelonota);
                                        usuariocancelonota=persona.getApellido().toUpperCase()+" "+persona.getNombre().toUpperCase();
                                        sb.replace(sb.indexOf("ocancelonota>")+13, sb.indexOf("</usuariocan"), usuariocancelonota.toUpperCase());
                                }

        } catch (Exception e) {
            logger.error("Error en metodo devolverNotaProcesadaSB "+e.getMessage());
        }finally{
           
            return sb;
        }
    }

    public String verNotasPedidoPaginadas(int index, int recordCount) {
        String result="<Lista>\n";
        try {
            Query consulta = em.createNativeQuery("SELECT FIRST "+recordCount+" SKIP ("+index+"*"+recordCount+") n.ID  FROM NOTADEPEDIDO n ORDER BY n.ID DESC, n.FECHADECOMPRA DESC", Notadepedido.class);
            List<Notadepedido>lista = consulta.getResultList();

            if(lista.size()==0)
                result="LA CONSULTA NO ARROJÓ RESULTADOS!!!";
            else{
                for (Iterator<Notadepedido> it = lista.iterator(); it.hasNext();) {
                    Notadepedido notape = it.next();
                    result+=devolverNotaProcesadaSB(notape).toString();

                }

            }

        } catch (Exception e) {
            result = "Error en metodo vernotaspedidopaginadas";
            logger.error("Error en metodo vernotasPedidoPaginadas");
        }finally{
        return result+"</Lista>\n";
        }

    }

    public long anularNotaPedido(long idnota, long idusuario, int estado) {
        long result = 0L;
        
        char anulada ='0';
        try {
            //--------------------------------------------------------------------
                GregorianCalendar gc = new GregorianCalendar(Locale.getDefault());
            //--------------------------------------------------------------------

                Notadepedido nota = em.find(Notadepedido.class,idnota);
                Empleados empleado = em.find(Empleados.class,idusuario);
                if(estado==1){
                    anulada ='1';
                    
                    nota.setFechaAnulado(gc.getTime());
                    
                    
                    nota.setIdusuarioAnulado((int)idusuario);
                    nota.setAnulado(Character.valueOf(anulada));
                    

                }else{
                    
                     nota.setAnulado(anulada);
                     nota.setFechaAnulado(null);
                     nota.setIdusuarioAnulado((int)idusuario);
                }

                List<Detallesnotadepedido>lista = nota.getDetallesnotadepedidoList();

                    for (Iterator<Detallesnotadepedido> it = lista.iterator(); it.hasNext();){
                            Detallesnotadepedido detallesnotadepedido = it.next();
                            detallesnotadepedido.setAnulado(Character.valueOf(anulada));
                    }
                Historiconotapedido historico = new Historiconotapedido();

                        if(estado==1){
                                historico.setAccion("Nota anulada"+empleado.getNameuser());
                                historico.setAnulado(anulada);
                                

                        }else{
                                historico.setAccion("NOTa no anulada por "+empleado.getNameuser());
                                historico.setAnulado(anulada);

                        }

                                historico.setIdusuarioanulo((int)idusuario);

                                historico.setAnticipo(BigDecimal.ZERO);

                                historico.setFecharegistro(gc.getTime());
                                historico.setFkidnotapedido(nota);
                                historico.setHoraregistro(gc.getTime());
                                
                                historico.setIdusuarioentrega(0);
                                historico.setIdusuarioexpidio(0);
                                historico.setIdusuariocancelo(0);

                                historico.setPorcentajeaplicado(Short.valueOf("0"));
                                historico.setSaldo(BigDecimal.ZERO);
                                historico.setTotal(BigDecimal.ZERO);
                                historico.setTotalapagar(BigDecimal.ZERO);
                                historico.setRecargo(BigDecimal.ZERO);
                                historico.setPorcrecargo(BigDecimal.ZERO);
                                historico.setPendiente('0');
                                
                                historico.setEntregado('0');
                                historico.setDescuento(BigDecimal.ZERO);
                                historico.setCancelado('0');
                                historico.setPorcentajedesc(BigDecimal.ZERO);


                                em.persist(historico);
                //----------------------------------------------------------------------------------
                            long notaID = procesaListNotaHistorico(nota,historico);
          //----------------------------------------------------------------------------------
                            em.flush();

                       result = notaID;


        } catch (Exception e) {
            logger.error("Error en metodo anularNotaPedido "+e.getCause());
            result = -1;
        }finally{
            return result;
        }
    }

    public long actualizarNotaPedido(String xmlnotapedidomodificada) {
        DatosNotaPedido datosnotapedido;
        long retorno =0L;
        try {
            datosnotapedido=xestreaNotapedido(xmlnotapedidomodificada);

            if(datosnotapedido.getIdnota()>0){
                Notadepedido nota = em.find(Notadepedido.class, datosnotapedido.getIdnota());
                retorno = procesarNotaaActualizar(datosnotapedido,nota);
            }else
                retorno =-2;


        } catch (Exception e) {
            retorno =-1;
            logger.error("Error en metodo actualizarNotapedido", e);
        }finally{
            return retorno;
        }
    }

    private long procesarNotaaActualizar(DatosNotaPedido datosnotapedido, Notadepedido nota) {
        long result =-3;
        try {

              //---------------------------------------------------------------------------------
            GregorianCalendar gc = new GregorianCalendar();
            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
            //---------------------------------------------------------------------------------
            
           
            
                                            nota.setAnulado(datosnotapedido.getAnulado());
                                    

                                            nota.setEnefectivo(datosnotapedido.getEnefectivo());
                                            

                                            nota.setEntregado(Character.valueOf(datosnotapedido.getEntregado()));
                                            

                                            nota.setFkIdcliente(em.find(Personas.class, datosnotapedido.getPersonas().getId()));
                                            

                                            nota.setFkidporcentajenotaId(em.find(Porcentajes.class, datosnotapedido.getPorcentajes().getId_porcentaje()));                      
                                 

                                            nota.setIdTarjetaFk(em.find(TarjetasCreditoDebito.class, datosnotapedido.getTarjetacredito().getId_tarjeta()));
                                            

                                            nota.setIdUsuarioExpidioNota(datosnotapedido.getUsuario_expidio_nota());


                                            nota.setAnticipo(BigDecimal.valueOf(datosnotapedido.getAnticipoacum()));
                        

                                            nota.setIdusuarioAnulado(datosnotapedido.getId_usuario_anulado());
                                            

                                            nota.setIdusuarioEntregado(datosnotapedido.getUsuario_entregado());
                                            

                                            nota.setMontoiva(BigDecimal.valueOf(datosnotapedido.getMontoiva()));
                                            

                                           
                                                nota.setNumerodecupon(datosnotapedido.getNumerodecupon());
                                            

                                            
                                                nota.setObservaciones(datosnotapedido.getObservaciones());
                                            

                                            nota.setPendiente(Character.valueOf(datosnotapedido.getPendiente()));
                                            

                                            nota.setRecargo(BigDecimal.valueOf(datosnotapedido.getRecargo()));
                                            

                                            nota.setSaldo(BigDecimal.valueOf(datosnotapedido.getSaldo()));
                                            

                                            nota.setStockfuturo(datosnotapedido.getStockfuturo());


                                            nota.setTotal(BigDecimal.valueOf(datosnotapedido.getMontototal()));                                     


                                            nota.setFechadecompra(sdf.parse(datosnotapedido.getFechacompra()));
                                           

                                            nota.setFechaentrega(sdf.parse(datosnotapedido.getFechaentrega()));
                                           

                                            nota.setCancelado(Character.valueOf(datosnotapedido.getCancelado()));
                                           

                                            nota.setDescuentonota(BigDecimal.valueOf(datosnotapedido.getDescuentonota()));

                                            nota.setDescuentoPesos(BigDecimal.valueOf(datosnotapedido.getDescuentopesos()));
                        

                                            nota.setIdusuariocancelo(datosnotapedido.getUsuario_cancelo_nota());
                        

                                            try {
                                                nota.setMontototalapagar(BigDecimal.valueOf(datosnotapedido.getMontototalapagar()));
                                             
                                            } catch (Exception ex) {
                                                ex.printStackTrace();
                                            }

                                            
                                            nota.setPorcdesctotal(BigDecimal.valueOf(datosnotapedido.getPorc_descuento_total()));
                                          
                                            nota.setPorcrecargo(BigDecimal.valueOf(datosnotapedido.getPorcentajerecargo()));

                                            if(datosnotapedido.getCancelado()=='1')
                                                nota.setFecancelado(gc.getTime());
                                            if(datosnotapedido.getAnulado()=='1')
                                                nota.setFechaAnulado(gc.getTime());


                                        em.persist(nota);
                                      
                                        

                                  //-------------------DETALLE---------------------------------------------------
                                                List<Itemdetallesnota>lista = datosnotapedido.getDetallesnotapedido().getDetallesnota();
                                                

                                                

                                                
                                                //----------------------------------------------------------------------------------
                                                
                                                Query deletesql = em.createNativeQuery("DELETE FROM DETALLESNOTADEPEDIDO d WHERE d.FK_IDNOTA = "+nota.getId());
                                                deletesql.executeUpdate();
                                                
                                                  em.refresh(nota);
                                                  
                                                  em.flush();
                                                  
                                                 
                                                    for (Iterator<Itemdetallesnota> it = lista.iterator(); it.hasNext();) {
                                                        Itemdetallesnota itemdetallesnota = it.next();

                                                            Detallesnotadepedido detallesnotadepedido = new Detallesnotadepedido();
                                                         
                                                            detallesnotadepedido.setAnulado(itemdetallesnota.getAnulado());
                                                         
                                                            detallesnotadepedido.setCancelado(itemdetallesnota.getCancelado());
                                                            
                                                            detallesnotadepedido.setCantidad(itemdetallesnota.getCantidad());
                                                           
                                                            Productos productos =em.find(Productos.class, itemdetallesnota.getId_producto());
                                                             

                                                            detallesnotadepedido.setDescuento(BigDecimal.valueOf(itemdetallesnota.getDescuento()));
                                                            
                                                            detallesnotadepedido.setDetallesnotadepedidoPK(new DetallesnotadepedidoPK(nota.getId(), itemdetallesnota.getId_producto()));
                                                            
                                                            detallesnotadepedido.setEntregado(Character.valueOf(itemdetallesnota.getEntregado()));
                                                            
                                                            detallesnotadepedido.setIva(BigDecimal.valueOf(itemdetallesnota.getIva()));
                                                            
                                                            detallesnotadepedido.setNotadepedido(nota);
                                                            
                                                            detallesnotadepedido.setPendiente(Character.valueOf(itemdetallesnota.getPendiente()));
                                                           
                                                            detallesnotadepedido.setPrecio(BigDecimal.valueOf(itemdetallesnota.getPrecio()));
                                                            
                                                            detallesnotadepedido.setPreciocondescuento(BigDecimal.valueOf(itemdetallesnota.getPreciocondescuento()));
                                                            
                                                            detallesnotadepedido.setProductos(productos);
                                                             
                                                            detallesnotadepedido.setSubtotal(BigDecimal.valueOf(itemdetallesnota.getSubtotal()));
                                                            

                                                           em.persist(detallesnotadepedido);
                                                           


                                                            Query consulta1 = em.createQuery("SELECT d FROM Detallesnotadepedido d WHERE d.detallesnotadepedidoPK.fkIdproducto = :fkIdproducto");

                                                            consulta1.setParameter("fkIdproducto", itemdetallesnota.getId_producto());


                                                            productos.setDetallesnotadepedidoList(consulta1.getResultList());

                                                        }


                                                    





                                                            

                                                        
                                                         
            Query consulta1 = em.createQuery("SELECT d FROM Detallesnotadepedido d WHERE d.detallesnotadepedidoPK.fkIdnota = :fkIdnota");
            

            consulta1.setParameter("fkIdnota", nota.getId());
          
           


            nota.setDetallesnotadepedidoList(consulta1.getResultList());
            



                    em.persist(nota);
                    em.flush();


                     result =  almacenarhistorico(datosnotapedido,nota);

                  result=nota.getId();
           logger.info("NOTA DE PEDIDO "+result+" ACTUALIZADA POR EMPLEADO "+datosnotapedido.getVendedor());
                                  //-----------------------------------------------------------------------------


        } catch (Exception e) {
            result=-4;
            logger.error("Error en metodo procesarNotaaActualizar "+e.getCause());
        }finally{
            
        return result;
        }
    }

    public String selecNotaEntreFechasEntrega(String fecha1, String fecha2, int idvendedor) {
        String xml="<Lista>\n";
        List<Notadepedido>lista = null;

        try {



            Query jpasql=em.createNativeQuery("SELECT * FROM NOTADEPEDIDO n WHERE CAST(n.FECHAENTREGA as date)  between CAST('"+fecha1+"' as DATE) and cast('"+fecha2+"' as date) and n.entregado=0 and n.pendiente=1 order by n.fechaentrega,n.horacompra,n.id desc", Notadepedido.class);
                lista= jpasql.getResultList();

                if(lista.size()>0){
                    for (Iterator<Notadepedido> it = lista.iterator(); it.hasNext();) {
                             Notadepedido notadepedido1 = it.next();
                             xml+=notadepedido1.toXML();
                    }
                    fecha1=fecha1.substring(3, 5)+"/"+fecha1.substring(0, 2)+"/"+fecha1.substring(6, 10);
                    fecha2=fecha2.substring(3, 5)+"/"+fecha2.substring(0, 2)+"/"+fecha2.substring(6, 10);
                    StringBuilder sb =new StringBuilder(xml);

                    String periodoconsultado = "<fechainicio>"+fecha1+"</fechainicio>\n" +
                            "<fechafinal>"+fecha2+"</fechafinal>\n";

                     sb.replace(sb.indexOf("</numerocupon>")+14, sb.indexOf("<observaciones>"), "\n"+periodoconsultado);
                     xml=sb.toString();


                }else
                    xml+="<result>lista vacia</result>\n";


                    


        } catch (Exception e) {
            xml+="<error>Error</error>\n";
            logger.error("Error en metodo selectnotaEntreFechas",e.fillInStackTrace());
        }finally{



                return xml+="</Lista>\n";
        }
    }

 






   
    
    
 
}
