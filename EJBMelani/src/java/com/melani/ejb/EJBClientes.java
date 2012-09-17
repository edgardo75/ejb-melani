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
import java.text.SimpleDateFormat;

import java.util.Date;
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
@Stateless(name="ejb/EJBClientes")
@WebService(serviceName="ServiceClientes",name="ClientesWs")
@SOAPBinding(style=SOAPBinding.Style.RPC)
public class EJBClientes implements EJBClientesRemote {
    private static Logger logger = Logger.getLogger(EJBClientes.class);
    @PersistenceContext(unitName="EJBMelaniPU2")
    private EntityManager em;
   // private long idcliente=0;
    @EJB
    EJBDomiciliosRemote ejbdomici;
    @EJB
    EJBTelefonosRemote ejbtele;
    @EJB
    EJBClienteDomicilioRemote ejbclidom;
    @EJB
    EJBClienteTelefonoRemote ejbclitel;
    @EJB
    EJBPresupuestosRemote ejbpresupuestos;

    private long retornoemail=0L;

    //----------------------------------------------------------------------------


    //------------------------------------------------------------------------------------------------------
    public long addDatosCliente(String apellido, String nombre, short idtipo, int numerodocu, String email, String observaciones,short idGenero) {

          long retorno =0L;
          
        try {             
            
            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
            //----------------------------------------------------------------------------------------
            Clientes cliente = new Clientes();
             
            cliente.setApellido(apellido);
             
            cliente.setNombre(nombre);
             
            cliente.setEmail(email);
             
            cliente.setFechaCarga(sdf.parse(sdf.format(new Date())));
             
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
            ClienteDomicilioTelefono todosDatos = (ClienteDomicilioTelefono) xstream.fromXML(ejbpresupuestos.parsearCaracteresEspecialesXML1(xmlClienteDomicilioTelefono).toString());
                   
            
            //------------------------------------------------------------------------------------------
             DatosCliente datosClientePersonales =todosDatos.getCliente();
           //------------------------------------------------------------------------------------------
            ///++++++++++++++++++++++++++++++++++++Primero Chequeo el email y luego la persona si existene en la base de datos
             retornoemail = chequearemail(datosClientePersonales.getEmail());

             switch((int)retornoemail){
                
                 case -7:{logger.info("Error en metodo chequear email");
                 break;
                 }
                 case -5:{logger.info("Email encontrado en metodo chequearemail");
                  
                 }
                 default:{
                 
           
             /*if(retorno==-5)
                 logger.info("Email encontrado en la base de datos");
             else
                if(retorno==-1)
                 logger.warn("Se produjo un error al buscar el email");*/
               
                        //*****************************************************************************++++
                                      idcliente= existe_cliente(datosClientePersonales.getNrodocu());

                                switch((int)idcliente){
                                    case 0:{
                                                //------agrego el cliente y todos sus datos desde cero
                                         logger.info("Por Agregar Datos Cliente desde Cero "+datosClientePersonales.getNrodocu()+" "+datosClientePersonales.getIdtipodocu());
                                            retorno =agregarTodosLosDatosCliente(todosDatos,datosClientePersonales,xmlClienteDomicilioTelefono);
                                            obtenerCliente(retorno);
                                        }
                                        break;
                                    case -1:{logger.warn("Fallo error al buscar cliente en metodo existe ");
                                        break;
                                    }

                                    default:{
                                        logger.info("El CLIENTE "+datosClientePersonales.getNrodocu()+" YA EXISTE!!!");
                                            retorno = actualizarDatos(todosDatos,datosClientePersonales,xmlClienteDomicilioTelefono,idcliente);
                                            if(retorno >0)
                                            obtenerCliente(retorno);
                                            break;
                                        }
                                }
                        //**********************************************************************************
                                

                 

             
             ///+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
                        break;
                    }


             }


        } catch (Exception e) {
            logger.error("Error en Metodo addCliente, EJBClientes, verifique"+e);
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
                
              if(consulta.getResultList().size()==1)
                  logger.info("Cliente encotrado en metodo existe_cliente "+consulta.getResultList().size())  ;


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

            if(cliente!=null)
                cli+=cliente.toXML();
            else
                cli="Cliente NO ENCONTRADO!!!!";
            

        } catch (Exception e) {
            logger.error("Error al obtener un cliente EJBCliente", e);
        }finally{
           //System.out.println(cli);
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



            
                    
                    if((datosClientePersonales.getEmail().toLowerCase().toString() == cliente.getEmail().toLowerCase().toString())&&retornoemail!=-5){
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

                logger.info("Datos del cliente Actualizado Nº "+cliente.getIdPersona());
            }
        } catch (Exception e) {
            retorno=-6;
            logger.error("Error al actualizar los datos, ejbClientes "+e);
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
                    result+=personas.toXML();
                }

            }else
                result+="<cliente>NO ENCONTRADO</cliente>";


        } catch (Exception e) {
            logger.info("Error en metodo obtenerClienteXTipoAndNumeroDocu en EJBClientes "+e);
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
        try {
            
            
            
            if(xmlClienteDomicilioTelefono.contains("<Domicilio>")){
            
                            long idDomicilio = ejbdomici.addDomicilios(todosDatos.getDomicilio());
            

                             //-------ADD RELACION--------------------------------------------------------------------------
                                String result=null;
            
                                //---------------------------------------------------------------------------------------------
                               switch((int)idDomicilio){
                                   case -1:{logger.error("Error No se pudo agregar domicilio Verifique!!!");
                                   break;}
                                   case -2:{logger.error("Error No se pudo agregar domicilio Verifique!!!");
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

                               /*        if(sqlPD.getResultList().size()==0)
                                            result= ejbclidom.addRelacionClienteDomicilio(cliente.getIdPersona(), idDomicilio,todosDatos.getIdusuario());
                                       else
                                           result ="Relacion Encontrada";*/
                                   }
                               }
                               //----------------------------------------------------------------------------------------------

                                if(result.contains("InyectóRelacion")){
            
                                    //---------------------------Agregamos la Relacion con Persona Cliente y Domicilios-----------
                                    //PersonasdomiciliosPK clave = new PersonasdomiciliosPK(idDomicilio, cliente.getIdPersona());
            

                                    List<PersonasDomicilios>listaPD = em.createQuery("SELECT p FROM PersonasDomicilios p WHERE " +
                                            "p.personasdomiciliosPK.idPersona = :idPersona").setParameter("idPersona", cliente.getIdPersona()).getResultList();
                                            /*em.createQuery("SELECT p FROM PersonasDomicilios p WHERE "
                                            + "p.personasdomiciliosPK = :clave").setParameter("clave", clave).getResultList();*/

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
                                                    logger.info("El telefono Nº"+datosTel.getNumero()+" ya se encuentra cargado");
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
            
            //---------------------------------------------------------------------------------------------
            
            retorno = cliente.getIdPersona();

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
                            xml=clientes.toXML();
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

    private long chequearemail(String email) {
        long retorno = -6;
        try {
            Query sqlemail = em.createQuery("SELECT p FROM Personas p WHERE p.email = :email");
                sqlemail.setParameter("email", email.toLowerCase());

                if(sqlemail.getResultList().size()==0)
                    logger.info("Email no encontrado en metodo chequearemail "+sqlemail.getResultList().size());
                    else{
                        logger.info("Email encontrado en metodo chequearemail "+sqlemail.getResultList().size());
               
                             retorno=-5;
                    }
                 
        } catch (Exception e) {
            retorno = -7;
            logger.error("Error en metodo chequear email en ejbClientes "+e);
        }finally{
        return retorno;
        }
    }








//--------------------------------------------------------------------------------------------------




}

















