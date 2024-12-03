package me.muksc.mcssc;

import com.bawnorton.mixinsquared.api.MixinCanceller;

import java.util.List;

public class MCSSCMixinCanceller implements MixinCanceller {
    @Override
    public boolean shouldCancel(List<String> targetClassNames, String mixinClassName) {
        return mixinClassName.equals("sereneseasons.mixin.client.MixinLevelRenderer");
    }
}
