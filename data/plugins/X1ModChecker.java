package data.plugins;

import com.fs.starfarer.api.Global;
import com.fs.starfarer.api.ModSpecAPI;
import com.fs.starfarer.api.loading.WeaponSpecAPI;
import com.fs.starfarer.api.loading.HullModSpecAPI;

public class X1ModChecker {

    public static String getNomModOrigine(String specId, boolean isHullmod) {
        if (specId == null || specId.isEmpty()) return "Vanilla";

        try {
            ModSpecAPI modSpec = null;
            if (isHullmod) {
                HullModSpecAPI hSpec = Global.getSettings().getHullModSpec(specId);
                if (hSpec != null) modSpec = hSpec.getSourceMod();
            } else {
                WeaponSpecAPI wSpec = Global.getSettings().getWeaponSpec(specId);
                if (wSpec != null) modSpec = wSpec.getSourceMod();
            }

            // Si le jeu n'associe aucun mod source à cet élément, c'est obligatoirement du Vanilla !
            if (modSpec == null) {
                return "Vanilla";
            }

            String modId = modSpec.getId();
            if (modId == null || modId.isEmpty() || modId.equals("core") || modId.equals("vacant")) {
                return "Vanilla";
            }

            if (modSpec.getName() != null) {
                return modSpec.getName();
            }
            return modId;

        } catch (Exception e) {
            // Sécurité Janino : en cas de plantage sur une Spec inconnue, on bascule sur un fallback prudent
        }

        return "Vanilla";
    }
}
