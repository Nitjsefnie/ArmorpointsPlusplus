//package dev.cheos.armorpointspp.render;
//
//import com.mojang.blaze3d.systems.RenderSystem;
//
//import dev.cheos.armorpointspp.config.ApppConfig;
//import net.minecraft.client.Minecraft;
//import net.minecraftforge.client.event.RenderGameOverlayEvent;
//import net.minecraftforge.client.event.RenderGameOverlayEvent.ElementType;
//import net.minecraftforge.client.gui.ForgeIngameGui;
//import net.minecraftforge.eventbus.api.EventPriority;
//import net.minecraftforge.eventbus.api.SubscribeEvent;
//
//@SuppressWarnings("deprecation")
//public class RenderGameOverlayHandler {
//	private static boolean debug = false;
//	private final HUDRenderer hudRenderer;
//	private int lastArmorHeight = -1, lastHealthHeight = -1;
//	private boolean renderArmor = false, renderHealth = false;
//
//	public RenderGameOverlayHandler(Minecraft minecraft) {
//		this.hudRenderer = new HUDRenderer(minecraft);
//	}
//
//	@SubscribeEvent(priority = EventPriority.HIGHEST, receiveCanceled = true)
//	public void renderGameOverlayFirst(RenderGameOverlayEvent.Pre event) {
//		if (event.getType() == ElementType.ALL)
//			renderArmor = renderHealth = false; // this will happen ALWAYS, FIRST (before anything else can possibly happen)
//	}
//
//	@SubscribeEvent(priority = EventPriority.LOWEST)
//	public void renderGameOverlayPre(RenderGameOverlayEvent.Pre event) {
//		switch (event.getType()) {
//			case ARMOR:
//				this.lastArmorHeight = baseY(event);
//				this.renderArmor = true;
//
//				if (conf("debug") || debug)
//					hudRenderer.debugTexture(event.getMatrixStack());
//				if (conf("enableArmorBar")) {
//					hudRenderer.renderArmor(event.getMatrixStack(), baseX(event), lastArmorHeight);
//					           // no event cancel for compat with other mods that use RenderGameOverlayEvent.Post
//					disable(); // prevent minecraft from rendering vanilla armor bar
//				}
//				break;
//
//			case HEALTH:
//				this.lastHealthHeight = baseY(event);
//				this.renderHealth = true;
//
//				if (conf("enableHealthBar")) {
//					hudRenderer.renderHealth(event.getMatrixStack(), baseX(event), lastHealthHeight);
//					           // no event cancel for compat with other mods that use RenderGameOverlayEvent.Post
//					disable(); // prevent minecraft from rendering vanilla health bar
//				}
//				break;
//			default:
//				return;
//		}
//	}
//
//	@SubscribeEvent(priority = EventPriority.HIGHEST)
//	public void renderGameOverlayPost(RenderGameOverlayEvent.Post event) {
//		switch (event.getType()) {
//			case ARMOR:
//				if (conf("enableArmorBar"))
//					enable();
//				return;
//
//			case HEALTH:
//				if (conf("enableHealthBar")) {
//					enable();
//					ForgeIngameGui.left_height = forgeLeftHeight(event, lastHealthHeight) + 10;
//				}
//				return;
//			default:
//				return;
//		}
//	}
//
//	@SubscribeEvent(priority = EventPriority.LOWEST)
//	public void renderGameOverlayLast(RenderGameOverlayEvent.Post event) { // top overlays will get rendered LAST to maximize compatibility
//		switch (event.getType()) {
//			case ARMOR:
//				if (conf("showResistance"))
//					hudRenderer.renderResistance(event.getMatrixStack(), baseX(event), lastArmorHeight);
//				if (conf("showProtection"))
//					hudRenderer.renderProtectionOverlay(event.getMatrixStack(), baseX(event), lastArmorHeight);
//				if (conf("showToughness"))
//					hudRenderer.renderArmorToughness(event.getMatrixStack(), baseX(event), lastArmorHeight);
//				return;
//
//			case HEALTH:
//				if (conf("enableHealthBar") && conf("showAbsorption")) // absorb borders only work on stacked hearts
//					hudRenderer.renderAbsorption(event.getMatrixStack(), baseX(event), lastHealthHeight);
//				return;
//
//			case TEXT:
//				if (conf("debug") || debug)
//					hudRenderer.debugText(event.getMatrixStack(), baseX(event), lastArmorHeight);
//				if (conf("showArmorValue") && renderArmor)   // armor render event was not cancelled
//					hudRenderer.renderArmorText(event.getMatrixStack(), baseX(event), lastArmorHeight);
//				if (conf("showHealthValue") && renderHealth) // health render event was not cancelled
//					hudRenderer.renderHealthText(event.getMatrixStack(), baseX(event), lastHealthHeight);
//				return;
//			default:
//				return;
//		}
//	}
//
//	private int baseX(RenderGameOverlayEvent event) {
//		return event.getWindow().getGuiScaledWidth() / 2 - 91;
//	}
//
//	private int baseY(RenderGameOverlayEvent event) {
//		return event.getWindow().getGuiScaledHeight() - ForgeIngameGui.left_height;
//	}
//
//	private int forgeLeftHeight(RenderGameOverlayEvent event, int height) {
//		return event.getWindow().getGuiScaledHeight() - height;
//	}
//
//	private void enable() {
//		RenderSystem.enableTexture();
//		RenderSystem.color4f(1, 1, 1, 1);
//	}
//
//	private void disable() {
//		RenderSystem.disableTexture();
//		RenderSystem.color4f(0, 0, 0, 0);
//	}
//
//	private boolean conf(String name) {
//		return ApppConfig.getBool(name);
//	}
//}
