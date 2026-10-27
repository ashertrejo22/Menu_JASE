/*
 * Clase: Producto
 * Versión: 1.0
 * Fecha: 25/10/2026
 * Copyright: Proyecto Sistema de ventas, UACM.
 */
package org.uacm.puntodeventa.Clases;

import java.time.LocalDate;

/**
 * Representa un producto del inventario, con sus precios, existencias
 * y datos de alertas y reabastecimiento.
 *
 * @author Jaasiel VC
 * @version 1.0, 25/10/2026
 */
public class Producto {

    /** Identificador del producto. */
    private int ID_producto;

    /** Nombre del producto. */
    private String nombre;

    /** Tipo o categoría del producto. */
    private String tipo;

    /** Existencias actuales. */
    private double stock;

    /** Precio de compra al proveedor. */
    private double precio_prov;

    /** Precio de venta al público (precio del proveedor + 20 %). */
    private double precio_venta;

    /** Ruta de la imagen del producto; null si no tiene. */
    private String rutaImagen;

    // Datos del módulo de alertas y reabastecimiento

    /** Existencias mínimas antes de generar una alerta. */
    private double stockMinimo;

    /** Proveedor del producto. */
    private String proveedor;

    /** Cantidad que se solicita al reabastecer. */
    private double cantidadReabastecimiento;

    /**
     * Constructor principal. Calcula el precio de venta e inicializa
     * los valores de alertas y reabastecimiento.
     *
     * @param id identificador del producto
     * @param nom nombre del producto
     * @param tip tipo del producto
     * @param stock existencias iniciales
     * @param precio_pro precio del proveedor
     * @throws IllegalArgumentException si el stock es negativo
     */
    public Producto(int id, String nom, String tip,
                    double stock, double precio_pro) {

        if (stock < 0) {
            throw new IllegalArgumentException(
                    "El stock no puede ser negativo."
            );
        }

        this.ID_producto = id;
        this.nombre = nom;
        this.tipo = tip;
        this.stock = stock;
        this.precio_prov = precio_pro;
        this.precio_venta = calcularPrecioVenta(precio_pro);
        this.rutaImagen = null;

        // Valores iniciales para alertas y reabastecimiento
        this.stockMinimo = 10.0;
        this.proveedor = "Proveedor general";
        this.cantidadReabastecimiento = 20.0;
    }

    /**
     * Constructor para productos con imagen y fecha de caducidad.
     *
     * @param id identificador del producto
     * @param nom nombre del producto
     * @param tip tipo del producto
     * @param stock existencias iniciales
     * @param precio_pro precio del proveedor
     * @param ruta ruta de la imagen del producto
     * @param caducidad fecha de caducidad del producto
     */
    public Producto(int id, String nom, String tip, int stock,
                    double precio_pro, String ruta,
                    LocalDate caducidad) {

        this(id, nom, tip, (double) stock, precio_pro);
        this.rutaImagen = ruta;
    }

    /**
     * Calcula el precio de venta con un incremento del 20 %.
     *
     * @param precio precio del proveedor
     * @return precio de venta redondeado hacia arriba
     */
    public double calcularPrecioVenta(double precio) {
        return Math.ceil(precio + precio * 0.2);
    }

    /**
     * Reduce el stock si hay existencias suficientes.
     *
     * @param cantidad cantidad a descontar
     * @return true si se descontó; false si la cantidad no es válida o
     *         no hay existencias suficientes
     */
    public boolean reducirStock(double cantidad) {

        if (cantidad <= 0) {
            return false;
        }

        if (this.stock >= cantidad) {
            this.stock -= cantidad;
            return true;
        }

        return false;
    }

    /**
     * Obtiene el identificador del producto.
     *
     * @return identificador del producto
     */
    public int getID_producto() {
        return ID_producto;
    }

    /**
     * Establece el identificador del producto.
     *
     * @param ID_producto nuevo identificador
     */
    public void setID_producto(int ID_producto) {
        this.ID_producto = ID_producto;
    }

    /**
     * Obtiene el nombre del producto.
     *
     * @return nombre del producto
     */
    public String getNombre() {
        return nombre;
    }

    /**
     * Establece el nombre del producto.
     *
     * @param nombre nuevo nombre
     */
    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    /**
     * Obtiene el tipo del producto.
     *
     * @return tipo del producto
     */
    public String getTipo() {
        return tipo;
    }

    /**
     * Establece el tipo del producto.
     *
     * @param tipo nuevo tipo
     */
    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    /**
     * Obtiene las existencias actuales.
     *
     * @return existencias actuales
     */
    public double getStock() {
        return stock;
    }

    /**
     * Establece las existencias del producto.
     *
     * @param stock nuevas existencias
     * @throws IllegalArgumentException si el stock es negativo
     */
    public void setStock(double stock) {

        if (stock < 0) {
            throw new IllegalArgumentException(
                    "El stock no puede ser negativo."
            );
        }

        this.stock = stock;
    }

