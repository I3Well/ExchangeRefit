package data.plugins;

import com.fs.starfarer.api.fleet.FleetMemberAPI;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class X1VaisseauTri implements Comparator {
    
    private final String typeTri;

    public X1VaisseauTri(String typeTri) {
        this.typeTri = typeTri;
    }

    public int compare(Object o1, Object m2Obj) {
        if (!(o1 instanceof FleetMemberAPI) || !(m2Obj instanceof FleetMemberAPI)) {
            return 0;
        }
        
        FleetMemberAPI m1 = (FleetMemberAPI) o1;
        FleetMemberAPI m2 = (FleetMemberAPI) m2Obj;
        
        float r1 = X1MecaniqueUtil.getCalculerPorteeMaxArmes(m1);
        float r2 = X1MecaniqueUtil.getCalculerPorteeMaxArmes(m2);
        
        if ("DESCENDING".equals(typeTri)) {
            return Float.compare(r2, r1);
        }
        return Float.compare(r1, r2);
    }

    public static void trierFlotte(List flotte, String typeTri) {
        if (flotte == null || flotte.isEmpty() || "NONE".equals(typeTri)) {
            return;
        }
        Collections.sort(flotte, new X1VaisseauTri(typeTri));
    }
}
