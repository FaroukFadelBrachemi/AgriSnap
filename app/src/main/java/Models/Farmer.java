package Models;

public class Farmer {
    private int farmerId;
    private String farmerName;
    private String farmerLastName;
    private String farmerBirth;

    public int getFarmerId() {
        return farmerId;
    }

    public void setFarmerId(int farmerId) {
        this.farmerId = farmerId;
    }

    public String getFarmerName() {
        return farmerName;
    }

    public void setFarmerName(String farmerName) {
        this.farmerName = farmerName;
    }

    public String getFarmerLastName() {
        return farmerLastName;
    }

    public void setFarmerLastName(String farmerLastName) {
        this.farmerLastName = farmerLastName;
    }

    public String getFarmerBirth() {
        return farmerBirth;
    }

    public void setFarmerBirth(String farmerBirth) {
        this.farmerBirth = farmerBirth;
    }

    public Farmer(int farmerId, String farmerName, String farmerLastName, String farmerBirth) {
        this.farmerId = farmerId;
        this.farmerName = farmerName;
        this.farmerLastName = farmerLastName;
        this.farmerBirth = farmerBirth;
    }
}

