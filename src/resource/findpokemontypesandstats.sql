-- name: pokemon-with-type-and-stats
-- Retrieves pokemon rows containing each type and stat.
SELECT PKMN_Data.Pokemon_Name, TYPE_Data.TYPE_NAME, PKMN_Data.Base_HP, PKMN_Data.Base_Atk, PKMN_Data.Base_Def, PKMN_Data.Base_SpA, PKMN_Data.Base_SpD, PKMN_Data.Base_Spe
FROM PKMN_Type
INNER JOIN PKMN_Data
ON PKMN_Type.SPECIES_ID = PKMN_Data.Pokemon_ID
INNER JOIN TYPE_Data
ON PKMN_Type.TYPE_ID = TYPE_Data.TYPE_ID
ORDER BY PKMN_Type.MATCHUP_ID ASC
