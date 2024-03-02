package com.example.multy_lab_15;

import android.app.ListActivity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;

public class MainActivity extends ListActivity {
    Integer i;
    String[] from;
    int[] to;
    static ListView listView;
    private EditText edit;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

//        Toolbar myToolbar = findViewById(R.id.my_toolbar);
//        AppCompatActivity.setSupportActionBar(myToolbar);

        edit = findViewById(R.id.editText1Rec);
        SharedPreferences save = getSharedPreferences("SAVE",0);
        edit.setText(save.getString("text",""));
        from = new String[]{"Name"};
        to = new int[] {R.id.textViewListItemText};
        Button add = findViewById(R.id.add);
        SQLiteDatabase db = openOrCreateDatabase("DBName",MODE_PRIVATE,null);
        db.execSQL("CREATE TABLE IF NOT EXISTS MyTable (_id INTEGER PRIMARY KEY AUTOINCREMENT, Name VARCHAR);");
        Cursor cursor = db.rawQuery("SELECT * FROM Mytable", null);
        i = cursor.getCount()+1;
        if (cursor.getCount()>0) {
            MyCursorAdapter scAdapter = new MyCursorAdapter(MainActivity.this,R.layout.list_item,cursor,from,to);
            listView = getListView();
            listView.setAdapter(scAdapter);
        }
        db.close();
        add.setOnClickListener(view -> {
            SQLiteDatabase db1 = openOrCreateDatabase("DBName",MODE_PRIVATE,null);
            Cursor cursor2 = db1.rawQuery("SELECT * FROM Mytable", null);
            i=cursor2.getCount()+1;
            for (int k=1;k<=i;k++) {
                Cursor cursor3 = db1.rawQuery("SELECT * FROM Mytable WHERE _id=" + k + "", null);
                if (cursor3.getCount() == 0) {
                    i = k;
                    break;
                }
            }
            db1.execSQL("INSERT INTO MyTable VALUES ('"+i+"','"+edit.getText().toString()+"');");
            Cursor cursor1 = db1.rawQuery("SELECT * FROM Mytable", null);
            MyCursorAdapter scAdapter = new MyCursorAdapter(MainActivity.this,R.layout.list_item, cursor1,from,to);
            listView = getListView();
            listView.setAdapter(scAdapter);
            db1.close();
            Toast.makeText(getListView().getContext(),"a row added to the table", Toast.LENGTH_LONG).show();

        });

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.show_student) {
            AlertDialog.Builder dialog = new AlertDialog.Builder(MainActivity.this);
            dialog.setMessage("Автор - Колтыга Валерий Евгеньевич, 3-ТИД-7");
            dialog.setTitle("Об авторе");
            dialog.setNeutralButton("OK", (dialog1, which) -> dialog1.dismiss());
            dialog.setIcon(R.mipmap.ic_launcher_round);
            AlertDialog alertDialog = dialog.create();
            alertDialog.show();
        }
        if (id == R.id.settings) {
            startActivity(new Intent(this, SettingsActivity.class ));
        }
        return super.onOptionsItemSelected(item);
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    protected void onStop() {
        super.onStop();
        SharedPreferences save = getSharedPreferences("SAVE",0);
        SharedPreferences.Editor editor = save.edit();
        editor.putString("text",edit.getText().toString());
        editor.apply();
    }
}