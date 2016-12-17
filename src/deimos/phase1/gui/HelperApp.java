package deimos.phase1.gui;

import java.io.IOException;

import deimos.phase1.gui.view.HelperOverviewController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class HelperApp extends Application {

    private Stage primaryStage;
    private VBox rootLayout;
    final public String title = "Deimos Helper";
    // private Person currentPerson;

    @Override
    public void start(Stage primaryStage) {
    	
        this.primaryStage = primaryStage;

        try {
        	
            // Load root layout from fxml file.        	
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(HelperApp.class.getResource("view/HelperOverview.fxml"));
            rootLayout = (VBox) loader.load();

            // Show the scene containing the root layout.
            Scene scene = new Scene(rootLayout);
            primaryStage.setTitle(title);
            primaryStage.setScene(scene);
            primaryStage.setResizable(false);
            
            primaryStage.show();
            
            HelperOverviewController controller = loader.getController();
            controller.setMainApp(this);
            
            
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        launch(args);
    }

	public Stage getPrimaryStage() {
		
		return primaryStage;
	}
}