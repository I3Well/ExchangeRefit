package data.plugins;

import com.fs.starfarer.api.Global;
import com.fs.starfarer.api.combat.ShipVariantAPI;

public class X1TerminalRouteAiguillage {

    public static void aiguillerBoutonAction(String opt, X1TerminalMenu menu) {
        if (opt.equals("LEAVE")) { menu.dialog.setPlugin(menu.originalPlugin); if (menu.originalPlugin != null) menu.originalPlugin.init(menu.dialog); return; }
        if (opt.equals("BACK_SHIPS")) { X1TerminalActions.chargerFlotteBrute(menu); X1TerminalMenusDisplay.afficherMenuVaisseaux(menu); return; }
        if (opt.equals("BACK_SLOTS")) { X1TerminalMenusDisplay.afficherMenuSlots(menu); return; }
        if (opt.equals("BACK_ACTIONS")) { X1TerminalMenusDisplay.afficherMenuActions(menu); return; }
        if (opt.equals("ACTION_GOTO_FILTERS")) { X1TerminalMenusDisplay.afficherMenuFiltres(menu); return; }
        if (opt.equals("NEXT_PAGE_SLOTS")) { menu.pageSlotsCourante = 2; X1TerminalMenusDisplay.afficherMenuSlots(menu); return; }
        if (opt.equals("PREV_PAGE_SLOTS")) { menu.pageSlotsCourante = 1; X1TerminalMenusDisplay.afficherMenuSlots(menu); return; }
        
        if (opt.equals("TOGGLE_WEAPONS")) { menu.filterWeapons = !menu.filterWeapons; X1TerminalMenusDisplay.afficherMenuFiltres(menu); return; }
        if (opt.equals("TOGGLE_HULLMODS")) { menu.filterHullmods = !menu.filterHullmods; X1TerminalMenusDisplay.afficherMenuFiltres(menu); return; }
        if (opt.equals("TOGGLE_SMODS")) { menu.filterSMods = !menu.filterSMods; X1TerminalMenusDisplay.afficherMenuFiltres(menu); return; }
        if (opt.equals("TOGGLE_TAGS")) { menu.filterTags = !menu.filterTags; X1TerminalMenusDisplay.afficherMenuFiltres(menu); return; }
        if (opt.equals("TOGGLE_FIGHTERS")) { menu.filterFighters = !menu.filterFighters; X1TerminalMenusDisplay.afficherMenuFiltres(menu); return; }
        if (opt.equals("TOGGLE_GROUPS")) { menu.filterGroups = !menu.filterGroups; X1TerminalMenusDisplay.afficherMenuFiltres(menu); return; }
        if (opt.equals("TOGGLE_OFFICER")) { menu.filterOfficer = !menu.filterOfficer; X1TerminalMenusDisplay.afficherMenuFiltres(menu); return; }
        
        // INTERCEPTION ET STABILISATION SYSTÉMATIQUE DU VAISSEAU AVANT LE RETRAIT DES S-MODS
        if (opt.equals("ACTION_STRIP_SMODS_CONFIRM")) {
            if (menu.vaisseauSelectionne != null && menu.vaisseauSelectionne.getVariant() != null) {
                try {
                    ShipVariantAPI variant = menu.vaisseauSelectionne.getVariant();
                    // On force Starsector à enregistrer et lier l'état actuel de la coque aux données de flotte.
                    // Cela nettoie les états "fantômes" créés si le joueur vient de poser des S-Mods à l'instant.
                    menu.vaisseauSelectionne.setVariant(variant, true, true);
                } catch (Exception e) {
                    // Sécurité en cas d'erreur de conversion interne
                }
            }
        }

        X1TerminalRouteFinAiguillage.aiguillerMecaniquesBoutons(opt, menu);
    }
}
