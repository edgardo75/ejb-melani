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

            TelefonosPK telepk = new TelefonosPK(Long.valueOf(datosTel.getNumero()),Long.valueOf(datosTel.getPrefijo()));
            //-----------------------------------------------------------------------------
            if(em.find(Telefonos.class, telepk)!=null)
                retorno =1;
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
            retorno =-1;
            logger.error("Error en metodo addTelefonos, EJBTelefonos "+e);
        }finally{
            return retorno;
        }
    }


 
}
