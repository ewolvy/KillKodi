package com.mooo.ewolvy.killkodi;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_settings:
                startActivity(new Intent(MainActivity.this,
                        SettingsActivity.class));
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void KillKodi(View v){
        SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(this);
        final String address = sharedPrefs.getString("kodi_address", "none");
        String stPort = sharedPrefs.getString("kodi_port", "0");
        if (stPort.equals("")){
            stPort = "0";
        }
        final int port = Integer.valueOf(stPort);
        if ((address.isEmpty()) || (port == 0)){
            Toast.makeText(this, getString(R.string.set_settings),
                    Toast.LENGTH_LONG).show();
        }else {
            new AlertDialog.Builder(this)
                    .setTitle(getString(R.string.confirm_title))
                    .setMessage(getString(R.string.confirm_message))
                    .setIcon(android.R.drawable.ic_dialog_alert)
                    .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int whichButton) {
                            new KillKodiAsyncTask(MainActivity.this).execute(new KKState(address, port));
                        }
                    })
                    .setNegativeButton(android.R.string.no, null).show();
        }
    }
}
