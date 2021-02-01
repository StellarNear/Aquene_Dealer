package stellarnear.aquene_companion.Divers;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import stellarnear.aquene_companion.Divers.Rolls.Dice20;
import stellarnear.aquene_companion.Divers.Rolls.Roll;
import stellarnear.aquene_companion.Divers.Rolls.RollList;
import stellarnear.aquene_companion.Perso.Attack;
import stellarnear.aquene_companion.Perso.Capacity;
import stellarnear.aquene_companion.Perso.KiCapacity;
import stellarnear.aquene_companion.Perso.Stance;


public class PostDataElement {
    private String targetSheet="Aquene";
    private String date="-";
    private String detail ="-";
    private String typeEvent="-";
    private String result="-";

    /* lancement d'une attaque */
    public PostDataElement(Attack atk) {
        SimpleDateFormat formater = new SimpleDateFormat("dd/MM/yy HH:mm:ss", Locale.FRANCE);
        this.date=formater.format(new Date());
        this.typeEvent="Lancement de "+atk.getName();
        this.detail=atk.getDescr();
    }


    /* jet  attaque et jet degat de l'attaque  */
    public PostDataElement(RollList rolls, String mode) {
        if(mode.equalsIgnoreCase("atk")) {
            initAtk(rolls);
        } else {
            initDmg(rolls);
        }
    }

    public PostDataElement(Capacity capa) {
        SimpleDateFormat formater = new SimpleDateFormat("dd/MM/yy HH:mm:ss", Locale.FRANCE);
        this.date=formater.format(new Date());
        this.typeEvent="Lancement capacité "+capa.getName();
        this.detail=capa.getDescr();
        if(capa.getValue()>0) {
            this.result = "Valeur : " + capa.getValue();
        }

    }

    private void initAtk(RollList atkRolls){
        SimpleDateFormat formater = new SimpleDateFormat("dd/MM/yy HH:mm:ss", Locale.FRANCE);
        this.date=formater.format(new Date());

        this.typeEvent="Jets d'attaques";
        String detailTxt="";
        for (Roll roll : atkRolls.getList()) {
            if (!detailTxt.equalsIgnoreCase("")) {
                detailTxt += " / ";
            }

            if(roll.isInvalid()){
                detailTxt +="-";
            } else if(roll.isCrit()) {
                detailTxt += "crit";
            } else  if(roll.isFailed()) {
                detailTxt += "fail";
            } else {
                detailTxt += "normal";
            }
            if(!roll.isInvalid()){
                detailTxt += "("+roll.getAtkDice().getRandValue();
                if(roll.getAtkDice().getMythicDice()!=null){detailTxt +=","+roll.getAtkDice().getMythicDice().getRandValue();}
                detailTxt +=")";
            }
        }

        this.detail=detailTxt;
        String resultTxt="";

        for (Roll roll : atkRolls.getList()) {
            if (!resultTxt.equalsIgnoreCase("")) {
                resultTxt += " / ";
            }
            int resultAtk=roll.getAtkValue();
            if(roll.getAtkDice().getMythicDice()!=null){
                resultAtk+=roll.getAtkDice().getMythicDice().getRandValue();
            }
            resultTxt += resultAtk;
        }
        this.result=resultTxt;
    }

    /* dégat d'une attaque */
    private void initDmg(RollList atkRolls){
        SimpleDateFormat formater = new SimpleDateFormat("dd/MM/yy HH:mm:ss", Locale.FRANCE);
        this.date=formater.format(new Date());

        this.typeEvent="Dégats de l'attaque";

        int sumPhy=0;
        int sumFire=0;
        for (Roll roll : atkRolls.getList()) {
            if(!roll.isInvalid()) {
                sumPhy += roll.getDmgSum();
                sumFire += roll.getDmgSum("fire");
            }
        }

        String detailTxt="";

        int nAtk=0;
        int nCrit=0;
        for (Roll roll : atkRolls.getList()) {
            if(roll.isHitConfirmed()){
                nAtk++;
                if (roll.isCritConfirmed()){
                    nCrit++;
                }
            }
        }
        detailTxt=nAtk+" hits, "+nCrit+" crits";

        this.detail=detailTxt;

        String resultTxt="";

        if(sumPhy>0){ resultTxt+=sumPhy+" Phy";}
        if(sumFire>0){ resultTxt+=" "+sumFire+" Feu";}

        this.result=resultTxt;
    }

    /* autre posts */
    public PostDataElement(String typeEvent,int result){
        SimpleDateFormat formater = new SimpleDateFormat("dd/MM/yy HH:mm:ss", Locale.FRANCE);
        this.date=formater.format(new Date());
        this.typeEvent=typeEvent;
        this.result=String.valueOf(result);
    }

    public PostDataElement(String typeEvent,String resultTxt){
        SimpleDateFormat formater = new SimpleDateFormat("dd/MM/yy HH:mm:ss", Locale.FRANCE);
        this.date=formater.format(new Date());
        this.typeEvent=typeEvent;
        this.result=resultTxt;
    }

    public PostDataElement(String typeEvent, Dice20 oriDice, int result){
        SimpleDateFormat formater = new SimpleDateFormat("dd/MM/yy HH:mm:ss", Locale.FRANCE);
        this.date=formater.format(new Date());

        this.typeEvent=typeEvent;
        this.result=String.valueOf(result);

        String detailTxt = String.valueOf(oriDice.getRandValue());
        if(oriDice.getMythicDice()!=null){detailTxt +=","+oriDice.getMythicDice().getRandValue();}
        this.detail =detailTxt;
    }

    public String getDetail() {
        return detail;
    }

    public String getDate() {
        return date;
    }

    public String getResult() {
        return result;
    }

    public String getTargetSheet() {
        return targetSheet;
    }

    public String getTypeEvent() {
        return typeEvent;
    }


    /* lancement d'un sort Ki*/
    public PostDataElement(KiCapacity kicapacity) {
        SimpleDateFormat formater = new SimpleDateFormat("dd/MM/yy HH:mm:ss", Locale.FRANCE);
        this.date=formater.format(new Date());
        this.detail ="Coût "+kicapacity.getCost()+" points de Ki";
        this.typeEvent="Capacité de Ki "+kicapacity.getName();
    }

    public PostDataElement(KiCapacity kicapacity,int cost, int armor) {  //pour ki armor
        SimpleDateFormat formater = new SimpleDateFormat("dd/MM/yy HH:mm:ss", Locale.FRANCE);
        this.date=formater.format(new Date());
        this.detail ="Dépense "+cost+" points de Ki";
        this.typeEvent="Capacité de Ki "+kicapacity.getName();
        this.result="Bonus de "+armor+ "CA, dure 1 min";
    }


    /* stance */
    public PostDataElement(Stance stance) {
        SimpleDateFormat formater = new SimpleDateFormat("dd/MM/yy HH:mm:ss", Locale.FRANCE);
        this.date=formater.format(new Date());
        if(stance!=null) {
            this.detail = stance.getId();
        } else { this.detail = "stance_null"; }
        this.typeEvent="stance_change"; //on gere cet event directement dans le script google
    }
}
