package recipefinder;

import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Slider;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyEvent;
import javafx.util.StringConverter;
import se.chalmers.ait.dat215.lab2.Recipe;
import se.chalmers.ait.dat215.lab2.RecipeDatabase;
import se.chalmers.ait.dat215.lab2.SearchFilter;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class SearchViewController implements Initializable {

    @FXML private ChoiceBox<String> cuisineChoiceBox;
    @FXML private ChoiceBox<String> mainIngredientChoiceBox;
    @FXML private ChoiceBox<String> difficultyChoiceBox;
    @FXML private TextField maxPriceTextField;
    @FXML private Slider maxTimeSlider;

    private Parent searchView;
    private Parent resultView;
    private ResultViewController resultViewController;


    @Override
    public void initialize(URL location, ResourceBundle resources) {

        if (cuisineChoiceBox != null) {
            cuisineChoiceBox.setItems(FXCollections.observableArrayList(
                    "Alla kök", "Sverige", "Grekland", "Indien", "Asien", "Afrika", "Frankrike"
            ));
            cuisineChoiceBox.setValue("Alla kök");
        }

        if (mainIngredientChoiceBox != null) {
            mainIngredientChoiceBox.setItems(FXCollections.observableArrayList(
                    "Alla ingredienser", "Kött", "Fisk", "Kykling", "Vegetarisk"
            ));
            mainIngredientChoiceBox.setValue("Alla ingredienser");
        }

        if (difficultyChoiceBox != null) {
            difficultyChoiceBox.setItems(FXCollections.observableArrayList(
                    "Alla svårighetsgrader", "Lätt", "Mellan", "Svår"
            ));
            difficultyChoiceBox.setValue("Alla svårighetsgrader");
        }

        if (maxTimeSlider != null) {
            maxTimeSlider.setLabelFormatter(new StringConverter<Double>() {
                @Override
                public String toString(Double object) {
                    if (object == 160) {
                        return "Alla tider";
                    } else {
                        return String.valueOf(object.intValue());
                    }
                }

                @Override
                public Double fromString(String string) {
                    // Probably isn't needed?
                    return null;
                }
            });
            maxTimeSlider.setValue(160.0);
        }
    }

    @FXML
    public void searchButtonWasPressed() throws IOException {

        boolean isNotDifficultyChoiceBox = difficultyChoiceBox.getValue().equals("Alla svårighetsgrader");
        boolean isNotMaxTimeSlider = maxTimeSlider.getValue() == 160.0;
        boolean isNotCuisineChoiceBox = cuisineChoiceBox.getValue().equals("Alla kök");
        boolean isNotMaxPriceTextField = maxPriceTextField.getText().isEmpty();
        boolean isNotMainIngredientChoiceBox = mainIngredientChoiceBox.getValue().equals("Alla ingredienser");

        // Perform search
        SearchFilter filter = new SearchFilter(
                isNotDifficultyChoiceBox ? null : difficultyChoiceBox.getValue(),
                isNotMaxTimeSlider ? 0 : (int)maxTimeSlider.getValue(),
                isNotCuisineChoiceBox ? null : cuisineChoiceBox.getValue(),
                isNotMaxPriceTextField ? 0 : Integer.parseInt(maxPriceTextField.getText()),
                isNotMainIngredientChoiceBox ? null : mainIngredientChoiceBox.getValue()
        );
        List<Recipe> searchResult = RecipeDatabase.getSharedInstance().search(filter);

        // Load result view lazily
        if (resultView == null || resultViewController == null) {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("ResultView.fxml"));
            this.resultView = loader.load();
            this.resultViewController = loader.<ResultViewController>getController();
            this.resultViewController.setSearchView(this.searchView);
        }

        // Switch view
        this.searchView.getScene().setRoot(this.resultView);

        // Pass result to result view controller
        resultViewController.setRecipes(searchResult,
                isNotDifficultyChoiceBox && isNotMaxTimeSlider && isNotCuisineChoiceBox &&
                        isNotMaxPriceTextField && isNotMainIngredientChoiceBox);
    }

    @FXML
    public void onMaxPriceKeyTyped(KeyEvent event) {
        // TODO!!!
    }

    public void setSearchView(Parent searchView) {
        this.searchView = searchView;
    }
}
