package data.plugins;

import com.fs.starfarer.api.campaign.OptionPanelAPI;
import com.fs.starfarer.api.campaign.TextPanelAPI;
import java.awt.Color;

public class X1FilterMenuDisplay {

    public static void afficherMenuConfigurationFiltres(X1TerminalMenu menu) {
        if (menu == null || menu.options == null) return;
        OptionPanelAPI options = menu.options;
        TextPanelAPI textPanel = menu.textPanel;
        options.clearOptions();

        textPanel.addParagraph("=== SELECTIVE REFIT COMPONENTS CONFIGURATION ===", Color.CYAN);
        textPanel.addParagraph("Toggle components to apply from the slot blueprint archive:", Color.WHITE);
        textPanel.addParagraph("");

        options.addOption("1. Mount Weapons layout: " + (menu.filterWeapons ? "[ENABLED]" : "[MUTED]"), "TOGGLE_FILTER_WEAPONS");
        options.addOption("2. Install Standard Hullmods & Flux: " + (menu.filterHullmods ? "[ENABLED]" : "[MUTED]"), "TOGGLE_FILTER_HULLMODS");
        options.addOption("3. Apply Story Modifications (S-Mods): " + (menu.filterSMods ? "[ENABLED]" : "[MUTED]"), "TOGGLE_FILTER_SMODS");
        options.addOption("4. Weapon Automation logic (AGC / AI Tweaks): " + (menu.filterTags ? "[ENABLED]" : "[MUTED]"), "TOGGLE_FILTER_TAGS");
        
        if (menu.vaisseauSelectionne != null && menu.vaisseauSelectionne.getVariant().getHullSpec().getFighterBays() > 0) {
            options.addOption("5. Install Fighter Wings layout: " + (menu.filterFighters ? "[ENABLED]" : "[MUTED]"), "TOGGLE_FILTER_FIGHTERS");
        } else {
            options.addOption("5. Install Fighter Wings layout: [NOT APPLICABLE - NO BAYS]", "TOGGLE_FILTER_FIGHTERS_DISABLED");
            options.setEnabled("TOGGLE_FILTER_FIGHTERS_DISABLED", false);
        }

        options.addOption("6. Reassign Weapon Group assignments: " + (menu.filterGroups ? "[ENABLED]" : "[MUTED]"), "TOGGLE_FILTER_GROUPS");
        options.addOption("7. Assign Officer blueprint profile: " + (menu.filterOfficer ? "[ENABLED]" : "[MUTED]"), "TOGGLE_FILTER_OFFICER");
        options.addOption("8. Additional Refit Upgrades (Prog S-Mods / Data): " + (X1TerminalRouteFinAiguillage.filterUpgradesStat ? "[ENABLED]" : "[MUTED]"), "TOGGLE_FILTER_UPGRADES");

        options.addOption("=> [Proceed to Selective Layout Confirmation]", "ACTION_GOTO_PREVIEW_FILTERED");
        options.addOption("<- Return to Slot Selection [Esc]", "MENU_GOTO_LOAD");
    }
}
