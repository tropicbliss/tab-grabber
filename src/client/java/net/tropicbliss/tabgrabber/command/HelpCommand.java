package net.tropicbliss.tabgrabber.command;

import net.fabricmc.fabric.api.client.command.v2.ClientCommandManager;
import net.fabricmc.fabric.api.client.command.v2.ClientCommandRegistrationCallback;
import net.minecraft.text.ClickEvent;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;

import java.net.URI;

public class HelpCommand {
    private static final URI HELP_LINK = URI.create("https://modrinth.com/mod/tab-grabber");

    public static void register() {
        ClientCommandRegistrationCallback.EVENT.register((dispatcher, registryAccess) -> dispatcher.register(ClientCommandManager.literal("tabgrabberhelp").executes(ctx -> {
            Text message = Text.translatable("text.tabgrabber.doc_link_msg").styled(style -> style.withClickEvent(new ClickEvent.OpenUrl(HELP_LINK)).withColor(Formatting.BLUE).withUnderline(true));
            ctx.getSource().sendFeedback(message);
            return 0;
        })));
    }
}
