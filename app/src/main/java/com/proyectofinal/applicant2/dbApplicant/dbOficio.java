package com.proyectofinal.applicant2.dbApplicant;

public class dbOficio {
    private String descripcion;

    public dbOficio() {
        ;
    }

    public dbOficio(String descripcion) {
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
        return "dbOficio{" +
                ", descripcion='" + descripcion + '\'' +
                '}';
    }
}
