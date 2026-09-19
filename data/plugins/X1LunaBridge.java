package data.plugins.luna;

public class X1LunaBridge {

    public static boolean lireConfigurationLangue() {
        try {
            return lunalib.backend.ui.components.LunaModSettings.getBoolean("exchange_refit_language");
        } catch (Throwable t) {}
        return false;
    }
}
