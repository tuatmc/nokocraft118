package nokocraft118.nokocraft118.setup;

import net.minecraft.client.Minecraft;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraftforge.client.event.ParticleFactoryRegisterEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.EntityAttributeCreationEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import nokocraft118.nokocraft118.dogs.TuatDogEntity;
import nokocraft118.nokocraft118.fireworks.StrworkDispenser;
import nokocraft118.nokocraft118.fireworks.StrworkParticle;
import nokocraft118.nokocraft118.fireworks.TuatParticleTypes;

import static nokocraft118.nokocraft118.Nokocraft118.MODID;


//Modの初期設定とかそういうごちゃごちゃしたのをまとめるためのクラス
@Mod.EventBusSubscriber(modid = MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class ModSetup {

    //クリエタブの設定，タブの名前，アイコンなどを設定
    public static final String TAB_NAME = "nokocraft";
    public static final CreativeModeTab ITEM_GROUP = new CreativeModeTab(TAB_NAME) {
        @Override
        //狗卵をアイコンに
        public ItemStack makeIcon() {
            return new ItemStack(Registration.DOG_EGG.get());
        }
    };

    //この部分がNokocraft118.javaがら呼び出される
    public static void init(FMLCommonSetupEvent event) {
        IEventBus bus = MinecraftForge.EVENT_BUS;
        event.enqueueWork(() -> {
        });
        StrworkDispenser.registerBehavior();
    }

    //狗のライフとかその他ステータスを設定する
    @SubscribeEvent
    public static void onAttributeCreate(EntityAttributeCreationEvent event) {
        event.put(Registration.DOG.get(), TuatDogEntity.prepareAttributes().build());
    }

    //パーティクルの登録
    @SubscribeEvent
    public static void onRegisterParticle(final ParticleFactoryRegisterEvent event) {
        Minecraft.getInstance().particleEngine.register(
                TuatParticleTypes.SPARK.get(),
                StrworkParticle.SparkProvider::new);
        Minecraft.getInstance().particleEngine.register(
                TuatParticleTypes.FLASH.get(),
                StrworkParticle.FlashProvider::new);
    }
}
