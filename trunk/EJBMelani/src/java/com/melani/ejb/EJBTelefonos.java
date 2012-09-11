/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.melani.ejb;

import cm.melani.utils.DatosTelefonos;
import com.melani.entity.EmpresaTelefonia;
import com.melani.entity.Telefonos;
import com.melani.entity.TelefonosPK;
import com.melani.entity.Tipostelefono;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import org.apache.log4j.Logger;

/**
 *
 * @author Edgardo
 */
@Stateless(name="ejb/EJBTelefonos")
public class EJBTelefonos implements EJBTelefonosRemote {
    private static Logger logger = Logger.getLogger(EJBTelefonos.class);
    @PersistenceContext
    private EntityManager em;

    public long addTelefonos(DatosTelefonos datosTel) {
        long retorno =0;
        try {
            //-----------------------------------------------------------------------------
           /* XStream xstream = new XStream();
            xstream.alias("telefono", DatosTelefonos.class);
            DatosTelefonos datosTel = (DatosTelefonos) xstream.fromXML(xmlTelefono);*/
            //-----------------------------------------------------------------------------

            TelefonosPK telepk = new TelefonosPK(Long.valueOf(datosTel.getNumero().trim()),Long.valueOf(datosTel.getPrefijo().trim()));
            //-----------------------------------------------------------------------------
            //Telefonos tel = em.find(Telefonos.class, telepk);
            Query consulta = em.createQuery("SELECT t FROM Telefonos t WHERE t.telefonosPK.idPrefijo = :idPrefijo and " +
                    "t.telefonosPK.numero = :numero");
            consulta.setParameter("idPrefijo", Long.valueOf(datosTel.getPrefijo().trim()));
            consulta.setParameter("numero", Long.valueOf(datosTel.getNumero().trim()));
            
            if(consulta.getResultList().size()==1)
                retorno = 1;
            else{
                Telefonos telefono = new Telefonos();
                telefono.setIdEmpresatelefonia(em.find(EmpresaTelefonia.class, datosTel.getIdEmpresaTelefonia().getIdempresatelefonia()));
                telefono.setIdTipotelefono(em.find(Tipostelefono.class, datosTel.getTipoTelefono().getTipoTelefono()));
                telefono.setTelefonosPK(telepk);
                em.persist(telefono);
                em.flush();
                retorno = 2;
                logger.info("Telefono agregado con exito "+datosTel.getNumero()+" "+datosTel.getPrefijo());
            }
            //--------------------------------------------------------------------------------------

        } catch (Exception e) {
            retorno = -1;
            logger.error("Error en metodo addTelefonos, EJBTelefonos "+e);
        }finally{
            return retorno;
        }
    }


 
}
