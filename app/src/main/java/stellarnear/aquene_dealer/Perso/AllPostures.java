package stellarnear.aquene_dealer.Perso;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.ScaleDrawable;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import stellarnear.aquene_dealer.R;
/**
 * Created by jchatron on 27/12/2017.
 */

public class AllPostures {
    private List<Posture> all_stance = new ArrayList<Posture>();
    private Context mC;
    public AllPostures(Context mC) {
        try {
            this.mC=mC;
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
                    all_stance.add(new Posture(
                            readValue("name", element2),
                            readValue("type", element2),
                            readValue("descr", element2),
                            readDrawable("drawable", element2)));
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
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
    public Drawable readDrawable(String tag, Element element) {
        try {
            NodeList nodeList = element.getElementsByTagName(tag).item(0).getChildNodes();
            Node node = nodeList.item(0);

            int id = mC.getResources().getIdentifier(node.getNodeValue(), "drawable", mC.getPackageName());

            return mC.getDrawable(id);

        } catch (Exception e){
            return null;
        }
    }

    public List<Posture> getStancesList(){
        return all_stance;
    }
}
