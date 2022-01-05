package e.joaopaulo.tcc;

import android.content.Intent;
import android.database.Cursor;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ListView;

public class CardsDeUmaDisciplina extends AppCompatActivity {
String nome_disciplina;
String id_disciplina;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cards_de_uma_disciplina);
        Intent intent=getIntent();
        Bundle budle = intent.getExtras();
        nome_disciplina=budle.getString("nome_disciplina");
        id_disciplina=budle.getString("id_disciplina");
        setTitle("Cards de "+nome_disciplina);
        listarCards();

    }
    @Override
    public void onStart(){
        super.onStart();
        listarCards();
    }
    //ESSE ON START E ON RESUME SERVEM PARA CONSEGUIR ATUALIZAR A LISTA, PQ QUANDO VC CRIA UMA NOVA
    //REVISAO AI NA HORA QUE VOLTA ELE PRECISA ATUALIZAR

    @Override
    public void onResume(){
        super.onResume();
        listarCards();
    }
    public void listarCards() {
        Lista listasCard = new Lista();
        final Cursor cursor = listasCard.listaCardsDisciplinas(this, Contract.Tabelas.TABLE_NAME3,nome_disciplina); // AQUI EU FIZ PARA SEPARAR O BD DA LISTA, ENTAO EU CRIO
        //UM OBJETO E CHAMO ELE PASSANDO O CONTEXTO. ELE ME RETORNA O CURSOR COM TUDO QUE ESTA NO BD
        //Find ListView to populate
        final ListView lvItems = (ListView)findViewById(R.id.lista_cards);

        final CardsCursorAdapter cardCursorAdapter = new CardsCursorAdapter(this, cursor);

        lvItems.setAdapter(cardCursorAdapter);

    }
}
