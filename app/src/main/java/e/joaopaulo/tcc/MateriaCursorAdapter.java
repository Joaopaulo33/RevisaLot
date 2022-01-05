package e.joaopaulo.tcc;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.TextView;

public class MateriaCursorAdapter extends CursorAdapter {
    private LayoutInflater cursorInflater;

    public MateriaCursorAdapter(Context context, Cursor c) {
        super(context, c, 0);
        cursorInflater = (LayoutInflater) context.getSystemService(
                Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {

        return LayoutInflater.from(context).inflate(R.layout.item_materia_list, parent, false);
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        TextView tvMateria = (TextView) view.findViewById(R.id.materia_textView);
        // Extract properties from cursor
        String materia = cursor.getString(cursor.getColumnIndexOrThrow(Contract.Tabelas.COLUNA_MATERIA));
        tvMateria.setText(materia);


    }
}