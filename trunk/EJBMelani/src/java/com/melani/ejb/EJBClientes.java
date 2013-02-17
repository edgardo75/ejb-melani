/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.melani.ejb;

import cm.melani.utils.ClienteDomicilioTelefono;
import cm.melani.utils.DatosCliente;
import cm.melani.utils.DatosDomicilios;
import cm.melani.utils.DatosTelefonos;
import cm.melani.utils.ListaTelefonos;
import com.melani.entity.Clientes;
import com.melani.entity.Domicilios;
import com.melani.entity.Generos;
import com.melani.entity.HistoricoDatosClientes;
import com.melani.entity.Personas;
import com.melani.entity.PersonasDomicilios;

import com.melani.entity.Personastelefonos;
import com.melani.entity.Telefonos;
import com.melani.entity.TelefonosPK;
import com.melani.entity.Tiposdocumento;
import com.thoughtworks.xstream.XStream;
import java.math.BigDecimal;
import java.math.BigInteger;



import java.sql.Connection;
import java.sql.SQLException;
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

import javax.sql.DataSource;
import oracle.xml.sql.query.OracleXMLQuery;
import org.apache.commons.lang3.StringEscapeUtils;
import org.apache.log4j.Logger;


/**
 *
 * @author Edgardo
 */
@Stateless(name="ejb/EJBClientes")
@WebService(serviceName="ServiceClientes",name="ClientesWs")
@SOAPBinding(style=SOAPBinding.Style.RPC)
public class EJBClientes implements EJBClientesRemote {
    private static Logger logger = Logger.getLogger(EJBClientes.class);
    @PersistenceContext(unitName="EJBMelaniPU2")
    private EntityManager em;
   
    @EJB
    EJBDomiciliosRemote ejbdomici;
    @EJB
    EJBTelefonosRemote ejbtele;
    @EJB
    EJBClienteDomicilioRemote ejbclidom;
    @EJB
    EJBClienteTelefonoRemote ejbclitel;
    

    private long retornoemail=0L;

    //----------------------------------------------------------------------------
    @Resource(name = "jdbc/_melani")
   private DataSource datasource;


