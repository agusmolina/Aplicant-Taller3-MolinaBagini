package com.proyectofinal.applicant2.dbApplicant;

public class dbUsuario {

    // usuario
    private String modo;
    private String nombre;
    private String apellido;
    private String correo;
    private String telefono;
    private String direccion;

    // postulante
    private String tipoDocumento;
    private String documento;
    private String fechaNacimiento;
    private String nacionalidad;
    private String sexo;
    private String oficio;

    // empleador
    private String razonSocial;
    private String cuit;
    private String fechaInicio;

    // vacio
    public dbUsuario() {
    }

    // completo
    public dbUsuario(String modo, String nombre, String apellido, String correo, String telefono, String direccion, String tipoDocumento, String documento, String fechaNacimiento, String nacionalidad, String sexo, String oficio, String razonSocial, String cuit, String fechaInicio) {
        this.modo = modo;
        this.nombre = nombre;
        this.apellido = apellido;
        this.correo = correo;
        this.telefono = telefono;
        this.direccion = direccion;
        this.tipoDocumento = tipoDocumento;
        this.documento = documento;
        this.fechaNacimiento = fechaNacimiento;
        this.nacionalidad = nacionalidad;
        this.sexo = sexo;
        this.oficio = oficio;
        this.razonSocial = razonSocial;
        this.cuit = cuit;
        this.fechaInicio = fechaInicio;
    }

    // postulante
    public dbUsuario(String modo, String nombre, String apellido, String correo, String telefono, String direccion, String tipoDocumento, String documento, String fechaNacimiento, String nacionalidad, String sexo, String oficio) {
        this.modo = modo;
        this.nombre = nombre;
        this.apellido = apellido;
        this.correo = correo;
        this.telefono = telefono;
        this.direccion = direccion;
        this.tipoDocumento = tipoDocumento;
        this.documento = documento;
        this.fechaNacimiento = fechaNacimiento;
        this.nacionalidad = nacionalidad;
        this.sexo = sexo;
        this.oficio = oficio;
    }

    // empleador
    public dbUsuario(String modo, String correo, String telefono, String direccion, String razonSocial, String cuit, String fechaInicio) {
        this.modo = modo;
        this.correo = correo;
        this.telefono = telefono;
        this.direccion = direccion;
        this.razonSocial = razonSocial;
        this.cuit = cuit;
        this.fechaInicio = fechaInicio;
    }


    public String getModo() {
        return modo;
    }

    public void setModo(String modo) {
        this.modo = modo;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getApellido() {
        return apellido;
    }

    public void setApellido(String apellido) {
        this.apellido = apellido;
    }

    public String getCorreo() {
        return correo;
    }

    public void setCorreo(String correo) {
        this.correo = correo;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    public String getTipoDocumento() {
        return tipoDocumento;
    }

    public void setTipoDocumento(String tipoDocumento) {
        this.tipoDocumento = tipoDocumento;
    }

    public String getDocumento() {
        return documento;
    }

    public void setDocumento(String documento) {
        this.documento = documento;
    }

    public String getFechaNacimiento() {
        return fechaNacimiento;
    }

    public void setFechaNacimiento(String fechaNacimiento) {
        this.fechaNacimiento = fechaNacimiento;
    }

    public String getNacionalidad() {
        return nacionalidad;
    }

    public void setNacionalidad(String nacionalidad) {
        this.nacionalidad = nacionalidad;
    }

    public String getSexo() {
        return sexo;
    }

    public void setSexo(String sexo) {
        this.sexo = sexo;
    }

    public String getOficio() {
        return oficio;
    }

    public void setOficio(String oficio) {
        this.oficio = oficio;
    }

    public String getRazonSocial() {
        return razonSocial;
    }

    public void setRazonSocial(String razonSocial) {
        this.razonSocial = razonSocial;
    }

    public String getCuit() {
        return cuit;
    }

    public void setCuit(String cuit) {
        this.cuit = cuit;
    }

    public String getFechaInicio() {
        return fechaInicio;
    }

    public void setFechaInicio(String fechaInicio) {
        this.fechaInicio = fechaInicio;
    }

    public String mostrarPostulante() {
        return "dbUsuario{" +
                "modo=" + modo +
                ", nombre='" + nombre + '\'' +
                ", apellido='" + apellido + '\'' +
                ", correo='" + correo + '\'' +
                ", telefono='" + telefono + '\'' +
                ", direccion='" + direccion + '\'' +
                ", tipoDocumento='" + tipoDocumento + '\'' +
                ", documento=" + documento +
                ", fechaNacimiento='" + fechaNacimiento + '\'' +
                ", nacionalidad='" + nacionalidad + '\'' +
                ", sexo='" + sexo + '\'' +
                ", oficio='" + oficio + '\'' +
                '}';
    }

    public String mostrarEmpleador() {
        return "dbUsuario{" +
                "modo=" + modo +
                ", correo='" + correo + '\'' +
                ", telefono='" + telefono + '\'' +
                ", direccion='" + direccion + '\'' +
                ", razonSocial='" + razonSocial + '\'' +
                ", cuit='" + cuit + '\'' +
                ", fechaInicio='" + fechaInicio + '\'' +
                '}';
    }

    private boolean validarNombre(String nombre){
        boolean ok=false;

        return ok;
    }
    private boolean validarApellido(String apellido){
        boolean ok=false;

        return ok;
    }
    private boolean validarDocumento(String documento){
        boolean ok=false;

        return ok;
    }
    private boolean validarTelefono(String telefono){
        boolean ok=false;

        return ok;
    }

    private boolean validarDireccion(String direccion){
        boolean ok=false;

        return ok;
    }

    private boolean validarOficio(String oficio){
        boolean ok=false;

        return ok;
    }

    private boolean validarRazonSocial(String razonSocial){
        boolean ok=false;

        return ok;
    }

    private boolean validarCuit(String cuit){
        boolean ok=false;

        return ok;
    }
}
