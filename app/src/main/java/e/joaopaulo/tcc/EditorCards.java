package e.joaopaulo.tcc;

import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;
import android.graphics.Bitmap;
import android.provider.MediaStore;
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
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;

public class EditorCards extends AppCompatActivity {
    //views de entrada de dados
    Cursor cursor;
    private String disciplinaDoCard="";
    private String materiaDoCard="";
    private EditText textoEditText;
    private EditText assuntoEditText;
    private Spinner mDiciplinaSpinner;
    private Spinner mMateriaSpinner;
    ImageButton btAddDisciplina;
    ImageButton btAddMateria;
    Bitmap img=null;
    Integer temFoto=0;
    int CAMERA = 7;

    private AlertDialog disciplinaVazia;

 //   AlertDialog.Builder builder ;

    //   parte do spinner
    ArrayList<String> list2 =new  ArrayList<String>();
    ArrayList<String> list =new  ArrayList<String>();
    DbHelper dbHelper=new DbHelper(this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editor_cards);
        DbHelper dbHelper = new DbHelper(this);


        textoEditText = findViewById(R.id.texto_editText);
        assuntoEditText = findViewById(R.id.assunto_editText);
        setTitle("Criar novo card");


     /*   //aleta dialog
        builder = new AlertDialog.Builder(this);
        //define o titulo
        builder.setTitle("Disciplina");
        //define a mensagem
        builder.setMessage("Selecione uma disciplina");
        //define um botão como positivo
        builder.setNeutralButton("OK", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface arg0, int arg1) {
            }
        });
*/


        btAddDisciplina = (ImageButton) findViewById(R.id.botaoAddDisciplina);
        btAddMateria = (ImageButton) findViewById(R.id.botaoAddMaterias);

