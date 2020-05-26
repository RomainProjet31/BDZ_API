package model;

public class BoundingBox {
    private double[][] coordinates;
    private String type;

    public BoundingBox() {
	this(new double[4][2], "nothing");
    }

    public BoundingBox(double[][] ds, String type) {
	super();
	this.coordinates = ds;
	this.type = type;
    }

    public double[][] getCoordinates() {
	return coordinates;
    }

    public void setCoordinates(double[][] coordinates) {
	this.coordinates = coordinates;
    }

    public String getType() {
	return type;
    }

    public void setType(String type) {
	this.type = type;
    }

}
