package com.proyectofinal.applicant2;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ingreso_empleador extends AppCompatActivity {

    public static final String USUARIO = "miUsuario";

    Button ButtonGuardarEmpleado;

    EditText EditTextRazonSocial;
    EditText EditTextCuit;
    EditText EditTextFechaInicio;

    EditText EditTextTelefono;
    EditText EditTextDireccion;

    public static String MODO = "vacio";
    public static String CORREO = "vacio";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ingreso_empleador);

        Bundle bundle = getIntent().getExtras();

        final String miModo = bundle.getString("modo");
        final String miCorreo = bundle.getString("correo");

        MODO=miModo;
        CORREO=miCorreo;


        ButtonGuardarEmpleado = findViewById(R.id.ButtonGuardarEmpleador);

        EditTextRazonSocial = findViewById(R.id.EditTextRazonSocial);
        EditTextCuit = findViewById(R.id.EditTextCuit);
        EditTextFechaInicio = findViewById(R.id.EditTextFechaInicio);

        EditTextTelefono = findViewById(R.id.EditTextTelefono);
        EditTextDireccion = findViewById(R.id.EditTextDireccion);

        ButtonGuardarEmpleado.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                guardar();
            }
        });
    }

    private void guardar() {
        if (this.verificarCampos()) {
            // Access a Cloud Firestore instance from your Activity
            final FirebaseFirestore db = FirebaseFirestore.getInstance();

            Map<String, Object> usuario = new HashMap<>();
            usuario.put("modo", obtenerModo());
            usuario.put("correo", obtenerCorreo());
            usuario.put("telefono", obtenerTelefono());
            usuario.put("direccion", obtenerDireccion());
            usuario.put("razonSocial", obtenerRazonSocial());
            usuario.put("cuit", obtenerCuit());
            usuario.put("fechaInicio", obtenerFechaInicio());

            // Add a new document with a generated ID
            db.collection("dbUsuario")
                    .add(usuario)
                    .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                        @Override
                        public void onSuccess(DocumentReference documentReference) {
                            Log.d("dbUsuario", "DocumentSnapshot added with ID: " + documentReference.getId());
                            //Toast.makeText(login_postulante.this, "Postulante Registrado", Toast.LENGTH_LONG).show();
                            Intent intent = new Intent(ingreso_empleador.this, MainActivity.class);

                            intent.putExtra("id", documentReference.getId());

                            startActivity(intent);

                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Log.w("dbUsuario", "Error adding document", e);
                        }
                    });
        } else {
            new AlertDialog.Builder(ingreso_empleador.this)
                    .setTitle("Registrar postulante")
                    .setMessage("Verificar los campos a completar.")
                    .setIcon(android.R.drawable.ic_dialog_alert)
                    .show();

        }


    }
    public boolean verificarCampos(){
        boolean ok=true;

        Pattern validarString = Pattern.compile("[0-9$&+,:;=\\\\\\\\?@#|/'<>.^*()%!-]");
        Pattern validarInt = Pattern.compile("[A-Za-z$&+,:;=\\\\\\\\?@#|/'<>.^*()%!-]");
        Pattern validarSpecial = Pattern.compile("[$&+,:;=\\\\\\\\?@#|/'<>^*()%!-]");
        Pattern validarFecha = Pattern.compile("[A-Za-z$&+,:;=\\\\\\\\?@#|'<>.^*()%!-]");


        String razonSocial = obtenerRazonSocial();
        Matcher validRazonSocial = validarSpecial.matcher(razonSocial);
        if (razonSocial.equals("") || validRazonSocial.find()) {
            if (razonSocial.equals("")) {
                EditTextRazonSocial.setError("La razon social esta vacia");
            } else {
                EditTextRazonSocial.setError("Debe ingresar una razon social valida");
            }
            ok=false;
        }


        String cuit = obtenerCuit();
        Matcher validCuit = validarInt.matcher(cuit);
        if (cuit.equals("") || validCuit.find()) {
            if (cuit.equals("")) {
                EditTextCuit.setError("El cuit esta vacio");
            } else {
                EditTextCuit.setError("Debe ingresar un cuit valido");
            }
            ok=false;
        }

        String fechaInicio = obtenerFechaInicio();
        Matcher validFechaInicio = validarFecha.matcher(fechaInicio);
        if (fechaInicio.equals("") || validFechaInicio.find()) {
            if (fechaInicio.equals("")) {
                EditTextFechaInicio.setError("La fecha de inicio esta vacia");
            } else {
                EditTextFechaInicio.setError("Debe ingresar una fecha de inicio valida");
            }
            ok=false;
        }

        String telefono = obtenerTelefono();
        Matcher validTelefono = validarInt.matcher(telefono);
        if (telefono.equals("") || validTelefono.find()) {
            if (telefono.equals("")) {
                EditTextTelefono.setError("El telefono esta vacio");
            } else {
                EditTextTelefono.setError("Debe ingresar un telefono valido");
            }
            ok=false;
        }

        String direccion = obtenerDireccion();
        Matcher validDireccion = validarSpecial.matcher(direccion);
        if (direccion.equals("") || validDireccion.find()) {
            if (direccion.equals("")) {
                EditTextDireccion.setError("La direccion esta vacia");
            } else {
                EditTextDireccion.setError("Debe ingresar una direccion valida");
            }
            ok=false;
        }



                /*if (password.equals("") || password.length() < 3) {
                    // Toast.makeText(getApplicationContext(), "please enter password upto 6char ", 1000).show();
                    edittext_Password.setError("Password cannot be blank");

                }

                if (phone.equals("") || phone.length() < 10) {
                    edittext_phone.setError("Invalid mobile number");
                    //Toast.makeText(getApplicationContext(), "please entermobile number must be 10 digit", 1000).show();
                }
                if (uname == null && uname.equals(nameValidate) || password == null || phone == null) {
                    confirmOtp();
                }*/

        return ok;
    }
    public String obtenerModo(){
        String modo = MODO;
        return modo;
    }
    public String obtenerCorreo(){
        String correo = CORREO;
        return correo;
    }
    public String obtenerTelefono(){
        String telefono = EditTextTelefono.getText().toString();
        return telefono;
    }
    public String obtenerDireccion(){
        String direccion = EditTextDireccion.getText().toString().toUpperCase();
        return direccion;
    }
    public String obtenerRazonSocial(){
        String razonSocial = EditTextRazonSocial.getText().toString().toUpperCase();
        return razonSocial;
    }
    public String obtenerCuit(){
        String cuit =  EditTextCuit.getText().toString();
        return cuit;
    }
    public String obtenerFechaInicio(){
        String fechaInicio = EditTextFechaInicio.getText().toString();
        return fechaInicio;
    }
}
