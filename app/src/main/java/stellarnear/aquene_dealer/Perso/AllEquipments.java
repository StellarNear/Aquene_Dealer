package stellarnear.aquene_dealer.Perso;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.ColorMatrixColorFilter;
import android.preference.PreferenceManager;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import stellarnear.aquene_dealer.Divers.CustomAlertDialog;
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
    private List<String> listTags = new ArrayList<>();
    private Activity mA;
    private Context mC;
    private  SharedPreferences settings;
    private Tools tools = new Tools();
    private View equipView;

    public AllEquipments(Context mC) {
        this.mC = mC;
        settings = PreferenceManager.getDefaultSharedPreferences(mC);
        buildList("equipped_slot", listEquipment);
        buildList("equipped_slotless", listOther);
        buildBag(); // TODO : objet séparé bag
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
        listBag = new ArrayList<>();
        String rawBagPref = settings.getString("bag_unequipped_list", "");

        String rawToParse = "";
        if (!rawBagPref.equalsIgnoreCase("")) {
            rawToParse = rawBagPref;
        } else {
            rawToParse = readXMLBag();
            settings.edit().putString("bag_unequipped_list",rawToParse).apply();
        }

        for (String line : rawToParse.split("\n")) {
            String lineTrim = line.trim();
            String name = "";
            String descr = "";
            String value = "";
            String tag = "";

            int indexFirstKeyDescr = 999;
            int indexFirstKeyVal = 999;
            int indexFirstKeyTag = 999;
            try {
                descr = lineTrim.substring(lineTrim.indexOf("(") + 1, lineTrim.indexOf(")"));
                indexFirstKeyDescr = lineTrim.indexOf("(");
            } catch (Exception e) {
                e.printStackTrace();
            }
            try {
                value = lineTrim.substring(lineTrim.indexOf("[") + 1, lineTrim.indexOf("]"));
                indexFirstKeyVal = lineTrim.indexOf("[");
            } catch (Exception e) {
                e.printStackTrace();
            }

            try {
                tag = lineTrim.substring(lineTrim.indexOf("{") + 1, lineTrim.indexOf("}"));
                if (!listBag.contains(tag)) {
                    listTags.add(tag);
                }
                indexFirstKeyTag = lineTrim.indexOf("{");
            } catch (Exception e) {
                e.printStackTrace();
            }

            int indexFirstKey = Collections.min(Arrays.asList(indexFirstKeyDescr, indexFirstKeyVal, indexFirstKeyTag));

            if (indexFirstKey > 0 && indexFirstKey != 999) {
                name = lineTrim.substring(0, indexFirstKey);
            } else {
                name = lineTrim;
            }
            Equipment equi = new Equipment(name, descr, value, "", tag, mC);
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
        this.mA=mA;
        LayoutInflater inflater = mA.getLayoutInflater();
        equipView = inflater.inflate(R.layout.equipment_dialog, null);
        CustomAlertDialog ca = new CustomAlertDialog(mA,mC,equipView);
        ca.setPermanent(true);
        ca.clickToHide(equipView.findViewById(R.id.equipment_dialog_main_title_frame));
        setImageOnDialog();
        ca.showAlert();
    }

    private void setImageOnDialog() {
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
                    buildBag();
                    customInfo(listBag);
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
                    customInfo(listOther);
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
                        customInfo(equi);
                    }
                });

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private void customInfo(Equipment equi) {
        LayoutInflater inflater = mA.getLayoutInflater();
        View view = inflater.inflate(R.layout.custom_toast_info, (ViewGroup) mA.findViewById(R.id.toast_RelativeLayout));
        ImageView img = view.findViewById(R.id.toast_image);
        if (equi.getImg()!=null){ img.setImageDrawable(equi.getImg());} else {img.setVisibility(View.GONE);}
        TextView name = view.findViewById(R.id.toast_textName);
        name.setText(equi.getName());
        TextView value = view.findViewById(R.id.toast_textVal);
        value.setText("Valeur : " + equi.getValue());
        TextView descr = view.findViewById(R.id.toast_textDescr);
        if (!equi.getDescr().equalsIgnoreCase("")){
            descr.setText( equi.getDescr() );
        } else { descr.setVisibility(View.GONE);}

        CustomAlertDialog ct = new CustomAlertDialog(mA,mC, view);
        ct.clickToHide(view.findViewById(R.id.toast_LinearLayout));
        ct.showAlert();
    }

    private void customInfo(List<Equipment> equipmentsList) {
        LayoutInflater inflater = mA.getLayoutInflater();
        View view = inflater.inflate(R.layout.custom_toast_list_info, null);
        if(equipmentsList.equals(listBag)){
            LinearLayout money = view.findViewById(R.id.toast_list_money);
            money.setVisibility(View.VISIBLE);
            TextView title = view.findViewById(R.id.toast_list_title);
            title.setText("Inventaire du sac");
            ((TextView)view.findViewById(R.id.money_plat_text)).setText(getMoney("money_plat"));
            ((TextView)view.findViewById(R.id.money_gold_text)).setText(getMoney("money_gold"));
            ((TextView)view.findViewById(R.id.money_silver_text)).setText(getMoney("money_silver"));
            ((TextView)view.findViewById(R.id.money_copper_text)).setText(getMoney("money_copper"));
            calculateTagsSums(((LinearLayout) view.findViewById(R.id.linearTagMoney)));
        }

        LinearLayout scrollLin = view.findViewById(R.id.toast_list_scroll_mainlin);
        scrollLin.removeAllViews();
        for (Equipment equi : equipmentsList) {
            View yourLayout = inflater.inflate(R.layout.custom_toast_list_element, null);
            ImageView img = yourLayout.findViewById(R.id.toast_list_element_image);
            if (equi.getImg()!=null){ img.setImageDrawable(equi.getImg());} else {img.setVisibility(View.GONE);}
            TextView name = yourLayout.findViewById(R.id.toast_list_element_textName);
            name.setText(equi.getName());
            TextView value = yourLayout.findViewById(R.id.toast_list_element_textVal);
            value.setText("Valeur : " + equi.getValue());
            TextView descr = yourLayout.findViewById(R.id.toast_list_element_textDescr);
            if (!equi.getDescr().equalsIgnoreCase("")){
                descr.setText(equi.getDescr());
            } else { descr.setVisibility(View.GONE);}
            scrollLin.addView(yourLayout);
        }

        CustomAlertDialog ca = new CustomAlertDialog(mA,mC, view);
        ca.setPermanent(true);
        ca.clickToHide(view.findViewById(R.id.toast_list_title_frame));
        ca.showAlert();
    }

    private String getMoney(String key) {
        long money = tools.toLong(settings.getString(key,"0"));
        String appendix ="";
        if (money>=1000000000){
            money = money/1000000000;
            appendix+="G";
        }
        if (money>=1000000){
            money = money/1000000;
            appendix+="M";
        }
        if (money>=1000){
            money = money/1000;
            appendix+="k";
        }
        String moneyTxt = String.valueOf(money)+appendix;
        return moneyTxt;
    }

    private void calculateTagsSums(LinearLayout tagMain) {
        List<String> displayedTags=new ArrayList<>();
        if (listTags.size() > 0) {
            tagMain.removeAllViews();
            tagMain.setVisibility(View.VISIBLE);
            for (String tag : listTags) {
                if (!displayedTags.contains(tag)) {
                    TextView text = new TextView(mC);
                    text.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT));
                    text.setGravity(Gravity.CENTER);
                    text.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
                    text.setText(tag + " : " + getSumPo(tag));
                    text.setCompoundDrawablesWithIntrinsicBounds(null, null, mC.getDrawable(R.drawable.ic_gold_coin), null);
                    tagMain.addView(text);
                    displayedTags.add(tag);
                }
            }
        }
    }

    private String getSumPo(String tag) {
        int moneySum = 0;
        for (Equipment equi : listBag) {
            if (equi.getSlotId().equalsIgnoreCase(tag)) {
                moneySum += getPo(equi.getValue());
            }
        }
        return String.valueOf(moneySum);
    }

    private int getPo(String value) {
        int po = 0;
        try {
            String numberTxt = value.substring(0, value.indexOf("p"));
            if (value.contains("po")) {
                po = tools.toInt(numberTxt.trim());
            } else if (value.contains("pp")) {
                po = 10 * tools.toInt(numberTxt.trim());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return po;
    }

    public int getAllItemsCount() {
        return listBag.size() + listEquipment.size() + listOther.size();
    }
}

