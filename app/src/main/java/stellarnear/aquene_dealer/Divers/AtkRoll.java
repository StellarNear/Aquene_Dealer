package stellarnear.aquene_dealer.Divers;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import java.util.Random;

import stellarnear.aquene_dealer.Activities.MainActivity;
import stellarnear.aquene_dealer.Perso.Perso;
import stellarnear.aquene_dealer.R;

public class AtkRoll {
    private Integer base = 0;
    private Integer preRandValue = 0;
    private Integer randAtk = 0;
    private Integer atk = 0;
    private ImageView imgAtk = null;
    private Boolean hitConfirmed = false;
    private Boolean crit = false;
    private Boolean critConfirmed = false;
    private Boolean fail = false;
    private Boolean invalid = false;
    private Context mC;
    private Activity mA;
    private Boolean manualDice;
    private Boolean amulette;
    private Boolean aldrassil;
    private SharedPreferences settings;
    private Perso aquene = MainActivity.aquene;
    private CheckBox hitCheckbox;
    private CheckBox critCheckbox;
    private OnRefreshEventListener mListener;

    public AtkRoll(Activity mA, Context mC, Integer base) {
        this.mA = mA;
        this.mC = mC;
        this.base = base;
        this.imgAtk = new ImageView(mC);
        settings = PreferenceManager.getDefaultSharedPreferences(mC);
        manualDice = settings.getBoolean("switch_manual_diceroll", mC.getResources().getBoolean(R.bool.switch_manual_diceroll_DEF));
        aldrassil = settings.getBoolean("switch_aldrassil", mC.getResources().getBoolean(R.bool.switch_aldrassil_DEF));
        amulette = settings.getBoolean("switch_amulette", mC.getResources().getBoolean(R.bool.switch_amulette_DEF));
        this.preRandValue = base + getBonusAtk();

        setAtkRand();
        constructCheckboxes();
    }

    public void setAtkRand() {
        if (manualDice) {
            this.imgAtk.setImageDrawable(resize(R.drawable.d20_main, mC.getResources().getDimensionPixelSize(R.dimen.icon_main_dices_combat_launcher_size)));
            setDiceImgListner(this.imgAtk, 20);
        } else {
            Random rand = new Random();
            int val_dice = 1 + rand.nextInt(20);
            this.randAtk = val_dice;
            setAtkDiceImg();
            calculAtk();
        }
    }

    public void setAtkRand(int randFromWheel) { //surcharge pour le retour depuis wheelpicker
        this.randAtk = randFromWheel;
        setAtkDiceImg();
        calculAtk();
        mListener.onEvent();
    }


    public interface OnRefreshEventListener {
        void onEvent();
    }

    public void setRefreshEventListener(OnRefreshEventListener eventListener) {
        mListener = eventListener;
    }

    private void setDiceImgListner(ImageView imgDice, final int dice) {
        imgDice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new DiceDealerDialog(mA, mC, AtkRoll.this, dice);
            }
        });
    }

    private void setAtkDiceImg() {
        int drawableId = mC.getResources().getIdentifier("d20_" + String.valueOf(this.randAtk), "drawable", mC.getPackageName());
        this.imgAtk.setImageDrawable(resize(drawableId, mC.getResources().getDimensionPixelSize(R.dimen.icon_main_dices_combat_launcher_size)));
        this.imgAtk.setOnClickListener(null);
    }

    private void constructCheckboxes() {
        hitCheckbox = new CheckBox(mC);
        hitCheckbox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                hitConfirmed = false;
                if (isChecked) {
                    hitConfirmed = true;
                }
            }
        });
        critCheckbox = new CheckBox(mC);
        critCheckbox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                critConfirmed = false;
                if (isChecked) {
                    hitCheckbox.setChecked(true);
                    critConfirmed = true;
                }
            }
        });
    }

    private void calculAtk() {
        this.atk = base + getBonusAtk() + randAtk;
        if (randAtk == 1) {
            this.fail = true;
        }
        int critSup = (aquene.featIsActive("feat_crit_sup")) ? 1 : 0;
        int critKeen = (aquene.featIsActive("feat_keen_strike")) ? 1 : 0;
        int critMin;
        if (aquene.featIsActive("feat_crit_sup") && aquene.featIsActive("feat_keen_strike")) {
            critMin = 21 - critSup * 2 * critKeen * 3;
        } else if (aquene.featIsActive("feat_crit_sup") || aquene.featIsActive("feat_keen_strike")) {
            critMin = 21 - critSup * 2 - critKeen * 3;
        } else {
            critMin = 20;
        }
        if (randAtk >= critMin) {
            this.crit = true;
        }
    }

    private int getBonusAtk() {
        int bonusAtk = 0;
        bonusAtk += toInt(settings.getString("bonus_jet_att", String.valueOf(mC.getResources().getInteger(R.integer.bonus_jet_att_DEF))));
        if (aquene.getAllStances().isActive("stance_lion")) {
            bonusAtk += (int) (1.5 * aquene.getAbilityMod("ability_force"));
        } else {
            bonusAtk += aquene.getAbilityMod("ability_force");
        }
        if (aldrassil) {
            bonusAtk += 2;
        }
        if (amulette) {
            bonusAtk += 5;
        }
        return bonusAtk;
    }
    //getters

    public ImageView getImgAtk() {
        return this.imgAtk;
    }

    public Integer getPreRandValue() {
        return preRandValue;
    }

    public Integer getValue() {
        return atk;
    }

    public Boolean isInvalid() {
        return invalid;
    }

    public void invalidated() {
        this.invalid = true;
        this.imgAtk.setImageDrawable(resize(R.drawable.d20_fail, mC.getResources().getDimensionPixelSize(R.dimen.icon_main_dices_combat_launcher_size)));
        this.imgAtk.setOnClickListener(null);
    }

    public boolean isFailed() {
        return this.fail;
    }

    public boolean isCrit() {
        return this.crit;
    }

    public void isDelt() {
        invalid = true;
        hitCheckbox.setEnabled(false);
        critCheckbox.setEnabled(false);
    }

    public boolean isHitConfirmed() {
        return this.hitConfirmed;
    }

    public boolean isCritConfirmed() {
        return this.critConfirmed;
    }

    public CheckBox getHitCheckbox() {
        return this.hitCheckbox;
    }

    public CheckBox getCritCheckbox() {
        return this.critCheckbox;
    }

    //tools
    private Integer toInt(String key) {
        Integer value = 0;
        try {
            value = Integer.parseInt(key);
        } catch (Exception e) {
        }
        return value;
    }

    private Drawable resize(int imageId, int pixel_size_icon) {
        Drawable image = mC.getDrawable(imageId);
        Bitmap b = ((BitmapDrawable) image).getBitmap();
        Bitmap bitmapResized = Bitmap.createScaledBitmap(b, pixel_size_icon, pixel_size_icon, false);
        return new BitmapDrawable(mC.getResources(), bitmapResized);
    }

}
