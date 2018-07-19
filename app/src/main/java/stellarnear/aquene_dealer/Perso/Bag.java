package stellarnear.aquene_dealer.Perso;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
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

import stellarnear.aquene_dealer.Divers.CustomAlertDialog;
import stellarnear.aquene_dealer.Divers.TinyDB;
import stellarnear.aquene_dealer.Divers.Tools;
import stellarnear.aquene_dealer.R;

public class Bag {
    private List<Equipment> listBag = new ArrayList<>();
    private List<String> listTags = new ArrayList<>();
    private SharedPreferences settings;
    private Activity mA;
    private Boolean removable;
    private Context mC;
    private Tools tools=new Tools();
    private TinyDB tinyDB;

    public Bag(Context mC){
        this.mC = mC;
        settings = PreferenceManager.getDefaultSharedPreferences(mC);

        tinyDB = new TinyDB(mC);
        List<Equipment> listDB = tinyDB.getListEquipments("localSaveListBag");
        if (listDB.size() == 0) {
            buildBag();
            saveLocalBag();
        } else {
            listBag = listDB;
        }
    }

    private void saveLocalBag() {
        tinyDB.putListEquipments("localSaveListBag", listBag);
    }

    public void buildBag() {
        listBag = new ArrayList<>();
        String rawToParse = readXMLBag();
        for (String line : rawToParse.split("\n")) {
            String lineTrim = line.trim();
            String name = "";   String descr = "";     String value = "";      String tag = "";

            int indexFirstKeyDescr = 999;  int indexFirstKeyVal = 999;    int indexFirstKeyTag = 999;
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

    private void remove(Equipment equi) {
        listBag.remove(equi);
        saveLocalBag();
    }

    public void showBag(Activity mA,Boolean removable){
        buildBag();
        this.mA=mA;
        this.removable=removable;
        customInfo();
    }

    private void customInfo() {
        LayoutInflater inflater = mA.getLayoutInflater();
        View view = inflater.inflate(R.layout.custom_toast_list_info, null);
        final CustomAlertDialog ca = new CustomAlertDialog(mA, mC, view);
        ca.setPermanent(true);
        ca.clickToHide(view.findViewById(R.id.toast_list_title_frame));

            LinearLayout money = view.findViewById(R.id.toast_list_money);
            money.setVisibility(View.VISIBLE);
            TextView title = view.findViewById(R.id.toast_list_title);
            title.setText("Inventaire du sac");
            ((TextView) view.findViewById(R.id.money_plat_text)).setText(getMoney("money_plat"));
            ((TextView) view.findViewById(R.id.money_gold_text)).setText(getMoney("money_gold"));
            ((TextView) view.findViewById(R.id.money_silver_text)).setText(getMoney("money_silver"));
            ((TextView) view.findViewById(R.id.money_copper_text)).setText(getMoney("money_copper"));
            calculateTagsSums(((LinearLayout) view.findViewById(R.id.linearTagMoney)));

        LinearLayout scrollLin = view.findViewById(R.id.toast_list_scroll_mainlin);
        scrollLin.removeAllViews();
        for (final Equipment equi : listBag) {
            View yourLayout = inflater.inflate(R.layout.custom_toast_list_element, null);
            ImageView img = yourLayout.findViewById(R.id.toast_list_element_image);
            if (equi.getImg(mC) != null) {
                img.setImageDrawable(equi.getImg(mC));
            } else {
                img.setVisibility(View.GONE);
            }
            TextView name = yourLayout.findViewById(R.id.toast_list_element_textName);
            name.setText(equi.getName());
            TextView value = yourLayout.findViewById(R.id.toast_list_element_textVal);
            value.setText("Valeur : " + equi.getValue());
            TextView descr = yourLayout.findViewById(R.id.toast_list_element_textDescr);
            if (!equi.getDescr().equalsIgnoreCase("")) {
                descr.setText(equi.getDescr());
            } else {
                descr.setVisibility(View.GONE);
            }
            if(removable){
                ImageView trash = yourLayout.findViewById(R.id.toast_info_element_trash);
                trash.setVisibility(View.VISIBLE);
                setButtonToDeleteFromBag(trash,equi,ca);
            }
            scrollLin.addView(yourLayout);
        }
        ca.showAlert();
    }

    private void setButtonToDeleteFromBag(ImageView trash, final Equipment equi,final CustomAlertDialog ca) {
        trash.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new AlertDialog.Builder(mA)
                        .setIcon(R.drawable.ic_warning_black_24dp)
                        .setTitle("Suppression")
                        .setMessage("Es-tu sûre de vouloir enlever cet objet de ton sac ?")
                        .setPositiveButton("Oui", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                ca.dismissToast();
                                remove(equi);
                                customInfo();
                            }
                        })
                        .setNegativeButton("Non", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                            }
                        }).show();
            }
        });
    }
}