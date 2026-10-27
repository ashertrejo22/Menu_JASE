/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package org.uacm.puntodeventa.Clases;

/**
 *
 * @author ashje
 */
public class ProductoGranel extends Producto{
    String unidadMedida;
    public ProductoGranel(int id, String nom, String tip, double stock, double precio_pro, String uM) {
        super(id, nom, tip, stock, precio_pro);
        this.unidadMedida = uM;
    }
    
    public double calcularPrecioExacto(double pesoVendido) {
        return super.getPrecio_venta() * pesoVendido; 
    }

    public String getUnidadMedida() {
        return unidadMedida;
    }

    public void setUnidadMedida(String unidadMedida) {
        this.unidadMedida = unidadMedida;
    }
    
    @Override
    public String mostrarDetalles() {
        return super.mostrarDetalles() + "\nUnidad de medida: " + this.unidadMedida;
    }
    
    
}
