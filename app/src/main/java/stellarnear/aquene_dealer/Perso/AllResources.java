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
 * Created by jchatron on 02/02/2018.
 */

public class AllResources {
    private Context mC;
    private AllAbilities allAbilities;
    private AllFeats allFeats;
    private Map<String, Resource> mapIDRes = new HashMap<>();
    private List<Resource> listResources = new ArrayList<>();
    private SharedPreferences settings;
    private Tools tools = new Tools();

    public AllResources(Context mC, AllFeats allFeats, AllAbilities allAbilities) {
        this.mC = mC;
        this.allAbilities = allAbilities;
        this.allFeats = allFeats;
        settings = PreferenceManager.getDefaultSharedPreferences(mC);
        buildResourcesList();
        refreshMaxs();
        loadCurrent();
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
                    Resource res = new Resource(
                            readValue("name", element2),
                            readValue("shortname", element2),
                            tools.toBool(readValue("testable", element2)),
                            tools.toBool(readValue("hide", element2)),
                            readValue("id", element2),
                            mC);
                    listResources.add(res);
                    mapIDRes.put(res.getId(), res);
                }
            }
            is.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public List<Resource> getResourcesListDisplay() {
        List<Resource> list = new ArrayList<>();

        for (Resource res : listResources) {
            if (!res.isHidden()) {
                list.add(res);
            }
        }
        return list;
    }

    public List<Resource> getResourcesList() {
        return listResources;
    }

    public Resource getResource(String resourceId) {
        Resource selectedResource;
        try {
            selectedResource = mapIDRes.get(resourceId);
        } catch (Exception e) {
            selectedResource = null;
        }
        return selectedResource;
    }

    public String readValue(String tag, Element element) {
        try {
            NodeList nodeList = element.getElementsByTagName(tag).item(0).getChildNodes();
            Node node = nodeList.item(0);
            return node.getNodeValue();
        } catch (Exception e) {
            return "";
        }
    }

    private int readResource(String key) {
        int resId = mC.getResources().getIdentifier(key.toLowerCase() + "_def", "integer", mC.getPackageName());
        return tools.toInt(settings.getString(key.toLowerCase(), String.valueOf(mC.getResources().getInteger(resId))));
    }

    public void refreshMaxs() {
        //partie from setting
        int hpPool = readResource("resource_hp");
        if (allFeats.getFeat("feat_robustness").isActive()) {
            hpPool += allAbilities.getAbi("ability_lvl").getValue();
        }
        hpPool += 5*readResource("mythic_tier");

        getResource("resource_hp").setMax(hpPool);
        getResource("resource_regen").setMax(readResource("resource_regen"));
        getResource("resource_heroic").setMax(readResource("resource_heroic"));

        //partie calcul
        int lvl = allAbilities.getAbi("ability_lvl").getValue();
        int kiPool = (int) (lvl / 2.0) + allAbilities.getAbi("ability_sagesse").getMod();
        if (allFeats.getFeat("feat_bonus_ki").isActive()) {
            kiPool += 2;
        }
        getResource("resource_ki").setMax(kiPool);
        getResource("resource_stun").setMax(lvl);
        getResource("resource_palm").setMax(1);

        if (settings.getBoolean("switch_save_ref_boot", mC.getResources().getBoolean(R.bool.switch_save_ref_boot_DEF))) {
            getResource("resource_boot_add_atk").setMax(1);
        }
        if (settings.getBoolean("feat_inhuman_stamina_sup", mC.getResources().getBoolean(R.bool.feat_inhuman_stamina_sup_DEF))) {
            getResource("resource_inhuman_stamina_sup").setMax(1);
        }
        if (settings.getBoolean("feat_iron_will_sup", mC.getResources().getBoolean(R.bool.feat_iron_will_sup_DEF))) {
            getResource("resource_iron_will_sup").setMax(1);
        }

        getResource("mythic_points").setMax(3+2*readResource("mythic_tier"));
        getResource("legendary_points").setMax(readResource("legendary_points"));

        if (settings.getBoolean("feat_blinding_speed", mC.getResources().getBoolean(R.bool.feat_blinding_speed_DEF))) {
            getResource("blinding_speed").setMax(5);
        }
    }

    private void loadCurrent() {
        for (Resource res : listResources) {
            res.loadCurrentFromSettings();
        }
    }

    public void sleepReset() {
        for (Resource res : listResources) {
            res.resetCurrent();
        }
    }
}
