/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.melani.ejb;
import com.melani.entity.Clientes;
import com.melani.entity.Domicilios;
import com.melani.entity.PersonasDomicilios;
import com.melani.entity.PersonasdomiciliosPK;
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
 * @version 1.0 Build 5600 Feb 20, 2013
 */
@Stateless(name="ejb/EJBClienteDomicilio")
public class EJBClienteDomicilio implements EJBClienteDomicilioRemote {
    private static Logger logger = Logger.getLogger(EJBClienteDomicilio.class);
    @PersistenceContext
    private EntityManager em;
    @EJB
    EJBHistoricoPersonaDomicilioRemote ejbhistperdom;
/**
 *
 * @param idCliente id de cliente registrado en la tabla correspondiente en la base de datos
 * @param idDomicilio id interno en la base de datos
 * @param idUsuario id usuario que realiza la accion
 * @return devuelve un string indicando el exito de la operación o no
 */
public String addRelacionClienteDomicilio(long idCliente, long idDomicilio,int idUsuario) {
        String retorno = "NADA";
        try {
            GregorianCalendar calendario = new GregorianCalendar(Locale.getDefault());
            PersonasdomiciliosPK perpk = new PersonasdomiciliosPK(idDomicilio, idCliente);
            PersonasDomicilios personadomici = renovarDomicilio(idDomicilio,idCliente,idUsuario);
                PersonasDomicilios personadomicilio = new PersonasDomicilios();
                personadomicilio.setDomicilioss(em.find(Domicilios.class, idDomicilio));
                personadomicilio.setEstado("Habitable".toUpperCase());
                personadomicilio.setPersonasdomiciliosPK(perpk);
                personadomicilio.setFechaingresovivienda(calendario.getTime());
                personadomicilio.setPersonas(em.find(Clientes.class, idCliente));
                em.persist(personadomicilio);
                em.flush();
                retorno ="InyectóRelacion";
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
           perdomi = em.find(PersonasDomicilios.class, new PersonasdomiciliosPK(idDomicilio, idCliente));
            Query consulta = em.createQuery("SELECT p FROM PersonasDomicilios p WHERE p.personasdomiciliosPK.idPersona = :idPersona");
            consulta.setParameter("idPersona", idCliente);
            List<PersonasDomicilios>lista= consulta.getResultList();
            PersonasDomicilios personasDomicilios = null;
            if(!lista.isEmpty()){
                    for (Iterator<PersonasDomicilios> it = lista.iterator(); it.hasNext();) {
                         personasDomicilios = it.next();
                        ejbhistperdom.addOneHomePerson(Integer.valueOf(String.valueOf(personasDomicilios.getDomicilioss().getId())),Integer.valueOf(String.valueOf(personasDomicilios.getPersonas().getIdPersona())), idUsuario);
                        em.remove(personasDomicilios);
                    }
            }
            em.flush();
            perdomi=personasDomicilios;
         } catch (Exception e) {
            logger.error("Error en metodo renovarDomicilio EJBClienteDomicilio "+e);
         }finally{
            return perdomi;
         }
    }
}