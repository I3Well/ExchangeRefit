package data.plugins;

public class X1TerminalFiltresAiguillage {

    public static void cycleHullFilter(X1TerminalMenu menu) {
        String f = menu.filtreTailleVaisseau;
        if (f.equals("ALL")) { menu.filtreTailleVaisseau = "FRIGATE"; }
        else if (f.equals("FRIGATE")) { menu.filtreTailleVaisseau = "DESTROYER"; }
        else if (f.equals("DESTROYER")) { menu.filtreTailleVaisseau = "CRUISER"; }
        else if (f.equals("CRUISER")) { menu.filtreTailleVaisseau = "CAPITAL_SHIP"; }
        else { menu.filtreTailleVaisseau = "ALL"; }
    }

    public static void cycleTechFilter(X1TerminalMenu menu) {
        String t = menu.filtreTechnoArmes;
        if (t.equals("ALL")) { menu.filtreTechnoArmes = "LOW_TECH"; }
        else if (t.equals("LOW_TECH")) { menu.filtreTechnoArmes = "MID_TECH"; }
        else if (t.equals("MID_TECH")) { menu.filtreTechnoArmes = "HIGH_TECH"; }
        else { menu.filtreTechnoArmes = "ALL"; }
    }
}
