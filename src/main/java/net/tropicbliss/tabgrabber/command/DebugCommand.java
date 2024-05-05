package net.tropicbliss.tabgrabber.command;

import net.fabricmc.fabric.api.client.command.v2.ClientCommandManager;
import net.fabricmc.fabric.api.client.command.v2.ClientCommandRegistrationCallback;
import net.minecraft.text.ClickEvent;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.tropicbliss.tabgrabber.TabGrabber;

import java.util.Optional;

public class DebugCommand {
    public static void register() {
        ClientCommandRegistrationCallback.EVENT.register((dispatcher, registryAccess) -> dispatcher.register(ClientCommandManager.literal("tabgrabberdebug").executes(ctx -> {
            Optional<String> debugInfo = TabGrabber.tabManager.getDebugInfo();
            if (debugInfo.isPresent()) {
                String debugText = debugInfo.get();
                if (debugText.isEmpty()) {
                    ctx.getSource().sendFeedback(Text.translatable("text.tabgrabber.no_tab_info").formatted(Formatting.RED));
                } else {
                    String sDebugInfo = debugInfo.get();
                    ctx.getSource().sendFeedback(Text.literal(sDebugInfo));
                    ctx.getSource().sendFeedback(Text.literal("\n"));
                    Text debugClipboardPrompt = Text.translatable("text.tabgrabber.debug_clipboard").styled(style -> style.withClickEvent(new ClickEvent(ClickEvent.Action.COPY_TO_CLIPBOARD, sDebugInfo)).withUnderline(true).withColor(Formatting.BLUE));
                    ctx.getSource().sendFeedback(debugClipboardPrompt);
                }
                return 0;
            } else {
                Text text = Text.translatable("text.tabgrabber.invalid_scene").formatted(Formatting.RED);
                ctx.getSource().sendFeedback(text);
                return 1;
            }
        })));
    }
}
