package stellarnear.aquene_dealer.Divers;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Point;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.preference.PreferenceManager;
import android.support.v7.app.AlertDialog;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.List;
import java.util.Random;

import stellarnear.aquene_dealer.Activities.MainActivity;
import stellarnear.aquene_dealer.Perso.Perso;
import stellarnear.aquene_dealer.R;

/**
 * Created by jchatron on 07/02/2018.
 */

public class CombatLauncherHitCritLines {
    private List<Roll>atksRolls;
    private Context mC;
    private Activity mA;
    private View mainView;
    private Boolean manualDice;
    private Boolean megaFail=false;
    private Perso aquene= MainActivity.aquene;
    public CombatLauncherHitCritLines(Activity mA, Context mC, View mainView, List<Roll> atksRolls) {
        this.mA=mA;
        this.mC=mC;
        this.mainView =mainView;
        this.atksRolls=atksRolls;
        SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(mC);
        this.manualDice = settings.getBoolean("switch_manual_diceroll", mC.getResources().getBoolean(R.bool.switch_manual_diceroll_DEF));
        if(manualDice){onChangeDiceListner();}
    }

    public void getPreRandValues() {
        LinearLayout line = mainView.findViewById(R.id.combat_dialog_prerand_value);
        line.removeAllViews();
        line.setVisibility(View.VISIBLE);
        for (Roll roll : atksRolls) {
            TextView atkTxt = new TextView(mC);
            atkTxt.setText("+" + roll.getPreRandValue());
            atkTxt.setGravity(Gravity.CENTER);
            atkTxt.setLayoutParams(new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT, 1));
            line.addView(atkTxt);
        }
    }

    private void onChangeDiceListner(){
        for (Roll roll :atksRolls){
            roll.setRefreshEventListener(new Roll.OnRefreshEventListener() {
                public void onEvent() {
                    getRandValues();
                }
            });
        }
    }

    public void getRandValues() {
        LinearLayout line = mainView.findViewById(R.id.combat_dialog_atk_dices);
        line.removeAllViews();
        line.setVisibility(View.VISIBLE);
        Boolean fail=false;
        for (Roll roll : atksRolls) {
            ImageView diceImg = roll.getImgAtk();
            if (fail) {
                roll.invalidated();
            } else {
                if(roll.isFailed()){fail=true;}
            }
            LinearLayout diceBox=new LinearLayout(mC);
            diceBox.setLayoutParams(new LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.WRAP_CONTENT,1));
            diceBox.setGravity(Gravity.CENTER);
            if (diceImg.getParent()!=null){
                ((ViewGroup)diceImg.getParent()).removeView(diceImg);
            }
            diceBox.addView(diceImg);
            line.addView(diceBox);
        }
        getPostRandValues();
    }

    public void getPostRandValues(){
        LinearLayout line = mainView.findViewById(R.id.combat_dialog_atk_value);
        line.removeAllViews();
        line.setVisibility(View.VISIBLE);
        int allRollSet=0;
        for (Roll roll : atksRolls) {
            TextView atkTxt = new TextView(mC);
            atkTxt.setText("?");
            if (roll.isInvalid()) {
                atkTxt.setText("-");
                allRollSet+=1;
            } else {
                if ((roll.getValue() != 0)) {
                    atkTxt.setText("+"+String.valueOf(roll.getValue()));
                    allRollSet+=1;
                }
            }
            atkTxt.setGravity(Gravity.CENTER);
            atkTxt.setLayoutParams(new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT, 1));
            line.addView(atkTxt);
        }
        if (allRollSet==atksRolls.size()){
            getHitAndCritLines();
        }
    }

    private void getHitAndCritLines() {
        Boolean anyCrit = false;
        Boolean anyHit = false;
        for (Roll roll : atksRolls){
            if(roll.getValue()!=0&&!roll.isInvalid()){anyHit=true;}
            if (roll.isCrit()&&!roll.isInvalid()){anyCrit=true;}
        }
        TextView title = mainView.findViewById(R.id.combat_dialog_hit_box_title);
        LinearLayout line = mainView.findViewById(R.id.combat_dialog_hit_box);
        line.removeAllViews();
        title.setVisibility(View.GONE);
        line.setVisibility(View.GONE);
        if(anyHit) {
            title.setVisibility(View.VISIBLE);
            line.setVisibility(View.VISIBLE);
            for (Roll roll : atksRolls) {
                LinearLayout frame = new LinearLayout(mC);
                frame.setGravity(Gravity.CENTER);
                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT, 1);
                frame.setLayoutParams(params);
                if (!roll.isInvalid() && !roll.isFailed()) {
                    frame.addView(roll.getHitCheckbox());
                }
                line.addView(frame);
            }
        } else { megaFail=true;}
      
        TextView titleCrit = mainView.findViewById(R.id.combat_dialog_crit_box_title);
        LinearLayout lineCrit = mainView.findViewById(R.id.combat_dialog_crit_box);
        titleCrit.setVisibility(View.GONE);
        lineCrit.setVisibility(View.GONE);
        if(anyCrit) {
            Animation anim = AnimationUtils.loadAnimation(mC,R.anim.zoomin);
            titleCrit.setVisibility(View.VISIBLE);
            if(aquene.featIsActive("feat_crit")){
                titleCrit.setText("Coup(s) qui crit (+4 au jet d'attaque pour confirmer) :");
            } else {
                titleCrit.setText("Coup(s) qui crit (jet d'attaque de confirmation) :");
            }
            titleCrit.startAnimation(anim);
            lineCrit.setVisibility(View.VISIBLE);
            lineCrit.removeAllViews();
            for (final Roll roll : atksRolls) {
                LinearLayout frame = new LinearLayout(mC);
                frame.setGravity(Gravity.CENTER);
                frame.setLayoutParams(new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT, 1));
                if (!roll.isInvalid() && !roll.isFailed() && roll.isCrit()) {
                    CheckBox check = roll.getCritCheckbox();
                    frame.addView(check);
                    Animation animCheck = AnimationUtils.loadAnimation(mC,R.anim.zoomin);
                    check.startAnimation(animCheck);
                }
                lineCrit.addView(frame);
            }
        }
    }


    public boolean isMegaFail(){
        return this.megaFail;
    }
}
