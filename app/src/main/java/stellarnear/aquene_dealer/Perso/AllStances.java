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

/**
 * Created by jchatron on 27/12/2017.
 */

public class AllStances  {
    private List<Stance> allStances = new ArrayList<Stance>();
    private Map<String,Stance> mapIdStance=new HashMap<>();
    private Context mC;
    private Stance currentStance;
    public AllStances(Context mC) {
        this.mC=mC;
        buildStancesList();
    }

    private void buildStancesList() {
        try {
            InputStream is = mC.getAssets().open("stances.xml");
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            Document doc = dBuilder.parse(is);

            Element element = doc.getDocumentElement();
            element.normalize();

            NodeList nList = doc.getElementsByTagName("stance");

            for (int i = 0; i < nList.getLength(); i++) {

                Node node = nList.item(i);
                if (node.getNodeType() == Node.ELEMENT_NODE) {
                    Element element2 = (Element) node;
                    Stance stance=new Stance(
                            readValue("name", element2),
                            readShortName("shortname", element2),
                            readValue("type", element2),
                            readValue("descr", element2),
                            readValue("id", element2),
                            readValue("featid", element2),
                            mC);
                    allStances.add(stance);
                    mapIdStance.put(stance.getId(),stance);
                }
            }
            is.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private String readShortName(String shortname, Element element2) {
        String val = readValue("shortname", element2);
        if(val.equals(""))
        {
            return readValue("name", element2);
        }
        else {
            return val;
        }
    }

    private String readValue(String tag, Element element) {
        try {
            NodeList nodeList = element.getElementsByTagName(tag).item(0).getChildNodes();
            Node node = nodeList.item(0);
            return node.getNodeValue();
        } catch (Exception e){
            return "";
        }
    }

    public List<Stance> getStancesList(){
        return allStances;
    }

    public Stance getStance(String stanceId) {
        Stance selectedStance;
        try {
            selectedStance=mapIdStance.get(stanceId);
        } catch (Exception e){selectedStance=null;}
        return selectedStance;
    }

    public void activateStance(Stance selectedStance) {
        for (Stance stance : allStances){
            stance.desactivate();
        }
        selectedStance.activate();
        currentStance=selectedStance;
    }

    public void desactivateAllStances() {
        for (Stance stance : allStances){
            stance.desactivate();
        }
        currentStance=null;
    }

    public Stance getCurrentStance(){
        return currentStance;
    }

    public boolean isActive(String id){
        boolean active=false;
        Stance wantedStance = getStance(id);
        if (wantedStance!=null && wantedStance.isActive()){
            active=true;
        }
        return  active;
    }
}
