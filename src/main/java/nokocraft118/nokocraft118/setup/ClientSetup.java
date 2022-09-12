package nokocraft118.nokocraft118.setup;

import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import nokocraft118.nokocraft118.dogs.TuatDogMultiModel;
import nokocraft118.nokocraft118.dogs.TuatDogRenderer;
import nokocraft118.nokocraft118.fireworks.StrworkRenderer;

import static nokocraft118.nokocraft118.Nokocraft118.MODID;

//登録するイベントが入ってるクラスにつけるやつ
//MODIDはメインクラスで付けたIDを直書きしても別にいい
//Dist.CLIENTで「クライアント側のみで動作させるよー」とコンパイラに認識してもらう
@Mod.EventBusSubscriber(modid = MODID, value = Dist.CLIENT, bus = Mod.EventBusSubscriber.Bus.MOD)
public class ClientSetup {

    //@SubscribeEventはイベントとしてForgeのどこかから呼び出されるよ，という意味の呪文だとでも思っていただければ
    //モデル登録系イベントに割り込むように，狗のモデルを登録している
    @SubscribeEvent
    public static void onRegisterLayers(EntityRenderersEvent.RegisterLayerDefinitions event) {
        //狗のモデル登録
        event.registerLayerDefinition(TuatDogMultiModel.LAYER_LOCATION, TuatDogMultiModel::createBodyLayer);
    }

    //ここではエンティティのレンダラーを登録
    //レンダラーのクラス内部ではテクスチャの場所を指定したり，上で登録したようなモデルをどう配置するかなどが記述されている
    @SubscribeEvent
    public static void onRegisterRenderer(EntityRenderersEvent.RegisterRenderers event) {
        //狗
        event.registerEntityRenderer(Registration.DOG.get(), TuatDogRenderer::new);
        //飛んでいく花火
        event.registerEntityRenderer(Registration.STRWORK.get(), StrworkRenderer::new);
    }
}
