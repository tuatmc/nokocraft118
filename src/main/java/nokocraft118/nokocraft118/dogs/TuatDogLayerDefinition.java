package nokocraft118.nokocraft118.dogs;


import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import static nokocraft118.nokocraft118.Nokocraft118.MODID;

//テクスチャの貼り方とかを設定する，見た目だけなのでクライアントのみで実行される
@Mod.EventBusSubscriber(modid = MODID, value = Dist.CLIENT, bus = Mod.EventBusSubscriber.Bus.MOD)
public class TuatDogLayerDefinition {
    @SubscribeEvent
    public static void registerLayers(EntityRenderersEvent.RegisterLayerDefinitions event) {
        event.registerLayerDefinition(TuatDogLayerLocation.DOG, TuatDogMultiModel::createBodyLayer);
    }
}
