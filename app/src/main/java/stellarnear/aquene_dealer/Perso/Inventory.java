package stellarnear.aquene_dealer.Perso;

import android.app.Activity;

import android.content.Context;

import android.graphics.ColorMatrixColorFilter;

import android.view.LayoutInflater;
import android.view.View;

import android.widget.ImageView;

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
    private CustomAlertDialog equipWindow;
    private boolean editable;

    public Inventory(Context mC) {
        this.mC = mC;
        this.bag = new Bag(mC);
        this.allEquipments = new AllEquipments(mC);
    }

    public void showEquipment(Activity mA,Boolean... canDelete){
        this.mA = mA;
        editable=canDelete.length > 0 ? canDelete[0] : false;  //parametre optionnel pour editer le contenu de l'invertaire

        LayoutInflater inflater = mA.getLayoutInflater();
        inventoryView = inflater.inflate(R.layout.equipment_dialog, null);
        equipWindow = new CustomAlertDialog(mA, mC, inventoryView);
        equipWindow.setPermanent(true);
        equipWindow.clickToHide(inventoryView.findViewById(R.id.equipment_dialog_main_title_frame));
        setImageOnDialog();
        equipWindow.showAlert();
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
                    bag.showBag(mA,editable);
                }
            });
        }

        for (final Equipment equi : allEquipments.getListAllEquipmentsEquiped()) {
            try {
                int resID = mC.getResources().getIdentifier(equi.getSlotId(), "id", mC.getPackageName());
                ImageView img = (ImageView) inventoryView.findViewById(resID);
                img.getDrawable().setColorFilter(matrix);
                img.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        allEquipments.showSlot(mA,equi.getSlotId(),editable);
                        allEquipments.setRefreshEventListener(new AllEquipments.OnRefreshEventListener() {
                            @Override
                            public void onEvent() {
                                equipWindow.dismissToast();
                                showEquipment(mA,editable);
                            }
                        });
                    }
                });
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }


    public int getAllItemsCount() {
        return bag.getBagSize() + allEquipments.getAllEquipmentsSize();
    }
}

