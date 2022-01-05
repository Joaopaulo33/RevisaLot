package e.joaopaulo.tcc;

import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.AppCompatImageView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class RevisaoCompleta extends AppCompatActivity {
    String id;
    DbHelper mDbHelper = new DbHelper(this);
     AlertDialog.Builder builder ;
    private AlertDialog revisaoLonge;
    private AlertDialog revisaoAtrasada;
    private AlertDialog excluirRevisao;
    AppCompatImageView excuirRevisao;

    AlertDialog.Builder builder2 ;
    AlertDialog.Builder builder3 ;
    AlertDialog.Builder builder4 ;

    private AlertDialog reiniciarRevisao;
    Button btReiniciaRevisao;

    SQLiteDatabase db;
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_revisao_completa);
        excuirRevisao=(AppCompatImageView)findViewById(R.id.excluir_button);

        excuirRevisao.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                excluirRevisao = builder4.create();
                //Exibe
                excluirRevisao.show();

            }
        });

        btReiniciaRevisao =(Button)findViewById(R.id.reiciciar);

        btReiniciaRevisao.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                reiniciarRevisao = builder2.create();
                //Exibe
                reiniciarRevisao.show();
            }
        });
        Intent intent=getIntent();
        Bundle budle = intent.getExtras();
        id=budle.getString("id");
       //aleta dialog
        builder = new AlertDialog.Builder(this);
        //define o titulo
        builder.setTitle("Revisão");
        //define a mensagem
        //define um botão como positivo
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface arg0, int arg1) {
            }
        });


        //SEGUNDOOOOOO
        builder2 = new AlertDialog.Builder(this);
        //define o titulo
        builder2.setTitle("Revisão");
        builder2.setMessage("Tem certeza que deseja reiniciar essa revisão? os ciclos serão retomados do inicio 1-7-15-30 dias");
        builder2.setPositiveButton("Sim", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface arg0, int arg1) {
                reiciciar();

            }
        });
        builder2.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface arg0, int arg1) {
            }
        });
        //TERCEIROOOOO
        builder3 = new AlertDialog.Builder(this);
        //define o titulo
        builder3.setTitle("Revisão");
        builder3.setMessage("Essa revisão está atrasada deseja reinicia-la?");
        builder3.setPositiveButton("Sim", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface arg0, int arg1) {
                reiniciarRevisao = builder2.create();
                //Exibe
                reiniciarRevisao.show();

            }
        });
        builder3.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface arg0, int arg1) {
            }
        });

        //quartooooooooooooooooooooooo
        builder4 = new AlertDialog.Builder(this);
        //define o titulo
        builder4.setTitle("Revisão");
        builder4.setMessage("Tem certeza que deseja excluir essa revisao?");
        builder4.setPositiveButton("Sim", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface arg0, int arg1) {
              deletarRevisao();

            }
        });
        builder4.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface arg0, int arg1) {
            }
        });




        if(verificaSeEstaAtrasada()==true){
            revisaoAtrasada = builder3.create();
            //Exibe
            revisaoAtrasada.show();

        }
        listarRevisao();
    }
