/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.melani.ejb;



import cm.melani.utils.DatosProductos;
import com.melani.entity.ExistenciasProductos;
import com.melani.entity.Productos;
import com.thoughtworks.xstream.XStream;
import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.sql.Connection;

import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.GregorianCalendar;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import javax.annotation.Resource;
import javax.ejb.Stateless;
import javax.jws.WebService;
import javax.jws.soap.SOAPBinding;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.sql.DataSource;
import oracle.xml.sql.query.OracleXMLQuery;
import org.apache.log4j.Logger;
/**
 *
 * @author Edgardo
 */
@Stateless(name="ejb/EJBProductos")
@WebService(serviceName="ServiceProductos",name="ProductosWs")
@SOAPBinding(style=SOAPBinding.Style.RPC)
public class EJBProductos implements EJBProductosRemote {
    private static Logger logger = Logger.getLogger(EJBProductos.class);
    @PersistenceContext(unitName="EJBMelaniPU2")    
    private EntityManager em;
    @Resource(name = "jdbc/_melani")
    private DataSource datasource;
    //--------------------------------------------------------------------------------------------------
    public long addProductoConImage(String nombre, int cantini, String cantactual, float precio) {
        long retorno =0;
        //Image image = null;
        InputStream is = null;
        Productos producto = null;
        try {

            // Read from an input stream
             String pathActual = System.getProperty("user.dir") + File.separatorChar + "Imagen" + File.separatorChar;
           
                is = new BufferedInputStream(
                        new FileInputStream(pathActual+"Megan-Fox-Wallpapers-WwW.LoMasInteresante.NeT.jpg"));
                       // new FileInputStream(pathActual+"Hola.jpg"));
                       // image = ImageIO.read(is);
                //BufferedImage imagin = ImageIO.read(is);
           
             // if(is.available()>65535)
               
              //else{
                
                int formDataLength = is.available();
                
                
                byte[] dataBytes = new byte[formDataLength];
                        int byteRead = 0;
                            int totalBytesRead = 0;
                    while (totalBytesRead < formDataLength) {
                        byteRead = is.read(dataBytes, totalBytesRead, formDataLength);
                        totalBytesRead += byteRead;
                    }

               

               retorno = agregarProducto (producto,null);
                
   

            

        } catch (Exception e) {
            retorno = -1;
            logger.error("Error en metodo addProductoConImage ejbProductos "+e);
        }finally{
            try {
                if(is!=null)
                    is.close();
                
            } catch (IOException ex) {
                logger.error("error cerrando conexiones!!!"+ex);
            }
            return retorno;

        }
    }
//------------------------------------------------------------------------------------------------------

    private static byte[] writtingImage(String fileLocation) throws IOException {
     
      byte[] dataBytes = null;
      InputStream is = null;
        try {
            is = new BufferedInputStream(
                        new FileInputStream("Megan-Fox-Wallpapers-WwW.LoMasInteresante.NeT.jpg"));
                       // image = ImageIO.read(is);
                //BufferedImage imagin = ImageIO.read(is);
     

                int formDataLength = is.available();


                dataBytes = new byte[formDataLength];
                        int byteRead = 0;
                            int totalBytesRead = 0;
                    while (totalBytesRead < formDataLength) {
                        byteRead = is.read(dataBytes, totalBytesRead, formDataLength);
                        totalBytesRead += byteRead;
                    }

        } catch (IOException e) {
            e.getMessage();
        }
      is.close();
        return dataBytes;
    }
//-----------------------------------------------------------------------------------------------------
    public long addExistenciasProducto(int idproducto, int cantidad,float precio,int idusuario) {
        long retorno = 0;
        try {

            GregorianCalendar gc = new GregorianCalendar(Locale.getDefault());
            

            Productos producto =em.find(Productos.class,(long) idproducto);
     
            producto.setCantidadDisponible(BigInteger.valueOf(producto.getCantidadDisponible().intValue()+cantidad));
     
            ExistenciasProductos existencias = new ExistenciasProductos();
     
            existencias.setCantidadactual(cantidad);
     
            existencias.setCantidadinicial(0);

            existencias.setIdUsuario(idusuario);
     
            existencias.setFechaagregado(gc.getTime());
     
            existencias.setProductos(em.find(Productos.class, producto.getSid()));

            if(precio!=0){
                producto.setPrecioUnitario(BigDecimal.valueOf(precio));
                existencias.setPreciounitario(BigDecimal.valueOf(precio));
            }else
                existencias.setPreciounitario(BigDecimal.valueOf(0));
            
            Query consulta = em.createQuery("SELECT e FROM ExistenciasProductos e WHERE e.productos.sid = :sid");
            consulta.setParameter("sid", producto.getSid());

            List<ExistenciasProductos>lista = consulta.getResultList();

            producto.setExistenciasProductoss(lista);
      
            retorno = producto.getSid();

            em.merge(producto);
            em.persist(existencias);

        } catch (Exception e) {
            retorno = -1;
            logger.error("Error en metodo addExistenciasProducto, ejbproductos "+e);
        }finally{
            return retorno;
        }
    }

