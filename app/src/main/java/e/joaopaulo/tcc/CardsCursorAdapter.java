package e.joaopaulo.tcc;

import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.ByteArrayInputStream;

public class CardsCursorAdapter extends CursorAdapter {

    private LayoutInflater cursorInflater;
    Context contexts;

    String idCard;


    public CardsCursorAdapter(final Context context, Cursor c) {
        super(context, c, 0);
        contexts = context;
        cursorInflater = (LayoutInflater) context.getSystemService(
                Context.LAYOUT_INFLATER_SERVICE);

    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {

        return LayoutInflater.from(context).inflate(R.layout.item_card, parent, false);
    }

    @Override
    public void bindView(View view, final Context context, Cursor cursor) {
        // Find fields to populate in inflated template
        TextView tvAssunto = (TextView) view.findViewById(R.id.assunto_textView);
        TextView tvTexto = (TextView) view.findViewById(R.id.texto_textView);
        TextView tvDisciplina = (TextView) view.findViewById(R.id.disciplina_textView);
        TextView tvMateria = (TextView) view.findViewById(R.id.materia_textView);
        ImageView imFoto = (ImageView) view.findViewById(R.id.imageCard);


   //     ImageView buttonExclui = (ImageView) view.findViewById(R.id.excluir_button);

        final String id = cursor.getString(cursor.getColumnIndexOrThrow(Contract.Tabelas._ID));
        idCard = id;
       // buttonExclui.setVisibility(View.INVISIBLE);
       // buttonExclui.setOnClickListener(new View.OnClickListener() {
        //    @Override
      //      public void onClick(View v) {
         //       excluirCard = builder.create();
                //Exibe
          //      excluirCard.show();


       //     }
      //  });

        // Extract properties from cursor
        String assunto = cursor.getString(cursor.getColumnIndexOrThrow(Contract.Tabelas.COLUNA_ASSUNTO));
        String texto = cursor.getString(cursor.getColumnIndexOrThrow(Contract.Tabelas.COLUNA_TEXTO_CARD));
        String disciplina = cursor.getString(cursor.getColumnIndexOrThrow(Contract.Tabelas.COLUNA_DISCIPLINA));
        String materia = cursor.getString(cursor.getColumnIndexOrThrow(Contract.Tabelas.COLUNA_MATERIA));
        int temFoto = cursor.getInt(cursor.getColumnIndexOrThrow(Contract.Tabelas.COLUNA_TEM_FOTO));

        if (temFoto == 1) {
            Bitmap bitmap;
            byte[] blob;
            blob = cursor.getBlob(cursor.getColumnIndexOrThrow(Contract.Tabelas.COLUNA_FOTO));

            if (blob != null) {
                ByteArrayInputStream bais = new ByteArrayInputStream(blob);
                bitmap = BitmapFactory.decodeStream(bais);
                Matrix matrix = new Matrix();
                matrix.postRotate(90);
                Bitmap rotated = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
                imFoto.setImageBitmap(rotated);
                imFoto.setVisibility(view.VISIBLE);

            }
        } else {
            imFoto.setVisibility(view.GONE);
        }
        tvAssunto.setText(assunto);
        tvTexto.setText(texto);
        tvDisciplina.setText(disciplina);

        if (materia.equals("Selecione")) {
            tvMateria.setVisibility(view.GONE);
            // tvMateria.setText("");
        } else {
            tvMateria.setVisibility(view.VISIBLE);
            tvMateria.setText(materia);

        }
    }

    private void deletarCard(String id) {
        DbHelper mDbHelper = new DbHelper(contexts);
        SQLiteDatabase db = mDbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        String querySql2 = "SELECT disciplina FROM cards WHERE " + Contract.Tabelas._ID + "=" + id;
        String disciplina = "";
        Cursor cursor = db.rawQuery(querySql2, null);
        cursor.moveToFirst();
        if (cursor.getCount() > 0) {
            disciplina = cursor.getString(cursor.getColumnIndex(Contract.Tabelas.COLUNA_DISCIPLINA));
            Log.v("chatuba1", disciplina + "");

            String querySql = "SELECT qtd_cards FROM disciplinas WHERE " + Contract.Tabelas.COLUNA_DISCIPLINA + "=" + "'" + disciplina + "'";
            cursor = db.rawQuery(querySql, null);
            cursor.moveToFirst();
        }
        if (cursor.getCount() > 0) {
            int qtdcards = cursor.getInt(cursor.getColumnIndex(Contract.Tabelas.COLUNA_QTD_CARDS));

            values.put(Contract.Tabelas.COLUNA_QTD_CARDS, qtdcards - 1);

            String whereClause = Contract.Tabelas.COLUNA_DISCIPLINA + "=" + "'" + disciplina + "'";
            long newRowId = db.update(Contract.Tabelas.TABLE_NAME3, values, whereClause, null);
        }
        db = mDbHelper.getWritableDatabase();
        // Create and/or open a database to write from it

        String whereClause2 = Contract.Tabelas._ID + "=" + id;

        long newRowId2 = db.delete(Contract.Tabelas.TABLE_NAME, whereClause2, null);
        if (newRowId2==-1){
            Toast.makeText(contexts, "Erro ao deletar card", Toast.LENGTH_SHORT).show();
        }else{
            Toast.makeText(contexts, "card deletado", Toast.LENGTH_SHORT).show();


        }
        mDbHelper.close();

        db.close();

    }
}
