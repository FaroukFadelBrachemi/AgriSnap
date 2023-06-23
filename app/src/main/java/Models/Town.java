package Models;

public class Town {
    public int townId;
    private String townName;
    public int getTownId() {
        return townId;
    }


    public Town(int townId, String townName) {
        this.townId = townId;
        this.townName = townName;
    }

    public Town( String townName) {
        this.townName = townName;
    }

    public Town() {

    }


    public void setTownId(int townId) {
        this.townId = townId;
    }

    public String getTownName() {
        return townName;
    }

    public void setTownName(String townName) {
        this.townName = townName;
    }
}

