import java.io.File;
import java.net.URL;
import java.util.ResourceBundle;

import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.ImageCursor;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.ToggleGroup;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import javafx.util.Duration;

public class Controller implements Initializable {

    @FXML
    private RadioButton chooseCombo;

    @FXML
    private ToggleGroup chooseGroup;

    @FXML
    private RadioButton chooseMap;

    @FXML
    private ComboBox<City> comboDistination;

    @FXML
    private ComboBox<City> comboScource;

    @FXML
    private VBox comboVbox;

    @FXML
    private RadioButton filterCost;

    @FXML
    private RadioButton filterDistance;

    @FXML
    private ToggleGroup filterGroup;

    @FXML
    private RadioButton filterTime;

    @FXML
    private Label mapDistination;

    @FXML
    private Label mapScource;

    @FXML
    private VBox mapVbox;

    @FXML
    private ImageView map_img;

    @FXML
    private TableView<City> pathTable;

    @FXML
    private Button resetBtn;

    @FXML
    private Button runBtn;

    @FXML
    private Text totalDistanceLabel;

    @FXML
    private AnchorPane imgPane;

    @FXML
    private AnchorPane imgContainer;

    @FXML
    private TableColumn<City, Double> xCoulmn;

    @FXML
    private TableColumn<City, Double> yColumn;

    @FXML
    private TableColumn<City, String> nameCoulmn;

    @FXML
    private Text totalCostLabel;

    @FXML
    private HBox startHbox;

    @FXML
    private HBox distinationHbox;

    @FXML
    private Text totalTimeLabel;

    @FXML
    private Text textHover;

    static LinkedList<City> path;
    static double prevLatitude = 0;
    static double prevLongitude = 0;
    static boolean whatIsSelected = false;

    @FXML
    void resetAction(ActionEvent event) {
        prevLatitude = 0;
        prevLongitude = 0;
        path = null;
        imgPane.getChildren().clear();
        pathTable.getItems().clear();
        whatIsSelected = false;
        startHbox.setStyle("-fx-effect: dropshadow(gaussian, green, 12, 0.2, 0, 0);");
        distinationHbox.setStyle("");

        totalDistanceLabel.setText("-");
        totalTimeLabel.setText("-");
        totalCostLabel.setText("-");

        if (chooseCombo.isSelected()) {
            comboScource.getSelectionModel().clearSelection();
            comboDistination.getSelectionModel().clearSelection();
            comboScource.setPromptText("-");
            comboDistination.setPromptText("-");
        }

        if (chooseMap.isSelected()) {
            mapScource.setText("-");
            mapDistination.setText("-");
            imgPane.getChildren().clear();
            mapScource.setText("-");
            mapDistination.setText("-");

            for (int i = 0; i < DijkstraAirTraffic.cities.length; i++) {
                System.out.println(DijkstraAirTraffic.cities[i].name);
                addCityChoose(imgPane, map_img.getFitWidth(), map_img.getFitHeight(), DijkstraAirTraffic.cities[i],
                        DijkstraAirTraffic.cities[i].x.doubleValue(), DijkstraAirTraffic.cities[i].y.doubleValue());
            }
        }
    }