    public String leerImagenBaseDatos(int idProducto) {
        String result="NADA";
        ByteArrayInputStream is = null;
        FileOutputStream fos = null;
        try {
            String pathActual = System.getProperty("user.dir") + File.separatorChar + "Imagen" + File.separatorChar;
            Productos producto = em.find(Productos.class,(long) idProducto);
            File file = new File(pathActual+"faro.jpg");
            fos = new FileOutputStream(file);
            byte[] buffer= producto.getImg();
            is = new ByteArrayInputStream(buffer);
            //----------------------------------------------------------------------------------
            while (is.read(buffer) > 0) {
              fos.write(buffer);
            }
            //----------------------------------------------------------------------------------
            result = "LEIDO";


        } catch (Exception e) {
            result = "ERROR";
            e.getMessage();
        }finally{
           try{
               if(fos!=null)
                fos.close();
               if(is!=null)
                    is.close();

           }catch(Exception e){
                logger.error("Error leyendo imagen leerImagenBaseDatos, en ejbproductos "+e);
            }finally{
                return result;
           }
        }
    }



    public String addProducto(String xmlProducto) {
        String retorno = "0L";
        Productos producto = null;
        long idproduct;
        try {

           
        idproduct =agregarProducto(producto, xmlProducto);

        if(idproduct>0){
            retorno=searchAllProductos();
        }else{
            retorno="<Lista>\n" +
                    "<producto>\n" +
                    "<id>"+idproduct+"</id>\n" +
                    "</producto>\n"+
                    "</Lista>\n";
        }




        } catch (Exception e) {
            logger.error("Error en metodo addProducto "+e);
        }finally{
        return retorno;
        }
    }

    private long agregarProducto(Productos producto, String xmlProducto) {
        long retorno = 0L;
        try {

            //-----------------------------------------------------------------------
             XStream xstream = new XStream();
                xstream.alias("producto", DatosProductos.class);
                DatosProductos datosprod = (DatosProductos) xstream.fromXML(xmlProducto);
            //-----------------------------------------------------------------------
                GregorianCalendar calendario = new GregorianCalendar(Locale.getDefault());
            //-----------------------------------------------------------------------
                producto =em.find(Productos.class, datosprod.getIdproducto());

                Query consulta1 = em.createNativeQuery("SELECT * FROM PRODUCTOS WHERE LOWER(PRODUCTOS.CODPRODUCTO) LIKE LOWER('"+datosprod.getCodproducto()+"%')");
                Query consulta = em.createNativeQuery("SELECT * FROM PRODUCTOS WHERE LOWER(PRODUCTOS.DESCRIPCION) LIKE LOWER('"+datosprod.getDescripcion()+"%')");

                    if(consulta1.getResultList().isEmpty()){
                        if(consulta.getResultList().isEmpty()){

                            if(producto==null){
                            //----------------------------Producto Nuevo-------------------------------------------
                                    producto = new Productos();

                                    producto.setCantidadDisponible(BigInteger.valueOf(datosprod.getCantidaddisponible()));

                                    producto.setCantidadInicial(BigInteger.valueOf(datosprod.getCantidadinicial()));
                                
                                    producto.setPrecioUnitario(BigDecimal.valueOf(datosprod.getPreciounitario()));

                                    producto.setDescripcion(datosprod.getDescripcion().toUpperCase());

                                    producto.setFecha(calendario.getTime());

                                    producto.setCodproducto(datosprod.getCodproducto().toUpperCase());

                                    em.persist(producto);

                                        ExistenciasProductos existencias = new ExistenciasProductos();

                                        existencias.setCantidadactual(Integer.valueOf(datosprod.getCantidaddisponible()));
                                        existencias.setCantidadinicial(datosprod.getCantidadinicial());
                                        existencias.setFechaagregado(calendario.getTime());
                                        existencias.setPreciounitario(BigDecimal.valueOf(datosprod.getPreciounitario()));
                                        existencias.setIdUsuario(datosprod.getIdusuario());
                                        existencias.setProductos(em.find(Productos.class, producto.getSid()));
                                        em.persist(existencias);

                                    retorno = existencias(producto);
                        //-----------------------------------------------------------------------

            }else{
                            if(producto.getCantidadDisponible().intValue()!=datosprod.getCantidaddisponible()&&producto.getPrecioUnitario()!=BigDecimal.valueOf(datosprod.getPreciounitario())){
                //--------------------------------Actualizo Producto Los CamposNecesarios-------------------------------
                                        producto.setCantidadDisponible(BigInteger.valueOf(producto.getCantidadDisponible().intValue()+datosprod.getCantidaddisponible()));
                                        producto.setPrecioUnitario(BigDecimal.valueOf(datosprod.getPreciounitario()));
                                        producto.setDescripcion(datosprod.getDescripcion().toUpperCase());
                                        producto.setCodproducto(datosprod.getCodproducto());
                                        em.merge(producto);
                //---------------------------------------------------------------------------------
                                        ExistenciasProductos existencias = new ExistenciasProductos();
                                        existencias.setCantidadactual(datosprod.getCantidaddisponible());
                                        existencias.setCantidadinicial(0);
                                        existencias.setFechaagregado(calendario.getTime());
                                        existencias.setPreciounitario(BigDecimal.valueOf(datosprod.getPreciounitario()));
                                        existencias.setProductos(em.find(Productos.class, producto.getSid()));
                                        em.persist(existencias);
                //---------------------------------------------------------------------------------
                                    retorno = existencias(producto);
                //---------------------------------------------------------------------------------
                            }else
                                retorno = producto.getSid();

            }
            }else
                retorno = -5;
            }else
                retorno = -6;

        } catch (Exception e) {
            retorno =-2;
            logger.error("Error en metodo agregarProducto, ejbProducto "+e);
        }finally{
            return retorno;
        }
    }

