package recipefinder;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception{

        primaryStage.setTitle("Studentrecept");

        FXMLLoader loader = new FXMLLoader(getClass().getResource("SearchView.fxml"));
        Parent searchView = loader.load();
        SearchViewController searchViewController = loader.<SearchViewController>getController();
        searchViewController.setSearchView(searchView);

        int minWidth = 1200;
        int minHeight = 600;
        Scene mainScene = new Scene(searchView, minWidth, minHeight);
        primaryStage.setMinWidth(minWidth);
        primaryStage.setMinHeight(minHeight);
        primaryStage.setScene(mainScene);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
