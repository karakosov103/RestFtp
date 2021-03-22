package ai.leantech.restftp.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties("ftp")
public class FtpProperties {

    private String sslPassword;

    private String listenerName;

    private Server server;

    private User user;

    public String getSslPassword() {
        return sslPassword;
    }

    public void setSslPassword(String sslPassword) {
        this.sslPassword = sslPassword;
    }

    public String getListenerName() {
        return listenerName;
    }

    public void setListenerName(String listenerName) {
        this.listenerName = listenerName;
    }

    public Server getServer() {
        return server;
    }

    public void setServer(Server server) {
        this.server = server;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public static class Server {
        private int port;
        private String passivePorts;
        private String passiveAddress;

        public String getPassiveAddress() {
            return passiveAddress;
        }

        public void setPassiveAddress(String passiveAddress) {
            this.passiveAddress = passiveAddress;
        }

        public int getPort() {
            return port;
        }

        public void setPort(int port) {
            this.port = port;
        }

        public String getPassivePorts() {
            return passivePorts;
        }

        public void setPassivePorts(String passivePorts) {
            this.passivePorts = passivePorts;
        }
    }

    public static class User {

        private String homeDirectory;
        private String username;
        private String password;
        private String token;

        public String getToken() {
            return token;
        }

        public void setToken(String token) {
            this.token = token;
        }

        public String getHomeDirectory() {
            return homeDirectory;
        }

        public void setHomeDirectory(String homeDirectory) {
            this.homeDirectory = homeDirectory;
        }

        public String getUsername() {
            return username;
        }

        public void setUsername(String username) {
            this.username = username;
        }

        public String getPassword() {
            return password;
        }

        public void setPassword(String password) {
            this.password = password;
        }

    }

}