package net.tropicbliss.tabgrabber;

import com.terraformersmc.modmenu.api.ConfigScreenFactory;
import com.terraformersmc.modmenu.api.ModMenuApi;
import net.tropicbliss.tabgrabber.config.ConfigManager;

public class ModMenuEntrypoint implements ModMenuApi {
    @Override
    public ConfigScreenFactory<?> getModConfigScreenFactory() {
        return ConfigManager::buildConfigScreen;
    }
}
