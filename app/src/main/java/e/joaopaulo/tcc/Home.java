package e.joaopaulo.tcc;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.NavUtils;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

public class Home extends AppCompatActivity {
    private static final String TAG = "Experencias";
    private Secoes secoes;
    private ViewPager viewPager;
    private AlertDialog informacoes;
     AlertDialog.Builder builder ;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        Log.d(TAG, "onCreate: Starting");

        secoes = new Secoes(getSupportFragmentManager());

        viewPager = (ViewPager) findViewById(R.id.container);
        setupViewPager(viewPager);

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabLayout);
        tabLayout.setupWithViewPager(viewPager);

        getSupportActionBar().setElevation(0);


        //aleta dialog
         builder = new AlertDialog.Builder(this);
        //define o titulo
          builder.setTitle("Informações");

        //define a mensagem
          builder.setMessage("Ao criar uma revisão os prazos predefinidos para realização das próximas revisões são de 1-7-15-30 dias, após esse ciclo as revisões passam a ser de 30 em 30 dias, a menos que você reinicie a revisão criada ! ");
        //define um botão como positivo
         builder.setNegativeButton("OK", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface arg0, int arg1) {
           }
          });
    }

    public void setupViewPager(ViewPager viewPager) {
        Secoes adaptador = new Secoes(getSupportFragmentManager());
        adaptador.adicionaFragmento(new RevisoesFragment(), "Revisões");
        adaptador.adicionaFragmento(new DisciplinasFragment(), "Disciplinas");
        adaptador.adicionaFragmento(new CardsFragment(), "Cards");
        viewPager.setAdapter(adaptador);
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu options from the res/menu/menu_editor.xml file.
        // This adds menu items to the app bar.
        getMenuInflater().inflate(R.menu.menu_informacao, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // User clicked on a menu option in the app bar overflow menu
        switch (item.getItemId()) {
            // Respond to a click on the "Save" menu option
            case R.id.botao_info:
                 informacoes = builder.create();
                //Exibe
                informacoes.show();
                return true;

            case android.R.id.home:
                // Navigate back to parent activity (CatalogActivity)
                NavUtils.navigateUpFromSameTask(this);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

}