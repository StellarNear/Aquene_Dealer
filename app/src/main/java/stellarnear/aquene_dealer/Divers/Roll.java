package stellarnear.aquene_dealer.Divers;

import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.CheckBox;

import java.util.List;

public class Roll {
    private AtkRoll atkRoll;
    private DmgRoll dmgRoll;
    private Activity mA;
    private Context mC;
    public Roll(Activity mA, Context mC,int atkBase) {
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
    public ImageView getImgAtk() {
        return atkRoll.getImgAtk();
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

    public List<ImageView> getDmgDiceImgList(int dice) {
        return dmgRoll.getDmgDiceImgList(dice);
    }

    public List<Integer> getDmgDiceValue(int dice) {
        return dmgRoll.getDmgDiceValue(dice);
    }

    public int getDmgBonus() {
        return dmgRoll.getDmgBonus();
    }


    public int getDmgSumPhy() {
        return dmgRoll.getSumPhy();
    }

    public int getDmgSumFire() {
        return dmgRoll.getSumFire();
    }

    public int getNDmgDice(int dice) {
        return dmgRoll.getNDmgDice(dice);
    }
}
