package stellarnear.aquene_dealer.Perso;

import android.app.Activity;
import android.content.Context;
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
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import stellarnear.aquene_dealer.Divers.CustomAlertDialog;
import stellarnear.aquene_dealer.Divers.TinyDB;
import stellarnear.aquene_dealer.Divers.Tools;
import stellarnear.aquene_dealer.R;

/**
 * Created by jchatron on 05/01/2018.
 */

public class AllEquipments {
    //private Map<String, Equipment> mapSlotIdEquipment = new HashMap<>();
    private List<Equipment> listEquipments = new ArrayList<>();
    private Context mC;
    private Activity mA;
    private Boolean editable;
    private Tools tools = new Tools();
    private TinyDB tinyDB;
    private OnRefreshEventListener mListener;

    public AllEquipments(Context mC) {
        this.mC = mC;
        tinyDB = new TinyDB(mC);
        List<Equipment> listDB = tinyDB.getListEquipments("localSaveListEquipments");
        if (listDB.size() == 0) {
            buildList();
            saveLocalAllEquipments();
        } else {
            listEquipments = listDB;
        }
    }

    private void saveLocalAllEquipments() {
        tinyDB.putListEquipments("localSaveListEquipments", listEquipments);
    }

    private void buildList() {
        try {
            InputStream is = mC.getAssets().open("equipment.xml");
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            Document doc = dBuilder.parse(is);
            Element element = doc.getDocumentElement();
            element.normalize();
            NodeList nList = doc.getElementsByTagName("equipment");
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
                            tools.toBool(readValue("equipped", element2)));
                    listEquipments.add(equi);
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

    public List<Equipment> getSlotListEquipment(String slot) {
        List<Equipment> list = new ArrayList<>();
        for (Equipment equipment : listEquipments) {
            if (equipment.getSlotId().equalsIgnoreCase(slot)) {
                list.add(equipment);
            }
        }
        return list;
    }

    public List<Equipment> getSpareEquipment(String slot) {
        List<Equipment> list = new ArrayList<>();
        for (Equipment equipment : listEquipments) {
            if (equipment.getSlotId().equalsIgnoreCase(slot) && !equipment.isEquiped()) {
                list.add(equipment);
            }
        }
        return list;
    }

    public int getAllEquipmentsSize() {
        return listEquipments.size();
    }

    public List<Equipment> getListAllEquipmentsEquiped() {
        List<Equipment> list = new ArrayList<>();
        for (Equipment equipment : listEquipments) {
            if (equipment.isEquiped()) {
                list.add(equipment);
            }
        }
        return list;
    }

    private Equipment getEquipmentsEquiped(String slot) {
        Equipment equiFind = null;
        for (Equipment equipment : listEquipments) {
            if (equipment.isEquiped() && equipment.getSlotId().equalsIgnoreCase(slot)) {
                equiFind=equipment;
            }
        }
        return equiFind;
    }


    public void equip(Equipment equiToPut) {
        for (Equipment equi: getSlotListEquipment(equiToPut.getSlotId())){
            if(equi!=equiToPut){
                equi.setEquiped(false);
            }
        }
        equiToPut.setEquiped(true);
    }

    public void showSlot(Activity mA,String slotId, Boolean editable) {
        this.mA=mA;
        this.editable=editable;
        if (slotId.equalsIgnoreCase("other_slot")){
            customInfo(getSlotListEquipment("other_slot"));
        } else {
            customInfo(getEquipmentsEquiped(slotId));
        }

    }

    private void customInfo(Equipment equi) {
        LayoutInflater inflater = mA.getLayoutInflater();
        View view = inflater.inflate(R.layout.custom_toast_info, (ViewGroup) mA.findViewById(R.id.toast_RelativeLayout));
        CustomAlertDialog ct = new CustomAlertDialog(mA, mC, view);
        ct.clickToHide(view.findViewById(R.id.toast_LinearLayout));

        ImageView img = view.findViewById(R.id.toast_image);
        if (equi.getImg(mC) != null) {
            img.setImageDrawable(equi.getImg(mC));
        } else {
            img.setVisibility(View.GONE);
        }
        TextView name = view.findViewById(R.id.toast_textName);
        name.setText(equi.getName());
        TextView value = view.findViewById(R.id.toast_textVal);
        value.setText("Valeur : " + equi.getValue());
        TextView descr = view.findViewById(R.id.toast_textDescr);
        if (!equi.getDescr().equalsIgnoreCase("")) {
            descr.setText(equi.getDescr());
        } else {
            descr.setVisibility(View.GONE);
        }
        if(editable) {
            List<Equipment> spareEquipments = getSpareEquipment(equi.getSlotId());
            if (spareEquipments.size() > 0) {
                ImageView swap = view.findViewById(R.id.toast_info_swap);
                setButtonToSwap(swap, equi, spareEquipments, ct);
            }
        }
        ct.showAlert();
    }

    private void setButtonToSwap(ImageView swap,final Equipment equi, final List<Equipment> spareEquipments, final CustomAlertDialog ct) {
        swap.setVisibility(View.VISIBLE);
        swap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ct.dismissToast();
                showSpareList(spareEquipments);
            }
        });
    }

    private void showSpareList(List<Equipment> spareEquipments) {
        customInfo(spareEquipments,true);
    }

    private void customInfo(List<Equipment> equipmentsList,Boolean... selectToEquipBool) {
        Boolean selectToEquip=selectToEquipBool.length > 0 ? selectToEquipBool[0] : false;
        LayoutInflater inflater = mA.getLayoutInflater();
        View view = inflater.inflate(R.layout.custom_toast_list_info, null);
        final CustomAlertDialog ca = new CustomAlertDialog(mA, mC, view);
        ca.setPermanent(true);
        ca.clickToHide(view.findViewById(R.id.toast_list_title_frame));
        Boolean bagList =false;
        if(selectToEquip){
            TextView title = view.findViewById(R.id.toast_list_title);
            title.setText("Rechange(s) possible(s)");
        }

        LinearLayout scrollLin = view.findViewById(R.id.toast_list_scroll_mainlin);
        scrollLin.removeAllViews();
        for (final Equipment equi : equipmentsList) {
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

            if(selectToEquip && editable){
                yourLayout.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        equip(equi);
                        ca.dismissToast();
                        mListener.onEvent();
                    }
                });
            }
            scrollLin.addView(yourLayout);
        }
        ca.showAlert();
    }

    public interface OnRefreshEventListener {
        void onEvent();
    }

    public void setRefreshEventListener(OnRefreshEventListener eventListener) {
        mListener = eventListener;
    }

}

