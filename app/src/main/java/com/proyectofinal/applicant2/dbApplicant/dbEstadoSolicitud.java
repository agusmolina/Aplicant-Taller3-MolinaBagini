package com.proyectofinal.applicant2.dbApplicant;

public class dbEstadoSolicitud {
    private String id;
    private String descripcion;

    public dbEstadoSolicitud() {
    }

    public dbEstadoSolicitud(String id, String descripcion) {
        this.id = id;
        this.descripcion = descripcion;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    @Override
    public String toString() {
        return "dbEstadoSolicitud{" +
                "id='" + id + '\'' +
                ", descripcion='" + descripcion + '\'' +
                '}';
    }
}
