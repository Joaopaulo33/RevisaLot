package e.joaopaulo.tcc;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class DisciplinasCursorAdapter extends CursorAdapter {
    private LayoutInflater cursorInflater;
    Context contexts;
    AlertDialog.Builder builder ;
    private AlertDialog excluirDisciplina;
    String idDisciplina;


    public DisciplinasCursorAdapter(Context context, Cursor c) {
        super(context, c, 0);
        contexts=context;
        cursorInflater = (LayoutInflater) context.getSystemService(
                Context.LAYOUT_INFLATER_SERVICE);
        //aleta dialog
        builder = new AlertDialog.Builder(contexts);
        //define o titulo
        builder.setTitle("Disciplina");
        builder.setMessage("Tem certeza que deseja excluir essa disciplina?");
        builder.setPositiveButton("Sim", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface arg0, int arg1) {
                deletarDisciplina(idDisciplina);

            }
        });
        builder.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface arg0, int arg1) {
            }
        });
        //define a mensagem
        //define um botão como positivo

    }
    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {

        return LayoutInflater.from(context).inflate(R.layout.item_disciplina_list, parent, false);
    }


    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        TextView tvDisciplina = (TextView) view.findViewById(R.id.disciplina_textView);
        TextView tvQtdCards =(TextView) view.findViewById(R.id.num_cards_textView);
        TextView tvQtdRevisoes =(TextView) view.findViewById(R.id.num_revisoes_textView);

      //  ImageView buttonExclui= (ImageView) view.findViewById(R.id.excluir_button);
        final String id = cursor.getString(cursor.getColumnIndexOrThrow(Contract.Tabelas._ID));
        idDisciplina=id;
      //  buttonExclui.setVisibility(View.INVISIBLE);

     //   buttonExclui.setOnClickListener(new View.OnClickListener() {
         //   @Override
          //  public void onClick(View v) {
    //            excluirDisciplina = builder.create();
                //Exibe
        //        excluirDisciplina.show();


      //     }
     //   });

        // Extract properties from cursor
        String disciplina = cursor.getString(cursor.getColumnIndexOrThrow(Contract.Tabelas.COLUNA_DISCIPLINA));
        String qtd_cards = cursor.getString(cursor.getColumnIndexOrThrow(Contract.Tabelas.COLUNA_QTD_CARDS));
        String qtd_revisoes = cursor.getString(cursor.getColumnIndexOrThrow(Contract.Tabelas.COLUNA_QTD_REVISOES));
        qtd_cards+= " cards";
        qtd_revisoes+= " revisões";

        tvDisciplina.setText(disciplina);
        tvQtdCards.setText(qtd_cards);
        tvQtdRevisoes.setText(qtd_revisoes);



    }
    private void deletarDisciplina(String id){
        DbHelper mDbHelper = new DbHelper(contexts);

        // Create and/or open a database to write from it
        SQLiteDatabase db = mDbHelper.getWritableDatabase();

        String whereClause =  Contract.Tabelas._ID + "="+ id;

        long newRowId = db.delete(Contract.Tabelas.TABLE_NAME2,whereClause,null);

        if (newRowId==-1){
            Toast.makeText(contexts, "Erro ao deletar disciplina", Toast.LENGTH_SHORT).show();
        }else{
            Toast.makeText(contexts, "disciplina deletada", Toast.LENGTH_SHORT).show();
        }

    }

}
