package com.example.tanya_app;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.net.NetworkInterface;
import java.util.HashMap;
import java.util.Map;

public class SignUpActivity extends AppCompatActivity {

    EditText emailEditText, passwordEditText, usernameEditText, confPassword;
    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        usernameEditText = findViewById(R.id.usernameSignUp);
        emailEditText = findViewById(R.id.emailSignUp);
        passwordEditText = findViewById(R.id.passwordSignUp);
        confPassword = findViewById(R.id.confPassword);
        progressDialog = new ProgressDialog(SignUpActivity.this);

        Button signupButton = findViewById(R.id.Signup);
        signupButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String username = usernameEditText.getText().toString();
                String email = emailEditText.getText().toString();
                String password = passwordEditText.getText().toString();
                String confPass = confPassword.getText().toString();

                if (password.equals(confPass) && !password.equals("")){
                    createDataToServer(username,email,password);
                    Intent intent = new Intent(SignUpActivity.this, LoginActivity.class);
                    startActivity(intent);
                } else {
                    Toast.makeText(getApplicationContext(), "Gagal! Password tidak cocok", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    public void createDataToServer(final String username, final String email, final String passwrord){
        if (chechNetworkConnection()){
            progressDialog.show();
            StringRequest stringRequest = new StringRequest(Request.Method.POST, DB_SignIn.urlRegister, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    try {
                        JSONObject jsonObject = new JSONObject(response);
                        String resp = jsonObject.getString("server_response");
                        if (resp.equals("Success")){
                            Toast.makeText(getApplicationContext(), "Registrasi Berhasil", Toast.LENGTH_SHORT).show();

                        } else {
                            Toast.makeText(getApplicationContext(), response.toString(), Toast.LENGTH_SHORT).show();
                        }
                    } catch (JSONException e) {
                        throw new RuntimeException(e);
                    }

                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {

                }
            }){
                @Override
                protected Map<String, String> getParams() throws AuthFailureError{
                    Map<String, String> params = new HashMap<>();
                    params.put("username", username);
                    params.put("email", email);
                    params.put("password", passwrord);
                    return params;
                }
            };
            volleyConnection.getInstance(SignUpActivity.this).addToRequestQue(stringRequest);

            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    progressDialog.cancel();
                }
            }, 2000);
        } else {
            Toast.makeText(getApplicationContext(), "Tidak Ada Koneksi Internet", Toast.LENGTH_SHORT).show();
        }
    }
//                if (!(username.isEmpty() || email.isEmpty() || password.isEmpty())){
//
//                    StringRequest stringRequest = new StringRequest(Request.Method.POST, DB_SignIn.urlRegister, new Response.Listener<String>() {
//                        @Override
//                        public void onResponse(String response) {
//                            Toast.makeText(getApplicationContext(), response.toString(), Toast.LENGTH_SHORT).show();
//
//                            startActivity(new Intent(getApplicationContext(), LoginActivity.class));
//                        }
//                    }, new Response.ErrorListener() {
//                        @Override
//                        public void onErrorResponse(VolleyError error) {
//                            Toast.makeText(getApplicationContext(), error.toString(), Toast.LENGTH_SHORT).show();
//                        }
//                    })
//                    {
//                        @Override
//                        protected HashMap<String, String> getParams() throws AuthFailureError{
//                            HashMap<String, String> params = new HashMap<>();
//
//                            params.put("username", username);
//                            params.put("email", email);
//                            params.put("password", password);
//
//                            return params;
//                        }
//                    };
//
//                    RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
//                    requestQueue.add(stringRequest);
//
//                } else {
//                    Toast.makeText(getApplicationContext(), "Ada data yang masih kosong", Toast.LENGTH_SHORT).show();
//                }
//            }
//        });
//    }
    public boolean chechNetworkConnection(){
        ConnectivityManager connectivityManager = (ConnectivityManager) this.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        return (networkInfo != null && networkInfo.isConnected());
    }
}
