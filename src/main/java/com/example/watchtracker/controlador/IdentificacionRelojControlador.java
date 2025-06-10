package com.example.watchtracker.controlador;

import com.example.watchtracker.modelo.Usuario;
import com.example.watchtracker.servicio.HistorialIdentificacionServicio;
import com.example.watchtracker.servicio.RoboflowServicio;
import com.example.watchtracker.servicio.UsuarioServicio;
import com.example.watchtracker.servicio.VisionService;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.*;

@Controller
public class IdentificacionRelojControlador {

    @Autowired
    private RoboflowServicio roboflowServicio;

    @Autowired
    private VisionService visionService;

    @Autowired
    private UsuarioServicio usuarioServicio;

    @Autowired
    private HistorialIdentificacionServicio historialServicio;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @GetMapping("/identificar-reloj")
    public String mostrarFormularioIdentificacion() {
        return "identificar-reloj";
    }

    @PostMapping("/identificar-reloj")
    public String identificarReloj(@RequestParam("imagen") MultipartFile imagen, Model model) {
        Map<String, Object> resultados = new LinkedHashMap<>();

        try {
            byte[] imagenBytes = imagen.getBytes();

            // 1. Google Cloud Vision – etiquetas
            List<String> etiquetasVision = visionService.detectarEtiquetas(imagen);
            Map<String, Float> visionMap = new LinkedHashMap<>();
            for (String e : etiquetasVision) {
                String[] partes = e.split(" \\(");
                if (partes.length == 2) {
                    visionMap.put(partes[0], Float.parseFloat(partes[1].replace(")", "")));
                }
            }
            resultados.put("Google Cloud Vision", visionMap);

            // 2. Google Cloud Vision – texto detectado
            String textoDetectado = visionService.detectarTexto(imagen);
            resultados.put("Texto detectado", textoDetectado);


                    // 3. Roboflow
            String resultadoRoboflow = llamarARoboflow(imagenBytes);
            resultados.put("Roboflow", resultadoRoboflow);

            // 4. Información técnica
            Map<String, String> infoTecnica = obtenerInformacionTecnica(resultadoRoboflow);
            resultados.put("Información Técnica", infoTecnica);

            // 5. Guardar en historial
            Authentication auth = SecurityContextHolder.getContext().getAuthentication();
            String correo = auth.getName();
            Usuario usuario = usuarioServicio.buscarPorCorreo(correo).orElse(null);
            historialServicio.guardarHistorial(imagenBytes, resultadoRoboflow, etiquetasVision.toString(), usuario);


        } catch (Exception e) {
            model.addAttribute("error", "Ocurrió un error: " + e.getMessage());
        }

        model.addAttribute("resultados", resultados);
        return "identificar-reloj";
    }


    private String llamarARoboflow(byte[] imagenBytes) throws IOException {
        String respuestaJson = roboflowServicio.clasificarImagen(imagenBytes);

        JsonNode root = objectMapper.readTree(respuestaJson);
        if (root.has("predictions") && root.get("predictions").isArray() && root.get("predictions").size() > 0) {
            return root.get("predictions").get(0).get("class").asText();
        }

        return "Sin resultado";
    }

    private Map<String, String> obtenerInformacionTecnica(String modeloReloj) {
        Map<String, String> datos = new LinkedHashMap<>();

        if ("Sin resultado".equalsIgnoreCase(modeloReloj)) {
            datos.put("Aviso", "No se pudo identificar un modelo de reloj.");
        } else {
            datos.put("Modelo", modeloReloj);
            datos.put("Movimiento", "Automático");
            datos.put("Material", "Acero inoxidable");
            datos.put("Resistencia al agua", "300m");
        }

        return datos;
    }

}
