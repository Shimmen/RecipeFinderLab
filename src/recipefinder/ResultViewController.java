package recipefinder;

import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Text;
import se.chalmers.ait.dat215.lab2.Ingredient;
import se.chalmers.ait.dat215.lab2.Recipe;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

public class ResultViewController implements Initializable {

    private Parent searchView;
    private List<Recipe> recipes;

    @FXML private Button backButton;
    @FXML private ListView<Recipe> resultList;

    @FXML private ScrollPane recipeScrollView;
    @FXML private AnchorPane scrollContentView;

    @FXML private Label recipeName;
    @FXML private Text description;
    @FXML private Text ingredients;

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        scrollContentView.prefWidthProperty().bind(recipeScrollView.widthProperty());
        description.wrappingWidthProperty().bind(recipeScrollView.widthProperty().subtract(14.0 * 2 + 10.0));

        resultList.getSelectionModel().selectedItemProperty().addListener((observable, prevSelectedRecipe, selectedRecipe) -> {

            if (selectedRecipe == null) {
                return;
            }

            // Set recipe detail view properties

            recipeName.setText(selectedRecipe.getName());
            description.setText(selectedRecipe.getDescription());

            String ingredientsString = selectedRecipe.getIngredients().stream()
                    .map(this::ingredientString)
                    .reduce("", (s, s2) -> s + "\n" + s2);
            ingredients.setText(ingredientsString);

        });
    }

    private String ingredientString(Ingredient ingredient) {
        return ingredient.getAmount() + " " + ingredient.getUnit() + " " + ingredient.getName();
    }

    public void setRecipes(List<Recipe> recipes, boolean includeAll) {
        this.recipes = recipes.stream()
                .filter(recipe -> recipe.getMatch() == 100 || includeAll)
                .collect(Collectors.toList());

        resultList.setItems(FXCollections.observableArrayList(this.recipes));
        resultList.getSelectionModel().selectFirst();
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
