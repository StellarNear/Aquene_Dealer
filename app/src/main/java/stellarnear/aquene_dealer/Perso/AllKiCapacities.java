package stellarnear.aquene_dealer.Perso;

import android.content.Context;

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

/**
 * Created by jchatron on 26/12/2017.
 */

public class AllKiCapacities {
    private Context mC;
    private List<KiCapacity> allKiCapacities = new ArrayList<>();
    private Map<String,KiCapacity> mapIdKicapacity=new HashMap<>();
    private Tools tools=new Tools();

    public AllKiCapacities(Context mC)
    {
        this.mC = mC;
        buildKiCapacitiesList();
    }

    private void buildKiCapacitiesList() {
        try {
            InputStream is = mC.getAssets().open("kicapacities.xml");
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            Document doc = dBuilder.parse(is);

            Element element = doc.getDocumentElement();
            element.normalize();

            NodeList nList = doc.getElementsByTagName("kicapacity");

            for (int i = 0; i < nList.getLength(); i++) {

                Node node = nList.item(i);
                if (node.getNodeType() == Node.ELEMENT_NODE) {
                    Element element2 = (Element) node;
                    KiCapacity kiCapacity=new KiCapacity(
                            readValue("name", element2),
                            tools.toInt(readValue("cost", element2)),
                            readValue("feat", element2),
                            readValue("descr", element2),
                            readValue("shortdescr", element2),
                            readValue("id", element2),
                            mC);
                    allKiCapacities.add(kiCapacity);
                    mapIdKicapacity.put(kiCapacity.getId(),kiCapacity);
                }
            }
            is.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public List<KiCapacity> getAllKiCapacitiesList(){
        return allKiCapacities;
    }

    public KiCapacity getKicapacity(String kicapacitytId) {
        KiCapacity selectedKiCapacity;
        try {
            selectedKiCapacity=mapIdKicapacity.get(kicapacitytId);
        } catch (Exception e){  selectedKiCapacity=null;  }
        return selectedKiCapacity;
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
}
