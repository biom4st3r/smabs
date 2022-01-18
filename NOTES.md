https://hackmd.io/rYPFfRjCSEiw8x7PL-1Eww?view

## Loading Smabs from json

`/config/smab/` directory will attempt to load all jsons into smabs and register them.

Registering a smab will automatically create: SmabSpecies, SmabItem, and an EntityType(if hasEntity is true) for the smab.


SmabSpecies Format
```json5
{
  "id": "ap_smabs:missingno",
  "base_intelligence": 0,
  "base_strength": 0,
  "base_dexterity": 0,
  "base_vitality": 0,
  "abilities": [], // Unused no touch
  "algo": "ap_smabs:missingno", // Identified to the level up algo you want. Currently only this one.
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
  "entity_dimension_width": 1,
  "entity_dimension_height": 1,
  "entity_dimension_fixed_size": true,
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