    //------------------------------------------------------------------------------------------------------
    public long addDatosCliente(String apellido, String nombre, short idtipo, int numerodocu, String email, String observaciones,short idGenero) {

          long retorno =0L;
          
        try {             
            
           
              GregorianCalendar calendario = new GregorianCalendar(Locale.getDefault());
            //----------------------------------------------------------------------------------------
            Clientes cliente = new Clientes();
             
            cliente.setApellido(apellido);
             
            cliente.setNombre(nombre);
             
            cliente.setEmail(email);
             
            cliente.setFechaCarga(calendario.getTime());
             
            cliente.setNrodocumento(numerodocu);
             
            cliente.setTipodocumento(em.find(Tiposdocumento.class, idtipo));
             
            cliente.setObservaciones(observaciones);
             
            cliente.setTotalCompras(BigDecimal.ZERO);
             
            cliente.setTotalEnPuntos(BigInteger.ZERO);

            cliente.setGeneros(em.find(Generos.class, idGenero));


            em.persist(cliente);
            em.flush();

            //----------------------------------------------------------------------------------------

            retorno = cliente.getIdPersona();

            


        } catch (Exception e) {
            retorno = -1;
            e.getMessage();
          
        }finally{          
            return retorno;
        }
    }

//-----------------------------------------------------------------------------------------------------
    public long addCliente(String xmlClienteDomicilioTelefono) {
        long retorno =0L;
        long idcliente=0L;

        

        try {
            //----------------------------------------------------------------------------------------


          
                    XStream  xstream = new XStream();
                    
                    xstream.alias("ClienteDomicilioTelefono",ClienteDomicilioTelefono.class);
                    
                    xstream.alias("item", DatosCliente.class);
                    
                    //---------------------------------------------------------------------
                    if(xmlClienteDomicilioTelefono.contains("<Domicilio>"))
                            xstream.alias("Domicilio", DatosDomicilios.class);
                    
                    //---------------------------------------------------------------------
                    if(xmlClienteDomicilioTelefono.contains("<telefono>")){
                        xstream.alias("listaTelefonos", ListaTelefonos.class);
                        xstream.alias("telefono", DatosTelefonos.class);
                        xstream.addImplicitCollection(ListaTelefonos.class,"list");
                    }
                    
            
            //------------------------------------------------------------------------------------------
            ClienteDomicilioTelefono todosDatos = (ClienteDomicilioTelefono) xstream.fromXML(parsearCaracteresEspecialesXML1(xmlClienteDomicilioTelefono));
                   
            
            //------------------------------------------------------------------------------------------
             DatosCliente datosClientePersonales =todosDatos.getCliente();
           //------------------------------------------------------------------------------------------
            ///++++++++++++++++++++++++++++++++++++Primero Chequeo el email y luego la persona si existene en la base de datos
             retornoemail = chequearemail(datosClientePersonales.getEmail(),datosClientePersonales.getNrodocu());

             switch((int)retornoemail){
                
                 case -7:{logger.error("Error en metodo chequear email");
                 retorno=retornoemail;
                 break;
                 }
                 case -8:{logger.error("Email encontrado en metodo chequearemail");
                 retorno=retornoemail;
                  break;
                 }
                 default:{
                 
           
            
               
                        //*****************************************************************************++++
                                      idcliente= existe_cliente(datosClientePersonales.getNrodocu());

                                switch((int)idcliente){
                                    case 0:{
                                                //------agrego el cliente y todos sus datos desde cero
                                         logger.info("entro por agregar todos");
                                            retorno =agregarTodosLosDatosCliente(todosDatos,datosClientePersonales,xmlClienteDomicilioTelefono);
                                            //obtenerCliente(retorno);
                                        }
                                        break;
                                    case -1:{logger.error("Fallo error al buscar cliente en metodo existe ");
                                        break;
                                    }

                                    default:{
                                        logger.info("entro por actualizar datos cli");
                                            retorno = actualizarDatos(todosDatos,datosClientePersonales,xmlClienteDomicilioTelefono,idcliente);
                                         
                                            break;
                                        }
                                }
                        //**********************************************************************************
                                

                 

             
             ///+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
                        break;
                    }


             }


        } catch (Exception e) {
            logger.error("Error en Metodo addCliente, EJBClientes, verifique "+e);
        }finally{
        return retorno;
        }
       
    }
//---------------------------------------------------------------------------------------------
    private long existe_cliente(int nrodocu) {
        long retorno =0;
        try {
            Query consulta = em.createQuery("SELECT p FROM Personas p WHERE p.nrodocumento = :nrodocu");
                consulta.setParameter("nrodocu", nrodocu);
                
              


           List<Personas>lista = consulta.getResultList();
            for (Iterator<Personas> it = lista.iterator(); it.hasNext();) {
                Personas personas = it.next();
                retorno=personas.getIdPersona();
            }


        } catch (Exception e) {
            retorno = -1;
            logger.error("Error al Buscar Cliente, metodo existe_cliente, EJBCliente "+e);
        }finally{
          
                return retorno;
        }
    }

    private long agregarTodosLosDatosCliente(ClienteDomicilioTelefono todosDatos, DatosCliente datosClientePersonales,String xmlClienteDomicilioTelefono) {
        long retorno =0;
        
        try {
            
            GregorianCalendar calendario = new GregorianCalendar(Locale.getDefault());
            //-----------------------------------------------------------------------------------

     

            //------------------------------------------------Proceso el Cliente -------------------------------
            Clientes cliente = new Clientes();
                
                cliente.setApellido(datosClientePersonales.getApellido().toUpperCase());
                
                cliente.setEmail(datosClientePersonales.getEmail());
                
                cliente.setFechaCarga(calendario.getTime());
                
                cliente.setGeneros(em.find(Generos.class, datosClientePersonales.getGenero().getIdgenero()));
                
                cliente.setNombre(datosClientePersonales.getNombre().toUpperCase());
                
                cliente.setNrodocumento(datosClientePersonales.getNrodocu());
                
                cliente.setObservaciones(datosClientePersonales.getObservaciones().toUpperCase());
                
                cliente.setTipodocumento(em.find(Tiposdocumento.class, datosClientePersonales.getIdtipodocu()));
                
                cliente.setTotalCompras(BigDecimal.valueOf(datosClientePersonales.getTotalcompras()));
                
                cliente.setTotalEnPuntos(BigInteger.valueOf(datosClientePersonales.getTotalpuntos()));
                
                em.persist(cliente);
                em.flush();
                
     
                retorno = guardarDomicilioyTelefonoCliente(xmlClienteDomicilioTelefono, cliente, todosDatos);
            /*//--------------------------------------------------------------------------------------------------*/
                
            

        } catch (Exception e) {
            retorno =-1;
            logger.error("Error en metodo agregarTodosLosDatosCliente, EJBClientes "+e.getMessage());
        }finally{
        return retorno;

        }
    }

