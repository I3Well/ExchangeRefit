package data.plugins;

import com.fs.starfarer.api.Global;
import com.fs.starfarer.api.campaign.TextPanelAPI;
import com.fs.starfarer.api.fleet.FleetMemberAPI;
import org.lwjgl.input.Keyboard;
import java.awt.Color;

public class X1TerminalMenusDisplay {

    public static void afficherMenuVaisseaux(X1TerminalMenu menu) {
        menu.options.clearOptions();
        menu.textPanel.addParagraph("Select a fleet hull to manage layouts:");
        for (int i = 0; i < menu.flotteFiltree.size(); i++) {
            FleetMemberAPI m = (FleetMemberAPI) menu.flotteFiltree.get(i);
            String label = m.getShipName() + " (" + m.getHullSpec().getHullName() + ")";
            if (m.isFlagship()) label += " [FLAGSHIP]";
            menu.options.addOption(label, "SHIP_" + i);
        }
        menu.options.addOption("<- Leave Terminal [Esc]", "LEAVE");
    }

    public static void afficherMenuSlots(X1TerminalMenu menu) {
        menu.options.clearOptions();
        if (menu.vaisseauSelectionne != null) {
            X1ShipScanner.afficherDiagnosticVaisseauActuel(menu.vaisseauSelectionne, menu.textPanel);
        }
        menu.textPanel.addParagraph("Select an Archive Slot for [" + menu.vaisseauSelectionne.getShipName() + "] - Page " + menu.pageSlotsCourante + "/2:");
        int start = menu.pageSlotsCourante == 2 ? 8 : 1;
        int end = menu.pageSlotsCourante == 2 ? 13 : 7;
        for (int i = start; i <= end; i++) {
            boolean existe = X1MecaniqueUtil.gabaritExiste(menu.vaisseauSelectionne, i);
            menu.options.addOption("Slot " + i + " (" + (existe ? "Archived" : "Empty") + ")", "SLOT_" + i);
        }
        menu.options.addOption(menu.pageSlotsCourante == 1 ? "Next Page (Slots 8 - 13) ->" : "<- Previous Page (Slots 1 - 7)", menu.pageSlotsCourante == 1 ? "NEXT_PAGE_SLOTS" : "PREV_PAGE_SLOTS");
        menu.options.addOption("<- Return to fleet selection [Esc]", "BACK_SHIPS");
    }

    public static void afficherMenuActions(X1TerminalMenu menu) {
        menu.options.clearOptions();
        boolean gabaritExiste = X1MecaniqueUtil.gabaritExiste(menu.vaisseauSelectionne, menu.slotChoisi);
        if (gabaritExiste) {
            if (menu.vaisseauSelectionne != null) X1ShipScanner.afficherDiagnosticVaisseauActuel(menu.vaisseauSelectionne, menu.textPanel);
            X1PreviewUtil.générerEtAfficherRécapitulatif(menu.vaisseauSelectionne, menu.slotChoisi, menu.textPanel, menu);
        } else {
            menu.textPanel.addParagraph("--- SELECTED SLOT " + menu.slotChoisi + " IS CURRENTLY EMPTY ---", Color.GRAY);
            menu.textPanel.addParagraph(""); 
        }
        menu.textPanel.addParagraph("Select an operation for Slot " + menu.slotChoisi + ":");
        menu.options.addOption("Copy (Save current ship layout & Officer profile to file)", "ACTION_COPY");
        menu.options.addOption("[STATION WORKSHOP] Strip all S-Mods from ship & 100% Refund Story Points", "ACTION_STRIP_SMODS_CONFIRM");
        if (gabaritExiste) {
            menu.options.addOption("Paste Layout (Default - Apply all components & Officer)", "ACTION_PASTE_DEFAULT");
            menu.options.addOption("Configure Selective Paste Filters... [Custom Setup]", "ACTION_GOTO_FILTERS");
            menu.options.addOption("Delete blueprint (Permanently wipe archive)", "ACTION_DELETE");
        }
        menu.options.addOption("<- Choose another Slot", "BACK_SLOTS"); 
        menu.options.addOption("<- Return to fleet selection [Esc]", "BACK_SHIPS");
    }

    public static void afficherMenuFiltres(X1TerminalMenu menu) {
        menu.options.clearOptions(); 
        X1PreviewUtil.générerEtAfficherRécapitulatif(menu.vaisseauSelectionne, menu.slotChoisi, menu.textPanel, menu);
        menu.textPanel.addParagraph("Configure Selective Paste Components (Click to Toggle):", Color.CYAN);
        menu.options.addOption("1. Weapons Mounted: " + (menu.filterWeapons ? "[ON]" : "[OFF]"), "TOGGLE_WEAPONS");
        menu.options.addOption("2. Standard Hullmods: " + (menu.filterHullmods ? "[ON]" : "[OFF]"), "TOGGLE_HULLMODS");
        menu.options.addOption("3. Story Mods (S-Mods): " + (menu.filterSMods ? "[ON]" : "[OFF]"), "TOGGLE_SMODS");
        menu.options.addOption("4. Advanced Gunnery Control (AGC): " + (menu.filterTags ? "[ON]" : "[OFF]"), "TOGGLE_TAGS");
        if (menu.vaisseauSelectionne.getVariant().getHullSpec().getFighterBays() > 0) {
            menu.options.addOption("5. Fighter Wings: " + (menu.filterFighters ? "[ON]" : "[OFF]"), "TOGGLE_FIGHTERS");
        } else { menu.filterFighters = false; }
        menu.options.addOption("6. Weapon Group Assignments: " + (menu.filterGroups ? "[ON]" : "[OFF]"), "TOGGLE_GROUPS");
        menu.options.addOption("7. Re-assign Officer Blueprint: " + (menu.filterOfficer ? "[ON]" : "[OFF]"), "TOGGLE_OFFICER");
        
        // CORRECTION ICI : Lecture de la variable statique au lieu du champ manquant du menu
        menu.options.addOption("8. Additional Refit Upgrades: " + (X1TerminalRouteFinAiguillage.filterUpgradesStat ? "[ON]" : "[OFF]"), "TOGGLE_UPGRADES");
        
        menu.options.addOption("=> EXECUTE SELECTIVE PASTE", "EXECUTE_FILTERED_PASTE");
        menu.options.addOption("<- Back to operation menu", "BACK_ACTIONS");
    }

    public static void executerOptionRoute(String opt, X1TerminalMenu menu) {
        X1TerminalRouteAiguillage.aiguillerBoutonAction(opt, menu);
    }

    public static void traiterCadreClavier(float amount, X1TerminalMenu menu) {
        if (Keyboard.isKeyDown(Keyboard.KEY_ESCAPE)) { 
            menu.dialog.setPlugin(menu.originalPlugin); 
            if (menu.originalPlugin != null) menu.originalPlugin.init(menu.dialog); 
        }
    }
}
