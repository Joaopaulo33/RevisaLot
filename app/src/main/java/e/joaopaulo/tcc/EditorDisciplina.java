package e.joaopaulo.tcc;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.Toast;

public class EditorDisciplina extends AppCompatActivity {
    //views de entrada de dados
    private EditText disciplinaEditText;
    private String idDiciplina;
    private int status;
    String disciplina;
    final static int ADD = 0;
    final static int EDIT = 1;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editor_disciplina);
        disciplinaEditText = findViewById(R.id.disciplina_editText);

        setTitle("Criar nova disciplina");
        DbHelper mDbHelper = new DbHelper(this);
        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();

        //if (bundle != null){

          //  this.setTitle("Edite diciplina");
            //idDiciplina = bundle.get("id").toString();
            //CarregarDados(idDiciplina);

            //status = EDIT;

      //  }else{

      //      status = ADD;

       // }

    }

    public void criarNovaDisciplina(){
        //trim remove the blanck spaces
         disciplina = disciplinaEditText.getText().toString().trim();

        // Create database helper
        DbHelper mDbHelper = new DbHelper(this);

        // Create and/or open a database to write from it
        SQLiteDatabase db = mDbHelper.getWritableDatabase();
        if(disciplina.length()>0) {

        //create the object no be included
        ContentValues values = new ContentValues();
      //  ContentValues values2 = new ContentValues();

        values.put(Contract.Tabelas.COLUNA_DISCIPLINA, disciplina);
      //  values2.put(Contract.Tabelas.COLUNA_DICIPLINA, diciplina);
        values.put(Contract.Tabelas.COLUNA_QTD_CARDS, 0);
        values.put(Contract.Tabelas.COLUNA_QTD_REVISOES, 0);



        long newRowId = db.insert(Contract.Tabelas.TABLE_NAME2, null, values);
      //  long newRowId2 = db2.insert(Contract.Tabelas.TABLE_NAME3, null, values2);

        if (newRowId==-1){
            Toast.makeText(this, "Erro ao criar disciplina", Toast.LENGTH_SHORT).show();
        }else{
            Toast.makeText(this, "Disciplina criada ", Toast.LENGTH_SHORT).show();

        }
            Intent devolve = new Intent();
            devolve.putExtra("nomeDisciplina", disciplina);
            setResult(RESULT_OK, devolve);
            this.finish();
        }else{
            Toast.makeText(this, "A disciplina não pode ficar vazia", Toast.LENGTH_SHORT).show();

        }

    }
    private void updateDisciplina(String id){
        DbHelper mDbHelper = new DbHelper(this);

        //trim remove the blanck spaces
        String nome = disciplinaEditText.getText().toString().trim();

        // Create and/or open a database to write from it
        SQLiteDatabase db = mDbHelper.getWritableDatabase();

        //create the object no be included
        ContentValues values = new ContentValues();
        values.put(Contract.Tabelas.COLUNA_DISCIPLINA, nome);
        String whereClause =  Contract.Tabelas._ID + "="+ id;

        long newRowId = db.update(Contract.Tabelas.TABLE_NAME2,values,whereClause,null);


        if (newRowId==-1){
            Toast.makeText(this, "Erro ao atualizar diciplina", Toast.LENGTH_SHORT).show();
        }else{
            Toast.makeText(this, "Diciplina atulizada", Toast.LENGTH_SHORT).show();
        }

    }
    private void CarregarDados(String id){

        String nome;

        // Create and/or open a database to read from it
        DbHelper mDbHelper = new DbHelper(this);
        SQLiteDatabase db = mDbHelper.getReadableDatabase();

        String whereClause =  Contract.Tabelas._ID + "="+ id;

        Cursor cursor = db.query(Contract.Tabelas.TABLE_NAME2,null,whereClause,null,null,null,null);

        if(cursor != null){

            cursor.moveToFirst();
            nome = cursor.getString(cursor.getColumnIndex(Contract.Tabelas.COLUNA_DISCIPLINA));
            disciplinaEditText.setText(nome);
        }else{
            Toast.makeText(this, "Erro ao abrir disciplina", Toast.LENGTH_SHORT).show();
        }
    }

    private void deletarDisciplina(String id){
        DbHelper mDbHelper = new DbHelper(this);

        // Create and/or open a database to write from it
        SQLiteDatabase db = mDbHelper.getWritableDatabase();

        String whereClause =  Contract.Tabelas._ID + "="+ id;

        long newRowId = db.delete(Contract.Tabelas.TABLE_NAME2,whereClause,null);

        if (newRowId==-1){
            Toast.makeText(this, "Erro ao deletar disciplina", Toast.LENGTH_SHORT).show();
        }else{
            Toast.makeText(this, "disciplina deletada", Toast.LENGTH_SHORT).show();

        }

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu options from the res/menu/menu_editor.xml file.
        // This adds menu items to the app bar.
        getMenuInflater().inflate(R.menu.menu_editor, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // User clicked on a menu option in the app bar overflow menu
        switch (item.getItemId()) {
            // Respond to a click on the "Save" menu option
            case R.id.botao_salvar:
                //Save on database
              //  if(status == ADD) {
                    criarNovaDisciplina();
             //   }else
                //    updateDisciplina(idDiciplina);

              //  finish();
                return true;
            // Respond to a click on the "Delete" menu option
          //  case R.id.botao_deletar :
                // Do nothing for now
            //    deletarDisciplina(idDiciplina);
              //  return true;
            // Respond to a click on the "Up" arrow button in the app bar
            case android.R.id.home:
                // Navigate back to parent activity (CatalogActivity)
                NavUtils.navigateUpFromSameTask(this);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
