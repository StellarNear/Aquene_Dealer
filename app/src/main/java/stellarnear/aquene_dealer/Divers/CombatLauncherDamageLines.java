package stellarnear.aquene_dealer.Divers;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.media.MediaPlayer;
import android.net.Uri;
import android.preference.PreferenceManager;
import android.support.v7.app.AlertDialog;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.VideoView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import stellarnear.aquene_dealer.R;

public class CombatLauncherDamageLines {
    private List<Roll> allRolls;
    private Context mC;

    private View mainView;
    private Boolean manualDiceDmg;

    private SharedPreferences settings;

    private int sumPhy;
    private int sumFire;

    private boolean inputDone = false;
    private List<Roll> selectedRolls;
    private boolean detailAvailable = false;
    private int nDicesDone = 0;
    private int nDicesSet;
    private CombatLauncherDamageDetailDialog combatLauncherDamageDetailDialog;
    private boolean statsDisplayed = false;
    private LinearLayout statPanelLinear;
    private Tools tools = new Tools();

    public CombatLauncherDamageLines(Context mC, View mainView, List<Roll> allRolls) {
        this.mC = mC;
        this.mainView = mainView;
        this.allRolls = allRolls;
        settings = PreferenceManager.getDefaultSharedPreferences(mC);
        manualDiceDmg = settings.getBoolean("switch_manual_diceroll_damage", mC.getResources().getBoolean(R.bool.switch_manual_diceroll_damage_DEF));
        statPanelLinear = mainView.findViewById(R.id.stats_linear);
        statPanelLinear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                switchStats();
            }
        });

        for(Roll roll:allRolls) {
            nDicesSet += roll.getDmgDiceList().size();
        }
    }

    public void getDamageLine() {
        sumPhy = 0;
        sumFire = 0;

        selectedRolls = new ArrayList<>();
        for (Roll roll : allRolls) {
            if (!roll.isHitConfirmed() || roll.isInvalid()) {
                continue;
            }
            selectedRolls.add(roll);
            roll.setDmgRand();
            roll.isDelt();

            sumPhy += roll.getDmgSumPhy();
            sumFire += roll.getDmgSumFire();
        }
        if (manualDiceDmg && !inputDone) {
            putDicesSummary();
        } else {
            printResult();
        }
        combatLauncherDamageDetailDialog = new CombatLauncherDamageDetailDialog( mC, selectedRolls);
        onChangeDiceListner();
    }

    private void onChangeDiceListner() {
        for (Roll roll : selectedRolls) {
            if (manualDiceDmg) {
                roll.getDmgRoll().setRefreshEventListener(new DmgRoll.OnRefreshEventListener() {
                    public void onEvent() {
                        checkAllRollSet();
                    }
                });
            } else {
                detailAvailable = true;
                roll.getDmgRoll().setRefreshEventListener(null);
            }
        }
    }

    private void checkAllRollSet() {
        nDicesDone += 1;
        if (nDicesDone == nDicesSet) {
            tools.customToast(mC, "Tu as fini la saisie !", "center");
            inputDone();
            combatLauncherDamageDetailDialog.changeCancelButtonToOk();
            detailAvailable = true;
        }
    }

    private void printResult() {
        TextView damageLineTitle = mainView.findViewById(R.id.combat_dialog_dmg_title);
        damageLineTitle.setVisibility(View.VISIBLE);
        LinearLayout damageLine = mainView.findViewById(R.id.combat_dialog_dmg);
        damageLine.removeAllViews();
        damageLine.setVisibility(View.VISIBLE);
        if (sumPhy + sumFire > 0) {
            LinearLayout frame = getFrameSummary();
            if (sumPhy > 0) {
                TextView sumPhyTxt = new TextView(mC);
                sumPhyTxt.setGravity(Gravity.CENTER);
                sumPhyTxt.setText(String.valueOf(sumPhy));
                sumPhyTxt.setCompoundDrawablesWithIntrinsicBounds(null, null, tools.resize(mC, R.drawable.phy_dmg_type, (int) mC.getResources().getDimension(R.dimen.combat_launcher_dmg_type_icon_size)), null);
                sumPhyTxt.setTextSize(18);
                sumPhyTxt.setTypeface(null, Typeface.BOLD);
                frame.addView(sumPhyTxt);
            }
            if (sumFire > 0) {
                TextView sumFireTxt = new TextView(mC);
                sumFireTxt.setGravity(Gravity.CENTER);
                sumFireTxt.setText(String.valueOf(sumFire));
                sumFireTxt.setCompoundDrawablesWithIntrinsicBounds(null, null, tools.resize(mC, R.drawable.fire_dmg_type, (int) mC.getResources().getDimension(R.dimen.combat_launcher_dmg_type_icon_size)), null);
                sumFireTxt.setTextSize(18);
                sumFireTxt.setTypeface(null, Typeface.BOLD);
                frame.addView(sumFireTxt);
            }
            damageLine.addView(frame);
            setSummaryListnerToShowStats(frame);
        } else {
            damageLineTitle.setVisibility(View.VISIBLE);
            TextView noDmg = new TextView(mC);
            noDmg.setTextSize(20);
            noDmg.setText("Aucun dégats infligé");
            damageLine.addView(noDmg);
        }
    }

    public int getSum() {
        return sumFire + sumPhy;
    }

    private void putDicesSummary() {
        TextView damageLineTitle = mainView.findViewById(R.id.combat_dialog_dmg_title);
        LinearLayout damageLine = mainView.findViewById(R.id.combat_dialog_dmg);
        damageLine.removeAllViews();
        if (nDicesSet > 0) {
            damageLine.setVisibility(View.VISIBLE);
            damageLineTitle.setVisibility(View.VISIBLE);
            LinearLayout summary = getFrameSummary();

            List<Integer> diceTypeList = Arrays.asList(10,8,6);
            for (Integer diceType : diceTypeList){
                int nDices=0;
                for (Roll roll : allRolls){
                    nDices+=roll.getDmgDiceList(diceType).size();
                }
                if (nDices>0){
                    TextView nd8Text = new TextView(mC);
                    nd8Text.setGravity(Gravity.CENTER);
                    nd8Text.setText(nDices + "d"+diceType);
                    nd8Text.setTextSize(18);
                    nd8Text.setTypeface(null, Typeface.BOLD);
                    int drawableId = mC.getResources().getIdentifier("d" + diceType + "_main", "drawable", mC.getPackageName());
                    nd8Text.setCompoundDrawablesWithIntrinsicBounds(tools.resize(mC, drawableId, mC.getResources().getDimensionPixelSize(R.dimen.icon_main_dices_combat_launcher_size)), null, null, null);
                    summary.addView(nd8Text);
                }
            }
            setSummaryListnerToInputManualDmg(summary);
            damageLine.addView(summary);
        } else {
            damageLine.setVisibility(View.VISIBLE);
            damageLineTitle.setVisibility(View.VISIBLE);
            TextView noDice = new TextView(mC);
            noDice.setTextSize(18);
            noDice.setTypeface(null, Typeface.BOLD);
            noDice.setText("Aucune attaque sélectionnée");
            damageLine.addView(noDice);
        }
    }

    private void setSummaryListnerToInputManualDmg(LinearLayout summary) {
        summary.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                combatLauncherDamageDetailDialog.showDialogManual();
            }
        });
    }

    private void setSummaryListnerToShowStats(LinearLayout summary) {
        summary.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!statsDisplayed) {
                    //ProbaFromDiceRand proba = new ProbaFromDiceRand(selectedRolls);
                    //proba.giveLinearToFill(statPanelLinear);
                    switchStats();
                } else {
                    switchStats();
                }
            }
        });
    }

    private void switchStats() {
        LinearLayout statsLine = mainView.findViewById(R.id.stats_linear);
        final Animation inFromBot = AnimationUtils.loadAnimation(mC, R.anim.infrombotstatspanel);
        final Animation outToBot = AnimationUtils.loadAnimation(mC, R.anim.outtobotstatspanel);
        if (!statsDisplayed) {
            statsLine.setVisibility(View.VISIBLE);
            statsLine.startAnimation(inFromBot);
            statsLine.setClickable(true); //pour que les boutons derriere ne soit plus clickable
            statsDisplayed = true;
        } else {
            statsLine.startAnimation(outToBot);
            statsLine.setVisibility(View.GONE);
            statsLine.setClickable(false); //pour pouvoir recliquer sur les fabs  quand il est masqué
            statsDisplayed = false;
        }
    }

    public void hideStatLine(){
        LinearLayout statsLine = mainView.findViewById(R.id.stats_linear);
        statsLine.setVisibility(View.GONE);
        statsLine.setClickable(false); //pour pouvoir recliquer sur les fabs  quand il est masqué
        statsDisplayed = false;
    }

    private void inputDone() {
        this.inputDone = true;
        sumPhy = 0;
        sumFire = 0;
        for (Roll roll : selectedRolls) {
            sumPhy += roll.getDmgSumPhy();
            sumFire += roll.getDmgSumFire();
        }
        printResult();
    }

    public void showDialogDetail() {
        if (selectedRolls != null && !selectedRolls.isEmpty() && detailAvailable) {
            combatLauncherDamageDetailDialog.showDialogDetail();
        } else {
            tools.customToast(mC, "Aucun dégat à afficher", "center");
        }
    }

    private LinearLayout getFrameSummary() {
        LinearLayout frame = new LinearLayout(mC);
        frame.setOrientation(LinearLayout.HORIZONTAL);
        frame.setGravity(Gravity.CENTER);
        int margin = (int) mC.getResources().getDimension(R.dimen.general_margin);
        frame.setPadding(margin, margin, margin, margin);
        frame.setBackground(mC.getDrawable(R.drawable.background_border_dice_list_summary));
        frame.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        return frame;
    }

}
