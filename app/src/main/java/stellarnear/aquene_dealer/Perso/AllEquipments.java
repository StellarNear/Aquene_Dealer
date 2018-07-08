package stellarnear.aquene_dealer.Perso;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.graphics.PorterDuff;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

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

import stellarnear.aquene_dealer.Activities.MainActivity;
import stellarnear.aquene_dealer.Divers.Tools;
import stellarnear.aquene_dealer.R;

/**
 * Created by jchatron on 05/01/2018.
 */

public class AllEquipments {

    private Map<String, Equipment> mapIDEquipment = new HashMap<>();
    private List<Equipment> listEquipment = new ArrayList<>();
    private Map<String, Equipment> mapIDBag = new HashMap<>();
    private List<Equipment> listBag = new ArrayList<>();
    private Context mC;
    private Tools tools = new Tools();

    public AllEquipments(Context mC) {
        this.mC = mC;
        buildAllEquipmentsList();
        buildAllBagList();
        //setImageOnDialog();
    }

    private void buildAllEquipmentsList() {
        try {
            InputStream is = mC.getAssets().open("equipment.xml");
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            Document doc = dBuilder.parse(is);
            Element element = doc.getDocumentElement();
            element.normalize();
            NodeList nList = doc.getElementsByTagName("equipped");
            for (int i = 0; i < nList.getLength(); i++) {
                Node node = nList.item(i);
                if (node.getNodeType() == Node.ELEMENT_NODE) {
                    Element element2 = (Element) node;
                    Equipment equi = new Equipment(
                            readValue("name", element2),
                            readValue("descr", element2),
                            readValue("imgId", element2),
                            readValue("slotId", element2),
                            mC);
                    listEquipment.add(equi);
                    mapIDEquipment.put(equi.getSlotId(), equi);
                }
            }
            is.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void buildAllBagList() {
        try {
            InputStream is = mC.getAssets().open("equipment.xml");
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            Document doc = dBuilder.parse(is);
            Element element = doc.getDocumentElement();
            element.normalize();
            NodeList nList = doc.getElementsByTagName("unequipped");
            for (int i = 0; i < nList.getLength(); i++) {
                Node node = nList.item(i);
                if (node.getNodeType() == Node.ELEMENT_NODE) {
                    Element element2 = (Element) node;
                    Equipment equi = new Equipment(
                            readValue("name", element2),
                            readValue("descr", element2),
                            readValue("imgId", element2),
                            readValue("slotId", element2),
                            mC);
                    listBag.add(equi);
                    mapIDBag.put(equi.getSlotId(), equi);
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


    public void setImageOnDialog(final Activity mA,View equipView) {

        for (final Equipment equi : listEquipment) {
            try {
                int resID = mC.getResources().getIdentifier(equi.getSlotId(), "id", mC.getPackageName());
                ImageView img = (ImageView) equipView.findViewById(resID);

                float[] mat = new float[]
                        {
                                1,0,0,0,0,
                                0,2,0,0,0,
                                0,0,1,0,0,
                                0,0,0,1,0,
                                0,0,0,0,1
                        };
                ColorMatrixColorFilter matrix = new ColorMatrixColorFilter(mat);
                img.getDrawable().setColorFilter(matrix);

                img.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        toatInfo(mA,equi);
                    }
                });

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public void toatInfo(Activity mA,Equipment equi) {
        LayoutInflater inflater = mA.getLayoutInflater();
        View view = inflater.inflate(R.layout.custom_toast_info,(ViewGroup) mA.findViewById(R.id.toast_RelativeLayout));

        ImageView img =  view.findViewById(R.id.toast_image);
        img.setImageDrawable(equi.getImg());
        TextView name = view.findViewById(R.id.toast_textName);
        name.setText(equi.getName());
        TextView descr = view.findViewById(R.id.toast_textDescr);
        descr.setText(equi.getDescr());
        tools.toastStanceTooltip(mC,view,"long");
    }

    public int getAllItemsCount(){
        return listBag.size()+listEquipment.size();
    }
}
