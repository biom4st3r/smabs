package adventurepack.mods.smab.smab.attributes;

public record Mutation(float intelligence, float strength, float dexterity, float vitarlity) {
    public static Mutation create(long l) { // TODO
        float[] stats = {0,0,0,0};
        for (int i = 0; i < 4; i++) {
            switch ((int)l & 0xF) {
                case 0:
                stats[i] = 0.7F;
                break;
                case 1: // Fallthrough
                case 2:
                stats[i] = 0.9F;
                break;
                case 13: // Fallthrough   
                case 14:
                stats[i] = 1.1F;
                break;
                case 15:
                stats[i] = 1.3F;
                break;
                default:
                stats[i] = 1.0F;
            }
            l >>= 4;
        }
        
        return new Mutation(stats[0], stats[1], stats[2], stats[3]);
    }
}
