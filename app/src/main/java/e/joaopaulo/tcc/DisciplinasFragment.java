package e.joaopaulo.tcc;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;


public class DisciplinasFragment extends Fragment {

    FloatingActionButton btAddDisciplinas;
    View view;
    //Objeto SQLiteOpenHelper


    @Override
    public void onStart() {
        super.onStart();
        listarDisciplinas();
    }
    //ESSE ON START E ON RESUME SERVEM PARA CONSEGUIR ATUALIZAR A LISTA, PQ QUANDO VC CRIA UMA NOVA
    //REVISAO AI NA HORA QUE VOLTA ELE PRECISA ATUALIZAR

    @Override
    public void onResume() {
        super.onResume();
        listarDisciplinas();
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.disciplinas_fragment, container, false);
        btAddDisciplinas = (FloatingActionButton) view.findViewById(R.id.botaoAddDisciplina);

        btAddDisciplinas.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), EditorDisciplina.class);
                startActivity(intent);
           }
        });
        listarDisciplinas();
        return view;

    }

    public void listarDisciplinas() {
        Lista listasDisciplina = new Lista();
        final Cursor cursor = listasDisciplina.listarInicial(getContext(), Contract.Tabelas.TABLE_NAME2); // AQUI EU FIZ PARA SEPARAR O BD DA LISTA, ENTAO EU CRIO
        //UM OBJETO E CHAMO ELE PASSANDO O CONTEXTO. ELE ME RETORNA O CURSOR COM TUDO QUE ESTA NO BD
        //Find ListView to populate
        final ListView lvItems = (ListView) view.findViewById(R.id.lista_disciplinas);

        final DisciplinasCursorAdapter disciplinaCursorAdapter = new DisciplinasCursorAdapter(getContext(), cursor);

        lvItems.setAdapter(disciplinaCursorAdapter);
        disciplinaCursorAdapter.notifyDataSetChanged();
        lvItems.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
             Intent intent = new Intent(getContext(), Materia.class);
                Bundle bundle=new Bundle();
                intent.putExtra("nome_disciplina",cursor.getString(1));
                intent.putExtra("id",cursor.getInt(0));
                intent.putExtras(bundle);
                startActivity(intent);

            }
        });

  }
}