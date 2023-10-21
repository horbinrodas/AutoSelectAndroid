package com.umg.autoselect;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.SearchView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.umg.vehiculomodel.MyAdapter;
import com.umg.vehiculomodel.SelectListener;
import com.umg.vehiculomodel.Vehiculo;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class MisVehiculosActivity extends AppCompatActivity implements SelectListener {
    List<Vehiculo> items;
    String user_id, user_name;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mis_vehiculos);
        SharedPreferences sharedPreferences = this.getSharedPreferences("sharedPrefs", MODE_PRIVATE);
        user_id = sharedPreferences.getString("user_id", "0");
        user_name = sharedPreferences.getString("user_name", "0");
        getVehiculos();
    }
    private void getVehiculos(){
        String url = "https://api.autoselect.online/usuarios/"+user_id;
        JsonObjectRequest jsonRequest = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                //Toast.makeText(MainActivity.this, response.toString(), Toast.LENGTH_SHORT).show();
                try {
                    RecyclerView recyclerView = findViewById(R.id.misVehiculosViews);
                    SearchView searchView = findViewById(R.id.misVehiculosSearchView);
                    items = new ArrayList<Vehiculo>();
                    searchView.clearFocus();
                    JSONArray jsonArray = response.getJSONArray("userVehiculos");
                    for(int i = 0; i<jsonArray.length(); i++){
                        JSONObject vehiculo = jsonArray.getJSONObject(i);
                        items.add(new Vehiculo(vehiculo.getInt("id"),
                                vehiculo.getInt("modelo"),
                                vehiculo.getString("img_link"),
                                vehiculo.getString("marca"),
                                vehiculo.getString("linea")));
                    }
                    recyclerView.setLayoutManager(new LinearLayoutManager(MisVehiculosActivity.this));
                    recyclerView.setAdapter(new MyAdapter(getApplicationContext(),items,MisVehiculosActivity.this));
                    searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
                        @Override
                        public boolean onQueryTextSubmit(String s) {
                            return false;
                        }
                        @Override
                        public boolean onQueryTextChange(String s) {
                            List<Vehiculo> filteredList = new ArrayList<Vehiculo>();
                            for(Vehiculo item: items){
                                String vehiculoFullDetails = item.getMarca() +" "+ item.getLinea() +" "+String.valueOf(item.getModelo());
                                if(vehiculoFullDetails.toLowerCase().contains(s.toLowerCase())){
                                    filteredList.add(item);
                                }
                            }
                            if(filteredList.isEmpty()){
                                Toast.makeText(MisVehiculosActivity.this, "0 resultados", Toast.LENGTH_SHORT).show();
                            }else{
                                recyclerView.setAdapter(new MyAdapter(getApplicationContext(), filteredList, MisVehiculosActivity.this));
                            }
                            return true;
                        }
                    });
                } catch (JSONException e) {
                    throw new RuntimeException(e);
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
            }
        });
        Volley.newRequestQueue(this).add(jsonRequest);
    }
    @Override
    public void onItemClicked(@NonNull Vehiculo vehiculo) {
        try {
            Intent vehiculoIntent = new Intent(this, VehiculoEditActivity.class);
            vehiculoIntent.putExtra("idVehiculo",vehiculo.getId());
            startActivity(vehiculoIntent);
        }catch (Exception E){
            Toast.makeText(this, E.toString(), Toast.LENGTH_SHORT).show();
        }
    }

    public void CrearVehiculo(View view){
        Intent crear = new Intent(this, VehiculoCreateActivity.class);
        startActivity(crear);
    }
    @Override
    public void onRestart() {
        super.onRestart();
        getVehiculos();
    }
}