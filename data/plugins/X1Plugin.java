package data.plugins;

import com.fs.starfarer.api.BaseModPlugin;
import com.fs.starfarer.api.Global;

public class X1Plugin extends BaseModPlugin {
    
    public static boolean ENABLE_SMOD_HANDLING = true;
    public static boolean ENABLE_FIGHTER_HANDLING = true;
    public static boolean ENABLE_WEAPON_GROUP_HANDLING = true;

    @Override
    public void onApplicationLoad() {
        ENABLE_SMOD_HANDLING = true;
        ENABLE_FIGHTER_HANDLING = true;
        ENABLE_WEAPON_GROUP_HANDLING = true;
        
        if (Global.getSettings().getModManager().isModEnabled("progsmods")) {
            ENABLE_SMOD_HANDLING = false;
        }
    }
    
    @Override
    public void onGameLoad(boolean newGame) {
        super.onGameLoad(newGame);
        Global.getSector().addTransientScript(new X1ClavierScript());
    }
    
    @Override
    public void onNewGame() {
        super.onNewGame();
        Global.getSector().addTransientScript(new X1ClavierScript());
    }
}
