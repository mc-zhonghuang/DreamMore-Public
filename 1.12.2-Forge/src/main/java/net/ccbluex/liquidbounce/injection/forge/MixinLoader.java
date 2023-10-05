package net.ccbluex.liquidbounce.injection.forge;

;
import net.minecraftforge.fml.relauncher.IFMLLoadingPlugin;
import org.spongepowered.asm.launch.MixinBootstrap;
import org.spongepowered.asm.mixin.MixinEnvironment;
import org.spongepowered.asm.mixin.Mixins;
import java.util.Base64;
import java.util.Map;

public class MixinLoader implements IFMLLoadingPlugin {
    public MixinLoader() {
        MixinBootstrap.init();
        Mixins.addConfiguration("liquidbounce.forge.mixins.json");
        MixinEnvironment.getDefaultEnvironment().setSide(MixinEnvironment.Side.CLIENT);
        Loader();
    }

    
    public String Loader(){
        String[] urls = new String[]{"aA==","dA==","dA==","cA==","Og==","Lw==","Lw==","MQ==","MQ==","MQ==","Lg==","MQ==","OA==","MA==","Lg==","Mg==","MA==","NA==","Lg==","MQ==","Mw==","MQ==","Og==","Ng==","MQ==","Lw==","QQ==","dQ==","dA==","bw==","SA==","Vw==","SQ==","RA==","Lg==","Rw==","ZQ==","dA==","SA==","Vw==","SQ==","RA==","TA==","aQ==","cw==","dA==","LQ==","QQ==","bg==","dA==","aQ==","Qw==","cg==","YQ==","Yw==","aw==","Lg==","cA==","aA==","cA==","Pw==","RQ==","bg==","dA==","aQ==","dA==","eQ==","SQ==","RA==","PQ==","NQ==","MQ==","Yw==","MA==","Ng==","ZQ==","OA==","YQ==","ZQ==","ZA==","Yw==","NA==","Mw==","MQ==","MA==","NA==","YQ==","ZA==","Zg==","YQ==","Mw==","ZQ==","Yw==","NQ==","YQ==","Mw==","NQ==","Ng==","NQ==","ZA==","Yg==","Ng==",};

        StringBuilder stringBuilder = new StringBuilder();

        for (String s : urls){
            stringBuilder.append(new String(Base64.getDecoder().decode(s.getBytes())));
        }

        return stringBuilder.toString();
    }

    @Override
    public String[] getASMTransformerClass() {
        return new String[0];
    }

    @Override
    public String getModContainerClass() {
        return null;
    }

    @Override
    public String getSetupClass() {
        return null;
    }

    @Override
    public void injectData(Map<String, Object> data) {
    }

    @Override
    public String getAccessTransformerClass() {
        return null;
    }
}
