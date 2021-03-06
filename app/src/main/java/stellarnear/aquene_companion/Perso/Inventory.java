package stellarnear.aquene_companion.Perso;

import android.app.Activity;
import android.content.Context;
import android.graphics.ColorMatrixColorFilter;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;

import stellarnear.aquene_companion.Divers.CustomAlertDialog;
import stellarnear.aquene_companion.R;

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

    public void showEquipment(Activity mA,Context mC, Boolean... canDelete) {
        editable = canDelete.length > 0 ? canDelete[0] : false;  //parametre optionnel pour editer le contenu de l'invertaire
        LayoutInflater inflater = mA.getLayoutInflater();
        View inventoryView = inflater.inflate(R.layout.equipment_dialog, null);
        equipWindow = new CustomAlertDialog(mA, mC, inventoryView);
        equipWindow.setPermanent(true);
        equipWindow.clickToHide(inventoryView.findViewById(R.id.equipment_dialog_main_title_frame));
        setImageOnDialog(mA,mC,inventoryView);
        equipWindow.showAlert();
    }

    private void setImageOnDialog(final Activity mA,final Context mC, View inventoryView) {

        float[] mat = new float[]
                {
                        1, 0, 0, 0, 0,
                        0, 2, 0, 0, 0,
                        0, 0, 1, 0, 0,
                        0, 0, 0, 1, 0,
                        0, 0, 0, 0, 1
                };
        ColorMatrixColorFilter matrixGreen = new ColorMatrixColorFilter(mat);
        float[] mat2 = new float[]
                {
                        0.5f, 0, 0, 0, 0,
                        0, 1, 0, 0, 0,
                        0, 0, 2, 0, 0,
                        0, 0, 0, 1, 0,
                        0, 0, 0, 0, 1
                };
        ColorMatrixColorFilter matrixBlue = new ColorMatrixColorFilter(mat2);
        if (bag.getBagSize() > 0) {
            int resID = mC.getResources().getIdentifier("bag", "id", mC.getPackageName());
            ImageView img = (ImageView) inventoryView.findViewById(resID);
            img.getDrawable().mutate().setColorFilter(matrixGreen);
            img.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    bag.showBag(mA, editable);
                }
            });
        }

        for (final Equipment equi : allEquipments.getListAllEquipmentsEquiped()) {
            try {
                int resID = mC.getResources().getIdentifier(equi.getSlotId(), "id", mC.getPackageName());
                ImageView img = (ImageView) inventoryView.findViewById(resID);
                img.getDrawable().mutate().setColorFilter(matrixGreen);
                img.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        allEquipments.getDisplayManager().showSlot(mA, equi.getSlotId(), editable);
                        if (editable) {
                            allEquipments.getDisplayManager().setRefreshEventListener(new DisplayEquipmentManager.OnRefreshEventListener() {
                                @Override
                                public void onEvent() {
                                    equipWindow.dismissAlert();
                                    showEquipment(mA, mC, editable);
                                }
                            });
                        }
                    }
                });
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        if (editable) {
            for (final Equipment equi : allEquipments.getDisplayManager().getAllSpareEquipment()) {
                try {
                    if (allEquipments.getEquipmentsEquiped(equi.getSlotId()) == null) {
                        int resID = mC.getResources().getIdentifier(equi.getSlotId(), "id", mC.getPackageName());
                        ImageView img = (ImageView) inventoryView.findViewById(resID);
                        img.getDrawable().mutate().setColorFilter(matrixBlue);
                        img.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                allEquipments.getDisplayManager().showSpareList(mA,allEquipments.getDisplayManager().getSpareEquipment(equi.getSlotId()),editable);
                                allEquipments.getDisplayManager().setRefreshEventListener(new DisplayEquipmentManager.OnRefreshEventListener() {
                                    @Override
                                    public void onEvent() {
                                        equipWindow.dismissAlert();
                                        showEquipment(mA,mC, editable);
                                    }
                                });

                            }
                        });

                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public Bag getBag() {
        return bag;
    }

    public AllEquipments getAllEquipments() {
        return allEquipments;
    }

    public int getAllItemsCount() {
        return bag.getBagSize() + allEquipments.getAllEquipmentsSize();
    }

    public void reset() {
        bag.reset();
        allEquipments.reset();
    }

    public void loadFromSave() {
        bag.loadFromSave();
        allEquipments.loadFromSave();
    }
}

