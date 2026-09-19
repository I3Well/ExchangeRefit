package data.plugins;

import com.fs.starfarer.api.Global;
import com.fs.starfarer.api.campaign.TextPanelAPI;
import com.fs.starfarer.api.fleet.FleetMemberAPI;
import com.fs.starfarer.api.loading.WeaponSlotAPI;
import java.awt.Color;

public class X1WeaponPreview {

    public static void traiterArmesEtChasseursArchive(FleetMemberAPI actuel, X1ArchiveDecoder.StructureArchive data, TextPanelAPI panel, java.util.List shortLarge, java.util.List shortMedium, java.util.List shortSmall) {
        java.util.List largeList = new java.util.ArrayList();
        java.util.List mediumList = new java.util.ArrayList();
        java.util.List smallList = new java.util.ArrayList();

        java.util.List slotsPhysiques = actuel.getVariant().getHullSpec().getAllWeaponSlotsCopy();
        for (int i = 0; i < slotsPhysiques.size(); i++) {
            WeaponSlotAPI slot = (WeaponSlotAPI) slotsPhysiques.get(i);
            if (slot == null || slot.isDecorative() || slot.getId().equalsIgnoreCase("SYSTEM")) continue;
            
            String slotId = slot.getId();
            String wIdArc = (String) data.armes.get(slotId);
            if (wIdArc == null || wIdArc.isEmpty()) continue;

            try {
                com.fs.starfarer.api.loading.WeaponSpecAPI spec = Global.getSettings().getWeaponSpec(wIdArc);
                String name = spec.getWeaponName();
                String dmgTypeStr = spec.getDamageType().toString();
                String mountTypeStr = slot.getWeaponType().toString();
                String origin = X1ModChecker.getNomModOrigine(wIdArc, false);

                Color cFinal = Color.WHITE;
                if (mountTypeStr.equalsIgnoreCase("MISSILE")) {
                    cFinal = new java.awt.Color(130, 50, 185); 
                } else if (dmgTypeStr.equalsIgnoreCase("HIGH_EXPLOSIVE") || dmgTypeStr.contains("EXPLOSIVE")) {
                    cFinal = new java.awt.Color(220, 90, 90); 
                } else if (dmgTypeStr.equalsIgnoreCase("KINETIC")) {
                    cFinal = new java.awt.Color(245, 245, 220); 
                } else if (dmgTypeStr.equalsIgnoreCase("ENERGY")) {
                    cFinal = new java.awt.Color(65, 105, 225); 
                } else if (dmgTypeStr.equalsIgnoreCase("FRAGMENTATION")) {
                    cFinal = new java.awt.Color(215, 200, 65); 
                } else if (dmgTypeStr.equalsIgnoreCase("EMP")) {
                    cFinal = com.fs.starfarer.api.util.Misc.getButtonTextColor();
                }

                String line = "  * [" + slotId + "] " + name + " (" + origin + ") - Rng: " + (int)spec.getMaxRange() + " | Type: " + dmgTypeStr + " [" + mountTypeStr + "] | Cost: " + (int)spec.getOrdnancePointCost(null) + " OP";
                X1ElementArme elem = new X1ElementArme(line, cFinal);
                
                String sizeName = slot.getSlotSize().toString().toUpperCase();
                if (sizeName.contains("LARGE")) largeList.add(elem);
                else if (sizeName.contains("MEDIUM")) mediumList.add(elem);
                else smallList.add(elem);

                X1ShortageChecker.verifierArmeCargo(wIdArc, slotId, name, actuel, shortLarge, shortMedium, shortSmall);
            } catch(Exception e){}
        }

        panel.addParagraph(X1Localisation.get("WEAPONS_LAYOUT"), Color.LIGHT_GRAY);
        for (int i = 0; i < largeList.size(); i++) { X1ElementArme e = (X1ElementArme) largeList.get(i); if (e != null) panel.addParagraph(e.texte, e.couleur); }
        for (int i = 0; i < mediumList.size(); i++) { X1ElementArme e = (X1ElementArme) mediumList.get(i); if (e != null) panel.addParagraph(e.texte, e.couleur); }
        for (int i = 0; i < smallList.size(); i++) { X1ElementArme e = (X1ElementArme) smallList.get(i); if (e != null) panel.addParagraph(e.texte, e.couleur); }

        java.util.List deLaCoque = actuel.getVariant().getWings();
        if (deLaCoque != null && !deLaCoque.isEmpty()) {
            panel.addParagraph("");
            panel.addParagraph(X1Localisation.get("FIGHTERS_LAYOUT"), Color.LIGHT_GRAY);
            for (int i = 0; i < deLaCoque.size(); i++) {
                String wingId = (String) deLaCoque.get(i);
                if (wingId == null || wingId.isEmpty()) continue;
                try {
                    com.fs.starfarer.api.loading.FighterWingSpecAPI wSpec = Global.getSettings().getFighterWingSpec(wingId);
                    if (wSpec != null) {
                        String originWing = "Vanilla";
                        if (wSpec.getVariant() != null && wSpec.getVariant().getHullSpec() != null) {
                            String man = wSpec.getVariant().getHullSpec().getManufacturer();
                            if (man != null && !man.isEmpty() && !man.equalsIgnoreCase("vanilla")) {
                                originWing = man;
                            }
                        }
                        
                        String roleStr = wSpec.getRole() != null ? wSpec.getRole().toString().toUpperCase() : "FIGHTER";
                        
                        Color cWing = Color.WHITE;
                        if (roleStr.contains("INTERCEPTOR")) {
                            cWing = new java.awt.Color(110, 195, 205); 
                        } else if (roleStr.contains("BOMBER")) {
                            cWing = new java.awt.Color(210, 140, 140); 
                        } else if (roleStr.contains("SUPPORT") || roleStr.contains("UTILITY")) {
                            cWing = new java.awt.Color(135, 170, 125); 
                        } else if (roleStr.contains("FIGHTER")) {
                            cWing = new java.awt.Color(140, 150, 155); 
                        }

                        panel.addParagraph("  * [BAY " + (i + 1) + "] " + wSpec.getWingName() + " (" + originWing + ") - Class: " + roleStr + " | Cost: " + (int)wSpec.getOpCost(null) + " OP", cWing);
                    }
                } catch(Exception e){}
            }
        }
        panel.addParagraph("");
    }
}
