package data.plugins;

import com.fs.starfarer.api.Global;
import com.fs.starfarer.api.campaign.CargoAPI;
import com.fs.starfarer.api.loading.WeaponSpecAPI;
import com.fs.starfarer.api.loading.FighterWingSpecAPI;
import java.util.ArrayList;

public class X1SouteChecker {

    public static void verifierArmeManquante(String wId, String slotTarget, ArrayList alertes) {
        try {
            CargoAPI soute = Global.getSector().getPlayerFleet().getCargo();
            if (soute.getNumWeapons(wId) <= 0) {
                WeaponSpecAPI spec = Global.getSettings().getWeaponSpec(wId);
                if (spec != null) {
                    String a = "   • " + spec.getWeaponName() + " [" + spec.getSize().name() + " | " + spec.getType().name() + "]\n" +
                               "     [Stats: Range: " + (int)spec.getMaxRange() + " su] | Source: " + X1ModChecker.getNomModOrigine(wId, true) + "\n" +
                               "     Target: Slot " + slotTarget + " -> Will leave slot vacant!";
                    if (!alertes.contains(a)) alertes.add(a);
                }
            }
        } catch (Exception e) {}
    }

    public static void verifierChasseurManquant(String wingId, int bayIdx, ArrayList alertes) {
        try {
            CargoAPI soute = Global.getSector().getPlayerFleet().getCargo();
            if (soute.getNumFighters(wingId) <= 0) {
                FighterWingSpecAPI spec = Global.getSettings().getFighterWingSpec(wingId);
                if (spec != null) {
                    String a = "   • [WING] " + spec.getWingName() + " (Cost: " + (int)spec.getOpCost(null) + " OP) | Source: " + X1ModChecker.getNomModOrigine(wingId, true) + "\n" +
                               "     Target: Fighter Bay #" + (bayIdx + 1) + " -> Will leave bay empty!";
                    if (!alertes.contains(a)) alertes.add(a);
                }
            }
        } catch (Exception e) {}
    }
}
