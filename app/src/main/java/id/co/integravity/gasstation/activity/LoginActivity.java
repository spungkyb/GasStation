package id.co.integravity.gasstation.activity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import id.co.integravity.gasstation.R;
import id.co.integravity.gasstation.util.SharedPrefManager;
import id.co.integravity.gasstation.util.api.BaseApiService;
import id.co.integravity.gasstation.util.api.UtilsApi;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity {

    EditText edtEmail, edtPassword;
    Button btnRegister, btnLogin;
    ProgressDialog loading;


    Context mContext;
    BaseApiService mApiService;

    SharedPrefManager sharedPrefManager;
    public static String LOGIN_PREF = "login.pref.file";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        getSupportActionBar().setTitle("Login");

        edtEmail = (EditText)findViewById(R.id.edt_email_msk);
        edtPassword = (EditText)findViewById(R.id.edt_password_msk);

        btnLogin = (Button)findViewById(R.id.btn_masuk);
        btnRegister = (Button)findViewById(R.id.btn_daftar_msk);

        mContext = this;
        mApiService = UtilsApi.getAPIService(); // meng-init yang ada di package apihelper
        sharedPrefManager = new SharedPrefManager(this);

        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(mContext, RegisterActivity.class));
            }
        });

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loading = ProgressDialog.show(mContext, null, "Harap Tunggu...", true, false);
                requestLogin();
            }
        });

        if (sharedPrefManager.getSPSudahLogin()){
            startActivity(new Intent(LoginActivity.this, MainActivity.class)
                    .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK));
            finish();
        }


    }

    private void requestLogin(){
        mApiService.loginRequest(edtEmail.getText().toString(), edtPassword.getText().toString())
                .enqueue(new Callback<ResponseBody>() {
                    @Override
                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                        if (response.isSuccessful()){
                            loading.dismiss();
                            try {
                                JSONObject jsonRESULTS = new JSONObject(response.body().string());
                                if (jsonRESULTS.getString("success").equals("true")){
                                    // Jika login berhasil maka data nama yang ada di response API
                                    // akan diparsing ke activity selanjutnya.
                                    Toast.makeText(mContext, "BERHASIL LOGIN", Toast.LENGTH_SHORT).show();
                                    String nama = jsonRESULTS.getString("nama");
                                    sharedPrefManager.saveSPString(SharedPrefManager.SP_NAMA, nama);
                                    String email = jsonRESULTS.getString("email");
                                    sharedPrefManager.saveSPString(SharedPrefManager.SP_EMAIL, email);
                                    // Shared Pref ini berfungsi untuk menjadi trigger session login
                                    sharedPrefManager.saveSPBoolean(SharedPrefManager.SP_SUDAH_LOGIN, true);
                                    SharedPreferences sp = getSharedPreferences(LOGIN_PREF, MODE_PRIVATE);

                                    SharedPreferences.Editor spEdit = sp.edit();

                                    spEdit.putString("nama",nama);
                                    spEdit.putString("token", "email");
                                    spEdit.apply();
                                    startActivity(new Intent(mContext, MainActivity.class)
                                            .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK));
                                    finish();
                                } else{
                                    // Jika login gagal
                                    String error_message = jsonRESULTS.getString("message");
                                    Toast.makeText(mContext, error_message, Toast.LENGTH_SHORT).show();
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        } else {
                            loading.dismiss();
                        }
                    }

                    @Override
                    public void onFailure(Call<ResponseBody> call, Throwable t) {
                        Log.e("debug", "onFailure: ERROR > " + t.toString());
                        String error_message = "LOGIN ERROR";
                        Toast.makeText(mContext, error_message, Toast.LENGTH_SHORT).show();
                        loading.dismiss();
                    }
                });
    }


}
