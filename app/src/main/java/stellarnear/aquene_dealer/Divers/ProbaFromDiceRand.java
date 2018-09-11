//package stellarnear.aquene_dealer.Divers;
//
//import android.util.Log;
//import android.view.View;
//import android.widget.LinearLayout;
//import android.widget.TextView;
//
//import java.math.BigDecimal;
//import java.math.BigInteger;
//import java.math.RoundingMode;
//import java.util.List;
//
//import stellarnear.aquene_dealer.R;
//
///**
// * Created by Utilisateur on 13/02/2018.
// */
//
//public class ProbaFromDiceRand {
//    private int minPhy=0;
//    private int minFire=0;
//    private int maxPhy=0;
//    private int maxFire=0;
//    private int sumFire=0;
//    private int sumPhy=0;
//    private int critFactor=2;
//    private int probaSumPhy=0;
//    private int probaSumPhyCrit=0;
//    private int probaSumFire=0;
//    private int probaNd10=0;
//    private int probaNd10Crit=0;
//    private int probaNd8=0;
//    private int probaNd6=0;
//    private List<Roll> selectedRolls;
//    public ProbaFromDiceRand(List<Roll> selectedRolls) {
//        this.selectedRolls=selectedRolls;
//        buildParameters();
//    }
//
//    private void buildParameters() {
//
//        for (Roll roll : selectedRolls)
//        {
//            if(roll.isCritConfirmed()){
//                probaNd10Crit += roll.getNDmgDice(10);
//                for (int i : roll.getDmgDiceValue(10)){probaSumPhyCrit+=i;sumPhy+=i*critFactor;}
//                sumPhy+=roll.getDmgBonus()*critFactor;
//                minPhy+=(roll.getNDmgDice(10)+roll.getDmgBonus())*critFactor;
//                maxPhy+=(10*roll.getNDmgDice(10)+roll.getDmgBonus())*critFactor;
//            } else {
//                probaNd10 += roll.getNDmgDice(10);
//                for (int i : roll.getDmgDiceValue(10)){probaSumPhy+=i;sumPhy+=i;}
//                sumPhy+=roll.getDmgBonus();
//                minPhy+=roll.getNDmgDice(10)+roll.getDmgBonus();
//                maxPhy+=10*roll.getNDmgDice(10)+roll.getDmgBonus();
//            }
//            probaNd8+=roll.getNDmgDice(8);
//            minPhy+=roll.getNDmgDice(8);
//            maxPhy+=8*roll.getNDmgDice(8);
//            for (int i : roll.getDmgDiceValue(8)){sumPhy+=i;}
//            for (int i : roll.getDmgDiceValue(8)){probaSumPhy+=i;}
//            probaNd6+=roll.getNDmgDice(6);
//            minFire+=roll.getNDmgDice(6);
//            maxFire+=6*roll.getNDmgDice(6);
//            for (int i : roll.getDmgDiceValue(6)){sumFire+=i;}
//            for (int i : roll.getDmgDiceValue(6)){probaSumFire+=i;}
//        }
//    }
//
//    private Double getProba(int nd6,int nd8,int nd10,int sum) {
//        Integer total = nd6 * 6 + nd8 * 8 + nd10 * 10;
//        Log.d("STATE (table)total", String.valueOf(total));
//        BigInteger[] combi_old = new BigInteger[total];          // table du nombre de combinaison pour chaque valeur somme
//        BigInteger[] combi_new = new BigInteger[total];
//        for (int i = 1; i <= total; i++) {
//            combi_old[i - 1] = BigInteger.ZERO;
//            combi_new[i - 1] = BigInteger.ZERO;
//        }
//        if (nd10 != 0) {
//            for (int i = 1; i <= 10; i++) {                     //on rempli la premiere itération
//                combi_old[i - 1] = BigInteger.ONE;
//            }
//            nd10 -= 1;
//        } else if (nd8 != 0) {
//            for (int i = 1; i <= 8; i++) {                     //on rempli la premiere itération
//                combi_old[i - 1] = BigInteger.ONE;
//            }
//            nd8 -= 1;
//        } else if (nd6 != 0) {                      //on rempli la premiere itération
//            for (int i = 1; i <= 6; i++) {
//                combi_old[i - 1] = BigInteger.ONE;
//            }
//            nd6 -= 1;
//        } else {
//            return 1.0;
//        }
//
//        for (int i=1;i<=nd6;i++) {              //pour chaque nouveau dès on ajoute la somme(cf https://wizardofodds.com/gambling/dice/2/)
//            //Log.d("STATE table_prob","traitement d10:"+String.valueOf(i));
//            for (int j=1;j<=total;j++) {
//                //Log.d("STATE table_prob","traitement ligne:"+String.valueOf(j));
//                for (int k = 6; k >= 1; k--) {
//                    //Log.d("STATE table_prob","traitement somme k:"+String.valueOf(k));
//                    if (j-1-k>=0) {
//                        //Log.d("STATE table_prob","combi_new[j-1]:"+String.valueOf(combi_new[j-1]));
//                        //Log.d("STATE table_prob","combi_old[j-1-k]:"+String.valueOf(combi_old[j-1-k]));
//                        combi_new[j-1]=combi_new[j-1].add(combi_old[j-1-k]);
//                    }
//                }
//            }
//            for (int l=1;l<=total;l++){
//                combi_old[l-1]=combi_new[l-1];
//                combi_new[l-1]=BigInteger.ZERO;
//            }
//        }
//
//        for (int i=1;i<=nd8;i++) {              //pour chaque nouveau dès on ajoute la somme(cf https://wizardofodds.com/gambling/dice/2/)
//            //Log.d("STATE table_prob","traitement d10:"+String.valueOf(i));
//            for (int j=1;j<=total;j++) {
//                //Log.d("STATE table_prob","traitement ligne:"+String.valueOf(j));
//                for (int k = 8; k >= 1; k--) {
//                    //Log.d("STATE table_prob","traitement somme k:"+String.valueOf(k));
//                    if (j-1-k>=0) {
//                        //Log.d("STATE table_prob","combi_new[j-1]:"+String.valueOf(combi_new[j-1]));
//                        //Log.d("STATE table_prob","combi_old[j-1-k]:"+String.valueOf(combi_old[j-1-k]));
//                        combi_new[j-1]=combi_new[j-1].add(combi_old[j-1-k]);
//                    }
//                }
//            }
//            for (int l=1;l<=total;l++){
//                combi_old[l-1]=combi_new[l-1];
//                combi_new[l-1]=BigInteger.ZERO;
//            }
//        }
//
//        for (int i=1;i<=nd10;i++) {       //pour chaque nouveau dès on ajoute la somme(cf https://wizardofodds.com/gambling/dice/2/)
//            //Log.d("STATE table_prob","traitement d6:"+String.valueOf(i));
//            for (int j=1;j<=total;j++) {
//                //Log.d("STATE table_prob","traitement ligne:"+String.valueOf(j));
//                for (int k = 10; k >= 1; k--) {
//                    //Log.d("STATE table_prob","traitement somme k:"+String.valueOf(k));
//                    if (j-1-k>=0) {
//                        //Log.d("STATE table_prob","combi_new[j-1]:"+String.valueOf(combi_new[j-1]));
//                        //Log.d("STATE table_prob","combi_old[j-1-k]:"+String.valueOf(combi_old[j-1-k]));
//                        combi_new[j-1]=combi_new[j-1].add(combi_old[j-1-k]);
//                    }
//                }
//            }
//            for (int l=1;l<=total;l++){
//                combi_old[l-1]=combi_new[l-1];
//                combi_new[l-1]=BigInteger.ZERO;
//            }
//        }
//
//        /* Sortie debug de toute la table
//        Log.d("STATE combi_prob_all","toutes les valeurs sum:n_comb");
//        for (int i=1;i<=total;i++){
//            Log.d("STATE combi_prob_all",String.valueOf(i)+":"+String.valueOf(combi_old[i-1]));
//        }
//        Log.d("STATE combi_sum",String.valueOf(sum));
//        Log.d("STATE combi_res",String.valueOf(combi_old[sum-1]));
//        */
//
//        BigInteger sum_combi,sum_combi_tot;
//        sum_combi=BigInteger.ZERO;
//        sum_combi_tot=BigInteger.ZERO;
//        for (int i=1;i<=total;i++){
//            sum_combi_tot=sum_combi_tot.add(combi_old[i-1]);
//            if (i==sum) {
//                sum_combi=sum_combi_tot;
//            }
//        }
//
//        BigDecimal temp_sum = new BigDecimal(sum_combi);
//        BigDecimal temp_sum_tot = new BigDecimal(sum_combi_tot);
//        BigDecimal result_percent;
//        result_percent= temp_sum.divide(temp_sum_tot,4, RoundingMode.HALF_UP);
//        //Log.d("STATE combi_res_prob",String.valueOf(result_percent));
//        return result_percent.doubleValue();
//    }
//
//    public  String getRange(String mode){
//        int max=0,min=0,sum=0;
//        if (mode.equalsIgnoreCase("phy")) { max=maxPhy; min=minPhy ; sum=sumPhy; }
//        if (mode.equalsIgnoreCase("fire"))  { max=maxFire; min=minFire ; sum=sumFire; }
//        Integer ecart =  max - min;
//        Double percentage=0d;
//        if(ecart!=0) {
//            percentage = 100d*(sum - min) / ecart;
//        }
//        String rangeTxt="["+min+" - "+max+"]\n("+String.valueOf(percentage.intValue()) +"%)";
//        return rangeTxt;
//    }
//
//    public  String getProba(String mode){
//        Double percentage=0d;
//        if(mode.equalsIgnoreCase("phy")){percentage = 100d-(100*getProba(0,probaNd8,probaNd10,probaSumPhy));}
//        if(mode.equalsIgnoreCase("phyCrit")){percentage = 100d-(100*getProba(0,0,probaNd10Crit,probaSumPhyCrit));}
//        if(mode.equalsIgnoreCase("fire")){percentage = 100d-(100*getProba(probaNd6,0,0,probaSumFire));}
//        String proba="("+String.format("%.02f", percentage) +"%)";
//        return proba;
//    }
//
//    public void giveLinearToFill(LinearLayout linearStats) {
//        TextView rangePhy =linearStats.findViewById(R.id.combat_dialog_phy_range_result);
//        rangePhy.setText(getRange("phy"));
//        TextView probaPhy =linearStats.findViewById(R.id.combat_dialog_phy_proba_result);
//        probaPhy.setText(getProba("phy"));
//        if(sumFire<=0) {
//            linearStats.findViewById(R.id.combat_dialog_fire_proba).setVisibility(View.GONE);
//            linearStats.findViewById(R.id.combat_dialog_fire_range).setVisibility(View.GONE);
//        } else {
//            linearStats.findViewById(R.id.combat_dialog_fire_proba).setVisibility(View.VISIBLE);
//            linearStats.findViewById(R.id.combat_dialog_fire_range).setVisibility(View.VISIBLE);
//            TextView rangeFire = linearStats.findViewById(R.id.combat_dialog_fire_range_result);
//            rangeFire.setText(getRange("fire"));
//            TextView probaFire =linearStats.findViewById(R.id.combat_dialog_fire_proba_result);
//            probaFire.setText(getProba("fire"));
//        }
//        if(probaSumPhyCrit<=0){
//            linearStats.findViewById(R.id.combat_dialog_phy_crit).setVisibility(View.GONE);
//        }else{
//            linearStats.findViewById(R.id.combat_dialog_phy_crit).setVisibility(View.VISIBLE);
//            TextView probaPhyCrit =linearStats.findViewById(R.id.combat_dialog_phy_crit_proba_result);
//            probaPhyCrit.setText(getProba("phyCrit"));
//        }
//    }
//}
//
