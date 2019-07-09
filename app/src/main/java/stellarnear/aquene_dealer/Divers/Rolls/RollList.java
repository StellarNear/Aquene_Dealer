package stellarnear.aquene_dealer.Divers.Rolls;


import java.util.ArrayList;
import java.util.List;

public class RollList {
    private List<Roll> rollList;

    public RollList() {
        this.rollList= new ArrayList<>();
    }

    public void add(Roll roll){
        this.rollList.add(roll);
    }

    public Integer getDmgSumFromType(String... elementArg) {
        String element = elementArg.length > 0 ? elementArg[0] : "";
        int sum=0;
        for (Roll roll:this.rollList){
            sum += roll.getDmgSum(element);
        }
        return sum;
    }

    public Integer getMaxDmgFromType(String... elementArg) {
        String element = elementArg.length > 0 ? elementArg[0] : "";
        int max=0;
        for (Roll roll:this.rollList){
            max+=roll.getMaxDmg(element);
        }
        return max;
    }

    public Integer getMinDmgFromType(String... elementArg) {
        String element = elementArg.length > 0 ? elementArg[0] : "";
        int min=0;
        for (Roll roll:this.rollList){
            min+=roll.getMinDmg(element);
        }
        return min;
    }

    public List<Roll> getList() {
        return this.rollList;
    }

    public boolean isEmpty() {
        return this.rollList.isEmpty();
    }

    public Roll get(int i) {
        return this.rollList.get(i);
    }

    public boolean hasCrit(){
        Boolean bool=false;
        for (Roll roll : rollList){
            if (roll.isCritConfirmed()){
                bool=true;
                break;
            }
        }
        return bool;
    }

    /* Concernant uniquement les dès */

    public DiceList getDmgDiceList(){
        DiceList diceList = new DiceList();
        for (Roll roll : this.rollList){
            diceList.add(roll.getDmgDiceList());
        }
        return diceList;
    }

    public DiceList getCritDmgDiceList(){ //le controle du crit doit se faire au niveau du roll pas pendant le dice
        DiceList diceList = new DiceList();
        for (Roll roll : this.rollList){
            if(roll.isCritConfirmed()){diceList.add(roll.getDmgDiceList());}
        }
        return diceList;
    }
}
