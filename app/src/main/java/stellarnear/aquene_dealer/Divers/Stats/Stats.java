package stellarnear.aquene_dealer.Divers.Stats;

import android.content.Context;

import stellarnear.aquene_dealer.Divers.Rolls.RollList;
import stellarnear.aquene_dealer.Divers.TinyDB;
import stellarnear.aquene_dealer.Perso.Attack;

public class Stats {
    private StatsList statsList = new StatsList();
    private TinyDB tinyDB;

    public Stats(Context mC){
        tinyDB = new TinyDB(mC);
        refreshStats();
    }

    private void saveLocalStats() { //sauvegarde dans local DB
        tinyDB.putStats("localSaveStats", statsList);
    }

    public void refreshStats(){ //Initialisation ou lecture depuis DB
        StatsList listDB = tinyDB.getStats("localSaveStats");
        if (listDB.size() == 0) {
            initStats();
            saveLocalStats();
        } else {
            statsList = listDB;
        }
    }

    private void initStats(){
        this.statsList =new StatsList();
    }

    public void storeStatsFromRolls(Attack attack,RollList selectedRolls) {
        Stat stat = new Stat();
        stat.feedStat(attack,selectedRolls);
        statsList.add(stat);
        saveLocalStats();
    }

    public StatsList getStatsList() {
        return statsList;
    }

    public void resetStats() {
        this.statsList =new StatsList();
        saveLocalStats();
    }

    public void reset() {
        this.statsList =new StatsList();
        saveLocalStats();
    }
}
