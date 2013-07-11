/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.melani.ejb;

import com.melani.entity.Productos;

import javax.ejb.Remote;

/**
 *
 * @author Edgardo
 */
@Remote
public interface EJBProductosRemote {

    long addProductoConImage(String nombre, int cantini, String cantactual, float precio);

    long addExistenciasProducto(int idproducto, int cantidad,float precio,int idusuario);

    String leerImagenBaseDatos(int idProducto);

    String addProducto(String xmlProducto);

    String selectoneproducto(long idproducto);

    Productos agregarProductos(Productos producto);

    String searchAllProductos();

    int controlStockProducto(long idProducto, int cantidad, int idUsuario);

    String actualizarProducto(String xmlProducto);

    String ShowReportProduct();

    int grabarImagen(int id_producto, byte[] longitudImagen);

    byte[] obtenerImagenProducto(int idProducto);
    
}
