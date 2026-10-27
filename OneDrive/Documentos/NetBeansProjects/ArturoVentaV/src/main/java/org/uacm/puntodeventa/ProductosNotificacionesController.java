/*
 * Clase: ProductosNotificacionesController
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
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

import org.uacm.puntodeventa.Clases.AlertasInventario;
import org.uacm.puntodeventa.Clases.InformacionProductos;
import org.uacm.puntodeventa.Clases.PedidoReabastecimiento;
import org.uacm.puntodeventa.Clases.Producto;

/**
 * Controlador de la pantalla de notificaciones de inventario. Muestra
 * los productos que necesitan reabastecimiento, permite generar el
 * pedido con su ticket y registrar la recepción de mercancía.
 *
 * @author Jaasiel VC
 * @version 1.0, 25/10/2026
 */
public class ProductosNotificacionesController
        implements Initializable {

    /** Tabla con los productos que requieren reabastecimiento. */
    @FXML private TableView<Producto> tableViewProductos;

    /** Columna del identificador del producto. */
    @FXML private TableColumn<Producto, Integer> columnaId;

    /** Columna del nombre del producto. */
    @FXML private TableColumn<Producto, String> columnaNombre;

    /** Columna de existencias actuales. */
    @FXML private TableColumn<Producto, Double> columnaStock;

    /** Columna del stock mínimo. */
    @FXML private TableColumn<Producto, Double> columnaMinimo;

    /** Columna del proveedor. */
    @FXML private TableColumn<Producto, String> columnaProveedor;

    /** Columna de la cantidad a reabastecer. */
    @FXML private TableColumn<Producto, Double> columnaCantidad;

    /** Columna del estado del inventario. */
    @FXML private TableColumn<Producto, String> columnaEstado;

    /** Botón para actualizar la tabla. */
    @FXML private Button btnActuStock;

    /** Botón para generar el pedido del producto seleccionado. */
    @FXML private Button btnGenerarPedido;

    /** Botón para registrar la recepción de mercancía. */
    @FXML private Button btnPedidosAutomaticos;

    /** Etiqueta con el resumen de productos en alerta. */
    @FXML private Label lblInfo;

    /** Lista de todos los productos del inventario. */
    private final ObservableList<Producto> productos =
            InformacionProductos.getInstance().getProductos();

    /** Pedidos generados durante la sesión. */
    private final ObservableList<PedidoReabastecimiento> pedidosGenerados =
            FXCollections.observableArrayList();

    /**
     * Inicializa el controlador: configura las columnas de la tabla y
     * carga los productos en alerta.
     *
     * @param url ubicación usada para resolver rutas relativas
     * @param rb recursos usados para localizar el objeto raíz
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {

        columnaId.setCellValueFactory(
                new PropertyValueFactory<>("ID_producto"));

        columnaNombre.setCellValueFactory(
                new PropertyValueFactory<>("nombre"));

        columnaStock.setCellValueFactory(
                new PropertyValueFactory<>("stock"));

        columnaMinimo.setCellValueFactory(
                new PropertyValueFactory<>("stockMinimo"));

        columnaProveedor.setCellValueFactory(
                new PropertyValueFactory<>("proveedor"));

        columnaCantidad.setCellValueFactory(
                new PropertyValueFactory<>("cantidadReabastecimiento"));

        columnaEstado.setCellValueFactory(
                new PropertyValueFactory<>("estadoInventario"));

        actualizarTabla();
    }

    /**
     * Muestra únicamente los productos cuyo stock está en o debajo del
     * mínimo, y actualiza el mensaje informativo.
     */
    private void actualizarTabla() {

        ObservableList<Producto> alertas =
                AlertasInventario.obtenerProductosConAlerta(productos);

        tableViewProductos.setItems(alertas);
        tableViewProductos.refresh();

        int cantidad = alertas.size();

        if (cantidad == 0) {
            lblInfo.setText(
                    "No hay productos que necesiten reabastecimiento.");
        } else {
            lblInfo.setText(
                    "Hay " + cantidad
                    + " producto(s) que requieren atención.");
        }
    }

    /**
     * Acción del botón Actualizar: vuelve a cargar la tabla.
     *
     * @param event evento del botón Actualizar
     */
    @FXML
    private void actualizarStock(ActionEvent event) {
        actualizarTabla();
    }

    /**
     * Genera un pedido para el producto seleccionado y abre el
     * ticket. Muestra una alerta si no hay producto seleccionado o si
     * el pedido no es válido.
     *
     * @param event evento del botón Generar pedido
     */
    @FXML
    private void generarPedido(ActionEvent event) {

        Producto seleccionado =
                tableViewProductos.getSelectionModel()
                        .getSelectedItem();

        if (seleccionado == null) {
            mostrarAlerta(
                    Alert.AlertType.WARNING,
                    "Producto no seleccionado",
                    "Selecciona un producto de la tabla."
            );
            return;
        }

        try {
            PedidoReabastecimiento pedido =
                    AlertasInventario.generarPedido(seleccionado);

            pedidosGenerados.add(pedido);
            abrirTicketPedido(pedido);

        } catch (IllegalArgumentException ex) {
            mostrarAlerta(
                    Alert.AlertType.WARNING,
                    "No se pudo generar el pedido",
                    ex.getMessage()
            );
        }
    }

    /**
     * Simula la recepción de mercancía para el producto seleccionado:
     * suma al stock la cantidad de reabastecimiento y actualiza la
     * tabla.
     *
     * @param event evento del botón de recepción de mercancía
     */
    @FXML
    private void generarPedidosAutomaticos(ActionEvent event) {

        Producto seleccionado =
                tableViewProductos.getSelectionModel()
                        .getSelectedItem();

        if (seleccionado == null) {
            mostrarAlerta(
                    Alert.AlertType.WARNING,
                    "Producto no seleccionado",
                    "Selecciona el producto al que deseas actualizarle el stock."
            );
            return;
        }

        double cantidad = seleccionado.getCantidadReabastecimiento();

        if (cantidad <= 0) {
            mostrarAlerta(
                    Alert.AlertType.WARNING,
                    "Cantidad incorrecta",
                    "La cantidad de reabastecimiento debe ser mayor que cero."
            );
            return;
        }

        // Suma al inventario la cantidad configurada para reabastecimiento.
        seleccionado.agregarStock(cantidad);

        // Vuelve a filtrar la tabla con el stock actualizado.
        actualizarTabla();

        mostrarAlerta(
                Alert.AlertType.INFORMATION,
                "Stock actualizado",
                "Producto: " + seleccionado.getNombre()
                + "\nCantidad recibida: " + cantidad
                + "\nStock actual: " + seleccionado.getStock()
                + (seleccionado.necesitaReabastecimiento()
                    ? "\nEl producto sigue necesitando reabastecimiento."
                    : "\nEl producto ya no aparece en las alertas.")
        );
    }

    /**
     * Abre el ticket del pedido usando la plantilla ticket.fxml.
     *
     * @param pedido pedido que se muestra en el ticket
     */
    private void abrirTicketPedido(PedidoReabastecimiento pedido) {

        try {
            FXMLLoader loader = new FXMLLoader(
                    getClass().getResource("/pantallas/ticket.fxml"));

            Parent root = loader.load();

            TicketController controller = loader.getController();

            controller.setTicketTexto("TICKET DE PEDIDO");
            controller.setTicketTexto(crearTextoTicket(pedido));

            Stage stage = new Stage();
            stage.setTitle("Pedido - Folio " + pedido.getFolio());
            stage.setScene(new Scene(root));
            stage.setResizable(false);
            stage.show();

        } catch (IOException ex) {
            ex.printStackTrace();

            mostrarAlerta(
                    Alert.AlertType.ERROR,
                    "Error al abrir el ticket",
                    "No se pudo cargar /pantallas/ticket.fxml."
            );
        }
    }

    /**
     * Crea el texto que aparece en el ticket del pedido.
     *
     * @param pedido pedido del que se toman los datos
     * @return texto del ticket con folio, fecha, producto, proveedor,
     *         cantidad y estado
     */
    private String crearTextoTicket(PedidoReabastecimiento pedido) {

        return "       SISTEMA DE PUNTO DE VENTA\n"
                + "       PEDIDO DE REABASTECIMIENTO\n"
                + "====================================\n"
                + "FOLIO: " + pedido.getFolio() + "\n"
                + "FECHA: " + pedido.getFechaFormateada() + "\n"
                + "------------------------------------\n"
                + "PRODUCTO: " + pedido.getNombreProducto() + "\n"
                + "ID PRODUCTO: " + pedido.getIdProducto() + "\n"
                + "PROVEEDOR: " + pedido.getProveedor() + "\n"
                + "CANTIDAD: " + pedido.getCantidad() + "\n"
                + "ESTADO: " + pedido.getEstado() + "\n"
                + "====================================\n"
                + "Comprobante interno de pedido.";
    }

    /**
     * Muestra una ventana de alerta y espera a que el usuario la cierre.
     *
     * @param tipo tipo de alerta
     * @param titulo título de la ventana
     * @param mensaje texto que se muestra al usuario
     */
    private void mostrarAlerta(
            Alert.AlertType tipo,
            String titulo,
            String mensaje) {

        Alert alerta = new Alert(tipo);
        alerta.setTitle(titulo);
        alerta.setHeaderText(null);
        alerta.setContentText(mensaje);
        alerta.showAndWait();
    }

    /**
     * Obtiene los pedidos generados durante la sesión.
     *
     * @return lista observable de pedidos generados
     */
    public ObservableList<PedidoReabastecimiento> getPedidosGenerados() {
        return pedidosGenerados;
    }
}