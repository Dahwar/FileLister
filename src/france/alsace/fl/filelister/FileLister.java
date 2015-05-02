package france.alsace.fl.filelister;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

/**
 * Main
 * @author Florent
 */
public class FileLister extends Application {
    
    @Override
    public void start(Stage stage) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("FXMLFileLister.fxml"));
        
        Scene scene = new Scene(root);
        Image icon = new Image("/img/list.png");
        stage.getIcons().add(icon);
        stage.setTitle("FileListerFX v1.2.2");
        
        stage.setScene(scene);
        stage.show();
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
        System.exit(0);
    }
    
    
    
}
