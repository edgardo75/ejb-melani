/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.melani.ejb;
import com.melani.entity.Porcentajes;
import javax.ejb.Stateless;
import javax.jws.WebService;
import javax.jws.soap.SOAPBinding;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import org.apache.log4j.Logger;
/**
 *
 * @author Edgardo
 */
@Stateless(name="ejb/EJBPorcentajesBean")
@WebService(serviceName="ServicePorcentajes",name="PorcentajesWs")
@SOAPBinding(style=SOAPBinding.Style.RPC)
public class EJBPorcentajesBean implements EJBPorcentajesRemote {
    private static Logger logger = Logger.getLogger(EJBPorcentajesBean.class);
    @PersistenceContext
    private EntityManager em;
    public short agregarDatosPorcenta(String descripcion, double valor) {
        short retorno =0;
        try {
            Porcentajes porcentaje = new Porcentajes();
            porcentaje.setDescripcion(descripcion.toUpperCase());
            porcentaje.setValor(valor);
            em.persist(porcentaje);
            retorno = porcentaje.getIdPorcentajes();
        } catch (Exception e) {
            logger.error("Error en metodo agregarDatosPorcenta, verifique", e.getCause());
        }finally{
        return retorno;
        }
    }
}
