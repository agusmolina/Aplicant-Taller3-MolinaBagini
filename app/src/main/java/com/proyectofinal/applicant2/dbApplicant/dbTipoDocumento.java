package com.proyectofinal.applicant2.dbApplicant;

public class dbTipoDocumento {
    private String descripcion;

    public dbTipoDocumento() {
    }

    public dbTipoDocumento(String descripcion) {
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
        return "dbTipoDocumento{" +
                ", descripcion='" + descripcion + '\'' +
                '}';
    }
}
