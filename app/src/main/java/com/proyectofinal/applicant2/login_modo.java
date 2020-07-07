package com.proyectofinal.applicant2;


import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;


public class login_modo extends AppCompatActivity {


    public static String USUARIO_MODO = "";

    GoogleSignInClient mGoogleSignInClient;
    TextView TextViewNombre;
    TextView TextViewEmail;
    //ImageView ImageViewImagen;
    Button ButtonConfirmar;
    Button ButtonCerrarSesion;

    RadioGroup RadioGroupModo;
    RadioButton RadioButtonPostulante;
    RadioButton RadioButtonEmpleador;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_modo);

        ButtonCerrarSesion = findViewById(R.id.ButtonCerrarSesion);
        TextViewNombre = findViewById(R.id.TextViewNombre);
        TextViewEmail = findViewById(R.id.TextViewEmail);
        //ImageViewImagen = findViewById(R.id.ImageViewImagen);
        ButtonConfirmar = findViewById(R.id.ButtonConfirmar);
        RadioGroupModo = findViewById(R.id.RadioGroupModo);
        RadioButtonPostulante = findViewById(R.id.RadioButtonPostulante);
        RadioButtonEmpleador = findViewById(R.id.RadioButtonEmpleador);


        // Configure sign-in to request the user's ID, email address, and basic
        // profile. ID and basic profile are included in DEFAULT_SIGN_IN.
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();

        // Build a GoogleSignInClient with the options specified by gso.
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);

        GoogleSignInAccount acct = GoogleSignIn.getLastSignedInAccount(login_modo.this);
        String googleMail = acct.getEmail();
        if (acct != null) {
            String nombre = acct.getDisplayName();
            String email = acct.getEmail();
            //Uri personPhoto = acct.getPhotoUrl();

            TextViewNombre.setText(nombre);
            TextViewEmail.setText(email);
            //Glide.with(this).load(personPhoto).into(ImageViewImagen);
        }

        ButtonConfirmar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                confirmar();
            }
        });

        ButtonCerrarSesion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                cerrarSesion();
            }
        });

        RadioGroupModo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                obtenerModo();
            }
        });

        RadioButtonPostulante.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                USUARIO_MODO = "postulante";
            }
        });

        RadioButtonEmpleador.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                USUARIO_MODO = "empleador";
            }
        });
    }

    private void cerrarSesion() {
        mGoogleSignInClient.signOut()
                .addOnCompleteListener(this, new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        Toast.makeText(login_modo.this, "Successfully signed out", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(login_modo.this, login_inicio.class));
                        finish();
                    }
                });
    }

    private void confirmar() {
        if (USUARIO_MODO != "") {
            // Access a Cloud Firestore instance from your Activity
            if (USUARIO_MODO == "postulante") {
                Intent intent = new Intent(login_modo.this, ingreso_postulante.class);

                intent.putExtra("modo", USUARIO_MODO); // Postulante o Empleador
                intent.putExtra("correo", TextViewEmail.getText().toString()); // correo

                startActivity(intent);

            } else if (USUARIO_MODO == "empleador") {
                Intent intent = new Intent(login_modo.this, ingreso_empleador.class);

                intent.putExtra("modo", USUARIO_MODO); // Postulante o Empleador
                intent.putExtra("correo", TextViewEmail.getText().toString()); // correo

                startActivity(intent);
            }
        } else {
            new AlertDialog.Builder(login_modo.this)
                    .setTitle("Registrar usuario")
                    .setMessage("Debe seleccionar un modo para ingresar.")
                    .setIcon(android.R.drawable.ic_dialog_alert)
                    .show();

        }
    }

    public String obtenerModo(){
        String modo = USUARIO_MODO;
        return modo;
    }
}




