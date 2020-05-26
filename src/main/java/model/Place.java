package model;

public class Place {

    private String idPlace;
    private String url;
    private String name;
    private String fullName;
    private String placeType;
    private String countryCode;
    private String country;
    private BoundingBox boundingBox;

    public Place(String idPlace, String url, String name, String fullName, String placeType, String countryCode,
	    String country, BoundingBox boundingBox) {
	super();
	this.idPlace = idPlace;
	this.url = url;
	this.name = name;
	this.fullName = fullName;
	this.placeType = placeType;
	this.countryCode = countryCode;
	this.country = country;
	this.boundingBox = boundingBox;
    }

    public Place(String idPlace, String url, String name, String fullName, String placeType, String countryCode,
	    String country) {
	this(idPlace, url, name, fullName, placeType, countryCode, country, new BoundingBox());
    }

    public String getIdPlace() {
	return idPlace;
    }

    public void setIdPlace(String idPlace) {
	this.idPlace = idPlace;
    }

    public String getUrl() {
	return url;
    }

    public void setUrl(String url) {
	this.url = url;
    }

    public String getName() {
	return name;
    }

    public void setName(String name) {
	this.name = name;
    }

    public String getFullName() {
	return fullName;
    }

    public void setFullName(String fullName) {
	this.fullName = fullName;
    }

    public String getPlaceType() {
	return placeType;
    }

    public void setPlaceType(String placeType) {
	this.placeType = placeType;
    }

    public String getCountryCode() {
	return countryCode;
    }

    public void setCountryCode(String countryCode) {
	this.countryCode = countryCode;
    }

    public String getCountry() {
	return country;
    }

    public void setCountry(String country) {
	this.country = country;
    }

    public BoundingBox getBoundingBox() {
	return boundingBox;
    }

    public void setBoundingBox(BoundingBox boundingBox) {
	this.boundingBox = boundingBox;
    }

}
