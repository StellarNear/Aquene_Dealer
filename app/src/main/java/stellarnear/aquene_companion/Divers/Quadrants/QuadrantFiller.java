package stellarnear.aquene_companion.Divers.Quadrants;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.ViewSwitcher;

import java.text.DecimalFormatSymbols;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import stellarnear.aquene_companion.Activities.MainActivity;
import stellarnear.aquene_companion.Divers.HealthDialog;
import stellarnear.aquene_companion.Divers.PostData;
import stellarnear.aquene_companion.Divers.PostDataElement;
import stellarnear.aquene_companion.Divers.TestAlertDialog;
import stellarnear.aquene_companion.Divers.Tools;
import stellarnear.aquene_companion.Perso.Ability;
import stellarnear.aquene_companion.Perso.Perso;
import stellarnear.aquene_companion.Perso.Resource;
import stellarnear.aquene_companion.R;


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
    private Map<LinearLayout,String> mapQuadrantType=new HashMap<>();
    private ViewSwitcher viewSwitcher;
    private ImageButton imgExit;
    private LinearLayout quadrantFullMain;
    private LayoutInflater inflater;

    private boolean fullscreen=false;
    private Tools tools=Tools.getTools();
    private View fullView;
    public QuadrantFiller(View mainView, Context mC, Activity mA) {
        this.mC=mC;
        this.mA=mA;
        this.mainView=mainView;
        viewSwitcher=mainView.findViewById(R.id.viewSwitcherQuadrant);
        inflater = (LayoutInflater) mC.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        quadrant1 = mainView.findViewById(R.id.main_frag_stats_quadrant1);
        quadrant2 = mainView.findViewById(R.id.main_frag_stats_quadrant2);
        quadrant3 = mainView.findViewById(R.id.main_frag_stats_quadrant3);
        quadrant4 = mainView.findViewById(R.id.main_frag_stats_quadrant4);
        mapLayoutTextTitle.put(quadrant1,mC.getResources().getString(R.string.quadrantQ1Title));
        mapQuadrantType.put(quadrant1,"base");
        mapLayoutTextTitle.put(quadrant2,mC.getResources().getString(R.string.quadrantQ2Title));
        mapQuadrantType.put(quadrant2,"res");
        mapLayoutTextTitle.put(quadrant3,mC.getResources().getString(R.string.quadrantQ3Title));
        mapQuadrantType.put(quadrant3,"def");
        mapLayoutTextTitle.put(quadrant4,mC.getResources().getString(R.string.quadrantQ4Title));
        mapQuadrantType.put(quadrant4,"advanced");

        /*partie full quadrant à page 2 switcher*/
        fullView =(View) inflater.inflate(R.layout.quadrant_full, null);
        quadrantFullMain = fullView.findViewById(R.id.quadrant_full_main);
        imgExit = fullView.findViewById(R.id.button_quadrant_full_exit);
        imgExit.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                switchViewPrevious();
            }
        });
        viewSwitcher.addView(fullView);
        buildAllMini();
    }

    private void buildAllMini() {
        String[] types = {"base","res","def","advanced"};
        for (int i=0;i<types.length ;i++){
            String nameQuad="main_frag_stats_quadrant"+String.valueOf(i+1);
            int layIdQuad = mC.getResources().getIdentifier(nameQuad, "id", mC.getPackageName());
            LinearLayout quad =  mainView.findViewById(layIdQuad);
            if (i==1){
                List<Resource> resList= aquene.getAllResources().getResourcesListDisplay();
                injectStatsRes(resList, quad, "mini");
            } else {
                List<Ability> abiList= aquene.getAllAbilities().getAbilitiesList(types[i]);
                injectStatsAbi(abiList, quad, "mini");
            }
        }
    }

    public void fullscreenQuadrant(LinearLayout layout){
        if (mapQuadrantType.get(layout).equalsIgnoreCase("res")){
            List<Resource> abiRes= aquene.getAllResources().getResourcesListDisplay();
            injectStatsRes(abiRes, quadrantFullMain,"full");
        } else {
            List<Ability> abiList= aquene.getAllAbilities().getAbilitiesList(mapQuadrantType.get(layout));
            injectStatsAbi(abiList, quadrantFullMain,"full");
        }
        switchTextTitle(mapLayoutTextTitle.get(layout));
        switchViewNext();
    }

    private void injectStatsAbi(List<Ability> abiList, LinearLayout quadrant,String mode) {
        quadrant.removeAllViews();
        for (Ability abi : abiList){
            addTextAbi(abi,quadrant,mode);
        }
    }

    private void injectStatsRes(List<Resource> resList, LinearLayout quadrant, String mode) {
        quadrant.removeAllViews();
        for (Resource res : resList){
            addTextRes(res, quadrant, mode);
        }
    }

    private void addTextAbi(Ability abi, LinearLayout quadrant, String mode) {
        LinearLayout line;View subLabel;
        if (mode.equalsIgnoreCase("mini")) {
            line = (LinearLayout) inflater.inflate(R.layout.quadrant_sub_element_mini_line, null);
            subLabel=(View) inflater.inflate(R.layout.quadrant_sub_element_mini_label, null);
        } else {
            line = (LinearLayout) inflater.inflate(R.layout.quadrant_sub_element_full_line, null);
            subLabel=(View) inflater.inflate(R.layout.quadrant_sub_element_full_label, null);
        }

        line.setWeightSum(10);
        TextView text = subLabel.findViewById(R.id.sub_element_quadrant_mini_label_text);
        if (mode.equalsIgnoreCase("mini")){
            text.setText(abi.getShortname()+" : ");
        } else {
            text.setText(abi.getName() + " : ");
        }
        ((ImageView) subLabel.findViewById(R.id.sub_element_quadrant_mini_label_icon)).setImageDrawable(abi.getImg());

        line.addView(subLabel);

        View subValue=(View) inflater.inflate(R.layout.quadrant_sub_element_mini_value, null);
        TextView text2 = subValue.findViewById(R.id.sub_element_quadrant_mini_value_text);
        float textSize;
        if (mode.equalsIgnoreCase("mini")){  textSize=mC.getResources().getDimension(R.dimen.textSizeQuadrantMini);} else { textSize=mC.getResources().getDimension(R.dimen.textSizeQuadrantFull);  }
        text2.setTextSize(TypedValue.COMPLEX_UNIT_PX,textSize);
        String txt2;
        if (abi.getType().equalsIgnoreCase("base")){
            String abScore = "";
            if(aquene.getAbilityMod(abi.getId())>=0){
                abScore = "+"+ aquene.getAbilityMod(abi.getId());
            } else {
                abScore = String.valueOf(aquene.getAbilityMod(abi.getId()));
            }
            txt2=String.valueOf(aquene.getAbilityScore(abi.getId()))+ " ("+abScore+")";
        } else {
            txt2 = String.valueOf(aquene.getAbilityScore(abi.getId()));
        }
            text2.setText(txt2);
        line.addView(subValue);
        if(mode.equalsIgnoreCase("full") && abi.isTestable()){
            setListner(subLabel,abi);
            setListner(text2,abi);
        }
        quadrant.addView(line);
        setWidghtAndHeightOrientation(line,subLabel,subValue,mode);
    }

    private void setWidghtAndHeightOrientation(LinearLayout line,View subElement1,View subElement2,String mode) {
        if(mode.equalsIgnoreCase("mini") || mC.getResources().getConfiguration().orientation== Configuration.ORIENTATION_PORTRAIT) {
            line.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, 0, 1));
            subElement1.setLayoutParams(new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT,6));
            subElement2.setLayoutParams(new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT,4));
        } else {
            line.setLayoutParams(new LinearLayout.LayoutParams( 0,LinearLayout.LayoutParams.MATCH_PARENT, 1));
            subElement1.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,0,6));
            subElement2.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,0,4));
        }

    }

    private void addTextRes(Resource res, LinearLayout quadrant, String mode) {
        LinearLayout line;View subLabel;
        if (mode.equalsIgnoreCase("mini")) {
            line = (LinearLayout) inflater.inflate(R.layout.quadrant_sub_element_mini_line, null);
            subLabel=(View) inflater.inflate(R.layout.quadrant_sub_element_mini_label, null);
        } else {
            line = (LinearLayout) inflater.inflate(R.layout.quadrant_sub_element_full_line, null);
            subLabel=(View) inflater.inflate(R.layout.quadrant_sub_element_full_label, null);
        }
        line.setWeightSum(10);
        TextView text = subLabel.findViewById(R.id.sub_element_quadrant_mini_label_text);
        if (mode.equalsIgnoreCase("mini")){
            text.setText(res.getShortname()+" : ");
        } else {
            String column1txt=res.getName()+" : ";
            if(mode.equalsIgnoreCase("full") && res.getId().equalsIgnoreCase("resource_hp") && res.getShield() > 0 ){
                column1txt= res.getName()+" (temp) : ";
            }
            text.setText(column1txt);
        }
        ((ImageView) subLabel.findViewById(R.id.sub_element_quadrant_mini_label_icon)).setImageDrawable(res.getImg());
        line.addView(subLabel);

        View subValue=(View) inflater.inflate(R.layout.quadrant_sub_element_mini_value, null);
        TextView text2 = subValue.findViewById(R.id.sub_element_quadrant_mini_value_text);
        float textSize;
        if (mode.equalsIgnoreCase("mini")){  textSize=mC.getResources().getDimension(R.dimen.textSizeQuadrantMini);} else { textSize=mC.getResources().getDimension(R.dimen.textSizeQuadrantFull);  }
        text2.setTextSize(TypedValue.COMPLEX_UNIT_PX,textSize);
        String column2txt;

            if (res.isInfinite()) {
                column2txt = String.valueOf(DecimalFormatSymbols.getInstance().getInfinity());
            } else if(res.isFromCapacity() && res.getCapa().getJoinedResourceId()!=null){
                column2txt = String.valueOf(aquene.getCurrentResourceValue(res.getCapa().getJoinedResourceId())/res.getCapa().getnSpendJoinedResource());//todo division par spend
            } else {
                column2txt = String.valueOf(aquene.getCurrentResourceValue(res.getId()));
            }
            if (mode.equalsIgnoreCase("mini") && res.getId().equalsIgnoreCase("resource_hp")) {
                column2txt = String.valueOf(aquene.getCurrentResourceValue(res.getId()) + aquene.getAllResources().getResource(res.getId()).getShield());
            }
            if (mode.equalsIgnoreCase("full") && res.getId().equalsIgnoreCase("resource_hp")) {
                column2txt = String.valueOf(aquene.getCurrentResourceValue(res.getId()));
                if (aquene.getAllResources().getResource(res.getId()).getShield() > 0) {
                    column2txt += " (" + String.valueOf(aquene.getAllResources().getResource(res.getId()).getShield()) + ")";
                }
            }

        text2.setText(column2txt);
        line.addView(subValue);

        if(mode.equalsIgnoreCase("full") && res.isTestable()){ //correspond à hp ou capacity
            setListner(subLabel,res);
            setListner(subValue,res);
        }
        quadrant.addView(line);

        setWidghtAndHeightOrientation(line,subLabel,subValue,mode);
    }

    private void setListner(View text,final Ability abi) {

            text.setOnClickListener( new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    TestAlertDialog testAlertDialog = new TestAlertDialog(mA, mC, abi);
                    testAlertDialog.setRefreshEventListener(new TestAlertDialog.OnRefreshEventListener() {
                        @Override
                        public void onEvent() {
                            buildAllMini();
                        }
                    });
                }
            });
    }

    private void setListner(View text,final Resource res) { //constructeur ressoruce
        if(res.getId().equalsIgnoreCase("resource_hp")) {
            text.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    HealthDialog healthDialog = new HealthDialog(mA, mC);
                    healthDialog.setRefreshEventListener(new HealthDialog.OnRefreshEventListener() {
                        public void onEvent() {
                            List<Resource> abiRes = aquene.getAllResources().getResourcesListDisplay();
                            injectStatsRes(abiRes, quadrantFullMain, "full"); //refreshCalculations le full
                            buildAllMini(); //refreshCalculations les mini
                        }
                    });
                    healthDialog.showAlertDialog();
                }
            });
        } else if(res.isFromCapacity()){
            text.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    boolean remainingUsage = res.getCapa().getJoinedResourceId()==null ?
                            aquene.getCurrentResourceValue(res.getId())>0
                            :aquene.getCurrentResourceValue(res.getCapa().getJoinedResourceId())-res.getCapa().getnSpendJoinedResource()>=0;
                    if(res.isInfinite() || remainingUsage) {
                        new AlertDialog.Builder(mC)
                                .setTitle("Utilisation de "+res.getName())
                                .setMessage(res.getCapaDescr())
                                .setIcon(android.R.drawable.ic_menu_help)
                                .setPositiveButton("Utiliser", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int whichButton) {
                                            if (!res.isInfinite()) {
                                                if(res.getCapa().getJoinedResourceId()!=null){
                                                    aquene.getAllResources().getResource(res.getCapa().getJoinedResourceId()).spend(res.getCapa().getnSpendJoinedResource());
                                                } else {
                                                    aquene.getAllResources().getResource(res.getId()).spend(1);
                                                }
                                                if(res.getId().equalsIgnoreCase("resource_blinding_speed")){
                                                    PreferenceManager.getDefaultSharedPreferences(mC).edit().putBoolean("switch_blinding_speed", true).apply();
                                                }
                                            }
                                            new PostData(mC, new PostDataElement(res.getCapa()));
                                            tools.customToast(mC, res.getName() + " lancée !", "center");

                                            List<Resource> abiRes = aquene.getAllResources().getResourcesListDisplay();//refreshCalculations le full
                                            injectStatsRes(abiRes, quadrantFullMain, "full"); //refreshCalculations le full
                                            buildAllMini(); //refreshCalculations les mini
                                    }
                                })
                                .setNegativeButton("Annuler", null).show();
                    } else {
                        tools.customToast(mC,"Tu n'as plus d'utilisation journalière...");
                    }
                }
            });
        }
    }

    private void switchViewNext() {
        imgExit.setVisibility(View.VISIBLE);
        quadrantFullMain.setVisibility(View.VISIBLE);
        viewSwitcher.setInAnimation(mC,R.anim.infromright);
        viewSwitcher.setOutAnimation(mC,R.anim.outtoleft);
        viewSwitcher.showNext();
        fullscreen=true;
        lockOrient();
    }
    private void switchViewPrevious() {
        imgExit.setVisibility(View.GONE);
        quadrantFullMain.setVisibility(View.GONE);
        switchTextTitle(mC.getResources().getString(R.string.quadrantGeneralTitle),"back");
        viewSwitcher.setInAnimation(mC,R.anim.infromleft);
        viewSwitcher.setOutAnimation(mC,R.anim.outtoright);
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
            in = AnimationUtils.loadAnimation(mC, R.anim.infromleftfilled);
            out = AnimationUtils.loadAnimation(mC, R.anim.outtorightfilled);
        } else {
            in = AnimationUtils.loadAnimation(mC, R.anim.infromrightfilled);
            out = AnimationUtils.loadAnimation(mC, R.anim.outtoleftfilled);
        }
        out.setAnimationListener( new  Animation.AnimationListener(){
            @Override
            public void onAnimationEnd(Animation arg0) {
                Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    public void run() {
                        quadrantTitle.setText(s);
                        quadrantTitle.clearAnimation();
                        quadrantTitle.startAnimation(in);
                    }
                }, 50);
            }
            @Override
            public void onAnimationRepeat(Animation arg0) {         }
            @Override
            public void onAnimationStart(Animation arg0) { }
        });
        quadrantTitle.startAnimation(out);
    }

    public boolean isFullscreen() { return fullscreen;  }

    private void lockOrient() {
            mA.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
    }

    private void unlockOrient() {     mA.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_FULL_SENSOR);
    }


}