    private long existencias(Productos producto) {
        long retorno = 0L;
        try {
            Query consulta = em.createQuery("SELECT e FROM ExistenciasProductos e WHERE e.productos.sid = :sid");

                consulta.setParameter("sid", producto.getSid());

                List<ExistenciasProductos>lista = consulta.getResultList();
                producto.setExistenciasProductoss(lista);
                em.merge(producto);
                retorno = producto.getSid();
        } catch (Exception e) {
            retorno =-1;
            logger.error("error en metodo existencias ejbProductos "+e);
        }finally{
            return retorno;
        }
    }

    public String selectoneproducto(long idproducto) {
        String result = "NADA";
        try {
            Productos producto = em.find(Productos.class, idproducto);

            result = producto.toXML();

        } catch (Exception e) {
            result = "ERROR";
            logger.error("Error en metodo selectoneproducto "+e);
        }finally{
        
        return result;
        }
    }

    public Productos agregarProductos(Productos producto) {
        try {
            GregorianCalendar calendario = new GregorianCalendar(Locale.getDefault());
            Productos produ = em.find(Productos.class, producto.getSid());

            if(produ!=null){
              produ.setFecha(calendario.getTime());
              em.persist(producto);
            }else
            {
              produ.setFecha(calendario.getTime());
              em.merge(producto);

            }

        } catch (Exception e) {
            logger.error("Error en metodo addProductos "+e);
        }finally{
        return producto;
        }

    }

