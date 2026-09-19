package data.plugins;

import com.fs.starfarer.api.Global;
import com.fs.starfarer.api.fleet.FleetMemberAPI;
import com.fs.starfarer.api.loading.HullModSpecAPI;
import java.util.ArrayList;

public class X1Sorter {

    public static void trierArmesParOp(ArrayList ids) {
        int n = ids.size();
        for (int i = 0; i < n - 1; i++) {
            for (int j = 0; j < n - i - 1; j++) {
                String id1 = (String) ids.get(j);
                String id2 = (String) ids.get(j + 1);
                if (X1SpecExtractor.getArmeOpCost(id1) < X1SpecExtractor.getArmeOpCost(id2)) {
                    ids.set(j, id2);
                    ids.set(j + 1, id1);
                }
            }
        }
    }

    public static void trierHullmodsParOp(ArrayList ids, FleetMemberAPI member) {
        int n = ids.size();
        for (int i = 0; i < n - 1; i++) {
            for (int j = 0; j < n - i - 1; j++) {
                String id1 = (String) ids.get(j);
                String id2 = (String) ids.get(j + 1);
                int cost1 = 0;
                int cost2 = 0;
                
                HullModSpecAPI spec1 = Global.getSettings().getHullModSpec(id1);
                HullModSpecAPI spec2 = Global.getSettings().getHullModSpec(id2);
                
                if (spec1 != null && member != null && member.getVariant() != null) {
                    cost1 = spec1.getCostFor(member.getVariant().getHullSize());
                }
                if (spec2 != null && member != null && member.getVariant() != null) {
                    cost2 = spec2.getCostFor(member.getVariant().getHullSize());
                }
                if (cost1 < cost2) {
                    ids.set(j, id2);
                    ids.set(j + 1, id1);
                }
            }
        }
    }
}
