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
        long idcliente=0;
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
            ClienteDomicilioTelefono todosDatos = (ClienteDomicilioTelefono) xstream.fromXML(xmlClienteDomicilioTelefono);
           
            
            //------------------------------------------------------------------------------------------
             DatosCliente datosClientePersonales =todosDatos.getCliente();
            //------------------------------------------------------------------------------------------
             idcliente= existe_cliente(datosClientePersonales.getNrodocu(),datosClientePersonales.getIdtipodocu(),datosClientePersonales.getGenero().getIdgenero());

                switch((int)idcliente){
                    case 0:{
                                //------agrego el cliente y todos sus datos desde cero
                         logger.info("Por Agregar Datos Cliente desde Cero "+datosClientePersonales.getNrodocu()+" "+datosClientePersonales.getIdtipodocu());
                            retorno =agregarTodosLosDatosCliente(todosDatos,datosClientePersonales,xmlClienteDomicilioTelefono);
                            obtenerCliente(retorno);
                        }
                        break;
                    case -1:logger.warn("Fallo error al buscar cliente en metodo existe ");
                    default:{
                        logger.info("El CLIENTE "+datosClientePersonales.getNrodocu()+" YA EXISTE!!!");
                            retorno = actualizarDatos(todosDatos,datosClientePersonales,xmlClienteDomicilioTelefono,idcliente);
                            obtenerCliente(retorno);
                        }
                }



        } catch (Exception e) {
            logger.error("Error en Metodo addCliente, EJBClientes, verifique"+e);
        }finally{
        return retorno;
        }
    }
