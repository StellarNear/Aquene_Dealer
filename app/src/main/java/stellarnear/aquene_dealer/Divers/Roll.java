package stellarnear.aquene_dealer.Divers;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.media.Image;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import stellarnear.aquene_dealer.Activities.MainActivity;
import stellarnear.aquene_dealer.Perso.Perso;
import stellarnear.aquene_dealer.R;

public class Roll {
    private Integer base = 0;
    private Integer randAtk = 0;
    private Integer preRandValue = 0;
    private Integer atk = 0;
    private ImageView imgAtk = null;
    private Integer sumPhy = 0;
    private List<Integer> nD10 = new ArrayList<>();
    private List<ImageView> listImgDmg10 = new ArrayList<>();
    private List<ImageView> listImgDmg8 = new ArrayList<>();
    private List<ImageView> listImgDmg6 = new ArrayList<>();
    private Integer sumFire = 0;
    private Boolean hitConfirmed = false;
    private Boolean crit = false;
    private Boolean critConfirmed = false;
    private Boolean fail = false;
    private Boolean invalid = false;
    private Context mC;
    private Activity mA;
    private Boolean manualDice;
    private Boolean manualDiceDmg;
    private Boolean amulette;
    private Boolean aldrassil;
    private SharedPreferences settings;
    private Perso aquene = MainActivity.aquene;
    private CheckBox hitCheckbox;
    private CheckBox critCheckbox;
    private OnRefreshEventListener mListener;

