package nokocraft118.nokocraft118.dogs;

import net.minecraft.ChatFormatting;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;

import static nokocraft118.nokocraft118.Nokocraft118.MODID;

public class TuatDogRenderer extends MobRenderer<TuatDogEntity, TuatDogMultiModel<TuatDogEntity>> {
    private static final ResourceLocation HAKKEN = new ResourceLocation(MODID, "textures/entity/dog/tuatdog_hakken.png");
    private static final ResourceLocation KOUKEN = new ResourceLocation(MODID, "textures/entity/dog/tuatdog_kouken.png");


    public TuatDogRenderer(EntityRendererProvider.Context context) {
        super(context, new TuatDogMultiModel<>(context.bakeLayer(TuatDogLayerLocation.DOG)), 0.3F);
    }

    @Override
    public ResourceLocation getTextureLocation(TuatDogEntity pEntity) {
        String s = ChatFormatting.stripFormatting(pEntity.getName().getString());

        //うさぎのテクスチャ割当を書き換えてハッケンとコウケンの2バリエーションがランダムで出現するように設定している

        switch(pEntity.getRabbitType()) {
            default:
            case 0:
            case 1:
            case 2:
                return HAKKEN;
            case 3:
            case 4:
            case 5:
                return KOUKEN;
        }
    }
}
