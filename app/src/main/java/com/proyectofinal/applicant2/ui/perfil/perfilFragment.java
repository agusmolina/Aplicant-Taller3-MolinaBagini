package com.proyectofinal.applicant2.ui.perfil;

import android.content.Context;
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
import com.proyectofinal.applicant2.R;
import com.proyectofinal.applicant2.dbApplicant.dbNacionalidad;
import com.proyectofinal.applicant2.dbApplicant.dbOficio;
import com.proyectofinal.applicant2.dbApplicant.dbSexo;
import com.proyectofinal.applicant2.dbApplicant.dbTipoDocumento;
import com.proyectofinal.applicant2.dbApplicant.dbUsuario;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class perfilFragment extends Fragment {

    Button ButtonGuardarPostulante;
    Button ButtonGuardarEmpleador;

    EditText EditTextNombre;
    EditText EditTextApellido;
    Spinner spinnerTipoDocumento;
    EditText EditTextDocumento;
    EditText EditTextFechaNacimiento;
    Spinner spinnerSexo;
    Spinner spinnerNacionalidad;
    Spinner spinnerOficio;

    EditText EditTextRazonSocial;
    EditText EditTextCuit;
    EditText EditTextFechaInicio;

    EditText EditTextTelefono;
    EditText EditTextDireccion;


    public static String MODO = "vacio";
    public static String CORREO = "vacio";

    private perfilViewModel galleryViewModel;

    public static final String USUARIO = "miUsuario";

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        galleryViewModel = ViewModelProviders.of(this).get(perfilViewModel.class);
        //View root = inflater.inflate(R.layout.activity_ingreso_postulante, container, false);
        View root;

        SharedPreferences preferencias = getContext().getSharedPreferences(USUARIO, Context.MODE_PRIVATE);
        String miModo = preferencias.getString("modo", "vacio");
        String miCorreo = preferencias.getString("correo", "vacio");

        MODO=miModo;
        CORREO=miCorreo;

       if (miModo.equals("postulante")){
           root = inflater.inflate(R.layout.activity_ingreso_postulante, container, false);

           ButtonGuardarPostulante = root.findViewById(R.id.ButtonGuardarPostulante);

           EditTextNombre = root.findViewById(R.id.EditTextNombre);
           EditTextApellido = root.findViewById(R.id.EditTextApellido);
           spinnerTipoDocumento = root.findViewById(R.id.spinnerTipoDocumento);
           EditTextDocumento = root.findViewById(R.id.EditTextDocumento);
           EditTextFechaNacimiento = root.findViewById(R.id.EditTextFechaNacimiento);
           spinnerSexo = root.findViewById(R.id.spinnerSexo);
           spinnerNacionalidad = root.findViewById(R.id.spinnerNacionalidad);
           spinnerOficio = root.findViewById(R.id.spinnerOficio);

           EditTextTelefono = root.findViewById(R.id.EditTextTelefono);
           EditTextDireccion = root.findViewById(R.id.EditTextDireccion);

           ButtonGuardarPostulante.setOnClickListener(new View.OnClickListener() {
               @Override
               public void onClick(View view) {
                   if (verificarCamposPostulante()){
                       guardarPostulante();
                       new CountDownTimer(2000, 1000) {

                           public void onTick(long millisUntilFinished) {
                               Toast.makeText(getContext(), "Usuario actualizado", Toast.LENGTH_SHORT).show();

                           }

                           public void onFinish() {
                               cargarPostulante();
                           }
                       }.start();
                       //startActivity(new Intent(getContext(), MainActivity.class));
                   }

               }
           });

           this.configuracionPostulante();

       }else {
           root = inflater.inflate(R.layout.activity_ingreso_empleador, container, false);

           ButtonGuardarEmpleador = root.findViewById(R.id.ButtonGuardarEmpleador);

           EditTextRazonSocial = root.findViewById(R.id.EditTextRazonSocial);
           EditTextCuit = root.findViewById(R.id.EditTextCuit);
           EditTextFechaInicio = root.findViewById(R.id.EditTextFechaInicio);

           EditTextTelefono = root.findViewById(R.id.EditTextTelefono);
           EditTextDireccion = root.findViewById(R.id.EditTextDireccion);

           ButtonGuardarEmpleador.setOnClickListener(new View.OnClickListener() {
               @Override
               public void onClick(View view) {
                   if (verificarCamposEmpleador()) {
                       guardarEmpleador();
                       new CountDownTimer(2000, 1000) {

                           public void onTick(long millisUntilFinished) {
                               Toast.makeText(getContext(), "Usuario actualizado", Toast.LENGTH_SHORT).show();

                           }

                           public void onFinish() {
                               cargarEmpleador();
                           }
                       }.start();
                       //startActivity(new Intent(getContext(), MainActivity.class));
                   }
               }
           });

           this.configuracionEmpleador();

       }

       return root;
    }

    private void configuracionPostulante() {
        this.cargarTipoDocumento();
        this.cargarNacionalidad();
        this.cargarSexo();
        this.cargarOficio();

        this.cargarPostulante();
    }

    private void configuracionEmpleador() {
        this.cargarEmpleador();
    }

    private void cargarPostulante() {
        final FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("dbUsuario").whereEqualTo("correo", CORREO).limit(1)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                dbUsuario usuario = document.toObject(dbUsuario.class);
                                EditTextNombre.setText(usuario.getNombre());
                                EditTextApellido.setText(usuario.getApellido());
                                EditTextDocumento.setText(usuario.getDocumento());
                                EditTextTelefono.setText(usuario.getTelefono());
                                EditTextDireccion.setText(usuario.getDireccion());
                                EditTextFechaNacimiento.setText(usuario.getFechaNacimiento());


                                // ****
                                // funciona a medias

                                /*final List<String> listaTipoDocumento = new ArrayList<>();
                                listaTipoDocumento.add(usuario.getTipoDocumento());
                                final ArrayAdapter<String> adapterTipoDocumento = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_item,listaTipoDocumento);
                                spinnerTipoDocumento.setAdapter(adapterTipoDocumento);

                                final List<String> listaNacionalidad = new ArrayList<>();
                                listaNacionalidad.add(usuario.getNacionalidad());
                                final ArrayAdapter<String> adapterNacionalidad = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_item,listaNacionalidad);
                                spinnerNacionalidad.setAdapter(adapterNacionalidad);

                                final List<String> listaSexo = new ArrayList<>();
                                listaSexo.add(usuario.getSexo());
                                final ArrayAdapter<String> adapterSexo = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_item,listaSexo);
                                spinnerSexo.setAdapter(adapterSexo);

                                final List<String> listaOficio = new ArrayList<>();
                                listaOficio.add(usuario.getOficio());
                                final ArrayAdapter<String> adapterOficio = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_item,listaOficio);
                                spinnerOficio.setAdapter(adapterOficio);*/

                                // ****



                                //final List<String> idlist = new ArrayList<>();
                                //idlist.add(document.getId());

                                //list.add("Choose a Date");

                                /*final ArrayAdapter<String> adapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_item,list);
                                            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                                            spinnerTipoDocumento.setAdapter(adapter);

                                            spinnerTipoDocumento.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                                                @Override
                                                public void onItemSelected(AdapterView<?> adapterView, View view,final int i, long l) {
                                                    String id = idlist.get(i);
                                                    Toast.makeText(getContext(), "ID: " + id , Toast.LENGTH_SHORT).show();


                                                }

                                                @Override
                                                public void onNothingSelected(AdapterView<?> adapterView) {

                                                }
                                            });*/
                                /*db.collection("dbTipoDocumento").whereEqualTo("descripcion", usuario.getTipoDocumento()).limit(1)
                                        .get()
                                        .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                            @Override
                                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                                if (task.isSuccessful()) {
                                                    for (QueryDocumentSnapshot document : task.getResult()) {

                                                        dbTipoDocumento tipoDocumento = document.toObject(dbTipoDocumento.class);

                                                    }*/

                            }
                        } else {
                            Log.d("dbUsuario", "Error getting documents: ", task.getException());
                        }
                    }
                });
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
                                EditTextRazonSocial.setText(usuario.getRazonSocial());
                                EditTextCuit.setText(usuario.getCuit());
                                EditTextFechaInicio.setText(usuario.getFechaInicio());
                                EditTextTelefono.setText(usuario.getTelefono());
                                EditTextDireccion.setText(usuario.getDireccion());
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

                                spinnerOficio.setAdapter(adaptador);
                            }

                        } else {
                            Log.d("dbOficio", "Error getting documents: ", task.getException());
                        }
                    }
                });
    }

    private void cargarSexo() {
        final ArrayList<String> listaSexo = new ArrayList<>();

        FirebaseFirestore db = FirebaseFirestore.getInstance();

        db.collection("dbSexo").orderBy("tipo")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                ArrayAdapter adaptador = new ArrayAdapter(getContext(),android.R.layout.simple_spinner_dropdown_item,listaSexo);

                                dbSexo sexo = document.toObject(dbSexo.class);

                                listaSexo.add(sexo.getTipo());

                                Log.d("add-sexo", "getId "+document.getId());

                                spinnerSexo.setAdapter(adaptador);
                            }

                        } else {
                            Log.d("db-sexo", "Error getting documents: ", task.getException());
                        }
                    }
                });
    }

    private void cargarNacionalidad() {
        final ArrayList<String> listaNacionalidad = new ArrayList<>();

        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("dbNacionalidad").orderBy("nombre")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                ArrayAdapter adaptador = new ArrayAdapter(getContext(),android.R.layout.simple_spinner_dropdown_item,listaNacionalidad);

                                dbNacionalidad nacionalidad = document.toObject(dbNacionalidad.class);

                                listaNacionalidad.add(nacionalidad.getNombre());

                                Log.d("add-nacionalidad", "getId "+document.getId());

                                spinnerNacionalidad.setAdapter(adaptador);
                            }

                        } else {
                            Log.d("dbNacionalidad", "Error getting documents: ", task.getException());
                        }
                    }
                });
    }

    private void cargarTipoDocumento() {
        final ArrayList<String> listaTipoDocumento = new ArrayList<>();

        FirebaseFirestore db = FirebaseFirestore.getInstance();

        db.collection("dbTipoDocumento").orderBy("descripcion")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                ArrayAdapter adaptador = new ArrayAdapter(getContext(),android.R.layout.simple_spinner_dropdown_item,listaTipoDocumento);

                                dbTipoDocumento tipoDocumento = document.toObject(dbTipoDocumento.class);

                                listaTipoDocumento.add(tipoDocumento.getDescripcion());

                                Log.d("add-tipoDocumento", "getId "+document.getId());

                                spinnerTipoDocumento.setAdapter(adaptador);
                            }

                        } else {
                            Log.d("dbTipoDocumento", "Error getting documents: ", task.getException());
                        }
                    }
                });
    }

    private void guardarEmpleador() {
            // Access a Cloud Firestore instance from your Activity
            final FirebaseFirestore db = FirebaseFirestore.getInstance();
            db.collection("dbUsuario").whereEqualTo("correo", CORREO).limit(1)
                    .get()
                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                            if (task.isSuccessful()) {
                                for (QueryDocumentSnapshot document : task.getResult()) {
                                    String usuario = document.getId();



                                    DocumentReference documentoUsuario = db.collection("dbUsuario").document(usuario);
                                    // [START update_document]
                                    // update razonSocial
                                    documentoUsuario
                                            .update("razonSocial", obtenerRazonSocial())
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
                                    // update cuit
                                    documentoUsuario
                                            .update("cuit", obtenerCuit())
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
                                    // update fechaInicio
                                    documentoUsuario
                                            .update("fechaInicio", obtenerFechaInicio())
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
                                    // update direccion
                                    documentoUsuario
                                            .update("direccion", obtenerDireccion())
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
                                    // update telefono
                                    documentoUsuario
                                            .update("telefono", obtenerTelefono())
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

                                }
                            } else {
                                Log.d("dbUsuario", "Error getting documents: ", task.getException());
                            }
                        }
                    });
    }



    private void guardarPostulante() {
            // Access a Cloud Firestore instance from your Activity
            final FirebaseFirestore db = FirebaseFirestore.getInstance();
            db.collection("dbUsuario").whereEqualTo("correo", CORREO).limit(1)
                    .get()
                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                            if (task.isSuccessful()) {
                                for (QueryDocumentSnapshot document : task.getResult()) {
                                    String usuario = document.getId();
                                    DocumentReference documentoUsuario = db.collection("dbUsuario").document(usuario);
                                    // [START update_document]
                                    // update nombre
                                    documentoUsuario
                                            .update("nombre", obtenerNombre())
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
                                    // update apellido
                                    documentoUsuario
                                            .update("apellido", obtenerApellido())
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
                                    // update tipoDocumento
                                    documentoUsuario
                                            .update("tipoDocumento", obtenerTipoDocumento())
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
                                    // update documento
                                    documentoUsuario
                                            .update("documento", obtenerDocumento())
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
                                    // update fechaNacimiento
                                    documentoUsuario
                                            .update("fechaNacimiento", obtenerFechaNacimiento())
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
                                    // update nacionalidad
                                    documentoUsuario
                                            .update("nacionalidad", obtenerNacionalidad())
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
                                    // update sexo
                                    documentoUsuario
                                            .update("sexo", obtenerSexo())
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
                                    // update direccion
                                    documentoUsuario
                                            .update("direccion", obtenerDireccion())
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
                                    // update telefono
                                    documentoUsuario
                                            .update("telefono", obtenerTelefono())
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
                                    // update oficio
                                    documentoUsuario
                                            .update("oficio", obtenerOficio())
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

                                }
                            } else {
                                Log.d("dbUsuario", "Error getting documents: ", task.getException());
                            }
                        }
                    });

    }

    public boolean verificarCamposEmpleador(){
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

    public boolean verificarCamposPostulante(){
        boolean ok=true;

        Pattern validarString = Pattern.compile("[0-9$&+,:;=\\\\\\\\?@#|/'<>.^*()%!-]");
        Pattern validarInt = Pattern.compile("[A-Za-z$&+,:;=\\\\\\\\?@#|/'<>.^*()%!-]");
        Pattern validarSpecial = Pattern.compile("[$&+,:;=\\\\\\\\?@#|/'<>^*()%!-]");
        Pattern validarFecha = Pattern.compile("[A-Za-z$&+,:;=\\\\\\\\?@#|'<>.^*()%!-]");


        String nombre = obtenerNombre();
        Matcher validNombre = validarString.matcher(nombre);
        if (nombre.equals("") || validNombre.find()) {
            if (nombre.equals("")) {
                EditTextNombre.setError("El nombre esta vacio");
            } else {
                EditTextNombre.setError("Debe ingresar un nombre valido");
            }
            ok=false;
        }

        String apellido = obtenerApellido();
        Matcher validApellido = validarString.matcher(apellido);
        if (apellido.equals("") || validApellido.find()) {
            if (apellido.equals("")) {
                EditTextApellido.setError("El apellido esta vacio");
            } else {
                EditTextApellido.setError("Debe ingresar un apellido valido");
            }
            ok=false;
        }

        String documento = obtenerDocumento();
        Matcher validDocumento = validarInt.matcher(documento);
        if (documento.equals("") || validDocumento.find()) {
            if (documento.equals("")) {
                EditTextDocumento.setError("El documento esta vacio");
            } else {
                EditTextDocumento.setError("Debe ingresar un documento valido");
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

        String fechaNacimiento = obtenerFechaNacimiento();
        Matcher validFechaNaciento = validarFecha.matcher(fechaNacimiento);
        if (fechaNacimiento.equals("") || validFechaNaciento.find()) {
            if (fechaNacimiento.equals("")) {
                EditTextFechaNacimiento.setError("La fecha de nacimiento esta vacia");
            } else {
                EditTextFechaNacimiento.setError("Debe ingresar una fecha de nacimiento valida");
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
    public String obtenerNombre(){
        String nombre = EditTextNombre.getText().toString().toUpperCase();
        return nombre;
    }
    public String obtenerApellido(){
        String apellido = EditTextApellido.getText().toString().toUpperCase();
        return apellido;
    }
    public String obtenerTipoDocumento(){
        String tipoDocumento = spinnerTipoDocumento.getSelectedItem().toString();
        return tipoDocumento;
    }
    public String obtenerDocumento(){
        String documento =  EditTextDocumento.getText().toString();
        return documento;
    }
    public String obtenerFechaNacimiento(){
        String fechaNacimiento = EditTextFechaNacimiento.getText().toString();
        return fechaNacimiento;
    }
    public String obtenerSexo(){
        String sexo = spinnerSexo.getSelectedItem().toString();
        return sexo;
    }
    public String obtenerNacionalidad(){
        String nacionalidad = spinnerNacionalidad.getSelectedItem().toString();
        return nacionalidad;
    }
    public String obtenerOficio(){
        String oficio = spinnerOficio.getSelectedItem().toString();
        return oficio;
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
