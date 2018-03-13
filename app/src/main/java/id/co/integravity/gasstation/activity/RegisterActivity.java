package id.co.integravity.gasstation.activity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import id.co.integravity.gasstation.R;
import id.co.integravity.gasstation.util.api.BaseApiService;
import id.co.integravity.gasstation.util.api.UtilsApi;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RegisterActivity extends AppCompatActivity {

    EditText edtEmail, edtNama, edtPassword;
    Button btnRegister, btnLogin;

    ProgressDialog loading;

    Context mContext;
    BaseApiService mApiService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        getSupportActionBar().setTitle("Register");
        edtEmail = (EditText)findViewById(R.id.edt_email_daftar);
        edtNama = (EditText)findViewById(R.id.edt_nama_daftar);
        edtPassword = (EditText)findViewById(R.id.edt_password_daftar);
        btnRegister =(Button)findViewById(R.id.btn_daftar);
        btnLogin = (Button)findViewById(R.id.btn_masuk_dftr);

        mContext = this;
        mApiService = UtilsApi.getAPIService();

        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loading = ProgressDialog.show(mContext, null, "Harap Tunggu...", true, false);
                requestRegister();
                startActivity(new Intent(mContext, LoginActivity.class));
            }
        });
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentLogin = new Intent(RegisterActivity.this,LoginActivity.class);
                startActivity(new Intent(intentLogin));
            }
        });
    }

    private void requestRegister() {
        mApiService.registerRequest(edtEmail.getText().toString().trim(),edtNama.getText().toString().trim(),
                edtPassword.getText().toString().trim())
                .enqueue(new Callback<ResponseBody>() {
                    @Override
                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                        if (response.isSuccessful()){
                            loading.dismiss();
                            try {
                                JSONObject jsonRESULTS = new JSONObject(response.body().string());
                                if (jsonRESULTS.getString("success").equals("true")){
                                    Toast.makeText(mContext, "Registrasi Berhasil", Toast.LENGTH_LONG).show();
                                    startActivity(new Intent(mContext, LoginActivity.class));
                                } else {
                                    Toast.makeText(mContext, "Registrasi Error", Toast.LENGTH_LONG).show();
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        } else {
                            Toast.makeText(mContext, "Error, Email sudah digunakan", Toast.LENGTH_LONG).show();
                            loading.dismiss();
                        }
                    }

                    @Override
                    public void onFailure(Call<ResponseBody> call, Throwable t) {
                        Toast.makeText(mContext, "Server Bermasalah", Toast.LENGTH_LONG).show();
                        loading.dismiss();
                    }
                });
    }


}
