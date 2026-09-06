package data.plugins;

import com.fs.starfarer.api.Global;

public class X1TerminalRouteFinAiguillage {

    // Variable statique globale pour stocker l'état du bouton 8 et contourner la limitation de X1TerminalMenu
    public static boolean filterUpgradesStat = true;

    public static void aiguillerMecaniquesBoutons(String opt, X1TerminalMenu menu) {
        if (opt.equals("ACTION_STRIP_SMODS_CONFIRM")) {
            X1TerminalConfirmations.afficherConfirmationWipeSMods(menu);
            return;
        }
        
        if (opt.equals("ACTION_STRIP_WIPE_TOTAL")) { 
            X1SModStripper.nettoyerVaisseauSMods(menu.vaisseauSelectionne, menu.textPanel, true); 
            X1TerminalMenusDisplay.afficherMenuActions(menu); 
            return; 
        }
        if (opt.equals("ACTION_STRIP_CONVERT_HULLMOD")) { 
            X1SModStripper.nettoyerVaisseauSMods(menu.vaisseauSelectionne, menu.textPanel, false); 
            X1TerminalMenusDisplay.afficherMenuActions(menu); 
            return; 
        }
        
        if (opt.equals("ACTION_PASTE_DEFAULT")) {
            X1TerminalConfirmations.afficherPreviewPasteDefault(menu);
            return;
        }
        
        // CORRECTION ICI : Inversion de notre variable statique locale
        if (opt.equals("TOGGLE_UPGRADES")) {
            filterUpgradesStat = !filterUpgradesStat;
            X1TerminalMenusDisplay.afficherMenuFiltres(menu);
            return;
        }
        
        if (opt.equals("ACTION_GOTO_FILTERS") || opt.equals("TOGGLE_WEAPONS") || opt.equals("TOGGLE_HULLMODS") || opt.equals("TOGGLE_SMODS") || opt.equals("TOGGLE_TAGS") || opt.equals("TOGGLE_FIGHTERS") || opt.equals("TOGGLE_GROUPS") || opt.equals("TOGGLE_OFFICER")) {
            X1TerminalMenusDisplay.afficherMenuFiltres(menu);
            return;
        }

        if (opt.equals("EXECUTE_FILTERED_PASTE")) {
            X1TerminalConfirmations.afficherPreviewPasteFiltered(menu);
            return;
        }

        if (opt.equals("CONFIRM_PASTE_DEFAULT")) {
            boolean bays = menu.vaisseauSelectionne.getVariant().getHullSpec().getFighterBays() > 0;
            X1MecaniqueUtil.importerGabaritFiltre(menu.vaisseauSelectionne, menu.slotChoisi, menu.textPanel, true, true, true, true, bays, true, true, true);
            X1TerminalMenusDisplay.afficherMenuActions(menu);
            return;
        }
        if (opt.equals("CONFIRM_PASTE_FILTERED")) {
            // Utilisation de la variable statique au lieu du champ manquant du menu
            X1MecaniqueUtil.importerGabaritFiltre(menu.vaisseauSelectionne, menu.slotChoisi, menu.textPanel, menu.filterWeapons, menu.filterHullmods, menu.filterSMods, menu.filterTags, menu.filterFighters, menu.filterGroups, menu.filterOfficer, filterUpgradesStat);
            X1TerminalMenusDisplay.afficherMenuActions(menu);
            return;
        }

        if (opt.equals("ACTION_COPY")) {
            if (X1MecaniqueUtil.gabaritExiste(menu.vaisseauSelectionne, menu.slotChoisi)) {
                menu.options.clearOptions(); menu.textPanel.addParagraph("[WARNING] Overwrite layout in Slot " + menu.slotChoisi + "?", java.awt.Color.YELLOW);
                menu.options.addOption("Yes, overwrite", "CONFIRM_OVERWRITE"); menu.options.addOption("No, cancel [Esc]", "BACK_ACTIONS");
            } else { X1MecaniqueUtil.exporterGabarit(menu.vaisseauSelectionne, menu.slotChoisi, menu.textPanel); X1TerminalMenusDisplay.afficherMenuActions(menu); }
            return;
        }
        if (opt.equals("CONFIRM_OVERWRITE")) { X1MecaniqueUtil.exporterGabarit(menu.vaisseauSelectionne, menu.slotChoisi, menu.textPanel); X1TerminalMenusDisplay.afficherMenuActions(menu); return; }
        if (opt.equals("ACTION_DELETE")) { X1MecaniqueUtil.executerSuppression(menu.vaisseauSelectionne, menu.slotChoisi, menu.textPanel); X1TerminalMenusDisplay.afficherMenuSlots(menu); return; }
    }
}
