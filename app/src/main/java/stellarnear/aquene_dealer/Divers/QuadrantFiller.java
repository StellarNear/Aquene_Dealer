package stellarnear.aquene_dealer.Divers;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.support.annotation.Dimension;
import android.util.TypedValue;
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
import java.util.Random;

import stellarnear.aquene_dealer.Activities.MainActivity;
import stellarnear.aquene_dealer.Perso.Ability;
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
    private Map<LinearLayout,String> mapQuadrantType=new HashMap<>();
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
        mapQuadrantType.put(quadrant1,"base");
        mapLayoutTextTitle.put(quadrant2,mC.getResources().getString(R.string.quadrantQ2Title));
        mapQuadrantType.put(quadrant2,"general");
        mapLayoutTextTitle.put(quadrant3,mC.getResources().getString(R.string.quadrantQ3Title));
        mapQuadrantType.put(quadrant3,"def");
        mapLayoutTextTitle.put(quadrant4,mC.getResources().getString(R.string.quadrantQ4Title));
        mapQuadrantType.put(quadrant4,"advanced");

        buildAllMini();
    }

    private void buildAllMini() {

        String[] types = {"base","general","def","advanced"};

        for (int i=0;i<types.length ;i++){
            List<Ability> abiList=aquene.getAllAbilities().getAbilitiesList(types[i]);
            String nameSub1="main_frag_stats_quadrant"+String.valueOf(i+1)+"_1";
            int layIdSub1 = mC.getResources().getIdentifier(nameSub1, "id", mC.getPackageName());
            LinearLayout sub1 =  mainView.findViewById(layIdSub1);
            String nameSub2="main_frag_stats_quadrant"+String.valueOf(i+1)+"_2";
            int layIdSub2 = mC.getResources().getIdentifier(nameSub2, "id", mC.getPackageName());
            LinearLayout sub2 =  mainView.findViewById(layIdSub2);

            injectStats(abiList, sub1, sub2,"mini");
        }
    }

    // call externe de refresh
    public void fullscreenQuadrant(LinearLayout layout){
        List<Ability> abiList=aquene.getAllAbilities().getAbilitiesList(mapQuadrantType.get(layout));
        injectStats(abiList, quadrantFullSub1, quadrantFullSub2,"full");
        switchTextTitle(mapLayoutTextTitle.get(layout));
        switchViewNext();
    }

    private void injectStats(List<Ability> abiList, LinearLayout quadrantSub1, LinearLayout quadrantSub2,String mode) {
        quadrantSub1.removeAllViews();
        quadrantSub2.removeAllViews();

        for (Ability abi : abiList){
            addText(abi,quadrantSub1,quadrantSub2,mode);
        }
    }

    private void addText(Ability abi, LinearLayout quadrantSub1, LinearLayout quadrantSub2,String mode) {
        float textSize;
        int iconSize;
        if (mode.equalsIgnoreCase("mini")){
            textSize=mC.getResources().getDimension(R.dimen.textSizeQuadrantMini);
            iconSize=mC.getResources().getDimensionPixelSize(R.dimen.iconSizeQuadrantMini);
        } else {
            textSize=mC.getResources().getDimension(R.dimen.textSizeQuadrantFull);
            iconSize=mC.getResources().getDimensionPixelSize(R.dimen.iconSizeQuadrantFull);
        }

        TextView text = new TextView(mC);
        text.setTextSize(TypedValue.COMPLEX_UNIT_PX,textSize);
        text.setText(abi.getShortname()+" : ");
        text.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT,1));
        text.setGravity(Gravity.CENTER_VERTICAL);
        int imgId=mC.getResources().getIdentifier("mire_test", "drawable", mC.getPackageName());
        text.setCompoundDrawablesWithIntrinsicBounds(resize(imgId,iconSize),null,null,null);
        if (mode.equalsIgnoreCase("full")){text.setCompoundDrawablePadding(mC.getResources().getDimensionPixelSize(R.dimen.full_quadrant_icons_margin));}
        quadrantSub1.addView(text);

        TextView text2 = new TextView(mC);
        text2.setTextSize(TypedValue.COMPLEX_UNIT_PX,textSize);
        String txt2;
        if (abi.getType().equalsIgnoreCase("base")){
            String abScore = "";
            if(aquene.getAllAbilities().getMod(abi.getId())>=0){
                abScore = "+"+aquene.getAllAbilities().getMod(abi.getId());
            } else {
                abScore = String.valueOf(aquene.getAllAbilities().getMod(abi.getId()));
            }
            txt2=String.valueOf(aquene.getAllAbilities().getScore(abi.getId()))+ " ("+abScore+")";
        } else {
            txt2=String.valueOf(aquene.getAllAbilities().getScore(abi.getId()));
        }

        text2.setText(txt2);
        text2.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT,1));
        text2.setGravity(Gravity.CENTER_VERTICAL);
        quadrantSub2.addView(text2);

        if(abi.isTestable()){
            setListner(text,abi);
            setListner(text2,abi);
        }
    }

    private void setListner(TextView text,final Ability abi) {
        text.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AbilityAlertDialog abilityAlertDialog = new AbilityAlertDialog(mA,mC,abi);
                abilityAlertDialog.showAlertDialog();
            }
        });
    }

    private Drawable resize(int imageId, int pixel_size_icon) {
        Drawable image = mC.getDrawable(imageId);
        Bitmap b = ((BitmapDrawable) image).getBitmap();
        Bitmap bitmapResized = Bitmap.createScaledBitmap(b, pixel_size_icon, pixel_size_icon, false);
        return new BitmapDrawable(mC.getResources(), bitmapResized);
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
                        quadrantTitle.clearAnimation();
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
