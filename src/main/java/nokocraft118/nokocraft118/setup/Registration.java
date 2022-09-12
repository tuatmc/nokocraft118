package nokocraft118.nokocraft118.setup;

import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.Material;
import net.minecraftforge.common.ForgeSpawnEggItem;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import nokocraft118.nokocraft118.dogs.TuatDogEntity;
import nokocraft118.nokocraft118.fireworks.StrworkEntity;
import nokocraft118.nokocraft118.fireworks.StrworkItem;
import nokocraft118.nokocraft118.fireworks.TuatParticleTypes;


import static nokocraft118.nokocraft118.Nokocraft118.MODID;

public class Registration {

    //アイテムとかブロックを登録するリストみたいなやつ
    private static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(ForgeRegistries.BLOCKS, MODID);
    private static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, MODID);
    private static final DeferredRegister<EntityType<?>> ENTITIES = DeferredRegister.create(ForgeRegistries.ENTITIES, MODID);

    //初期化，ここがメインクラスより呼ばれて以下のものがMinecraftに登録される
    public static void init() {
        IEventBus bus = FMLJavaModLoadingContext.get().getModEventBus();
        BLOCKS.register(bus);
        ITEMS.register(bus);
        ENTITIES.register(bus);
        TuatParticleTypes.register(FMLJavaModLoadingContext.get().getModEventBus());
    }

    //ブロックの持つ設定，ここを書き換えると硬さや歩いたとき・破壊したときの音，明るさ，ツール必須などの設定を行える．
    public static final BlockBehaviour.Properties BLOCK_PROPERTIES = BlockBehaviour.Properties.of(Material.STONE).strength(2f).requiresCorrectToolForDrops().noOcclusion();
    //アイテムの持つ設定，ここを書き換えることで食べられるようにしたりできる
    public static final Item.Properties ITEM_PROPERTIES = new Item.Properties().tab(ModSetup.ITEM_GROUP);


    //TATマークを追加する
    public static final RegistryObject<Block> TAT = BLOCKS.register("tat", () -> new Block(BLOCK_PROPERTIES));
    public static final RegistryObject<Item> TAT_ITEM = fromBlock(TAT);

    //花火アイテムを追加する
    public static final RegistryObject<Item> STRWORK_ITEM = ITEMS.register("strwork", () -> new StrworkItem(ITEM_PROPERTIES));


    //狗エンティティの登録
    public static final RegistryObject<EntityType<TuatDogEntity>> DOG = ENTITIES.register("tuatdog", () -> EntityType.Builder.of(TuatDogEntity::new, MobCategory.CREATURE)
            .sized(0.6f, 0.6f)
            .clientTrackingRange(8)
            .setShouldReceiveVelocityUpdates(false)
            .build("dog"));

    //いぬたまごを追加する
    public static final RegistryObject<Item> DOG_EGG = ITEMS.register("tuatdog", () -> new ForgeSpawnEggItem(DOG, 0x00ff00, 0x0000ff, ITEM_PROPERTIES));

    //花火エンティティの登録
    public static final RegistryObject<EntityType<StrworkEntity>> STRWORK = ENTITIES.register("strwork", () -> EntityType.Builder.<StrworkEntity>of(StrworkEntity::new, MobCategory.MISC)
            .sized(0.1f, 0.1f)
            .clientTrackingRange(8)
            .setShouldReceiveVelocityUpdates(false)
            .build("strwork"));

    //ブロックから自動的にブロックアイテムを作るやつ，便利なのでコピペ推奨
    public static <B extends Block> RegistryObject<Item> fromBlock(RegistryObject<B> block) {
        return ITEMS.register(block.getId().getPath(), () -> new BlockItem(block.get(), ITEM_PROPERTIES));
    }
}
