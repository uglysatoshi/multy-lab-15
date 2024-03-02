package com.example.multy_lab_15;

import static android.content.Context.MODE_PRIVATE;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.cursoradapter.widget.SimpleCursorAdapter;

public class MyCursorAdapter extends SimpleCursorAdapter {
    private final int layout_;
    String[] from;
    int[] to;

    ListView listView;
    EditText edit2;
    public MyCursorAdapter(Context context, int layout, Cursor c, String[] from, int[] to) {
        super(context, layout, c, from, to);
        layout_ = layout;
    }
    @Override
    public void bindView(View view, Context _context, Cursor cursor) {
        @SuppressLint("Range") String data = cursor.getString(cursor.getColumnIndex("Name"));
        @SuppressLint("Range") int id = cursor.getInt(cursor.getColumnIndex("_id"));
        TextView text = view.findViewById(R.id.textViewListItemText);
        text.setText(data);
        Button butdel = view.findViewById(R.id.buttonDelete);
        Button butedit = view.findViewById(R.id.buttonEdit);
        listView = MainActivity.listView;
        butdel.setOnClickListener(v -> {
            SQLiteDatabase db = _context.openOrCreateDatabase("DBName",MODE_PRIVATE,null);
            db.execSQL("DELETE FROM MyTable WHERE _id="+id);
            Cursor cursor12 = db.rawQuery("SELECT * FROM Mytable", null);
            from = new String[]{"Name"};
            to =new int[] {R.id.textViewListItemText};
            MyCursorAdapter scAdapter = new MyCursorAdapter(_context,R.layout.list_item, cursor12,from,to);
            listView.setAdapter(scAdapter);
            db.close();
            Toast.makeText(_context,"row deleted from the db id="+id,Toast.LENGTH_LONG).show();
        });
        butedit.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("MissingInflatedId")
            @Override
            public void onClick(View view) {
                AlertDialog.Builder dialog = new AlertDialog.Builder(_context);
                dialog.setMessage("Enter new value:");
                dialog.setTitle("Changing the item");
                LayoutInflater inflater = new LayoutInflater(_context) {
                    @Override
                    public LayoutInflater cloneInContext(Context context) {
                        return null;
                    }
                };
                View dialogview = inflater.inflate(R.layout.dialog,null);
                dialog.setView(dialogview);
                edit2 = dialogview.findViewById(R.id.editTextCnahgeDBRecord);
                edit2.setText(text.getText().toString());
                dialog.setNeutralButton("OK", (dialog1, i) -> {
                    SQLiteDatabase db = _context.openOrCreateDatabase("DBName",MODE_PRIVATE,null);
                    db.execSQL("UPDATE MyTable SET Name='"+edit2.getText().toString()+"' WHERE _id="+id);
                    Cursor cursor1 = db.rawQuery("SELECT * FROM Mytable", null);
                    from = new String[]{"Name"};
                    to =new int[] {R.id.textViewListItemText};
                    MyCursorAdapter scAdapter = new MyCursorAdapter(_context,R.layout.list_item, cursor1,from,to);
                    listView.setAdapter(scAdapter);
                    db.close();
                    Toast.makeText(_context,"row edited from the db row id="+id,Toast.LENGTH_LONG).show();
                    dialog1.dismiss();
                });
                dialog.setIcon(R.mipmap.ic_launcher_round);
                AlertDialog alertDialog = dialog.create();
                alertDialog.show();
            }
        });

    }
    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        return inflater.inflate(layout_,parent,false);
    }
}
