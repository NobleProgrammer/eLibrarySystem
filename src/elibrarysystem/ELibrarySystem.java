/*
    Authorized Access Only!
    Admin Panel Only.
    Name: Ammar Talat Joharji
    ID: 1742559
 */
package elibrarysystem;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 *
 * @author Amar
 */
public class ELibrarySystem extends Application {
    
    @Override
    public void start(Stage stage) throws Exception {
        stage.setResizable(false);
        stage.sizeToScene();
        Parent root = FXMLLoader.load(getClass().getResource("/login/LoginPage.fxml"));
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }
    
}
