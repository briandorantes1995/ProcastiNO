package com.example.dispositivosmoviles;



import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import androidx.appcompat.app.AppCompatActivity;
import java.util.Locale;

import android.content.res.Configuration;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.Locale;

public class MainActivity extends AppCompatActivity {
    private Button button;
    private Locale locale;
    private Configuration config = new Configuration();
    private FirebaseAuth mAuth;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inicio);

        mAuth = FirebaseAuth.getInstance();
        FirebaseUser user = mAuth.getCurrentUser();

    }


    public void Lenguaje(View view){
        {
            showDialog();
        }};

    //FUNCION DE BOTONES "IR" HACIA CADA PANTALLA
    public void irIniciar(View view){
        Intent i = new Intent (this, IniciarSesionActivity.class);
        startActivity(i);
    }
    public void irRegistrar(View view){
        Intent i = new Intent(this, REGISTRO.class);
        startActivity(i);

    }

    private void showDialog(){
        AlertDialog.Builder b = new AlertDialog.Builder(this);
        b.setTitle(getResources().getString(R.string.btn_Lenguaje));
        //obtiene los idiomas del array de string.xml
        String[] types = getResources().getStringArray(R.array.languages);
        b.setItems(types, new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {

                dialog.dismiss();
                switch(which){
                    case 0:
                        locale = new Locale("en");
                        config.locale =locale;
                        break;
                    case 1:
                        locale = new Locale("es");
                        config.locale =locale;
                        break;
                    case 2:
                        locale = new Locale("pt");
                        config.locale =locale;
                        break;
                }
                getResources().updateConfiguration(config, null);
                Intent refresh = new Intent(MainActivity.this, MainActivity.class);
                startActivity(refresh);
                finish();
            }

        });

        b.show();
    }





}