    /**
     * Agrega existencias al inventario.
     *
     * @param cantidad cantidad a agregar
     * @throws IllegalArgumentException si la cantidad no es mayor
     *         que cero
     */
    public void agregarStock(double cantidad) {

        if (cantidad <= 0) {
            throw new IllegalArgumentException(
                    "La cantidad debe ser mayor que cero."
            );
        }

        this.stock += cantidad;
    }

    /**
     * Obtiene el precio del proveedor.
     *
     * @return precio del proveedor
     */
    public double getPrecio_prov() {
        return precio_prov;
    }

    /**
     * Establece el precio del proveedor y recalcula el precio de
     * venta.
     *
     * @param precio_prov nuevo precio del proveedor
     */
    public void setPrecio_prov(double precio_prov) {
        this.precio_prov = precio_prov;
        this.precio_venta = calcularPrecioVenta(precio_prov);
    }

    /**
     * Obtiene el precio de venta.
     *
     * @return precio de venta
     */
    public double getPrecio_venta() {
        return precio_venta;
    }

    /**
     * Obtiene la ruta de la imagen del producto.
     *
     * @return ruta de la imagen; null si no tiene
     */
    public String getRutaImag() {
        return rutaImagen;
    }

    /**
     * Establece la ruta de la imagen del producto.
     *
     * @param ruta nueva ruta de la imagen
     */
    public void setRutaImag(String ruta) {
        this.rutaImagen = ruta;
    }

    /**
     * Determina si el producto requiere reabastecimiento.
     *
     * @return true si el stock es menor o igual al stock mínimo
     */
    public boolean necesitaReabastecimiento() {
        return this.stock <= this.stockMinimo;
    }

    /**
     * Devuelve el estado actual del inventario del producto.
     *
     * @return "Sin existencias", "Stock bajo" o "Stock normal"
     */
    public String getEstadoInventario() {

        if (this.stock <= 0) {
            return "Sin existencias";
        }

        if (necesitaReabastecimiento()) {
            return "Stock bajo";
        }

        return "Stock normal";
    }

    /**
     * Obtiene el stock mínimo.
     *
     * @return stock mínimo
     */
    public double getStockMinimo() {
        return stockMinimo;
    }

    /**
     * Establece el stock mínimo.
     *
     * @param stockMinimo nuevo stock mínimo
     * @throws IllegalArgumentException si el valor es negativo
     */
    public void setStockMinimo(double stockMinimo) {

        if (stockMinimo < 0) {
            throw new IllegalArgumentException(
                    "El stock mínimo no puede ser negativo."
            );
        }

        this.stockMinimo = stockMinimo;
    }

    /**
     * Obtiene el proveedor del producto.
     *
     * @return proveedor del producto
     */
    public String getProveedor() {
        return proveedor;
    }

    /**
     * Establece el proveedor del producto, sin espacios sobrantes.
     *
     * @param proveedor nuevo proveedor
     * @throws IllegalArgumentException si es null o está vacío
     */
    public void setProveedor(String proveedor) {

        if (proveedor == null || proveedor.trim().isEmpty()) {
            throw new IllegalArgumentException(
                    "El proveedor no puede estar vacío."
            );
        }

        this.proveedor = proveedor.trim();
    }

    /**
     * Obtiene la cantidad que se solicita al reabastecer.
     *
     * @return cantidad de reabastecimiento
     */
    public double getCantidadReabastecimiento() {
        return cantidadReabastecimiento;
    }

    /**
     * Establece la cantidad que se solicita al reabastecer.
     *
     * @param cantidadReabastecimiento nueva cantidad
     * @throws IllegalArgumentException si no es mayor que cero
     */
    public void setCantidadReabastecimiento(
            double cantidadReabastecimiento) {

        if (cantidadReabastecimiento <= 0) {
            throw new IllegalArgumentException(
                    "La cantidad de reabastecimiento debe ser mayor que cero."
            );
        }

        this.cantidadReabastecimiento = cantidadReabastecimiento;
    }

    /**
     * Genera un texto con los detalles del producto.
     *
     * @return detalles del producto, uno por línea
     */
    public String mostrarDetalles() {

        return "\nID: " + this.ID_producto
                + "\nNombre: " + this.nombre
                + "\nTipo: " + this.tipo
                + "\nStock: " + this.stock
                + "\nStock mínimo: " + this.stockMinimo
                + "\nProveedor: " + this.proveedor
                + "\nCantidad de reabastecimiento: "
                + this.cantidadReabastecimiento
                + "\nEstado: " + getEstadoInventario()
                + "\nPrecio proveedor: " + this.precio_prov
                + "\nPrecio venta: " + this.precio_venta;
    }
}