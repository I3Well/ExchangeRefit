package data.plugins;

import com.fs.starfarer.api.Global;
import com.fs.starfarer.api.campaign.CargoAPI;
import com.fs.starfarer.api.fleet.FleetMemberAPI;
import java.util.List;

public class X1ShortageChecker {

    public static void verifierArmeCargo(String wIdArc, String slotId, String name, FleetMemberAPI act, List l, List m, List s) {
        try {
            if (Global.getSector() == null || Global.getSector().getPlayerFleet() == null) return;
            CargoAPI cargo = Global.getSector().getPlayerFleet().getCargo();
            if (cargo == null) return;

            int qtePossedee = (int) cargo.getCommodityQuantity(wIdArc);
            
            String wIdAct = "";
            if (act.getVariant() != null && act.getVariant().getWeaponId(slotId) != null) {
                wIdAct = act.getVariant().getWeaponId(slotId).trim();
            }

            if (qtePossedee <= 0 && !wIdAct.equalsIgnoreCase(wIdArc)) {
                com.fs.starfarer.api.loading.WeaponSpecAPI spec = Global.getSettings().getWeaponSpec(wIdArc);
                int cost = (int) spec.getOrdnancePointCost(null);
                float range = spec.getMaxRange();
                String dmgType = spec.getDamageType().toString();
                
                String origin = X1ModChecker.getNomModOrigine(wIdArc, false);
                
                String line = "  + [" + slotId + "] " + name + " (" + origin + ") - Rng: " + (int)range + " | Type: " + dmgType + " | Cost: " + cost + " OP";
                java.awt.Color orangeFonce = new java.awt.Color(220, 90, 10);
                X1ElementArme elem = new X1ElementArme(line, orangeFonce);
                
                String slotSize = act.getVariant().getHullSpec().getWeaponSlotAPI(slotId).getSlotSize().toString().toUpperCase();
                if (slotSize.contains("LARGE")) l.add(elem);
                else if (slotSize.contains("MEDIUM")) m.add(elem);
                else s.add(elem);
            }
        } catch (Exception e) {}
    }

    public static void verifierHullmodsFaction(String fileName, FleetMemberAPI member, List shortages) {
        try {
            String raw = Global.getSettings().readTextFileFromCommon(fileName);
            String[] lines = raw.split("\n");
            for (int i = 0; i < lines.length; i++) {
                String line = lines[i];
                if (line == null || line.isEmpty()) continue;
                String trimmed = line.trim();

                if (trimmed.startsWith("HULLMOD:") || trimmed.startsWith("S_HULLMOD:")) {
                    boolean isSMod = trimmed.startsWith("S_HULLMOD:");
                    String mId = isSMod ? trimmed.substring(10).trim() : trimmed.substring(8).trim();
                    
                    if (Global.getSector() != null && Global.getSector().getPlayerFaction() != null) {
                        boolean connu = Global.getSector().getPlayerFaction().getKnownHullMods().contains(mId);
                        if (!connu) {
                            com.fs.starfarer.api.loading.HullModSpecAPI spec = Global.getSettings().getHullModSpec(mId);
                            String displayName = spec.getDisplayName();
                            int cost = spec.getCostFor(member.getVariant().getHullSize());
                            
                            String origin = X1ModChecker.getNomModOrigine(mId, true);
                            
                            shortages.add("  + " + displayName + " (" + origin + ") - Cost: " + cost + " OP" + (isSMod ? " [S-Mod]" : ""));
                        }
                    }
                }
            }
        } catch(Exception e) {}
    }
}
