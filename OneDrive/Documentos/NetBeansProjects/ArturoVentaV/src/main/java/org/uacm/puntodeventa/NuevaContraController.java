package org.uacm.puntodeventa;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.stage.Stage;

import org.uacm.puntodeventa.Clases.Usuario;

public class NuevaContraController {

    @FXML
    private PasswordField txtNuevaContra;

    @FXML
    private PasswordField txtConfirmarContra;

    @FXML
    private Label lblError;

   
    private String usuario;

    
    public void setUsuario(String usuario) {

        this.usuario = usuario;
    }

    @FXML
    private void guardarContra() {

        String contra1 = txtNuevaContra.getText();
        String contra2 = txtConfirmarContra.getText();

        // campo vacio
        if (contra1.isEmpty() || contra2.isEmpty()) {

            lblError.setText("Completa todos los campos");
            return;
        }

        // caracteres minimos 6
        if (contra1.length() < 6) {

            lblError.setText("Debe tener mínimo 6 caracteres");
            return;
        }

        // letras y numeros
        boolean tieneNumero = contra1.matches(".*\\d.*");
        boolean tieneLetra = contra1.matches(".*[a-zA-Z].*");

        if (!tieneNumero || !tieneLetra) {

            lblError.setText("Debe contener letras y números");
            return;
        }

        // COINCIDENCIA
        if (!contra1.equals(contra2)) {

            lblError.setText("Las contraseñas no coinciden");
            return;
        }

        // LIMPIAR ERROR
        lblError.setText("");

        // GUARDAR NUEVA CONTRASEÑA
        Usuario.cambiarContra(usuario, contra1);

        // ALERTA aprobado
        Alert alerta = new Alert(Alert.AlertType.INFORMATION);

        alerta.setTitle("Contraseña actualizada");
        alerta.setHeaderText(null);
        alerta.setContentText(
                "Tu contraseña fue actualizada correctamente.");

        alerta.showAndWait();

        volverLogin();
    }

    private void volverLogin() {

        try {

            Parent root = FXMLLoader.load(
                    getClass().getResource("/pantallas/Login.fxml"));

            Stage stage = new Stage();

            stage.setScene(new Scene(root));
            stage.show();

            // CERRAR ACTUAL
            Stage actual = (Stage)
                    txtNuevaContra.getScene().getWindow();

            actual.close();

        } catch (Exception e) {

            e.printStackTrace();
        }
    }
}