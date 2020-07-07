package com.proyectofinal.applicant2.dbApplicant;

public class dbNacionalidad {
    private String nombre;

    public dbNacionalidad() {
    }

    public dbNacionalidad(String nombre) {
        this.nombre = nombre;
    }


    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    @Override
    public String toString() {
        return "dbNacionalidad{" +
                ", nombre='" + nombre + '\'' +
                '}';
    }
}
