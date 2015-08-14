package com.databsemanager.xu.databasemanager;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.AdapterView.OnItemClickListener;


public class MainActivity extends AppCompatActivity {

    private DataBaseManager manager;
    private Cursor cursor;
    private ListView listView;
    private SimpleCursorAdapter cursorAdapter;
    private EditText editText;
    private ImageButton imageButton;
    private ProgressDialog pDialog;
    private AlertDialog.Builder dialog;
    private String _id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        listView = (ListView)findViewById(R.id.listView);
        listView.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Cursor cursor = (Cursor) parent.getAdapter().getItem(position);
                _id = Integer.toString(cursor.getInt(cursor.getColumnIndex("_id")));
                loadAlertView();
            }
        });


        editText = (EditText)findViewById(R.id.editText);
        imageButton = (ImageButton)findViewById(R.id.imageButton);
        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (v.getId() == R.id.imageButton) {
                    new LoadContacts().execute();
                }
            }
        });

        loadContacts();
    }

    private void loadContacts() {
        manager = new DataBaseManager(this);
        cursor = manager.getAllContacts();

        String[] from = new String[]{manager.CN_NAME, manager.CN_PHONE};
        int[] to = new int[]{android.R.id.text1, android.R.id.text2};

        cursorAdapter = new SimpleCursorAdapter(this, android.R.layout.two_line_list_item, cursor, from, to, 0);
        listView.setAdapter(cursorAdapter);
    }

    private void loadAlertView() {

        dialog = new AlertDialog.Builder(this);
        dialog.setTitle("Options entry");
        dialog.setMessage("Select your option");
        dialog.setCancelable(false);
        dialog.setPositiveButton("Delete", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                // continue with delete
                manager.remove(_id);
                loadContacts();
            }
        });
        dialog.setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                // do nothing
            }
        });
        dialog.setNeutralButton("Modify", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                // continue with modify
                goToModify(_id);
            }
        });
        dialog.setIcon(android.R.drawable.ic_dialog_alert);
        dialog.show();
    }

    private void goToModify(String id) {
        Intent intent = new Intent(this, ModifyActivity.class);
        intent.putExtra("_id", id);
        startActivity(intent);
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadContacts();
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
        if (id == R.id.addContact) {
            Intent intent = new Intent(this, CreateActivity.class);
            startActivity(intent);
            return true;
        }
        else if (id == R.id.showContacts) {
            editText.setText("");
            loadContacts();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private class LoadContacts extends AsyncTask<Void, Void, Void> {

        String text = editText.getText().toString();

        @Override
        protected void onPreExecute() {
            pDialog = new ProgressDialog(MainActivity.this);
            pDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            pDialog.setMessage("Searching...");
            pDialog.setCancelable(false);
        }

        @Override
        protected Void doInBackground(Void... params) {
            cursor = manager.getContact(text);
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            cursorAdapter.changeCursor(cursor);
            pDialog.dismiss();
        }
    }
}
