package stellarnear.aquene_dealer.Divers.SettingsFragments;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.preference.Preference;
import android.preference.PreferenceCategory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import stellarnear.aquene_dealer.Activities.MainActivity;
import stellarnear.aquene_dealer.Divers.CustomAlertDialog;
import stellarnear.aquene_dealer.Divers.Tools;
import stellarnear.aquene_dealer.Perso.Equipment;
import stellarnear.aquene_dealer.Perso.Perso;
import stellarnear.aquene_dealer.R;


public class PrefAllInventoryFragment {

    private Perso aquene= MainActivity.aquene;
    private Activity mA;
    private Context mC;
    private Tools tools=new Tools();
    private OnRefreshEventListener mListener;

    public PrefAllInventoryFragment(Activity mA,Context mC){
        this.mA=mA;
        this.mC=mC;
    }

    public void addEditableEquipment(PreferenceCategory otherList, PreferenceCategory spareList) {
        addOtherSlotEquipment(otherList);
        addSpareEquipmentList(spareList);
    }

    private void addOtherSlotEquipment(PreferenceCategory otherList) {
        for (final Equipment equi : aquene.getInventory().getAllEquipments().getSlotListEquipment("other_slot")) {
            Preference pref = new Preference(mC);
            pref.setKey("equipment_" + equi.getName());
            pref.setTitle(equi.getName());
            String txt = "Valeur : " + equi.getValue();
            if (!equi.getSlotId().equalsIgnoreCase("")) {
                txt += "\nEmplacement : " + translateSlotName(equi.getSlotId());
            }
            if (!equi.getDescr().equalsIgnoreCase("")) {
                txt += "\n" + equi.getDescr();
            }
            pref.setSummary(txt);
            pref.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
                @Override
                public boolean onPreferenceClick(Preference preference) {
                    new AlertDialog.Builder(mC)
                            .setIcon(R.drawable.ic_warning_black_24dp)
                            .setTitle("Suppression de l'équipement")
                            .setMessage("Es-tu sûre de vouloir jeter cet équipement ?")
                            .setPositiveButton("Oui", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    aquene.getInventory().getAllEquipments().remove(equi);
                                    if(mListener!=null){mListener.onEvent();}
                                }
                            })
                            .setNegativeButton("Non", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                }
                            })
                            .show();
                    return false;
                }
            });
            otherList.addPreference(pref);
        }
    }

    private void addSpareEquipmentList(PreferenceCategory spareList) {
        for (final Equipment equi : aquene.getInventory().getAllEquipments().getAllSpareEquipment()) {
            Preference pref = new Preference(mC);
            pref.setKey("equipment_" + equi.getName());
            pref.setTitle(equi.getName());
            String txt = "Valeur : " + equi.getValue();
            if (!equi.getSlotId().equalsIgnoreCase("")) {
                txt += "\nEmplacement : " + translateSlotName(equi.getSlotId());
            }
            if (!equi.getDescr().equalsIgnoreCase("")) {
                txt += "\n" + equi.getDescr();
            }
            pref.setSummary(txt);
            pref.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
                @Override
                public boolean onPreferenceClick(Preference preference) {
                    new AlertDialog.Builder(mC)
                            .setIcon(R.drawable.ic_warning_black_24dp)
                            .setTitle("Suppression de l'équipement")
                            .setMessage("Es-tu sûre de vouloir jeter cet équipement ?")
                            .setPositiveButton("Oui", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    aquene.getInventory().getAllEquipments().remove(equi);
                                    if(mListener!=null){mListener.onEvent();}
                                }
                            })
                            .setNegativeButton("Non", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                }
                            })
                            .show();
                    return false;
                }
            });
            spareList.addPreference(pref);
        }
    }

    private String translateSlotName(String slotId) {
        String val = "";
        String[] vals = mC.getResources().getStringArray(R.array.slot_choice_val);
        String[] name = mC.getResources().getStringArray(R.array.slot_choice_name);

        for (int i = 0; i < vals.length; i++) {
            if (vals[i].equalsIgnoreCase(slotId)) {
                val = name[i];
                break;
            }
        }
        return val;
    }

    public void addBagList(PreferenceCategory bagList) {
        for (final Equipment equi : aquene.getInventory().getBag().getListBag()) {
            Preference pref = new Preference(mC);
            pref.setKey("bag_" + equi.getName());
            pref.setTitle(equi.getName());
            String txt = "Valeur : " + equi.getValue();
            if (!equi.getSlotId().equalsIgnoreCase("")) {
                txt += "\nTag : " + equi.getSlotId();
            }
            if (!equi.getDescr().equalsIgnoreCase("")) {
                txt += "\n" + equi.getDescr();
            }
            pref.setSummary(txt);
            pref.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
                @Override
                public boolean onPreferenceClick(Preference preference) {
                    new AlertDialog.Builder(mC)
                            .setIcon(R.drawable.ic_warning_black_24dp)
                            .setTitle("Suppression de l'objet")
                            .setMessage("Es-tu sûre de vouloir jeter cet objet ?")
                            .setPositiveButton("Oui", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    aquene.getInventory().getBag().remove(equi);
                                    if(mListener!=null){mListener.onEvent();}
                                }
                            })
                            .setNegativeButton("Non", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                }
                            })
                            .show();
                    return false;
                }
            });
            bagList.addPreference(pref);
        }
    }

    public void createBagItem() {
        LayoutInflater inflater = mA.getLayoutInflater();
        final View creationView = inflater.inflate(R.layout.custom_toast_bag_item_creation, null);

        CustomAlertDialog creationItemAlert = new CustomAlertDialog(mA, mC, creationView);
        creationItemAlert.setPermanent(true);
        creationItemAlert.addConfirmButton("Créer");
        creationItemAlert.addCancelButton("Annuler");
        creationItemAlert.setAcceptEventListener(new CustomAlertDialog.OnAcceptEventListener() {
            @Override
            public void onEvent() {
                String name = ((EditText) creationView.findViewById(R.id.name_item_creation)).getText().toString();
                String value = ((EditText) creationView.findViewById(R.id.value_item_creation)).getText().toString() + " po";
                String tag = ((EditText) creationView.findViewById(R.id.tag_item_creation)).getText().toString();
                String descr = ((EditText) creationView.findViewById(R.id.descr_item_creation)).getText().toString();
                Equipment equi = new Equipment(name, descr, value, "", tag, false);
                aquene.getInventory().getBag().createItem(equi);
                if(mListener!=null){mListener.onEvent();}
                tools.customToast(mC, equi.getName() + " ajouté !");
            }
        });
        creationItemAlert.showAlert();
        final EditText editName = ((EditText) creationView.findViewById(R.id.name_item_creation));
        editName.post(new Runnable() {
            public void run() {
                editName.setFocusableInTouchMode(true);
                editName.requestFocusFromTouch();
                InputMethodManager lManager = (InputMethodManager) mA.getSystemService(Context.INPUT_METHOD_SERVICE);
                lManager.showSoftInput(editName, InputMethodManager.SHOW_IMPLICIT);
            }
        });
    }

    public void createEquipment() {
        LayoutInflater inflater = mA.getLayoutInflater();
        View selectItem = inflater.inflate(R.layout.equipment_dialog, null);
        ((TextView) selectItem.findViewById(R.id.equipment_dialog_main_title)).setText("Selectionnes l'emplacement");
        selectItem.findViewById(R.id.bag).setVisibility(View.GONE);
        selectItem.findViewById(R.id.equipment_dialog_back_arrow).setVisibility(View.GONE);
        final CustomAlertDialog selectItemAlert = new CustomAlertDialog(mA,mC, selectItem);
        selectItemAlert.setPermanent(true);
        selectItemAlert.addCancelButton("Annuler");
        for (final String slot : mC.getResources().getStringArray(R.array.slot_choice_val)) {
            int resID = mC.getResources().getIdentifier(slot, "id", mC.getPackageName());
            ImageView img = (ImageView) selectItem.findViewById(resID);
            img.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    selectItemAlert.dismissAlert();
                    createEquipment(slot);
                }
            });
        }
        selectItemAlert.showAlert();
    }

    private void createEquipment(final String slot) {
        LayoutInflater inflater = mA.getLayoutInflater();
        final View creationView = inflater.inflate(R.layout.custom_toast_equipment_creation, null);
        CustomAlertDialog creationEquipmentAlert = new CustomAlertDialog(mA,mC, creationView);
        creationEquipmentAlert.setPermanent(true);
        creationEquipmentAlert.addConfirmButton("Créer");
        creationEquipmentAlert.addCancelButton("Annuler");
        creationEquipmentAlert.setAcceptEventListener(new CustomAlertDialog.OnAcceptEventListener() {
            @Override
            public void onEvent() {
                Boolean equiped = false;
                if (slot.equalsIgnoreCase("other_slot")) {
                    equiped = true;
                }
                String name = ((EditText) creationView.findViewById(R.id.name_equipment_creation)).getText().toString();
                String value = ((EditText) creationView.findViewById(R.id.value_equipment_creation)).getText().toString() + " po";
                String descr = ((EditText) creationView.findViewById(R.id.descr_equipment_creation)).getText().toString();
                Equipment equi = new Equipment(name, descr, value, "equipment_" + slot + "_def", slot, equiped);
                aquene.getInventory().getAllEquipments().createEquipment(equi);
                if(mListener!=null){mListener.onEvent();}
                tools.customToast(mC, equi.getName() + " ajouté !");
            }
        });
        creationEquipmentAlert.showAlert();
        final EditText editName = ((EditText) creationView.findViewById(R.id.name_equipment_creation));
        editName.post(new Runnable() {
            public void run() {
                editName.setFocusableInTouchMode(true);
                editName.requestFocusFromTouch();
                InputMethodManager lManager = (InputMethodManager) mA.getSystemService(Context.INPUT_METHOD_SERVICE);
                lManager.showSoftInput(editName, InputMethodManager.SHOW_IMPLICIT);
            }
        });
    }

    public interface OnRefreshEventListener {
        void onEvent();
    }

    public void setRefreshEventListener(OnRefreshEventListener eventListener) {
        mListener = eventListener;
    }
}
