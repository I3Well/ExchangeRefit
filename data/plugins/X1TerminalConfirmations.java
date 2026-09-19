package data.plugins;

import com.fs.starfarer.api.Global;
import com.fs.starfarer.api.combat.ShipVariantAPI;
import com.fs.starfarer.api.loading.HullModSpecAPI;
import java.awt.Color;
import java.util.ArrayList;
import java.util.List;

public class X1TerminalConfirmations {

    public static void afficherConfirmationWipeSMods(X1TerminalMenu menu) {
        menu.options.clearOptions(); 
        menu.textPanel.addParagraph("!!! WARNING - STRUCTURAL REFIT !!!", Color.RED);
        try {
            ShipVariantAPI variant = menu.vaisseauSelectionne.getVariant();
            List permanentMods = new ArrayList(variant.getPermaMods());
            int sModsCount = 0;
            for (int i = 0; i < permanentMods.size(); i++) {
                if (variant.getSMods().contains((String) permanentMods.get(i))) sModsCount++;
            }
            menu.textPanel.addParagraph("Select how to strip S-Mods from [" + menu.vaisseauSelectionne.getShipName() + "] (" + sModsCount + " Story Points refunded):", Color.YELLOW);
            menu.textPanel.addParagraph(""); 
            boolean aDesSMods = false;
            for (int i = 0; i < permanentMods.size(); i++) {
                String modId = (String) permanentMods.get(i);
                if (variant.getSMods().contains(modId)) {
                    if (!aDesSMods) { menu.textPanel.addParagraph("The following S-Mods will be affected:", Color.CYAN); aDesSMods = true; }
                    HullModSpecAPI spec = Global.getSettings().getHullModSpec(modId);
                    if (spec != null) {
                        menu.textPanel.addParagraph("   • " + spec.getDisplayName() + " [" + X1ModChecker.getNomModOrigine(modId, true) + "] (Cost: " + X1SpecExtractor.getHullmodOpCost(modId, menu.vaisseauSelectionne) + " OP)", Color.WHITE);
                    }
                }
            }
            if (aDesSMods) menu.textPanel.addParagraph(""); 
        } catch (Exception e) {}
        menu.options.addOption("Option 1: Complete Wipe (Remove S-Mods entirely)", "ACTION_STRIP_WIPE_TOTAL"); 
        menu.options.addOption("Option 2: Downgrade to Standard (Keep them equipped as normal Hullmods)", "ACTION_STRIP_CONVERT_HULLMOD"); 
        menu.options.addOption("<- Abort and return [Esc]", "BACK_ACTIONS");
    }

    public static void afficherPreviewPasteDefault(X1TerminalMenu menu) {
        menu.options.clearOptions();
        menu.textPanel.addParagraph("=== REFIT COMMAND PREVIEW (DEFAULT PASTE v1.4.0) ===", Color.GREEN);
        menu.textPanel.addParagraph("Review the modifications order to apply on [" + menu.vaisseauSelectionne.getShipName() + "]:", Color.WHITE);
        menu.textPanel.addParagraph(" • Mount Weapons layout: YES", Color.GREEN);
        menu.textPanel.addParagraph(" • Install Standard Hullmods: YES", Color.GREEN);
        menu.textPanel.addParagraph(" • Allocate Flux Capacitors & Vents: YES", Color.GREEN);
        menu.textPanel.addParagraph(" • Apply Story Mods (S-Mods): YES", Color.GREEN);
        menu.textPanel.addParagraph(" • Reassign Weapon Automation (AGC / AI Tweaks): YES", Color.GREEN);
        if (menu.vaisseauSelectionne.getVariant().getHullSpec().getFighterBays() > 0) menu.textPanel.addParagraph(" • Install Fighter Wings layout: YES", Color.GREEN);
        menu.textPanel.addParagraph(" • Assign Officer profile blueprint: YES", Color.GREEN);
        menu.textPanel.addParagraph(" • Apply Additional Refit Upgrades: YES", Color.GREEN);
        menu.textPanel.addParagraph("");
        menu.textPanel.addParagraph("Are you sure you want to execute this shipyard refit?", Color.YELLOW);
        menu.options.addOption("=> [Confirm and Execute Refit]", "CONFIRM_PASTE_DEFAULT");
        menu.options.addOption("<- Cancel and return [Esc]", "BACK_ACTIONS");
    }

    public static void afficherPreviewPasteFiltered(X1TerminalMenu menu) {
        menu.options.clearOptions();
        menu.textPanel.addParagraph("=== REFIT COMMAND PREVIEW (CUSTOM SETUP v1.4.0) ===", Color.GREEN);
        menu.textPanel.addParagraph("Review your selective components configuration order for [" + menu.vaisseauSelectionne.getShipName() + "]:", Color.WHITE);
        menu.textPanel.addParagraph(" • Mount Weapons layout: " + (menu.filterWeapons ? "YES" : "NO"), menu.filterWeapons ? Color.GREEN : Color.RED);
        menu.textPanel.addParagraph(" • Install Standard Hullmods: " + (menu.filterHullmods ? "YES" : "NO"), menu.filterHullmods ? Color.GREEN : Color.RED);
        menu.textPanel.addParagraph(" • Allocate Flux Capacitors & Vents: " + (menu.filterHullmods ? "YES" : "NO"), menu.filterHullmods ? Color.GREEN : Color.RED);
        menu.textPanel.addParagraph(" • Apply Story Mods (S-Mods): " + (menu.filterSMods ? "YES" : "NO"), menu.filterSMods ? Color.GREEN : Color.RED);
        menu.textPanel.addParagraph(" • Reassign Weapon Automation (AGC / AI Tweaks): " + (menu.filterTags ? "YES" : "NO"), menu.filterTags ? Color.GREEN : Color.RED);
        if (menu.vaisseauSelectionne.getVariant().getHullSpec().getFighterBays() > 0) {
            menu.textPanel.addParagraph(" • Install Fighter Wings layout: " + (menu.filterFighters ? "YES" : "NO"), menu.filterFighters ? Color.GREEN : Color.RED);
        }
        menu.textPanel.addParagraph(" • Assign Weapon Group Assignments: " + (menu.filterGroups ? "YES" : "NO"), menu.filterGroups ? Color.GREEN : Color.RED);
        menu.textPanel.addParagraph(" • Assign Officer profile blueprint: " + (menu.filterOfficer ? "YES" : "NO"), menu.filterOfficer ? Color.GREEN : Color.RED);
        
        boolean viewUpgrades = X1TerminalRouteFinAiguillage.filterUpgradesStat;
        menu.textPanel.addParagraph(" • Apply Additional Refit Upgrades: " + (viewUpgrades ? "YES" : "NO"), viewUpgrades ? Color.GREEN : Color.RED);
        
        menu.textPanel.addParagraph("");
        menu.textPanel.addParagraph("Are you sure you want to execute this selective shipyard refit?", Color.YELLOW);
        menu.options.addOption("=> [Confirm and Execute Selective Refit]", "CONFIRM_PASTE_FILTERED");
        menu.options.addOption("<- Return to Filters setup [Esc]", "ACTION_GOTO_FILTERS");
    }
}
