package e.joaopaulo.tcc;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.NavUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;


public class RevisoesFragment extends Fragment {
    FloatingActionButton btAddRevisao;
    Button btRevisaoFeita;
    View view;

    //Objeto SQLiteOpenHelper


    @Override
    public void onStart(){
        super.onStart();
        listarRevisoes();
        updateProximaRevisao();
        updateDiasProximaRevisao();

    }
                                   //ESSE ON START E ON RESUME SERVEM PARA CONSEGUIR ATUALIZAR A LISTA, PQ QUANDO VC CRIA UMA NOVA
                                    //REVISAO AI NA HORA QUE VOLTA ELE PRECISA ATUALIZAR

    @Override
    public void onResume(){
        super.onResume();
        listarRevisoes();
    }
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.revisoes_fragment, container, false);
        btAddRevisao=(FloatingActionButton)view.findViewById(R.id.botaoAddRevisao);

        btAddRevisao.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(),EditorRevisao.class);
                startActivity(intent);
            }
        });


        View.OnClickListener myButtonClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                View parentRow = (View) v.getParent();
                ListView listView = (ListView) parentRow.getParent();
                final int position = listView.getPositionForView(parentRow);
                Log.v("soueu","oi");

            }
        };
        updateProximaRevisao();
        listarRevisoes();
        updateDiasProximaRevisao();


        return view;

    }

  public void updateProximaRevisao(){
      Integer qtd_revisoes=0;
      Lista listaRevisao = new Lista();

      Cursor cursor = listaRevisao.listarInicial(getContext(), Contract.Tabelas.TABLE_NAME);
      DbHelper mDbHelper = new DbHelper(getContext());
      SQLiteDatabase db = mDbHelper.getWritableDatabase();

      ContentValues values = new ContentValues();

      if(cursor.getCount()>0) {

          String id ;
          String criacao_revisao ;

          cursor.moveToFirst();

          do{

               id = cursor.getString(cursor.getColumnIndexOrThrow(Contract.Tabelas._ID));
              qtd_revisoes = cursor.getInt(cursor.getColumnIndexOrThrow(Contract.Tabelas.COLUNA_QTD_REVISOES_FEITAS));
               criacao_revisao = cursor.getString(cursor.getColumnIndexOrThrow(Contract.Tabelas.COLUNA_DATA_CRIACAO));

              Log.v("joaozao", criacao_revisao);
              Log.v("joaozao2", qtd_revisoes + "");

              Calendar calendar;
              SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm");
              calendar = DbHelper.converteStringPraCalendar(criacao_revisao, sdf);

                  if (qtd_revisoes == 0) {
                  calendar.set(Calendar.DAY_OF_MONTH, calendar.get(Calendar.DAY_OF_MONTH) + 1);

              } else if (qtd_revisoes == 1) {
                  calendar.set(Calendar.DAY_OF_MONTH, calendar.get(Calendar.DAY_OF_MONTH) + 7);

              } else if (qtd_revisoes == 2) {
                  calendar.set(Calendar.DAY_OF_MONTH, calendar.get(Calendar.DAY_OF_MONTH) + 15);

              } else if (qtd_revisoes > 2) {

                  calendar.set(Calendar.DAY_OF_MONTH, calendar.get(Calendar.DAY_OF_MONTH) + (30 * (qtd_revisoes - 2)));

              }

              sdf.format(calendar.getTime());
              values.put(Contract.Tabelas.COLUNA_DATA_PROXIMA_REVISAO, sdf.format(calendar.getTime()));


              String whereClause = Contract.Tabelas._ID + "=" + "'" + id + "'";
              Log.v("joaozao3", id + "");

              long newRowId2 = db.update(Contract.Tabelas.TABLE_NAME, values, whereClause, null);
              id = null;
          } while (cursor.moveToNext());
      }
      db.close();

  }

  public void updateDiasProximaRevisao(){
      Lista listaRevisao = new Lista();
      Cursor cursor = listaRevisao.listarInicial(getContext(), Contract.Tabelas.TABLE_NAME);
      DbHelper mDbHelper = new DbHelper(getContext());
      SQLiteDatabase db = mDbHelper.getWritableDatabase();

      ContentValues values = new ContentValues();


      if(cursor.getCount()>0) {
          cursor.moveToFirst();
          do{
          String id = cursor.getString(cursor.getColumnIndexOrThrow(Contract.Tabelas._ID));
          String proxima_revisao = cursor.getString(cursor.getColumnIndexOrThrow(Contract.Tabelas.COLUNA_DATA_PROXIMA_REVISAO));

          Calendar calendar;
          SimpleDateFormat sdf=new SimpleDateFormat("dd/MM/yyyy HH:mm");
          calendar=DbHelper.converteStringPraCalendar(proxima_revisao,sdf);
          Calendar diaHoje= Calendar.getInstance();
          int diasPraProximaRevisao=(calendar.get(Calendar.DAY_OF_YEAR))-(diaHoje.get(Calendar.DAY_OF_YEAR));
          if (diasPraProximaRevisao<0){
              diasPraProximaRevisao=diasPraProximaRevisao+10000;
          }
          values.put(Contract.Tabelas.COLUNA_DIAS_PRA_PROXIMA_REVISAO,diasPraProximaRevisao);


          String whereClause = Contract.Tabelas._ID + "=" + "'" + id + "'";
          Log.v("dias",diasPraProximaRevisao+"");

          long newRowId2 = db.update(Contract.Tabelas.TABLE_NAME,values,whereClause,null );
          } while (cursor.moveToNext());

      }
      db.close();

  }






    public void listarRevisoes(){

        Lista listaRevisao = new Lista();
       final Cursor cursor = listaRevisao.listaDeRevisoes(getContext(), Contract.Tabelas.TABLE_NAME);


        // AQUI EU FIZ PARA SEPARAR O BD DA LISTA, ENTAO EU CRIO
        //UM OBJETO E CHAMO ELE PASSANDO O CONTEXTO. ELE ME RETORNA O CURSOR COM TUDO QUE ESTA NO BD
        //Find ListView to populate
        final ListView lvItems = (ListView) view.findViewById(R.id.lista_revisoes);

        final RevisoesCursorAdapter revisaCursorAdapter = new RevisoesCursorAdapter(getContext(), cursor);

        lvItems.setAdapter(revisaCursorAdapter);

        lvItems.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {


                Intent intent = new Intent(getContext(), RevisaoCompleta.class);
                Bundle bundle=new Bundle();
                intent.putExtra("id",cursor.getString(0));
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });







    }


}
