package stellarnear.aquene_dealer.Perso;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import stellarnear.aquene_dealer.Divers.Tools;
import stellarnear.aquene_dealer.R;

/**
 * Created by jchatron on 26/12/2017.
 */

public class AllAttacks {
    private Map<String, Attack> mapIDAtk = new HashMap<>();
    private List<Attack> listAttacks= new ArrayList<>();
    private Context mC;
    private String combatMode;
    private int atkRange;
    private Tools tools=new Tools();

    public AllAttacks(Context mC) {
        this.mC = mC;
        combatMode="mode_normal";
        buildAttacksList();

        refreshAllAttacks();
    }

    public void refreshAllAttacks() {
        SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(mC);
        atkRange= tools.toInt(settings.getString("attack_range", String.valueOf(mC.getResources().getInteger(R.integer.attack_range_DEF))));
    }

    private void buildAttacksList() {
        try {
            InputStream is = mC.getAssets().open("attacks.xml");
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            Document doc = dBuilder.parse(is);

            Element element = doc.getDocumentElement();
            element.normalize();

            NodeList nList = doc.getElementsByTagName("attack");

            for (int i = 0; i < nList.getLength(); i++) {

                Node node = nList.item(i);
                if (node.getNodeType() == Node.ELEMENT_NODE) {
                    Element element2 = (Element) node;
                    Attack atk=new Attack(
                            readValue("name", element2),
                            readValue("shortname", element2),
                            readValue("type", element2),
                            readValue("descr", element2),
                            tools.toBool(readValue("save", element2)),
                            readValue("id", element2),
                            mC);
                    listAttacks.add(atk);
                    mapIDAtk.put(atk.getId(),atk);
                }
            }
            is.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public int getAtkRange() {
        return atkRange;
    }


    public List<Attack> getAttacksList(){
        return listAttacks;
    }

    public Attack getAttack(String atkId) {
        Attack selectedAtk;
        try {
            selectedAtk=mapIDAtk.get(atkId);
        } catch (Exception e){  selectedAtk=null;  }
        return selectedAtk;
    }

    public String readValue(String tag, Element element) {
        try {
            NodeList nodeList = element.getElementsByTagName(tag).item(0).getChildNodes();
            Node node = nodeList.item(0);
            return node.getNodeValue();
        } catch (Exception e){
            return "";
        }
    }

    public void setCombatMode(String combatMode) {
        this.combatMode = combatMode;
    }

    public String getCombatMode() {
        return combatMode;
    }

    public List<Attack> getAttacksForType(String actionType) {
        List<Attack> selList=new ArrayList<>();
        Map<String,Integer> matchActionValue= new HashMap<>();
        matchActionValue.put("simple",1);
        matchActionValue.put("complex",2);

        for(Attack atk : listAttacks){
            if((actionType.equalsIgnoreCase("simple")||actionType.equalsIgnoreCase("complex")) &&
            (matchActionValue.get(atk.getType())<=matchActionValue.get(actionType))){
                selList.add(atk);
            }
        }
        return selList;
    }
}