package stellarnear.aquene_dealer.Divers;

import android.app.Activity;
import android.content.Context;
import android.content.pm.ActivityInfo;
import android.os.Handler;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.ViewSwitcher;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import stellarnear.aquene_dealer.Activities.MainActivity;
import stellarnear.aquene_dealer.Perso.Perso;
import stellarnear.aquene_dealer.R;

/**
 * Created by jchatron on 19/01/2018.
 */

public class QuadrantFiller {
    private Context mC;
    private Activity mA;
    private View mainView;
    private LinearLayout quadrant1;
    private LinearLayout quadrant2;
    private LinearLayout quadrant3;
    private LinearLayout quadrant4;
    private Perso aquene = MainActivity.aquene;
    private Map<LinearLayout,String> mapLayoutTextTitle =new HashMap<>();
    private List<LinearLayout> quadrantList;
    private ViewSwitcher viewSwitcher;
    private ImageButton imgExit;
    LinearLayout quadrantFullSub1;
    LinearLayout quadrantFullSub2;
    private boolean fullscreen=false;
    View fullView;
    public QuadrantFiller(View mainView, Context mC, Activity mA) {
        this.mC=mC;
        this.mA=mA;
        this.mainView=mainView;
        viewSwitcher=mainView.findViewById(R.id.viewSwitcherQuadrant);

        LayoutInflater inflater = (LayoutInflater) mC.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        fullView =(View) inflater.inflate(R.layout.quadrant_full, null);
        viewSwitcher.addView(fullView);
        quadrantFullSub1 = fullView.findViewById(R.id.quadrant_full_sub1);
        quadrantFullSub2 = fullView.findViewById(R.id.quadrant_full_sub2);
        imgExit = fullView.findViewById(R.id.button_quadrant_full_exit);
        imgExit.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                switchViewPrevious();
            }
        });

        quadrant1 = mainView.findViewById(R.id.main_frag_stats_quadrant1);
        quadrant2 = mainView.findViewById(R.id.main_frag_stats_quadrant2);
        quadrant3 = mainView.findViewById(R.id.main_frag_stats_quadrant3);
        quadrant4 = mainView.findViewById(R.id.main_frag_stats_quadrant4);
        mapLayoutTextTitle.put(quadrant1,mC.getResources().getString(R.string.quadrantQ1Title));
        mapLayoutTextTitle.put(quadrant2,mC.getResources().getString(R.string.quadrantQ2Title));
        mapLayoutTextTitle.put(quadrant3,mC.getResources().getString(R.string.quadrantQ3Title));
        mapLayoutTextTitle.put(quadrant4,mC.getResources().getString(R.string.quadrantQ4Title));

        quadrantList= Arrays.asList(quadrant1, quadrant2, quadrant3, quadrant4);

        buildAllMini();
    }

    private void buildAllMini() {
        buildMiniQ1();
        buildMiniQ2();
        buildMiniQ3();
        buildMiniQ4();
    }

    private void buildMiniQ1() {
        String[] baseAbi = {"FOR", "DEX", "CON","INT","SAG","CHA"};  //Q1
        String[] baseAbiTxt = {"Force", "Dextérité", "Constitution","Intelligence","Sagesse","Charisme"};  //Q1
        LinearLayout quadrantSub1 = mainView.findViewById(R.id.main_frag_stats_quadrant1_1);
        LinearLayout quadrantSub2 = mainView.findViewById(R.id.main_frag_stats_quadrant1_2);
        injectBaseStats(baseAbi,baseAbiTxt,quadrantSub1,quadrantSub2);
    }
    private void buildMiniQ2() {
        String[] baseStat    = {"HP","LVL","MS","HEROIC"};//Q2
        //String[] baseStatTxt = {"Points de vie","Niveau","Vitesse de déplacement","Points héroiques"};//Q2
        String[] baseStatTxt = {"Points de vie","Niveau","Déplacement","Points héroiques"};//Q2
        LinearLayout quadrantSub1 = mainView.findViewById(R.id.main_frag_stats_quadrant2_1);
        LinearLayout quadrantSub2 = mainView.findViewById(R.id.main_frag_stats_quadrant2_2);
        injectStats(baseStat,baseStatTxt,quadrantSub1,quadrantSub2);

    }
    private void buildMiniQ3() {
        String[] defStat=      {"CA","REF","VIG","VOL","RM","REDUC","REGEN"};//Q3
        //String[] defStatTxt=   {"Classe d'armure","Reflexe","Vigeur","Volonté","Résistance magie","Réduction (/chaotique)","Régénération"};//Q3
        String[] defStatTxt=   {"Classe d'armure","Reflexe","Vigeur","Volonté","Résistance magie","Réduction","Régénération"};//Q3
        LinearLayout quadrantSub1 = mainView.findViewById(R.id.main_frag_stats_quadrant3_1);
        LinearLayout quadrantSub2 = mainView.findViewById(R.id.main_frag_stats_quadrant3_2);
        injectStats(defStat,defStatTxt,quadrantSub1,quadrantSub2);
    }
    private void buildMiniQ4() {
        String[] advanceStat=  {"BMO","DMD","INIT","BBA", "KI"};// Q4
        //String[] advanceStatTxt= {"Bonus de\nmanoeuvre offensive","Degré de\nmanoeuvre défensive","Initiative","Bonus de base\nà l'attaque", "Réserve de Ki"};// Q4
        String[] advanceStatTxt= {"BMO","DMD","Initiative","BBA", "Réserve de Ki"};// Q4
        LinearLayout quadrantSub1 = mainView.findViewById(R.id.main_frag_stats_quadrant4_1);
        LinearLayout quadrantSub2 = mainView.findViewById(R.id.main_frag_stats_quadrant4_2);
        injectStats(advanceStat,advanceStatTxt,quadrantSub1,quadrantSub2);
    }

    private void injectStats(String[] stat, String[] statTxt, LinearLayout quadrantSub1, LinearLayout quadrantSub2) {
        quadrantSub1.removeAllViews();
        quadrantSub2.removeAllViews();
        for (int i=0;i<stat.length ;i++){
            addText(statTxt[i]+" : ",quadrantSub1);
            String valuesTxt=String.valueOf(aquene.getAllAbilities().getScore(stat[i]));
            addText(valuesTxt,quadrantSub2);
        }
    }

    private void injectBaseStats(String[] stat, String[] statTxt, LinearLayout quadrantSub1, LinearLayout quadrantSub2) {
        quadrantSub1.removeAllViews();
        quadrantSub2.removeAllViews();
        for (int i=0;i<stat.length ;i++){
            addText( statTxt[i]+" : ",quadrantSub1);
            String abScore = "";
            if(aquene.getAllAbilities().getMod(stat[i])>=0){
                abScore = "+"+aquene.getAllAbilities().getMod(stat[i]);
            } else {
                abScore = String.valueOf(aquene.getAllAbilities().getMod(stat[i]));
            }
            String valuesTxt=String.valueOf(aquene.getAllAbilities().getScore(stat[i]))+ " ("+abScore+")";
            addText(valuesTxt,quadrantSub2);
        }
    }

    private void addText(String s, LinearLayout parentLayout) {
        TextView text = new TextView(mC);
        text.setText(s);
        text.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT,1));
        text.setGravity(Gravity.CENTER_VERTICAL);
        parentLayout.addView(text);
    }

    // partie build full screen
    private void buildFullQ1() {
        quadrantFullSub1.removeAllViews();
        quadrantFullSub2.removeAllViews();
        addText("blabla 1",quadrantFullSub1);
        addText("ok",quadrantFullSub2);
    }
    private void buildFullQ2() {
        quadrantFullSub1.removeAllViews();
        quadrantFullSub2.removeAllViews();
        addText("blabla 2",quadrantFullSub1);
        addText("ok",quadrantFullSub2);
    }
    private void buildFullQ3() {
        quadrantFullSub1.removeAllViews();
        quadrantFullSub2.removeAllViews();
        addText("blabla 3",quadrantFullSub1);
        addText("ok",quadrantFullSub2);
    }
    private void buildFullQ4() {
        quadrantFullSub1.removeAllViews();
        quadrantFullSub2.removeAllViews();
        addText("blabla 4",quadrantFullSub1);
        addText("ok",quadrantFullSub2);

    }

    // call externe de refresh
    public void fullscreenQuadrant(LinearLayout layout){
        if(layout.equals(quadrant1)){
            buildFullQ1();
        } else if(layout.equals(quadrant2)){
            buildFullQ2();
        } else if(layout.equals(quadrant3)){
            buildFullQ3();
        } else if(layout.equals(quadrant4)){
            buildFullQ4();
        }
        switchTextTitle(mapLayoutTextTitle.get(layout));
        switchViewNext();
    }

    private void switchViewNext() {
        imgExit.setVisibility(View.VISIBLE);
        viewSwitcher.setInAnimation(mC,R.anim.infromleft);
        viewSwitcher.setOutAnimation(mC,R.anim.outtoright);
        viewSwitcher.showNext();
        fullscreen=true;
        lockOrient();
    }
    private void switchViewPrevious() {
        imgExit.setVisibility(View.GONE);
        switchTextTitle(mC.getResources().getString(R.string.quadrantGeneralTitle),"back");
        viewSwitcher.setInAnimation(mC,R.anim.infromright);
        viewSwitcher.setOutAnimation(mC,R.anim.outtoleft);
        viewSwitcher.showPrevious();
        fullscreen=false;
        unlockOrient();
    }

    private void switchTextTitle(final String s,String... mode){
        String modeSelected=mode.length > 0 ? mode[0] : "";  //parametre optionnel mode
        final TextView quadrantTitle=mainView.findViewById(R.id.quadrantGeneralTitle);
        quadrantTitle.clearAnimation();
        final Animation in;
        Animation out;
        if (modeSelected.equals("back"))
        {
            in = AnimationUtils.loadAnimation(mC, R.anim.infromright);
            out = AnimationUtils.loadAnimation(mC, R.anim.outtoleft);
        } else {
            in = AnimationUtils.loadAnimation(mC, R.anim.infromleft);
            out = AnimationUtils.loadAnimation(mC, R.anim.outtoright);
        }
        out.setAnimationListener( new  Animation.AnimationListener(){
            @Override
            public void onAnimationEnd(Animation arg0) {

                Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    public void run() {
                        quadrantTitle.setText(s);
                        quadrantTitle.startAnimation(in);
                    }
                }, 50);

            }

            @Override
            public void onAnimationRepeat(Animation arg0) {
                // TODO Auto-generated method stub

            }

            @Override
            public void onAnimationStart(Animation arg0) {
                // TODO Auto-generated method stub
            }
        });
        quadrantTitle.startAnimation(out);
    }

    public boolean isFullscreen() {
        return fullscreen;
    }

    private void lockOrient() {
        mA.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
    }

    private void unlockOrient() {
        mA.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_FULL_SENSOR);
    }
}
