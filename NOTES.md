https://hackmd.io/rYPFfRjCSEiw8x7PL-1Eww?view


SmabSpecies Format
```json5
{
    "id":"ap_smabs:missingno",
    "base_intelligence":0,
    "base_strength":0,
    "base_dexterity":0,
    "base_vitality":0,
    "abilities":[ // Currently unused

    ],
    "algo":"ap_smabs:missingno", // Algorithm used to determine the exp needed to level up
    "diet":[
        {
            "TYPE":"net.minecraft.item.Item", // Must be this exact string
            "SET":[ // a list of items
                "minecraft:diamond"
            ]
        },
        {
            "TYPE":"adventurepack.mods.smab.smab.DietaryEffect", // Must be this exact string
            "LIST":[ // a list of Diertary effects
                {
                    "intelligence":-5,
                    "strength":0,
                    "dexterity":0,
                    "vitality":5
                }
            ]
        }
    ],
    "tags":{ // Currently unused

    }
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