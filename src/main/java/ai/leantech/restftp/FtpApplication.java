package ai.leantech.restftp;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;

@SpringBootApplication(exclude = {SecurityAutoConfiguration.class })
@ConfigurationPropertiesScan("ai.leantech.restftp.config")
public class FtpApplication {

	public static void main(String[] args) {
		SpringApplication.run(FtpApplication.class, args);
	}

}
