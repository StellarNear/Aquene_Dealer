package stellarnear.aquene_dealer.Perso;

import android.content.Context;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by jchatron on 26/12/2017.
 */

public class Attacks {
    private Context mC;
    private String combatMode;

    public Attacks(Context mC) {
        this.mC = mC;

    }

    public void setCombatMode(String combatMode) {
        this.combatMode = combatMode;
    }

    public String getCombatMode() {
        return combatMode;
    }
}