    public String obtenerCliente(long idCliente) {
        String cli="<Lista>\n";
        try {
            Clientes cliente = em.find(Clientes.class, idCliente);

            if(cliente!=null){
                cli+="<item>\n";
                cli+=cliente.toXML();
                cli+=cliente.toXMLCLI();
                cli+="</item>\n";
            }else
                cli="Cliente NO ENCONTRADO!!!!";
            

        } catch (Exception e) {
            logger.error("Error al obtener un cliente EJBCliente", e);
        }finally{
           
            return cli+="</Lista>\n";

        }
    }


    private long actualizarDatos(ClienteDomicilioTelefono todosDatos, DatosCliente datosClientePersonales,String xmlClienteDomicilioTelefono,long idcliente) {
        long retorno = 0;
        try {
            //**********************************************************************************
         
            //**********************************************************************************

            Clientes cliente = em.find(Clientes.class, idcliente);

            if(cliente!=null){

                HistoricoDatosClientes histcli = new HistoricoDatosClientes();
                histcli.setApellido(cliente.getApellido().toUpperCase());
                
                histcli.setIdCliente(idcliente);
                histcli.setIdgenero(cliente.getGeneros().getIdGenero());
                histcli.setNombre(cliente.getNombre().toUpperCase());

                cliente.setObservaciones(datosClientePersonales.getObservaciones());
            
                cliente.setApellido(datosClientePersonales.getApellido().toUpperCase());
            
                cliente.setNombre(datosClientePersonales.getNombre().toUpperCase());
            
            
            
                cliente.setGeneros(em.find(Generos.class, datosClientePersonales.getGenero().getIdgenero()));



            
                    
                    if(retornoemail != -5){
                            histcli.setEmail(cliente.getEmail());
                            cliente.setEmail(datosClientePersonales.getEmail());
                    }
            
            
            
            
            
            

            cliente.setTotalCompras(BigDecimal.valueOf(datosClientePersonales.getTotalcompras()));
            
            cliente.setTotalEnPuntos(BigInteger.valueOf(datosClientePersonales.getTotalpuntos()));

            histcli.setTotalCompras(BigDecimal.valueOf(datosClientePersonales.getTotalcompras()));
            histcli.setTotalEnPuntos(BigInteger.valueOf(datosClientePersonales.getTotalpuntos()));
            
          

            /*
             * Actualizo domicilio
             */

            retorno = guardarDomicilioyTelefonoCliente(xmlClienteDomicilioTelefono,cliente,todosDatos);
                
               //-------------------------------------------------------------------------------------------------
                em.merge(cliente);
                em.persist(histcli);

                
            }
        } catch (Exception e) {
            retorno=-6;
            logger.error("Error en metodo actualizarDatos, ejbClientes "+e);
        }finally{
            return retorno;
        }
    }

    public String obtenerClienteXTipoAndNumeroDocu(short idTipo, int nrodDocu) {
        String result = "<Lista>\n";
        try {
            Query consulta = em.createQuery("SELECT p FROM Personas p WHERE p.nrodocumento = :nrodocumento and p.tipodocumento.id = :idtipo" +
                    " and p.pertype = :pertype");
            consulta.setParameter("nrodocumento", nrodDocu);
            consulta.setParameter("idtipo", idTipo);
            consulta.setParameter("pertype", "CLI");
            List<Personas> lisPer =consulta.getResultList();

            if(!lisPer.isEmpty()){
                for (Iterator<Personas> it = lisPer.iterator(); it.hasNext();) {
                    Personas personas = it.next();
                    result+="<item>\n";
                    result+=personas.toXML();
                    result+="</item>\n";
                }

            }else
                result+="<cliente>NO ENCONTRADO</cliente>";


        } catch (Exception e) {
            logger.error("Error en metodo obtenerClienteXTipoAndNumeroDocu en EJBClientes "+e);
        }finally{
          
        return result+"</Lista>";

        }
    }

