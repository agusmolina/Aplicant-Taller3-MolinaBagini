package com.proyectofinal.applicant2.dbApplicant;

public class dbModo {
    private String tipo;

    public dbModo() {
    }

    public dbModo(String tipo) {
        this.tipo = tipo;
    }


    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    @Override
    public String toString() {
        return "dbModo{" +
                ", tipo='" + tipo + '\'' +
                '}';
    }
}
