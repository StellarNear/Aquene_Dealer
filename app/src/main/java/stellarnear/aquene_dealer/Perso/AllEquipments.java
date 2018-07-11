package stellarnear.aquene_dealer.Perso;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.graphics.ColorMatrixColorFilter;
import android.graphics.Point;
import android.preference.PreferenceManager;
import android.support.v7.app.AlertDialog;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import stellarnear.aquene_dealer.Divers.Tools;
import stellarnear.aquene_dealer.R;

/**
 * Created by jchatron on 05/01/2018.
 */

public class AllEquipments {

    //private Map<String, Equipment> mapIDEquipment = new HashMap<>();
    private List<Equipment> listEquipment = new ArrayList<>();
    //private Map<String, Equipment> mapIDOther = new HashMap<>();
    private List<Equipment> listOther = new ArrayList<>();
    //private Map<String, Equipment> mapIDBag = new HashMap<>();
    private List<Equipment> listBag = new ArrayList<>();
    private Context mC;
    private Tools tools = new Tools();
    private View equipView;

    public AllEquipments(Context mC) {
        this.mC = mC;
        buildList("equipped_slot", listEquipment);
        buildList("equipped_slotless", listOther);

        buildBag();
    }


    private void buildList(String tag, List<Equipment> list) {
        try {
            InputStream is = mC.getAssets().open("equipment.xml");

            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            Document doc = dBuilder.parse(is);
            Element element = doc.getDocumentElement();
            element.normalize();
            NodeList nList = doc.getElementsByTagName(tag);
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
                            mC);
                    list.add(equi);
                    //map.put(equi.getSlotId(), equi);
                }
            }
            is.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void buildBag() {
        SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(mC);
        String rawBagPref = settings.getString("bag_unequipped_pref", "");

        String rawToParse = "";
        if (!rawBagPref.equalsIgnoreCase("")) {
            rawToParse = rawBagPref;
        } else {
            rawToParse = readXMLBag();
        }

        for (String line : rawToParse.split("\n")) {
            String name = "";
            String value = "";

            try {
                name = line.substring(0, line.indexOf("("));
                value = line.substring(line.indexOf("(")+1, line.indexOf(")"));
            } catch (Exception e) {
                name=line;
                e.printStackTrace();
            }

            Equipment equi = new Equipment(name, "", value, "", "", mC);
            listBag.add(equi);
        }
    }

    private String readXMLBag() {
        String rawBagXML = "";
        try {
            InputStream is = mC.getAssets().open("equipment.xml");
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            Document doc = dBuilder.parse(is);
            Element element = doc.getDocumentElement();
            element.normalize();
            rawBagXML = doc.getElementsByTagName("unequipped").item(0).getFirstChild().getNodeValue();
            is.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return rawBagXML;
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

    public void showEquipment(Activity mA) {
        LayoutInflater inflater = mA.getLayoutInflater();
        equipView = inflater.inflate(R.layout.equipment_dialog, null);
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(mA, R.style.CustomDialog);
        dialogBuilder.setView(equipView);
        dialogBuilder.setNegativeButton("Ok", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                // User clicked cancel button
            }
        });
        AlertDialog alertDialog = dialogBuilder.create();
        alertDialog.show();
        Display display = mA.getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        Float factor = mC.getResources().getInteger(R.integer.percent_fullscreen_customdialog) / 100f;
        alertDialog.getWindow().setLayout((int) (factor * size.x), (int) (factor * size.y));
        Button onlyButton = alertDialog.getButton(AlertDialog.BUTTON_NEGATIVE);
        LinearLayout.LayoutParams onlyButtonLL = (LinearLayout.LayoutParams) onlyButton.getLayoutParams();
        onlyButtonLL.width = ViewGroup.LayoutParams.WRAP_CONTENT;
        onlyButton.setLayoutParams(onlyButtonLL);
        onlyButton.setTextColor(mC.getColor(R.color.colorBackground));
        onlyButton.setBackground(mC.getDrawable(R.drawable.button_ok_gradient));

        setImageOnDialog(mA);
    }

    private void setImageOnDialog(final Activity mA) {

        float[] mat = new float[]
                {
                        1, 0, 0, 0, 0,
                        0, 2, 0, 0, 0,
                        0, 0, 1, 0, 0,
                        0, 0, 0, 1, 0,
                        0, 0, 0, 0, 1
                };
        ColorMatrixColorFilter matrix = new ColorMatrixColorFilter(mat);

        if (listBag.size() > 0) {
            int resID = mC.getResources().getIdentifier("bag_slot", "id", mC.getPackageName());
            ImageView img = (ImageView) equipView.findViewById(resID);
            img.getDrawable().setColorFilter(matrix);
            img.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    toatListInfo(mA, listBag);
                }
            });
        }
        if (listOther.size() > 0) {
            int resID = mC.getResources().getIdentifier("other_slot", "id", mC.getPackageName());
            ImageView img = (ImageView) equipView.findViewById(resID);
            img.getDrawable().setColorFilter(matrix);
            img.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    toatListInfo(mA, listOther);
                }
            });
        }

        for (final Equipment equi : listEquipment) {
            try {
                int resID = mC.getResources().getIdentifier(equi.getSlotId(), "id", mC.getPackageName());
                ImageView img = (ImageView) equipView.findViewById(resID);
                img.getDrawable().setColorFilter(matrix);
                img.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        toatInfo(mA, equi);
                    }
                });

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private void toatInfo(Activity mA, Equipment equi) {
        LayoutInflater inflater = mA.getLayoutInflater();
        View view = inflater.inflate(R.layout.custom_toast_info, (ViewGroup) mA.findViewById(R.id.toast_RelativeLayout));
        ImageView img = view.findViewById(R.id.toast_image);
        img.setImageDrawable(equi.getImg());
        TextView name = view.findViewById(R.id.toast_textName);
        name.setText(equi.getName());
        TextView descr = view.findViewById(R.id.toast_textDescr);
        String descrTxt = "Valeur : " + equi.getValue();
        if (!equi.getDescr().equalsIgnoreCase("")){ descrTxt+= "\n\n" + equi.getDescr();}
        descr.setText( descrTxt );
        tools.toastTooltipCustomView(mC, view, "long");
    }

    private void toatListInfo(Activity mA, List<Equipment> equipmentsList) {

        LayoutInflater inflater = mA.getLayoutInflater();
        View view = inflater.inflate(R.layout.custom_toast_slotless_list_info, (ViewGroup) mA.findViewById(R.id.toast_list_RelativeLayout));

        if(equipmentsList.equals(listBag)){
            LinearLayout mainlin = view.findViewById(R.id.toast_list_linear);
            LinearLayout money = new LinearLayout(mC);
            money.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));

            for(int i=0;i<=3;i++) {
                TextView text = new TextView(mC);
            text.setLayoutParams(new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT, 1));
            text.setText("bon bah "+String.valueOf(i));
            money.addView(text);
            }
            mainlin.addView(money,0);
        }

        LinearLayout scrollLin = view.findViewById(R.id.toast_list_scroll_mainlin);
        for (Equipment equi : equipmentsList) {
            View yourLayout = inflater.inflate(R.layout.custom_toast_slotless_list_element, scrollLin, false);
            ImageView img = yourLayout.findViewById(R.id.toast_list_element_image);
            img.setImageDrawable(equi.getImg());
            TextView name = yourLayout.findViewById(R.id.toast_list_element_textName);
            name.setText(equi.getName());
            TextView descr = yourLayout.findViewById(R.id.toast_list_element_textDescr);

            String descrTxt = "Valeur : " + equi.getValue();
            if (!equi.getDescr().equalsIgnoreCase("")){ descrTxt+= "\n\n" + equi.getDescr();}
            descr.setText( descrTxt );

            scrollLin.addView(yourLayout);
        }

        tools.toastTooltipCustomView(mC, view, "long");
    }

    public int getAllItemsCount() {
        return listBag.size() + listEquipment.size() + listOther.size();
    }


}

