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

        try {
            ShipVariantAPI variant = member.getVariant();
            List permanentMods = new ArrayList(variant.getPermaMods());
            
            // 1. Première vérification : Est-ce que le vaisseau a des S-Mods ?
            int countSMods = 0;
            for (int i = 0; i < permanentMods.size(); i++) {
                if (variant.getSMods().contains((String) permanentMods.get(i))) {
                    countSMods++;
                }
            }

            if (countSMods == 0) {
                textPanel.addParagraph("This vessel has no built-in S-Mods to process.", java.awt.Color.YELLOW);
                return;
            }

            // 2. Si c'est un Downgrade (Option 2), vérifier si le vaisseau a assez d'OP libres
            if (!wipeTotal) {
                int totalOpCostRequired = 0;
                for (int i = 0; i < permanentMods.size(); i++) {
                    String modId = (String) permanentMods.get(i);
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

            // 3. Exécution de la modification et collecte des métadonnées des modules retirés
            int countRefunded = 0;
            ArrayList détailsModulesRetirés = new ArrayList();

            for (int i = 0; i < permanentMods.size(); i++) {
                String modId = (String) permanentMods.get(i);
                
                if (variant.getSMods().contains(modId)) {
                    // Extraction des métadonnées du S-Mod avant retrait
                    HullModSpecAPI spec = Global.getSettings().getHullModSpec(modId);
                    if (spec != null) {
                        String nom = spec.getDisplayName();
                        String origine = X1ModChecker.getNomModOrigine(modId, true);
                        int coutOP = X1SpecExtractor.getHullmodOpCost(modId, member);
                        
                        // Création de la ligne de texte descriptive
                        détailsModulesRetirés.add("   • " + nom + " [" + origine + "] (Cost: " + coutOP + " OP)");
                    }

                    if (wipeTotal) {
                        // Option 1 : Wipe Total
                        variant.removePermaMod(modId);
                        variant.removeMod(modId);
                    } else {
                        // Option 2 : Downgrade sécurisé
                        variant.removePermaMod(modId);
                        if (!variant.getHullMods().contains(modId)) {
                            variant.addMod(modId);
                        }
                    }
                    countRefunded++;
                }
            }

            // 4. Affichage du compte-rendu aminci et propre
            if (countRefunded > 0) {
                if (Global.getSector() != null && Global.getSector().getPlayerPerson() != null) {
                    Global.getSector().getPlayerPerson().getStats().addStoryPoints(countRefunded);
                }

                member.updateStats();
                
                // Titre de validation principale
                if (wipeTotal) {
                    textPanel.addParagraph("Wipe Complete: " + countRefunded + " S-Mods removed entirely. Story points fully refunded.", java.awt.Color.GREEN);
                } else {
                    textPanel.addParagraph("Conversion Complete: " + countRefunded + " S-Mods downgraded to standard Hullmods. Story points fully refunded.", java.awt.Color.GREEN);
                }

                // Impression de la liste des s-mods affectés avec leurs détails
                textPanel.addParagraph("Processed S-Mods details:", java.awt.Color.CYAN);
                for (int i = 0; i < détailsModulesRetirés.size(); i++) {
                    textPanel.addParagraph((String) détailsModulesRetirés.get(i), java.awt.Color.WHITE);
                }
                textPanel.addParagraph(""); // Saut de ligne d'aération
            }

        } catch (Exception e) {
            textPanel.addParagraph("Error executing operation: " + e.getMessage(), java.awt.Color.RED);
            e.printStackTrace();
        }
    }
}
