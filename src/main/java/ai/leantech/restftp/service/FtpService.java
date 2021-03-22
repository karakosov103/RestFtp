package ai.leantech.restftp.service;

import ai.leantech.restftp.dto.MessageResponse;
import ai.leantech.restftp.exception.FileDeleteFailedException;
import ai.leantech.restftp.exception.FileNotFoundException;
import ai.leantech.restftp.model.FileInfo;
import ai.leantech.restftp.model.FileType;
import ai.leantech.restftp.config.FtpProperties;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.FileSystemUtils;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.core.io.Resource;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class FtpService {
    private final FtpProperties ftpProperties;

    public FtpService(FtpProperties ftpProperties) {
        this.ftpProperties = ftpProperties;
    }

    public List<FileInfo> getList() throws IOException {
        Path root = Path.of(ftpProperties.getUser().getHomeDirectory());
        return buildFileInfoRecursive(root);
    }

    public MessageResponse saveFile(MultipartFile file, String directory) throws IOException {
        String pathToUploads = Path.of(ftpProperties.getUser().getHomeDirectory().concat(File.separator) +
                Paths.get(directory)).toAbsolutePath().toString();
        if (!new File(pathToUploads).exists()) {
            new File(pathToUploads).mkdirs();
        }
        file.transferTo(new File(pathToUploads.concat(File.separator) + file.getOriginalFilename()));
        MessageResponse message = new MessageResponse();
        message.setResponse("File " + file.getOriginalFilename() + " saved successfully in directory".concat(File.separator) + directory);
        return message;
    }

    public ResponseEntity<Resource> downloadFile(String path) throws IOException {
        Resource file = load(path);
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + file.getFilename() + "\"").body(file);
    }

    public Resource load(String path) throws IOException {
        try {
            Path root = Path.of(ftpProperties.getUser().getHomeDirectory(), path);
            Resource resource = new UrlResource(root.toUri());
            if (resource.exists() || resource.isReadable()) {
                return resource;
            } else {
                throw new FileNotFoundException();
            }
        } catch (MalformedURLException e) {
            throw new RuntimeException("Error: " + e.getMessage());
        }
    }

    private List<FileInfo> buildFileInfoRecursive(Path root) {
        return getNodeList(root.toFile());
    }

    private FileInfo getNode(File root) {
        String path = preprocessPath(root.getPath());
        if (root.isDirectory()) {
            return new FileInfo(FileType.DIRECTORY, root.getName(), path, getNodeList(root));
        } else {
            return new FileInfo(FileType.FILE, root.getName(), path);
        }
    }

    private List<FileInfo> getNodeList(File root) {
        File[] files = root.listFiles();
        if (files == null) {
            return Collections.emptyList();
        }
        return Arrays.stream(files)
                .map(this::getNode)
                .collect(Collectors.toList());
    }

    private String preprocessPath(String path) {
        int start = path.indexOf(ftpProperties.getUser().getHomeDirectory());
        int end = start + ftpProperties.getUser().getHomeDirectory().length();
        return path.substring(end);
    }

    public MessageResponse deleteFile(String path) {
        MessageResponse message = new MessageResponse();
        String pathToUploads = Path.of(ftpProperties.getUser().getHomeDirectory().concat(File.separator) +
                Paths.get(path)).toAbsolutePath().toString();
        File fileToDelete = new File(pathToUploads);
        if (fileToDelete.exists()) {
            boolean fileDeleted;
            if (fileToDelete.isFile()) {
                fileDeleted = fileToDelete.delete();
            } else {
                fileDeleted = FileSystemUtils.deleteRecursively(fileToDelete);
            }
            if (!fileDeleted) {
                throw new FileDeleteFailedException();
            }
        } else {
            throw new FileNotFoundException();
        }
        message.setResponse("File " + path + " deleted successfully.");
        return message;
    }
}
