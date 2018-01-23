package stellarnear.aquene_dealer.Perso;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.Gravity;
import android.widget.Toast;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

/**
 * Created by jchatron on 05/01/2018.
 */

public class AllAbilities {

    private Map<String, Ability> mapIDAbi = new HashMap<>();
    private List<Ability> listAbilities= new ArrayList<>();
    private AllStances allStances;
    private AllFeats allFeats;
    private Context mC;

    public AllAbilities(AllStances allStances, AllFeats allFeats, Context mC) {
        this.allStances = allStances;
        this.allFeats = allFeats;
        this.mC = mC;
        buildAbilitiesList();
        refreshAllAbilities();
    }

    private void buildAbilitiesList() {
        try {
            InputStream is = mC.getAssets().open("abilities.xml");
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            Document doc = dBuilder.parse(is);

            Element element = doc.getDocumentElement();
            element.normalize();

            NodeList nList = doc.getElementsByTagName("ability");

            for (int i = 0; i < nList.getLength(); i++) {

                Node node = nList.item(i);
                if (node.getNodeType() == Node.ELEMENT_NODE) {
                    Element element2 = (Element) node;
                    Ability abi = new Ability(
                            readValue("name", element2),
                            readValue("shortname", element2),
                            readValue("type", element2),
                            readValue("descr", element2),
                            toBool(readValue("testable", element2)),
                            toBool(readValue("focusable", element2)),
                            readValue("id", element2),
                            mC);
                    listAbilities.add(abi);
                    mapIDAbi.put(abi.getId(), abi);
                }
            }
            is.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private String readValue(String tag, Element element) {
        try {
            NodeList nodeList = element.getElementsByTagName(tag).item(0).getChildNodes();
            Node node = nodeList.item(0);
            return node.getNodeValue();
        } catch (Exception e) {
            return "";
        }
    }

    private int readAbility(String key) {
        SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(mC);
        int resId = mC.getResources().getIdentifier("carac_base" + key.toUpperCase() + "_DEF", "integer", mC.getPackageName());
        return toInt(settings.getString("carac_base" + key.toUpperCase(), String.valueOf(mC.getResources().getInteger(resId))));
    }

    public void refreshAllAbilities() {
        for (Ability abi : listAbilities) {
            //(abiKey.equals("FOR") && allStances.getCurrentStance()!=null && allStances.getCurrentStance().getId().equals("bear") pour test les stance en meme temps
            int val = 0;
            if (abi.getId().equalsIgnoreCase("CA")) {
                val = readAbility("CA_STUFF") + readAbility("CA_MONK") + getMod("DEXTERITE") + 10;
            } else if (abi.getId().equalsIgnoreCase("BMO")) {
                val = readAbility("LVL") + getMod("FORCE");
            } else if (abi.getId().equalsIgnoreCase("DMD")) {
                val = readAbility("LVL") + getMod("FORCE") + 10 + getMod("DEXTERITE");
            } else if (abi.getId().equalsIgnoreCase("INIT")) {
                val = getMod("DEXTERITE");
                if (allFeats.isActive("init")) {
                    val += 4;
                }
            } else if (abi.getId().equalsIgnoreCase("RM")) {
                val = readAbility("LVL") + 10;
            } else {
                val = readAbility(abi.getId());
            }
            abi.setValue(val);
        }
    }

    public List<Ability> getAbilitiesList(String... type){
        String typeSelected = type.length > 0 ? type[0] : "";  //parametre optionnel type
        List<Ability> list= new ArrayList<>();
        if (typeSelected.equalsIgnoreCase("base")
                ||typeSelected.equalsIgnoreCase("general")
                ||typeSelected.equalsIgnoreCase("def")
                ||typeSelected.equalsIgnoreCase("advanced")){
            for(Ability abi : listAbilities){
                if(abi.getType().equalsIgnoreCase(typeSelected)){
                    list.add(abi);
                }
            }
        } else {
            list=listAbilities;
        }
        return list;
    }

    private Ability getAbi(String abiId){
        Ability abiSelected=null;
        for (Ability abi : listAbilities){
            if (abi.getId().equalsIgnoreCase(abiId)){
               abiSelected=abi;
            }
        }
        return abiSelected;
    }

    public int getScore(String key) {
        int val=0;
        if (getAbi(key)!=null) {
            val = getAbi(key).getValue();
        }

        if (key.equalsIgnoreCase("FORCE") && allStances.getCurrentStance() != null && allStances.getCurrentStance().getId().equals("bear")) {
            val+=5;
        }
        return val;
    }

    public int getMod(String key) {
        int score = 0;
        if (getAbi(key) != null) {
            score = getScore(key);
        }

        int mod;
        if (getAbi(key).getType().equalsIgnoreCase("base")) {
            float modFloat = (float) ((score - 10) / 2.0);
            if (modFloat >= 0) {
                mod = (int) modFloat;
            } else {
                mod = -1 * Math.round(Math.abs(modFloat));
            }
        } else {
            mod = 0;
        }

        return mod;
    }

    private Boolean toBool(String key) {
        Boolean value = false;
        try {
            value = Boolean.valueOf(key);
        } catch (Exception e) {
        }
        return value;
    }

    private Integer toInt(String key) {
        Integer value = 0;
        try {
            value = Integer.parseInt(key);
        } catch (Exception e) {
        }
        return value;
    }



}
