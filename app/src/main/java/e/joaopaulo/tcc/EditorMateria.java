package e.joaopaulo.tcc;

import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class EditorMateria extends AppCompatActivity {
    private EditText materiaEditText;
    Integer id_disciplina;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editor_materia);

        materiaEditText = findViewById(R.id.materia_editText);
        setTitle("Criar nova Matéria");
        Intent intent=getIntent();
             Bundle budle = intent.getExtras();
        id_disciplina=budle.getInt("id");

    }

    public void criarNovaMateria(View v){
        //trim remove the blanck spaces
        String materia = materiaEditText.getText().toString().trim();

        // Create database helper
        DbHelper mDbHelper = new DbHelper(this);

        // Create and/or open a database to write from it
        SQLiteDatabase db = mDbHelper.getWritableDatabase();
        if(materia.length()>0) {

        //create the object no be included
        ContentValues values = new ContentValues();
        values.put(Contract.Tabelas.COLUNA_MATERIA, materia);
        values.put(Contract.Tabelas.COLUNA_ID_DISCIPLINA, id_disciplina);

        long newRowId = db.insert(Contract.Tabelas.TABLE_NAME4, null, values);


        if (newRowId==-1){
            Toast.makeText(this, "Erro ao criar Matéria", Toast.LENGTH_SHORT).show();
        }else{
            Toast.makeText(this, "Matéria criada ", Toast.LENGTH_SHORT).show();

        }
        Intent devolve = new Intent();
        devolve.putExtra("nomeMateria", materia);
        setResult(0, devolve);
        mDbHelper.close();
        db.close();
        this.finish();
        }else{
            Toast.makeText(this, "A matéria não pode ficar vazia", Toast.LENGTH_SHORT).show();

        }
    }



}
