package data.plugins;

import com.fs.starfarer.api.Global;
import com.fs.starfarer.api.campaign.TextPanelAPI;
import com.fs.starfarer.api.combat.ShipVariantAPI;
import com.fs.starfarer.api.fleet.FleetMemberAPI;
import com.fs.starfarer.api.loading.HullModSpecAPI;
import com.fs.starfarer.api.characters.MutableCharacterStatsAPI;
import java.util.ArrayList;
import java.util.List;

public class X1SModStripper {

    public static void nettoyerVaisseauSMods(FleetMemberAPI member, TextPanelAPI textPanel, boolean wipeTotal) {
        if (member == null || member.getVariant() == null) {
            textPanel.addParagraph("Error: Ship data is null.", java.awt.Color.RED);
            return;
        }

        // Interrogation sécurisée et découplée de la mémoire sectorielle
        boolean enableSmodHandling = true;
        if (Global.getSector() != null && Global.getSector().getMemoryWithoutUpdate() != null) {
            if (Global.getSector().getMemoryWithoutUpdate().contains("$exchangerefit_hasProgSMods")) {
                enableSmodHandling = !Global.getSector().getMemoryWithoutUpdate().getBoolean("$exchangerefit_hasProgSMods");
            }
        }

        // Blocage si Progressive S-Mods gère la coque
        if (!enableSmodHandling) {
            textPanel.addParagraph("!!! OPERATION DENIED !!!", java.awt.Color.RED);
            textPanel.addParagraph("S-Mod stripping via this terminal is disabled because Progressive S-Mods is managing this fleet's hull progression. Please use their interface.", java.awt.Color.YELLOW);
            return;
        }

        try {
            ShipVariantAPI variant = member.getVariant();
            List permanentMods = new ArrayList(variant.getPermaMods());
            List builtInMods = member.getHullSpec().getBuiltInMods();
            
            // 1. Décompte des S-Mods posés par le joueur (anti-triche coques natives)
            int countSMods = 0;
            for (int i = 0; i < permanentMods.size(); i++) {
                String modId = (String) permanentMods.get(i);
                if (builtInMods != null && builtInMods.contains(modId)) continue;
                
                if (variant.getSMods().contains(modId)) {
                    countSMods++;
                }
            }

            if (countSMods == 0) {
                textPanel.addParagraph("This vessel has no player-installed S-Mods to process (Native/Built-In chassis mods cannot be stripped).", java.awt.Color.YELLOW);
                return;
            }

            // 2. Vérification de la réserve d'Ordnance Points pour le Downgrade (Option 2)
            if (!wipeTotal) {
                int totalOpCostRequired = 0;
                for (int i = 0; i < permanentMods.size(); i++) {
                    String modId = (String) permanentMods.get(i);
                    if (builtInMods != null && builtInMods.contains(modId)) continue;
                    
                    if (variant.getSMods().contains(modId)) {
                        totalOpCostRequired += X1SpecExtractor.getHullmodOpCost(modId, member);
                    }
                }

                MutableCharacterStatsAPI charStats = null;
                if (Global.getSector() != null && Global.getSector().getPlayerPerson() != null) {
                    charStats = Global.getSector().getPlayerPerson().getStats();
                }

                int currentUnusedOP = variant.getUnusedOP(charStats);

                if (currentUnusedOP < totalOpCostRequired) {
                    int manquant = totalOpCostRequired - currentUnusedOP;
                    textPanel.addParagraph("!!! OPERATION ABORTED: INSUFFICIENT ORDNANCE POINTS !!!", java.awt.Color.RED);
                    textPanel.addParagraph("Cannot downgrade S-Mods to Standard Hullmods.", java.awt.Color.WHITE);
                    textPanel.addParagraph(" * OP required for conversion: " + totalOpCostRequired + " OP", java.awt.Color.YELLOW);
                    textPanel.addParagraph(" * Current available OP free: " + currentUnusedOP + " OP", java.awt.Color.YELLOW);
                    textPanel.addParagraph(" * Please free up " + manquant + " OP on your ship configuration first.", java.awt.Color.CYAN);
                    return; 
                }
            }

            // 3. Traitement
            int countRefunded = 0;
            ArrayList détailsModulesRetirés = new ArrayList();

            for (int i = 0; i < permanentMods.size(); i++) {
                String modId = (String) permanentMods.get(i);
                if (builtInMods != null && builtInMods.contains(modId)) continue;
                
                if (variant.getSMods().contains(modId)) {
                    HullModSpecAPI spec = Global.getSettings().getHullModSpec(modId);
                    if (spec != null) {
                        String nom = spec.getDisplayName();
                        String origine = X1ModChecker.getNomModOrigine(modId, true);
                        int coutOP = X1SpecExtractor.getHullmodOpCost(modId, member);
                        
                        détailsModulesRetirés.add("   • " + nom + " [" + origine + "] (Cost: " + coutOP + " OP)");
                    }

                    if (wipeTotal) {
                        variant.removePermaMod(modId);
                        variant.removeMod(modId);
                    } else {
                        variant.removePermaMod(modId);
                        if (!variant.getHullMods().contains(modId)) {
                            variant.addMod(modId);
                        }
                    }
                    countRefunded++;
                }
            }

            // 4. Remboursement et rafraîchissement d'état
            if (countRefunded > 0) {
                if (Global.getSector() != null && Global.getSector().getPlayerPerson() != null) {
                    Global.getSector().getPlayerPerson().getStats().addStoryPoints(countRefunded);
                }

                // APPLICATION DE LA SEULE MÉTHODE OFFICIELLE DE STARSECTOR POUR RECALCULER LES STATS
                member.updateStats(); // Force la recalculation brute de la coque et des OP libérés
                
                if (wipeTotal) {
                    textPanel.addParagraph("Wipe Complete: " + countRefunded + " S-Mods removed entirely. Story points fully refunded.", java.awt.Color.GREEN);
                } else {
                    textPanel.addParagraph("Conversion Complete: " + countRefunded + " S-Mods downgraded to standard Hullmods. Story points fully refunded.", java.awt.Color.GREEN);
                }

                textPanel.addParagraph("Processed S-Mods details:", java.awt.Color.CYAN);
                for (int i = 0; i < détailsModulesRetirés.size(); i++) {
                    textPanel.addParagraph((String) détailsModulesRetirés.get(i), java.awt.Color.WHITE);
                }
                textPanel.addParagraph(""); 
            }

        } catch (Exception e) {
            textPanel.addParagraph("Error executing operation: " + e.getMessage(), java.awt.Color.RED);
            e.printStackTrace();
        }
    }
}
