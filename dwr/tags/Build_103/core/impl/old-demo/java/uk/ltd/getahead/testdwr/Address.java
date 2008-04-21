package uk.ltd.getahead.testdwr;

/**
 * A demo of a QuickAddress type Address object
 * @author Joe Walker [joe at getahead dot ltd dot uk]
 */
public class Address
{
    private String house;
    private String street;
    private String line2;
    private String line3;
    private String town;
    private String area;
    private String zipcode;

    public String getArea() {
        return area;
    }

    public void setArea(String area) {
        this.area = area;
    }

    public String getHouse() {
        return house;
    }

    public void setHouse(String house) {
        this.house = house;
    }

    public String getLine2() {
        return line2;
    }

    public void setLine2(String line1) {
        this.line2 = line1;
    }

    public String getLine3() {
        return line3;
    }

    public void setLine3(String line2) {
        this.line3 = line2;
    }

    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public String getTown() {
        return town;
    }

    public void setTown(String town) {
        this.town = town;
    }

    public String getZipcode()  {
        return zipcode;
    }

    public void setZipcode(String zipcode) {
        this.zipcode = zipcode;
    }
}
