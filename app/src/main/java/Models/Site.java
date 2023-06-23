package Models;

public class Site {
    private int siteId;
    private String siteName;

    public Site(int siteId, String siteName) {
        this.siteId = siteId;
        this.siteName = siteName;
    }
    public Site( String siteName) {
        this.siteName = siteName;
    }

    public Site() {

    }

    public int getSiteId() {
        return siteId;
    }

    public void setSiteId(int siteId) {
        this.siteId = siteId;
    }

    public String getSiteName() {
        return siteName;
    }

    public void setSiteName(String siteName) {
        this.siteName = siteName;
    }
}