    public long addClientes(Clientes clientes) {
        long resultCode = 0;
        try {
            //-----------------------------------------------------------------------------------
            GregorianCalendar calendario = new GregorianCalendar(Locale.getDefault());
            //-----------------------------------------------------------------------------------
            //--------------------------------------------------------------------------
            if(clientes == null)
            {
                logger.warn("Objeto Cliente es invalido");
                resultCode = -1;
                throw new IllegalArgumentException("Objeto Cliente es invalido");

            }
            //--------------------------------------------------------------------------
            Query consulta = em.createQuery("SELECT c FROM Clientes c WHERE c.nrodocumento = :nrodocumento");
            consulta.setParameter("nrodocumento",clientes.getNrodocumento());
            //--------------------------------------------------------------------------
            if(consulta.getResultList().size()==1){
                 logger.warn("Clientes existente, numero de documento");
                 resultCode=-3;
                 throw new RuntimeException("Entrada Cliente existente");

            }
            //-------------------------------------------------------------------------

            String nombre = clientes.getNombre();
            String apellido = clientes.getApellido();
            short idtipo = clientes.getTipodocumento().getId();
            int nrodocu = clientes.getNrodocumento();
            String email = clientes.getEmail();
            String observaciones = clientes.getObservaciones();

            //------------------------------------------------------------------------

            Clientes cli = new Clientes();
            cli.setApellido(apellido.toUpperCase());
            cli.setNombre(nombre.toUpperCase());
            cli.setEmail(email);
            cli.setObservaciones(observaciones);
            cli.setNrodocumento(nrodocu);
            cli.setTipodocumento(em.find(Tiposdocumento.class, idtipo));
            cli.setFechaCarga(calendario.getTime());
            cli.setTotalCompras(BigDecimal.ZERO);
            cli.setTotalEnPuntos(BigInteger.ZERO);

            em.persist(cli);
            resultCode = cli.getIdPersona();
//---------------------------------------------------------------------------------------------------

        } catch (Exception e) {
            logger.error("Ocurriò un error al insertar un Objeto Cliente, verifique", e);
            resultCode = -2;
        }finally{

            return resultCode;
        }

    }

    public long updateCliente(Clientes cliente) {
        long resultCode =0L;
        try {
            //--------------------------------------------------------
            if(cliente==null){
                logger.warn("Error Objeto Cliente Invalido al actualizar");
                resultCode=-2;
                throw new IllegalArgumentException("Objeto Cliente invalido al actualizar");
            }
            //--------------------------------------------------------

            String apellido = cliente.getApellido();
            String nombre = cliente.getNombre();
            String email = cliente.getEmail();
            String observaciones = cliente.getObservaciones();
            BigDecimal totalCompras = cliente.getTotalCompras();
            BigInteger puntos = cliente.getTotalEnPuntos();
            short idTipo = cliente.getTipodocumento().getId();
            int nroDocu = cliente.getNrodocumento();
            long clientId = cliente.getIdPersona();
            //--------------------------------------------------------
                    if(clientId <= 0 ||
                            nombre == null ||
                            apellido == null)
                        {
                        logger.warn("Error Objeto Cliente Invalido al actualizar");
                        resultCode=-3;
                            throw new IllegalArgumentException("Objeto Cliente invalido al actualizar");
                        }
                       //---------------------------------------------------------
                        Clientes cli = em.find(Clientes.class, clientId);

                        cli.setApellido(apellido.toUpperCase());
                        cli.setEmail(email);
                        cli.setNombre(nombre.toUpperCase());
                        cli.setObservaciones(observaciones);
                        cli.setTotalCompras(totalCompras);
                        cli.setTotalEnPuntos(puntos);
                        cli.setTipodocumento(em.find(Tiposdocumento.class, idTipo));
                        cli.setNrodocumento(nroDocu);
                        //--------------------------------------------------------

                            resultCode = cli.getIdPersona();
                        //--------------------------------------------------------


        } catch (Exception e) {
            logger.error("Error en metodo updateCliente, verifique ",e);
            resultCode =-1;
        }finally{

            return resultCode;
        }

    }