    @FXML
    void runAction(ActionEvent event) {
        try {
            totalDistanceLabel.setText("-");
            totalTimeLabel.setText("-");
            totalCostLabel.setText("-");

            pathTable.getItems().clear();
            if (chooseCombo.isSelected()) {
                if (comboScource.getSelectionModel().getSelectedItem() != null
                        && comboDistination.getSelectionModel().getSelectedItem() != null) {

                    imgPane.getChildren().clear();
                    prevLatitude = 0;
                    prevLongitude = 0;
                    path = new LinkedList<>();
                    whatIsSelected = false;
                    startHbox.setStyle("-fx-effect: dropshadow(gaussian, green, 12, 0.2, 0, 0);");
                    distinationHbox.setStyle("");

                    if (filterDistance.isSelected()) {
                        path = DijkstraAirTraffic.dijkstra(comboScource.getSelectionModel().getSelectedIndex(),
                                comboDistination.getSelectionModel().getSelectedIndex(), 3);
                    }
                    if (filterTime.isSelected()) {
                        path = DijkstraAirTraffic.dijkstra(comboScource.getSelectionModel().getSelectedIndex(),
                                comboDistination.getSelectionModel().getSelectedIndex(), 2);
                    }
                    if (filterCost.isSelected()) {
                        path = DijkstraAirTraffic.dijkstra(comboScource.getSelectionModel().getSelectedIndex(),
                                comboDistination.getSelectionModel().getSelectedIndex(), 1);
                    }
                    if (path != null) {
                        for (int i = 0; i < path.size(); i++) {
                            addCity(imgPane, map_img.getFitWidth(), map_img.getFitHeight(), path.get(i).name,
                                    path.get(i).x.doubleValue(),
                                    path.get(i).y.doubleValue());
                        }
                    }
                }
            } else {
                prevLatitude = 0;
                prevLongitude = 0;
                path = new LinkedList<>();
                whatIsSelected = false;
                int sourceIndex = DijkstraAirTraffic.findIndex(mapScource.getText());
                int distinationIndex = DijkstraAirTraffic.findIndex(mapDistination.getText());
                whatIsSelected = false;
                startHbox.setStyle("-fx-effect: dropshadow(gaussian, green, 12, 0.2, 0, 0);");
                distinationHbox.setStyle("");

                if (filterDistance.isSelected()) {
                    path = DijkstraAirTraffic.dijkstra(sourceIndex,
                            distinationIndex, 3);
                }
                if (filterTime.isSelected()) {
                    path = DijkstraAirTraffic.dijkstra(sourceIndex,
                            distinationIndex, 2);
                }
                if (filterCost.isSelected()) {
                    path = DijkstraAirTraffic.dijkstra(sourceIndex,
                            distinationIndex, 1);
                }
                if (path != null) {
                    imgPane.getChildren().clear();
                    for (int i = 0; i < path.size(); i++) {
                        addCity(imgPane, map_img.getFitWidth(), map_img.getFitHeight(), path.get(i).name,
                                path.get(i).x.doubleValue(),
                                path.get(i).y.doubleValue());
                    }
                }
            }

            ObservableList<City> observableList = FXCollections.observableArrayList();
            Node<City> current = path.getHead();
            while (current != null) {
                observableList.add(current.value);
                current = current.next;
            }

            int totalCost = 0;
            int totalTime = 0;
            int totalDistance = 0;

            pathTable.setItems(observableList);

            for (int i = 0; i < path.size() - 1; i++) {
                City currentCity = path.get(i);
                City nextCity = path.get(i + 1);

                for (int j = 0; j < DijkstraAirTraffic.graph[DijkstraAirTraffic.findIndex(currentCity.name)]
                        .size(); j++) {
                    Edge edge = DijkstraAirTraffic.graph[DijkstraAirTraffic.findIndex(currentCity.name)].get(j);
                    if (edge.to == DijkstraAirTraffic.findIndex(nextCity.name)) {
                        totalCost += edge.cost;
                        totalTime += edge.time;
                        totalDistance += edge.distance;
                        break;
                    }
                }
            }

            totalDistanceLabel.setText(String.valueOf(totalDistance));
            totalTimeLabel.setText(String.valueOf(totalTime));
            totalCostLabel.setText(String.valueOf(totalCost));

        } catch (Exception ex) {
            if (chooseCombo.isSelected()) {
                showError("Error in the path",
                        "Can't find a path from: " + comboScource.getSelectionModel().getSelectedItem().name + " to "
                                + comboDistination.getSelectionModel().getSelectedItem().name);
            } else {
                showError("Error in the path",
                        "Can't find a path from: " + mapScource.getText() + " to "
                                + mapDistination.getText());
            }
        }
    }

    @FXML
    void chooseComboAction(ActionEvent event) {
        pathTable.getItems().clear();

        totalDistanceLabel.setText("-");
        totalTimeLabel.setText("-");
        totalCostLabel.setText("-");

        if (chooseCombo.isSelected()) {
            comboScource.getSelectionModel().clearSelection();
            comboDistination.getSelectionModel().clearSelection();
            comboVbox.setVisible(true);
            mapVbox.setVisible(false);

            imgPane.getChildren().clear();
            mapScource.setText("-");
            mapDistination.setText("-");
        } else {
            mapVbox.setVisible(true);
            comboVbox.setVisible(false);

            imgPane.getChildren().clear();
        }
    }

    @FXML
    void chooseMapAction(ActionEvent event) {
        pathTable.getItems().clear();
        whatIsSelected = false;
        totalDistanceLabel.setText("-");
        totalTimeLabel.setText("-");
        totalCostLabel.setText("-");

        if (chooseMap.isSelected()) {
            mapVbox.setVisible(true);
            comboVbox.setVisible(false);

            imgPane.getChildren().clear();
            mapScource.setText("-");
            mapDistination.setText("-");

            for (int i = 0; i < DijkstraAirTraffic.cities.length; i++) {
                addCityChoose(imgPane, map_img.getFitWidth(), map_img.getFitHeight(), DijkstraAirTraffic.cities[i],
                        DijkstraAirTraffic.cities[i].x.doubleValue(), DijkstraAirTraffic.cities[i].y.doubleValue());
            }

        } else {
            comboVbox.setVisible(true);
            mapVbox.setVisible(false);

            imgPane.getChildren().clear();
        }
    }

    @FXML
    void filterCostAction(ActionEvent event) {

    }

    @FXML
    void filterDistanceAction(ActionEvent event) {

    }

    @FXML
    void filterTimeAction(ActionEvent event) {

    }

