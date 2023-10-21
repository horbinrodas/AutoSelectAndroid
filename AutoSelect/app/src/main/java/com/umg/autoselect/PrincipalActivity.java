package com.umg.autoselect;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class PrincipalActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_principal);
    }
    //Metodo Boton Busqueda
    public void Busqueda(View view){
        Intent busqueda = new Intent(this, BusquedaActivity.class);
        startActivity(busqueda);
    }
    public void Cotizar(View view){
        Intent cotizar = new Intent(this, CotizarActivity.class);
        startActivity(cotizar);
    }
    public void MisVehiculos(View view){
        Intent misVehiculos = new Intent(this, MisVehiculosActivity.class);
        startActivity(misVehiculos);
    }
    public void Informacion(View view){
        Intent informacion = new Intent(this, InformacionActivity.class);
        startActivity(informacion);
    }
}