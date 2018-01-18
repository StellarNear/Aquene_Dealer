package stellarnear.aquene_dealer.Activities;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.pm.ActivityInfo;
import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.Arrays;
import java.util.List;

import stellarnear.aquene_dealer.Perso.Perso;
import stellarnear.aquene_dealer.R;

/**
 * A placeholder fragment containing a simple view.
 */
public class MainActivityFragment extends Fragment {
    Perso aquene = MainActivity.aquene;
    View returnFragView;
    public MainActivityFragment() {
    }

    @Override
    public View onCreateView(final LayoutInflater inflater,final ViewGroup container,
                             Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (container != null) {
            container.removeAllViews();
        }

        returnFragView= inflater.inflate(R.layout.fragment_main, container, false);

        ImageButton fabCombat = (ImageButton) returnFragView.findViewById(R.id.button_frag_to_combat);
        setButtonActivity(fabCombat,new MainActivityFragmentCombat());

        ImageButton fabKi = (ImageButton) returnFragView.findViewById(R.id.button_frag_to_ki);
        setButtonActivity(fabKi, new MainActivityFragmentKi());

        ImageButton fabSkill = (ImageButton) returnFragView.findViewById(R.id.button_frag_to_skill);
        setButtonActivity(fabSkill,new MainActivityFragmentSkill());

        buildStatList();

        return returnFragView;
    }

    private void buildStatList() {
        String[] baseAbi = {"FOR", "DEX", "CON","INT","SAG","CHA"};  //Q1
        String[] baseAbiTxt = {"Force", "Dextérité", "Constitution","Intelligence","Sagesse","Charisme"};  //Q1
        LinearLayout quadrant1 = returnFragView.findViewById(R.id.main_frag_stats_quadrant1);
        injectStats(baseAbi,baseAbiTxt,quadrant1);

        String[] baseStat    = {"HP","LVL","MS","HEROIC"};//Q2
        String[] baseStatTxt = {"Points de vie","Niveau","Vitesse de déplacement","Points héroiques"};//Q2
        LinearLayout quadrant2 = returnFragView.findViewById(R.id.main_frag_stats_quadrant2);
        injectStats(baseStat,baseStatTxt,quadrant2);

        String[] defStat=      {"CA","REF","VIG","VOL","RM","REDUC","REGEN"};//Q3
        String[] defStatTxt=   {"Classe d'armure","Reflexe","Vigeur","Volonté","Résistance magie","Réduction (/chaotique)","Régénération"};//Q3
        LinearLayout quadrant3 = returnFragView.findViewById(R.id.main_frag_stats_quadrant3);
        injectStats(defStat,defStatTxt,quadrant3);

        String[] advanceStat=  {"BMO","DMD","INIT","BBA", "KI"};// Q4
        String[] advanceStatTxt= {"Bonus de manoeuvre offensive","Degré de manoeuvre défensive","Initiative","Bonus de base à l'attaque", "Réserve de Ki"};// Q4
        LinearLayout quadrant4 = returnFragView.findViewById(R.id.main_frag_stats_quadrant4);
        injectStats(advanceStat,advanceStatTxt,quadrant4);

    }

    private void injectStats(String[] stat, String[] statTxt, LinearLayout quadrant) {
        for (int i=0;i<stat.length ;i++){
            TextView text = new TextView(getContext());
            String txt = statTxt[i]+" : "+ aquene.getAbilities().getScore(stat[i]);
            text.setText(txt);
            quadrant.addView(text);
        }
    }

    private void setButtonActivity(ImageButton button, final Fragment ActivityFragment) {
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                lockOrient();
                FragmentManager fragmentManager = getActivity().getFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.fragment_main_frame_layout, ActivityFragment);
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();
            }
        });
    }


    private void lockOrient() {
        getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
    }

}
