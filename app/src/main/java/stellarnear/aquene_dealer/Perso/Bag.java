package stellarnear.aquene_dealer.Perso;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.view.Gravity;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import stellarnear.aquene_dealer.Divers.Tools;
import stellarnear.aquene_dealer.R;

public class Bag {
    private List<Equipment> listBag = new ArrayList<>();
    private List<String> listTags = new ArrayList<>();
    private SharedPreferences settings;
    private Context mC;
    private Tools tools=new Tools();

    public Bag(Context mC){
        this.mC = mC;
        settings = PreferenceManager.getDefaultSharedPreferences(mC);
        buildBag();
    }

    public void buildBag() {
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
            Equipment equi = new Equipment(name, descr, value, "", tag, false);
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
            rawBagXML = doc.getElementsByTagName("bag").item(0).getFirstChild().getNodeValue();
            is.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return rawBagXML;
    }

    public String getMoney(String key) {
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

    public void calculateTagsSums(LinearLayout tagMain) {
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

    public List<Equipment> getListBag() {
        return listBag;
    }

    public List<String> getListTags() {
        return listTags;
    }

    public int getBagSize(){
        return listBag.size();
    }

    public void remove(Equipment equi) {
        listBag.remove(equi);
        saveToString();
    }

    private void saveToString() {
        String bagList="";
        for (Equipment equi : listBag){
            String equiTxt="";
            if (!equi.getName().equalsIgnoreCase("")){ equiTxt+=equi.getName();}
            if (!equi.getDescr().equalsIgnoreCase("")){ equiTxt+="("+equi.getDescr()+")";}
            if (!equi.getValue().equalsIgnoreCase("")){ equiTxt+="["+equi.getValue()+"]";}
            if (!equi.getSlotId().equalsIgnoreCase("")){ equiTxt+="{"+equi.getSlotId()+"}";}
            if (bagList.equalsIgnoreCase("")){
                bagList += equiTxt;
            } else {
                bagList += "\n"+equiTxt;
            }

        }
        settings.edit().putString("bag_unequipped_list",bagList).apply();
    }
}
