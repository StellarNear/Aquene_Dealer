package stellarnear.aquene_dealer.Divers;

import android.content.Context;
import android.os.Handler;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.TranslateAnimation;
import android.widget.LinearLayout;
import android.widget.TextView;

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
    private View mainView;
    private LinearLayout quadrantLine1;
    private LinearLayout quadrantLine2;
    private LinearLayout quadrant1;
    private LinearLayout quadrant2;
    private LinearLayout quadrant3;
    private LinearLayout quadrant4;
    private Perso aquene = MainActivity.aquene;
    private Map<LinearLayout,Boolean> mapLayoutFull =new HashMap<>();
    private List<LinearLayout> quadrantList;
    public QuadrantFiller(View mainView, Context mC) {
        this.mC=mC;
        this.mainView=mainView;
        quadrantLine1 = mainView.findViewById(R.id.main_frag_stats_quadrantLine1);
        quadrantLine2 = mainView.findViewById(R.id.main_frag_stats_quadrantLine2);
        quadrant1 = mainView.findViewById(R.id.main_frag_stats_quadrant1);
        mapLayoutFull.put(quadrant1,false);
        quadrant2 = mainView.findViewById(R.id.main_frag_stats_quadrant2);
        mapLayoutFull.put(quadrant2,false);
        quadrant3 = mainView.findViewById(R.id.main_frag_stats_quadrant3);
        mapLayoutFull.put(quadrant3,false);
        quadrant4 = mainView.findViewById(R.id.main_frag_stats_quadrant4);
        mapLayoutFull.put(quadrant4,false);
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
            String valuesTxt=String.valueOf(aquene.getAbilities().getScore(stat[i]));
            addText(valuesTxt,quadrantSub2);
        }
    }

    private void injectBaseStats(String[] stat, String[] statTxt, LinearLayout quadrantSub1, LinearLayout quadrantSub2) {
        quadrantSub1.removeAllViews();
        quadrantSub2.removeAllViews();
        for (int i=0;i<stat.length ;i++){
            addText( statTxt[i]+" : ",quadrantSub1);
            String abScore = "";
            if(aquene.getAbilities().getMOD(stat[i])>=0){
                abScore = "+"+aquene.getAbilities().getMOD(stat[i]);
            } else {
                abScore = "-"+aquene.getAbilities().getMOD(stat[i]);
            }
            String valuesTxt=String.valueOf(aquene.getAbilities().getScore(stat[i]))+ " ("+abScore+")";
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
        LinearLayout quadrantSub1 = mainView.findViewById(R.id.main_frag_stats_quadrant1_1);
        LinearLayout quadrantSub2 = mainView.findViewById(R.id.main_frag_stats_quadrant1_2);
        quadrantSub1.removeAllViews();
        quadrantSub2.removeAllViews();
        addText("blabla 1",quadrantSub1);
        addText("ok",quadrantSub2);
    }
    private void buildFullQ2() {
        LinearLayout quadrantSub1 = mainView.findViewById(R.id.main_frag_stats_quadrant2_1);
        LinearLayout quadrantSub2 = mainView.findViewById(R.id.main_frag_stats_quadrant2_2);
        quadrantSub1.removeAllViews();
        quadrantSub2.removeAllViews();
        addText("blabla 2",quadrantSub1);
        addText("ok",quadrantSub2);
    }
    private void buildFullQ3() {
        LinearLayout quadrantSub1 = mainView.findViewById(R.id.main_frag_stats_quadrant3_1);
        LinearLayout quadrantSub2 = mainView.findViewById(R.id.main_frag_stats_quadrant3_2);
        quadrantSub1.removeAllViews();
        quadrantSub2.removeAllViews();
        addText("blabla 3",quadrantSub1);
        addText("ok",quadrantSub2);
    }
    private void buildFullQ4() {
        LinearLayout quadrantSub1 = mainView.findViewById(R.id.main_frag_stats_quadrant4_1);
        LinearLayout quadrantSub2 = mainView.findViewById(R.id.main_frag_stats_quadrant4_2);
        quadrantSub1.removeAllViews();
        quadrantSub2.removeAllViews();
        addText("blabla 4",quadrantSub1);
        addText("ok",quadrantSub2);

    }

    // call externe de refresh

    public void fullscreenQuadrant(LinearLayout layout){
        if(layout.equals(quadrant1)){
            switchTextTitle(mC.getResources().getString(R.string.quadrantQ1Title));
            buildFullQ1();
        } else if(layout.equals(quadrant2)){
            switchTextTitle(mC.getResources().getString(R.string.quadrantQ2Title));
            buildFullQ2();
        } else if(layout.equals(quadrant3)){
            switchTextTitle(mC.getResources().getString(R.string.quadrantQ3Title));
            buildFullQ3();
        } else if(layout.equals(quadrant4)){
            switchTextTitle(mC.getResources().getString(R.string.quadrantQ4Title));
            buildFullQ4();
        }
    }

    public void minimizeQuadrant(LinearLayout layout){
        switchTextTitle(mC.getResources().getString(R.string.quadrantGeneralTitle));
        if(layout.equals(quadrant1)){
            buildMiniQ1();
        } else if(layout.equals(quadrant2)){
            buildMiniQ2();
        } else if(layout.equals(quadrant3)){
            buildMiniQ3();
        } else if(layout.equals(quadrant4)){
            buildMiniQ4();
        }
    }

    private void switchTextTitle(final String s){
        final TextView quadrantTitle=mainView.findViewById(R.id.quadrantGeneralTitle);
        quadrantTitle.clearAnimation();

        final Animation fromLeft = new TranslateAnimation(-400,0, 0, 0);
        fromLeft.setDuration(175);
        final Animation fadeIn = new AlphaAnimation(0.0f, 1.0f);
        fadeIn.setDuration(175);
        final AnimationSet setIn = new AnimationSet(true);
        setIn.setFillAfter(true);
        setIn.addAnimation(fadeIn);
        setIn.addAnimation(fromLeft);

        final Animation toRight = new TranslateAnimation(0, 400, 0, 0);
        toRight.setDuration(175);
        Animation fadeOut = new AlphaAnimation(1.0f, 0.0f);
        fadeOut.setDuration(175);
        fadeOut.setAnimationListener( new  Animation.AnimationListener(){
            @Override
            public void onAnimationEnd(Animation arg0) {

                Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    public void run() {
                        quadrantTitle.setText(s);
                        quadrantTitle.startAnimation(setIn);
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

        AnimationSet setOut = new AnimationSet(true);
        setOut.setFillAfter(true);
        setOut.addAnimation(fadeOut);
        setOut.addAnimation(toRight);

        quadrantTitle.startAnimation(setOut);

    }
}
