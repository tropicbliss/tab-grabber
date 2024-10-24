package net.tropicbliss.tabgrabber.keybind;

import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;
import net.minecraft.text.Text;
import net.tropicbliss.tabgrabber.TabGrabber;
import net.tropicbliss.tabgrabber.config.ConfigManager;
import net.tropicbliss.tabgrabber.config.ModConfig;
import org.lwjgl.glfw.GLFW;

public class KeyInputHandler {
    public static String KEY_TOGGLE_GUI = "key." + TabGrabber.MOD_ID + ".togglegui";
    public static KeyBinding toggleGuiKey = KeyBindingHelper
            .registerKeyBinding(
                    new KeyBinding(KEY_TOGGLE_GUI, InputUtil.Type.KEYSYM, GLFW.GLFW_KEY_O, KeyBinding.UI_CATEGORY)
            );

    public static void register() {
        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            if (toggleGuiKey.wasPressed()) {
                ConfigManager.toggleGuiOption();
                if (client.player != null) {
                    ModConfig config = ConfigManager.getConfig();
                    String messageKey = config.enable ? "text.tabgrabber.enabled_hud" : "text.tabgrabber.disabled_hud";
                    client.player.sendMessage(Text.translatable(messageKey), false);
                }
            }
        });
    }
}
