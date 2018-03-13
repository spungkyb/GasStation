package id.co.integravity.gasstation.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import id.co.integravity.gasstation.R;
import id.co.integravity.gasstation.util.SharedPrefManager;

public class MainActivity extends AppCompatActivity {

    TextView tvEmailProfil, tvNamaProfil;
    SharedPrefManager sharedPrefManager;
    SharedPreferences loginPref;
    View viewMenuAll;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getSupportActionBar().setTitle("Home");
        tvNamaProfil = (TextView)findViewById(R.id.tv_nama_profil);
        tvEmailProfil = (TextView)findViewById(R.id.tv_email_profil);

        sharedPrefManager = new SharedPrefManager(this);

        tvNamaProfil.setText(sharedPrefManager.getSPNama());

        tvEmailProfil.setText(sharedPrefManager.getSPEmail());
        loginPref = getSharedPreferences(LoginActivity.LOGIN_PREF, MODE_PRIVATE);

        viewMenuAll = (View)findViewById(R.id.menu_all);

        viewMenuAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent menuAllIntent = new Intent(MainActivity.this,AllMarkerActivity.class);
                startActivity(menuAllIntent);
            }
        });


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_logout) {
            sharedPrefManager.saveSPBoolean(SharedPrefManager.SP_SUDAH_LOGIN, false);
            sharedPrefManager.deleteSP();
            startActivity(new Intent(MainActivity.this, LoginActivity.class)
                    .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK));
            finish();
        }

        return super.onOptionsItemSelected(item);
    }
}
