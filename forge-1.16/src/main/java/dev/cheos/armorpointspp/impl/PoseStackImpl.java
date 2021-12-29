package dev.cheos.armorpointspp.impl;

import com.mojang.blaze3d.matrix.MatrixStack;

import dev.cheos.armorpointspp.core.adapter.IPoseStack;

public class PoseStackImpl implements IPoseStack {
	private final MatrixStack poseStack;
	
	public PoseStackImpl(MatrixStack poseStack) {
		this.poseStack = poseStack;
	}
	
	@Override
	public void pushPose() {
		this.poseStack.pushPose();
	}
	
	@Override
	public void popPose() {
		this.poseStack.popPose();
	}
	
	@Override
	public void scale(float x, float y, float z) {
		this.poseStack.scale(x, y, z);
	}
	
	@Override
	public void translate(double x, double y, double z) {
		this.poseStack.translate(x, y, z);
	}
	
	@Override
	public Object getPoseStack() {
		return this.poseStack;
	}
}
