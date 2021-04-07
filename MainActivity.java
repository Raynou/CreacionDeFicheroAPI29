package com.example.crearundirectorio;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.io.File;

public class MainActivity extends AppCompatActivity {

    Button btnCreate, btnView;
    File myNewFolder;
    private static final int REQUEST_WRITE_EXTERNAL_STORAGE = 111;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        init();
    }

    private void init() {
        btnCreate = (Button) findViewById(R.id.btnCreate);
        btnView = (Button) findViewById(R.id.btnView);
        btnCreate.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void onClick(View v) {
                if (checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE)== PackageManager.PERMISSION_GRANTED){
                    createFolder();
                }else {
                    if (shouldShowRequestPermissionRationale(Manifest.permission.WRITE_EXTERNAL_STORAGE)){
                        Toast.makeText(MainActivity.this, "Se requiere permiso para acceder al almacenamiento para crear una carpeta", Toast.LENGTH_SHORT).show();
                    }
                    requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, REQUEST_WRITE_EXTERNAL_STORAGE);
                }
            }

        });

        btnView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                viewFolderDirection();
            }
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == REQUEST_WRITE_EXTERNAL_STORAGE){
            if (grantResults[0]==PackageManager.PERMISSION_GRANTED){
                createFolder();
            }else {
                Toast.makeText(this, "No se otorgó ningún permiso", Toast.LENGTH_SHORT).show();
            }
        }else {

            super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }

    private void createFolder() {
        File root = getExternalFilesDir(null);
        File newFile = new File(root, "myFolder");

        if (newFile.isDirectory()){
            Toast.makeText(this, "El directorio actualmente ya existe.", Toast.LENGTH_SHORT).show();
        }else {
            newFile.mkdirs();
            if (newFile.isDirectory()){
                Toast.makeText(this, "Se creó el directorio correctamente", Toast.LENGTH_SHORT).show();
                newFile = myNewFolder;
            }else {
                Toast.makeText(this, "No se pudo crear el directorio correctamente", Toast.LENGTH_SHORT).show();
            }
        }


    }

    private void viewFolderDirection() {
        File myDirection = new File(getExternalFilesDir(null), "myFolder");
        Toast.makeText(this, "Dirección: " + myDirection.toString(), Toast.LENGTH_SHORT).show();
        Uri myUri = Uri.parse(myDirection.toString());

        Intent intent = new Intent();
        startActivity(new Intent(Intent.ACTION_GET_CONTENT).setDataAndType(myUri, "*/*"));

    }
}