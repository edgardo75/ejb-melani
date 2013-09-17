/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.melani.ejb;
import com.melani.entity.TarjetasCreditoDebito;
import java.util.Iterator;
import java.util.List;
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
@Stateless(name="ejb/EJBTarjetasBean")
@WebService(serviceName="ServiceTarjetas",name="TartjetasWs")
@SOAPBinding(style=SOAPBinding.Style.RPC)
public class EJBTarjetasBean implements EJBTarjetasRemote {
    private static Logger logger = Logger.getLogger(EJBTarjetasBean.class);
    @PersistenceContext
    private EntityManager em;
    public String searchAllTarjetasCreditoDebito() {
        String xml = "<Lista>\n";
        try {
            Query consulta = em.createQuery("SELECT t FROM TarjetasCreditoDebito t Order by t.idtarjeta");
            List<TarjetasCreditoDebito>lista = consulta.getResultList();
            if(lista.size()>0){
                for (Iterator<TarjetasCreditoDebito> it = lista.iterator(); it.hasNext();) {
                    TarjetasCreditoDebito tarjetasCreditoDebito = it.next();
                    xml+=tarjetasCreditoDebito.toXML();
                }
            }else
                xml+="no hay tarjetas";
            xml+="</Lista>\n";
        } catch (Exception e) {
            logger.error("Error en metodo searchalltarjetascreditodebito", e.getCause());
        }finally{
            return xml;
        }
    }
}
