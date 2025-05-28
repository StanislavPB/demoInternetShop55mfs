package org.demointernetshop55mfs.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.demointernetshop55mfs.entity.FileInfo;
import org.demointernetshop55mfs.entity.User;
import org.demointernetshop55mfs.repository.FileInfoRepository;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class FileInfoService {


    private final FileInfoRepository repository;
    private final UserService userService;

    private final String LOCAL_STORAGE_PATH = "src/main/resources/static/upload";

    @SneakyThrows
    @Transactional
    public String uploadLocalStorage(MultipartFile uploadFile) {

        Path fileStorageLocation = Paths.get(LOCAL_STORAGE_PATH);

        String newFileName = createFileName(uploadFile);

        // создаем targetLocation который будет содержать полный путь до места хранения и имя файла
        Path targetLocation = fileStorageLocation.resolve(newFileName);

        // копируем данные из файла upload, который хранится во временном хранилище сервера
        // в папку и под именем, которое мы создали

        Files.copy(uploadFile.getInputStream(), targetLocation, StandardCopyOption.REPLACE_EXISTING);

        String link = targetLocation.toString();


        User currentUser = userService.getCurrentUser();

        FileInfo fileInfo = new FileInfo();
        fileInfo.setLink(link);
        fileInfo.setUser(currentUser);
        repository.save(fileInfo);

        userService.setPhotoLink(link);

        return "Файл " + link + " успешно сохранен";

    }

        // создаем запрос на отправку файла

        /*
        варианты что может вернуть getContentType():

        Изображения:
        - image/jpeg
        - image/png
        - image/gif
        ...

        Не изображения:
        text/plain - txt
        text/html - html
        ...

        Документы:
        application/pdf
        application/msword
        application/vnd.ms-excel
        application/vnd.ms-powerpoint

        Архивы:
        application/zip
        ...

        Аудио
        audio/mp3
        audio/wav
        ...

        Видео
        video/mp4
        video/x-matroska (MKV)

         */


    private String createFileName(MultipartFile uploadFile){
        String originalFileName = uploadFile.getOriginalFilename();
        // получаем исходное имя файла

        String extension = "";
        if (originalFileName != null) {
            int indexExtension = originalFileName.lastIndexOf(".") + 1;
            //получаем индекс начала расширения полученного файла (следующий символ за '.')
            extension = originalFileName.substring(indexExtension);
        } else {
            throw new NullPointerException("Null original file name");
        }

        // генерируем случайное имя для файла с помощью UUID
        // заменяем название файла для того, чтобы избежать ситуации,
        // когда нам пытаются передать файл с таким-же названием
        // потому что он просто перезапишется (а если первый файл
        // был другой - то мы потеряем данные)

        String uuidFileName = UUID.randomUUID().toString();
        String newFileName = uuidFileName + "." + extension;

        return newFileName;
    }

}