    public String searchAllProductos() {
        String xml = "NADA";
        Connection con =null;
        OracleXMLQuery oxq = null;
        try {
          /*  try {
                con = datasource.getConnection();

            } catch (Exception e) {
                logger.error("Error al conectar a base de datos", e);
            }
            String consulta = "SELECT p.sid,p.DESCRIPCION,p.codproducto,p.PRECIOUNITARIO,p.CANTIDADINICIAL,p.CANTIDADDISPONIBLE" +
                    ",p.FECHA FROM PRODUCTOS p Order by p.sid";

            oxq = new OracleXMLQuery(con, consulta);

            oxq.setRowTag("producto");
            oxq.setRowsetTag("Lista");
            oxq.setEncoding("ISO-8859-1");
            oxq.setDateFormat("dd/MM/yyyy");
            xml = oxq.getXMLString();
            oxq.close();

            
            if (xml.contains("<Lista/>")) {
                xml = "La Consulta no arrojó resultados!!!";
            }*/
            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
            Query query = em.createNativeQuery("SELECT p.sid,p.DESCRIPCION,p.codproducto,p.PRECIOUNITARIO,p.CANTIDADINICIAL,p.CANTIDADDISPONIBLE" +
                    ",p.FECHA FROM PRODUCTOS p Order by p.sid", Productos.class);
            List<Productos> lista = query.getResultList();
                    if(lista.isEmpty())
                        xml="LA CONSULTA NO ARROJÓ RESULTADOS";
                    else{
                        Iterator iter = lista.iterator();
                        xml="<Lista>\n";
                        while(iter.hasNext()){
                            Productos prod = (Productos) iter.next();
                            xml+="<producto>\n"
                                    + "<id>"+prod.getSid()+"</id>\n"
                                    + "<idproduct>"+prod.getCodproducto()+"</idproduct>\n"
                                    + "<descripcion>"+prod.getDescripcion()+"</descripcion>\n"
                                    + "<cantidadDisponible>"+prod.getCantidadDisponible()+"</cantidadDisponible>\n"
                                    + "<cantidadInicial>"+prod.getCantidadInicial()+"</cantidadInicial>\n"
                                    + "<fecha>"+sdf.format(prod.getFecha())+"</fecha>\n" 
                                    +"<precio>"+prod.getPrecioUnitario()+"</precio>\n"
                                    + "</producto>\n";
                        
                        
                        }
                        xml+="</Lista>\n";

                    }


        } catch (Exception e) {
            logger.error("Error al buscar todos los producto EJBProducto", e);
        }finally{
            /*try {
                if (con != null) {
                    con.close();
                }
                if (oxq != null) {
                    oxq.close();
                }
                
            } catch (SQLException ex) {
               logger.error("Error a cerrar conexiones EJBProducto", ex);
            }*/
            
            return xml;

        }
    }

    public int controlStockProducto(long idProducto, int cantidad, int idUsuario) {
        int resultado = 0;
        try {

           
                        GregorianCalendar gc = new GregorianCalendar(Locale.getDefault());

                        Productos producto = em.find(Productos.class, idProducto);
                        producto.setCantidadDisponible(producto.getCantidadDisponible().subtract(BigInteger.valueOf(cantidad)));
     
                        //-----------------------------------------------------------------------------------------------------
                            ExistenciasProductos existencias = new ExistenciasProductos();

                                    existencias.setCantidadactual(-cantidad);
                                    existencias.setCantidadinicial(0);
                                    existencias.setFechaagregado(gc.getTime());
                                    existencias.setIdUsuario(idUsuario);
                                    existencias.setPreciounitario(producto.getPrecioUnitario());
                                    existencias.setProductos(em.find(Productos.class, idProducto));
                                    em.persist(existencias);


                               Query consulta = em.createQuery("SELECT e FROM ExistenciasProductos e WHERE e.productos.sid = :idproducto");
                               consulta.setParameter("idproducto", producto.getSid());
                               List<ExistenciasProductos>lista = consulta.getResultList();

                                   producto.setExistenciasProductoss(lista);
                               em.persist(producto);
                                    


                                    resultado = (int) existencias(producto);

        } catch (Exception e) {
            logger.error("Error en metodo controlStockProducto", e.getCause());
            resultado = 1;
        } finally {
            return resultado;
        }

    }

    public String actualizarProducto(String xmlProducto) {
          String retorno = "0L";
        Productos producto = null;
        long idproduct;
        try {


        idproduct =updateProducto(producto, xmlProducto);

        
            retorno="<Lista>\n" +
                    "<producto>\n" +
                    "<id>"+idproduct+"</id>\n" +
                    "</producto>\n"+
                    "</Lista>\n";
        




        } catch (Exception e) {
            logger.error("Error en metodo addProducto "+e);
        }finally{
        return retorno;
        }
    }