    private long guardarDomicilioyTelefonoCliente(String xmlClienteDomicilioTelefono,Clientes cliente, ClienteDomicilioTelefono todosDatos) {
        long retorno =0;
        long idDomicilio=0L;
        try {
            
            
            
            if(xmlClienteDomicilioTelefono.contains("<Domicilio>")){
            
                            idDomicilio = ejbdomici.addDomicilios(todosDatos.getDomicilio());
            

                             //-------ADD RELACION--------------------------------------------------------------------------
                                String result=null;
            
                                //---------------------------------------------------------------------------------------------
                               switch((int)idDomicilio){
                                   case -1:{logger.error("Error No se pudo agregar domicilio Verifique!!!");
                                   retorno = -1;
                                   break;}
                                   case -2:{logger.error("Error No se pudo agregar domicilio Verifique!!!");
                                   retorno = -2;
                                   break;}
                                   case 0:{logger.error("Error no se pudo agregar domicilio Verifique!!!");
                                   retorno = 0;
                                   break;}
                                   case -3:{logger.error("Error en metodo actualizar domicilio Verifique!!!");
                                   retorno = -3;
                                   break;}
                                   default:{
                                       String consulta="SELECT p FROM PersonasDomicilios p WHERE p.personasdomiciliosPK.idPersona = :idPersona" +
                                               " and p.personasdomiciliosPK.iddomicilio = :iddomicilio";
                                       Query sqlPD = em.createQuery(consulta);
                                       sqlPD.setParameter("idPersona", cliente.getIdPersona());
                                       sqlPD.setParameter("iddomicilio", idDomicilio);
            
                                       switch(sqlPD.getResultList().size()){
                                           case 0:{
                                            result= ejbclidom.addRelacionClienteDomicilio(cliente.getIdPersona(), idDomicilio,todosDatos.getIdusuario());
                                            break;
                                           }
                                           
                                           case 1:{
                                               result="Relacion Encontrada PD";
                                               break;
                                           }

                                       }
                                       break;
                                   }
                               }
                               //----------------------------------------------------------------------------------------------

                                if(result.contains("InyectóRelacion")){
            
                                    //---------------------------Agregamos la Relacion con Persona Cliente y Domicilios-----------
                               
            

                                    List<PersonasDomicilios>listaPD = em.createQuery("SELECT p FROM PersonasDomicilios p WHERE " +
                                            "p.personasdomiciliosPK.idPersona = :idPersona").setParameter("idPersona", cliente.getIdPersona()).getResultList();
                               

                                    //-----------------------------------------------------------------------------
            
                                    cliente.setPersonasDomicilioss(listaPD);
            

                                    Domicilios domici = em.find(Domicilios.class, idDomicilio);
            
                                    domici.setPersonasDomicilioss(listaPD);
            
                                    //-----------------------------------------------------------------------------

                                    em.persist(domici);
                                    em.persist(cliente);


                                }else
                                   logger.info("La relación ClienteDomicilio cliente "+cliente.getIdPersona()+" Domicilio "+idDomicilio+" "+result);
                }
               //-------------------------------------------------------------------------------------------------
                //---------------------------------------------------------------------------------------------
                    if(xmlClienteDomicilioTelefono.contains("<telefono>")){
            
                           if(todosDatos.getListaTelefonos().getList().size()>0){
            
                               Iterator iter = todosDatos.getListaTelefonos().getList().iterator();
                               //------------------------------------------------------------------------------

                               String resultTC="";
                               DatosTelefonos datosTel=null;
                               while (iter.hasNext())
                               {
                                datosTel = (DatosTelefonos) iter.next();



                                 
                                            long rettelefono = ejbtele.addTelefonos(datosTel);

                                            if(rettelefono==2){
                                                resultTC = ejbclitel.addClienteTelefono(datosTel.getNumero(), datosTel.getPrefijo(), cliente.getIdPersona());
                                                if(resultTC.indexOf("RelacionTelefonoExistente")!=-1)
                                                    logger.info("Relacion existente para el cliente "+cliente.getNrodocumento());
                                                else{ 
                                                    if(resultTC.indexOf("InyectoRelacionClienteTelefono")!=-1)
                                                        logger.info("Relacion Telefono Insertada "+resultTC+" para el cliente "+cliente.getNrodocumento());
                                                    else
                                                        logger.error(resultTC);
                                                
                                                }
                                            }else{
                                                if(rettelefono==1){
                                                    
                                                     resultTC = ejbclitel.addClienteTelefono(datosTel.getNumero(), datosTel.getPrefijo(), cliente.getIdPersona());
                                                     if(resultTC.indexOf("RelacionTelefonoExistente")!=-1)
                                                             logger.info("Relacion existente para el cliente "+cliente.getNrodocumento());
                                                            else{
                                                                if(resultTC.indexOf("InyectoRelacionClienteTelefono")!=-1)
                                                                    logger.info("Relacion Telefono Insertada "+resultTC+" para el cliente "+cliente.getNrodocumento());
                                                                else
                                                                    logger.error(resultTC);
                                                            }

                                            }
                                   

                               }
                               
            


                           }
                               ///+++++++++++++++++++++++++++++++++++++++++++++++++++++++++Relaciono las Entidades****************************+++++
                               //------------------------------------------------------------------------------

                                       Query clitele = em.createQuery("SELECT p FROM Personastelefonos p WHERE p.personastelefonosPK.idPersona = :idpersona");

                                       clitele.setParameter("idpersona", cliente.getIdPersona());

                                       
                                       



                                       List<Personastelefonos>listaTel = clitele.getResultList();




                                       cliente.setPersonastelefonoss(listaTel);

                                        Telefonos telef=null;
                                           for (Iterator<Personastelefonos> it = listaTel.iterator(); it.hasNext();) {
                                             Personastelefonos personastelefonos = it.next();
                                              telef = em.find(Telefonos.class, new TelefonosPK(personastelefonos.getPersonastelefonosPK().getNumerotel(), personastelefonos.getPersonastelefonosPK().getPrefijo()));
                                             telef.setPersonastelefonosCollection(listaTel);
                                          }

                                            em.persist(telef);


                                            em.persist(cliente);
                               //*****************************************************************************************************************
                            
                             
                        }
                         
                    }
          
                retorno = cliente.getIdPersona();
            //---------------------------------------------------------------------------------------------
            
            

        } catch (Exception e) {
            retorno =-3;
            logger.error("ERROR EN METODO GUARDAR DOMICILIO Y TELEFONO CLIENTE "+e.getMessage());
        }finally{
            
            return retorno;

        }
    }

