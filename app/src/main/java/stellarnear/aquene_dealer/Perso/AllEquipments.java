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
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import stellarnear.aquene_dealer.Divers.TinyDB;
import stellarnear.aquene_dealer.Divers.Tools;

/**
 * Created by jchatron on 05/01/2018.
 */

public class AllEquipments {
    //private Map<String, Equipment> mapSlotIdEquipment = new HashMap<>();
    private List<Equipment> listEquipments = new ArrayList<>();
    private Context mC;
    private Tools tools = new Tools();
    private TinyDB tinyDB;

    public AllEquipments(Context mC) {
        this.mC = mC;
        tinyDB = new TinyDB(mC);
        List<Equipment> listDB = tinyDB.getListEquipments("localSaveListEquipments");
        if (listDB.size() == 0) {
            buildList();
            saveLocalAllEquipments();
        } else {
            listEquipments = listDB;
        }
    }


    private void saveLocalAllEquipments() {
        tinyDB.putListEquipments("localSaveListEquipments", listEquipments);
    }

    private void buildList() {
        try {
            InputStream is = mC.getAssets().open("equipment.xml");
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            Document doc = dBuilder.parse(is);
            Element element = doc.getDocumentElement();
            element.normalize();
            NodeList nList = doc.getElementsByTagName("equipment");
            for (int i = 0; i < nList.getLength(); i++) {
                Node node = nList.item(i);
                if (node.getNodeType() == Node.ELEMENT_NODE) {
                    Element element2 = (Element) node;
                    Equipment equi = new Equipment(
                            readValue("name", element2),
                            readValue("descr", element2),
                            readValue("value", element2),
                            readValue("imgId", element2),
                            readValue("slotId", element2),
                            tools.toBool(readValue("equipped", element2)));
                    listEquipments.add(equi);
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

    public List<Equipment> getSlotListEquipment(String slot) {
        List<Equipment> list = new ArrayList<>();
        for (Equipment equipment : listEquipments) {
            if (equipment.getSlotId().equalsIgnoreCase(slot)) {
                list.add(equipment);
            }
        }
        return list;
    }

    public Equipment getSlotEquipment(String slot) {
        Equipment equi = null;
        for (Equipment equipment : listEquipments) {
            if (equipment.getSlotId().equalsIgnoreCase(slot)) {
                equi = equipment;
            }
        }
        return equi;
    }

    public int getAllEquipmentsSize() {
        return listEquipments.size();
    }

    public List<Equipment> getListAllEquipments() {
        return listEquipments;
    }

    public List<Equipment> getListAllEquipmentsEquiped() {
        List<Equipment> list = new ArrayList<>();
        for (Equipment equipment : listEquipments) {
            if (equipment.isEquiped()) {
                list.add(equipment);
            }
        }
        return list;
    }
}

