package com.example.watchtracker.servicio;

import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.Base64;

@Service
public class RoboflowServicio {

    private static final String API_KEY = "svTKI4DlBagWlCdnu90y"; // 🔁 Sustituye esto por tu clave real de Roboflow
    private static final String UPLOAD_URL = "https://infer.roboflow.com/watch-classification/1"; // Asegúrate de usar la versión correcta del modelo

    public String clasificarImagen(byte[] imageBytes) throws IOException {
        String base64Image = Base64.getEncoder().encodeToString(imageBytes);
        String body = String.format("{\"base64\":\"%s\"}", base64Image);

        try (CloseableHttpClient httpClient = HttpClients.createDefault()) {
            HttpPost request = new HttpPost(UPLOAD_URL + "?api_key=" + API_KEY);
            request.setHeader("Content-Type", "application/json");
            request.setEntity(new StringEntity(body));

            try (CloseableHttpResponse response = httpClient.execute(request)) {
                HttpEntity entity = response.getEntity();
                return EntityUtils.toString(entity);
            }
        }
    }
}

