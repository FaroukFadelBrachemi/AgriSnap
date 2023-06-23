package Models;

public class Agent {


        private int agentId;
        private String agentName;
        private String agentLastName;
        private String agentUsername;
        private String agentPassword;
        private String agentDevice;
        private String agentPicture;
        private String agentBirth;

    public Agent(String agentName, String agentLastName) {
        this.agentName = agentName;
        this.agentLastName = agentLastName;
    }

    public int getAgentId() {
        return agentId;
    }

    public void setAgentId(int agentId) {
        this.agentId = agentId;
    }

    public String getAgentName() {
        return agentName;
    }

    public void setAgentName(String agentName) {
        this.agentName = agentName;
    }

    public String getAgentLastName() {
        return agentLastName;
    }

    public void setAgentLastName(String agentLastName) {
        this.agentLastName = agentLastName;
    }

    public String getAgentUsername() {
        return agentUsername;
    }

    public void setAgentUsername(String agentUsername) {
        this.agentUsername = agentUsername;
    }

    public String getAgentPassword() {
        return agentPassword;
    }

    public void setAgentPassword(String agentPassword) {
        this.agentPassword = agentPassword;
    }

    public String getAgentDevice() {
        return agentDevice;
    }

    public void setAgentDevice(String agentDevice) {
        this.agentDevice = agentDevice;
    }

    public String getAgentPicture() {
        return agentPicture;
    }

    public void setAgentPicture(String agentPicture) {
        this.agentPicture = agentPicture;
    }

    public String getAgentBirth() {
        return agentBirth;
    }

    public void setAgentBirth(String agentBirth) {
        this.agentBirth = agentBirth;
    }
}