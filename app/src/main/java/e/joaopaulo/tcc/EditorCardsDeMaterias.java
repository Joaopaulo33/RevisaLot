package e.joaopaulo.tcc;

import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;
import android.graphics.Bitmap;
import android.support.design.widget.FloatingActionButton;
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
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;

public class EditorCardsDeMaterias extends AppCompatActivity {
    //views de entrada de dados
    Cursor cursor;
    private String disciplinaDoCard="";
    private String materiaDoCard="";
    private EditText textoEditText;
    private EditText assuntoEditText;
    ImageButton btAddDisciplina;
    Bitmap img=null;
    Integer temFoto=0;
    LinearLayout layoutMateria;
    LinearLayout layoutDisciplina;

    // Spinner sppinerMateria;
    //ImageButton btAdicionarMateria;
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
        Intent intent=getIntent();
        Bundle budle = intent.getExtras();
        materiaDoCard=budle.getString("nome_materia");
        disciplinaDoCard=budle.getString("nome_discipina");
        textoEditText = findViewById(R.id.texto_editText);
        assuntoEditText = findViewById(R.id.assunto_editText);
        setTitle("Criar novo card");

        layoutMateria = (LinearLayout) findViewById(R.id.container_materia);
        layoutDisciplina = (LinearLayout) findViewById(R.id.container_disciplina);

        layoutMateria.setVisibility(View.GONE);
        layoutDisciplina.setVisibility(View.GONE);



    }
    public void tirarFoto (View view){
        Intent intent = new Intent("android.media.action.IMAGE_CAPTURE");
        temFoto=1;
        startActivityForResult(intent, 1);
    }







    public void onStart() {
        super.onStart();
        list2= dbHelper.getTodasMaterias(disciplinaDoCard);
        list= dbHelper.getTodasDiciplinas();


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


//descobrir atravez da materia a discipina
 //           String querySql2 = "SELECT disciplina FROM cards WHERE " + Contract.Tabelas.COLUNA_MATERIA + "=" + "'" + materiaDoCard + "'";
   //         Cursor cursor2 = db.rawQuery(querySql2, null);
     //       cursor2.moveToFirst();
       //         if(cursor2.getCount()>0)
         //   disciplinaDoCard = cursor2.getString(cursor.getColumnIndex(Contract.Tabelas.COLUNA_DISCIPLINA));

            ContentValues values = new ContentValues();
//mudar a quantidade de cardes de determinada disciiplina
            String querySql = "SELECT qtd_cards FROM disciplinas WHERE " + Contract.Tabelas.COLUNA_DISCIPLINA + "=" + "'" + disciplinaDoCard + "'";
            Cursor cursor = db.rawQuery(querySql, null);
                cursor.moveToFirst();
            int qtdcard=0;
            if(cursor.getCount()>0)
                qtdcard = cursor.getInt(cursor.getColumnIndex(Contract.Tabelas.COLUNA_QTD_CARDS));

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
            if (resultCode ==EditorRevisao.RESULT_OK  && requestCod == 1) {
                Bundle bundle = data.getExtras();
                if (bundle != null) {
                    img = (Bitmap) bundle.get("data");
                    ImageView iv = (ImageView) findViewById(R.id.imageView1);
                    iv.setImageBitmap(img);
                    //     guardarImagen(img);

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
