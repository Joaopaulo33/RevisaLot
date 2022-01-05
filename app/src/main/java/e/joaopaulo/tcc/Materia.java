package e.joaopaulo.tcc;

import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

public class Materia extends AppCompatActivity {

    String nome_disciplina;
    Integer id_disciplina;
    AlertDialog.Builder builder ;
    private AlertDialog deletarDisciplina;
    Button btMostrarCards;
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_materia);

        Intent intent=getIntent();
        Bundle budle = intent.getExtras();
        nome_disciplina=budle.getString("nome_disciplina");
        id_disciplina=budle.getInt("id");

        setTitle("Materias de "+nome_disciplina);

        listarMaterias();

        btMostrarCards = (Button) findViewById(R.id.botaoCardsDeUmaDisciplina);
        btMostrarCards.setText("Mostrar todos os cards de "+nome_disciplina);
        btMostrarCards.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //   Lista listasDisciplina = new Lista();
                //   final Cursor cursor = listasDisciplina.listarInicial(MateriaECards.this, Contract.Tabelas.TABLE_NAME4);
                Intent intent = new Intent(Materia.this, CardsDeUmaDisciplina.class);
                Bundle bundle=new Bundle();
                intent.putExtra("nome_disciplina",nome_disciplina);
                intent.putExtra("id_disciplina",id_disciplina);

                intent.putExtras(bundle);
                startActivity(intent);

            }
        });
        //SEGUNDOOOOOO
        builder = new AlertDialog.Builder(this);
        //define o titulo
        builder.setTitle("Revisão");
        builder.setMessage("Tem certeza que deseja excluir a disciplina "+nome_disciplina+"?");
        builder.setPositiveButton("Sim", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface arg0, int arg1) {
            deletarDisciplina(id_disciplina);
            finish();
            }
        });
        builder.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface arg0, int arg1) {
            }
        });
    }

    @Override
    public void onStart(){
        super.onStart();
        listarMaterias();
    }
    //ESSE ON START E ON RESUME SERVEM PARA CONSEGUIR ATUALIZAR A LISTA, PQ QUANDO VC CRIA UMA NOVA
    //REVISAO AI NA HORA QUE VOLTA ELE PRECISA ATUALIZAR

    @Override
    public void onResume(){
        super.onResume();
        listarMaterias();
    }

    public void novaMateria(View v){
        Intent intent = new Intent(Materia.this, EditorMateria.class);
        Bundle bundle=new Bundle();
        intent.putExtra("id",id_disciplina);
        intent.putExtras(bundle);
        startActivity(intent);
    }
    public void listarMaterias(){
        Lista listaMateria = new Lista();
        final Cursor cursor = listaMateria.listaDeMaterias(Materia.this, Contract.Tabelas.TABLE_NAME4,id_disciplina); // AQUI EU FIZ PARA SEPARAR O BD DA LISTA, ENTAO EU CRIO
        //UM OBJETO E CHAMO ELE PASSANDO O CONTEXTO. ELE ME RETORNA O CURSOR COM TUDO QUE ESTA NO BD
        //Find ListView to populate
        final ListView lvItems = (ListView)findViewById(R.id.lista_materias);

        final MateriaCursorAdapter materiaCursorAdapter = new MateriaCursorAdapter(Materia.this, cursor);

        lvItems.setAdapter(materiaCursorAdapter);
        lvItems.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(Materia.this, MateriaECards.class);
                Bundle bundle=new Bundle();
                intent.putExtra("nome_materia",cursor.getString(1));
                intent.putExtra("nome_disciplina",nome_disciplina);
                intent.putExtra("id",cursor.getInt(0));
                intent.putExtras(bundle);
                startActivity(intent);

            }
        });


    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu options from the res/menu/menu_editor.xml file.
        // This adds menu items to the app bar.
        getMenuInflater().inflate(R.menu.menu_materia, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // User clicked on a menu option in the app bar overflow menu
        switch (item.getItemId()) {
            // Respond to a click on the "Save" menu option
            case R.id.botao_excuir:
                //Save on database
                //  if(status == ADD) {
                deletarDisciplina = builder.create();
                //Exibe
                deletarDisciplina.show();
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
    private void deletarDisciplina(Integer id){
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

}
