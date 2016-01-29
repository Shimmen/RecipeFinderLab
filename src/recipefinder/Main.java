package recipefinder;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception{

        primaryStage.setTitle("Student Recipe");
        primaryStage.setMinWidth(400.0);
        primaryStage.setMinHeight(380.0);

        FXMLLoader loader = new FXMLLoader(getClass().getResource("SearchView.fxml"));
        Parent searchView = loader.load();
        SearchViewController searchViewController = loader.<SearchViewController>getController();
        searchViewController.setSearchView(searchView);

        Scene mainScene = new Scene(searchView, 900, 600);
        primaryStage.setScene(mainScene);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
