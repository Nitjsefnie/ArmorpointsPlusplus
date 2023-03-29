package dev.cheos.armorpointspp.impl;

import dev.cheos.armorpointspp.core.adapter.IMobEffect;
import dev.cheos.armorpointspp.core.adapter.IMobEffectInstance;
import net.minecraft.world.effect.MobEffectInstance;

public class MobEffectInstanceImpl implements IMobEffectInstance {
	private final MobEffectInstance effect;
	
	public MobEffectInstanceImpl(MobEffectInstance effect) {
		this.effect = effect;
	}
	
	@Override
	public int amplifier() {
		return this.effect.getAmplifier();
	}

	@Override
	public int duration() {
		return this.effect.getDuration();
	}

	@Override
	public IMobEffect effect() {
		return MobEffectImpl.of(this.effect.getEffect());
	}
}
