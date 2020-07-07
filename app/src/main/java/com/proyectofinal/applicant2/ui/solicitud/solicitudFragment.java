package com.proyectofinal.applicant2.ui.solicitud;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.proyectofinal.applicant2.MainActivity;
import com.proyectofinal.applicant2.R;
import com.proyectofinal.applicant2.dbApplicant.dbModalidadOficio;
import com.proyectofinal.applicant2.dbApplicant.dbOficio;
import com.proyectofinal.applicant2.dbApplicant.dbSolicitud;
import com.proyectofinal.applicant2.dbApplicant.dbUsuario;
import com.proyectofinal.applicant2.solicitud;

import java.util.ArrayList;

public class solicitudFragment extends Fragment {

    Button ButtonSolicitar;
    EditText EditTextEmpleador;
    Spinner SpinnerOficio;
    Spinner SpinnerModalidadOficio;


    public static String MODO = "vacio";
    public static String CORREO = "vacio";

    private solicitudViewModel slideshowViewModel;

    public static final String USUARIO = "miUsuario";


    public View onCreateView(@NonNull LayoutInflater inflater,ViewGroup container, Bundle savedInstanceState) {
        slideshowViewModel = ViewModelProviders.of(this).get(solicitudViewModel.class);
        View root = inflater.inflate(R.layout.fragment_solicitud, container, false);


        SharedPreferences preferencias = getContext().getSharedPreferences(USUARIO, Context.MODE_PRIVATE);
        String miModo = preferencias.getString("modo", "vacio");
        String miCorreo = preferencias.getString("correo", "vacio");

        MODO=miModo;
        CORREO=miCorreo;

        if (MODO.equals("postulante")){
            new AlertDialog.Builder(getContext())
                    .setTitle("Solicitudes")
                    .setMessage("Los postulante no pueden generar solicitudes de trabajo.")
                    .setIcon(android.R.drawable.ic_dialog_alert)
                    .show();
            new CountDownTimer(3000, 1000) {

                public void onTick(long millisUntilFinished) {

                }

                public void onFinish() {
                    startActivity(new Intent(getContext(), MainActivity.class));
                }
            }.start();
        }



        ButtonSolicitar = root.findViewById(R.id.ButtonSolicitar);
        EditTextEmpleador = root.findViewById(R.id.EditTextEmpleador);
        SpinnerOficio = root.findViewById(R.id.spinnerOficio);
        SpinnerModalidadOficio = root.findViewById(R.id.spinnerModalidadOficio);

        ButtonSolicitar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                actualizar();
            }
        });

        this.configuracion();

        return root;
    }

    private void configuracion() {
        this.cargarEmpleador();
        this.cargarOficio();
        this.cargarModalidadOficio();
    }


     private void cargarEmpleador() {
             final FirebaseFirestore db = FirebaseFirestore.getInstance();
             db.collection("dbUsuario").whereEqualTo("correo", CORREO).limit(1)
                     .get()
                     .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                         @Override
                         public void onComplete(@NonNull Task<QuerySnapshot> task) {
                             if (task.isSuccessful()) {
                                 for (QueryDocumentSnapshot document : task.getResult()) {
                                     dbUsuario usuario = document.toObject(dbUsuario.class);
                                     EditTextEmpleador.setText(usuario.getRazonSocial());
                                 }
                             } else {
                                 Log.d("dbUsuario", "Error getting documents: ", task.getException());
                             }
                         }
                     });
         }

    private void cargarOficio() {
        final ArrayList<String> listaOficio = new ArrayList<>();

        FirebaseFirestore db = FirebaseFirestore.getInstance();

        db.collection("dbOficio").orderBy("descripcion")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                ArrayAdapter adaptador = new ArrayAdapter(getContext(),android.R.layout.simple_spinner_dropdown_item,listaOficio);

                                dbOficio oficio = document.toObject(dbOficio.class);

                                listaOficio.add(oficio.getDescripcion());

                                Log.d("add-oficio", "getId "+document.getId());

                                SpinnerOficio.setAdapter(adaptador);
                            }

                        } else {
                            Log.d("dbOficio", "Error getting documents: ", task.getException());
                        }
                    }
                });
    }

    private void cargarModalidadOficio() {
        final ArrayList<String> listaModalidadOficio = new ArrayList<>();

        FirebaseFirestore db = FirebaseFirestore.getInstance();

        db.collection("dbModalidadOficio").orderBy("descripcion")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                ArrayAdapter adaptador = new ArrayAdapter(getContext(),android.R.layout.simple_spinner_dropdown_item,listaModalidadOficio);

                                dbModalidadOficio modalidadOficio = document.toObject(dbModalidadOficio.class);

                                listaModalidadOficio.add(modalidadOficio.getDescripcion());

                                Log.d("add-modalidadOficio", "getId "+document.getId());

                                SpinnerModalidadOficio.setAdapter(adaptador);
                            }

                        } else {
                            Log.d("dbModalidadOficio", "Error getting documents: ", task.getException());
                        }
                    }
                });
    }



    private void actualizar() {
        if (this.verificarCampos()) {
            // Access a Cloud Firestore instance from your Activity
            final FirebaseFirestore db = FirebaseFirestore.getInstance();

            String empleador = obtenerEmpleador();
            String oficio = obtenerOficio();
            String modalidadOficio = obtenerModalidadOficio();
            String estadoSolicitud = obtenerEstadoSolicitud();

            dbSolicitud solicitud = new dbSolicitud(empleador, oficio, modalidadOficio, estadoSolicitud);

            // Add a new document with a generated ID
            db.collection("dbSolicitud")
                    .add(solicitud)
                    .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                        @Override
                        public void onSuccess(DocumentReference documentReference) {
                            Log.d("dbSolicitud", "DocumentSnapshot added with ID: " + documentReference.getId());
                            //Toast.makeText(login_postulante.this, "Postulante Registrado", Toast.LENGTH_LONG).show();
                            Intent intent = new Intent(getContext(), MainActivity.class);

                            intent.putExtra("id", documentReference.getId());

                            startActivity(intent);

                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Log.w("dbSolicitud", "Error adding document", e);


                        }
                    });
        } else {
            new AlertDialog.Builder(getContext())
                    .setTitle("Registrar solicitud")
                    .setMessage("Verificar los campos a completar.")
                    .setIcon(android.R.drawable.ic_dialog_alert)
                    .show();

        }
    }


    public boolean verificarCampos(){
        boolean ok=true;

        return ok;
    }

    public String obtenerEmpleador(){
        String empleador = EditTextEmpleador.getText().toString();
        return empleador;
    }
    public String obtenerOficio(){
        String oficio = SpinnerOficio.getSelectedItem().toString();
        return oficio;
    }
    public String obtenerModalidadOficio(){
        String modalidadOficio = SpinnerModalidadOficio.getSelectedItem().toString();
        return modalidadOficio;
    }
    public String obtenerEstadoSolicitud(){
        String estadoSolicitud = "activa";
        return estadoSolicitud;
    }
}
