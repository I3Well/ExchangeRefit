===============================================================================
                       EXCHANGE REFIT - SHIPYARD TERMINAL
                                 Version 1.4.0
===============================================================================

Author: Anjasnare
Game Version Compatibility: Starsector 0.98a-RC8 (and above)
Dependencies: None (100% Standalone)
Supported Mod Utilities: Version Checker, Advanced Gunnery Control (AGC), AI Tweaks, Progressive S-Mods

-------------------------------------------------------------------------------
1. OVERVIEW
-------------------------------------------------------------------------------
ExchangeRefit introduces a powerful, secure Shipyard Terminal accessible 
directly from station docks. It allows captains to scan, duplicate, archive, 
and selectively paste complete ship loadouts using resources from the fleet 
cargo and can reattribute s_mods. 

Never manually re-fit a fleet of identical hulls weapon-by-weapon again!

-------------------------------------------------------------------------------
2. KEY FEATURES
-------------------------------------------------------------------------------
* Docks Hotkey: Press [X] while interacting with any station dock to open the 
  ExchangeRefit Terminal immediately.
* Live Hull Scanner Diagnostic: Instantly displays an advanced breakdown of 
  your active hulls, including deployment DP costs, shield types, live speed, 
  unassigned Ordnance Points (OP), custom station refit upgrades, and detailed 
  officer or AI Core skill profiles.
* Smart Weapon Sorting: Mounted weaponry in reviews is automatically filtered 
  and sorted by size brackets (Large, Medium, Small) and ranked by OP costs.
* Advanced Visual Palette: Weapons are dynamically color-coded by damage type 
  for swift tactical assessment (Red for High Explosive, White/Beige for Kinetic, 
  Blue for Energy, Yellow for Fragmentation).
* Unified Missile Profiles: Missile racks and mounts bypass damage type colors 
  and are assigned a distinct dark purple profile for instant layout mapping.
* Integrated Fighter Tracking: Carrier flight decks are scanned and displayed 
  with desaturated pastel colors based on their combat roles (Interceptor, 
  Bomber, Fighter, Support) and include mod origin tracking.
* Selective Custom Setup (Paste Filters): Choose exactly what components to 
  apply from a blueprint. Toggle individual modules:
    1. Mounted Weapons
    2. Standard Hullmods (sorted by OP)
    3. Story Mods (S-Mods)
    4. Weapon Automation (Advanced Gunnery Control & AI Tweaks tags)
    5. Fighter Wings layout
    6. Weapon Group assignments
    7. Officer profile assignment
    8. Additional Refit Upgrades (Station specific sub-upgrades / Mod tracking)
* Cargo Shortage Alerts: The diagnostic engine tracks your fleet stockpiles 
  and dynamically alerts you with dark orange indicators if weapons or required 
  hullmod blueprints are missing from your cargo before executing a paste.
* Progressive S-Mods Synergy: Fully compatible with the Progressive S-Mods framework. 
  By toggling Option 8 (Additional Refit Upgrades), your hull's progressive experience 
  (XP) tags and unique progression data are safely archived and restored.
* Dynamic Refit Command Previews: A visual validation step showing a concise 
  [YES] / [NO] confirmation checklist before executing any layout override.
* Secure S-Mod Stripper Tool: Refund and manage built-in modifications with 
  two safe workshop commands (Built-in chassis S-Mods like XIV hulls are protected):
    - Option 1 (Complete Wipe): Removes S-Mods and returns 100% Story Points.
    - Option 2 (Downgrade to Standard): Strips the S-Mod status but safely 
      attempts to keep the mod equipped as a normal Hullmod (blocks action and 
      notifies you with cost requirements if remaining ship OP is insufficient).
* Ergonomic Navigation: Native terminal design supporting raw [Esc] hotkey mapping 
  on all backward options.

-------------------------------------------------------------------------------
3. INSTALLATION
-------------------------------------------------------------------------------
1. Extract the content of the "ExchangeRefit.zip" file.
2. Place the "ExchangeRefit" folder inside your Starsector mod directory:
   "../Starsector/mods/"
3. Launch Starsector and enable "ExchangeRefit" in the mod manager checklist.

-------------------------------------------------------------------------------
4. CONFIGURATION & LANGUAGES
-------------------------------------------------------------------------------
The mod is English by default. A local translation toggle is available for 
bilingual setups. To modify your preferences, open the local configuration file:
"../ExchangeRefit/data/config/exchangerefit_config.json"

* Set "french_translation": true  for French interface.
* Set "french_translation": false for English interface (Default).

Your ship templates are universally archived as structural plain text blueprints. 
They are automatically written to and loaded from your local Starsector common 
save path:
"../Starsector/saves/common/exchangerefit_[hull_id]_slot[1-13].txt"

Blueprints can be easily shared between profiles or manually wiped through the 
terminal interface using the "Delete blueprint" operational command.

-------------------------------------------------------------------------------
5. CHANGELOG & STABILITY (v1.4.0)
-------------------------------------------------------------------------------
* Implemented full architectural compatibility with AI Tweaks mod behaviors.
* Added native blacklist filters preventing internal/hidden combat AI hullmods 
  from polluting or corrupting exported blueprint text files.
* Enhanced Shipyard Terminal visual diagnostic panel to dynamically show when 
  a hull profile is driven by AI Tweaks global automation behaviors.
* Added full display support for carrier ship variants, tracking weapon range, 
  wing roles, and mod origins within archived layouts.
* Refactored text encoding mechanisms to seamlessly escape specific layout 
  control characters (like ':' symbol) within Commander data layouts.
* Hardcoded defensive cross-platform file name sanitizers forcing absolute 
  lowercase and alpha-numeric bounds to avoid cross-OS I/O load crashes.
* Sealed S-Mod Stripper engine to prevent illegal Story Point generation exploits 
  via native hull hullmods (XIV variants, Hegemony blueprints).
* Added immediate UI dynamic memory cache flushing when execution downgrades occur 
  to prevent ghost Ordnance Point calculation lags on the campaign layer.

-------------------------------------------------------------------------------
Thank you for downloading ExchangeRefit! Safe travels through the Persean Sector.
===============================================================================
