package e.joaopaulo.tcc;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.SystemClock;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

public class EditorRevisao extends AppCompatActivity {
    //views de entrada de dados
    // String selection;
    private EditText assuntoEditText;
    private EditText materiaEditText;
    private Spinner mDisciplinaSpinner;
    private String disciplinaDaRevisao="";
    private AlertDialog disciplinaVazia;
    // AlertDialog.Builder builder ;
    private Spinner mMateriaSpinner;
    private String materiaDaRevisao="";
    ImageButton btAddDisciplina;
    ImageButton btAddMateria;
    //   parte do spinner
    ArrayList<String> list2 =new  ArrayList<String>();
    ArrayList<String> list =new  ArrayList<String>();
    DbHelper dbHelper=new DbHelper(this);


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editor_revisoes);
        assuntoEditText = findViewById(R.id.assunto_editText);
        materiaEditText = findViewById(R.id.materia_editText);
        setTitle("Criar nova revisão");



        //aleta dialog
        // builder = new AlertDialog.Builder(this);
        //define o titulo
        //  builder.setTitle("Disciplina");
        //define a mensagem
        //  builder.setMessage("Selecione uma disciplina");
        //define um botão como positivo
        // builder.setNeutralButton("OK", new DialogInterface.OnClickListener() {
        //    public void onClick(DialogInterface arg0, int arg1) {
        //   }
        //  });





        btAddDisciplina=(ImageButton)findViewById(R.id.botaoAddDisciplina) ;
        btAddMateria=(ImageButton)findViewById(R.id.botaoAddMaterias) ;

        btAddDisciplina.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(EditorRevisao.this,EditorDisciplina.class);
                startActivityForResult(intent,1);
            }
        });
        btAddMateria.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DbHelper mDbHelper = new DbHelper(EditorRevisao.this);

                SQLiteDatabase db = mDbHelper.getReadableDatabase();
                Lista listasDisciplina = new Lista();

                if(disciplinaDaRevisao !="Selecione") {
                    String querySql="SELECT _ID FROM disciplinas WHERE disciplina ="+"'"+ disciplinaDaRevisao +"'";
                    Cursor cursor = db.rawQuery(querySql, null);
                    cursor.moveToFirst();

                    Intent intent = new Intent(EditorRevisao.this, EditorMateria.class);
                    Bundle bundle = new Bundle();
                    intent.putExtras(bundle);

                    intent.putExtra("id", cursor.getInt(0));
                    startActivityForResult(intent,1);
                }else {
                    // disciplinaVazia = builder.create();
                    //Exibe
                    //disciplinaVazia.show();
                    Toast.makeText(EditorRevisao.this, "Selecione uma disciplina", Toast.LENGTH_SHORT).show();

                }
            }
        });

        list= dbHelper.getTodasDiciplinas();
        setupSpinnerDisciplina();

        setupSpinnerMateria();
        if(materiaDaRevisao!="" &&materiaDaRevisao!="Selecione") {
            list2.add(0, materiaDaRevisao);
        }
        dbHelper.close();
    }



    @Override
    public void onStart() {
        super.onStart();
        list2= dbHelper.getTodasMaterias(disciplinaDaRevisao);
        list= dbHelper.getTodasDiciplinas();

        setupSpinnerDisciplina();
        setupSpinnerMateria();

        if(disciplinaDaRevisao!="" &&disciplinaDaRevisao!="Selecione") {
            list.add(0, disciplinaDaRevisao);
        }
        if(materiaDaRevisao!="" &&materiaDaRevisao!="Selecione") {
            list2.add(0, materiaDaRevisao);
        }
    }


    @Override
    public void onActivityResult(int requestCod,int resultCode, Intent data){

        super.onActivityResult(requestCod,resultCode,data);

        if(resultCode==EditorRevisao.RESULT_OK && requestCod==1){
            list= dbHelper.getTodasDiciplinas();
            if (data!=null)
                disciplinaDaRevisao=data.getStringExtra("nomeDisciplina");
        }

        if(resultCode==0 && requestCod==1){
            list= dbHelper.getTodasDiciplinas();
            if (data!=null)
                materiaDaRevisao=data.getStringExtra("nomeMateria");
            //  int position=mDiciplinaSpinner.getSelectedItemPosition();
            //  String discplinaSelecionada=list.get(mDiciplinaSpinner.getSelectedItemPosition());
        }
        if(materiaDaRevisao!="" && materiaDaRevisao!="Selecione") {
            list2.add(0, materiaDaRevisao);
        }
    }






    @Override
    public void onResume(){
        super.onResume();
        //  list2= dbHelper.getTodasMaterias(disciplinaDaRevisao);
        if(materiaDaRevisao!="" &&materiaDaRevisao!="Selecione") {
            list2.add(0, materiaDaRevisao);
        }

    }


    private void setupSpinnerDisciplina() {
        mDisciplinaSpinner =(Spinner)findViewById(R.id.spinner_diciplina);
        ArrayAdapter<String> adapter=new ArrayAdapter<String>(this, R.layout.spinner_layout, R.id.text, list);

        mDisciplinaSpinner.setAdapter(adapter);

        mDisciplinaSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String  selection = (String) parent.getItemAtPosition(position);

                if (!TextUtils.isEmpty(selection)) {
                    list= dbHelper.getTodasDiciplinas();

                    disciplinaDaRevisao = selection;
                    Log.v("disciplinadaRevisao",disciplinaDaRevisao+"");
                    setupSpinnerMateria();
                    if(materiaDaRevisao!="" &&materiaDaRevisao!="Selecione") {
                        list2.add(0, materiaDaRevisao);
                    }
                }

            }




            // Because AdapterView is an abstract cass, onNothingSelected must be defined
            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }






    private void setupSpinnerMateria() {
        list2= dbHelper.getTodasMaterias(disciplinaDaRevisao);

        mMateriaSpinner=(Spinner)findViewById(R.id.spinner_materia);

        ArrayAdapter<String> adapter2=new ArrayAdapter<String>(this, R.layout.spinner_layout, R.id.text, list2);

        mMateriaSpinner.setAdapter(adapter2);

        mMateriaSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                String selection = (String) parent.getItemAtPosition(position);

                if (!TextUtils.isEmpty(selection)) {
                    list2= dbHelper.getTodasMaterias(disciplinaDaRevisao);
                    materiaDaRevisao=selection;

                }
            }

            // Because AdapterView is an abstract class, onNothingSelected must be defined
            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }


    public void criarNovaRevisao(){
        if(disciplinaDaRevisao !="Selecione") {
            String assunto=assuntoEditText.getText().toString().trim();

            if(assunto.length()>0) {
                //Tudo sobre data
                Calendar calendar=Calendar.getInstance();
                calendar.set(Calendar.DAY_OF_MONTH, calendar.get(Calendar.DAY_OF_MONTH) -2);

                SimpleDateFormat df =new SimpleDateFormat("dd/MM/yyyy HH:mm");
                df.format(calendar.getTime());


                DbHelper mDbHelper = new DbHelper(this);
                SQLiteDatabase db = mDbHelper.getWritableDatabase();

                //create the object no be included
                ContentValues values = new ContentValues();

                String querySql = "SELECT qtd_revisoes FROM disciplinas WHERE " + Contract.Tabelas.COLUNA_DISCIPLINA + "=" + "'" + disciplinaDaRevisao + "'";

                Cursor cursor = db.rawQuery(querySql, null);
                cursor.moveToFirst();
                int qtdrevisoes = cursor.getInt(cursor.getColumnIndex(Contract.Tabelas.COLUNA_QTD_REVISOES));

                values.put(Contract.Tabelas.COLUNA_QTD_REVISOES, qtdrevisoes + 1);

                String whereClause = Contract.Tabelas.COLUNA_DISCIPLINA + "=" + "'" + disciplinaDaRevisao + "'";
                long newRowId = db.update(Contract.Tabelas.TABLE_NAME2, values, whereClause, null);


                SQLiteDatabase db2 = mDbHelper.getWritableDatabase();
                ContentValues values2 = new ContentValues();

                //trim remove the blanck spaces
                // Create database helper
                // Create and/or open a database to write from it
                //create the object no be included


                values2.put(Contract.Tabelas.COLUNA_ASSUNTO, assunto);
                values2.put(Contract.Tabelas.COLUNA_DISCIPLINA, disciplinaDaRevisao);
                values2.put(Contract.Tabelas.COLUNA_MATERIA, materiaDaRevisao);

                values2.put(Contract.Tabelas.COLUNA_QTD_REVISOES_FEITAS, 0);
                values2.put(Contract.Tabelas.COLUNA_NIVEL_DE_CONSOLIDACAO, 0);
                values2.put(Contract.Tabelas.COLUNA_DATA_CRIACAO, df.format(calendar.getTime()));


                long newRowId2 = db2.insert(Contract.Tabelas.TABLE_NAME, null, values2);


                //aquiiiiiiiiiiiiiiiiiiiiiii



                //create the object no be included

                String querySql3 = "SELECT * FROM "+ Contract.Tabelas.TABLE_NAME+" WHERE _id = (SELECT MAX(_id)  FROM "+Contract.Tabelas.TABLE_NAME+");";

                cursor = db.rawQuery(querySql3, null);
                cursor.moveToFirst();
                String id="";
                id = cursor.getString(cursor.getColumnIndex(Contract.Tabelas._ID));
                if (newRowId2 == -1) {
                    Toast.makeText(this, "Erro ao criar revisão", Toast.LENGTH_SHORT).show();
                } else {
                    calendar.set(Calendar.DAY_OF_MONTH, calendar.get(Calendar.DAY_OF_MONTH) );

                    criaAlarme(id,df.format(calendar.getTime()));

                    Toast.makeText(this, "Revisão criada ", Toast.LENGTH_SHORT).show();

                }
                mDbHelper.close();
                db.close();
                finish();
            }else{
                Toast.makeText(this, "O assunto não pode ficar vazio", Toast.LENGTH_SHORT).show();

            }
        }else{

            Toast.makeText(this, "Selecione uma disciplina", Toast.LENGTH_SHORT).show();

            // disciplinaVazia = builder.create();
            //Exibe
            // disciplinaVazia.show();
        }





    }

    public boolean criaAlarme(String id , String proxima_revisao){

        boolean retorno = false;




        Calendar calendar;
        SimpleDateFormat sdf=new SimpleDateFormat("dd/MM/yyyy HH:mm");

        //descomentar
        //calendar=DbHelper.converteStringPraCalendar(proxima_revisao,sdf);

        //teste

        calendar = Calendar.getInstance();
        Log.v("minutos1",Integer.toString(calendar.get(Calendar.MINUTE)));

        //setei para almentar 1 min do horario atual
        calendar.set(Calendar.MINUTE, calendar.get(Calendar.MINUTE) + 1);

        Log.v("minutos2",Integer.toString(calendar.get(Calendar.MINUTE)));


        //criando alarme
        AlarmManager alarmMgr = (AlarmManager) this.getSystemService(Context.ALARM_SERVICE);

        Intent intent = new Intent(this, Alarme.class);

        intent.putExtra("id", 0);

        PendingIntent alarmIntent = PendingIntent.getBroadcast(this, 0, intent, 0);

        alarmMgr.set(AlarmManager.RTC_WAKEUP,calendar.getTimeInMillis(), alarmIntent);


        return retorno;
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
                criarNovaRevisao();

                return true;
            // Respond to a click on the "Delete" menu option
            //   case R.id.botao_deletar :
            // Do nothing for now
            // deletarDisciplina(idDiciplina);
            //     finish();
            //   return true;
            // Respond to a click on the "Up" arrow button in the app bar
            case android.R.id.home:
                // Navigate back to parent activity (CatalogActivity)
                NavUtils.navigateUpFromSameTask(this);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

}
