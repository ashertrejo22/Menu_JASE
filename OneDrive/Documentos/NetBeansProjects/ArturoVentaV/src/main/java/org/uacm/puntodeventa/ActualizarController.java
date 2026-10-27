package org.uacm.puntodeventa;

import java.io.File;
import java.net.URL;
import java.time.LocalDate;
import java.util.ResourceBundle;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import org.uacm.puntodeventa.Clases.Producto;

public class ActualizarController implements Initializable {

    @FXML private TextField txtFieldNombre;
    @FXML private TextField txtFieldPrecioProv;
    @FXML private TextField txtFieldCantidad;
    @FXML private ComboBox<String> comboBoxTipoProducto;
    @FXML private DatePicker datePickerCaducidad;
    @FXML private Button buttonActualizar;
    @FXML private Button buttonCancelar;
    @FXML private Button btnSeleccionarImagen; 
    @FXML private ImageView previewImagen;
    
    private String rutaImagenSeleccionada;
    private Producto productoActual;
    private ObservableList<Producto> listaProductos; 

    public void setProducto(Producto producto) {
        this.productoActual = producto;
        cargarDatos();
    }

    public void setListaProductos(ObservableList<Producto> lista) {
        this.listaProductos = lista;
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // --- AÑADIDO "Granel" ---
        comboBoxTipoProducto.getItems().addAll("Normal", "Digital", "Perecedero", "Granel");
        
        datePickerCaducidad.setVisible(false); // La hacemos invisible en lugar de solo deshabilitarla

        comboBoxTipoProducto.setOnAction(e -> {
            String tipo = comboBoxTipoProducto.getValue();
            if ("Perecedero".equals(tipo)) {
                datePickerCaducidad.setVisible(true);
            } else {
                datePickerCaducidad.setVisible(false);
                datePickerCaducidad.setValue(null);
            }
        });
    }

    private void cargarDatos() {
        if (productoActual != null) {
            txtFieldNombre.setText(productoActual.getNombre());
            txtFieldPrecioProv.setText(String.valueOf(productoActual.getPrecio_prov()));
            txtFieldCantidad.setText(String.valueOf(productoActual.getStock()));
            comboBoxTipoProducto.setValue(productoActual.getTipo());
            
            // --- CORRECCIÓN DE HERENCIA ---
            if (productoActual instanceof org.uacm.puntodeventa.Clases.ProductoPerecedero) {
                org.uacm.puntodeventa.Clases.ProductoPerecedero perecedero = (org.uacm.puntodeventa.Clases.ProductoPerecedero) productoActual;
                
                if (perecedero.getFechaCaducidad() != null) {
                    datePickerCaducidad.setValue(perecedero.getFechaCaducidad());
                    datePickerCaducidad.setVisible(true);
                }
            } else {
                datePickerCaducidad.setValue(null);
                datePickerCaducidad.setVisible(false);
            }
            
            if(productoActual.getRutaImag() != null) { 
                rutaImagenSeleccionada = productoActual.getRutaImag();
                previewImagen.setImage(null);
                previewImagen.setImage(new Image(rutaImagenSeleccionada));
            }
        }
    }

    @FXML
    private void actualizarProductoExitoso(ActionEvent event) {
        // Validaciones...
        if (txtFieldNombre.getText().trim().isEmpty() || 
            txtFieldPrecioProv.getText().trim().isEmpty() || 
            txtFieldCantidad.getText().trim().isEmpty()) {
            mostrarAlerta("No puede haber campos vacíos");
            return;
        }

        double precio;
        double cantidad;
        
        try {
            precio = Double.parseDouble(txtFieldPrecioProv.getText());
            cantidad = Double.parseDouble(txtFieldCantidad.getText());
        } catch (NumberFormatException e) {
            mostrarAlerta("Precio y cantidad deben ser números válidos");
            return;
        }
        
        if (precio < 0 || cantidad < 0) {
            mostrarAlerta("Los valores no pueden ser negativos");
            return;
        }
        
        String tipo = comboBoxTipoProducto.getValue();
        if (tipo == null) {
            mostrarAlerta("Selecciona un tipo de producto");
            return;
        }
        
        LocalDate caducidad = null;
        if ("Perecedero".equals(tipo)) {
            if (datePickerCaducidad.getValue() == null) {
                mostrarAlerta("Debes seleccionar una fecha de caducidad");
                return;
            }
            if (!datePickerCaducidad.getValue().isAfter(LocalDate.now())) {
                mostrarAlerta("La fecha de caducidad debe ser futura");
                return;
            }
            caducidad = datePickerCaducidad.getValue();
        }
        productoActual.setNombre(txtFieldNombre.getText().trim());
        productoActual.setPrecio_prov(precio);  
        productoActual.setStock(cantidad);      
        productoActual.setTipo(tipo);
        productoActual.setRutaImag(rutaImagenSeleccionada); 
       
        if (productoActual instanceof org.uacm.puntodeventa.Clases.ProductoPerecedero) {
            org.uacm.puntodeventa.Clases.ProductoPerecedero perecedero = (org.uacm.puntodeventa.Clases.ProductoPerecedero) productoActual;
            perecedero.setFechaCaducidad(caducidad);
        }
        
        if (listaProductos != null) {
            int index = listaProductos.indexOf(productoActual);
            if (index >= 0) {
                listaProductos.set(index, productoActual); 
            }
        }
        
        Stage stage = (Stage) buttonActualizar.getScene().getWindow();
        stage.close();
    }

    @FXML
    private void cerrarVentana() {
        Stage stage = (Stage) buttonCancelar.getScene().getWindow();
        stage.close();
    }

    private void mostrarAlerta(String mensaje) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle("Validación");
        alert.setHeaderText(null);
        alert.setContentText(mensaje);
        alert.showAndWait();
    }

    @FXML
    private void seleccionarImagen(ActionEvent event) { 
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Seleccionar imagen");

        fileChooser.getExtensionFilters().addAll(
            new FileChooser.ExtensionFilter("Imágenes", "*.png", "*.jpg", "*.jpeg")
        );

        File file = fileChooser.showOpenDialog(null);

        if (file != null) {
            rutaImagenSeleccionada = file.toURI().toString();
            previewImagen.setImage(null);
            previewImagen.setImage(new Image(rutaImagenSeleccionada));
        }
    }
}