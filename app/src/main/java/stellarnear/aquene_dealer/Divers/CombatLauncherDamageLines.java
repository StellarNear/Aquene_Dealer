package stellarnear.aquene_dealer.Divers;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Point;
import android.graphics.Typeface;
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

public class CombatLauncherDamageLines {
    private List<Roll>atksRolls;
    private Context mC;
    private Activity mA;
    private View mainView;
    private Boolean manualDice;
    private AlertDialog diceList;
    private SharedPreferences settings;
    private Perso aquene= MainActivity.aquene;
    public CombatLauncherDamageLines(Activity mA, Context mC, View mainView, List<Roll> atksRolls) {
        this.mA=mA;
        this.mC=mC;
        this.mainView =mainView;
        this.atksRolls=atksRolls;
        settings = PreferenceManager.getDefaultSharedPreferences(mC);
        this.manualDice = settings.getBoolean("switch_manual_diceroll_damage", mC.getResources().getBoolean(R.bool.switch_manual_diceroll_damage_DEF));

    }
    

    private Drawable resize(int imageId, int pixel_size_icon) {
        Drawable image = mC.getDrawable(imageId);
        Bitmap b = ((BitmapDrawable) image).getBitmap();
        Bitmap bitmapResized = Bitmap.createScaledBitmap(b, pixel_size_icon, pixel_size_icon, false);
        return new BitmapDrawable(mC.getResources(), bitmapResized);
    }

    public void getDamageLine() {
        int nD10=0;
        int nD8=0;
        int nD6=0;
        if (manualDice){

        } else {
            for (Roll roll: atksRolls){
                if(!roll.isHitConfirmed() || roll.isInvalid()){
                    continue;
                }
                roll.isDelt();
                Random rand1D10 = new Random();
                int val1D10 = 1 + rand1D10.nextInt(10);
                Random rand2D10 = new Random();
                int val2D10 = 1 + rand2D10.nextInt(10);
                roll.setDmgRand(10,new int[]{val1D10,val2D10});
                nD10+=2;

                if(settings.getBoolean("switch_aldrassil", mC.getResources().getBoolean(R.bool.switch_aldrassil_DEF))) {
                    Random randD8 = new Random();
                    int valD8 = 1 + randD8.nextInt(8);
                    roll.setDmgRand(8, new int[]{valD8});
                    nD8+=1;
                }

                if(settings.getBoolean("switch_amulette", mC.getResources().getBoolean(R.bool.switch_amulette_DEF))){
                    Random randD6 = new Random();
                    int valD6 = 1 + randD6.nextInt(6);
                    roll.setDmgRand(6, new int[]{valD6});
                    nD6+=1;
                }
            }
        }
        putDicesSummary(nD10,nD8,nD6);
    }

    private void putDicesSummary(int nD10,int nD8,int nD6) {
        TextView damageLineTitle = mainView.findViewById(R.id.combat_dialog_dmg_title);
        LinearLayout damageLine = mainView.findViewById(R.id.combat_dialog_dmg);
        damageLine.removeAllViews();
        if(nD10+nD8+nD6>0){
            damageLine.setVisibility(View.VISIBLE);
            damageLineTitle.setVisibility(View.VISIBLE);
            LinearLayout summary = new LinearLayout(mC);
            summary.setOrientation(LinearLayout.HORIZONTAL);
            summary.setGravity(Gravity.CENTER);
            int margin= (int) mC.getResources().getDimension(R.dimen.general_margin);
            summary.setPadding(margin,margin,margin,margin);
            summary.setBackground(mC.getDrawable(R.drawable.background_border_dice_list_summary));
            summary.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));

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
            damageLine.addView(summary);
        } else {
            damageLine.setVisibility(View.VISIBLE);
            damageLineTitle.setVisibility(View.VISIBLE);
            TextView noDice=new TextView(mC);
            noDice.setTextSize(20);
            noDice.setText("Aucune attaque sélectionnée");
            damageLine.addView(noDice);

        }
    }

    private void putDicesImgs() {


        for (Roll roll: atksRolls){
            LinearLayout atkLine = new LinearLayout(mC);
            atkLine.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));

            for(int dmg10 : roll.getDmgDiceRand(10)){
                ImageView diceImg=new ImageView(mC);
                int drawableId = mC.getResources().getIdentifier("d10_" + String.valueOf(dmg10), "drawable", mC.getPackageName());
                diceImg.setImageDrawable(resize(drawableId, mC.getResources().getDimensionPixelSize(R.dimen.icon_main_dices_combat_launcher_size)));
                atkLine.addView(diceImg);
            }

            for(int dmg8 : roll.getDmgDiceRand(8)){
                ImageView diceImg=new ImageView(mC);
                int drawableId = mC.getResources().getIdentifier("d8_" + String.valueOf(dmg8), "drawable", mC.getPackageName());
                diceImg.setImageDrawable(resize(drawableId, mC.getResources().getDimensionPixelSize(R.dimen.icon_main_dices_combat_launcher_size)));
                atkLine.addView(diceImg);
            }

            for(int dmg6 : roll.getDmgDiceRand(6)){
                ImageView diceImg=new ImageView(mC);
                int drawableId = mC.getResources().getIdentifier("d6_" + String.valueOf(dmg6), "drawable", mC.getPackageName());
                diceImg.setImageDrawable(resize(drawableId, mC.getResources().getDimensionPixelSize(R.dimen.icon_main_dices_combat_launcher_size)));
                atkLine.addView(diceImg);
            }
            //damageLine.addView(atkLine);
        }
        showDialog();
    }

    private void showDialog() {
    }
}