        btAddDisciplina.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(EditorCards.this, EditorDisciplina.class);
                startActivityForResult(intent, 2);

            }
        });


        btAddMateria.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DbHelper mDbHelper = new DbHelper(EditorCards.this);

                SQLiteDatabase db = mDbHelper.getReadableDatabase();
                Lista listasDisciplina = new Lista();

                if (disciplinaDoCard != "Selecione") {
                    String querySql = "SELECT _ID FROM disciplinas WHERE disciplina =" + "'" + disciplinaDoCard + "'";
                    Cursor cursor = db.rawQuery(querySql, null);
                    cursor.moveToFirst();

                    Intent intent = new Intent(EditorCards.this, EditorMateria.class);
                    Bundle bundle = new Bundle();
                    intent.putExtra("id", cursor.getInt(0));
                    intent.putExtras(bundle);
                    startActivityForResult(intent, 2);

                } else {
                    Toast.makeText(EditorCards.this, "Selecione uma disciplina", Toast.LENGTH_SHORT).show();
                    // disciplinaVazia = builder.create();
                    //Exibe
                    // disciplinaVazia.show();
                }
            }
        });
        list = dbHelper.getTodasDiciplinas();
        setupSpinnerDisciplina();

        setupSpinnerMateria();
        if (materiaDoCard != "" && materiaDoCard != "Selecione") {
            list2.add(0, materiaDoCard);
        }
        dbHelper.close();

    }
        public void tirarFoto (View view){
            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

            temFoto=1;
            if(intent.resolveActivity(getPackageManager())!=null) {
                startActivityForResult(intent, CAMERA);
            }
        }







    public void onStart() {
        super.onStart();
        list2= dbHelper.getTodasMaterias(disciplinaDoCard);
        list= dbHelper.getTodasDiciplinas();

        setupSpinnerDisciplina();
        setupSpinnerMateria();

        if(disciplinaDoCard!="" &&disciplinaDoCard!="Selecione") {
            list.add(0, disciplinaDoCard);
        }
        if(disciplinaDoCard!="" &&disciplinaDoCard!="Selecione") {
            list2.add(0, disciplinaDoCard);
        }
    }


    @Override
    public void onResume(){
        super.onResume();
        //  list2= dbHelper.getTodasMaterias(disciplinaDaRevisao);
        if(materiaDoCard!="" &&materiaDoCard!="Selecione") {
            list2.add(0, materiaDoCard);
        }

    }




    public void criarNovoCard(){

        if(disciplinaDoCard !="Selecione") {
            DbHelper mDbHelper = new DbHelper(this);
            SQLiteDatabase db = mDbHelper.getWritableDatabase();
            //create the object no be included
            ContentValues values = new ContentValues();


            String querySql = "SELECT qtd_cards FROM disciplinas WHERE " + Contract.Tabelas.COLUNA_DISCIPLINA + "=" + "'" + disciplinaDoCard + "'";
            Cursor cursor = db.rawQuery(querySql, null);
            cursor.moveToFirst();
            int qtdcard = cursor.getInt(cursor.getColumnIndex(Contract.Tabelas.COLUNA_QTD_CARDS));

            values.put(Contract.Tabelas.COLUNA_QTD_CARDS, qtdcard + 1);

            String whereClause = Contract.Tabelas.COLUNA_DISCIPLINA + "=" + "'" + disciplinaDoCard + "'";
            long newRowId = db.update(Contract.Tabelas.TABLE_NAME2, values, whereClause, null);


            //trim remove the blanck spaces
            String texto = textoEditText.getText().toString().trim();
            String assunto = assuntoEditText.getText().toString().trim();
            // Create database helper


            // Create and/or open a database to write from it
            DbHelper mDbHelper2 = new DbHelper(this);

            ContentValues values2 = new ContentValues();
            SQLiteDatabase db2 = mDbHelper2.getWritableDatabase();

            //create the object no be included
            values2.put(Contract.Tabelas.COLUNA_FOTO, guardarImagem(img));
            values2.put(Contract.Tabelas.COLUNA_TEXTO_CARD, texto);
            values2.put(Contract.Tabelas.COLUNA_ASSUNTO, assunto);
            values2.put(Contract.Tabelas.COLUNA_DISCIPLINA, disciplinaDoCard);
            values2.put(Contract.Tabelas.COLUNA_MATERIA, materiaDoCard);
            values2.put(Contract.Tabelas.COLUNA_TEM_FOTO,temFoto);



            //  values.put(Contract.Tabelas.COLUNA_ID_DISCIPLINA, id_disciplina);


            long newRowId2 = db2.insert(Contract.Tabelas.TABLE_NAME3, null, values2);


            if (newRowId == -1) {
                Toast.makeText(this, "Erro ao criar card", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "card criado ", Toast.LENGTH_SHORT).show();

            }
            mDbHelper.close();
            db.close();
            finish();
        }else{
            Toast.makeText(this, "Selecione uma disciplina", Toast.LENGTH_SHORT).show();

         //   disciplinaVazia = builder.create();
            //Exibe
          //  disciplinaVazia.show();
        }
    }

    @Override
    public void onActivityResult(int requestCod,int resultCode, Intent data) {
        super.onActivityResult(requestCod, resultCode, data);


        if (data != null) {
            if (resultCode == -1 && requestCod == 2) {
                list = dbHelper.getTodasDiciplinas();
                if (data != null)
                    disciplinaDoCard = data.getStringExtra("nomeDisciplina");
            }

            if (resultCode == 0 && requestCod == 2) {
                list = dbHelper.getTodasDiciplinas();
                if (data != null)
                    materiaDoCard = data.getStringExtra("nomeMateria");

                //  int position=mDiciplinaSpinner.getSelectedItemPosition();
                //  String discplinaSelecionada=list.get(mDiciplinaSpinner.getSelectedItemPosition());
            }
            if (resultCode ==EditorRevisao.RESULT_OK  && requestCod == CAMERA) {
                Bundle bundle = data.getExtras();
                if (bundle != null) {
                    img = (Bitmap) bundle.get("data");
                    ImageView iv = (ImageView) findViewById(R.id.imageView1);
                    iv.setImageBitmap(img);
                    //     guardarImagen(img);

                }
                if (materiaDoCard != "" && materiaDoCard != "Selecione") {
                    list2.add(0, materiaDoCard);
                }

            }
        }
    }
    public byte[] guardarImagem( Bitmap bitmap){
        // tamaño del baos depende del tamaño de tus imagenes en promedio
        byte[] blob=null;
        if(bitmap!=null) {
            ByteArrayOutputStream baos = new ByteArrayOutputStream(20480);
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, baos);
             blob = baos.toByteArray();
            // aqui tenemos el byte[] con el imagen comprimido, ahora lo guardemos en SQLite
            //  values.put(Contract.Tabelas.COLUNA_FOTO, blob);
            return blob;
        }

    return blob;
    }

    private void setupSpinnerDisciplina() {
        mDiciplinaSpinner =(Spinner)findViewById(R.id.spinner_diciplina);
        ArrayAdapter<String> adapter=new ArrayAdapter<String>(this, R.layout.spinner_layout, R.id.text, list);

        mDiciplinaSpinner.setAdapter(adapter);

        mDiciplinaSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String  selection = (String) parent.getItemAtPosition(position);

                if (!TextUtils.isEmpty(selection)) {
                    list= dbHelper.getTodasDiciplinas();

                    disciplinaDoCard = selection;
                    Log.v("disciplinadaRevisao",disciplinaDoCard+"");
                    setupSpinnerMateria();
                    if(materiaDoCard!="" &&materiaDoCard!="Selecione") {
                        list2.add(0, materiaDoCard);
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
        list2= dbHelper.getTodasMaterias(disciplinaDoCard);

        mMateriaSpinner=(Spinner)findViewById(R.id.spinner_materia);

        ArrayAdapter<String> adapter2=new ArrayAdapter<String>(this, R.layout.spinner_layout, R.id.text, list2);

        mMateriaSpinner.setAdapter(adapter2);

        mMateriaSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                String selection = (String) parent.getItemAtPosition(position);

                if (!TextUtils.isEmpty(selection)) {
                    list2= dbHelper.getTodasMaterias(disciplinaDoCard);
                    materiaDoCard=selection;

                }
            }

            // Because AdapterView is an abstract class, onNothingSelected must be defined
            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
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
                    criarNovoCard();
                //finish();
                return true;
            // Respond to a click on the "Delete" menu option
        ///    case R.id.botao_deletar :
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
