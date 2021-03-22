package ai.leantech.restftp.controller;

import ai.leantech.restftp.dto.MessageResponse;
import ai.leantech.restftp.model.FileInfo;
import ai.leantech.restftp.service.FtpService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.core.io.Resource;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("api/files")
public class FtpController {
    private final FtpService service;

    public FtpController(FtpService service) {
        this.service = service;
    }

    @GetMapping("/list")
    public List<FileInfo> getList() throws IOException {
        return service.getList();
    }

    @GetMapping
    public ResponseEntity<Resource> downloadFile(@RequestParam String path) throws IOException {
        return service.downloadFile(path);
    }

    @PostMapping
    public MessageResponse saveFile(@RequestParam MultipartFile file, @RequestParam String directory) throws IOException {
        return service.saveFile(file, directory);
    }

    @DeleteMapping
    public MessageResponse deleteFile(@RequestParam String path) throws IOException {
        return service.deleteFile(path);
    }
}
