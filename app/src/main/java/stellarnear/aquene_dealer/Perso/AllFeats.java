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
 * Created by jchatron on 26/12/2017.
 */

public class AllFeats {
    Context mC;
    List<Feat> all_feats= new ArrayList<>();
    private Map<String,Feat> mapIdFeat=new HashMap<>();
    public AllFeats(Context mC)
    {
        this.mC = mC;
        buildFeatsList();
    }

    private void buildFeatsList() {
        try {
            InputStream is = mC.getAssets().open("dons.xml");
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            Document doc = dBuilder.parse(is);

            Element element = doc.getDocumentElement();
            element.normalize();

            NodeList nList = doc.getElementsByTagName("don");

            for (int i = 0; i < nList.getLength(); i++) {

                Node node = nList.item(i);
                if (node.getNodeType() == Node.ELEMENT_NODE) {
                    Element element2 = (Element) node;
                    Feat feat=new Feat(
                            readValue("name", element2),
                            readValue("type", element2),
                            readValue("descr", element2),
                            readValue("id", element2),
                            readValue("stance_id", element2),
                            mC);
                    all_feats.add(feat);
                    mapIdFeat.put(feat.getId(),feat);
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public List<Feat> getFeatsList(){
        return all_feats;
    }

    public Feat getFeat(String feat_id) {
        Feat selected_feat=null;
        try {
            selected_feat=mapIdFeat.get(feat_id);
        } catch (Exception e){  }
        return selected_feat;
    }

    public boolean isActive(String id){
        Feat wanted_feat=getFeat(id);
        boolean active=false;
        if (wanted_feat!=null && wanted_feat.isActive()){
            active=true;
        }
        return  active;
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


    public void refreshAllSwitch() {
        for (Feat feat : all_feats){
            feat.refreshSwitch();
        }
    }
}
