package stellarnear.aquene_dealer.Perso;

/**
 * Created by jchatron on 05/01/2018.
 */

public class AbilitiesCalculator {
    private int FOR;
    private int DEX;
    private int CON;
    private int INT;
    private int SAG;
    private int CHA;
    private int CA;
    private int BMO;
    private int DMD;
    private Abilities baseAbilities;
    private AllStances allStances;
    private AllFeats allFeats;
    public AbilitiesCalculator(Abilities baseAbilities, AllStances allStances, AllFeats allFeats) {
        this.baseAbilities = baseAbilities;
        this.allStances=allStances;
        this.allFeats=allFeats;
        setFOR();

    }


    public int getFOR() {
        return FOR;
    }

    public void setFOR() {
        int val= baseAbilities.getFOR();
        if (allStances.isActive("bear")) {
            val +=4;
        }
        this.FOR = val;
    }

    public int getDEX() {
        return DEX;
    }

    public void setDEX(int DEX) {
        this.DEX = DEX;
    }

    public int getCON() {
        return CON;
    }

    public void setCON(int CON) {
        this.CON = CON;
    }

    public int getINT() {
        return INT;
    }

    public void setINT(int INT) {
        this.INT = INT;
    }

    public int getSAG() {
        return SAG;
    }

    public void setSAG(int SAG) {
        this.SAG = SAG;
    }

    public int getCHA() {
        return CHA;
    }

    public void setCHA(int CHA) {
        this.CHA = CHA;
    }

    public int getCA() {
        return CA;
    }

    public void setCA(int CA) {
        this.CA = CA;
    }

    public int getBMO() {
        return BMO;
    }

    public void setBMO(int BMO) {
        this.BMO = BMO;
    }

    public int getDMD() {
        return DMD;
    }

    public void setDMD(int DMD) {
        this.DMD = DMD;
    }

}
