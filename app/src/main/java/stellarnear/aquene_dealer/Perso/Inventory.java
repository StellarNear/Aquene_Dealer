package stellarnear.aquene_dealer.Perso;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.ColorMatrixColorFilter;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.gson.Gson;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import stellarnear.aquene_dealer.Activities.MainActivity;
import stellarnear.aquene_dealer.Divers.CustomAlertDialog;
import stellarnear.aquene_dealer.R;

/**
 * Created by jchatron on 05/01/2018.
 */

public class Inventory {

    private AllEquipments allEquipments;
    private Bag bag;
    private Activity mA;
    private Context mC;
    private View inventoryView;

    public Inventory(Context mC) {
        this.mC = mC;
        this.bag = new Bag(mC);
        this.allEquipments = new AllEquipments(mC);
    }

    public void showEquipment(Activity mA) {
        this.mA = mA;
        LayoutInflater inflater = mA.getLayoutInflater();
        inventoryView = inflater.inflate(R.layout.equipment_dialog, null);
        CustomAlertDialog ca = new CustomAlertDialog(mA, mC, inventoryView);
        ca.setPermanent(true);
        ca.clickToHide(inventoryView.findViewById(R.id.equipment_dialog_main_title_frame));
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

        if (bag.getBagSize() > 0) {
            int resID = mC.getResources().getIdentifier("bag", "id", mC.getPackageName());
            ImageView img = (ImageView) inventoryView.findViewById(resID);
            img.getDrawable().setColorFilter(matrix);
            img.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    bag.buildBag();
                    customInfo(bag.getListBag());
                }
            });
        }
        final List<Equipment> otherEquip = allEquipments.getSlotListEquipment("other_slot");
        if (otherEquip.size() > 0) {
            int resID = mC.getResources().getIdentifier("other_slot", "id", mC.getPackageName());
            ImageView img = (ImageView) inventoryView.findViewById(resID);
            img.getDrawable().setColorFilter(matrix);
            img.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    customInfo(otherEquip);
                }
            });
        }

        for (final Equipment equi : allEquipments.getListAllEquipmentsEquiped()) {
            if (otherEquip.size() > 0 && otherEquip.contains(equi)) {
                continue;
            } //ils sont traités plus haut à part pour faire une liste
            try {
                int resID = mC.getResources().getIdentifier(equi.getSlotId(), "id", mC.getPackageName());
                ImageView img = (ImageView) inventoryView.findViewById(resID);
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

        CustomAlertDialog ct = new CustomAlertDialog(mA, mC, view);
        ct.clickToHide(view.findViewById(R.id.toast_LinearLayout));
        ct.showAlert();
    }

    private void customInfo(List<Equipment> equipmentsList) {
        LayoutInflater inflater = mA.getLayoutInflater();
        View view = inflater.inflate(R.layout.custom_toast_list_info, null);
        CustomAlertDialog ca = new CustomAlertDialog(mA, mC, view);
        ca.setPermanent(true);
        ca.clickToHide(view.findViewById(R.id.toast_list_title_frame));
        Boolean bagList =false;
        if (equipmentsList.equals(bag.getListBag())) {
            bagList=true;
            LinearLayout money = view.findViewById(R.id.toast_list_money);
            money.setVisibility(View.VISIBLE);
            TextView title = view.findViewById(R.id.toast_list_title);
            title.setText("Inventaire du sac");
            ((TextView) view.findViewById(R.id.money_plat_text)).setText(bag.getMoney("money_plat"));
            ((TextView) view.findViewById(R.id.money_gold_text)).setText(bag.getMoney("money_gold"));
            ((TextView) view.findViewById(R.id.money_silver_text)).setText(bag.getMoney("money_silver"));
            ((TextView) view.findViewById(R.id.money_copper_text)).setText(bag.getMoney("money_copper"));
            bag.calculateTagsSums(((LinearLayout) view.findViewById(R.id.linearTagMoney)));
        }

        LinearLayout scrollLin = view.findViewById(R.id.toast_list_scroll_mainlin);
        scrollLin.removeAllViews();
        for (Equipment equi : equipmentsList) {
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
            /* TODO decommenter pour continuer de travailler sur les swap et delete
            if(bagList){
                ImageView trash = yourLayout.findViewById(R.id.toast_info_element_trash);
                trash.setVisibility(View.VISIBLE);
                setButtonToDeleteFromBag(trash,equi,ca);
            } */
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
                                bag.remove(equi);
                                customInfo(bag.getListBag());
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

    public int getAllItemsCount() {
        return bag.getBagSize() + allEquipments.getAllEquipmentsSize();
    }
}

