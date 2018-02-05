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

/**
 * Created by jchatron on 02/02/2018.
 */

public class AllResources {
    private Context mC;
    private AllAbilities allAbilities;
    private AllFeats allFeats;
    private Map<String, Resource> mapIDRes = new HashMap<>();
    private List<Resource> listResources= new ArrayList<>();

    public AllResources(Context mC, AllFeats allFeats, AllAbilities allAbilities)
    {
        this.mC = mC;
        this.allAbilities=allAbilities;
        this.allFeats=allFeats;

        buildResourcesList();
        refreshMaxs();
        sleepReset();
    }

    private void buildResourcesList() {
        try {
            InputStream is = mC.getAssets().open("resources.xml");
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            Document doc = dBuilder.parse(is);

            Element element = doc.getDocumentElement();
            element.normalize();

            NodeList nList = doc.getElementsByTagName("resource");

            for (int i = 0; i < nList.getLength(); i++) {

                Node node = nList.item(i);
                if (node.getNodeType() == Node.ELEMENT_NODE) {
                    Element element2 = (Element) node;
                    Resource atk=new Resource(
                            readValue("name", element2),
                            readValue("descr", element2),
                            toBool(readValue("testable", element2)),
                            readValue("id", element2),
                            mC);
                    listResources.add(atk);
                    mapIDRes.put(atk.getId(),atk);
                }
            }
            is.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public List<Resource> getResourcesList(){
        return listResources;
    }

    public Resource getResource(String resourceId) {
        Resource selectedResource;
        try {
            selectedResource=mapIDRes.get(resourceId);
        } catch (Exception e){  selectedResource=null;  }
        return selectedResource;
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

    private int readResource(String key) {
        SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(mC);
        int resId = mC.getResources().getIdentifier( key.toLowerCase() + "_def", "integer", mC.getPackageName());
        return toInt(settings.getString( key.toLowerCase(), String.valueOf(mC.getResources().getInteger(resId))));
    }

    public void refreshMaxs() {
        //partie from setting
        getResource("resource_hp").setMax(readResource("resource_hp"));
        getResource("resource_regen").setMax(readResource("resource_regen"));
        getResource("resource_heroic").setMax(readResource("resource_heroic"));

        //partie calcul
        int lvl=allAbilities.getAbi("ability_lvl").getValue();
        int kiPool=lvl;
        if(allFeats.getFeat("feat_bonus_ki").isActive()){kiPool+=2;}
        getResource("resource_ki").setMax(kiPool);
        getResource("resource_stun").setMax(lvl);
        getResource("resource_palm").setMax(1);
    }

    public void sleepReset(){
        for (Resource res: listResources){
            res.resetCurrent();
        }
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
