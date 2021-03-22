package ai.leantech.restftp.config;

import org.apache.ftpserver.FtpServer;
import org.apache.ftpserver.ftplet.FtpException;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
public class FtpServerRunner {

    private final FtpServer ftpServer;

    public FtpServerRunner(FtpServer ftpServer) {
        this.ftpServer = ftpServer;
    }

    @EventListener(ApplicationReadyEvent.class)
    public void startFtpServer() throws FtpException {
        ftpServer.start();
    }

}
