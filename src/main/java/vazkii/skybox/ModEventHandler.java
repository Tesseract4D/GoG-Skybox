package vazkii.skybox;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.multiplayer.WorldClient;
import net.minecraft.world.WorldProvider;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

import java.lang.reflect.Field;

public class ModEventHandler {
    public static int ticksInGame = 0;
    public static float partialTicks = 0.0f;
    public static float delta = 0.0f;
    public static float total = 0.0f;

    private static void calcDelta() {
        float oldTotal = total;
        total = (float) ticksInGame + partialTicks;
        delta = total - oldTotal;
    }

    @SubscribeEvent
    public static void renderTick(TickEvent.RenderTickEvent event) {
        if (event.phase == TickEvent.Phase.START) {
            partialTicks = event.renderTickTime;
        }
    }

    @SubscribeEvent
    public static void clientTickEnd(TickEvent.ClientTickEvent event) {
        if (event.phase == TickEvent.Phase.END) {
            Minecraft mc = Minecraft.getMinecraft();
            GuiScreen gui = mc.currentScreen;
            if (gui == null || !gui.doesGuiPauseGame()) {
                ++ticksInGame;
                partialTicks = 0.0f;
            }
            ModEventHandler.calcDelta();
        }
    }

    @SubscribeEvent
    public static void onRender(RenderWorldLastEvent event) {
        WorldClient world = Minecraft.getMinecraft().world;
        if (world.provider.getDimension() == 0 && !(world.provider.getSkyRenderer() instanceof SkyblockSkyRenderer)) {
            world.provider.setSkyRenderer(new SkyblockSkyRenderer());
        }
    }
}