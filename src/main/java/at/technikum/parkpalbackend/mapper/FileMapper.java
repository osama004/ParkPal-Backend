package at.technikum.parkpalbackend.mapper;

import at.technikum.parkpalbackend.dto.FileDto;

import at.technikum.parkpalbackend.model.File;
import at.technikum.parkpalbackend.service.UploadService;
import at.technikum.parkpalbackend.service.UserService;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Component
public class FileMapper {

    public UserService userService;
    public UploadService uploadService;

    public FileDto toDto(File file) {
        if (file == null) {
            throw new IllegalArgumentException("File entity or its fields cannot be null");
        }
        return FileDto.builder()
                .id(file.getId())
                .userId(file.getUser().getId())
                .uploadDate(file.getUploadDate())
                .build();
    }

    public File toEntity(FileDto fileDto) {
        if (fileDto == null) {
            throw new IllegalArgumentException("PictureDTO or its fields cannot be null");
        }
        return File.builder()
                .id(fileDto.getId())
                .user(userService.findByUserId(fileDto.getUserId()))
                .uploadDate(fileDto.getUploadDate())
                .build();
    }

    public File fromMultipartFileToEntity(MultipartFile file) throws IOException {
        return File.builder().file(uploadService.transferToBytes(file)).build();
        // add logic later according to usage;
        // maybe change File to MultipartFileType;
    }
}
