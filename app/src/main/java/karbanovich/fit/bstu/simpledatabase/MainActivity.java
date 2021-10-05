package karbanovich.fit.bstu.simpledatabase;

import androidx.appcompat.app.AppCompatActivity;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    //View
    EditText id;
    EditText f;
    EditText t;
    Button insert;
    Button select;
    Button selectRaw;
    Button update;
    Button delete;

    //Data
    DBHelper dbHelper;
    SQLiteDatabase db;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        binding();
        setListeners();
    }

    @Override
    public void onDestroy(){
        super.onDestroy();
        db.close();
    }

    private void binding() {
        id = findViewById(R.id.id);
        f = findViewById(R.id.f);
        t = findViewById(R.id.t);
        insert = findViewById(R.id.btnInsert);
        select = findViewById(R.id.btnSelect);
        selectRaw = findViewById(R.id.btnSelectRaw);
        update = findViewById(R.id.btnUpdate);
        delete = findViewById(R.id.btnDelete);

        dbHelper = new DBHelper(getApplicationContext());
        db = dbHelper.getReadableDatabase();
    }

    private void setListeners() {
        insert.setOnClickListener(view -> {
            String id = this.id.getText().toString();
            String f = this.f.getText().toString();
            String t = this.t.getText().toString();

            if(!validation(id, f, t)) return;

            if(id.isEmpty())
                dbHelper.insert(db, Double.parseDouble(f), t);
            else
                dbHelper.insert(db, Integer.parseInt(id), Double.parseDouble(f), t);

            Toast.makeText(this, "Данные успешно добавлены", Toast.LENGTH_SHORT).show();
            clearFields(true);
        });

        select.setOnClickListener(view -> {
            String id = this.id.getText().toString();

            if(!checkInt(id)) {
                Toast.makeText(this, "Проверьте введенный ID", Toast.LENGTH_SHORT).show();
                clearFields(false);
            }
            else
                outputByKey(dbHelper.select(db, id));
        });

        selectRaw.setOnClickListener(view -> {
            String id = this.id.getText().toString();

            if(!checkInt(id)) {
                Toast.makeText(this, "Проверьте введенный ID", Toast.LENGTH_SHORT).show();
                clearFields(false);
            }
            else
                outputByKey(dbHelper.selectRaw(db, Integer.parseInt(id)));
        });

        update.setOnClickListener(view -> {
            String id = this.id.getText().toString();
            String f = this.f.getText().toString();
            String t = this.t.getText().toString();

            if(!validation(id, f, t)) return;

            if(id.isEmpty()) {
                Toast.makeText(this, "Введите ID", Toast.LENGTH_SHORT).show();
                clearFields(false);
                return;
            }

            if(dbHelper.update(db, id, Double.parseDouble(f), t) == 0)
                Toast.makeText(this, "Такого ключа не существует", Toast.LENGTH_SHORT).show();
            else
                Toast.makeText(this, "Данные успешно обновлены", Toast.LENGTH_SHORT).show();
        });

        delete.setOnClickListener(view -> {
            String id = this.id.getText().toString();
            String f = this.f.getText().toString();
            String t = this.t.getText().toString();

            if(id.isEmpty()) {
                Toast.makeText(this, "Введите ID", Toast.LENGTH_SHORT).show();
                clearFields(false);
                return;
            }

            if(dbHelper.delete(db, id) == 0)
                Toast.makeText(this, "Такого ключа не существует", Toast.LENGTH_SHORT).show();
            else {
                Toast.makeText(this, "Данные успешно удалены", Toast.LENGTH_SHORT).show();
                clearFields(true);
            }
        });
    }

    private void outputByKey(Cursor cursor) {
        if(cursor.moveToFirst()) {
            f.setText(cursor.getString(cursor.getColumnIndexOrThrow("F")));
            t.setText(cursor.getString(cursor.getColumnIndexOrThrow("T")));
        } else {
            Toast.makeText(this, "Такого ключа не существует", Toast.LENGTH_SHORT).show();
            clearFields(false);
        }
        cursor.close();
    }

    private boolean validation(String id, String f, String t) {
        if(!checkInt(id) && !id.isEmpty() || !checkDouble(f) || t.isEmpty()) {
            Toast.makeText(this, "Проверьте введенные данные", Toast.LENGTH_SHORT).show();
            return false;
        } else
            return true;
    }

    private void clearFields(boolean clearId) {
        if(clearId)
            this.id.setText("");
        this.f.setText("");
        this.t.setText("");
    }

    private boolean checkInt(String str) {
        try {
            Integer.parseInt(str);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    private boolean checkDouble(String str) {
        try {
            Double.parseDouble(str);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }
}