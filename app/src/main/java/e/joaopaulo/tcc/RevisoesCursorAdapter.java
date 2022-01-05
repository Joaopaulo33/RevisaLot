package e.joaopaulo.tcc;

import android.content.Context;
import android.database.Cursor;
import android.graphics.Color;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Calendar;

import static android.view.View.*;

public class RevisoesCursorAdapter extends CursorAdapter {
    private LayoutInflater cursorInflater;
    public RevisoesCursorAdapter(Context context, Cursor c) {
        super(context, c, 0);
        cursorInflater = (LayoutInflater) context.getSystemService(
                Context.LAYOUT_INFLATER_SERVICE);


    }
    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {

        return LayoutInflater.from(context).inflate(R.layout.item_revisoes_list, parent, false);
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {



        // Find fields to populate in inflated template
        TextView tvAssunto = (TextView) view.findViewById(R.id.assunto_textView);
        TextView tvDisciplina = (TextView) view.findViewById(R.id.disciplina_textView);
        TextView tvData = (TextView) view.findViewById(R.id.data_textView);
        TextView tvMateria = (TextView) view.findViewById(R.id.materia_textView);
        TextView data_textView = (TextView) view.findViewById(R.id.data_textView);

        CardView principal=(CardView) view.findViewById(R.id.principal);



        //calendar
        Calendar calendar;
        SimpleDateFormat sdf=new SimpleDateFormat("dd/MM/yyyy");



        // Extract properties from cursor
      //  String assunto = cursor.getString(cursor.getColumnIndexOrThrow(Contract.Tabelas.COLUNA_ASSUNTO));
        String disciplina = cursor.getString(cursor.getColumnIndexOrThrow(Contract.Tabelas.COLUNA_DISCIPLINA));
        String dataProximaRevisao=cursor.getString(cursor.getColumnIndexOrThrow(Contract.Tabelas.COLUNA_DATA_PROXIMA_REVISAO));
        String materia=cursor.getString(cursor.getColumnIndexOrThrow(Contract.Tabelas.COLUNA_MATERIA));
        int diasPraproximaRevisao=cursor.getInt(cursor.getColumnIndexOrThrow(Contract.Tabelas.COLUNA_DIAS_PRA_PROXIMA_REVISAO));

        calendar=DbHelper.converteStringPraCalendar(dataProximaRevisao,sdf);

     //   tvAssunto.setText(assunto);
        tvDisciplina.setText(disciplina);
        tvData.setText(sdf.format(calendar.getTime()));
        Log.v("prima",diasPraproximaRevisao+"");
        data_textView.setTextColor(Color.parseColor("#e220741e"));

        if(diasPraproximaRevisao > 100){
    Log.v("prima1",diasPraproximaRevisao+"");

            data_textView.setTextColor(Color.parseColor("#e2ba171f"));

}
        if(materia.equals("Selecione")) {
           tvMateria.setText("");
        }else{
            tvMateria.setText(materia);
        }

    }
}
