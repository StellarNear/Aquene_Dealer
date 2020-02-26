package stellarnear.aquene_dealer.Divers.Rolls;

import android.app.Activity;
import android.content.Context;
import android.widget.ImageView;

import java.util.Random;

import stellarnear.aquene_dealer.Divers.Tools;

public class Dice {
    private int nFace;
    private int randValue;
    private String element;
    private ImgForDice imgForDice;
    private Context mC;
    private Activity mA;
    private boolean rolled=false;
    private boolean delt=false;
    private boolean canCrit=false;
    private boolean legendarySurge=false;
    private Tools tools= Tools.getTools();

    private Dice mythicDice; //si c'est un d20 il a un dés mythic attaché

    private OnMythicEventListener mListenerMythic;
    private OnRefreshEventListener mListenerRefresh;

    public Dice(Activity mA, Context mC, Integer nFace, String... elementArg) {
        this.nFace=nFace;
        this.element = elementArg.length > 0 ? elementArg[0] : "";
        this.mC=mC;
        this.mA=mA;
    }

    public void rand(Boolean manual){
        if (manual){
            new DiceDealerDialog(mA, Dice.this);
        } else {
            Random rand = new Random();
            this.randValue = 1 + rand.nextInt(nFace);
            this.rolled=true;
        }
    }

    public void setRand(int randFromWheel) { // le retour depuis wheelpicker
        this.randValue = randFromWheel;
        this.rolled=true;
        refreshDiceImg();
        if(mListenerRefresh!=null){mListenerRefresh.onEvent();}
    }

    private void refreshDiceImg() {
        if(imgForDice!=null) {
            imgForDice.refreshImg();
        }
    }

    public interface OnRefreshEventListener {
        void onEvent();
    }

    public void setRefreshEventListener(OnRefreshEventListener eventListener) {
        mListenerRefresh = eventListener;
    }

    public boolean isRolled() {
        return rolled;
    }

    public int getnFace() {
        return nFace;
    }

    public ImageView getImg() {
        if(imgForDice==null){imgForDice = new ImgForDice(this,mA,mC);}
        return imgForDice.getImg();
    }

    public int getRandValue() {
        return this.randValue;
    }

    public String getElement() {
        return this.element;
    }

    public void makeCritable(){
        this.canCrit=true;
    }

    public boolean canCrit(){
        return this.canCrit;
    }

    public void delt(){
        this.delt=true;
        imgForDice.getImg().setOnClickListener(null);
    }

    public boolean isDelt(){
        return this.delt;
    }

    public interface OnMythicEventListener {
        void onEvent();
    }

    public void setMythicEventListener(OnMythicEventListener eventListener) {
        mListenerMythic = eventListener;
    }

    public void setMythicDice(Dice mythicDice){
        this.mythicDice=mythicDice;
        if(mListenerMythic !=null){
            mListenerMythic.onEvent();
        }
    }

    public void setLegendarySurge(boolean legendarySurge) {
        this.legendarySurge = legendarySurge;
    }

    public boolean hasLegendarySurge() {
        return legendarySurge;
    }

    public Dice getMythicDice(){
        return this.mythicDice;
    }
}