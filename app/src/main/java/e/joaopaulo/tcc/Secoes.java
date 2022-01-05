package e.joaopaulo.tcc;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.ArrayList;
import java.util.List;

public class Secoes extends FragmentPagerAdapter {

    private final List<Fragment> listaDeFragmentos = new ArrayList<>();
    private final List<String> listaDeTitulos = new ArrayList<>();

    public void adicionaFragmento(Fragment fragmento, String titulo) {
        listaDeFragmentos.add(fragmento);
        listaDeTitulos.add(titulo);
    }

    public Secoes(FragmentManager fm) {
        super(fm);
    }

    @Override
    public CharSequence getPageTitle(int posicao) {

        return listaDeTitulos.get(posicao);
    }

    @Override
    public Fragment getItem(int posicao) {
        return listaDeFragmentos.get(posicao);
    }

    @Override
    public int getCount() {
        return listaDeFragmentos.size();
    }
}