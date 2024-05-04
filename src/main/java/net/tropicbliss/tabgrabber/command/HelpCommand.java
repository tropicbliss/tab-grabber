package net.tropicbliss.tabgrabber.command;

import net.fabricmc.fabric.api.client.command.v2.ClientCommandManager;
import net.fabricmc.fabric.api.client.command.v2.ClientCommandRegistrationCallback;
import net.minecraft.text.ClickEvent;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;

public class HelpCommand {
    private static final String HELP_LINK = "https://github.com/tropicbliss/tab-grabber?tab=readme-ov-file#usage";

    public static void register() {
        ClientCommandRegistrationCallback.EVENT.register((dispatcher, registryAccess) -> dispatcher.register(ClientCommandManager.literal("tabgrabberhelp").executes(ctx -> {
            Text message = Text.translatable("text.tabgrabber.doc_link_msg").styled(style -> style.withClickEvent(new ClickEvent(ClickEvent.Action.OPEN_URL, HELP_LINK)).withColor(Formatting.BLUE).withUnderline(true));
            ctx.getSource().sendFeedback(message);
            return 0;
        })));
    }
}
