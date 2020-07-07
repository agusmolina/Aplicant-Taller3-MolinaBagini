package com.proyectofinal.applicant2.ui.inicio;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;


import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.proyectofinal.applicant2.R;
import com.proyectofinal.applicant2.dbApplicant.dbOficio;
import com.proyectofinal.applicant2.dbApplicant.dbSolicitud;
import com.proyectofinal.applicant2.login_inicio;
import com.proyectofinal.applicant2.solicitud;

import java.util.ArrayList;

public class inicioFragment extends Fragment  {
    ListView ListViewSolicitudes;
    Button ButtonBuscar;
    Spinner spinnerBuscarOficio;

    Button ButtonLogout;

    private inicioViewModel homeViewModel;

    final ArrayList<String> listaSolicitud = new ArrayList<>();
    final ArrayList<dbSolicitud> solicitudes = new ArrayList<>();

    GoogleSignInClient mGoogleSignInClient;


    public static final String USUARIO = "miUsuario";


    public View onCreateView(@NonNull LayoutInflater inflater,ViewGroup container, Bundle savedInstanceState) {
        homeViewModel = ViewModelProviders.of(this).get(inicioViewModel.class);

        View root = inflater.inflate(R.layout.fragment_inicio, container, false);

        Context context = getActivity();

        ListViewSolicitudes = (ListView) root.findViewById(R.id.ListViewSolicitudes);
        ButtonBuscar = root.findViewById(R.id.ButtonBuscar);
        spinnerBuscarOficio = root.findViewById(R.id.spinnerBuscarOficio);

        ButtonLogout = root.findViewById(R.id.ButtonLogout);
        // Configure sign-in to request the user's ID, email address, and basic
        // profile. ID and basic profile are included in DEFAULT_SIGN_IN.
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();

        // Build a GoogleSignInClient with the options specified by gso.
        mGoogleSignInClient = GoogleSignIn.getClient(getContext(), gso);


        ButtonBuscar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                buscar();
            }
        });

        ButtonLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                cerrarSesion();
            }
        });


        this.configuracion();

        ListViewSolicitudes.setOnItemClickListener(new AdapterView.OnItemClickListener(){
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(getContext(), solicitud.class);

                dbSolicitud solicitud = solicitudes.get(position);


                intent.putExtra("id_empleador",  solicitud.getId_empleador());
                intent.putExtra("id_estadoSolicitud", solicitud.getId_estadoSolicitud());
                intent.putExtra("id_modalidadOficio", solicitud.getId_modalidadOficio());
                intent.putExtra("id_oficio", solicitud.getId_oficio());



                intent.putExtra("id", id); // id
                intent.putExtra("position", position); // position


                startActivity(intent);
            }

        });

        return root;
    }




    private void configuracion(){
        this.cargarOficio();
        this.cargarSolicitudes();
    }

    private void cargarSolicitudes() {


        FirebaseFirestore db = FirebaseFirestore.getInstance();

        SharedPreferences preferencias = getContext().getSharedPreferences(USUARIO, Context.MODE_PRIVATE);
        String miModo = preferencias.getString("modo", "vacio");
        String miCorreo = preferencias.getString("correo", "vacio");
        String miOficio = preferencias.getString("oficio", "vacio");

        //Log.i("oficio-inicio", miOficio);


        db.collection("dbSolicitud").whereEqualTo("id_oficio", miOficio).whereEqualTo("id_estadoSolicitud", "activa")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                ArrayAdapter adaptador = new ArrayAdapter(getContext(),android.R.layout.simple_list_item_1,listaSolicitud);

                                dbSolicitud solicitud = document.toObject(dbSolicitud.class);

                                listaSolicitud.add(solicitud.getId_empleador()+": "+solicitud.getId_oficio()+" - "+solicitud.getId_modalidadOficio());

                                Log.d("add-solicitud", "getId "+document.getId());

                                ListViewSolicitudes.setAdapter(adaptador);

                                solicitudes.add(solicitud);


                            }

                        } else {
                            Log.d("dbSolicitud", "Error getting documents: ", task.getException());
                        }
                    }
                });
    }



        /*db.collection("dbSolicitud").whereEqualTo("id_oficio", busqueda)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                ArrayAdapter adaptador = new ArrayAdapter(getContext(),android.R.layout.simple_list_item_1,listaSolicitud);

                                dbSolicitud solicitud = document.toObject(dbSolicitud.class);

                                listaSolicitud.add(solicitud.getId_empleador()+": "+solicitud.getId_oficio()+" - "+solicitud.getId_modalidadOficio());

                                Log.d("add-solicitud", "getId "+document.getId());

                                ListViewSolicitudes.setAdapter(adaptador);
                            }

                        } else {
                            Log.d("dbSolicitud", "Error getting documents: ", task.getException());
                        }
                    }
                });*/


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

                                spinnerBuscarOficio.setAdapter(adaptador);

                            }

                        } else {
                            Log.d("dbOficio", "Error getting documents: ", task.getException());
                        }
                    }
                });
    }

    private void buscar() {
        final ArrayList<String> listaSolicitud = new ArrayList<>();
        final ArrayList<dbSolicitud> solicitudes = new ArrayList<>();


        FirebaseFirestore db = FirebaseFirestore.getInstance();

        String busqueda = spinnerBuscarOficio.getSelectedItem().toString();
        listaSolicitud.clear();


        db.collection("dbSolicitud").whereEqualTo("id_oficio", busqueda)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                ArrayAdapter adaptador = new ArrayAdapter(getContext(), android.R.layout.simple_list_item_1, listaSolicitud);

                                dbSolicitud solicitud = document.toObject(dbSolicitud.class);

                                listaSolicitud.add(solicitud.getId_empleador() + ": " + solicitud.getId_oficio() + " - " + solicitud.getId_modalidadOficio());

                                Log.d("add-solicitud", "getId " + document.getId());

                                ListViewSolicitudes.setAdapter(adaptador);


                                solicitudes.add(solicitud);

                            }

                        } else {
                            Log.d("dbSolicitud", "Error getting documents: ", task.getException());
                        }
                    }
                });
    }

    private void cerrarSesion(){
        mGoogleSignInClient.signOut()
                .addOnCompleteListener(getActivity(), new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        Toast.makeText(getContext(), "Successfully signed out", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(getContext(), login_inicio.class));
                    }
                });
    }
}
