package com.example.dispositivosmoviles;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class Chat extends AppCompatActivity {


    private RecyclerView rvMensajes;
    private EditText etName;
    private EditText etMensaje;
    private ImageButton btnSend;
    private List<MensajeVO> lstMensajes;
    private AdapterRVMensajes mAdapterRVMensajes;


    private void setComponents(){
        rvMensajes = findViewById(R.id.rvMensajes);
        etName = findViewById(R.id.etName);
        etMensaje = findViewById(R.id.etMensaje);
        btnSend = findViewById(R.id.btnSend);

        lstMensajes = new ArrayList<>();
        mAdapterRVMensajes = new AdapterRVMensajes(lstMensajes);
        rvMensajes.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        rvMensajes.setAdapter(mAdapterRVMensajes);
        rvMensajes.setHasFixedSize(true);

        FirebaseFirestore.getInstance().collection("Chat")
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
                        if (queryDocumentSnapshots != null) {
                            for (DocumentChange mDocumentChange : queryDocumentSnapshots.getDocumentChanges()){
                                if(mDocumentChange.getType() == DocumentChange.Type.ADDED) {
                                    lstMensajes.add(mDocumentChange.getDocument().toObject(MensajeVO.class));
                                    mAdapterRVMensajes.notifyDataSetChanged();
                                    rvMensajes.smoothScrollToPosition(lstMensajes.size());
                                }

                            }

                        }
                    }
                });

        btnSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(etName.length() == 0 || etMensaje.length() == 0)
                    return;
                MensajeVO mMensajeVO = new MensajeVO();
                mMensajeVO.setMessage(etMensaje.getText().toString());
                mMensajeVO.setName(etName.getText().toString());
                FirebaseFirestore.getInstance().collection("Chat").add(mMensajeVO);
                etMensaje.setText("");

            }
        });



    }

    public void Volver(View view){

        Intent i = new Intent(getApplicationContext(), IniciarSesionActivity.class);
        startActivity(i);
        finish();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        setComponents();

    }
}