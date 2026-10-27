package org.uacm.puntodeventa;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import org.uacm.puntodeventa.Clases.RegistroVentas;
import org.uacm.puntodeventa.Clases.Venta;
import org.uacm.puntodeventa.Clases.Recarga;
import org.uacm.puntodeventa.Clases.ItemReporte;
import java.io.IOException;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.stage.Stage;

public class ReporteVentasController implements Initializable {

    @FXML private TextField txtBusqueda;
    @FXML private TableView<ItemReporte> tablaVentas;
    @FXML private TableColumn<ItemReporte, String> colFolio;
    @FXML private TableColumn<ItemReporte, String> colFecha;
    @FXML private TableColumn<ItemReporte, String> colCantidadProductos; 
    @FXML private TableColumn<ItemReporte, Double> colTotal;
    @FXML private Label lblTotalDia;

    private ObservableList<ItemReporte> listaCombinada;
    private FilteredList<ItemReporte> listaFiltrada;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        colFolio.setCellValueFactory(new PropertyValueFactory<>("folio"));
        colFecha.setCellValueFactory(new PropertyValueFactory<>("fecha"));
        colCantidadProductos.setCellValueFactory(new PropertyValueFactory<>("detalles"));
        colTotal.setCellValueFactory(new PropertyValueFactory<>("total"));

        List<ItemReporte> datosParaReporte = new ArrayList<>();

        for (Venta v : RegistroVentas.getInstance().getHistorialVentas()) {
            String detallesVenta = "Venta Física"; 
            datosParaReporte.add(new ItemReporte(v.getFolio(), v.getFecha_venta().toString(), detallesVenta, v.getTotal()));
        }

        for (Recarga r : RegistroVentas.getInstance().getHistorialRecargas()) {
            String detallesRecarga = "Recarga: " + r.getCompania();
            datosParaReporte.add(new ItemReporte(r.getFolio (), r.getFecha().toString(), detallesRecarga, r.getMonto()));
        }

        listaCombinada = FXCollections.observableArrayList(datosParaReporte);
        listaFiltrada = new FilteredList<>(listaCombinada, v -> true);
        
        txtBusqueda.textProperty().addListener((obs, oldVal, newVal) -> {
            String texto = (newVal == null) ? "" : newVal.toLowerCase();
            listaFiltrada.setPredicate(item -> {
                if (texto.isEmpty()) return true; 
                return item.getFolio().toLowerCase().contains(texto)
                    || item.getFecha().toLowerCase().contains(texto)
                    || item.getDetalles().toLowerCase().contains(texto); 
            });
        });

        tablaVentas.setItems(listaFiltrada);
        double totalDia = listaCombinada.stream().mapToDouble(ItemReporte::getTotal).sum();
        lblTotalDia.setText(String.format("$%.2f", totalDia));
        
        tablaVentas.setOnMouseClicked(event -> {
            if (event.getClickCount() == 2) {
                ItemReporte seleccionado = tablaVentas.getSelectionModel().getSelectedItem();
                if (seleccionado != null) {
                    try {
                        FXMLLoader loader = new FXMLLoader(getClass().getResource("/pantallas/ticket.fxml"));
                        Parent root = loader.load();
                        TicketController controller = loader.getController();
                        controller.setTicketTexto(buscarTicketPorFolio(seleccionado.getFolio()));
                        Stage stage = new Stage();
                        stage.setTitle("Ticket");
                        stage.setScene(new Scene(root));
                        stage.setResizable(false);
                        stage.show();
                    }
                    catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }
    private String buscarTicketPorFolio(String folio) {
        for (Venta v : RegistroVentas.getInstance().getHistorialVentas()) {
            if (v.getFolio().equals(folio)) {
                return v.generarTicket();
            }
        }
        for (Recarga r : RegistroVentas.getInstance().getHistorialRecargas()) {
            if (r.getFolio().equals(folio)) {
                return r.generarComprobante();
            }
        }
        return null;
    }
}