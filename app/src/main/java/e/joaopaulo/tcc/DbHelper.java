package e.joaopaulo.tcc;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

public class DbHelper extends SQLiteOpenHelper {
    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "datfas.db";
 //   ArrayList<String> listDiscipinas = new ArrayList<String>();


    public DbHelper(Context context) {

        super(context, DATABASE_NAME, null, DATABASE_VERSION);

    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // Executa O SQL para criação da tabela no banco de dados

        db.execSQL(Contract.Tabelas.SQL_CREATE_REVISOES_TABLE);

        db.execSQL(Contract.Tabelas.SQL_CREATE_DISCIPINAS_TABLE);

        db.execSQL(Contract.Tabelas.SQL_CREATE_CARDS_TABLE);

        db.execSQL(Contract.Tabelas.SQL_CREATE_MATERIAS_TABLE);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }



    public ArrayList<String> getTodasDiciplinas() {
        ArrayList<String> list = new ArrayList<String>();
        list.add("Selecione");
        // Open the database for reading
        SQLiteDatabase db = this.getReadableDatabase();
        // Start the transaction.
        db.beginTransaction();
        try {
            String selectQuery = "SELECT " + Contract.Tabelas.COLUNA_DISCIPLINA + " FROM " + Contract.Tabelas.TABLE_NAME2;

            Cursor cursor = db.rawQuery(selectQuery, null);
            if (cursor.getCount() > 0) {
                while (cursor.moveToNext()) {
                    // Add province name to arraylist
                    String disciplina = cursor.getString(cursor.getColumnIndex("disciplina"));
                    list.add(disciplina);

                }
            }
            db.setTransactionSuccessful();

        } catch (SQLiteException e) {
            e.printStackTrace();

        } finally {
            db.endTransaction();
            // End the transaction.
            db.close();

            // Close database
        }
        return list;


    }







    public ArrayList<String> getTodasMaterias(String disciplinaDaMateria) {
        ArrayList<String> list = new ArrayList<String>();
            list.add("Selecione");



        SQLiteDatabase db = this.getReadableDatabase();
        //create the object no be included

        String querySql = "SELECT _id FROM disciplinas WHERE " + Contract.Tabelas.COLUNA_DISCIPLINA + "=" + "'" + disciplinaDaMateria + "'";
        Cursor cursor = db.rawQuery(querySql, null);
        cursor.moveToFirst();
        int id=0;
        if(cursor.getCount()>0)
             id = cursor.getInt(cursor.getColumnIndex(Contract.Tabelas._ID));

        db.beginTransaction();


        try {


            String selectQuery =  "SELECT "+Contract.Tabelas.COLUNA_MATERIA+" FROM materias WHERE " + Contract.Tabelas.COLUNA_ID_DISCIPLINA + "=" +id;


             cursor = db.rawQuery(selectQuery, null);

            if (cursor.getCount() > 0) {
                while (cursor.moveToNext()) {
                    // Add province name to arraylist
                    String materia = cursor.getString(cursor.getColumnIndex("materia"));

                    list.add(materia);

                }


            }
            cursor.moveToFirst();

            db.setTransactionSuccessful();

        } catch (SQLiteException e) {
            e.printStackTrace();

        } finally {
            db.endTransaction();
            // End the transaction.
            db.close();


            // Close database
        }
        return list;


    }


    public static Calendar converteStringPraCalendar(String data,SimpleDateFormat sdf) {

        try {

            Calendar calendar = Calendar.getInstance();
            if (data!=null)
            calendar.setTime(sdf.parse(data));
            return calendar;
        } catch (ParseException e) {
            e.printStackTrace();

        }


return null;
    }


}