    public String getCustomerDocNumber(Integer docNumber) {
        String xml="";
        try {
            Query jsql=em.createQuery("SELECT p FROM Personas p WHERE p.nrodocumento = :nrodocumento and " +
                    "p.pertype = :pertype");
            jsql.setParameter("nrodocumento", docNumber);
            jsql.setParameter("pertype", "CLI");

            List<Clientes>lista = jsql.getResultList();

           switch(lista.size()){
               case 0:xml="Cliente no encontrado";
               break;
               case 1:{
                    for (Iterator<Clientes> it = lista.iterator(); it.hasNext();) {
                            Clientes clientes = it.next();
                            xml+="<item>\n";
                            xml+=clientes.toXML();
                            xml+=clientes.toXMLCLI();
                            xml+="</item>\n";
                     }
               }
           }
            


        } catch (Exception e) {
            logger.error("Error en metodo getCustomerDocNumber "+e.getMessage());
            xml="Error";
        }finally{
          
            return xml;
        }
    }

    public long chequearemail(String email,Integer nrodocu) {
        long retorno = -6;
        try {
            Query sqlemail = em.createQuery("SELECT p FROM Personas p WHERE p.email = :email and p.nrodocumento = :nrodocumento");
                        sqlemail.setParameter("email", email.toLowerCase());
                        sqlemail.setParameter("nrodocumento", nrodocu);

                if(sqlemail.getResultList().size()==1)
                             retorno = -5;
                else{
                    sqlemail = em.createQuery("SELECT p FROM Personas p WHERE p.email = :email");
                        sqlemail.setParameter("email", email.toLowerCase());
                        if(sqlemail.getResultList().size()==1)
                            retorno =-8;

                }
                 
        } catch (Exception e) {
            retorno = -7;
            logger.error("Error en metodo chequear email en ejbClientes "+e);
        }finally{
        return retorno;
        }
    }

    public String searchClientForNameAndLastName(String name,String lastname) {
        String xml = "<Lista>\n";
        try {
            StringBuilder sbname = new StringBuilder();
                sbname.append(name);
                sbname.append("%");
                    StringBuilder sblastname = new StringBuilder();
                        sblastname.append(lastname);
                        sblastname.append("%");

            String sql = "SELECT p FROM Personas p WHERE p.nombre LIKE :nombre and p.apellido LIKE :apellido";
            Query consulta = em.createQuery(sql);
            consulta.setParameter("nombre", sbname.toString().toUpperCase());
            consulta.setParameter("apellido", sblastname.toString().toUpperCase());
            List<Personas>lista = consulta.getResultList();

            for (Iterator<Personas> it = lista.iterator(); it.hasNext();) {
                Personas personas = it.next();
                xml+="<item>\n"
                + "<id>"+personas.getIdPersona()+"</id>\n"
                + "<apellido>"+personas.getApellido()+"</apellido>\n"
                + "<nombre>"+personas.getNombre()+"</nombre>\n"
                + "<idtipodocu>"+personas.getTipodocumento().getId()+"</idtipodocu>\n"
                + "<nrodocu>"+personas.getNrodocumento()+"</nrodocu>\n" +
                        "</item>\n";
            }

        } catch (Exception e) {
            logger.error("Error en metodo searchClientForNameAndLastName "+e.getCause());
        }finally{
        return xml+="</Lista>\n";
        }
    }

