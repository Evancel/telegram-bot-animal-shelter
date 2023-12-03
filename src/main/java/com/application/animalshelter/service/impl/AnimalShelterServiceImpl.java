package com.application.animalshelter.service.impl;

import com.application.animalshelter.dao.AnimalShelterDAO;
import com.application.animalshelter.dao.PassRulesDao;
import com.application.animalshelter.entity.AnimalShelter;
import com.application.animalshelter.entity.PassRules;
import com.application.animalshelter.enums.City;
import com.application.animalshelter.service.AnimalShelterService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Collections;
import java.util.List;

import static java.nio.file.StandardOpenOption.CREATE_NEW;

@Service
public class AnimalShelterServiceImpl implements AnimalShelterService {
    @Value("${path.to.passRules.folder}")
    private String pathToPassRules;
    private final AnimalShelterDAO animalShelterDAO;
    private final PassRulesDao passRulesDao;

    public AnimalShelterServiceImpl(AnimalShelterDAO animalShelterDAO, PassRulesDao passRulesDao) {
        this.animalShelterDAO = animalShelterDAO;
        this.passRulesDao = passRulesDao;
    }

    @Override
    public AnimalShelter saveAnimalShelter(AnimalShelter animalShelter) {
        return  animalShelterDAO.save(animalShelter);
    }

    @Override
    public AnimalShelter updateAddress(Long id, String address) {
        AnimalShelter animalShelter = animalShelterDAO.findById(id).get();
        animalShelter.setAddress(address);
        return  animalShelterDAO.save(animalShelter);
    }

    @Override
    public void uploadPassRules(Long id, MultipartFile passRules) throws IOException {
        AnimalShelter animalShelter = animalShelterDAO.findById(id).get();

        Path filePath = Path.of(pathToPassRules,id + "." +getExtension(passRules.getOriginalFilename()));
        Files.createDirectories(filePath.getParent());
        Files.deleteIfExists(filePath);

        //сохранение в файл на Локальный диск
        try(InputStream is = passRules.getInputStream();
            OutputStream os = Files.newOutputStream(filePath, CREATE_NEW);
            BufferedInputStream bis = new BufferedInputStream(is,1024);
            BufferedOutputStream bos = new BufferedOutputStream(os, 1024)
        ){
            bis.transferTo(bos);
        }

        PassRules prevPassRules = animalShelter.getPassRules();

        if (prevPassRules==null){
            prevPassRules = new PassRules();
        }

        prevPassRules.setFilePath(filePath.toString());
        prevPassRules.setFileSize(passRules.getSize());
        prevPassRules.setMediaType(passRules.getContentType());

        //создание уменьшенной копии
        prevPassRules.setData(generateImagePreview(filePath));

        //сохранение в БД
        PassRules savedPassRules = passRulesDao.save(prevPassRules);

        animalShelter.setPassRules(savedPassRules);
        animalShelterDAO.save(animalShelter);
    }

    @Override
    public AnimalShelter getAnimalShelter(Long Id) {

        return animalShelterDAO.findById(Id).orElse(null);
    }

    @Override
    public List<AnimalShelter> getAnimalShelters() {
        return Collections.unmodifiableList(animalShelterDAO.findAll());
    }

    @Override
    public List<AnimalShelter> getAnimalSheltersByCity(City city) {
        return Collections.unmodifiableList(animalShelterDAO.findByCity(city));
    }

    @Override
    public void deleteAnimalShelter(Long Id) {
        AnimalShelter animalShelter = getAnimalShelter(Id);
        if(animalShelter==null){
            //TODO добавить Exception
        }
        animalShelterDAO.delete(animalShelter);
    }

    private String getExtension(String fileName){
        return fileName.substring(fileName.lastIndexOf(".")+1);
    }

    private byte[] generateImagePreview(Path filePath) throws IOException {
        try(InputStream is = Files.newInputStream(filePath);
            BufferedInputStream bis = new BufferedInputStream(is, 1024);
            ByteArrayOutputStream baos = new ByteArrayOutputStream()) {
            BufferedImage image = ImageIO.read(bis);

            int height = image.getHeight() / (image.getWidth() / 100);
            BufferedImage preview = new BufferedImage(100, height, image.getType());
            Graphics2D graphics = preview.createGraphics();
            graphics.drawImage(image, 0, 0, 100, height, null);
            graphics.dispose();

            ImageIO.write(preview, getExtension(filePath.getFileName().toString()), baos);
            return baos.toByteArray();
        }
    }
}
