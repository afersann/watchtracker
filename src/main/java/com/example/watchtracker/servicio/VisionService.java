package com.example.watchtracker.servicio;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.vision.v1.*;
import com.google.protobuf.ByteString;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

@Service
public class VisionService {

    public List<String> detectarEtiquetas(MultipartFile archivo) throws Exception {
        List<String> resultados = new ArrayList<>();

        ClassPathResource jsonCredencial = new ClassPathResource("credenciales/watchtracker-131313-aeb754876c69.json");

        try (InputStream credStream = jsonCredencial.getInputStream()) {
            GoogleCredentials credenciales = GoogleCredentials.fromStream(credStream);
            ImageAnnotatorSettings settings = ImageAnnotatorSettings.newBuilder()
                    .setCredentialsProvider(() -> credenciales)
                    .build();

            try (ImageAnnotatorClient cliente = ImageAnnotatorClient.create(settings)) {
                ByteString bytes = ByteString.readFrom(archivo.getInputStream());

                Image imagen = Image.newBuilder().setContent(bytes).build();
                Feature feature = Feature.newBuilder().setType(Feature.Type.LABEL_DETECTION).build();

                AnnotateImageRequest request = AnnotateImageRequest.newBuilder()
                        .addFeatures(feature)
                        .setImage(imagen)
                        .build();

                BatchAnnotateImagesResponse respuesta = cliente.batchAnnotateImages(List.of(request));

                for (AnnotateImageResponse r : respuesta.getResponsesList()) {
                    if (r.hasError()) {
                        resultados.add("Error: " + r.getError().getMessage());
                    } else {
                        for (EntityAnnotation etiqueta : r.getLabelAnnotationsList()) {
                            resultados.add(etiqueta.getDescription() + " (" + etiqueta.getScore() + ")");
                        }
                    }
                }
            }
        }

        return resultados;
    }

    public String detectarTexto(MultipartFile archivo) throws Exception {
        StringBuilder texto = new StringBuilder();

        ClassPathResource jsonCredencial = new ClassPathResource("credenciales/watchtracker-131313-aeb754876c69.json");

        try (InputStream credStream = jsonCredencial.getInputStream()) {
            GoogleCredentials credenciales = GoogleCredentials.fromStream(credStream);
            ImageAnnotatorSettings settings = ImageAnnotatorSettings.newBuilder()
                    .setCredentialsProvider(() -> credenciales)
                    .build();

            try (ImageAnnotatorClient cliente = ImageAnnotatorClient.create(settings)) {
                ByteString bytes = ByteString.readFrom(archivo.getInputStream());

                Image imagen = Image.newBuilder().setContent(bytes).build();
                Feature feature = Feature.newBuilder().setType(Feature.Type.TEXT_DETECTION).build();

                AnnotateImageRequest request = AnnotateImageRequest.newBuilder()
                        .addFeatures(feature)
                        .setImage(imagen)
                        .build();

                BatchAnnotateImagesResponse respuesta = cliente.batchAnnotateImages(List.of(request));

                for (AnnotateImageResponse r : respuesta.getResponsesList()) {
                    if (r.hasError()) {
                        texto.append("Error: ").append(r.getError().getMessage());
                    } else {
                        texto.append(r.getTextAnnotationsList().isEmpty() ? "Sin texto detectado" :
                                r.getTextAnnotationsList().get(0).getDescription());
                    }
                }
            }
        }

        return texto.toString();
    }
}
