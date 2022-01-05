package e.joaopaulo.tcc;

import android.content.ContentValues;
import android.content.DialogInterface;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.AppCompatImageView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import java.util.Random;

public class MostrarCardsAleatoriamente extends AppCompatActivity {
    AlertDialog.Builder builder;
    private AlertDialog excluirCardAlert;
    String id=""+geraNumeroAleatorio();
    Button btSei;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mostrar_cards_aleatoriamente);
        btSei=(Button) findViewById(R.id.btsei);
        btSei.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String id=""+geraNumeroAleatorio();
                onResume();


            }
        });

        builder = new AlertDialog.Builder(this);
        //define o titulo
        builder.setTitle("Card");
        builder.setMessage("Tem certeza que deseja excluir esse card?");
        builder.setPositiveButton("Sim", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface arg0, int arg1) {
                deletarCard();
                Lista listaCard = new Lista();
                final Cursor cursorNovo = listaCard.listarInicial(MostrarCardsAleatoriamente.this, Contract.Tabelas.TABLE_NAME3);
                //CardsCursorAdapter(context,cursorNovo);
            }
        });
        builder.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface arg0, int arg1) {
            }
        });



    }




    @Override
    public void onResume() {
        super.onResume();
        listarCard();
    }

    @Override
    public void onStart() {
        super.onStart();
        listarCard();
    }

    public void listarCard() {

        Lista lista = new Lista();
        final Cursor cursor = lista.listaCompleta(MostrarCardsAleatoriamente.this, Contract.Tabelas.TABLE_NAME3, id);

        final ListView lvItems = (ListView) findViewById(R.id.lista_card);

        final CardAleatorioCursorAdapter cardCursorAdapter = new CardAleatorioCursorAdapter(MostrarCardsAleatoriamente.this, cursor);

        lvItems.setAdapter(cardCursorAdapter);


    }
    public void deletarCard() {
        DbHelper mDbHelper = new DbHelper(this);
        SQLiteDatabase db = mDbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        String querySql2 = "SELECT disciplina FROM cards WHERE " + Contract.Tabelas._ID + "=" + 1 ;
        String disciplina="";
        Cursor cursor = db.rawQuery(querySql2, null);
        cursor.moveToFirst();
        if (cursor.getCount() > 0) {
            disciplina = cursor.getString(cursor.getColumnIndex(Contract.Tabelas.COLUNA_DISCIPLINA));
            Log.v("chatuba1",disciplina+"");

            String querySql = "SELECT qtd_cards FROM disciplinas WHERE " + Contract.Tabelas.COLUNA_DISCIPLINA + "=" +"'"+ disciplina+ "'";
            cursor = db.rawQuery(querySql, null);
            cursor.moveToFirst();
        }
        if (cursor.getCount() > 0) {
            int qtdcards = cursor.getInt(cursor.getColumnIndex(Contract.Tabelas.COLUNA_QTD_CARDS));

            values.put(Contract.Tabelas.COLUNA_QTD_REVISOES, qtdcards - 1);

            String whereClause = Contract.Tabelas.COLUNA_DISCIPLINA + "=" + "'"+disciplina+"'"  ;
            long newRowId = db.update(Contract.Tabelas.TABLE_NAME2, values, whereClause, null);
        }
        db = mDbHelper.getWritableDatabase();
        // Create and/or open a database to write from it

        String whereClause2 =  Contract.Tabelas._ID + "="+ id;

        long newRowId2 = db.delete(Contract.Tabelas.TABLE_NAME3,whereClause2,null);
        if (newRowId2==-1){
            Toast.makeText(this, "Erro ao deletar card", Toast.LENGTH_SHORT).show();
        }else{
            Toast.makeText(this, "card deletado", Toast.LENGTH_SHORT).show();

        }
        mDbHelper.close();

        db.close();

        this.finish();


    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_materia, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.botao_excuir:


                excluirCardAlert = builder.create();
                //Exibe
                excluirCardAlert.show();

                return true;

            case android.R.id.home:
                NavUtils.navigateUpFromSameTask(this);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
    int pegaMaiorId(){
        DbHelper mDbHelper = new DbHelper(this);
        SQLiteDatabase db = mDbHelper.getWritableDatabase();
        String querySql3 = "SELECT * FROM "+ Contract.Tabelas.TABLE_NAME3+" WHERE _id = (SELECT MAX(_id)  FROM "+Contract.Tabelas.TABLE_NAME3+");";

        Cursor cursor = db.rawQuery(querySql3, null);
        cursor.moveToFirst();
        int id=0;
        id = cursor.getInt(cursor.getColumnIndex(Contract.Tabelas._ID));
        return id;
    }

    int geraNumeroAleatorio(){

        Random gerador=new Random();
        int x =gerador.nextInt(32);
        return x;
    }


}
