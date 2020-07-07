package com.proyectofinal.applicant2.dbApplicant;

public class dbModalidadOficio {
    private String descripcion;

    public dbModalidadOficio() {

    }

    public dbModalidadOficio(String descripcion) {
        this.descripcion = descripcion;
    }


    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    @Override
    public String toString() {
        return "dbModalidadOficio{" +
                ", descripcion='" + descripcion + '\'' +
                '}';
    }
}
