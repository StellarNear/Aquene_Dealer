package stellarnear.aquene_dealer.Perso;

import android.content.Context;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

/**
 * Created by jchatron on 27/12/2017.
 */

public class AllStances  {
    private List<Stance> all_stances = new ArrayList<Stance>();
    private Context mC;
    private Stance currentStance;
    public AllStances(Context mC) {
        this.mC=mC;
        buildStancesList();
    }

    private void buildStancesList() {
        try {
            InputStream is = mC.getAssets().open("postures.xml");
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
                    all_stances.add(new Stance(
                            readValue("name", element2),
                            readShortName("shortname", element2),
                            readValue("type", element2),
                            readValue("descr", element2),
                            readValue("id", element2)));
                }
            }

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
        return all_stances;
    }

    public void activateStance(Stance selected_stance) {
        for (Stance stance : all_stances){
            stance.desactivate();
        }
        selected_stance.activate();
        currentStance=selected_stance;
    }
    public Stance getCurrentStance(){
        return currentStance;
    }

    public boolean isActive(String id){
        boolean active=false;
        if (currentStance!=null && currentStance.getId().contains(id)){
            active=true;
        }
        return  active;
    }
}
