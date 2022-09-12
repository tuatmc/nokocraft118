package nokocraft118.nokocraft118;

import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import nokocraft118.nokocraft118.setup.ModSetup;
import nokocraft118.nokocraft118.setup.Registration;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


//MODIDは変更したら↓の部分とresources/META-INF/mods.toml内両方を書き換えるのを忘れずに
@Mod(Nokocraft118.MODID)
public class Nokocraft118 {

    //LOG4Jの登録，Minecraftを起動したときに出てくるログにメッセージを出したりできる
    //便利な機能は色々あるけど普通にメッセージ送るなら正直System.out.print("")でもいい
    private static final Logger LOGGER = LogManager.getLogger();

    //MODIDは静的定数として宣言しておくと便利，他のところからMODIDで呼び出せるので変更時に楽
    public static final String MODID = "nokocraft118";

    //コンストラクタ
    //こういう感じの「クラス名(){}」みたいな部分は最初に必ず一回実行される部分，とだけ覚えておけばOK
    public Nokocraft118() {

        //登録用クラス
        //ここでまとめてアイテムとかエンティティとかを登録している
        Registration.init();

        //イベント登録に必要なやつ
        IEventBus modbus = FMLJavaModLoadingContext.get().getModEventBus();

        //ModSetupにModの初期化設定を色々まとめている
        modbus.addListener(ModSetup::init);
    }
}
