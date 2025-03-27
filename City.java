import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.DoubleProperty;

public class City {
    String name;
    DoubleProperty x;  // Use DoubleProperty for x
    DoubleProperty y;  // Use DoubleProperty for y
    int index;

    public City(String name, double x, double y, int index) {
        this.name = name;
        this.x = new SimpleDoubleProperty(x);  // Initialize with SimpleDoubleProperty
        this.y = new SimpleDoubleProperty(y);  // Initialize with SimpleDoubleProperty
        this.index = index;
    }

    public String getName() {
        return name;
    }

    // Getter method for x, returns SimpleDoubleProperty
    public DoubleProperty xProperty() {
        return x;
    }

    // Getter method for y, returns SimpleDoubleProperty
    public DoubleProperty yProperty() {
        return y;
    }

    public int getIndex() {
        return index;
    }

    // Optional: For non-property access
    public double getX() {
        return x.get();
    }

    public void setX(double x) {
        this.x.set(x);
    }

    public double getY() {
        return y.get();
    }

    public void setY(double y) {
        this.y.set(y);
    }

    @Override
    public String toString() {
        return name;
    }
}
