package Models;

public class Land {
    private int landId;
    private String landName;

    public Land(int landId, String landName) {
        this.landId = landId;
        this.landName = landName;
    }
    public Land( String landName) {
        this.landName = landName;
    }

    public Land() {

    }

    public int getLandId() {
        return landId;
    }

    public void setLandId(int landId) {
        this.landId = landId;
    }

    public String getLandName() {
        return landName;
    }

    public void setLandName(String landName) {
        this.landName = landName;
    }
}

