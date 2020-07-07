package com.proyectofinal.applicant2;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.proyectofinal.applicant2.dbApplicant.dbSolicitud;
import com.proyectofinal.applicant2.dbApplicant.dbUsuario;


public class solicitud extends AppCompatActivity {

    public static String MODO = "vacio";
    public static String CORREO = "vacio";

    public static String ID = "vacio";
    public static String POSITION = "vacio";

    public static String ID_EMPLEADOR = "vacio";
    public static String ID_ESTADOSOLICITUD = "vacio";
    public static String ID_MODALIDADOFICIO = "vacio";
    public static String ID_OFICIO = "vacio";

    public static final String USUARIO = "miUsuario";

    Button ButtonSolicitar;
    Button ButtonVolver;
    EditText EditTextEmpleador;
    EditText EditTextCuit;
    EditText EditTextFechaInicio;
    EditText EditTextOficio;
    EditText EditTextModalidadOficio;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_solicitud);

        ButtonSolicitar = findViewById(R.id.ButtonSolicitar);
        ButtonVolver = findViewById(R.id.ButtonVolver);
        EditTextEmpleador = findViewById(R.id.EditTextEmpleador);
        EditTextCuit = findViewById(R.id.EditTextCuit);
        EditTextFechaInicio = findViewById(R.id.EditTextFechaInicio);
        EditTextOficio = findViewById(R.id.EditTextOficio);
        EditTextModalidadOficio = findViewById(R.id.EditTextModalidadOficio);

        // ***** DATOS DEL EMPLEADOR *****
        SharedPreferences preferencias = this.getSharedPreferences(USUARIO, Context.MODE_PRIVATE);
        String miModo = preferencias.getString("modo", "vacio");
        String miCorreo = preferencias.getString("correo", "vacio");

        MODO=miModo;
        CORREO=miCorreo;
        // *******************************

        // ***** DATOS DE LA SOLICITUD *****
        Bundle bundle = getIntent().getExtras();
        final String miId = bundle.getString("id");
        final String miPosition = bundle.getString("position");

        final String miId_empleador = bundle.getString("id_empleador");
        final String miId_estadoSolicitud = bundle.getString("id_estadoSolicitud");
        final String miId_modalidadOficio = bundle.getString("id_modalidadOficio");
        final String miId_oficio = bundle.getString("id_oficio");

        ID=miId;
        POSITION=miPosition;

        ID_EMPLEADOR=miId_empleador;
        ID_ESTADOSOLICITUD=miId_estadoSolicitud;
        ID_MODALIDADOFICIO=miId_modalidadOficio;
        ID_OFICIO=miId_oficio;
        // *******************************


        ButtonSolicitar.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    new AlertDialog.Builder(solicitud.this)
                            .setTitle("Aplicar solicitud")
                            .setMessage("¿Desea aplicar la solcitud?")
                            .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    confirmar();
                                    volver();
                                }
                            })
                            .setNegativeButton(android.R.string.no, null)
                            .setIcon(android.R.drawable.ic_dialog_alert)
                            .show();
                }
        });

        ButtonVolver.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                volver();
            }
        });

        this.configuracion();;
    }

    private void configuracion() {
        this.cargarSolicitud();
    }


    private void cargarSolicitud(){
        EditTextEmpleador.setText(ID_EMPLEADOR);
        EditTextOficio.setText(ID_OFICIO);
        EditTextModalidadOficio.setText(ID_MODALIDADOFICIO);

        final FirebaseFirestore db = FirebaseFirestore.getInstance();

        /*db.collection("dbSolicitud").whereEqualTo("id", ID).limit(1)
                    .get()
                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                            if (task.isSuccessful()) {
                                for (QueryDocumentSnapshot document : task.getResult()) {
                                    dbSolicitud solicitud = document.toObject(dbSolicitud.class);
                                    EditTextEmpleador.setText(solicitud.getId_empleador());
                                    EditTextOficio.setText(solicitud.getId_oficio());
                                    EditTextModalidadOficio.setText(solicitud.getId_modalidadOficio());
                                }
                            } else {
                                Log.d("dbSolicitud", "Error getting documents: ", task.getException());
                            }
                        }
                    });*/

        /*db.collection("dbUsuario").whereEqualTo("correo", CORREO).limit(1)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                dbUsuario usuario = document.toObject(dbUsuario.class);
                                EditTextCuit.setText(usuario.getCuit());
                                EditTextFechaInicio.setText(usuario.getFechaInicio());
                            }
                        } else {
                            Log.d("dbUsuario", "Error getting documents: ", task.getException());
                        }
                    }
                });*/
        }

    private void confirmar() {
        // Access a Cloud Firestore instance from your Activity
        final FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("dbSolicitud").whereEqualTo("id_empleador", ID_EMPLEADOR).whereEqualTo("id_estadoSolicitud", ID_ESTADOSOLICITUD).whereEqualTo("id_estadoSolicitud", ID_ESTADOSOLICITUD).whereEqualTo("id_modalidadOficio", ID_MODALIDADOFICIO).whereEqualTo("id_oficio", ID_OFICIO).limit(1)
            .get()
            .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
            if (task.isSuccessful()) {
                for (QueryDocumentSnapshot document : task.getResult()) {
                    String solicitud = document.getId();
                    DocumentReference documentoSolicitud = db.collection("dbSolicitud").document(solicitud);
                    // [START update_document]
                    // update estado de la solicitud
                    documentoSolicitud
                        .update("id_estadoSolicitud", obtenerEstadoSolicitud())
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                //Log.i("update", "ok");
                            }
                        })
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    //Log.i("update", "error");
                                }
                            });
                    // [END update_document]

                }
            } else {
                Log.d("dbSolicitud", "Error getting documents: ", task.getException());
            }
                        }
            });
    }


    public String obtenerEstadoSolicitud(){
        String estadoSolicitud = "solicitada";
        return estadoSolicitud;
    }

    private void volver() {
        startActivity(new Intent(solicitud.this, MainActivity.class));
    }
}
