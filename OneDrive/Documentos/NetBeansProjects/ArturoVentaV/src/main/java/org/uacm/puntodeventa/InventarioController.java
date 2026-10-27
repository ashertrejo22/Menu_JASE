/*
 * Clase: InventarioController
 * Versión: 1.0
 * Fecha: 25/10/2026
 * Copyright: Proyecto Sistema de ventas, UACM.
 */
package org.uacm.puntodeventa;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import org.uacm.puntodeventa.Clases.InformacionProductos;
import org.uacm.puntodeventa.Clases.Producto;

/**
 * Controlador de la pantalla de inventario. Muestra la tabla de
 * productos, permite buscar, registrar, actualizar y eliminar
 * productos, y avisa cuando hay productos por reabastecer.
 *
 * @author josem
 * @version 1.0, 25/10/2026
 */
public class InventarioController implements Initializable {

    /** Campo de texto para buscar productos por nombre o ID. */
    @FXML
    private TextField txtFieldBuscar;

    /** Botón para registrar un producto nuevo. */
    @FXML
    private Button btnNuevo;

    /** Botón para actualizar el producto seleccionado. */
    @FXML
    private Button btnActualizar;

    /** Botón para eliminar el producto seleccionado. */
    @FXML
    private Button btnEliminar;

    /** Tabla que muestra los productos del inventario. */
    @FXML
    private TableView<Producto> tableViewProductos;

    /** Columna del nombre del producto. */
    @FXML
    private TableColumn<Producto, String> columnaNombre;

    /** Columna del precio de venta. */
    @FXML
    private TableColumn<Producto, Double> columnaPrecio;

    /** Columna de existencias. */
    @FXML
    private TableColumn<Producto, Integer> columnaStock;

    /** Columna del tipo de producto. */
    @FXML
    private TableColumn<Producto, String> columnaTipo;

    /** Columna del identificador del producto. */
    @FXML
    private TableColumn<Producto, Integer> columnaide;

    /** Lista observable con todos los productos. */
    ObservableList<Producto> list = InformacionProductos.getInstance().getProductos();

    /** Lista filtrada que se muestra en la tabla. */
    private FilteredList<Producto> filteredProductos;

    /** Nivel de permisos del usuario actual. */
    private int nivelUsuarioActual;

    /** Etiqueta de información de la pantalla. */
    @FXML
    private Label lblInfo;

    /** Botón de notificaciones; cambia de color según las alertas. */
    @FXML
    private Button notificacion;

