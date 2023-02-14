package me.aurora.client;

import gg.essential.api.EssentialAPI;
import me.aurora.client.commands.AuroraSpammerCommand;
import me.aurora.client.commands.HUDCommand;
import me.aurora.client.config.Config;
import me.aurora.client.commands.ConfigCommand;
import me.aurora.client.config.HUDEdit;
import me.aurora.client.events.TickEndEvent;
import me.aurora.client.events.packets.PacketHandler;
import me.aurora.client.features.ArrayList;
import me.aurora.client.features.dungeons.*;
import me.aurora.client.features.garden.AutoComposter;
import me.aurora.client.features.garden.GrassESP;
import me.aurora.client.features.mining.GemstoneScanner;
import me.aurora.client.features.mining.StructureScanner;
import me.aurora.client.features.misc.*;
import me.aurora.client.features.movement.*;
import me.aurora.client.features.visual.*;
import me.aurora.client.krypton.Main;
import me.aurora.client.utils.VersionUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import org.lwjgl.opengl.Display;

import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.Random;

@Mod(modid = Aurora.MODID, name = Aurora.MODNAME, version = Aurora.VERSION, clientSideOnly = true)
public class Aurora
{
    public static final String MODID = "bossbar_customizer";
    public static final String MODNAME = "BossbarCustomizer";
    public static final String VERSION = "1.2.1";
    public static final String CURRENTVERSIONBUILD = "330";
    public static HUDEdit hudEdit = new HUDEdit();

    public static GuiScreen guiToOpen = null;
    public static Config config;
    public static Minecraft mc = Minecraft.getMinecraft();
    public static File modFile = null;

    public static java.util.ArrayList<Element> getHudModules() {
        return hudModules;
    }

    private static java.util.ArrayList<Element> hudModules = new java.util.ArrayList<Element>();


    @EventHandler
    public void init(FMLInitializationEvent event) throws IOException, FontFormatException {
        Display.setTitle("Minecraft 1.8.9 - Aurora 3.3");
        EssentialAPI.getCommandRegistry().registerCommand(new ConfigCommand());
        EssentialAPI.getCommandRegistry().registerCommand(new HUDCommand());
        EssentialAPI.getCommandRegistry().registerCommand(new AuroraSpammerCommand());
        MinecraftForge.EVENT_BUS.register(this);
        config = new Config();
        config.preload();
        MinecraftForge.EVENT_BUS.register(new ArrayList());
        MinecraftForge.EVENT_BUS.register(new AutoSell());
        MinecraftForge.EVENT_BUS.register(new Ghostblock());
        MinecraftForge.EVENT_BUS.register(new WitherDoorRemover());
        MinecraftForge.EVENT_BUS.register(new TpAnywhere());
        MinecraftForge.EVENT_BUS.register(new HarpStealer());
        MinecraftForge.EVENT_BUS.register(new NoSlowButWorse());
        MinecraftForge.EVENT_BUS.register(new GemstoneScanner());
        MinecraftForge.EVENT_BUS.register(new TickEndEvent());
        MinecraftForge.EVENT_BUS.register(new AutoJoinSkyblock());
        MinecraftForge.EVENT_BUS.register(new AutoRogue());
        MinecraftForge.EVENT_BUS.register(new AutoSecrets());
        MinecraftForge.EVENT_BUS.register(new MelodyThrottle());
        MinecraftForge.EVENT_BUS.register(new StructureScanner());
/*        MinecraftForge.EVENT_BUS.register(new AutoGabagool());
        MinecraftForge.EVENT_BUS.register(new AutoGabagoolTEST());*/
        MinecraftForge.EVENT_BUS.register(new NoDowntime());
        MinecraftForge.EVENT_BUS.register(new AutoSprint());
        MinecraftForge.EVENT_BUS.register(new AutoCrystals());
        MinecraftForge.EVENT_BUS.register(new WitherCloakAura());
        MinecraftForge.EVENT_BUS.register(new DungeonMap());
        MinecraftForge.EVENT_BUS.register(new AutoTank());
        MinecraftForge.EVENT_BUS.register(new NoBedrock());
        MinecraftForge.EVENT_BUS.register(new VClip());
        MinecraftForge.EVENT_BUS.register(new CrystalPlacer());
        MinecraftForge.EVENT_BUS.register(new AntiLimbo());
        MinecraftForge.EVENT_BUS.register(new Main());
        MinecraftForge.EVENT_BUS.register(new AutoSellBz());
        MinecraftForge.EVENT_BUS.register(new PacketHandler());
        MinecraftForge.EVENT_BUS.register(new GrassESP());
        MinecraftForge.EVENT_BUS.register(new AutoComposter());
        if (VersionUtil.isOutdated(Integer.parseInt(CURRENTVERSIONBUILD))) {
            Runtime.getRuntime().addShutdownHook(new Thread(this::update));
        }
        hudModules.add(new Watermark());
        hudModules.add(new Keystrokes());
        hudModules.add(new PacketDebug());
        hudModules.add(new FPS());
    }

    @SubscribeEvent
    public void onTick(TickEvent event) {
        if (guiToOpen!=null){
            mc.displayGuiScreen(guiToOpen);
            guiToOpen=null;
        }
    }

    private void update() {
        if (!Config.autoUpdate) return;
        try {
            InputStream in = new URL("https://github.com/Gabagooooooooooool/AuroraUpdater/releases/download/1.0/updater.jar").openStream();
            File updater = new File(System.getProperty("java.io.tmpdir") + "aurora_updater_" + new Random().nextInt() + ".jar");
            Files.copy(in, updater.toPath(), StandardCopyOption.REPLACE_EXISTING);
            ProcessBuilder pb = new ProcessBuilder("java", "-jar", "\"" + updater.getAbsolutePath() + "\"", "1000", "\"" + modFile.getAbsolutePath() + "\"", "mainrepo");
            Process p = pb.start();
            System.out.println("Updating...");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Mod.EventHandler
    public void preInit(FMLPreInitializationEvent event) {
        modFile = event.getSourceFile();
    }

    @SubscribeEvent
    public void renderGameOverlayEvent(RenderGameOverlayEvent.Post event) {
        hudModules.forEach(x->hudEdit.RenderGUI(x));
    }


}
