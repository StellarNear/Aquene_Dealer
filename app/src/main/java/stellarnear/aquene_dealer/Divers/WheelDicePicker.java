package stellarnear.aquene_dealer.Divers;

import android.app.Activity;
import android.content.Context;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.widget.Button;
import android.widget.ImageButton;

import com.oguzdev.circularfloatingactionmenu.library.FloatingActionMenu;

import stellarnear.aquene_dealer.Activities.MainActivity;
import stellarnear.aquene_dealer.Activities.MainActivityFragmentCombat;
import stellarnear.aquene_dealer.R;


public class WheelDicePicker extends AppCompatActivity {
    public WheelDicePicker(FloatingActionButton mainFab, Activity mA) {


        FloatingActionMenu.Builder actionMenu = new FloatingActionMenu.Builder(mA);

        ImageButton newButton = new ImageButton(mA);
        newButton.setImageResource(R.drawable.acrob);

        ImageButton newButton2 = new ImageButton(mA);
        newButton2.setImageResource(R.drawable.climb);

        actionMenu.addSubActionView(newButton);
        actionMenu.addSubActionView(newButton2);

        actionMenu.attachTo(mainFab);
        actionMenu.build();


               // doc : https://github.com/oguzbilgener/CircularFloatingActionMenu


    }

}