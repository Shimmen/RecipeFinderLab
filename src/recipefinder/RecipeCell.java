package recipefinder;

import javafx.scene.SnapshotParameters;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.OverrunStyle;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.image.WritableImage;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.text.Font;
import javafx.scene.text.TextAlignment;
import se.chalmers.ait.dat215.lab2.Recipe;

public class RecipeCell extends ListCell<Recipe>{

    private static final double IMAGE_SIZE = 40.0;
    private static final Circle cropCircle = new Circle(IMAGE_SIZE / 2.0);
    private static final SnapshotParameters parameters = new SnapshotParameters();

    private static final Font titleFont = new Font("Trebuchet MS", 14);

    static {
        parameters.setFill(Color.TRANSPARENT);
    }

    public RecipeCell(ListView<Recipe> recipeListView) {
        // Do nothing
    }

    @Override
    protected void updateItem(Recipe item, boolean empty) {
        super.updateItem(item, empty);
        setTextOverrun(OverrunStyle.CENTER_ELLIPSIS);
        setTextAlignment(TextAlignment.JUSTIFY);

        if(item == null || empty) {
            setText(null);
            setGraphic(null);
        } else {

            setText(item.getName());
            setFont(titleFont);

            // Scale image to be at least 100x100
            double width = item.getImage().getIconWidth();
            double height = item.getImage().getIconHeight();
            double scaleFactor = IMAGE_SIZE / Math.min(width, height);
            Image image = item.getFXImage(width * scaleFactor, height * scaleFactor, true);

            // Crop image to small centered circle
            ImageView imageView = new ImageView(image);
            cropCircle.setCenterX(width * scaleFactor / 2.0);
            cropCircle.setCenterY(height * scaleFactor / 2.0);
            imageView.setClip(cropCircle);
            WritableImage croppedImage = imageView.snapshot(parameters, null);
            imageView.setClip(null);
            imageView.setImage(croppedImage);
            setGraphic(imageView);
        }
    }
}
