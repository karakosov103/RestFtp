package ai.leantech.restftp.config;

import org.apache.ftpserver.DataConnectionConfiguration;
import org.apache.ftpserver.DataConnectionConfigurationFactory;
import org.apache.ftpserver.FtpServer;
import org.apache.ftpserver.FtpServerFactory;
import org.apache.ftpserver.ftplet.FtpException;
import org.apache.ftpserver.ftplet.UserManager;
import org.apache.ftpserver.listener.Listener;
import org.apache.ftpserver.listener.ListenerFactory;
import org.apache.ftpserver.usermanager.PasswordEncryptor;
import org.apache.ftpserver.usermanager.PropertiesUserManagerFactory;
import org.apache.ftpserver.usermanager.impl.BaseUser;
import org.apache.ftpserver.usermanager.impl.WritePermission;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Collections;

@Configuration
public class FtpConfig {

    private final FtpProperties ftpProperties;

    public FtpConfig(FtpProperties ftpProperties) {
        this.ftpProperties = ftpProperties;
    }

    @Bean
    public Path homeDirectory() throws IOException {
        String pathToUploads = Path.of(ftpProperties.getUser().getHomeDirectory()).toAbsolutePath().toString();
        if (!new File(pathToUploads).exists()) {
            new File(pathToUploads).mkdirs();
        }
        Path path = Path.of(pathToUploads);
        return path;
    }

    @Bean
    public BaseUser baseUser(Path homeDirectory) {
        BaseUser baseUser = new BaseUser();
        baseUser.setHomeDirectory(homeDirectory.normalize().toString());
        baseUser.setName(ftpProperties.getUser().getUsername());
        baseUser.setPassword(ftpProperties.getUser().getPassword());
        baseUser.setEnabled(true);
        baseUser.setAuthorities(Collections.singletonList(new WritePermission()));
        return baseUser;
    }

    @Bean
    public UserManager userManager(BaseUser baseUser) throws FtpException {
        PropertiesUserManagerFactory userManagerFactory = new PropertiesUserManagerFactory();
        userManagerFactory.setPasswordEncryptor(passwordEncryptor());
        UserManager userManager = userManagerFactory.createUserManager();
        userManager.save(baseUser);
        return userManager;
    }

//    @Bean
//    public SslConfiguration ssl() {
//        SslConfigurationFactory ssl = new SslConfigurationFactory();
//        ssl.setKeystoreFile(new File("src/main/resources/keystore"));
//        ssl.setKeystorePassword(ftpProperties.getSslPassword());
//        return ssl.createSslConfiguration();
//    }

    @Bean
    public Listener listener() {
        DataConnectionConfigurationFactory factory = new DataConnectionConfigurationFactory();
        factory.setPassivePorts(ftpProperties.getServer().getPassivePorts());
        //factory.setActiveEnabled(false);
        factory.setPassiveExternalAddress(ftpProperties.getServer().getPassiveAddress());
        factory.setPassiveAddress(ftpProperties.getServer().getPassiveAddress());


        ListenerFactory listenerFactory = new ListenerFactory();
        listenerFactory.setDataConnectionConfiguration(factory.createDataConnectionConfiguration());
        listenerFactory.setPort(ftpProperties.getServer().getPort());
//        listenerFactory.setSslConfiguration(ssl());
//        listenerFactory.setImplicitSsl(true);
        return listenerFactory.createListener();
    }

    @Bean
    public FtpServer ftpServer(UserManager userManager) throws FtpException {
        FtpServerFactory serverFactory = new FtpServerFactory();
        serverFactory.addListener(ftpProperties.getListenerName(), listener());
        serverFactory.setUserManager(userManager);
        return serverFactory.createServer();
    }

    @Bean
    PasswordEncryptor passwordEncryptor() {
        return new PasswordEncryptor() {

            @Override
            public String encrypt(String password) {
                return password;
            }

            @Override
            public boolean matches(String passwordToCheck, String storedPassword) {
                return passwordToCheck.equals(storedPassword);
            }
        };
    }
}
