package com.databsemanager.xu.databasemanager;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;


public class ModifyActivity extends Activity {

    private DataBaseManager manager;

    private EditText editTextName1;
    private EditText editTextPhone1;
    private Button buttonModify1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_modify);

        final String id = getIntent().getStringExtra("_id");

        editTextName1 = (EditText) findViewById(R.id.editTextName1);
        editTextPhone1 = (EditText) findViewById(R.id.editTextPhone1);
        buttonModify1 = (Button) findViewById(R.id.buttonModify1);

        buttonModify1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                manager = new DataBaseManager(getApplicationContext());
                if ((editTextName1.getText().toString() != "") && (editTextPhone1.getText().toString() != "")) {
                    manager.modify(id, editTextName1.getText().toString(), editTextPhone1.getText().toString());
                    finish();
                } else {
                    Toast.makeText(getApplicationContext(), "Exists an empty field", Toast.LENGTH_SHORT);
                }
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu2_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.seeContact) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
