package org.uacm.puntodeventa;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class RecuperarController {

    @FXML
    private TextField txtUsuario;
   @FXML
   
private void continuar() {
    try {
        if (txtUsuario.getText().trim().isEmpty()) {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/pantallas/AlertaRecuperar.fxml"));
            Parent root = loader.load();
            
            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.show();
            
            Stage actual = (Stage) txtUsuario.getScene().getWindow();
            return; 
        }
        
        String usuario = txtUsuario.getText().trim();
        if (!usuario.equals("Administrador") && !usuario.equals("Empleado")) {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/pantallas/AlertaUsuarioIncorrecto.fxml"));
            Parent root = loader.load();
            
            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.show();
            return; 
        }
        
        FXMLLoader loader = new FXMLLoader(
                getClass().getResource("/pantallas/Preguntas.fxml"));
        Parent root = loader.load();

        PreguntasController controller = loader.getController();
        controller.setUsuario(txtUsuario.getText());

        Stage stage = new Stage();
        stage.setScene(new Scene(root));
        stage.show();

        Stage actual = (Stage) txtUsuario.getScene().getWindow();
        actual.close();

    } catch (Exception e) {
        e.printStackTrace();
    }
}
    @FXML
    private void volverLogin(javafx.event.ActionEvent event) {
        try {
            Parent root = FXMLLoader.load(
                    getClass().getResource("/pantallas/Login.fxml"));

            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.show();

            Stage actual = (Stage) ((javafx.scene.Node) event.getSource()).getScene().getWindow();
            actual.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}