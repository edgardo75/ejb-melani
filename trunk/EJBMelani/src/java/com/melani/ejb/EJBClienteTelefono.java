/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.melani.ejb;

import com.melani.entity.Clientes;
import com.melani.entity.Personastelefonos;
import com.melani.entity.PersonastelefonosPK;
import com.melani.entity.Telefonos;
import com.melani.entity.TelefonosPK;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import org.apache.log4j.Logger;

/**
 *
 * @author Edgardo
 */
@Stateless(name="ejb/EJBClienteTelefono")
public class EJBClienteTelefono implements EJBClienteTelefonoRemote {
    private static Logger logger = Logger.getLogger(EJBClienteTelefono.class);
    @PersistenceContext
    private EntityManager em;

    public String addClienteTelefono(String numero, String prefijo, long idcliente) {
        String retorno = "NADA";
        try {
            PersonastelefonosPK persotelpk = new PersonastelefonosPK(Long.valueOf(numero), Long.valueOf(prefijo), idcliente);
            TelefonosPK telepk = new TelefonosPK(Long.valueOf(numero), Long.valueOf(prefijo));

            if(em.find(Personastelefonos.class, persotelpk)!=null)
                retorno ="RelacionTelefonoExistente";
            else{
                Personastelefonos personatel = new Personastelefonos();
                    personatel.setDetalles("TelefonoCliente".toUpperCase());
                    personatel.setEstado("Activo".toUpperCase());
                    personatel.setIdPersona(em.find(Clientes.class, idcliente));
                    personatel.setPersonastelefonosPK(persotelpk);
                    personatel.setTelefonos(em.find(Telefonos.class, telepk));
                    em.persist(personatel);
                    em.flush();
                    retorno = "InyectoRelacionClienteTelefono";
                    logger.info("Inyecto Relacion Telefono "+prefijo+" "+numero+" para el cliente "+idcliente);

            }

        } catch (Exception e) {
            retorno = "Error";
            logger.error("Error en metodo addClienteTelefono "+e);
        }finally{
            return retorno;
        }
    }



 
}