    private long updateProducto(Productos producto, String xmlProducto) {
         long retorno = 0L;
        try {
            
            //-----------------------------------------------------------------------
             XStream xstream = new XStream();
            
                xstream.alias("producto", DatosProductos.class);
                DatosProductos datosprod = (DatosProductos) xstream.fromXML(xmlProducto);
            //-----------------------------------------------------------------------
                GregorianCalendar calendario = new GregorianCalendar(Locale.getDefault());
            //-----------------------------------------------------------------------
            
                if(datosprod.getIdproducto()>0)
                    producto =em.find(Productos.class, datosprod.getIdproducto());

                   

                        Query consulta1 = em.createNativeQuery("SELECT * FROM PRODUCTOS WHERE LOWER(PRODUCTOS.CODPRODUCTO) LIKE LOWER('"+datosprod.getCodproducto()+"%')");
                        Query consulta = em.createNativeQuery("SELECT * FROM PRODUCTOS WHERE LOWER(PRODUCTOS.DESCRIPCION) LIKE LOWER('"+datosprod.getDescripcion()+"%')");
            


                            if(producto==null){
                            //----------------------------Producto Nuevo-------------------------------------------
            
                                    producto = new Productos();

                                    producto.setCantidadDisponible(BigInteger.valueOf(datosprod.getCantidaddisponible()));

                                    producto.setCantidadInicial(BigInteger.valueOf(datosprod.getCantidadinicial()));

                                    producto.setPrecioUnitario(BigDecimal.valueOf(datosprod.getPreciounitario()));

                                    if(consulta.getResultList().isEmpty())
                                        producto.setDescripcion(datosprod.getDescripcion().toUpperCase());

                                    producto.setFecha(calendario.getTime());

                                    if(consulta1.getResultList().isEmpty())
                                        producto.setCodproducto(datosprod.getCodproducto().toUpperCase());

                                    em.persist(producto);

                                        ExistenciasProductos existencias = new ExistenciasProductos();

                                        existencias.setCantidadactual(Integer.valueOf(datosprod.getCantidaddisponible()));
                                        existencias.setCantidadinicial(datosprod.getCantidadinicial());
                                        existencias.setFechaagregado(calendario.getTime());
                                        existencias.setPreciounitario(BigDecimal.valueOf(datosprod.getPreciounitario()));
                                        existencias.setIdUsuario(datosprod.getIdusuario());
                                        existencias.setProductos(em.find(Productos.class, producto.getSid()));
                                        em.persist(existencias);

                                    retorno = existencias(producto);
            
                        //-----------------------------------------------------------------------

                         }else{
                            if(producto.getCantidadDisponible().intValue()!=datosprod.getCantidaddisponible()||producto.getPrecioUnitario()!=BigDecimal.valueOf(datosprod.getPreciounitario())){
                                
                //--------------------------------Actualizo Producto Los CamposNecesarios-------------------------------
                                        producto.setCantidadDisponible(BigInteger.valueOf(producto.getCantidadDisponible().intValue()+datosprod.getCantidaddisponible()));
                                        producto.setPrecioUnitario(BigDecimal.valueOf(datosprod.getPreciounitario()));
                                        if(consulta.getResultList().isEmpty())
                                            producto.setDescripcion(datosprod.getDescripcion().toUpperCase());
                                        if(consulta1.getResultList().isEmpty())
                                            producto.setCodproducto(datosprod.getCodproducto());
                                        em.persist(producto);
                //---------------------------------------------------------------------------------
                                        ExistenciasProductos existencias = new ExistenciasProductos();
                                        existencias.setCantidadactual(datosprod.getCantidaddisponible());
                                        existencias.setCantidadinicial(0);
                                        existencias.setFechaagregado(calendario.getTime());
                                        existencias.setPreciounitario(BigDecimal.valueOf(datosprod.getPreciounitario()));
                                        existencias.setProductos(em.find(Productos.class, producto.getSid()));
                                        em.persist(existencias);
                //---------------------------------------------------------------------------------
                                    retorno = existencias(producto);
            
                //---------------------------------------------------------------------------------
                            }else
                                retorno = producto.getSid();

            }


        } catch (Exception e) {
            retorno =-2;
            logger.error("Error en metodo updateProducto, ejbProducto "+e);
        }finally{
            return retorno;
        }
    }

    public String ShowReportProduct() {

        String xml = "";
        OracleXMLQuery oxq = null;
        Connection con = null;
        try {
             try {
                con = datasource.getConnection();

            } catch (Exception e) {
                logger.error("Error al conectar a base de datos", e);
            }
            String consulta = "SELECT p.sid,p.DESCRIPCION,p.codproducto,p.PRECIOUNITARIO,p.CANTIDADINICIAL,p.CANTIDADDISPONIBLE" +
                    ",p.FECHA FROM PRODUCTOS p Order by p.sid";

            oxq = new OracleXMLQuery(con, consulta);

            oxq.setRowTag("producto");
            oxq.setRowsetTag("Lista");
            oxq.setEncoding("ISO-8859-1");
            oxq.setDateFormat("dd/MM/yyyy");
            xml = oxq.getXMLString();
            oxq.close();
    }catch(Exception e){
            logger.error("Error en metodo ShowReportProduct");

    }finally{
        try {
                        if (con != null) {

                                con.close();

                        }
                        if (oxq != null) {
                            oxq.close();
                        }
                 } catch (SQLException ex) {
                        logger.error("error al cerrar conexion", ex);
                    }
        return xml;
    }
    }
//---------------------------------------------------------------------------------------------------

//---------------------------------------------------------------------------------------------------




 
}
