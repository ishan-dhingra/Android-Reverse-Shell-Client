package com.anythingintellect.androidreverseshell;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Html;
import android.text.TextUtils;
import android.text.method.LinkMovementMethod;
import android.text.util.Linkify;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.anythingintellect.androidreverseshell.utils.RSPreferences;

public class MainActivity extends AppCompatActivity {

    RSPreferences rsPreferences;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        TextView txtIntro = (TextView) findViewById(R.id.txtAppIntro);
        txtIntro.setText(Html.fromHtml(getString(R.string.app_intro)));
        Linkify.addLinks(txtIntro, Linkify.ALL);
        txtIntro.setMovementMethod(LinkMovementMethod.getInstance());
        rsPreferences = RSPreferences.getInstance(this);
        Intent reverseService = new Intent(this, ReverseTcpService.class);
        startService(reverseService);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.actSetting: {
                showConfigureDialog();
                break;
            }
        }
        return super.onOptionsItemSelected(item);
    }

    private void showConfigureDialog() {
        final View configureView = LayoutInflater.from(this).inflate(R.layout.dialog_configure, null);
        final EditText txtHost = (EditText) configureView.findViewById(R.id.txtHostIP);
        final EditText txtPort = (EditText) configureView.findViewById(R.id.txtPort);
        txtHost.setText(rsPreferences.getHost());
        txtPort.setText(String.valueOf(rsPreferences.getPort()));
        AlertDialog.Builder configDialog = new AlertDialog.Builder(this);
        configDialog.setView(configureView);
        configDialog.setPositiveButton("Update", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                String host = txtHost.getText().toString();
                String port = txtPort.getText().toString();
                if (isValid(host, port)) {
                    saveInfo(host, port);
                }
            }
        });
        configDialog.setTitle("Configure");
        configDialog.setNegativeButton("Cancel", null);
        configDialog.setCancelable(false);
        configDialog.show();
    }

    private void saveInfo(String host, String port) {
        rsPreferences.setHost(host);
        rsPreferences.setPort(Integer.parseInt(port));
    }

    private boolean isValid(String host, String port) {
        return !TextUtils.isEmpty(host)
                && !TextUtils.isEmpty(port)
                && TextUtils.isDigitsOnly(port);
    }
}
