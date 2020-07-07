package com.proyectofinal.applicant2.dbApplicant;

public class dbSexo {
    private String tipo;

    public dbSexo() {
    }

    public dbSexo(String tipo) {
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
        return "dbSexo{" +
                ", tipo='" + tipo + '\'' +
                '}';
    }
}
