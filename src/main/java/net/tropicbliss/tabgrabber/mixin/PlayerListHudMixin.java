package net.tropicbliss.tabgrabber.mixin;

import net.minecraft.client.gui.hud.PlayerListHud;
import net.minecraft.client.network.PlayerListEntry;
import net.minecraft.text.Text;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;
import org.spongepowered.asm.mixin.gen.Invoker;

import java.util.List;

@Mixin(PlayerListHud.class)
public interface PlayerListHudMixin {
    @Invoker("collectPlayerEntries")
    List<PlayerListEntry> invokeCollectPlayerEntries();

    @Accessor
    Text getHeader();

    @Accessor
    Text getFooter();
}