    /**
     * Inicializa el controlador: configura las columnas, el filtro de
     * búsqueda, la apertura de detalles con doble clic y el color del
     * botón de notificaciones.
     *
     * @param url ubicación usada para resolver rutas relativas
     * @param rb recursos usados para localizar el objeto raíz
     */
@Override
public void initialize(URL url, ResourceBundle rb) {

    // Configurar las columnas de la tabla
    columnaNombre.setCellValueFactory(
            new PropertyValueFactory<>("nombre"));

    columnaStock.setCellValueFactory(
            new PropertyValueFactory<>("stock"));

    columnaide.setCellValueFactory(
            new PropertyValueFactory<>("ID_producto"));

    columnaTipo.setCellValueFactory(
            new PropertyValueFactory<>("tipo"));

    columnaPrecio.setCellValueFactory(
            new PropertyValueFactory<>("precio_venta"));

    // Configurar el filtro de productos
    filteredProductos = new FilteredList<>(list, p -> true);

    tableViewProductos.setItems(filteredProductos);

    // Buscar productos conforme se escribe
    txtFieldBuscar.textProperty().addListener(
            (obs, oldVal, newVal) -> {
                aplicarFiltro(newVal);
            });

    // Abrir los detalles del producto con doble clic
    tableViewProductos.setOnMouseClicked(event -> {

        if (event.getClickCount() == 2) {

            Producto productoSeleccionado =
                    tableViewProductos.getSelectionModel()
                            .getSelectedItem();

            if (productoSeleccionado != null) {

                try {
                    FXMLLoader loader = new FXMLLoader(
                            getClass().getResource(
                                    "/pantallas/DetalleProducto.fxml"));

                    Parent root = loader.load();

                    DetalleProductoController controller =
                            loader.getController();

                    controller.setProducto(productoSeleccionado);

                    Stage stage = new Stage();
                    stage.setTitle("Detalles del Producto");
                    stage.setScene(new Scene(root));
                    stage.setResizable(false);
                    stage.show();

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    });

    // Actualizar el color inicial del botón Notificaciones
    actualizarColorNotificaciones();

    // Actualizar el color cuando cambia la lista de productos
    list.addListener(
            (javafx.collections.ListChangeListener<Producto>)
            cambio -> actualizarColorNotificaciones()
    );
}

    /**
     * Filtra la tabla por nombre o ID del producto.
     *
     * @param texto texto a buscar; si es null o vacío muestra todos
     */
    private void aplicarFiltro(String texto) {
        if (texto == null || texto.trim().isEmpty()) {
            filteredProductos.setPredicate(p -> true);  // Muestra todos
            return;
        }
        String lower = texto.toLowerCase();
        filteredProductos.setPredicate(p
                -> p.getNombre().toLowerCase().contains(lower)
                || String.valueOf(p.getID_producto()).contains(lower)
        );
    }

    /**
     * Abre la ventana para registrar un producto nuevo.
     *
     * @param event evento del botón Nuevo
     */
    @FXML
    private void click(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/pantallas/ProductoNuevo.fxml"));
            Parent root = loader.load();
            ProductoNuevoController controller = loader.getController();
            controller.setProductoList(this.list);
            Stage stage = new Stage();
            stage.setTitle("Registrar Nuevo Producto");
            stage.setScene(new Scene(root));
            stage.show();

        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Error al cargar ProductoNuevo.fxml");
        }
    }

    /**
     * Abre la ventana para eliminar el producto seleccionado.
     *
     * @param event evento del botón Eliminar
     */
    @FXML
    private void eliminar(ActionEvent event) {
        Producto seleccionado = tableViewProductos.getSelectionModel().getSelectedItem();
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/pantallas/Eliminar.fxml"));
            Parent root = loader.load();
            EliminarController controller = loader.getController();
            controller.setProducto(seleccionado, this.list);
            Stage stage = new Stage();
            stage.setTitle("Eliminar Producto");
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Configura los permisos según el nivel del usuario. El nivel 2
     * deshabilita los botones Nuevo, Actualizar y Eliminar.
     *
     * @param nivel nivel de permisos del usuario
     */
    public void configurarPermisos(int nivel) {
        this.nivelUsuarioActual = nivel;
        if (nivel == 2) {
            btnNuevo.setDisable(true);
            btnActualizar.setDisable(true);
            btnEliminar.setDisable(true);
        }
    }

    /**
     * Abre la ventana para actualizar el producto seleccionado. No
     * hace nada si no hay un producto seleccionado.
     *
     * @param event evento del botón Actualizar
     */
    @FXML
    private void actualizarProducto(ActionEvent event) {
        Producto seleccionado = tableViewProductos.getSelectionModel().getSelectedItem();
        if (seleccionado == null) {
            return;
        }
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/pantallas/Actualizar.fxml"));
            Parent root = loader.load();
            ActualizarController controller = loader.getController();
            controller.setProducto(seleccionado);
            controller.setListaProductos(this.list);  // pasa la lista observable
            Stage stage = new Stage();
            stage.setTitle("Actualizar Producto");
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Abre la ventana con los productos que necesitan reabastecerse.
     *
     * @param event evento del botón Notificaciones
     */
    @FXML
    private void Notificaciones(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(
                    getClass().getResource(
                            "/pantallas/ProductosNotificaciones.fxml"
                    )
            );
            Parent root = loader.load();
            ProductosNotificacionesController controller
                    = loader.getController();
            Stage stage = new Stage();
            stage.setTitle("Productos por actualizar..");
            stage.setScene(
                    new Scene(root)
            );
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println(
                    "Error al cargar ProductosNotificaciones.fxml"
            );
        }
    }
    
    /**
     * Pone el botón de notificaciones en rojo si algún producto
     * necesita reabastecimiento, y en verde si no hay alertas.
     */
    private void actualizarColorNotificaciones() {

    boolean hayAlertas = false;

    for (Producto producto : list) {

        if (producto != null
                && producto.necesitaReabastecimiento()) {

            hayAlertas = true;
            break;
        }
    }

    if (hayAlertas) {
        notificacion.setStyle(
                "-fx-background-color: #e53935;"
                + "-fx-text-fill: white;"
        );
    } else {
        notificacion.setStyle(
                "-fx-background-color: #4caf50;"
                + "-fx-text-fill: white;"
        );
    }
}
}