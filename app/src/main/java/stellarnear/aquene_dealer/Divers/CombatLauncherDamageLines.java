package stellarnear.aquene_dealer.Divers;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Typeface;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.preference.PreferenceManager;
import android.support.v7.app.AlertDialog;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import stellarnear.aquene_dealer.R;

public class CombatLauncherDamageLines {
    private List<Roll>atksRolls;
    private Context mC;
    private Activity mA;
    private View mainView;
    private Boolean manualDiceDmg;
    private AlertDialog diceList;
    private SharedPreferences settings;
    private int nD10;
    private int nD8;
    private int nD6;
    private int sumPhy;
    private int sumFire;
    private boolean inputDone=false;
    private List<Roll> selectedRolls;
    private CombatLauncherDamageDetailDialog combatLauncherDamageDetailDialog;
    public CombatLauncherDamageLines(Activity mA, Context mC, View mainView, List<Roll> atksRolls) {
        this.mA=mA;
        this.mC=mC;
        this.mainView =mainView;
        this.atksRolls=atksRolls;
        SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(mC);
        manualDiceDmg = settings.getBoolean("switch_manual_diceroll_damage", mC.getResources().getBoolean(R.bool.switch_manual_diceroll_damage_DEF));
    }

    private Drawable resize(int imageId, int pixel_size_icon) {
        Drawable image = mC.getDrawable(imageId);
        Bitmap b = ((BitmapDrawable) image).getBitmap();
        Bitmap bitmapResized = Bitmap.createScaledBitmap(b, pixel_size_icon, pixel_size_icon, false);
        return new BitmapDrawable(mC.getResources(), bitmapResized);
    }

    public void getDamageLine() {
        nD10 = 0;
        nD8 = 0;
        nD6 = 0;
        sumPhy=0;
        sumFire=0;
        selectedRolls=new ArrayList<>();
        for (Roll roll : atksRolls) {
            if (!roll.isHitConfirmed() || roll.isInvalid()) {
                continue;
            }
            selectedRolls.add(roll);
            roll.setDmgRand();
            roll.isDelt();
            List<ImageView> l10=roll.getDmgDiceImgList(10);
            nD10+=l10.size();
            List<ImageView> l8=roll.getDmgDiceImgList(8);
            nD8+=l8.size();
            List<ImageView> l6=roll.getDmgDiceImgList(6);
            nD6+=l6.size();

            sumPhy+=roll.getSumPhy();
            sumFire+=roll.getSumFire();
        }
        if (manualDiceDmg && !inputDone){
            putDicesSummary();
        } else {
            printResult();
        }
        combatLauncherDamageDetailDialog =new CombatLauncherDamageDetailDialog(mA,mC,selectedRolls);
    }

    public void printResult() {
        TextView damageLineTitle = mainView.findViewById(R.id.combat_dialog_dmg_title);
        damageLineTitle.setVisibility(View.VISIBLE);
        LinearLayout damageLine = mainView.findViewById(R.id.combat_dialog_dmg);
        damageLine.removeAllViews();
        damageLine.setVisibility(View.VISIBLE);
        if(sumPhy+sumFire>0){
            LinearLayout frame = getFrameSummary();
            if(sumPhy>0) {
                TextView sumPhyTxt = new TextView(mC);
                sumPhyTxt.setGravity(Gravity.CENTER);
                sumPhyTxt.setText(String.valueOf(sumPhy));
                sumPhyTxt.setCompoundDrawablesWithIntrinsicBounds(null, null, resize(R.drawable.phy_dmg_type, (int) mC.getResources().getDimension(R.dimen.combat_launcher_dmg_type_icon_size)), null);
                sumPhyTxt.setTextSize(18);
                sumPhyTxt.setTypeface(null, Typeface.BOLD);
                frame.addView(sumPhyTxt);
            }
            if(sumFire>0) {
                TextView sumFireTxt = new TextView(mC);
                sumFireTxt.setGravity(Gravity.CENTER);
                sumFireTxt.setText(String.valueOf(sumFire));
                sumFireTxt.setCompoundDrawablesWithIntrinsicBounds(null, null, resize(R.drawable.fire_dmg_type, (int) mC.getResources().getDimension(R.dimen.combat_launcher_dmg_type_icon_size)), null);
                sumFireTxt.setTextSize(18);
                sumFireTxt.setTypeface(null, Typeface.BOLD);
                frame.addView(sumFireTxt);
            }
            damageLine.addView(frame);
            setSummaryListnerToShowStats(frame);
        } else {
            damageLineTitle.setVisibility(View.VISIBLE);
            TextView noDmg=new TextView(mC);
            noDmg.setTextSize(20);
            noDmg.setText("Aucun dégats infligé");
            damageLine.addView(noDmg);
        }
    }

