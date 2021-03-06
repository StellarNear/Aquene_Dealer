package stellarnear.aquene_companion.Divers.CombatPanel;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.TextView;

import stellarnear.aquene_companion.Activities.MainActivity;
import stellarnear.aquene_companion.Divers.PostData;
import stellarnear.aquene_companion.Divers.PostDataElement;
import stellarnear.aquene_companion.Divers.Rolls.AtkRoll;
import stellarnear.aquene_companion.Divers.Rolls.Dice;
import stellarnear.aquene_companion.Divers.Rolls.Dice20;
import stellarnear.aquene_companion.Divers.Rolls.Roll;
import stellarnear.aquene_companion.Divers.Rolls.RollList;
import stellarnear.aquene_companion.Perso.Perso;
import stellarnear.aquene_companion.R;

/**
 * Created by jchatron on 07/02/2018.
 */

public class CombatLauncherHitCritLines {
    private RollList allRolls;
    private Context mC;
    private View mainView;
    private Boolean manualDice;
    private Boolean megaFail=false;
    private Perso aquene= MainActivity.aquene;
    public CombatLauncherHitCritLines(Context mC, View mainView, RollList allRolls) {
        this.mC=mC;
        this.mainView =mainView;
        this.allRolls = allRolls;
        SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(mC);
        this.manualDice = settings.getBoolean("switch_manual_diceroll", mC.getResources().getBoolean(R.bool.switch_manual_diceroll_DEF));
        if(manualDice){onChangeDiceListner();}
    }

    public void getPreRandValues() {
        LinearLayout line = mainView.findViewById(R.id.combat_dialog_prerand_value);
        line.removeAllViews();
        line.setVisibility(View.VISIBLE);
        for (Roll roll : allRolls.getList()) {
            TextView atkTxt = new TextView(mC);
            atkTxt.setText("+" + roll.getPreRandValue());
            atkTxt.setGravity(Gravity.CENTER);
            atkTxt.setLayoutParams(new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT, 1));
            line.addView(atkTxt);
        }
    }

    private void onChangeDiceListner(){
        for (Roll roll : allRolls.getList()){
            roll.getAtkRoll().setRefreshEventListener(new AtkRoll.OnRefreshEventListener() {
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
        for (Roll roll : allRolls.getList()) {
            //ImageView diceImg = roll.getImgAtk();
            if (fail) {
                roll.invalidated();
            } else {
                if(roll.isFailed()){fail=true;}
            }
            LinearLayout diceBox=new LinearLayout(mC);
            diceBox.setLayoutParams(new LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.WRAP_CONTENT,1));
            diceBox.setGravity(Gravity.CENTER);

            if (roll.getAtkDice().getImg().getParent()!=null){
                ((ViewGroup)roll.getAtkDice().getImg().getParent()).removeView(roll.getAtkDice().getImg());
            }

            roll.getAtkDice().setRefreshEventListener(new Dice.OnRefreshEventListener() {
                @Override
                public void onEvent() {
                    getRandValues();
                }

            });
            roll.getAtkDice().setMythicEventListener(new Dice20.OnMythicEventListener() {
                @Override
                public void onEvent() {
                    getRandValues();
                }
            });

            diceBox.addView(roll.getAtkDice().getImg());
            line.addView(diceBox);
        }
        getPostRandValues();
    }

    private void getPostRandValues(){
        LinearLayout line = mainView.findViewById(R.id.combat_dialog_atk_value);
        line.removeAllViews();
        line.setVisibility(View.VISIBLE);
        int allRollSet=0;
        for (Roll roll : allRolls.getList()) {
            TextView atkTxt = new TextView(mC);
            atkTxt.setText("?");
            if (roll.isInvalid()) {
                atkTxt.setText("-");
                allRollSet+=1;
            } else {
                if ((roll.getAtkDice().getRandValue() != 0)) {
                    int val = roll.getAtkValue();
                    if(roll.getAtkDice().getMythicDice()!=null){
                        val+=roll.getAtkDice().getMythicDice().getRandValue();
                    }
                    atkTxt.setText(String.valueOf(val));
                    allRollSet+=1;
                }
            }
            atkTxt.setGravity(Gravity.CENTER);
            atkTxt.setLayoutParams(new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT, 1));
            line.addView(atkTxt);
        }
        if (allRollSet== allRolls.getList().size()){
            getHitAndCritLines();
            new PostData(mC,new PostDataElement(allRolls,"atk"));
        }
    }

    private void getHitAndCritLines() {
        Boolean anyCrit = false;
        Boolean anyHit = false;
        for (Roll roll : allRolls.getList()){
            if(roll.getAtkValue()!=0&&!roll.isInvalid()){anyHit=true;}
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
            for (Roll roll : allRolls.getList()) {
                LinearLayout frame = new LinearLayout(mC);
                frame.setGravity(Gravity.CENTER);
                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT, 1);
                frame.setLayoutParams(params);
                if (!roll.isInvalid() && !roll.isFailed()) {
                    if (roll.getHitCheckbox().getParent()!=null){
                        ((ViewGroup)roll.getHitCheckbox().getParent()).removeView(roll.getHitCheckbox());
                    }
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
            for (final Roll roll : allRolls.getList()) {
                LinearLayout frame = new LinearLayout(mC);
                frame.setGravity(Gravity.CENTER);
                frame.setLayoutParams(new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT, 1));
                if (!roll.isInvalid() && !roll.isFailed() && roll.isCrit()) {
                    CheckBox check = roll.getCritCheckbox();
                    if (check.getParent()!=null){
                        ((ViewGroup)check.getParent()).removeView(check);
                    }
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
