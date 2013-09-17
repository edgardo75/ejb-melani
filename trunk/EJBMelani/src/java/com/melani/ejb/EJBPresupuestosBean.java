/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.melani.ejb;
import cm.melani.utils.DatosPresupuestos;
import cm.melani.utils.DetallesPresupuesto;
import cm.melani.utils.ItemDetallesPresupuesto;
import com.melani.entity.Detallespresupuesto;
import com.melani.entity.DetallespresupuestoPK;
import com.melani.entity.Presupuestos;
import com.melani.entity.Productos;
import com.thoughtworks.xstream.XStream;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.GregorianCalendar;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Level;
import javax.annotation.Resource;
import javax.ejb.Stateless;
import javax.jws.WebService;
import javax.jws.soap.SOAPBinding;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.sql.DataSource;
import oracle.xml.sql.query.OracleXMLQuery;
import org.apache.commons.lang3.StringEscapeUtils;
import org.apache.log4j.Logger;
/**
 *
 * @author Edgardo
 */
@Stateless(name="ejb/EJBPresupuestosBean")
@WebService(serviceName="ServicesPresupuestos",name="PresupuestoWs")
@SOAPBinding(style=SOAPBinding.Style.RPC)
public class EJBPresupuestosBean implements EJBPresupuestosRemote {
    Logger logger = Logger.getLogger(EJBPresupuestosBean.class);
    @PersistenceContext
    private EntityManager em;
    @Resource(name="jdbc/_melani")
     private DataSource datasource;
    public long addBudgets(String xmlPresupuesto) {
        long retorno =0L;
        try {
            XStream xstream = new XStream();
            xstream.alias("presupuesto", DatosPresupuestos.class);
            xstream.alias("detallepresupuesto", DetallesPresupuesto.class);
            xstream.alias("itemdetallespresupuesto", ItemDetallesPresupuesto.class);
            xstream.addImplicitCollection(DetallesPresupuesto.class, "lista");
            DatosPresupuestos datospresupuesto = (DatosPresupuestos) xstream.fromXML(parsearCaracteresEspecialesXML(xmlPresupuesto).toString());
            retorno = almacenarPresupuesto(datospresupuesto);
        }catch(NullPointerException npe)    {
            retorno =-4;
            logger.error("Error en metodo addBuget en ejbpresupuestos "+npe.getMessage());
        }catch(com.thoughtworks.xstream.io.StreamException ex)    {
            retorno =-3;
            logger.error("Error en el armado del xml en metodo addBudgets "+ex.getMessage());
        } catch (Exception e) {
            retorno =-1;
            logger.error("Error en metodo addBudgets en EJBPresupuestosBean "+e.getMessage());
        }finally{
            return retorno;
        }
    }
    private long almacenarPresupuesto(DatosPresupuestos datospresupuesto) {
        long result = 0L;
        try {
            GregorianCalendar gc = new GregorianCalendar();
            GregorianCalendar fvalidez = new GregorianCalendar();
            fvalidez.add(GregorianCalendar.DATE, 60);
                    Presupuestos presupuesto = new Presupuestos();
                            presupuesto.setTotalapagar(BigDecimal.valueOf(datospresupuesto.getTotalapagar()));
                            presupuesto.setFechapresupuesto(gc.getTime());
                            presupuesto.setApellido(datospresupuesto.getApellido().toUpperCase());
                            presupuesto.setNombre(datospresupuesto.getNombre().toUpperCase());
                            presupuesto.setIva(BigDecimal.valueOf(datospresupuesto.getIva()));
                            presupuesto.setValidez(fvalidez.getTime());
                            presupuesto.setPorcetajedescuentoTOTAL(BigDecimal.valueOf(datospresupuesto.getPorc_descuento_total()));
                            presupuesto.setIdUsuarioFk(datospresupuesto.getId_usuario_expidio());
                            presupuesto.setObservaciones(datospresupuesto.getObservaciones());
                            presupuesto.setTotal(BigDecimal.valueOf(datospresupuesto.getTotal()));
                            presupuesto.setRecargototal(BigDecimal.valueOf(datospresupuesto.getRecargototal()));
                            presupuesto.setPorcentajerecargo(BigDecimal.valueOf(datospresupuesto.getPorcentajerecargo()));
                            presupuesto.setDescuentoresto(BigDecimal.valueOf(datospresupuesto.getDescuentoresto()));
                            presupuesto.setPorcetajedescuentoTOTAL(BigDecimal.valueOf(datospresupuesto.getPorc_descuento_total()));
                            em.persist(presupuesto);
            //++++++++++++++++++++++++++Alamcenar Detalle del Presupuesto+++++++++++++++++++++++++++
            //**************************************************************************************
                List<ItemDetallesPresupuesto>lista = datospresupuesto.getDetallesPresupuesto().getLista();
                for (Iterator<ItemDetallesPresupuesto> it = lista.iterator(); it.hasNext();) {
                        ItemDetallesPresupuesto itemDetallesPresupuesto = it.next();
                        Productos producto = em.find(Productos.class, Long.valueOf(Integer.valueOf(itemDetallesPresupuesto.getFk_id_producto())));
                        DetallespresupuestoPK detpresPK = new DetallespresupuestoPK(presupuesto.getIdPresupuesto(), itemDetallesPresupuesto.getFk_id_producto());
                            Detallespresupuesto detpres = new Detallespresupuesto(detpresPK);
                                detpres.setCantidad(Short.valueOf(itemDetallesPresupuesto.getCantidad()));
                                detpres.setDescuento(BigDecimal.valueOf(itemDetallesPresupuesto.getDescuento()));
                                detpres.setPrecioDesc(BigDecimal.valueOf(itemDetallesPresupuesto.getPrecio_desc()));
                                detpres.setPrecio(BigDecimal.valueOf(itemDetallesPresupuesto.getPrecio()));
                                detpres.setDetallespresupuestoPK(detpresPK);
                                detpres.setPresupuestos(em.find(Presupuestos.class, presupuesto.getIdPresupuesto()));
                                detpres.setProductos(producto);
                                detpres.setSubtotal(BigDecimal.valueOf(itemDetallesPresupuesto.getSubtotal()));
                                em.persist(detpres);
                                Query consultaListProdPres = em.createQuery("SELECT d FROM Detallespresupuesto d WHERE d.detallespresupuestoPK.fkProducto = :fkProducto");
                                    consultaListProdPres.setParameter("fkProducto", itemDetallesPresupuesto.getFk_id_producto());
                                producto.setDetallepresupuestosList(consultaListProdPres.getResultList());
                  }
                        Query consulta1 = em.createQuery("SELECT d FROM Detallespresupuesto d WHERE d.detallespresupuestoPK.idDpFk = :idDpFk");
                        consulta1.setParameter("idDpFk", presupuesto.getIdPresupuesto());
                        presupuesto.setDetallepresupuestosList(consulta1.getResultList());
                        em.merge(presupuesto);
                        result=Long.valueOf(Integer.valueOf(presupuesto.getIdPresupuesto()));
        } catch (Exception e) {
            result=-2;
            logger.error("Error en método almacenarPresupuesto EJBPresupuesto "+e.getMessage());
        }finally{
            return result;
        }
    }
    public String selectAllPresupuestosJPA() {
        String xmlpresupuesto="<Lista>\n";
        try {
            Query consulta  = em.createNativeQuery("SELECT * FROM PRESUPUESTOS p order by p.FECHAPRESUPUESTO desc ,p.ID_PRESUPUESTO desc", Presupuestos.class);
            List<Presupuestos>lista = consulta.getResultList();
            if(lista.size()==0)
                xmlpresupuesto="LA CONSULTA NO ARROJÓ RESULTADOS!!!";
            else{
                    for (Iterator<Presupuestos> it = lista.iterator(); it.hasNext();) {
                        Presupuestos presupuestos = it.next();
                        xmlpresupuesto+=presupuestos.toXML();
                    }
                    xmlpresupuesto+="</Lista>\n";
            }
        } catch (Exception e) {
            xmlpresupuesto="Error";
            logger.error("Error en metodo selectAllPresupuestoJPA "+e.getMessage());
        }finally{
            return xmlpresupuesto;
        }
    }
    public Integer getRecordCount() {
        int retorno =0;
        try {
            Query presupuesto = em.createNamedQuery("Presupuestos.findAll");
            retorno =presupuesto.getResultList().size();
        } catch (Exception e) {
            retorno=-1;
            logger.error("Error en getRecordCount en EJBPresupuestosBean", e.getCause());
        }finally{
            return retorno;
        }
    }
    public String selectPresupuestoOfTheDay(){
        String presupuesto="<Lista>\n";
        try {
            Query consulta = em.createNativeQuery(" SELECT * FROM PRESUPUESTOS p WHERE p.FECHAPRESUPUESTO='today'", Presupuestos.class);
            List<Presupuestos>lista = consulta.getResultList();
            if(lista.size()==0)
                presupuesto="LA CONSULTA NO ARROJÓ RESULTADOS!!!";
            else{
                    for (Iterator<Presupuestos> it = lista.iterator(); it.hasNext();) {
                         Presupuestos presupuestoss = it.next();
                         presupuesto+=presupuestoss.toXML();
                    }
            }
            presupuesto+="</Lista>\n";
        } catch (Exception e) {
            presupuesto="Error";
            logger.error("Error en selectPresupuestoPaging en EJBPresupuestosBean"+e.getMessage());
        }finally{
         return presupuesto;
        }
    }
    public String verPresupuestosPaginados(Integer index, Integer record) {
        String result ="<Lista>\n";
        try {
            Query paging = em.createNativeQuery("SELECT FIRST "+record+" SKIP ("+index+"*"+record+") p.ID_PRESUPUESTO  FROM PRESUPUESTOS p order by p.FECHAPRESUPUESTO desc ,p.ID_PRESUPUESTO desc", Presupuestos.class);
            List<Presupuestos>lista = paging.getResultList();
            if(lista.size()==0)
                result="LA CONSULTA NO ARROJÓ RESULTADOS!!!";
            else{
                for (Iterator<Presupuestos> it = lista.iterator(); it.hasNext();) {
                    Presupuestos presupuestos = it.next();
                    result+=presupuestos.toXML();
                }
                result +="</Lista>\n";
            }
        } catch (Exception e) {
            result="Error";
            logger.error("Error en metodo verPresupuestos en EBPresupuestosBean "+e.getMessage());
        }finally{
            return result;
        }
    }
    public String searchOneBudget(int idpresupuesto) {
        String result ="<Lista>\n";
        try {
            Query paging = em.createNativeQuery("SELECT * FROM PRESUPUESTOS p WHERE p.ID_PRESUPUESTO ="+idpresupuesto+" order by p.FECHAPRESUPUESTO desc ,p.ID_PRESUPUESTO desc", Presupuestos.class);
            paging.setParameter("idpresupuesto", idpresupuesto);
            List<Presupuestos>lista = paging.getResultList();
            if(lista.size()==0)
                result="LA CONSULTA NO ARROJÓ RESULTADOS!!!";
            else{
                for (Iterator<Presupuestos> it = lista.iterator(); it.hasNext();) {
                    Presupuestos presupuestos = it.next();
                    result+=presupuestos.toXML();
                }
                result +="</Lista>\n";
            }
        } catch (Exception e) {
            result="Error";
            logger.error("Error en metodo verPresupuestos en EBPresupuestosBean "+e.getMessage());
        }finally{
            return result;
        }
    }
    //-----------------------------------------------------------------------------------------------------
    //-----------------------------------------------------------------------------------------------------
public StringBuilder parsearCaracteresEspecialesXML(String xmlPresupuesto){
    String xml = "No paso Nada";
    StringBuilder sb=null;
    try {
        sb=new StringBuilder(xmlPresupuesto);
            xml=StringEscapeUtils.escapeXml(xmlPresupuesto.substring(xmlPresupuesto.indexOf("es>")+3,xmlPresupuesto.indexOf("</ob")));
            sb.replace(sb.indexOf("es>")+3, sb.indexOf("</ob"), xml);
    } catch (Exception e) {
        xml = "Error";
        logger.error("Error en metodo parsearCaracteresEspecialesXML ",e.getCause());
    }finally{
    return sb;
    }
}
    public String ShowReportPresupuesto(Integer idPresupuesto) {
        String xml = "";
        OracleXMLQuery oxq = null;
        Connection con = null;
        try {
            try {
                        con = datasource.getConnection();
                    } catch (Exception e) {
                        logger.error("No se pudo Obtener La Conexion con La base de Datos en metodo searchallbarrios"+e);
                        xml = "No se pudo Obtener La Conexion con La base de Datos";
                    }
                String sql = " SELECT * FROM PRESUPUESTOS p join DETALLESPRESUPUESTO d ON d.ID_DP_FK=p.ID_PRESUPUESTO JOIN PRODUCTOS pro on pro.SID = d.FK_PRODUCTO WHERE p.ID_PRESUPUESTO = "+idPresupuesto;
                //----------------------------------------------------------------------------
                        oxq = new OracleXMLQuery(con, sql);
                        oxq.setRowTag("Item");
                        oxq.setRowsetTag("Lista");
                        oxq.setEncoding("ISO-8859-1");
                        oxq.setDateFormat("dd/MM/yyyy");
                        xml = oxq.getXMLString();
                        oxq.close();
        } catch (Exception e) {
            logger.error("Error en metodo ShowReportPresupuesto");
        }finally{
            try {
                        if (con != null) {
                            con.close();
                        }
                        if (oxq != null) {
                            oxq.close();
                        }
                    } catch (SQLException ex) {
                        logger.error("Error al Cerrar las Conexiones en Servlet ShowReportPresupuesto WebMelani Proyect "+ex);
                    }
        return xml;
        }
    }
    public String ShowReportVerPresupuesto(Long first, Long last) {
        OracleXMLQuery oxq = null;
        Connection con = null;
        String xml = "";
        try {
            try {
                        con = datasource.getConnection();
                    } catch (Exception e) {
                        xml = "No se pudo Obtener La Conexion con La base de Datos";
                    }
            String sql =   "SELECT * FROM PRESUPUESTOS p JOIN DETALLESPRESUPUESTO d on p.ID_PRESUPUESTO=d.ID_DP_FK JOIN PRODUCTOS pr on pr.SID = d.FK_PRODUCTO WHERE p.ID_PRESUPUESTO BETWEEN "+first+" and "+last+" order by p.FECHAPRESUPUESTO desc ,p.ID_PRESUPUESTO desc";
             //----------------------------------------------------------------------------
                        oxq = new OracleXMLQuery(con, sql);
                        oxq.setRowTag("Item");
                        oxq.setRowsetTag("Lista");
                        oxq.setEncoding("ISO-8859-1");
                        oxq.setDateFormat("dd/MM/yyyy");
                        xml = oxq.getXMLString();
                        oxq.close();
        } catch (Exception e) {
            logger.error("Error en metodo ShowReportVerPresupuesto");
        }finally{
            try {
                if (con != null) {
                    con.close();
                }
                if (oxq != null) {
                    oxq.close();
                }
            } catch (SQLException ex) {
                java.util.logging.Logger.getLogger(EJBPresupuestosBean.class.getName()).log(Level.SEVERE, null, ex);
            }
            return xml;
        }
    }
}
