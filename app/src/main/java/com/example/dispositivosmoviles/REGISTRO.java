package com.example.dispositivosmoviles;


import static android.content.ContentValues.TAG;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Application;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.auth.api.identity.BeginSignInRequest;
import com.google.android.gms.auth.api.identity.SignInClient;
import com.google.android.gms.auth.api.identity.SignInCredential;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;

public class REGISTRO extends AppCompatActivity {


    private FirebaseAuth mAuth;
    private EditText correo;
    private EditText contrasena;
    private EditText contrasenaconfirmacion;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registro);
        mAuth = FirebaseAuth.getInstance();
        correo = findViewById(R.id.Correo);
        contrasena = findViewById(R.id.Contrasena);
        contrasenaconfirmacion = findViewById(R.id.ContrasenaConfirmacion);
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
                                //Envia correo de verificacion
                                user.sendEmailVerification();
                                //Mensaje de correo enviado
                                Toast.makeText(getApplicationContext(), "E-mail de Verificacion enviado", Toast.LENGTH_SHORT).show();
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
    } }



