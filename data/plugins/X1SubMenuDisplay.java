package data.plugins;

import com.fs.starfarer.api.campaign.OptionPanelAPI;
import com.fs.starfarer.api.campaign.TextPanelAPI;
import java.awt.Color;

public class X1SubMenuDisplay {

    public static void dessinerPageSecondaire(X1TerminalMenu menu) {
        OptionPanelAPI options = menu.options;
        TextPanelAPI textPanel = menu.textPanel;

        if (menu.pageActuelle == 4) {
            boolean fr = X1Localisation.estEnFrancais();
            textPanel.addParagraph(X1Localisation.get("SMOD_STRIP_TITLE"), Color.CYAN);
            textPanel.addParagraph(X1Localisation.get("SMOD_STRIP_DESC"), Color.WHITE);
            
            options.addOption(fr ? "1. Nettoyage Complet: Retirer totalement les S-Mods (Remboursement 100%)" : "1. Complete Wipe: Strip player S-Mods entirely (100% Points Refunded)", "ACTION_STRIP_WIPE_TOTAL");
            options.addOption(fr ? "2. Déclassement Conversion: Convertir les S-Mods en Hullmods Standards" : "2. Downgrade Conversion: Convert S-Mods to Standard Hullmods", "ACTION_STRIP_CONVERT_HULLMOD");
            options.addOption(fr ? "<- Retourner à l'Espace de Travail des Slots [Esc]" : "<- Return to Slot Workspace [Esc]", "PAGE4_BACK_TO_WORKSPACE");
            return;
        }

        if (menu.pageActuelle == 5) {
            actualiserFiltres(menu);
        }
    }

    public static void actualiserFiltres(X1TerminalMenu menu) {
        OptionPanelAPI options = menu.options;
        TextPanelAPI textPanel = menu.textPanel;
        options.clearOptions();

        boolean fr = X1Localisation.estEnFrancais();
        textPanel.addParagraph(X1Localisation.get("FILTER_TITLE"), Color.CYAN);
        textPanel.addParagraph(X1Localisation.get("FILTER_DESC"), Color.WHITE);
        
        String on = X1Localisation.get("STATE_ON");
        String off = X1Localisation.get("STATE_OFF");

        options.addOption(X1Localisation.get("TOGGLE_WEAPONS") + ": " + (menu.filterWeapons ? on : off), "TOGGLE_FILTER_WEAPONS");
        options.addOption(X1Localisation.get("TOGGLE_HULLMODS") + ": " + (menu.filterHullmods ? on : off), "TOGGLE_FILTER_HULLMODS");
        options.addOption(X1Localisation.get("TOGGLE_SMODS") + ": " + (menu.filterSMods ? on : off), "TOGGLE_FILTER_SMODS");
        
        String automatedDesc = fr ? "Filtre: Logique d'Automatisation des Armes (AGC / IA)" : "Filter: Weapon Automation logic (AGC / AI Tweaks)";
        options.addOption(automatedDesc + ": " + (menu.filterTags ? on : off), "TOGGLE_FILTER_TAGS");
        
        if (menu.vaisseauSelectionne.getVariant().getHullSpec().getFighterBays() > 0) {
            options.addOption(X1Localisation.get("TOGGLE_FIGHTERS") + ": " + (menu.filterFighters ? on : off), "TOGGLE_FILTER_FIGHTERS");
        } else {
            options.addOption(fr ? "Filter: Escadrons de Chasseurs [NON APPLICABLE]" : "Filter: Fighter Wings layout: [NOT APPLICABLE]", "TOGGLE_FILTER_FIGHTERS_DISABLED");
            options.setEnabled("TOGGLE_FILTER_FIGHTERS_DISABLED", false);
        }
        
        options.addOption(X1Localisation.get("TOGGLE_GROUPS") + ": " + (menu.filterGroups ? on : off), "TOGGLE_FILTER_GROUPS");
        options.addOption(X1Localisation.get("TOGGLE_OFFICER") + ": " + (menu.filterOfficer ? on : off), "TOGGLE_FILTER_OFFICER");
        
        // CORRECTION DE SÉCURITÉ : Remplacement définitif du mot par Modifications
        String upgradesDesc = fr ? "Filtre: Améliorations de Modifications Additionnelles" : "Filter: Additional Refit Upgrades";
        options.addOption(upgradesDesc + ": " + (X1TerminalRouteFinAiguillage.filterUpgradesStat ? on : off), "TOGGLE_FILTER_UPGRADES");
        
        options.addOption(fr ? "=> [Procéder à la Confirmation du Plan Filtré]" : "=> [Proceed to Selective Layout Confirmation]", "ACTION_GOTO_PREVIEW_FILTERED");
        options.addOption(fr ? "<- Retourner à l'Espace de Travail des Slots [Esc]" : "<- Return to Slot Workspace [Esc]", "PAGE5_BACK_TO_WORKSPACE");
    }
}
