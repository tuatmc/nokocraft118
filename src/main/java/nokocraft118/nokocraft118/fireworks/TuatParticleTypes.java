package nokocraft118.nokocraft118.fireworks;

import net.minecraft.core.particles.ParticleType;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

import static nokocraft118.nokocraft118.Nokocraft118.MODID;

public class TuatParticleTypes {
    public static final DeferredRegister<ParticleType<?>> PARTICLE_TYPES =
            DeferredRegister.create(ForgeRegistries.PARTICLE_TYPES, MODID);

    public static final RegistryObject<SimpleParticleType> SPARK =
            PARTICLE_TYPES.register("strspark", () -> new SimpleParticleType(true));

    public static final RegistryObject<SimpleParticleType> FLASH =
            PARTICLE_TYPES.register("strflash", () -> new SimpleParticleType(true));


    public static void register(IEventBus eventBus) {
        PARTICLE_TYPES.register(eventBus);
    }
}
