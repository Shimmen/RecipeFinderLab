package recipefinder;

import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import se.chalmers.ait.dat215.lab2.Recipe;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

public class ResultViewController implements Initializable {

    private List<Recipe> recipes;

    @FXML Button backButton;
    @FXML ListView<Recipe> resultList;

    private Parent searchView;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        // ...
    }

    public void setRecipes(List<Recipe> recipes, boolean includeAll) {
        this.recipes = recipes.stream()
                .filter(recipe -> recipe.getMatch() == 100 || includeAll)
                .collect(Collectors.toList());

        resultList.setItems(FXCollections.observableArrayList(this.recipes));
        resultList.setCellFactory(RecipeCell::new);
    }

    @FXML
    public void backButtonWasPressed() throws IOException {
        backButton.getScene().setRoot(this.searchView);
    }

    public void setSearchView(Parent searchView) {
        this.searchView = searchView;
    }
}
