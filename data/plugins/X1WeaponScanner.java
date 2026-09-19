package data.plugins;

import com.fs.starfarer.api.Global;
import com.fs.starfarer.api.campaign.TextPanelAPI;
import com.fs.starfarer.api.fleet.FleetMemberAPI;
import com.fs.starfarer.api.loading.WeaponSlotAPI;
import java.awt.Color;

public class X1WeaponScanner {

    public static void scannerEtAfficherArmes(FleetMemberAPI member, TextPanelAPI textPanel) {
        textPanel.addParagraph(X1Localisation.get("WEAPONS_LAYOUT"), Color.LIGHT_GRAY);
        
        java.util.List largeList = new java.util.ArrayList();
        java.util.List mediumList = new java.util.ArrayList();
        java.util.List smallList = new java.util.ArrayList();

        java.util.List slotsPhysiques = member.getVariant().getHullSpec().getAllWeaponSlotsCopy();
        for (int i = 0; i < slotsPhysiques.size(); i++) {
            WeaponSlotAPI slot = (WeaponSlotAPI) slotsPhysiques.get(i);
            if (slot == null || slot.isDecorative() || slot.getId().equalsIgnoreCase("SYSTEM")) continue;
            
            String slotId = slot.getId();
            String weaponId = member.getVariant().getWeaponId(slotId);
            
            if (weaponId != null && !weaponId.isEmpty()) {
                try {
                    com.fs.starfarer.api.loading.WeaponSpecAPI wSpec = Global.getSettings().getWeaponSpec(weaponId);
                    String name = wSpec.getWeaponName();
                    int cost = (int) wSpec.getOrdnancePointCost(null);
                    float range = wSpec.getMaxRange();
                    String dmgTypeStr = wSpec.getDamageType().toString();
                    String mountTypeStr = slot.getWeaponType().toString();
                    
                    String origin = "Vanilla";
                    if (wSpec.getManufacturer() != null && !wSpec.getManufacturer().isEmpty() && !wSpec.getManufacturer().equalsIgnoreCase("vanilla")) {
                        origin = wSpec.getManufacturer();
                    }

                    Color cFinal = Color.WHITE;
                    if (mountTypeStr.equalsIgnoreCase("MISSILE")) {
                        cFinal = new java.awt.Color(130, 50, 185); 
                    } else {
                        if (dmgTypeStr.equalsIgnoreCase("KINETIC")) {
                            cFinal = new java.awt.Color(245, 245, 220); 
                        } else if (dmgTypeStr.equalsIgnoreCase("HIGH_EXPLOSIVE") || dmgTypeStr.contains("EXPLOSIVE")) {
                            cFinal = new java.awt.Color(220, 90, 90); 
                        } else if (dmgTypeStr.equalsIgnoreCase("ENERGY")) {
                            cFinal = new java.awt.Color(65, 105, 225); 
                        } else if (dmgTypeStr.equalsIgnoreCase("FRAGMENTATION")) {
                            cFinal = new java.awt.Color(215, 200, 65); 
                        } else if (dmgTypeStr.equalsIgnoreCase("EMP")) {
                            cFinal = com.fs.starfarer.api.util.Misc.getButtonTextColor();
                        }
                    }

                    String line = "  * [" + slotId + "] " + name + " (" + origin + ") - Rng: " + (int)range + " | Type: " + dmgTypeStr + " [" + mountTypeStr + "] | Cost: " + cost + " OP";
                    X1ElementArme elem = new X1ElementArme(line, cFinal);
                    
                    String sizeName = slot.getSlotSize().toString().toUpperCase();
                    if (sizeName.contains("LARGE")) largeList.add(elem);
                    else if (sizeName.contains("MEDIUM")) mediumList.add(elem);
                    else smallList.add(elem);
                    
                } catch (Exception e) {}
            }
        }

        for (int i = 0; i < largeList.size(); i++) { X1ElementArme e = (X1ElementArme) largeList.get(i); if (e != null) textPanel.addParagraph(e.texte, e.couleur); }
        for (int i = 0; i < mediumList.size(); i++) { X1ElementArme e = (X1ElementArme) mediumList.get(i); if (e != null) textPanel.addParagraph(e.texte, e.couleur); }
        for (int i = 0; i < smallList.size(); i++) { X1ElementArme e = (X1ElementArme) smallList.get(i); if (e != null) textPanel.addParagraph(e.texte, e.couleur); }

        java.util.List wingsEquipped = member.getVariant().getWings();
        if (wingsEquipped != null && !wingsEquipped.isEmpty()) {
            textPanel.addParagraph("");
            textPanel.addParagraph(X1Localisation.get("FIGHTERS_LAYOUT"), Color.LIGHT_GRAY);
            for (int i = 0; i < wingsEquipped.size(); i++) {
                String wingId = (String) wingsEquipped.get(i);
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

                        textPanel.addParagraph("  * [BAY " + (i + 1) + "] " + wSpec.getWingName() + " (" + originWing + ") - Class: " + roleStr + " | Cost: " + (int)wSpec.getOpCost(null) + " OP", cWing);
                    }
                } catch(Exception e){}
            }
        }
    }
}
