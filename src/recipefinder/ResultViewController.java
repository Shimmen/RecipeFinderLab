package recipefinder;

import javafx.application.Platform;
import javafx.beans.binding.DoubleBinding;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
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

    // Recipe list
    @FXML private Button backButton;
    @FXML private ListView<Recipe> resultList;

    // Recipe detail
    @FXML private ScrollPane recipeScrollPane;
    @FXML private BorderPane scrollContent;
    @FXML private VBox detailContainer;

    @FXML private Label recipeName;
    @FXML private Separator separator;
    @FXML private GridPane iconsGrid;
    @FXML private Label timeRequired;
    @FXML private Label difficulty;
    @FXML private Label price;
    @FXML private Label servings;
    @FXML private Text description;
    @FXML private ImageView image;
    @FXML private Text ingredients;
    @FXML private Text instructions;

    // Layout constants
    private final static double DEFAULT_INSET = 14.0;

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        // Make sure scroll pane never needs to scroll horizontally
        scrollContent.prefWidthProperty().bind(recipeScrollPane.widthProperty().subtract(DEFAULT_INSET * 2));

        // Snap detail content to edges
        DoubleBinding prefWidth = detailContainer.widthProperty().subtract(DEFAULT_INSET * 2);
        recipeName.prefWidthProperty().bind(prefWidth);
        separator.prefWidthProperty().bind(prefWidth);
        iconsGrid.prefWidthProperty().bind(prefWidth);
        description.wrappingWidthProperty().bind(prefWidth);
        image.fitWidthProperty().bind(prefWidth);
        ingredients.wrappingWidthProperty().bind(prefWidth);
        instructions.wrappingWidthProperty().bind(prefWidth);

        // On new recipe selected
        resultList.getSelectionModel().selectedItemProperty().addListener((observable, prevSelectedRecipe, selectedRecipe) -> {
            onRecipeSelected(selectedRecipe);
        });
    }

    private void onRecipeSelected(Recipe selectedRecipe) {
        if (selectedRecipe == null) {
            return;
        }

        // Scroll to top of detail view
        recipeScrollPane.setVvalue(0.0);

        // Set recipe detail view properties

        recipeName.setText(selectedRecipe.getName());

        int minutes = selectedRecipe.getTime() % 60;
        int hours = selectedRecipe.getTime() / 60;
        String timeText = "";
        if (hours != 0) {
            timeText += hours + " timma" + (hours != 1 ? "r" : "");
        }
        if (minutes != 0) {
            if (!timeText.isEmpty()) {
                timeText += " och ";
            }
            timeText += minutes + " minut" + (minutes != 1 ? "er": "");
        }
        timeRequired.setText(timeText);

        difficulty.setText(selectedRecipe.getDifficulty());
        price.setText(String.valueOf(selectedRecipe.getPrice()) + " kr/portion");

        int numServings = selectedRecipe.getServings();
        servings.setText(String.valueOf(numServings) + " portion" + (numServings == 1 ? "" : "er"));

        description.setText(selectedRecipe.getDescription());
        image.setImage(selectedRecipe.getFXImage());

        String ingredientsString = selectedRecipe.getIngredients().stream()
                .map(this::ingredientString)
                .reduce("", (s, s2) -> s.isEmpty() ? s2 : s + "\n" + s2);
        ingredients.setText(ingredientsString);
        ingredients.setY(description.getLayoutBounds().getHeight() + 14.0 + 130.0);

        instructions.setText(selectedRecipe.getInstruction());
    }

    private String ingredientString(Ingredient ingredient) {
        return ingredient.getAmount() + " " + ingredient.getUnit() + " " + ingredient.getName();
    }

    public void setRecipes(List<Recipe> recipes, boolean includeAll) {
        List<Recipe> filteredRecipes = recipes.stream()
                .filter(recipe -> recipe.getMatch() == 100 || includeAll)
                .collect(Collectors.toList());

        resultList.setItems(FXCollections.observableArrayList(filteredRecipes));
        resultList.setCellFactory(RecipeCell::new);

        // Run this later to assure that the scroll pane is ready for the content.
        // This fixes the bug where the scroll pane doesn't scroll properly and
        // its layout buggers up when the scroll pane changes size.
        Platform.runLater(() ->
            resultList.getSelectionModel().selectFirst()
        );
    }

    @FXML
    public void backButtonWasPressed() throws IOException {
        backButton.getScene().setRoot(this.searchView);
    }

    public void setSearchView(Parent searchView) {
        this.searchView = searchView;
    }
}
