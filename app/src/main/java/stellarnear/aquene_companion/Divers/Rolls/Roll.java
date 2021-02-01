package stellarnear.aquene_companion.Divers.Rolls;

import android.app.Activity;
import android.content.Context;
import android.widget.CheckBox;

public class Roll {
    private AtkRoll atkRoll;
    private DmgRoll dmgRoll;
    private Activity mA;
    private Context mC;
    private int nthAtkRoll;

    public Roll(Activity mA, Context mC, Integer atkBase) {
        this.mA=mA;
        this.mC=mC;
        this.atkRoll=new AtkRoll(mA,mC,atkBase);
    }

    public AtkRoll getAtkRoll(){
        return this.atkRoll;
    }

    public void setDmgRand() {
        if (this.dmgRoll==null){
            this.dmgRoll=new DmgRoll(mA,mC,atkRoll.isCritConfirmed());
        }
        this.dmgRoll.setDmgRand();
    }
    public DmgRoll getDmgRoll(){
        return this.dmgRoll;
    }


    public Boolean isInvalid() {
        return atkRoll.isInvalid();
    }

    public void invalidated() {
        atkRoll.invalidated();
    }

    public boolean isFailed() {
        return atkRoll.isFailed();
    }

    public boolean isCrit() {
        return atkRoll.isCrit();
    }

    public boolean isHitConfirmed() {
        return atkRoll.isHitConfirmed();
    }

    public boolean isCritConfirmed() {
        return atkRoll.isCritConfirmed();
    }
    public Integer getPreRandValue() {
        return atkRoll.getPreRandValue();
    }
    public Dice20 getAtkDice() {
        return atkRoll.getDice();
    }
    public int getAtkValue() {
        return atkRoll.getValue();
    }
    public CheckBox getHitCheckbox() {
        return atkRoll.getHitCheckbox();
    }
    public CheckBox getCritCheckbox() {
        return atkRoll.getCritCheckbox();
    }

    public void isDelt() {
        atkRoll.isDelt();
    }

    //partie dégat

    public DiceList getDmgDiceListFromNface(int nFace) {
        return dmgRoll==null?null:dmgRoll.getDmgDiceList().filterWithNface(nFace);
    }

    public DiceList getDmgDiceList() {
        return dmgRoll==null?null:dmgRoll.getDmgDiceList();
    }

    public int getDmgBonus() {
        return dmgRoll==null?0:dmgRoll.getDmgBonus();
    }

    public int getDmgSum(String... elementArg) {
        String element = elementArg.length > 0 ? elementArg[0] : "";
        return dmgRoll==null?0:dmgRoll.getSumDmg(element);
    }

    public int getMaxDmg(String... elementArg) {
        String element = elementArg.length > 0 ? elementArg[0] : "";
        return dmgRoll==null?0:dmgRoll.getMaxDmg(element);
    }

    public int getMinDmg(String... elementArg) {
        String element = elementArg.length > 0 ? elementArg[0] : "";
        return dmgRoll==null?0:dmgRoll.getMinDmg(element);
    }

    public void setFromCharge() {
        atkRoll.setFromCharge();
    }

    public Integer getCritMultiplier(){
        return dmgRoll==null?0:dmgRoll.getCritMultiplier();
    }

    public void setNthAtkRoll(int nthAtkRoll) {
        this.nthAtkRoll = nthAtkRoll;
    }

    public int getNthAtkRoll() {
        return nthAtkRoll;
    }

    public boolean isMissed() {
        return atkRoll.isMissed();
    }

    public void setMissed() {
        this.atkRoll.setMiss();
    }
}