    public String addClienteDatosPersonales(String datospersonalescliente) {
        String xml="<Lista>\n";
        try {

                    XStream  xstream = new XStream();
                    xstream.alias("ClienteDomicilioTelefono",ClienteDomicilioTelefono.class);
                    xstream.alias("item", DatosCliente.class);

                    ClienteDomicilioTelefono datoscliente = (ClienteDomicilioTelefono) xstream.fromXML(parsearCaracteresEspecialesXML1(datospersonalescliente).toString());
                    DatosCliente getcliente = datoscliente.getCliente();

                     retornoemail = chequearemail(getcliente.getEmail(),getcliente.getNrodocu());

             switch((int)retornoemail){

                 case -7:{logger.error("Error en metodo chequear email");
                    xml+="<error>Error en metodo chequear email</error>\n";
                 break;
                 }
                 case -8:{logger.error("Email encontrado en metodo chequearemail");
                    xml+="<info>Email encontrado en metodo chequearemail<info>\n";
                  break;
                 }
                 default:{

                     long idcliente = addDatosCliente(getcliente.getApellido(), getcliente.getNombre(), getcliente.getIdtipodocu(), getcliente.getNrodocu(), getcliente.getEmail(), getcliente.getObservaciones(), getcliente.getGenero().getIdgenero());
                     if(idcliente>0){
                             Clientes cliem = em.find(Clientes.class, idcliente);
                             xml+="<item>\n";
                             xml+=cliem.toXML();
                             xml+=cliem.toXMLCLI();
                             xml+="</item>\n";

                     }


                 }
             }

        } catch (Exception e) {
            xml+="<error>se produjo un error</error>\n";
            logger.error("Error en metodo addclientepersonales en ejbcliente", e.getCause());
        }finally{
            return xml+="</Lista>\n";
        }

    }

    public String ShowReportClient() {
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
            String sql = "SELECT * FROM PERSONAS p WHERE p.pertype like 'CLI' Order BY p.id_persona asc";
            oxq = new OracleXMLQuery(con, sql);

            oxq.setRowTag("Item");
            oxq.setRowsetTag("Lista");
            oxq.setEncoding("ISO-8859-1");
            xml = oxq.getXMLString();
            oxq.close();
        }catch(Exception e){

        }finally{
            try {
                if (con != null) {
                    con.close();
                }
                if (oxq != null) {
                    oxq.close();
                }
                } catch (SQLException ex) {
                java.util.logging.Logger.getLogger(EJBBarrios.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
            }

        return xml;
        }
    }











//--------------------------------------------------------------------------------------------------

      public String parsearCaracteresEspecialesXML1(String xmlaParsear) {
        String xml = "No paso Nada";
        StringBuilder sb=null;
    try {
        
        sb=new StringBuilder(xmlaParsear);

            if(xmlaParsear.indexOf("<item>")!=-1){


                xml=StringEscapeUtils.escapeXml(xmlaParsear.substring(xmlaParsear.indexOf("nes>")+4,xmlaParsear.indexOf("</obse")));
                
                sb.replace(sb.indexOf("nes>")+4, sb.indexOf("</obse"), xml);
                
            }
           if(xmlaParsear.indexOf("<Domicilio>")!=-1){

                xml=StringEscapeUtils.escapeXml(xmlaParsear.substring(xmlaParsear.indexOf("mes>")+4,xmlaParsear.indexOf("</det1")));
                
                sb.replace(sb.indexOf("mes>")+4, sb.indexOf("</det1"), xml);
                
           }

    } catch (Exception e) {
        xml = "Error";

        logger.error("Error en metodo parsearCaracteresEspecialesXML1 ",e);
    }finally{

        return sb.toString();
    }
    }




}

















