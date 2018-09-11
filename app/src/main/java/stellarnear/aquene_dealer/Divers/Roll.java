package stellarnear.aquene_dealer.Divers;

import android.content.Context;
import android.widget.CheckBox;
import android.widget.ImageView;

import java.util.ArrayList;
import java.util.List;

public class Roll {
    private AtkRoll atkRoll;
    private DmgRoll dmgRoll;
    private Context mC;
    public Roll(Context mC,Integer atkBase) {
        this.mC=mC;
        this.atkRoll=new AtkRoll(mC,atkBase);
    }

    public AtkRoll getAtkRoll(){
        return this.atkRoll;
    }

    public void setDmgRand() {
        if (this.dmgRoll==null){
            this.dmgRoll=new DmgRoll(mC,atkRoll.isCritConfirmed());
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


    public List<Dice> getDmgDiceList(int nFace) {
        List<Dice> list = new ArrayList<>();
        for (Dice dice : dmgRoll.getDmgDiceList()){
            if (dice.getnFace()==nFace){
                list.add(dice);
            }
        }
        return list;
    }

    public List<Dice> getDmgDiceList() {
        return dmgRoll.getDmgDiceList();
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

    public void setFromCharge() {
        atkRoll.setFromCharge();
    }
}
