package gui;

import java.io.IOException;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 * Hlavni trida projektu - spousteci
 * nacte gui z fxml souboru
 * @author Jakub Vašta
 */
public class Main extends Application {

	static {
		System.setProperty("log4j.configurationFile", "logger-config.xml");
	}
	
    /** Refernce na primarni scenu */
    private static Stage primaryStage = null;

    public static void main(String[] args) {
        launch(args);
    }

    /**
     * Metoda nacitajici gui z fxml souboru
     * @param stage reference na scenu
     * @throws IOException pokud se nepovede nacist fxml soubor
     */
    @Override
    public void start(Stage stage) throws IOException {
        primaryStage = stage;

        Parent root = FXMLLoader.load(getClass().getResource("/FXML/gui.fxml"));

        Scene scene = new Scene(root);

        primaryStage.setTitle("Composer");
        primaryStage.setScene(scene);
        primaryStage.show();

    }

    public static Stage getPrimaryStage() {
        return primaryStage;
    }
}
