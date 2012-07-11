/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.melani.ejb;

import com.melani.entity.Clientes;
import com.melani.entity.Domicilios;

import com.melani.entity.PersonasDomicilios;
import com.melani.entity.PersonasdomiciliosPK;
import java.text.SimpleDateFormat;
import java.util.GregorianCalendar;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import org.apache.log4j.Logger;


/**
 *
 * @author Edgardo
 */
@Stateless(mappedName="ejb/EJBClienteDomicilio")
public class EJBClienteDomicilio implements EJBClienteDomicilioRemote {
    private static Logger logger = Logger.getLogger(EJBClienteDomicilio.class);
    @PersistenceContext
    private EntityManager em;

    @EJB
    EJBHistoricoPersonaDomicilioRemote ejbhistperdom;

    public String addRelacionClienteDomicilio(long idCliente, long idDomicilio,int idUsuario) {
        String retorno = "NADA";
        try {

            
            
            GregorianCalendar calendario = new GregorianCalendar(Locale.getDefault());
            PersonasdomiciliosPK perpk = new PersonasdomiciliosPK(idDomicilio, idCliente);

            //if(em.find(PersonasDomicilios.class, perpk)!=null){

                PersonasDomicilios personadomici = renovarDomicilio(idDomicilio,idCliente,idUsuario);
                if(personadomici!=null)
                    logger.info("DOMICILIO RENOVADO");
                //retorno ="LA RELACION PERSONADOMICILIO EXISTE";
                
           // }else{

                PersonasDomicilios personadomicilio = new PersonasDomicilios();
                personadomicilio.setDomicilioss(em.find(Domicilios.class, idDomicilio));
                personadomicilio.setEstado("Habitable".toUpperCase());
                personadomicilio.setPersonasdomiciliosPK(perpk);
                personadomicilio.setFechaingresovivienda(calendario.getTime());
                personadomicilio.setPersonas(em.find(Clientes.class, idCliente));

                em.persist(personadomicilio);
                em.flush();
                retorno ="InyectóRelacion";

                logger.info("Relacion PersonaDomicilio Agregada Con Exito Cliente "+idCliente+" Domicilio "+idDomicilio);

            //}


        } catch (Exception e) {
            retorno ="Error";
            logger.error("Error en metodo addRelacionClienteDomicilio "+e);
        }finally{
            return retorno;
        }
    }

    private PersonasDomicilios renovarDomicilio(long idDomicilio, long idCliente,int idUsuario) {
        PersonasDomicilios perdomi = null;
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
            GregorianCalendar calendario = new GregorianCalendar(Locale.getDefault());
            perdomi = em.find(PersonasDomicilios.class, new PersonasdomiciliosPK(idDomicilio, idCliente));

            Query consulta = em.createQuery("SELECT p FROM PersonasDomicilios p WHERE p.personasdomiciliosPK.idPersona = :idPersona");
            consulta.setParameter("idPersona", idCliente);
            List<PersonasDomicilios>lista= consulta.getResultList();

            PersonasDomicilios personasDomicilios = null;
            if(!lista.isEmpty()){
                    for (Iterator<PersonasDomicilios> it = lista.iterator(); it.hasNext();) {
                         personasDomicilios = it.next();
                        ejbhistperdom.addOneHomePerson(Integer.valueOf(String.valueOf(personasDomicilios.getDomicilioss().getId())),Integer.valueOf(String.valueOf(personasDomicilios.getPersonas().getIdPersona())), idUsuario);
                        logger.info("Domicilio Removido de la Relacion "+personasDomicilios.getPersonas().getIdPersona()+" el dia "+sdf.format(calendario.getTime())+"Domicilio ID "+personasDomicilios.getDomicilioss().getId());
                        em.remove(personasDomicilios);


                    }
                
            }
            em.flush();
            perdomi=personasDomicilios;


            /*if(perdomi==null){

                PersonasDomicilios clidomi = new PersonasDomicilios();
                clidomi.setDomicilioss(em.find(Domicilios.class, idDomicilio));
                clidomi.setPersonas(em.find(Clientes.class, idCliente));
                clidomi.setEstado("Domicilio RENOVADO AGREGADO");
                clidomi.setFechaingresovivienda(calendario.getTime());
                clidomi.setPersonasdomiciliosPK(new PersonasdomiciliosPK(idDomicilio, idCliente));
                em.persist(clidomi);
                em.flush();
                logger.info("Domicilio Renovado del Cliente "+idCliente+" el dia "+GregorianCalendar.getInstance().toString()+"Domicilio ID "+idDomicilio);
                perdomi = clidomi;
            }*/
            



        } catch (Exception e) {
            logger.error("Error en metodo renovarDomicilio EJBClienteDomicilio "+e);
        }finally{
            return perdomi;
        }
    }
    
    
 
}