    public Roll(Activity mA, Context mC, Integer base) {
        this.mA = mA;
        this.mC = mC;
        this.base = base;
        this.imgAtk = new ImageView(mC);
        settings = PreferenceManager.getDefaultSharedPreferences(mC);
        manualDice = settings.getBoolean("switch_manual_diceroll", mC.getResources().getBoolean(R.bool.switch_manual_diceroll_DEF));
        manualDiceDmg = settings.getBoolean("switch_manual_diceroll_damage", mC.getResources().getBoolean(R.bool.switch_manual_diceroll_damage_DEF));
        aldrassil = settings.getBoolean("switch_aldrassil", mC.getResources().getBoolean(R.bool.switch_aldrassil_DEF));
        amulette = settings.getBoolean("switch_amulette", mC.getResources().getBoolean(R.bool.switch_amulette_DEF));
        this.preRandValue = base + getBonusAtk();

        setAtkRand();
        constructCheckboxes();
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

    public void setRand(ImageView img, int dice, int randFromWheel) {//retour du wheelpicker
        if (dice == 20) {
            this.randAtk = randFromWheel;
            setAtkDiceImg();
            calculAtk();
        }
        if (dice == 10) {
            this.nD10.add(dice);
            if (this.nD10.size() ==2) {
                if (critConfirmed) {
                    sumPhy += (nD10.get(0) + nD10.get(1) + getBonusDmg()) * 2;
                } else {
                    sumPhy += nD10.get(0) + nD10.get(1) + getBonusDmg();
                }
            }
            addDmgDiceImg(img, dice, randFromWheel);
        }
        if (dice == 8) {
            this.sumPhy += randFromWheel;
            addDmgDiceImg(img, dice, randFromWheel);
        }
        if (dice == 6) {
            this.sumFire += randFromWheel;
            addDmgDiceImg(img, dice, randFromWheel);
        }
        mListener.onEvent(); //on a refresh une valeur de dès
    }

    private void setDiceImgListner(final ImageView imgDice, final int dice) {
        imgDice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new DiceDealerDialog(mA, mC, Roll.this, imgDice, dice);
            }
        });
    }

    private void setAtkDiceImg() {
        int drawableId = mC.getResources().getIdentifier("d20_" + String.valueOf(this.randAtk), "drawable", mC.getPackageName());
        this.imgAtk.setImageDrawable(resize(drawableId, mC.getResources().getDimensionPixelSize(R.dimen.icon_main_dices_combat_launcher_size)));
        this.imgAtk.setOnClickListener(null);
    }

    private void addDmgDiceImg(ImageView diceImg, int dice, int diceValue) {
        int drawableId = mC.getResources().getIdentifier("d" + dice + "_" + String.valueOf(diceValue), "drawable", mC.getPackageName());
        diceImg.setImageDrawable(resize(drawableId, mC.getResources().getDimensionPixelSize(R.dimen.icon_main_dices_combat_launcher_size)));
        diceImg.setOnClickListener(null);
        if (dice == 10) {
            listImgDmg10.add(diceImg);
        }
        if (dice == 8) {
            listImgDmg8.add(diceImg);
        }
        if (dice == 6) {
            listImgDmg6.add(diceImg);
        }
    }

    public List<ImageView> getDmgDiceImgList(int dice) {
        List<ImageView> list=null;
        if (dice == 10) {
            list=listImgDmg10;
        }
        if (dice == 8) {
            list=listImgDmg8;
        }
        if (dice == 6) {
            list=listImgDmg6;
        }
        return list;
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

    private int getBonusDmg() {
        int bonusDmg = 0;
        bonusDmg += toInt(settings.getString("bonus_jet_dmg", String.valueOf(mC.getResources().getInteger(R.integer.bonus_jet_dmg_DEF))));
        if (aquene.getAllStances().isActive("stance_lion")) {
            bonusDmg += (int) (1.5 * aquene.getAbilityMod("ability_force"));
        } else {
            bonusDmg += aquene.getAbilityMod("ability_force");
        }
        if (aldrassil) {
            bonusDmg += 2;
        }
        if (amulette) {
            bonusDmg += 5;
        }
        return bonusDmg;
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

    public boolean isHitConfirmed() {
        return this.hitConfirmed;
    }

    public CheckBox getHitCheckbox() {
        return this.hitCheckbox;
    }

    public CheckBox getCritCheckbox() {
        return this.critCheckbox;
    }

    public Integer getSumPhy() {
        return this.sumPhy;
    }

    public Integer getSumFire() {
        return this.sumFire;
    }

    public ImageView getImgAtk() {
        return this.imgAtk;
    }

    public void isDelt() {
        invalid = true;
        hitCheckbox.setEnabled(false);
        critCheckbox.setEnabled(false);
    }

    public void setDmgRand() {
        if (manualDiceDmg) {
            ImageView img1D10 = new ImageView(mC);
            img1D10.setImageDrawable(resize(R.drawable.d10_main, mC.getResources().getDimensionPixelSize(R.dimen.icon_main_dices_combat_launcher_size)));
            ImageView img2D10 = new ImageView(mC);
            img2D10.setImageDrawable(resize(R.drawable.d10_main, mC.getResources().getDimensionPixelSize(R.dimen.icon_main_dices_combat_launcher_size)));
            listImgDmg10.add(img1D10);
            listImgDmg10.add(img2D10);

            if (aldrassil) {
                ImageView imgD8 = new ImageView(mC);
                imgD8.setImageDrawable(resize(R.drawable.d8_main, mC.getResources().getDimensionPixelSize(R.dimen.icon_main_dices_combat_launcher_size)));
                listImgDmg8.add(imgD8);
            }
            if (amulette) {
                ImageView imgD6 = new ImageView(mC);
                imgD6.setImageDrawable(resize(R.drawable.d6_main, mC.getResources().getDimensionPixelSize(R.dimen.icon_main_dices_combat_launcher_size)));
                listImgDmg6.add(imgD6);
            }
            setImgDmgListner();
        } else {
            Random rand1D10 = new Random();
            int val1D10 = 1 + rand1D10.nextInt(10);
            Random rand2D10 = new Random();
            int val2D10 = 1 + rand2D10.nextInt(10);
            addDmgDiceImg(new ImageView(mC), 10, val1D10);
            addDmgDiceImg(new ImageView(mC), 10, val2D10);
            if (critConfirmed) {
                sumPhy += (val1D10 + val2D10 + getBonusDmg()) * 2;
            } else {
                sumPhy += val1D10 + val2D10 + getBonusDmg();
            }

            if (aldrassil) {
                Random randD8 = new Random();
                int valD8 = 1 + randD8.nextInt(8);
                addDmgDiceImg(new ImageView(mC), 8, valD8);
                sumPhy += (valD8);
            }
            if (amulette) {
                Random randD6 = new Random();
                int valD6 = 1 + randD6.nextInt(6);
                addDmgDiceImg(new ImageView(mC), 6, valD6);
                sumFire += (valD6);
            }
        }
    }

    private void setImgDmgListner() {
        for (ImageView img : listImgDmg10) {
            setDiceImgListner(img, 10);
        }
        for (ImageView img : listImgDmg8) {
            setDiceImgListner(img, 8);
        }
        for (ImageView img : listImgDmg6) {
            setDiceImgListner(img, 6);
        }
    }

    // UTILITAIRES
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

    public interface OnRefreshEventListener {
        void onEvent();
    }

    public void setRefreshEventListener(OnRefreshEventListener eventListener) {
        mListener = eventListener;
    }
}
