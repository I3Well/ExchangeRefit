package data.plugins;

import com.fs.starfarer.api.Global;
import com.fs.starfarer.api.campaign.TextPanelAPI;
import com.fs.starfarer.api.loading.WeaponSpecAPI;
import java.awt.Color;
import java.util.List;

public class X1PreviewPrinter {

    public static Color obtenirCouleurTypeDegats(String weaponId, String typeDegats) {
        // Détection pour forcer la couleur Violet sombre sur l'emplacement ou l'arme MISSILE
        if (weaponId != null) {
            try {
                WeaponSpecAPI spec = Global.getSettings().getWeaponSpec(weaponId.trim());
                if (spec != null && spec.getMountType() == com.fs.starfarer.api.loading.WeaponSlotAPI.WeaponType.MISSILE) {
                    return new java.awt.Color(130, 50, 185); // Violet sombre personnalisé
                }
            } catch (Exception e) {}
        }

        if (typeDegats == null) return Color.WHITE;
        String dmgType = typeDegats.toUpperCase().trim();
        
        Color couleurLigne = Color.WHITE;

        if (dmgType.equals("HIGH_EXPLOSIVE")) {
            couleurLigne = new java.awt.Color(220, 90, 90); 
        } else if (dmgType.equals("KINETIC")) {
            couleurLigne = new java.awt.Color(245, 245, 220); 
        } else if (dmgType.equals("ENERGY")) {
            couleurLigne = new java.awt.Color(65, 105, 225); 
        } else if (dmgType.equals("FRAGMENTATION")) {
            couleurLigne = new java.awt.Color(215, 200, 65); 
        }
        
        return couleurLigne;
    }

    public static void imprimerComposantsColorés(TextPanelAPI text, List elements) {
        if (text == null || elements == null) return;
        for (int i = 0; i < elements.size(); i++) {
            X1ElementArme el = (X1ElementArme) elements.get(i);
            if (el != null && el.texte != null) {
                text.addParagraph(el.texte, el.couleur != null ? el.couleur : Color.WHITE);
            }
        }
    }
}
