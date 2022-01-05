package e.joaopaulo.tcc;

import android.content.Intent;
import android.database.Cursor;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;

public class MateriaECards extends AppCompatActivity {
String nome_materia,nome_disciplina;
    FloatingActionButton btAddCards;
    Button btMostraCardsAleatoriamente;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_materia_e_cards);
        Intent intent=getIntent();
        Bundle budle = intent.getExtras();
        nome_materia=budle.getString("nome_materia");
        nome_disciplina=budle.getString("nome_disciplina");
        Log.v("lalal",nome_materia);
        setTitle("Cards de "+nome_materia);
       // listarCards();
        btAddCards = (FloatingActionButton)findViewById(R.id.botaoAddCard);
        btMostraCardsAleatoriamente = (Button) findViewById(R.id.botaoCardsAleatoriamente);

        btAddCards.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Lista listasDisciplina = new Lista();
                final Cursor cursor = listasDisciplina.listarInicial(MateriaECards.this, Contract.Tabelas.TABLE_NAME4);
                Intent intent = new Intent(MateriaECards.this, EditorCardsDeMaterias.class);
                Bundle bundle=new Bundle();
                intent.putExtra("nome_materia",nome_materia);
                intent.putExtra("nome_discipina",nome_disciplina);

                intent.putExtras(bundle);
                startActivity(intent);

            }
        });

     /*   btMostraTodosCards.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
             //   Lista listasDisciplina = new Lista();
             //   final Cursor cursor = listasDisciplina.listarInicial(MateriaECards.this, Contract.Tabelas.TABLE_NAME4);
                Intent intent = new Intent(MateriaECards.this, CardsDeUmaMateria.class);
                Bundle bundle=new Bundle();
                intent.putExtra("nome_materia",nome_materia);
                intent.putExtra("nome_discipina",nome_disciplina);

                intent.putExtras(bundle);
                startActivity(intent);

            }
        });*/
        btMostraCardsAleatoriamente.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //   Lista listasDisciplina = new Lista();
                //   final Cursor cursor = listasDisciplina.listarInicial(MateriaECards.this, Contract.Tabelas.TABLE_NAME4);
                Intent intent = new Intent(MateriaECards.this, MostrarCardsAleatoriamente.class);
                Bundle bundle=new Bundle();
                intent.putExtra("nome_materia",nome_materia);
                intent.putExtra("nome_discipina",nome_disciplina);

                intent.putExtras(bundle);
                startActivity(intent);

            }
        });
        listarCards();


    }
    @Override
    public void onStart() {
        super.onStart();
        listarCards();
    }
    //ESSE ON START E ON RESUME SERVEM PARA CONSEGUIR ATUALIZAR A LISTA, PQ QUANDO VC CRIA UMA NOVA
    //REVISAO AI NA HORA QUE VOLTA ELE PRECISA ATUALIZAR

    @Override
    public void onResume() {
        super.onResume();
        listarCards();
    }


    public void listarCards() {
        Lista listasCard = new Lista();
        final Cursor cursor = listasCard.listaCardsMaterias(this, Contract.Tabelas.TABLE_NAME3,nome_materia); // AQUI EU FIZ PARA SEPARAR O BD DA LISTA, ENTAO EU CRIO
        //UM OBJETO E CHAMO ELE PASSANDO O CONTEXTO. ELE ME RETORNA O CURSOR COM TUDO QUE ESTA NO BD
        //Find ListView to populate
        final ListView lvItems = (ListView)findViewById(R.id.lista_cards);

        final CardsCursorAdapter cardCursorAdapter = new CardsCursorAdapter(this, cursor);

        lvItems.setAdapter(cardCursorAdapter);

    }
}
