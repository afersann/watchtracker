package com.example.watchtracker.dto;

import com.example.watchtracker.modelo.Reloj;
import java.util.List;

public class GrupoRelojesPorReferencia {
    private String referencia;
    private List<Reloj> relojes;

    public GrupoRelojesPorReferencia(String referencia, List<Reloj> relojes) {
        this.referencia = referencia;
        this.relojes = relojes;
    }

    public String getReferencia() {
        return referencia;
    }

    public List<Reloj> getRelojes() {
        return relojes;
    }
}
