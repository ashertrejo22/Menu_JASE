package org.uacm.puntodeventa.Clases;

import java.time.LocalDate; // Importante para las fechas de los perecederos
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

/**
 *
 * @author laloa
 */
public class InformacionProductos {
    private static InformacionProductos instancia;
    private ObservableList<Producto> productos;

    private InformacionProductos() {
        productos = FXCollections.observableArrayList();
        productos.addAll(
            new Producto(1, "Agua", "Normal", 25.0, 20.0),
            new Producto(2, "Cereal", "Normal", 15.0, 35.0),
            
            new ProductoPerecedero(4, "Caja de donas", "Perecedero", 20.0, 145.0, LocalDate.now().plusDays(5), "L-100"),
            
            new Producto(5, "Papas", "Normal", 20.0, 14.0),
            new Producto(6, "Coca-Cola", "Normal", 50.0, 18.0),
  
            new Producto(8, "Jugo de manzana", "Normal", 15.0, 11.0), 
            new Producto(9, "Cajetilla de cigarros", "Normal", 5.0, 58.0),
            new Producto(10, "Emperador", "Normal", 28.0, 14.0),
 
            new ProductoPerecedero(11, "Leche", "Perecedero", 20.0, 18.0, LocalDate.now().plusDays(15), "L-999"),
            
            new Producto(12, "Tortillas de harina", "Normal", 20.0, 20.0),

            new ProductoGranel(13, "Jamon Fino", "Granel", 10.5, 120.0, "Kg"),
            new ProductoGranel(14, "Queso Oaxaca", "Granel", 5.250, 180.0, "Kg")
        );
    }

    public static InformacionProductos getInstance() {
        if (instancia == null) instancia = new InformacionProductos();
        return instancia;
    }

    public ObservableList<Producto> getProductos() {
        return productos;
    }
}