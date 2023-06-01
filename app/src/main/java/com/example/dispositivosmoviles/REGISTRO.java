package com.example.dispositivosmoviles;


import static android.content.ContentValues.TAG;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;


import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;


import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import timber.log.Timber;

public class REGISTRO extends AppCompatActivity {


    private FirebaseAuth mAuth;
    private EditText nombre;
    private EditText correo;
    private EditText contrasena;
    private EditText contrasenaconfirmacion;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registro);
        mAuth = FirebaseAuth.getInstance();
        nombre = findViewById(R.id.nombre);
        correo = findViewById(R.id.Correo);
        contrasena = findViewById(R.id.Contrasena);
        contrasenaconfirmacion = findViewById(R.id.ContrasenaConfirmacion);
        // Inicializa base de datos para guardas info del usuario
    }
    @Override
    public void onStart() {
        super.onStart();
        FirebaseUser currentUser = mAuth.getCurrentUser();
    }

    //IF CONTRASENAS COINCIDEN
    public void registrarUsuario(View view){
        if (contrasena.getText().toString().equals(contrasenaconfirmacion.getText().toString())) {
            mAuth.createUserWithEmailAndPassword(correo.getText().toString(), contrasena.getText().toString())
                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            //IF DE LA TAREA (SI NO TIENE INTERNET SE VA AL ELSE
                            if (task.isSuccessful()) {
                                //Registro exitoso se recopilaron los datos y se muestra mensaje
                                Toast.makeText(getApplicationContext(), "Usuario Creado exitosamente", Toast.LENGTH_SHORT).show();
                                FirebaseUser user = mAuth.getCurrentUser();
                                //Se devuelve a la pantalla principal
                                Intent i = new Intent(getApplicationContext(), MainActivity.class);
                                //Envia Mail de autenticacion
                                user.sendEmailVerification();
                                Toast.makeText(getApplicationContext(), "E-mail de Verificacion enviado", Toast.LENGTH_SHORT).show();

                                //Agrega datos a base de datos

                                Map<String, Object> data = new HashMap<>();
                                data.put("nombre", nombre.getText().toString());
                                data.put("correo", correo.getText().toString());
                                data.put("admin", false);
                                data.put("actividades", Arrays.asList(""));



                                FirebaseFirestore.getInstance().collection("Users")
                                        .add(data)
                                        .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                                            @Override
                                            public void onSuccess(DocumentReference documentReference) {
                                                Timber.tag(TAG).d("DocumentSnapshot written with ID: %s", documentReference.getId());
                                            }
                                        })
                                        .addOnFailureListener(new OnFailureListener() {
                                            @Override
                                            public void onFailure(@NonNull Exception e) {
                                                Timber.tag(TAG).w(e, "Error adding document");
                                            }
                                        });//fin de agregar database

                                startActivity(i);


                            }else{
                                Toast.makeText(getApplicationContext(), "Autenticacion Fallida",Toast.LENGTH_SHORT).show();

                            }
                        }
                    });

            //ELSE SI NO COINCIDEN
        }else{
            Toast.makeText(this,"Las contraseñas no coinciden", Toast.LENGTH_SHORT).show();
        }
    }




}//fin activity



