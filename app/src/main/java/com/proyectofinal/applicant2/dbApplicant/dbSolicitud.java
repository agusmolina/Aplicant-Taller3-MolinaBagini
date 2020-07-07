package com.proyectofinal.applicant2.dbApplicant;

public class dbSolicitud {
    private String id_empleador;
    private String id_oficio;
    private String id_modalidadOficio;
    private String id_postulante;
    private String id_estadoSolicitud;

    public dbSolicitud() {

    }

    public dbSolicitud(String id_empleador, String id_oficio, String id_modalidadOficio, String id_estadoSolicitud) {
        this.id_empleador = id_empleador;
        this.id_oficio = id_oficio;
        this.id_modalidadOficio = id_modalidadOficio;
        this.id_estadoSolicitud = id_estadoSolicitud;
    }

    public String getId_empleador() {
        return id_empleador;
    }

    public void setId_empleador(String id_empleador) {
        this.id_empleador = id_empleador;
    }

    public String getId_oficio() {
        return id_oficio;
    }

    public void setId_oficio(String id_oficio) {
        this.id_oficio = id_oficio;
    }

    public String getId_modalidadOficio() {
        return id_modalidadOficio;
    }

    public void setId_modalidadOficio(String id_modalidadOficio) {
        this.id_modalidadOficio = id_modalidadOficio;
    }

    public String getId_postulante() {
        return id_postulante;
    }

    public void setId_postulante(String id_postulante) {
        this.id_postulante = id_postulante;
    }

    public String getId_estadoSolicitud() {
        return id_estadoSolicitud;
    }

    public void setId_estadoSolicitud(String id_estadoSolicitud) {
        this.id_estadoSolicitud = id_estadoSolicitud;
    }

    @Override
    public String toString() {
        return "dbSolicitud{" +
                ", id_empleador='" + id_empleador + '\'' +
                ", id_oficio='" + id_oficio + '\'' +
                ", id_modalidadOficio='" + id_modalidadOficio + '\'' +
                ", id_postulante='" + id_postulante + '\'' +
                ", id_estadoSolicitud='" + id_estadoSolicitud + '\'' +
                '}';
    }
}
