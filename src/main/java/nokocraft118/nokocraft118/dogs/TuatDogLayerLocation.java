package nokocraft118.nokocraft118.dogs;

import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.resources.ResourceLocation;

import static nokocraft118.nokocraft118.Nokocraft118.MODID;

public class TuatDogLayerLocation {
    //テクスチャ名を指定，resources/assets/nokocraft118/textures/entity内で以下の名前に該当するものがあるか自動で探索される
    public static final ModelLayerLocation DOG = new ModelLayerLocation(new ResourceLocation(MODID, "tuatdog"), "main");
}