//---------------------------------------------------------------------------------------------
    private long existe_cliente(int nrodocu,short idtipodocu,short idgenero) {
        long retorno =0;
        try {
            Query consulta = em.createQuery("SELECT p FROM Personas p WHERE p.nrodocumento = :nrodocu and p.tipodocumento.id = :idtipodocu and " +
                    "p.generos.idGenero = :idgenero");
                consulta.setParameter("nrodocu", nrodocu);
                consulta.setParameter("idtipodocu", idtipodocu);
                consulta.setParameter("idgenero", idgenero);
              logger.info("Cliente encotrado en existe_cliente "+consulta.getResultList().size())  ;
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
        String cli=null;
        try {
            Clientes cliente = em.find(Clientes.class, idCliente);

            if(cliente!=null)
                cli=cliente.toXML();
            else
                cli="Cliente NO ENCONTRADO!!!!";
            

        } catch (Exception e) {
            logger.error("Error al obtener un cliente EJBCliente", e);
        }finally{
           System.out.println(cli);
            return cli;

        }
    }


    private long actualizarDatos(ClienteDomicilioTelefono todosDatos, DatosCliente datosClientePersonales,String xmlClienteDomicilioTelefono,long idcliente) {
        long retorno = 0;
        try {
            //**********************************************************************************
         
            //**********************************************************************************

            Clientes cliente = em.find(Clientes.class, idcliente);

            if(cliente!=null){

            
            cliente.setApellido(datosClientePersonales.getApellido());
            
            cliente.setNombre(datosClientePersonales.getNombre());
            
            cliente.setEmail(datosClientePersonales.getEmail());
            
            cliente.setGeneros(em.find(Generos.class, datosClientePersonales.getGenero().getIdgenero()));
            
            //cliente.setNrodocumento(datosClientePersonales.getNrodocu());
            
            //cliente.setTipodocumento(em.find(Tiposdocumento.class, datosClientePersonales.getIdtipodocu()));
            
            cliente.setObservaciones(cliente.getObservaciones().toUpperCase());
            

            cliente.setTotalCompras(BigDecimal.valueOf(datosClientePersonales.getTotalcompras()));
            
            cliente.setTotalEnPuntos(BigInteger.valueOf(datosClientePersonales.getTotalpuntos()));
            
          

            /*
             * Actualizo domicilio
             */

            retorno = guardarDomicilioyTelefonoCliente(xmlClienteDomicilioTelefono,cliente,todosDatos);
                
               //-------------------------------------------------------------------------------------------------
                em.merge(cliente);
                

                logger.info("Datos del cliente Actualizado Nº "+cliente.getIdPersona());
            }
        } catch (Exception e) {
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
            System.out.println("1");
            
            System.out.println("1");
            if(xmlClienteDomicilioTelefono.contains("<Domicilio>")){
                System.out.println("1");
                            long idDomicilio = ejbdomici.addDomicilios(todosDatos.getDomicilio());
                            System.out.println("1");

                             //-------ADD RELACION--------------------------------------------------------------------------
                                String result=null;
                                System.out.println("1");
                                //---------------------------------------------------------------------------------------------
                               switch((int)idDomicilio){
                                   case -1:logger.error("Error No se pudo agregar domicilio Verifique ");
                                   break;
                                   case -2:logger.error("Error No se pudo agregar domicilio Verifique ");
                                   break;
    
                                   default:{
                                       String consulta="SELECT p FROM PersonasDomicilios p WHERE p.personasdomiciliosPK.idPersona = :idPersona" +
                                               " and p.personasdomiciliosPK.iddomicilio = :iddomicilio";
                                       Query sqlPD = em.createQuery(consulta);
                                       sqlPD.setParameter("idPersona", cliente.getIdPersona());
                                       sqlPD.setParameter("iddomicilio", idDomicilio);
                                       System.out.println("Resultado de la consulta "+sqlPD.getResultList().size());
                                       switch(sqlPD.getResultList().size()){
                                           case 0:{
                                            result= ejbclidom.addRelacionClienteDomicilio(cliente.getIdPersona(), idDomicilio,todosDatos.getIdusuario());
                                           }
                                           break;
                                           case 1:
                                               result="Relacion Encontrada PD";
                                               break;

                                       }

                               /*        if(sqlPD.getResultList().size()==0)
                                            result= ejbclidom.addRelacionClienteDomicilio(cliente.getIdPersona(), idDomicilio,todosDatos.getIdusuario());
                                       else
                                           result ="Relacion Encontrada";*/
                                   }
                               }
                               //----------------------------------------------------------------------------------------------

                                if(result.contains("InyectóRelacion")){
                                System.out.println("3");
                                    //---------------------------Agregamos la Relacion con Persona Cliente y Domicilios-----------
                                    //PersonasdomiciliosPK clave = new PersonasdomiciliosPK(idDomicilio, cliente.getIdPersona());
                                     System.out.println("4");

                                    List<PersonasDomicilios>listaPD = em.createQuery("SELECT p FROM PersonasDomicilios p WHERE " +
                                            "p.personasdomiciliosPK.idPersona = :idPersona").setParameter("idPersona", cliente.getIdPersona()).getResultList();
                                            /*em.createQuery("SELECT p FROM PersonasDomicilios p WHERE "
                                            + "p.personasdomiciliosPK = :clave").setParameter("clave", clave).getResultList();*/

                                    //-----------------------------------------------------------------------------
                                    System.out.println("4 VALOR DE LISTA DOMICILIO DE CLIENTE "+listaPD.size());
                                    cliente.setPersonasDomicilioss(listaPD);
                                    System.out.println("5");

                                    Domicilios domici = em.find(Domicilios.class, idDomicilio);
                                    System.out.println("6");
                                    domici.setPersonasDomicilioss(listaPD);
                                    System.out.println("7");
                                    //-----------------------------------------------------------------------------

                                    em.persist(domici);
                                    em.persist(cliente);


                                }else
                                   logger.info("La relación ClienteDomicilio cliente "+cliente.getIdPersona()+" Domicilio "+idDomicilio+" "+result);
                }
               //-------------------------------------------------------------------------------------------------
                //---------------------------------------------------------------------------------------------
                    if(xmlClienteDomicilioTelefono.contains("<telefono>")){
                        System.out.println("1");
                           if(todosDatos.getListaTelefonos().getList().size()>0){
                           System.out.println("1");
                               Iterator iter = todosDatos.getListaTelefonos().getList().iterator();
                               //------------------------------------------------------------------------------
                               while (iter.hasNext())
                               {
                                   System.out.println("1");

                                   DatosTelefonos datosTel = (DatosTelefonos) iter.next();
                                    long rettelefono = ejbtele.addTelefonos(datosTel);
                                    if(rettelefono==2){
                                        String resultTC = ejbclitel.addClienteTelefono(datosTel.getNumero(), datosTel.getPrefijo(), cliente.getIdPersona());
                                        logger.debug("Relacion Telefono Insertada "+resultTC);
                                    }else{
                                        if(rettelefono==1)
                                            logger.info("El telefono Nº"+datosTel.getNumero()+" ya se encuentra cargado para el cliente Nº "+cliente.getIdPersona());

                                    }

                               }
                               //------------------------------------------------------------------------------
                               Query clitele = em.createQuery("SELECT p FROM Personastelefonos p WHERE p.personastelefonosPK.idPersona = :idpersona");
                               clitele.setParameter("idpersona", cliente.getIdPersona());

                               List<Personastelefonos>listaTel = clitele.getResultList();


                                System.out.println("1");

                               cliente.setPersonastelefonoss(listaTel);
                                   for (Iterator<Personastelefonos> it = listaTel.iterator(); it.hasNext();) {
                                     Personastelefonos personastelefonos = it.next();
                                     Telefonos telef = em.find(Telefonos.class, new TelefonosPK(personastelefonos.getPersonastelefonosPK().getNumerotel(), personastelefonos.getPersonastelefonosPK().getPrefijo()));
                                     telef.setPersonastelefonosCollection(listaTel);
                                  }


                               em.persist(cliente);
                               System.out.println("22");


                           }
                            //logger.info("Cliente Sin Telefonos a insertar IDCliente "+cliente.getIdPersona());

                    }
            
            //---------------------------------------------------------------------------------------------
            System.out.println("12");
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
            System.out.println(xml);
            return xml;
        }
    }








//--------------------------------------------------------------------------------------------------




}

















