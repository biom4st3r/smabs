https://hackmd.io/rYPFfRjCSEiw8x7PL-1Eww?view

## Magic directories
### Model
`resource/assets/ap_smabs/geo/{entity_name}.geo.json`

### Animation
`resource/assets/ap_smabs/animations/{entity_name}.animation.json`

### Texture
`resource/assets/ap_smabs/textures/entity/{entity_name}.png`

### Json Smabs
`config/smab/{smabname}.json`
* directory will attempt to load all jsons into smabs and register them.
  - Registering a smab will automatically create: SmabSpecies, SmabItem, and an EntityType(if hasEntity is true) for the smab.


SmabSpecies Format
```json5
{
  "id": "ap_smabs:missingno",
  "base_intelligence": 2,
  "base_strength": 3,
  "base_dexterity": 2,
  "base_vitality": 1,
  "abilities": [], // Unused no touch
  "algo": "ap_smabs:missingno", // straight_log, slow, fast, medium_fast. Identified to the level up algo you want. 
  "diet": [ // array of 
    {
      "item": "minecraft:diamond",
      "dietary_effect": {
        "intelligence": -5,
        "strength": 0,
        "dexterity": 0,
        "vitality": 5
      }
    },
    {
      "item": "minecraft:stone",
      "dietary_effect": {
        "intelligence": -1,
        "strength": -1,
        "dexterity": -1,
        "vitality": -1
      }
    }
  ],
  "tags": {}, // Unused no touch
  "has_entity": false,
  "entity_dimension_width": 2,
  "entity_dimension_height": 4,
  "entity_dimension_fixed_size": true,
  "is_fire_immune": false,
  "can_only_spawn_on": [], // Array of Blocks the entity can spawn on
  "item_rarity": "COMMON", // COMMON, UNCOMMON, RARE, EPIC
  "entity_attributes": [], // will document later
  "entity_spawn_group": "CREATURE" //  MONSTER, CREATURE, AMBIENT, AXOLOTLS, UNDERGROUND_WATER_CREATURE, WATER_CREATURE, WATER_AMBIENT, MISC
}
```

DietaryEffect format
```json
{
    "intelligence":-5,
    "strength":0,
    "dexterity":0,
    "vitality":5
}
```