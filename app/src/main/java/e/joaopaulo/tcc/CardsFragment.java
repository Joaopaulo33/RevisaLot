package e.joaopaulo.tcc;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;


public class CardsFragment extends Fragment {

    FloatingActionButton btAddCards;
    View view;
    //Objeto SQLiteOpenHelper


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public void onStart() {
        super.onStart();
        listarFragmentCards();
    }
    //ESSE ON START E ON RESUME SERVEM PARA CONSEGUIR ATUALIZAR A LISTA, PQ QUANDO VC CRIA UMA NOVA
    //REVISAO AI NA HORA QUE VOLTA ELE PRECISA ATUALIZAR

    @Override
    public void onResume() {
        super.onResume();
        listarFragmentCards();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.cards_fragment, container, false);
        btAddCards = (FloatingActionButton) view.findViewById(R.id.botaoAddCard);

        btAddCards.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), EditorCards.class);
                startActivity(intent);
            }
        });
        listarFragmentCards();

        return view;

    }

    public void listarFragmentCards() {
        Lista listasCard = new Lista();
        final Cursor cursor = listasCard.listarInicial(getContext(), Contract.Tabelas.TABLE_NAME3); // AQUI EU FIZ PARA SEPARAR O BD DA LISTA, ENTAO EU CRIO
        //UM OBJETO E CHAMO ELE PASSANDO O CONTEXTO. ELE ME RETORNA O CURSOR COM TUDO QUE ESTA NO BD
        //Find ListView to populate
        final ListView lvItems = (ListView) view.findViewById(R.id.lista_fragment_cards);

        final CardsCursorAdapter cardCursorAdapter = new CardsCursorAdapter(getContext(), cursor);

        lvItems.setAdapter(cardCursorAdapter);

        lvItems.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {


                Intent intent = new Intent(getContext(), CardCompleto.class);
                Bundle bundle=new Bundle();
                intent.putExtra("id",cursor.getString(0));
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });
       /* lvItems.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //   Cursor atual = (Cursor)FragmentsCardsCursorAdapter.getItem(position);
                //String _id = atual.getString(atual.getColumnIndexOrThrow(Contract.Tabelas._ID));
                Intent intent = new Intent(getContext(), Card.class);
                Bundle bundle=new Bundle();
                intent.putExtra("id_disciplina",cursor.getInt(0));
                intent.putExtras(bundle);
                startActivity(intent);

            }
        });
*/
    }

}