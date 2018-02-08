package stellarnear.aquene_dealer.Divers;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Color;
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
import android.widget.CompoundButton;
import android.widget.FrameLayout;
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

public class CombatLauncherLines {
    private List<Roll>atksRolls;
    private Context mC;
    private Activity mA;
    private View mainView;
    private Boolean manualDice;
    private Boolean megaFail=false;
    private Perso aquene= MainActivity.aquene;
    public CombatLauncherLines(Activity mA,Context mC, View mainView, List<Roll> atksRolls) {
        this.mA=mA;
        this.mC=mC;
        this.mainView =mainView;
        this.atksRolls=atksRolls;
        SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(mC);
        this.manualDice = settings.getBoolean("switch_manual_diceroll", mC.getResources().getBoolean(R.bool.switch_manual_diceroll_DEF));

    }

    public void getPreRandValues() {
        LinearLayout line = mainView.findViewById(R.id.combat_dialog_prerand_value);
        line.removeAllViews();
        for (Roll roll : atksRolls) {
            TextView atkTxt = new TextView(mC);
            atkTxt.setText("+" + roll.getPreRandValue());
            atkTxt.setGravity(Gravity.CENTER);
            atkTxt.setLayoutParams(new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT, 1));
            line.addView(atkTxt);
        }
    }

    public void getRandValues() {
        LinearLayout line = mainView.findViewById(R.id.combat_dialog_atk_dices);
        line.removeAllViews();
        Boolean fail=false;
        for (Roll roll : atksRolls) {
            ImageView dice_img = new ImageView(mC);
            dice_img.setLayoutParams(new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT, 1));
            if (fail) {
                roll.invalidated();
                dice_img.setImageDrawable(resize(R.drawable.mire_test, mC.getResources().getDimensionPixelSize(R.dimen.icon_main_dices_combat_launcher_size)));
            } else {
                if (manualDice) {
                    if(roll.isUnset()){
                        dice_img.setImageDrawable(resize(R.drawable.d20_main, mC.getResources().getDimensionPixelSize(R.dimen.icon_main_dices_combat_launcher_size)));
                        setAttackDiceListner(dice_img, roll);
                    } else {
                        int drawableId = mC.getResources().getIdentifier("d20_" + String.valueOf(roll.getRand()), "drawable", mC.getPackageName());
                        dice_img.setImageDrawable(resize(drawableId, mC.getResources().getDimensionPixelSize(R.dimen.icon_main_dices_combat_launcher_size)));
                    }
                    if(roll.isFailed()){fail=true;}
                } else {
                    Random rand = new Random();
                    int val_dice = 1 + rand.nextInt(20);
                    roll.setRand(val_dice);
                    if (roll.isFailed()) {  fail = true;  }
                    int drawableId = mC.getResources().getIdentifier("d20_" + String.valueOf(val_dice), "drawable", mC.getPackageName());
                    dice_img.setImageDrawable(resize(drawableId, mC.getResources().getDimensionPixelSize(R.dimen.icon_main_dices_combat_launcher_size)));
                }
            }
            line.addView(dice_img);
        }
        buildPostRandValues();
    }

    private void buildPostRandValues() {
        getPostRandValues();
    }

    private void setAttackDiceListner(final ImageView dice_img, final Roll roll) {
        dice_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                buildAlertDialogWheelPicker(dice_img, roll);
            }
        });
    }

    private void buildAlertDialogWheelPicker(final ImageView dice_img, final Roll roll) {
        LayoutInflater inflater = mA.getLayoutInflater();
        View dialogViewWheelPicker = inflater.inflate(R.layout.custom_dialog_wheel_picker, null);
        RelativeLayout relativeCenter = dialogViewWheelPicker.findViewById(R.id.relative_custom_dialog_center);
        final WheelDicePicker wheelPicker = new WheelDicePicker(relativeCenter, 20, mC);
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(mA, R.style.CustomDialog);
        dialogBuilder.setView(dialogViewWheelPicker);
        dialogBuilder.setPositiveButton("Valider", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                int drawableId = mC.getResources().getIdentifier("d20_" + String.valueOf(wheelPicker.getValue_selected()), "drawable", mC.getPackageName());
                dice_img.setImageDrawable(resize(drawableId, mC.getResources().getDimensionPixelSize(R.dimen.icon_main_dices_combat_launcher_size)));
                roll.setRand(wheelPicker.getValue_selected());
                dice_img.setOnClickListener(null);
                getRandValues();
                buildPostRandValues();
            }
        });
        dialogBuilder.setNegativeButton("Annuler", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
            }
        });
        AlertDialog alertDialogWheelPicker = dialogBuilder.create();
        showDialogWheelPicker(alertDialogWheelPicker);

    }

    private void showDialogWheelPicker(AlertDialog alertDialogWheelPicker) {
        alertDialogWheelPicker.show();
        Display display = mA.getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        Float factor = (mC.getResources().getInteger(R.integer.percent_fullscreen_combat_launcher_dialog) - 5) / 100f;
        alertDialogWheelPicker.getWindow().setLayout((int) (factor * size.x), (int) (factor * size.y));

        Button positiveButton = alertDialogWheelPicker.getButton(AlertDialog.BUTTON_POSITIVE);
        LinearLayout.LayoutParams positiveButtonLL = (LinearLayout.LayoutParams) positiveButton.getLayoutParams();
        positiveButtonLL.width = ViewGroup.LayoutParams.WRAP_CONTENT;
        positiveButtonLL.setMargins(mC.getResources().getDimensionPixelSize(R.dimen.general_margin), 0, 0, 0);
        positiveButton.setLayoutParams(positiveButtonLL);
        positiveButton.setTextColor(mC.getColor(R.color.colorBackground));
        positiveButton.setBackground(mC.getDrawable(R.drawable.button_ok_gradient));

        Button negativeButton = alertDialogWheelPicker.getButton(AlertDialog.BUTTON_NEGATIVE);
        LinearLayout.LayoutParams negativeButtonLL = (LinearLayout.LayoutParams) negativeButton.getLayoutParams();
        negativeButtonLL.width = ViewGroup.LayoutParams.WRAP_CONTENT;
        negativeButton.setLayoutParams(negativeButtonLL);
        negativeButton.setTextColor(mC.getColor(R.color.colorBackground));
        negativeButton.setBackground(mC.getDrawable(R.drawable.button_cancel_gradient));
    }

    public void getPostRandValues(){
        LinearLayout line = mainView.findViewById(R.id.combat_dialog_atk_value);
        line.removeAllViews();
        for (Roll roll : atksRolls) {
            TextView atkTxt = new TextView(mC);
            atkTxt.setText("?");
            if (roll.isInvalid()) {
                atkTxt.setText("-");
            } else {
                if ((roll.getValue() != 0)) {
                    atkTxt.setText("+"+String.valueOf(roll.getValue()));
                }
            }
            atkTxt.setGravity(Gravity.CENTER);
            atkTxt.setLayoutParams(new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT, 1));
            line.addView(atkTxt);
        }
        if (atksRolls.get(atksRolls.size()-1).getValue()!=0 || atksRolls.get(atksRolls.size()-1).isInvalid()){
            getHitAndCritLines();
        }
    }

    private void getHitAndCritLines() {
        Boolean anyCrit = false;
        Boolean anyHit = false;
        for (Roll roll : atksRolls){
            if(roll.getValue()!=0){anyHit=true;}
            if (roll.isCrit()){anyCrit=true;}
        }
        TextView title = mainView.findViewById(R.id.combat_dialog_hit_box_title);
        LinearLayout line = mainView.findViewById(R.id.combat_dialog_hit_box);
        title.setVisibility(View.GONE);
        line.setVisibility(View.GONE);
        if(anyHit) {
            title.setVisibility(View.VISIBLE);
            line.setVisibility(View.VISIBLE);
            line.removeAllViews();
            for (final Roll roll : atksRolls) {
                LinearLayout frame = new LinearLayout(mC);
                frame.setGravity(Gravity.CENTER);
                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT, 1);
                frame.setLayoutParams(params);
                if (!roll.isInvalid() && !roll.isFailed()) {
                    CheckBox check = new CheckBox(mC);
                    check.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener()
                    {
                        @Override
                        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked)
                        {
                            if ( isChecked )
                            {
                                roll.setHitConfirmed(true);
                            }
                            roll.setHitConfirmed(false);
                        }
                    });
                    frame.addView(check);
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
            titleCrit.startAnimation(anim);
            lineCrit.setVisibility(View.VISIBLE);
            lineCrit.removeAllViews();
            for (final Roll roll : atksRolls) {
                LinearLayout frame = new LinearLayout(mC);
                frame.setGravity(Gravity.CENTER);
                frame.setLayoutParams(new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT, 1));
                if (!roll.isInvalid() && !roll.isFailed() && roll.isCrit()) {
                    CheckBox check = new CheckBox(mC);
                    frame.addView(check);
                    check.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener()
                    {
                        @Override
                        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked)
                        {
                            if ( isChecked )
                            {
                                roll.setCritConfirmed(true);
                            }
                            roll.setCritConfirmed(false);
                        }
                    });
                    Animation animCheck = AnimationUtils.loadAnimation(mC,R.anim.zoomin);
                    check.startAnimation(animCheck);
                }
                lineCrit.addView(frame);
            }
            TextView titleCritConfirm=mainView.findViewById(R.id.combat_dialog_crit_confirm_title);
            LinearLayout critConfirm=mainView.findViewById(R.id.combat_dialog_crit_confirm);
            titleCritConfirm.setVisibility(View.GONE);
            critConfirm.setVisibility(View.GONE);
            if(aquene.featIsActive("feat_crit")){
                titleCritConfirm.setVisibility(View.VISIBLE);
                critConfirm.setVisibility(View.VISIBLE);
                getCritConfirmLine();}
        }
    }

    private void getCritConfirmLine() {
        LinearLayout line = mainView.findViewById(R.id.combat_dialog_crit_confirm);
        line.removeAllViews();
        for (Roll roll : atksRolls) {
            TextView atkTxt = new TextView(mC);
            atkTxt.setText("");
            if (roll.isCrit()){
                if ((roll.getValue() != 0)) {
                    atkTxt.setText("+"+String.valueOf(roll.getValue()+4));
                }
            }
            atkTxt.setGravity(Gravity.CENTER);
            atkTxt.setLayoutParams(new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT, 1));
            line.addView(atkTxt);
        }
    }

    public boolean isMegaFail(){
        return this.megaFail;
    }

    private Drawable resize(int imageId, int pixel_size_icon) {
        Drawable image = mC.getDrawable(imageId);
        Bitmap b = ((BitmapDrawable) image).getBitmap();
        Bitmap bitmapResized = Bitmap.createScaledBitmap(b, pixel_size_icon, pixel_size_icon, false);
        return new BitmapDrawable(mC.getResources(), bitmapResized);
    }



}