    private void addCity(Pane pane, double mapWidth, double mapHeight, String name, double latitude, double longitude) {
        double x = getX(longitude, mapWidth);
        double y = getY(latitude, mapHeight);

        Text cityName = new Text(name);
        cityName.setFill(Color.WHITE);
        cityName.setX(x + 15);
        cityName.setY(y + 5);

        Circle cityMarker = new Circle(x, y, 5, Color.RED);
        pane.getChildren().addAll(cityMarker, cityName);

        if (prevLatitude != 0 && prevLongitude != 0) {
            Line cityLine = new Line();
            cityLine.setStroke(Color.WHITE);
            cityLine.setStrokeWidth(3);

            cityLine.setStartX(prevLongitude);
            cityLine.setStartY(prevLatitude);

            pane.getChildren().add(cityLine);

            Timeline timeline = new Timeline(
                    new KeyFrame(Duration.ZERO,
                            new KeyValue(cityLine.endXProperty(), prevLongitude),
                            new KeyValue(cityLine.endYProperty(), prevLatitude)),
                    new KeyFrame(Duration.seconds(1),
                            new KeyValue(cityLine.endXProperty(), x),
                            new KeyValue(cityLine.endYProperty(), y)));

            timeline.play();
        }

        prevLatitude = y;
        prevLongitude = x;
    }

    private void addCityChoose(Pane pane, double mapWidth, double mapHeight, City city, double latitude,
            double longitude) {
        double x = getX(longitude, mapWidth);
        double y = getY(latitude, mapHeight);

        Circle cityMarker = new Circle(x, y, 5, Color.RED);
        mapVbox.getChildren().addAll(cityMarker);
        pane.getChildren().addAll(cityMarker);

        cityMarker.setOnMouseClicked(new EventHandler<Event>() {
            @Override
            public void handle(Event arg0) {
                if (whatIsSelected == false) {
                    mapScource.setText(city.name);
                } else if (whatIsSelected == true) {
                    mapDistination.setText(city.name);
                } else {
                    whatIsSelected = false;
                }
            }

        });

        cityMarker.setOnMouseEntered(new EventHandler<Event>() {

            @Override
            public void handle(Event arg0) {
                textHover.setVisible(true);
                textHover.setText(city.name);
            }

        });

        cityMarker.setOnMouseExited(new EventHandler<Event>() {

            @Override
            public void handle(Event arg0) {
                textHover.setVisible(false);
                textHover.setText("-");
            }

        });
    }

    public double getX(double longitude, double width) {
        return ((longitude + 180) / 360 * width);
    }

    public double getY(double latitude, double height) {
        return (height - (latitude + 90) / 180 * height);
    }

    @FXML
    void selectDistination(MouseEvent event) {
        whatIsSelected = true;
        distinationHbox.setStyle("-fx-effect: dropshadow(gaussian, green, 12, 0.2, 0, 0);");
        startHbox.setStyle("");
    }

    @FXML
    void selectStart(MouseEvent event) {
        whatIsSelected = false;
        startHbox.setStyle("-fx-effect: dropshadow(gaussian, green, 12, 0.2, 0, 0);");
        distinationHbox.setStyle("");
    }

    public static void showError(String title, String message) {
        Alert alert = new Alert(AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText("Error");
        alert.setContentText(message);
        alert.showAndWait();
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        map_img.setImage(new Image("img/World-Map.jpg"));

        chooseCombo.setSelected(true);
        chooseMap.setSelected(false);
        filterDistance.setSelected(true);
        filterCost.setSelected(false);
        filterTime.setSelected(false);

        comboVbox.setVisible(true);
        mapVbox.setVisible(false);

        DijkstraAirTraffic.readFromFile(new File("DATA.txt"));

        ObservableList<City> observableList = FXCollections.observableArrayList(DijkstraAirTraffic.cities);
        comboScource.setItems(observableList);
        comboDistination.setItems(observableList);

        Image img = new Image("img/pin.png");
        double centerX = img.getWidth() / 2;
        double centerY = img.getHeight() / 2;
        imgContainer.setCursor(new ImageCursor(img, centerX, centerY));

        nameCoulmn.setCellValueFactory(new PropertyValueFactory("name"));
        xCoulmn.setCellValueFactory(new PropertyValueFactory("x"));
        yColumn.setCellValueFactory(new PropertyValueFactory("y"));

        totalDistanceLabel.setText("-");
        totalTimeLabel.setText("-");
        totalCostLabel.setText("-");

        Rectangle clip = new Rectangle(map_img.getFitWidth(), map_img.getFitHeight());
        clip.setArcWidth(30);
        clip.setArcHeight(30);
        map_img.setClip(clip);

        whatIsSelected = false;
        startHbox.setStyle("-fx-effect: dropshadow(gaussian, green, 12, 0.2, 0, 0);");
        distinationHbox.setStyle("");

    }

}
