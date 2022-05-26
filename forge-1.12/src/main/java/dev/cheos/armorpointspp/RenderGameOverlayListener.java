package dev.cheos.armorpointspp;

import static net.minecraftforge.client.event.RenderGameOverlayEvent.ElementType.*;

import net.minecraft.client.Minecraft;
import net.minecraftforge.client.GuiIngameForge;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.client.event.RenderGameOverlayEvent.*;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.eventhandler.*;
import net.minecraftforge.fml.relauncher.Side;

@EventBusSubscriber(modid = Armorpointspp.MODID, value = Side.CLIENT)
public class RenderGameOverlayListener {
	private static final Minecraft minecraft = Minecraft.getMinecraft();
	private static boolean reposting, working;
	
	@SubscribeEvent(priority = EventPriority.HIGHEST, receiveCanceled = true)
	public static void handle(RenderGameOverlayEvent event) {
		if (event instanceof ApppRenderGameOverlayEvent) return;
		if (reposting) return;
		if (working) return;
		
		if (event.getType() != ARMOR && event.getType() != HEALTH) { // we only want to handle armor and health ourselves
			if (!(event.isCancelable() && event.isCanceled())) // do not repost if our event somehow got cancelled - SHOULD not happen, though
				if (repost(event) && event.isCancelable())     // repost the event, returns true if cancelled
					event.setCanceled(true);                   // cancel our "parent" event if our reposted event got cancelled
			if (event.getType() != TEXT || (event.isCancelable() && event.isCanceled()))
				return;                                        // return if we are cancelled and not a text event, we want to render but not handle text events
		}
		
		working = true;
		GuiIngameForge gui  = (GuiIngameForge) minecraft.ingameGUI;
		float partialTicks = event.getPartialTicks();
		int screenWidth    = event.getResolution().getScaledWidth();
		int screenHeight   = event.getResolution().getScaledHeight();
		
		switch (event.getType()) {
			case ARMOR:
				if (!(event instanceof Pre)) // extra check for this because mantle thinks it's great and stuff
					break;
				event.setCanceled(true); // prevent forge from rendering vanilla stuff
				if (pre(event, ARMOR))
					break;
				Overlays.armorLevel      (gui, partialTicks, screenWidth, screenHeight);
				Overlays.magicShield     (gui, partialTicks, screenWidth, screenHeight);
				Overlays.resistance      (gui, partialTicks, screenWidth, screenHeight);
				Overlays.protection      (gui, partialTicks, screenWidth, screenHeight);
				Overlays.armorToughness  (gui, partialTicks, screenWidth, screenHeight);
				Overlays.armorToughnessOv(gui, partialTicks, screenWidth, screenHeight);
				post(event, ARMOR);
				break;
			case HEALTH:
				if (!(event instanceof Pre)) // extra check for this because mantle thinks it's great and stuff
					break;
				event.setCanceled(true); // prevent forge from rendering vanilla stuff
				if (pre(event, HEALTH)) {
					if (Armorpointspp.MANTLE) // specific fix, JUST for mantle... why are you like this, mantle?
						post(event, HEALTH);
					break;
				}
				Overlays.playerHealth(gui, partialTicks, screenWidth, screenHeight);
				Overlays.absorption  (gui, partialTicks, screenWidth, screenHeight);
				post(event, HEALTH);
				break;
			case TEXT:
				if (event instanceof Post)
					break;
				Overlays.armorText (gui, partialTicks, screenWidth, screenHeight);
				Overlays.healthText(gui, partialTicks, screenWidth, screenHeight);
				Overlays.debug     (gui, partialTicks, screenWidth, screenHeight);
				break;
			default: // we can only reach this switch with ARMOR, HEALTH and TEXT, everything else indicates something going wrong
				working = false;
				throw new RuntimeException("Something went wrong - reached code that should not be reachable with event type " + event.getType());
			}
		Overlays.cleanup();
		working = false;
	}
	
	private static boolean repost(RenderGameOverlayEvent event) {
		if (event instanceof Chat)
			return post(new ApppRenderGameOverlayEvent.Chat(event, ((Chat) event).getPosX(), ((Chat) event).getPosY()));
		else if (event instanceof Text)
			return post(new ApppRenderGameOverlayEvent.Text(event, ((Text) event).getLeft(), ((Text) event).getRight()));
		else if (event instanceof BossInfo)
			return post(new ApppRenderGameOverlayEvent.BossInfo(event, ((BossInfo) event).getType(), ((BossInfo) event).getBossInfo(), ((BossInfo) event).getX(), ((BossInfo) event).getY(), ((BossInfo) event).getIncrement()));
		else if (event instanceof Pre)
			return pre(event, event.getType());
		else if (event instanceof Post)
			post(event, event.getType());
		return false;
	}
	
	private static boolean post(Event event) {
		reposting = true;
		boolean cancelled = MinecraftForge.EVENT_BUS.post(event);
		reposting = false;
		return cancelled;
	}
	
	private static boolean pre(RenderGameOverlayEvent parent, ElementType type) {
		return post(new ApppRenderGameOverlayEvent.Pre(parent, type));
	}
	
	private static void post(RenderGameOverlayEvent parent, ElementType type) {
		post(new ApppRenderGameOverlayEvent.Post(parent, type));
	}
}