    private void putDicesSummary() {
        TextView damageLineTitle = mainView.findViewById(R.id.combat_dialog_dmg_title);
        LinearLayout damageLine = mainView.findViewById(R.id.combat_dialog_dmg);
        damageLine.removeAllViews();
        if(nD10+nD8+nD6>0){
            damageLine.setVisibility(View.VISIBLE);
            damageLineTitle.setVisibility(View.VISIBLE);
            LinearLayout summary = getFrameSummary();

            if(nD10>0){
                TextView nd10Text = new TextView(mC);
                nd10Text.setGravity(Gravity.CENTER);
                nd10Text.setText(nD10+"d10");
                nd10Text.setTextSize(18);
                nd10Text.setTypeface(null, Typeface.BOLD);
                nd10Text.setCompoundDrawablesWithIntrinsicBounds(resize(R.drawable.d10_main, mC.getResources().getDimensionPixelSize(R.dimen.icon_main_dices_combat_launcher_size)),null,null,null);
                summary.addView(nd10Text);
            }
            if(nD8>0){
                TextView nd8Text = new TextView(mC);
                nd8Text.setGravity(Gravity.CENTER);
                nd8Text.setText(nD8+"d8");
                nd8Text.setTextSize(18);
                nd8Text.setTypeface(null, Typeface.BOLD);
                nd8Text.setCompoundDrawablesWithIntrinsicBounds(resize(R.drawable.d8_main, mC.getResources().getDimensionPixelSize(R.dimen.icon_main_dices_combat_launcher_size)),null,null,null);
                summary.addView(nd8Text);
            }
            if(nD6>0){
                TextView nd6Text = new TextView(mC);
                nd6Text.setGravity(Gravity.CENTER);
                nd6Text.setText(nD6+"d6");
                nd6Text.setTextSize(18);
                nd6Text.setTypeface(null, Typeface.BOLD);
                nd6Text.setCompoundDrawablesWithIntrinsicBounds(resize(R.drawable.d6_main, mC.getResources().getDimensionPixelSize(R.dimen.icon_main_dices_combat_launcher_size)),null,null,null);
                summary.addView(nd6Text);
            }
            setSummaryListnerToInputManualDmg(summary);
            damageLine.addView(summary);
        } else {
            damageLine.setVisibility(View.VISIBLE);
            damageLineTitle.setVisibility(View.VISIBLE);
            TextView noDice=new TextView(mC);
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
                ProbaFromDiceRand proba = new ProbaFromDiceRand(selectedRolls);
                toastIt(proba.getPhysicalRange(mC));
                toastIt(proba.getPhysicalProba());
            }
        });
    }

    private void toastIt(String txt) {
        Toast toast = Toast.makeText(mC, txt, Toast.LENGTH_LONG);
        TextView v = (TextView) toast.getView().findViewById(android.R.id.message);
        if( v != null) v.setGravity(Gravity.CENTER);
        toast.setGravity(Gravity.CENTER,0,0);
        toast.show();
    }

    private LinearLayout getFrameSummary() {
        LinearLayout frame =new LinearLayout(mC);
        frame.setOrientation(LinearLayout.HORIZONTAL);
        frame.setGravity(Gravity.CENTER);
        int margin= (int) mC.getResources().getDimension(R.dimen.general_margin);
        frame.setPadding(margin,margin,margin,margin);
        frame.setBackground(mC.getDrawable(R.drawable.background_border_dice_list_summary));
        frame.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        return frame;
    }

    public List<Roll> getSelectedRolls() {
         return  selectedRolls;
    }

    public int getSelectedRollsNdices() {
        return  nD6+nD8+nD10;
    }

    public CombatLauncherDamageDetailDialog getDamageDetailDialog() {
        return combatLauncherDamageDetailDialog;
    }

    public void inputDone(){
        this.inputDone=true;
        nD10 = 0;
        nD8 = 0;
        nD6 = 0;
        sumPhy=0;
        sumFire=0;
        for (Roll roll : selectedRolls) {
            List<ImageView> l10=roll.getDmgDiceImgList(10);
            nD10+=l10.size();
            List<ImageView> l8=roll.getDmgDiceImgList(8);
            nD8+=l8.size();
            List<ImageView> l6=roll.getDmgDiceImgList(6);
            nD6+=l6.size();
            sumPhy+=roll.getSumPhy();
            sumFire+=roll.getSumFire();
        }
        printResult();
    }
}
