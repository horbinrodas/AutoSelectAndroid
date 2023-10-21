package com.umg.autoselect;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class RegistroActivity extends AppCompatActivity {
    EditText nombre, apellido, email, telefono, pwd, pwdRepeat;
    CheckBox termsCheck;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registro);
        nombre = findViewById(R.id.nombreRegister);
        apellido = findViewById(R.id.apellidoRegister);
        email = findViewById(R.id.emailRegister);
        telefono = findViewById(R.id.telefonoRegister);
        pwd = findViewById(R.id.pwdRegister);
        pwdRepeat = findViewById(R.id.pwdRepeat);
        termsCheck = findViewById(R.id.termsCheck);
    }
    public void Registro(View view){
        sendRegistro();
    }

    public void sendRegistro(){
        String url = "https://api.autoselect.online/usuarios";
        Date fecha = Calendar.getInstance().getTime();
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        String fechaFormateada = df.format(fecha);

        Map<String, String> params = new HashMap();
        params.put("action", "REGISTER");
        params.put("nombre", nombre.getText().toString());
        params.put("apellido", apellido.getText().toString());
        params.put("telefono", telefono.getText().toString());
        params.put("email", email.getText().toString());
        params.put("pwd", pwd.getText().toString());
        params.put("fecha", fechaFormateada);

        JSONObject parameters = new JSONObject(params);

        JsonObjectRequest jsonRequest = new JsonObjectRequest(Request.Method.POST, url, parameters, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    if (response.getString("id").equals("email taken")){
                        Toast.makeText(RegistroActivity.this, "El Correo Ya Esta Registrado", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    Toast.makeText(RegistroActivity.this, "Usuario Registrado", Toast.LENGTH_SHORT).show();
                    Intent login = new Intent(RegistroActivity.this, MainActivity.class);
                    startActivity(login);
                } catch (JSONException e) {
                    throw new RuntimeException(e);
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
                Toast.makeText(RegistroActivity.this, "Ocurrio Un Error", Toast.LENGTH_SHORT).show();
            }
        });
        Volley.newRequestQueue(this).add(jsonRequest);
    }
}