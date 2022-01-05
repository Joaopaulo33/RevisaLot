package e.joaopaulo.tcc;

import android.provider.BaseColumns;

public class Contract {

    private Contract(){}

    public static class Tabelas implements BaseColumns {
        public static final String _ID = BaseColumns._ID;

        //revisoes
        public static final String TABLE_NAME = "revisoes";
        public static final String COLUNA_ASSUNTO = "assunto";
        public static final String COLUNA_MATERIA = "materia";
        public static final String COLUNA_QTD_REVISOES_FEITAS = "revisoes_feitas";
        public static final String COLUNA_DATA_CRIACAO = "data_criacao";
        public static final String COLUNA_DATA_PROXIMA_REVISAO = "data_proxima_revisao";
        public static final String COLUNA_DIAS_PRA_PROXIMA_REVISAO = "dias_proxima_revisao";
        public static final String COLUNA_NIVEL_DE_CONSOLIDACAO = "nivel_de_consolidacao";




        //diciplina
        public static final String TABLE_NAME2 = "disciplinas";
        public static final String COLUNA_DISCIPLINA = "disciplina";
        public static final String COLUNA_QTD_CARDS = "qtd_cards";
        public static final String COLUNA_QTD_REVISOES = "qtd_revisoes";


        //cards
        public static final String TABLE_NAME3 = "cards";
        public static final String COLUNA_TEXTO_CARD = "texto_card";
        public static final String COLUNA_ID_DISCIPLINA = "id_disciplina";
        public static final String COLUNA_FOTO = "foto";
        public static final String COLUNA_TEM_FOTO = "tem_foto";



        //materias
        public static final String TABLE_NAME4 = "materias";





        //SQL para criação das tabelas
        //resvisoes
        public static  final String SQL_CREATE_REVISOES_TABLE =  "CREATE TABLE " + TABLE_NAME + " ("
                + _ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + COLUNA_ASSUNTO + " TEXT, "
                + COLUNA_MATERIA + " TEXT ,"
                + COLUNA_DISCIPLINA + " TEXT, "
                + COLUNA_ID_DISCIPLINA + " TEXT, "
                + COLUNA_QTD_REVISOES_FEITAS + " INTEGER, "
                + COLUNA_DATA_CRIACAO + " TEXT, "
                + COLUNA_DATA_PROXIMA_REVISAO + " TEXT, "
                + COLUNA_DIAS_PRA_PROXIMA_REVISAO + " INTEGER, "
                + COLUNA_NIVEL_DE_CONSOLIDACAO + " INTEGER);";



        //Diciplinas
        public static  final String SQL_CREATE_DISCIPINAS_TABLE=  "CREATE TABLE " + TABLE_NAME2 + " ("
                + _ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + COLUNA_DISCIPLINA + " TEXT, "
                + COLUNA_QTD_CARDS + " INTEGER, "
                + COLUNA_QTD_REVISOES + " INTEGER);";

        //cards
        public static  final String SQL_CREATE_CARDS_TABLE =  "CREATE TABLE " + TABLE_NAME3 + " ("
                + _ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + COLUNA_TEXTO_CARD + " TEXT, "
                + COLUNA_ID_DISCIPLINA + " INTEGER ,"
                + COLUNA_ASSUNTO + " TEXT, "
                + COLUNA_DISCIPLINA + " TEXT, "
                + COLUNA_MATERIA + " TEXT, "
                + COLUNA_FOTO + " blob, "
                + COLUNA_TEM_FOTO + " INTEGER);";

        //Materias
        public static  final String SQL_CREATE_MATERIAS_TABLE =  "CREATE TABLE " + TABLE_NAME4 + " ("
                + _ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + COLUNA_MATERIA + " TEXT, "
                + COLUNA_ID_DISCIPLINA + " INTEGER);";
    }
}