@Override
    public void onResume(){
        super.onResume();
    listarRevisao();
    }

 @Override
 public void onStart(){
        super.onStart();
     listarRevisao();
 }

    public void revisaoFeita(View v){
        //create the object no be included
        ContentValues values = new ContentValues();
        db = mDbHelper.getWritableDatabase();

        String querySql2 = "SELECT "+Contract.Tabelas.COLUNA_DIAS_PRA_PROXIMA_REVISAO +" FROM revisoes WHERE " + Contract.Tabelas._ID.toString() + "=" + "'" + id + "'";
        Cursor cursor2 = db.rawQuery(querySql2, null);
        if(cursor2.getCount()>0)
            cursor2.moveToFirst();
        int diasPraProximaRevisao = cursor2.getInt(cursor2.getColumnIndex(Contract.Tabelas.COLUNA_DIAS_PRA_PROXIMA_REVISAO));

        if(diasPraProximaRevisao<=1 || diasPraProximaRevisao>100 ) {

        String querySql = "SELECT revisoes_feitas FROM revisoes WHERE " + Contract.Tabelas._ID.toString() + "=" + "'" + id + "'";
        Cursor cursor = db.rawQuery(querySql, null);
        if(cursor.getCount()>0)
        cursor.moveToFirst();

        int revisoesFeitas = cursor.getInt(cursor.getColumnIndex(Contract.Tabelas.COLUNA_QTD_REVISOES_FEITAS));
        values.put(Contract.Tabelas.COLUNA_QTD_REVISOES_FEITAS,revisoesFeitas+1);

        String whereClause = Contract.Tabelas._ID + " = "+id;

        long newRowId = db.update(Contract.Tabelas.TABLE_NAME, values,whereClause,null);

        if (newRowId==-1){
            Toast.makeText(this, "Erro ao concluir revisão", Toast.LENGTH_SHORT).show();
        }else{
            Toast.makeText(this, "revisão concluída", Toast.LENGTH_SHORT).show();

        }
        mDbHelper.close();
        db.close();
        this.finish();

        }else{
            builder.setMessage("Só é possível concluir uma revisão faltando 1 dia ou menos para a sua data prevista");

            revisaoLonge = builder.create();
            //Exibe
            revisaoLonge.show();
        }

    }

    public Boolean  verificaSeEstaAtrasada(){
        ContentValues values = new ContentValues();
        db = mDbHelper.getWritableDatabase();

        String querySql = "SELECT "+Contract.Tabelas.COLUNA_DIAS_PRA_PROXIMA_REVISAO +" FROM revisoes WHERE " + Contract.Tabelas._ID.toString() + "=" + "'" + id + "'";
        Cursor cursor = db.rawQuery(querySql, null);
        if(cursor.getCount()>0)
            cursor.moveToFirst();
        int diasPraProximaRevisao = cursor.getInt(cursor.getColumnIndex(Contract.Tabelas.COLUNA_DIAS_PRA_PROXIMA_REVISAO));
        if(diasPraProximaRevisao>100){
            return true;
        }

        return false;
    }
    public void reiciciar(){

        Calendar calendar=Calendar.getInstance();
        SimpleDateFormat df =new SimpleDateFormat("dd/MM/yyyy HH:mm");
        df.format(calendar.getTime());

        DbHelper mDbHelper = new DbHelper(this);
        SQLiteDatabase db = mDbHelper.getWritableDatabase();

        //create the object no be included
        ContentValues values = new ContentValues();
        values.put(Contract.Tabelas.COLUNA_DATA_CRIACAO, df.format(calendar.getTime()));
        calendar.set(Calendar.DAY_OF_MONTH, calendar.get(Calendar.DAY_OF_MONTH) + 1);
        values.put(Contract.Tabelas.COLUNA_DATA_PROXIMA_REVISAO, df.format(calendar.getTime()));
        values.put(Contract.Tabelas.COLUNA_QTD_REVISOES_FEITAS,0);


        String whereClause = Contract.Tabelas._ID + " = " + id;
        long newRowId2 = db.update(Contract.Tabelas.TABLE_NAME, values,whereClause,null);
        if (newRowId2 == -1) {
            Toast.makeText(this, "Erro ao reiniciar revisao", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "Revisão reiniciada", Toast.LENGTH_SHORT).show();

        }
        mDbHelper.close();
        db.close();
        finish();

    }
    public void adiar(View v){
        //create the object no be included
        ContentValues values = new ContentValues();
        db = mDbHelper.getWritableDatabase();

        String querySql2 = "SELECT "+Contract.Tabelas.COLUNA_DIAS_PRA_PROXIMA_REVISAO +" FROM revisoes WHERE " + Contract.Tabelas._ID.toString() + "=" + "'" + id + "'";
        Cursor cursor2 = db.rawQuery(querySql2, null);
        if(cursor2.getCount()>0)
            cursor2.moveToFirst();
        int diasPraProximaRevisao = cursor2.getInt(cursor2.getColumnIndex(Contract.Tabelas.COLUNA_DIAS_PRA_PROXIMA_REVISAO));



        if(diasPraProximaRevisao<=1 || diasPraProximaRevisao>100 ) {
            String querySql = "SELECT data_criacao FROM revisoes WHERE " + Contract.Tabelas._ID.toString() + "=" + "'" + id + "'";
            Cursor cursor = db.rawQuery(querySql, null);
            if (cursor.getCount() > 0)
                cursor.moveToFirst();
            String dataCriacao = cursor.getString(cursor.getColumnIndex(Contract.Tabelas.COLUNA_DATA_CRIACAO));

            Calendar calendar;
            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm");
            calendar = DbHelper.converteStringPraCalendar(dataCriacao, sdf);
            calendar.set(Calendar.DAY_OF_MONTH, calendar.get(Calendar.DAY_OF_MONTH) + 1);


            values.put(Contract.Tabelas.COLUNA_DATA_CRIACAO, sdf.format(calendar.getTime()));

            String whereClause = Contract.Tabelas._ID + " = " + id;
            long newRowId = db.update(Contract.Tabelas.TABLE_NAME, values, whereClause, null);
            if (newRowId == -1) {
                Toast.makeText(this, "Erro ao adiar revisão", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "revisão adiada um dia", Toast.LENGTH_SHORT).show();

            }
            mDbHelper.close();

            db.close();
            this.finish();
        }else{
            builder.setMessage("Só é possível adiar uma revisão faltando 1 dia ou menos para a sua data prevista");
            revisaoLonge = builder.create();
            revisaoLonge.show();
        }






    }
    public void deletarRevisao() {
        DbHelper mDbHelper = new DbHelper(this);
        SQLiteDatabase db = mDbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        String querySql2 = "SELECT disciplina FROM revisoes WHERE " + Contract.Tabelas._ID + "=" + id ;
        String disciplina="";
        Cursor cursor = db.rawQuery(querySql2, null);
        cursor.moveToFirst();
        if (cursor.getCount() > 0) {
            disciplina = cursor.getString(cursor.getColumnIndex(Contract.Tabelas.COLUNA_DISCIPLINA));
            Log.v("chatuba1",disciplina+"");

            String querySql = "SELECT qtd_revisoes FROM disciplinas WHERE " + Contract.Tabelas.COLUNA_DISCIPLINA + "=" +"'"+ disciplina+ "'";
            cursor = db.rawQuery(querySql, null);
            cursor.moveToFirst();
        }
        if (cursor.getCount() > 0) {
            int qtdrevisoes = cursor.getInt(cursor.getColumnIndex(Contract.Tabelas.COLUNA_QTD_REVISOES));
            Log.v("chatuba",qtdrevisoes+"");

            values.put(Contract.Tabelas.COLUNA_QTD_REVISOES, qtdrevisoes - 1);

            String whereClause = Contract.Tabelas.COLUNA_DISCIPLINA + "=" + "'"+disciplina+"'"  ;
            long newRowId = db.update(Contract.Tabelas.TABLE_NAME2, values, whereClause, null);
        }
        db = mDbHelper.getWritableDatabase();
        // Create and/or open a database to write from it

        String whereClause2 =  Contract.Tabelas._ID + "="+ id;

        long newRowId2 = db.delete(Contract.Tabelas.TABLE_NAME,whereClause2,null);
        if (newRowId2==-1){
            Toast.makeText(this, "Erro ao deletar revisão", Toast.LENGTH_SHORT).show();
        }else{
            Toast.makeText(this, "revisão deletada", Toast.LENGTH_SHORT).show();

        }
        mDbHelper.close();

        db.close();

        this.finish();


    }
    public void listarRevisao() {

        Calendar calendar;
        SimpleDateFormat sdf=new SimpleDateFormat("dd/MM/yyyy");


        Lista listaRevisao = new Lista();
        final Cursor cursor = listaRevisao.listaCompleta(RevisaoCompleta.this, Contract.Tabelas.TABLE_NAME,id);

        // AQUI EU FIZ PARA SEPARAR O BD DA LISTA, ENTAO EU CRIO
        //UM OBJETO E CHAMO ELE PASSANDO O CONTEXTO. ELE ME RETORNA O CURSOR COM TUDO QUE ESTA NO BD
        //Find ListView to populate
        TextView tvAssunto = (TextView)findViewById(R.id.assunto_textView);
        TextView tvDisciplina = (TextView)findViewById(R.id.disciplina_textView);
        TextView tvData = (TextView)findViewById(R.id.proxRevisao_textView);
        TextView tvMateria = (TextView)findViewById(R.id.materia_textView);

        String disciplina = cursor.getString(cursor.getColumnIndexOrThrow(Contract.Tabelas.COLUNA_DISCIPLINA));
        String dataProximaRevisao=cursor.getString(cursor.getColumnIndexOrThrow(Contract.Tabelas.COLUNA_DATA_PROXIMA_REVISAO));
        String materia=cursor.getString(cursor.getColumnIndexOrThrow(Contract.Tabelas.COLUNA_MATERIA));
        String assunto=cursor.getString(cursor.getColumnIndexOrThrow(Contract.Tabelas.COLUNA_ASSUNTO));

        calendar=DbHelper.converteStringPraCalendar(dataProximaRevisao,sdf);

        tvAssunto.setText(assunto);
        tvDisciplina.setText(disciplina);
        tvData.setText(sdf.format(calendar.getTime()));

        if(materia.equals("Selecione")) {
            tvMateria.setText("");
        }else{
            tvMateria.setText(materia);
        }

    }
}
