package org.uacm.puntodeventa;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import org.uacm.puntodeventa.Clases.Usuario;

import java.io.IOException;

public class PrimaryController {

    @FXML
    private Button btnIniciarSesion;
    @FXML
    private TextField TxtFieldUsuario;
    @FXML
    private PasswordField PasswordField;
    @FXML
    private Label lblErrores;
    @FXML
    private Label lblUsuario;
    @FXML
    private Label lblContra;

    public void initialize() {
        btnIniciarSesion.setOnAction(event -> {
            try {
                String usuario = TxtFieldUsuario.getText().trim();
                String contra = PasswordField.getText().trim();

                if (usuario.isEmpty() || contra.isEmpty()) {
                    throw new IllegalArgumentException("Los campos no pueden estar vacíos.");
                }

                Usuario u1 = new Usuario(usuario, contra);
                int nivelUsuario = u1.validar(usuario, contra);

                if (nivelUsuario == 0) {
                    throw new SecurityException("¡Usuario o contraseña incorrectos!");
                }
                
                abrirSistemaPrincipal(nivelUsuario, usuario);

            } catch (IllegalArgumentException | SecurityException e) {
                lblErrores.setStyle("-fx-text-fill: red;");
                lblErrores.setText(e.getMessage());
            } catch (Exception e) {
                lblErrores.setStyle("-fx-text-fill: red;");
                lblErrores.setText("Ocurrió un error inesperado en el inicio de sesión.");
                e.printStackTrace();
            }
        });
    }

    private void abrirSistemaPrincipal(int nivelUsuario, String nombreUsuario) {
        try {
            FXMLLoader loader = new FXMLLoader(
                    getClass().getResource("/pantallas/PantallaPrincipal.fxml"));
            Parent root = loader.load();

            PantallaPrincipalController controller = loader.getController();
            controller.configurarPermisos(nivelUsuario, nombreUsuario);

            Stage stage = new Stage();
            stage.setTitle("Sistema de Ventas");
            stage.setScene(new Scene(root, 917, 660));
            stage.show();
            Stage actual = (Stage) btnIniciarSesion.getScene().getWindow();
            actual.close();

        } catch (IOException e) {
            mostrarAlertaError("Error de Sistema", "No se pudo cargar la pantalla principal. Archivo FXML no encontrado.");
            e.printStackTrace();
        }
    }
    
    @FXML
    private void abrirRecuperacion() {
        try {
            Parent root = FXMLLoader.load(
                    getClass().getResource("/pantallas/Recuperar.fxml"));

            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.show();
            Stage actual = (Stage) btnIniciarSesion.getScene().getWindow(); 
            actual.close();

        } catch (IOException e) {
            mostrarAlertaError("Error de Sistema", "No se pudo cargar la pantalla de recuperación.");
            e.printStackTrace();
        }
    }

    private void mostrarAlertaError(String titulo, String mensaje) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(titulo);
        alert.setHeaderText(null);
        alert.setContentText(mensaje);
        alert.showAndWait();
    }
}