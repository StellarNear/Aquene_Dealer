package stellarnear.aquene_dealer.Divers.Rolls;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.widget.CheckBox;
import android.widget.CompoundButton;

import stellarnear.aquene_dealer.Activities.MainActivity;
import stellarnear.aquene_dealer.Divers.Tools;
import stellarnear.aquene_dealer.Perso.Perso;
import stellarnear.aquene_dealer.R;

public class AtkRoll {
    private Dice atkDice;

    private Integer preRandValue = 0;
    private Integer atk = 0;

    private Boolean miss = false;
    private Boolean hitConfirmed = false;
    private Boolean crit = false;
    private Boolean critConfirmed = false;
    private Boolean fail = false;
    private Boolean invalid = false;
    private Context mC;
    private Activity mA;

    private Boolean manualDice;
    private Boolean fromCharge=false;
    private SharedPreferences settings;
    private Perso aquene = MainActivity.aquene;
    private CheckBox hitCheckbox;
    private CheckBox critCheckbox;
    private OnRefreshEventListener mListener;
    private Tools tools=new Tools();

    public AtkRoll(Activity mA,Context mC, Integer base) {
        this.mA= mA;
        this.mC = mC;
        this.atkDice = new Dice(mA,mC,20);

        settings = PreferenceManager.getDefaultSharedPreferences(mC);
        manualDice = settings.getBoolean("switch_manual_diceroll", mC.getResources().getBoolean(R.bool.switch_manual_diceroll_DEF));
        this.preRandValue = base + getBonusAtk();

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
                    CritConfirmAlertDialog contactDialog = new CritConfirmAlertDialog(mA,mC,preRandValue);
                    contactDialog.showAlertDialog();
                    contactDialog.setSuccessEventListener(new CritConfirmAlertDialog.OnSuccessEventListener() {
                        @Override
                        public void onSuccessEvent() {
                            hitCheckbox.setChecked(true);
                            critConfirmed = true;
                            if(atkDice.getRandValue()==20){
                                aquene.getAllResources().getResource("resource_ki").earn(1);
                                tools.customToast(mC,"Grace à Aaleilis tu gagnes un point de Ki !","center");
                            }
                        }
                    });
                    contactDialog.setFailEventListener(new CritConfirmAlertDialog.OnFailEventListener() {
                        @Override
                        public void onFailEvent() {
                            critCheckbox.setChecked(false);
                            critConfirmed = false;
                        }
                    });
                }
            }
        });
    }

    private int getBonusAtk() {
        int bonusAtk = 0;
        bonusAtk += tools.toInt(settings.getString("bonus_temp_jet_att", String.valueOf(mC.getResources().getInteger(R.integer.bonus_temp_jet_att_DEF))));
        if (aquene.getAllStances().isActive("stance_rat") && (aquene.getAbilityMod("ability_dexterite") > aquene.getAbilityMod("ability_force")) ) {
            bonusAtk += aquene.getAbilityMod("ability_dexterite");
        } else if (aquene.getAllStances().isActive("stance_dragon") && (aquene.getAbilityMod("ability_sagesse") > aquene.getAbilityMod("ability_force")) ) {
            bonusAtk += aquene.getAbilityMod("ability_sagesse");
        } else {
            bonusAtk += aquene.getAbilityMod("ability_force");
        }
        if (aquene.getInventory().getAllEquipments().testIfNameItemIsEquipped("Amulette des poings invincibles d'allonge de feu (+5)")) {
            bonusAtk += 5;
        }
        if (aquene.getInventory().getAllEquipments().testIfNameItemIsEquipped("Cestes vampirique (+5)")) {
            bonusAtk += 5;
        }
        if ( settings.getBoolean("switch_temp_rapid",mC.getResources().getBoolean(R.bool.switch_temp_rapid_DEF))|| settings.getBoolean("switch_blinding_speed",mC.getResources().getBoolean(R.bool.switch_blinding_speed_DEF))) {
            bonusAtk += 1;
        }

        return bonusAtk;
    }

    //getters

    public Dice getDice() {
        return this.atkDice;
    }

    public Integer getPreRandValue() {
        if(fromCharge){this.preRandValue+=2;}
        setAtkRand();
        return preRandValue;
    }

    private void setAtkRand() {
        atkDice.rand(manualDice);
        if (manualDice) {
            atkDice.setRefreshEventListener(new Dice.OnRefreshEventListener() {
                @Override
                public void onEvent() {
                    calculAtk();
                    if(mListener!=null){mListener.onEvent();}
                }
            });
        } else {
            calculAtk();
        }
    }

    private void calculAtk() {
        this.atk = this.preRandValue + atkDice.getRandValue();
        if (atkDice.getRandValue() == 1 && !aquene.getAllMythicCapacities().mythicCapacityIsActive("mythiccapacity_still_a_chance")) {
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
        if (atkDice.getRandValue() >= critMin) {
            this.crit = true;
        }
    }

    public void setFromCharge() {
        this.fromCharge=true;
    }

    public void setMiss(){
        this.miss=true;
    }

    public boolean isMissed(){
        return this.miss;
    }

    public Dice getAtkDice() {
        return this.atkDice;
    }


    public interface OnRefreshEventListener {
        void onEvent();
    }

    public void setRefreshEventListener(OnRefreshEventListener eventListener) {
        mListener = eventListener;
    }


    // straigth getters

    public Integer getValue() {
        calculAtk();
        return atk;
    }

    public Boolean isInvalid() {
        return invalid;
    }

    public void invalidated() {
        this.invalid = true;
        atkDice.getImg().setImageDrawable(tools.resize(mC,R.drawable.d20_fail, mC.getResources().getDimensionPixelSize(R.dimen.icon_main_dices_combat_launcher_size)));
        atkDice.getImg().setOnClickListener(null);
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

}
