package org.uacm.puntodeventa.Clases;

import java.time.LocalDate;

public class ProductoPerecedero extends Producto {
    
    private LocalDate fechaCaducidad;
    private String lote;
    public double precioConDescuento = 0;
    
    public ProductoPerecedero(int id, String nom, String tip, double stock, double precio_pro, LocalDate fech, String lot) {
        super(id, nom, tip, stock, precio_pro);
        this.fechaCaducidad = fech;
        this.lote = lot;
    }

    public LocalDate getFechaCaducidad() {
        return fechaCaducidad;
    }

    public void setFechaCaducidad(LocalDate fechaCaducidad) {
        this.fechaCaducidad = fechaCaducidad;
    }

    public String getLote() {
        return lote;
    }

    public void setLote(String lote) {
        this.lote = lote;
    }

    public double getPrecioConDescuento() {
        return precioConDescuento;
    }

    public void setPrecioConDescuento(double precioConDescuento) {
        this.precioConDescuento = precioConDescuento;
    }
    
    public boolean validarCaducidad() {
        return fechaCaducidad.isBefore(LocalDate.now());
    }
    
    public void aplicarDescuento() {
        this.precioConDescuento = super.getPrecio_venta();
        if (fechaCaducidad.isBefore(LocalDate.now().plusDays(7))) {
            this.precioConDescuento *= 0.85; 
        }
    }    

    @Override
    public String mostrarDetalles() {
        return super.mostrarDetalles() + "\nCaducidad: " + this.fechaCaducidad + "\nLote: " + this.lote;
    }
}