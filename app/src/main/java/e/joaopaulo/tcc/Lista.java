package e.joaopaulo.tcc;


import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.text.SimpleDateFormat;
import java.util.Calendar;

import static e.joaopaulo.tcc.Contract.Tabelas.COLUNA_DIAS_PRA_PROXIMA_REVISAO;
import static e.joaopaulo.tcc.Contract.Tabelas._ID;
import static java.lang.Integer.parseInt;


public class Lista {
    //public DbHelper mdbHelper_main;
    public Cursor cursor;


    public Cursor listarInicial(Context context,String nomeTabela) {

        DbHelper mDbHelper = new DbHelper(context);

        SQLiteDatabase db = mDbHelper.getReadableDatabase();

        cursor = db.query(nomeTabela, null, null, null, null, null, _ID+"   DESC");
        cursor.moveToFirst();
        return cursor;
    }
    public Cursor listaDeRevisoes(Context context,String nomeTabela) {

        DbHelper mDbHelper = new DbHelper(context);
        Calendar calendar=Calendar.getInstance();
        SimpleDateFormat sdf=new SimpleDateFormat("dd/MM/yyyy");
        SQLiteDatabase db = mDbHelper.getReadableDatabase();
        DbHelper.converteStringPraCalendar(Contract.Tabelas.COLUNA_DATA_PROXIMA_REVISAO,sdf);
        cursor = db.query(nomeTabela, null, null, null, null, null, COLUNA_DIAS_PRA_PROXIMA_REVISAO+"   ASC");
        cursor.moveToFirst();
        return cursor;
    }

    public Cursor listaDeMaterias(Context context,String nomeTabela,Integer id) {

        DbHelper mDbHelper = new DbHelper(context);

        SQLiteDatabase db = mDbHelper.getReadableDatabase();

        String select = Contract.Tabelas.COLUNA_ID_DISCIPLINA + " = ?";
        cursor = db.query(nomeTabela, new String[] {"*"}, select, new String[] {id.toString()},null, null, null);

        cursor.moveToFirst();
        return cursor;
    }
    public Cursor listaCardsMaterias(Context context,String nomeTabela,String materia) {

        DbHelper mDbHelper = new DbHelper(context);

        SQLiteDatabase db = mDbHelper.getReadableDatabase();

        String select = Contract.Tabelas.COLUNA_MATERIA + " = ?";
        cursor = db.query(nomeTabela, new String[] {"*"}, select, new String[] {materia},null, null, _ID+"   DESC");

        cursor.moveToFirst();
        return cursor;
    }
    public Cursor listaCardsDisciplinas(Context context,String nomeTabela,String disciplina) {

        DbHelper mDbHelper = new DbHelper(context);

        SQLiteDatabase db = mDbHelper.getReadableDatabase();

        String select = Contract.Tabelas.COLUNA_DISCIPLINA + " = ?";
        cursor = db.query(nomeTabela, new String[] {"*"}, select, new String[] {disciplina},null, null, _ID+"   DESC");

        cursor.moveToFirst();
        return cursor;
    }

    public Cursor listaCompleta(Context context , String nomeTabela, String id){

        DbHelper mDbHelper = new DbHelper(context);

        SQLiteDatabase db = mDbHelper.getReadableDatabase();

        String select = Contract.Tabelas._ID + " = ?";
        cursor = db.query(nomeTabela, new String[] {"*"}, select, new String[] {id.toString()},null, null, null);

        cursor.moveToFirst();
        return cursor;

    }

}