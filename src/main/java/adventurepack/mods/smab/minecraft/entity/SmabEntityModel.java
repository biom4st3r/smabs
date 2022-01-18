package adventurepack.mods.smab.minecraft.entity;

import adventurepack.mods.smab.smab.SmabSpecies;
import net.minecraft.util.Identifier;
import software.bernie.geckolib3.model.AnimatedGeoModel;

public class SmabEntityModel extends AnimatedGeoModel<SmabEntity> {
    @SuppressWarnings({"unused"})
    private final SmabSpecies smab;
    private final Identifier ANIMATION;
    private final Identifier MODEL;
    private final Identifier TEXTURE;

    public SmabEntityModel(SmabSpecies smab) {
        this.smab = smab;
        // ANIMATION = new Identifier(smab.id().getNamespace(), "animation/"+smab.id().getPath());
        // MODEL = new Identifier(smab.id().getNamespace(), "model/"+smab.id().getPath());
        // TEXTURE = new Identifier(smab.id().getNamespace(), "texture/"+smab.id().getPath());
        ANIMATION = new Identifier(smab.id().getNamespace(), String.format("animations/%s.geo.json", smab.id().getPath()));
        MODEL = new Identifier(smab.id().getNamespace(), String.format("geo/%s.geo.json", smab.id().getPath()));
        TEXTURE = new Identifier(smab.id().getNamespace(), String.format("textures/%s.geo.json", smab.id().getPath()));
    }

    @Override
    public Identifier getAnimationFileLocation(SmabEntity animatable) {
        return ANIMATION;
    }

    @Override
    public Identifier getModelLocation(SmabEntity object) {
        return MODEL;
    }

    @Override
    public Identifier getTextureLocation(SmabEntity object) {
        return TEXTURE;
    }
    
}
