(ns poketype.core
  (:require [clojure.math.combinatorics :as combo ]
            [clojure.core.reducers :as r]
            [clojure.string :as str]
            [com.rpl.specter :as specter]
            [yesql.core :refer [defqueries]]
            )
  (:use clojure.pprint )
  )

(def type-index->keyword
  [:normal
   :fighting
   :flying
   :poison
   :ground
   :rock
   :bug
   :ghost
   :steel
   :fire
   :water
   :grass
   :electric
   :psychic
   :ice
   :dragon
   :dark
   :fairy
   ]
  )

(def keyword->type-index
  (into {}  (map (fn [x]
                   [x (.indexOf type-index->keyword x)] )
                 type-index->keyword))
  )

(def type-effectiveness-attack-defence
  [
   [1 1 1 1 1 1/2 1 0 1/2 1 1 1 1 1 1 1 1 1]
   [2 1 1/2 1/2 1 2 1/2 0 2 1 1 1 1 1/2 2 1 2 1/2]
   [1 2 1 1 1 1/2 2 1 1/2 1 1 2 1/2 1 1 1 1 1]
   [1 1 1 1/2 1/2 1/2 1 1/2 0 1 1 2 1 1 1 1 1 2]
   [1 1 0 2 1 2 1/2 1 2 2 1 1/2 2 1 1 1 1 1]
   [1 1/2 2 1 1/2 1 2 1 1/2 2 1 1 1 1 2 1 1 1]
   [1 1/2 1/2 1/2 1 1 1 1/2 1/2 1/2 1 2 1 2 1 1 2 1/2]
   [0 1 1 1 1 1 1 2 1 1 1 1 1 2 1 1 1/2 1]
   [1 1 1 1 1 2 1 1 1/2 1/2 1/2 1 1/2 1 2 1 1 2]
   [1 1 1 1 1 1/2 2 1 2 1/2 1/2 2 1 1 2 1/2 1 1]
   [1 1 1 1 2 2 1 1 1 2 1/2 1/2 1 1 1 1/2 1 1]
   [1 1 1/2 1/2 2 2 1/2 1 1/2 1/2 2 1/2 1 1 1 1/2 1 1]
   [1 1 2 1 0 1 1 1 1 1 2 1/2 1/2 1 1 1/2 1 1]
   [1 2 1 2 1 1 1 1 1/2 1 1 1 1 1/2 1 1 0 1]
   [1 1 2 1 2 1 1 1 1/2 1/2 1/2 2 1 1 1/2 2 1 1]
   [1 1 1 1 1 1 1 1 1/2 1 1 1 1 1 1 2 1 0]
   [1 1/2 1 1 1 1 1 2 1 1 1 1 1 2 1 1 1/2 1/2]
   [1 2 1 1/2 1 1 1 1 1/2 1/2 1 1 1 1 1 2 2 1 ]
   ]

  )

(def all-attack-types
  (range 0 (count (get type-effectiveness-attack-defence 0))))


(defn type-combos [amount-of-types-in-loadout]
  (combo/combinations all-attack-types amount-of-types-in-loadout))

(defn type-combos-vector [amount-of-types-in-loadout]
  (map (fn [x] (apply vector x)) (type-combos amount-of-types-in-loadout) )
  )

(defn transpose [m]
  (apply mapv vector m))

(def type-effectiveness-defence-attack
  (transpose type-effectiveness-attack-defence)
  )

(defn generate-dual-type-effectiveness-number-args [type1 type2]
  (map * (get type-effectiveness-defence-attack type1)
       (get type-effectiveness-defence-attack type2)
       ))

(defn generate-dual-type-effectiveness [type1 type2]
  (generate-dual-type-effectiveness-number-args
    (type1 keyword->type-index)
    (type2 keyword->type-index)
    ))

(def dual-type-combos
  {:normal [:fire
            :water
            :electric
            :grass
            :fighting
            :ground
            :flying
            :psychic
            :fairy]
   :fire [
          :water
          :electric
          :fighting
          :ground
          :flying
          :psychic
          :bug
          :rock
          :ghost
          :dragon
          :dark
          :steel
          ]
   :water [
           :electric
           :grass
           :ice
           :fighting
           :poison
           :ground
           :flying
           :psychic
           :bug
           :rock
           :ghost
           :dragon
           :dark
           :steel
           :fairy
           ]
   :electric [:grass
              :ice
              :ground
              :flying
              :bug
              :ghost
              :dragon
              :steel
              :fairy
              ]
   :grass [:ice
           :fighting
           :poison
           :ground
           :flying
           :psychic
           :bug
           :rock
           :ghost
           :dragon
           :dark
           :steel
           :fairy
           ]
   :ice   [:ground
           :flying
           :psychic
           :rock
           :ghost
           :dragon
           :dark
           ]
   :fighting [:poison
           :flying
           :psychic
           :bug
           :rock
           :dark
           :steel
           ]
   :poison [:ground
            :flying
            :bug
            :ghost
            :dragon
            :dark
            ]
   :ground [:flying
            :psychic
            :bug
            :rock
            :ghost
            :dragon
            :dark
            :steel
            ]
   :flying [:psychic
            :bug
            :rock
            :ghost
            :dragon
            :dark
            :steel
            :fairy
            ]
:psychic [:rock
          :ghost
          :dragon
          :dark
          :steel
          :fairy]
:bug     [:rock
          :ghost
          :steel]
:rock    [:dragon
          :dark
          :steel
          :fairy]
:ghost   [:dragon
          :dark
          :steel]
:dragon  [:dark
          :steel
          :fairy]
:dark    [:steel]
:steel   [:fairy]
}
)

(def type-effectiveness-dual-defence
  (map (fn [x]
         (map (partial generate-dual-type-effectiveness (key x))
              (val x)
              ))  dual-type-combos) )

(def type-effectiveness-attack-defence-dual
  (transpose
    (apply conj
           type-effectiveness-defence-attack
           (apply concat type-effectiveness-dual-defence)))
  )

(defn select-loadout [& type-vector]
  (map (fn [x] (get type-effectiveness-attack-defence-dual x)) type-vector)
  )

(defn loadout-max-effectiveness-array [& type-vector]
  (map (fn [x] (apply max x)) (transpose (apply select-loadout type-vector)))
  )

(defn loadout-total-effectiveness [max-arr]
  (apply + max-arr)
  )

(defn loadouts->key-val-loadouts-effectiveness-array [loadouts]
  (pmap (fn[x] (let [max-arr (apply loadout-max-effectiveness-array x)] [x  [max-arr [(apply min max-arr) (loadout-total-effectiveness max-arr) ]] ])) loadouts)
  )

(defn loadouts-map-string [loadoutsMapping]
  (map (fn[x] [
               (map type-index->keyword (get x 0))
               (get (get x 1) 1)
               ] ) loadoutsMapping)
  )

(defn highest-score [amount-of-types-in-loadout]
  (apply max (map #(get (get (get %1 1) 1) 1)
                  (loadouts->key-val-loadouts-effectiveness-array (type-combos-vector amount-of-types-in-loadout) )))
  )

(def highest-score-with-all-types
  (highest-score (count type-index->keyword)))

(defn score-cutoff [amount-of-types-in-loadout]
  (highest-score amount-of-types-in-loadout) )

(defn get-and-print-loadouts [amount-of-types-in-loadout]
  (binding [*print-right-margin* 100]
    (pprint (loadouts-map-string
              (take 5 (sort #(compare (get (get %2 1) 1) (get (get %1 1) 1))
                            (into []
                                  (loadouts->key-val-loadouts-effectiveness-array (type-combos-vector amount-of-types-in-loadout))))))))

  )

(defn recursive-1-to-n-loadout-checks [n]
  (if (> n 1)
    (recursive-1-to-n-loadout-checks (dec n))
    )(get-and-print-loadouts n))

(defn -main []
  (recursive-1-to-n-loadout-checks 18)
  )

;total stats, pokemon name, types.
(def pokemon-name-type-and-value-i-to-vi
'((318 "Bulbasaur" [:grass :poison])
  (405 "Ivysaur" [:grass :poison])
  (525 "Venusaur" [:grass :poison])
  (625 "Mega Venusaur" [:grass :poison])
  (309 "Charmander" [:fire])
  (405 "Charmeleon" [:fire])
  (534 "Charizard" [:fire :flying])
  (634 "Mega Charizard X" [:dragon :fire])
  (634 "Mega Charizard Y" [:fire :flying])
  (314 "Squirtle" [:water])
  (405 "Wartortle" [:water])
  (530 "Blastoise" [:water])
  (630 "Mega Blastoise" [:water])
  (195 "Caterpie" [:bug])
  (205 "Metapod" [:bug])
  (395 "Butterfree" [:bug :flying])
  (195 "Weedle" [:bug :poison])
  (205 "Kakuna" [:bug :poison])
  (395 "Beedrill" [:bug :poison])
  (495 "Mega Beedrill" [:bug :poison])
  (251 "Pidgey" [:flying :normal])
  (349 "Pidgeotto" [:flying :normal])
  (479 "Pidgeot" [:flying :normal])
  (579 "Mega Pidgeot" [:flying :normal])
  (253 "Rattata" [:normal])
  (413 "Raticate" [:normal])
  (262 "Spearow" [:flying :normal])
  (442 "Fearow" [:flying :normal])
  (288 "Ekans" [:poison])
  (438 "Arbok" [:poison])
  (320 "Pikachu" [:electric])
  (485 "Raichu" [:electric])
  (300 "Sandshrew" [:ground])
  (450 "Sandslash" [:ground])
  (275 "Nidoran-F" [:poison])
  (365 "Nidorana" [:poison])
  (505 "Nidoqueen" [:ground :poison])
  (273 "Nidoran-M" [:poison])
  (365 "Nidorino" [:poison])
  (505 "Nidoking" [:ground :poison])
  (323 "Clefairy" [:fairy])
  (483 "Clefable" [:fairy])
  (299 "Vulpix" [:fire])
  (505 "Ninetales" [:fire])
  (270 "Jigglypuff" [:fairy :normal])
  (435 "Wigglytuff" [:fairy :normal])
  (245 "Zubat" [:flying :poison])
  (455 "Golbat" [:flying :poison])
  (320 "Oddish" [:grass :poison])
  (395 "Gloom" [:grass :poison])
  (490 "Vileplume" [:grass :poison])
  (285 "Paras" [:bug :grass])
  (405 "Parasect" [:bug :grass])
  (305 "Venonat" [:bug :poison])
  (450 "Venomoth" [:bug :poison])
  (265 "Diglett" [:ground])
  (405 "Dugtrio" [:ground])
  (290 "Meowth" [:normal])
  (440 "Persian" [:normal])
  (320 "Psyduck" [:water])
  (500 "Golduck" [:water])
  (305 "Mankey" [:fighting])
  (455 "Primeape" [:fighting])
  (350 "Growlithe" [:fire])
  (555 "Arcanine" [:fire])
  (300 "Poliwag" [:water])
  (385 "Poliwhirl" [:water])
  (510 "Poliwrath" [:fighting :water])
  (310 "Abra" [:psychic])
  (400 "Kadabra" [:psychic])
  (500 "Alakazam" [:psychic])
  (590 "Mega Alakazam" [:psychic])
  (305 "Machop" [:fighting])
  (405 "Machoke" [:fighting])
  (505 "Machamp" [:fighting])
  (300 "Bellsprout" [:grass :poison])
  (390 "Weepinbell" [:grass :poison])
  (490 "Victreebel" [:grass :poison])
  (335 "Tentacool" [:poison :water])
  (515 "Tentacruel" [:poison :water])
  (300 "Geodude" [:ground :rock])
  (390 "Graveler" [:ground :rock])
  (495 "Golem" [:ground :rock])
  (410 "Ponyta" [:fire])
  (500 "Rapidash" [:fire])
  (315 "Slowpoke" [:psychic :water])
  (490 "Slowbro" [:psychic :water])
  (590 "Mega Slowbro" [:psychic :water])
  (325 "Magnemite" [:electric :steel])
  (465 "Magneton" [:electric :steel])
  (352 "Farfetch'd" [:flying :normal])
  (310 "Doduo" [:flying :normal])
  (460 "Dodrio" [:flying :normal])
  (325 "Seel" [:water])
  (475 "Dewgong" [:ice :water])
  (325 "Grimer" [:poison])
  (500 "Muk" [:poison])
  (305 "Shellder" [:water])
  (525 "Cloyster" [:ice :water])
  (310 "Gastly" [:ghost :poison])
  (405 "Haunter" [:ghost :poison])
  (500 "Gengar" [:ghost :poison])
  (600 "Mega Gengar" [:ghost :poison])
  (385 "Onix" [:ground :rock])
  (328 "Drowzee" [:psychic])
  (483 "Hypno" [:psychic])
  (325 "Krabby" [:water])
  (475 "Kingler" [:water])
  (330 "Voltorb" [:electric])
  (480 "Electrode" [:electric])
  (325 "Exeggcute" [:grass :psychic])
  (520 "Exeggcutor" [:grass :psychic])
  (320 "Cubone" [:ground])
  (425 "Marowak" [:ground])
  (455 "Hitmonlee" [:fighting])
  (455 "Hitmonchan" [:fighting])
  (385 "Lickitung" [:normal])
  (340 "Koffing" [:poison])
  (490 "Weezing" [:poison])
  (345 "Rhyhorn" [:ground :rock])
  (485 "Rhydon" [:ground :rock])
  (450 "Chansey" [:normal])
  (435 "Tangela" [:grass])
  (490 "Kangaskhan" [:normal])
  (590 "Mega Kangaskhan" [:normal])
  (295 "Horsea" [:water])
  (440 "Seadra" [:water])
  (320 "Goldeen" [:water])
  (450 "Seaking" [:water])
  (340 "Staryu" [:water])
  (520 "Starmie" [:psychic :water])
  (460 "Mr. Mime" [:fairy :psychic])
  (500 "Scyther" [:bug :flying])
  (455 "Jynx" [:ice :psychic])
  (490 "Electabuzz" [:electric])
  (495 "Magmar" [:fire])
  (500 "Pinsir" [:bug])
  (600 "Mega Pinsir" [:bug :flying])
  (490 "Tauros" [:normal])
  (200 "Magikarp" [:water])
  (540 "Gyarados" [:flying :water])
  (640 "Mega Gyarados" [:dark :water])
  (535 "Lapras" [:ice :water])
  (288 "Ditto" [:normal])
  (325 "Eevee" [:normal])
  (525 "Vaporeon" [:water])
  (525 "Jolteon" [:electric])
  (525 "Flareon" [:fire])
  (395 "Porygon" [:normal])
  (355 "Omanyte" [:rock :water])
  (495 "Omastar" [:rock :water])
  (355 "Kabuto" [:rock :water])
  (495 "Kabutops" [:rock :water])
  (515 "Aerodactyl" [:flying :rock])
  (615 "Mega Aerodactyl" [:flying :rock])
  (540 "Snorlax" [:normal])
  (580 "Articuno" [:flying :ice])
  (580 "Zapdos" [:electric :flying])
  (580 "Moltres" [:fire :flying])
  (300 "Dratini" [:dragon])
  (420 "Dragonair" [:dragon])
  (600 "Dragonite" [:dragon :flying])
  (680 "Mewtwo" [:psychic])
  (780 "Mega Mewtwo X" [:fighting :psychic])
  (780 "Mega Mewtwo Y" [:psychic])
  (600 "Mew" [:psychic])
  (318 "Chikorita" [:grass])
  (405 "Bayleef" [:grass])
  (525 "Meganium" [:grass])
  (309 "Cyndaquil" [:fire])
  (405 "Quilava" [:fire])
  (534 "Typhlosion" [:fire])
  (314 "Totodile" [:water])
  (405 "Croconaw" [:water])
  (530 "Feraligatr" [:water])
  (215 "Sentret" [:normal])
  (415 "Furret" [:normal])
  (262 "Hoothoot" [:flying :normal])
  (442 "Noctowl" [:flying :normal])
  (265 "Ledyba" [:bug :flying])
  (390 "Ledian" [:bug :flying])
  (250 "Spinarak" [:bug :poison])
  (390 "Ariados" [:bug :poison])
  (535 "Crobat" [:flying :poison])
  (330 "Chinchou" [:electric :water])
  (460 "Lanturn" [:electric :water])
  (205 "Pichu" [:electric])
  (218 "Cleffa" [:fairy])
  (210 "Igglybuff" [:fairy :normal])
  (245 "Togepi" [:fairy])
  (405 "Togetic" [:fairy :flying])
  (320 "Natu" [:flying :psychic])
  (470 "Xatu" [:flying :psychic])
  (280 "Mareep" [:electric])
  (365 "Flaaffy" [:electric])
  (510 "Ampharos" [:electric])
  (610 "Mega Ampharos" [:dragon :electric])
  (490 "Bellossom" [:grass])
  (250 "Marill" [:fairy :water])
  (420 "Azumarill" [:fairy :water])
  (410 "Sudowoodo" [:rock])
  (500 "Politoed" [:water])
  (250 "Hoppip" [:flying :grass])
  (340 "Skiploom" [:flying :grass])
  (460 "Jumpluff" [:flying :grass])
  (360 "Aipom" [:normal])
  (180 "Sunkern" [:grass])
  (425 "Sunflora" [:grass])
  (390 "Yanma" [:bug :flying])
  (210 "Wooper" [:ground :water])
  (430 "Quagsire" [:ground :water])
  (525 "Espeon" [:psychic])
  (525 "Umbreon" [:dark])
  (405 "Murkrow" [:dark :flying])
  (490 "Slowking" [:psychic :water])
  (435 "Misdreavus" [:ghost])
  (336 "Unown" [:psychic])
  (405 "Wobbuffet" [:psychic])
  (455 "Girafarig" [:normal :psychic])
  (290 "Pineco" [:bug])
  (465 "Forretress" [:bug :steel])
  (415 "Dunsparce" [:normal])
  (430 "Gligar" [:flying :ground])
  (510 "Steelix" [:ground :steel])
  (610 "Mega Steelix" [:ground :steel])
  (300 "Snubbull" [:fairy])
  (450 "Granbull" [:fairy])
  (430 "Qwilfish" [:poison :water])
  (500 "Scizor" [:bug :steel])
  (600 "Mega Scizor" [:bug :steel])
  (505 "Shuckle" [:bug :rock])
  (500 "Heracross" [:bug :fighting])
  (600 "Mega Heracross" [:bug :fighting])
  (430 "Sneasel" [:dark :ice])
  (330 "Teddiursa" [:normal])
  (500 "Ursaring" [:normal])
  (250 "Slugma" [:fire])
  (410 "Magcargo" [:fire :rock])
  (250 "Swinub" [:ground :ice])
  (450 "Piloswine" [:ground :ice])
  (380 "Corsola" [:rock :water])
  (300 "Remoraid" [:water])
  (480 "Octillery" [:water])
  (330 "Delibird" [:flying :ice])
  (465 "Mantine" [:flying :water])
  (465 "Skarmory" [:flying :steel])
  (330 "Houndour" [:dark :fire])
  (500 "Houndoom" [:dark :fire])
  (600 "Mega Houndoom" [:dark :fire])
  (540 "Kingdra" [:dragon :water])
  (330 "Phanpy" [:ground])
  (500 "Donphan" [:ground])
  (515 "Porygon2" [:normal])
  (465 "Stantler" [:normal])
  (250 "Smeargle" [:normal])
  (210 "Tyrogue" [:fighting])
  (455 "Hitmontop" [:fighting])
  (305 "Smoochum" [:ice :psychic])
  (360 "Elekid" [:electric])
  (365 "Magby" [:fire])
  (490 "Miltank" [:normal])
  (540 "Blissey" [:normal])
  (580 "Raikou" [:electric])
  (580 "Entei" [:grass])
  (580 "Suicune" [:water])
  (300 "Larvitar" [:ground :rock])
  (410 "Pupitar" [:ground :rock])
  (600 "Tyranitar" [:dark :rock])
  (700 "Mega Tyranitar" [:dark :rock])
  (680 "Lugia" [:flying :psychic])
  (680 "Ho-Oh" [:fire :flying])
  (600 "Celebi" [:grass :psychic])
  (310 "Treecko" [:grass])
  (405 "Grovyle" [:grass])
  (530 "Sceptile" [:grass])
  (630 "Mega Sceptile" [:dragon :grass])
  (310 "Torchic" [:fire])
  (405 "Combusken" [:fighting :fire])
  (530 "Blaziken" [:fighting :fire])
  (630 "Mega Blaziken" [:fighting :fire])
  (310 "Mudkip" [:water])
  (405 "Marshtomp" [:ground :water])
  (535 "Swampert" [:ground :water])
  (635 "Mega Swampert" [:ground :water])
  (220 "Poochyena" [:dark])
  (420 "Mightyena" [:dark])
  (240 "Zigzagoon" [:normal])
  (420 "Linoone" [:normal])
  (195 "Wurmple" [:bug])
  (205 "Silcoon" [:bug])
  (395 "Beautifly" [:bug :flying])
  (205 "Cascoon" [:bug])
  (385 "Dustox" [:bug :poison])
  (220 "Lotad" [:grass :water])
  (340 "Lombre" [:grass :water])
  (480 "Ludicolo" [:grass :water])
  (220 "Seedot" [:grass])
  (340 "Nuzleaf" [:dark :grass])
  (480 "Shiftry" [:dark :grass])
  (270 "Taillow" [:flying :normal])
  (430 "Swellow" [:flying :normal])
  (270 "Wingull" [:flying :water])
  (430 "Pelipper" [:flying :water])
  (198 "Ralts" [:fairy :psychic])
  (278 "Kirlia" [:fairy :psychic])
  (518 "Gardevoir" [:fairy :psychic])
  (618 "Mega Gardevoir" [:fairy :psychic])
  (269 "Surskit" [:bug :water])
  (414 "Masquerain" [:bug :flying])
  (295 "Shroomish" [:grass])
  (460 "Breloom" [:fighting :grass])
  (280 "Slakoth" [:normal])
  (440 "Vigoroth" [:normal])
  (670 "Slaking" [:normal])
  (266 "Nincada" [:bug :ground])
  (456 "Ninjask" [:bug :flying])
  (236 "Shedinja" [:bug :ghost])
  (240 "Whismur" [:normal])
  (360 "Loudred" [:normal])
  (490 "Exploud" [:normal])
  (237 "Makuhita" [:fighting])
  (474 "Hariyama" [:fighting])
  (190 "Azurill" [:fairy :normal])
  (375 "Nosepass" [:rock])
  (260 "Skitty" [:normal])
  (380 "Delcatty" [:normal])
  (380 "Sableye" [:dark :ghost])
  (480 "Mega Sableye" [:dark :ghost])
  (380 "Mawile" [:fairy :steel])
  (480 "Mega Mawile" [:fairy :steel])
  (330 "Aron" [:rock :steel])
  (430 "Lairon" [:rock :steel])
  (530 "Aggron" [:rock :steel])
  (630 "Mega Aggron" [:steel])
  (280 "Meditite" [:fighting :psychic])
  (410 "Medicham" [:fighting :psychic])
  (510 "Mega Medicham" [:fighting :psychic])
  (295 "Electrike" [:electric])
  (475 "Manectric" [:electric])
  (575 "Mega Manectric" [:electric])
  (405 "Plusle" [:electric])
  (405 "Minun" [:electric])
  (400 "Volbeat" [:bug])
  (400 "Illumise" [:bug])
  (400 "Roselia" [:grass :poison])
  (302 "Gulpin" [:poison])
  (467 "Swalot" [:poison])
  (305 "Carvanha" [:dark :water])
  (460 "Sharpedo" [:dark :water])
  (560 "Mega Sharpedo" [:dark :water])
  (400 "Wailmer" [:water])
  (500 "Wailord" [:water])
  (305 "Numel" [:fire :ground])
  (460 "Camerupt" [:fire :ground])
  (560 "Mega Camerupt" [:fire :ground])
  (470 "Torkoal" [:fire])
  (330 "Spoink" [:psychic])
  (470 "Grumpig" [:psychic])
  (360 "Spinda" [:normal])
  (290 "Trapinch" [:ground])
  (340 "Vibrava" [:dragon :ground])
  (520 "Flygon" [:dragon :ground])
  (335 "Cacnea" [:grass])
  (475 "Cacturne" [:dark :grass])
  (310 "Swablu" [:flying :normal])
  (490 "Altaria" [:dragon :flying])
  (590 "Mega Altaria" [:dragon :fairy])
  (458 "Zangoose" [:normal])
  (458 "Seviper" [:poison])
  (440 "Lunatone" [:psychic :rock])
  (440 "Solrock" [:psychic :rock])
  (288 "Barboach" [:ground :water])
  (468 "Whiscash" [:ground :water])
  (308 "Corphish" [:water])
  (468 "Crawdaunt" [:dark :water])
  (300 "Baltoy" [:ground :psychic])
  (500 "Claydol" [:ground :psychic])
  (355 "Lileep" [:grass :rock])
  (495 "Cradily" [:grass :rock])
  (355 "Anorith" [:bug :rock])
  (495 "Armaldo" [:bug :rock])
  (200 "Feebas" [:water])
  (540 "Milotic" [:water])
  (420 "Castform" [:normal])
  (420 "Castform (Rain)" [:water])
  (420 "Castform (Sun)" [:fire])
  (420 "Castform (Hail)" [:ice])
  (440 "Kecleon" [:normal])
  (295 "Shuppet" [:ghost])
  (455 "Banette" [:ghost])
  (555 "Mega Banette" [:ghost])
  (295 "Duskull" [:ghost])
  (455 "Dusclops" [:ghost])
  (460 "Tropius" [:flying :grass])
  (425 "Chimecho" [:psychic])
  (465 "Absol" [:dark])
  (565 "Mega Absol" [:dark])
  (260 "Wynaut" [:psychic])
  (300 "Snorunt" [:ice])
  (480 "Glalie" [:ice])
  (580 "Mega Glalie" [:ice])
  (290 "Spheal" [:ice :water])
  (410 "Sealeo" [:ice :water])
  (530 "Walrein" [:ice :water])
  (345 "Clamperl" [:water])
  (485 "Huntail" [:water])
  (485 "Gorebyss" [:water])
  (485 "Relicanth" [:rock :water])
  (330 "Luvdisc" [:water])
  (300 "Bagon" [:dragon])
  (420 "Shelgon" [:dragon])
  (600 "Salamence" [:dragon :flying])
  (700 "Mega Salamence" [:dragon :flying])
  (300 "Beldum" [:psychic :steel])
  (420 "Metang" [:psychic :steel])
  (600 "Metagross" [:psychic :steel])
  (700 "Mega Metagross" [:psychic :steel])
  (580 "Regirock" [:rock])
  (580 "Regice" [:ice])
  (580 "Registeel" [:steel])
  (600 "Latias" [:dragon :psychic])
  (700 "Mega Latias" [:dragon :psychic])
  (600 "Latios" [:dragon :psychic])
  (700 "Mega Latios" [:dragon :psychic])
  (670 "Kyogre" [:water])
  (770 "Primal Kyogre" [:water])
  (670 "Groudon" [:ground])
  (770 "Primal Groudon" [:fire :ground])
  (680 "Rayquaza" [:dragon :flying])
  (780 "Mega Rayquaza" [:dragon :flying])
  (600 "Jirachi" [:psychic])
  (600 "Deoxys" [:psychic])
  (600 "Deoxys (Attack)" [:psychic])
  (600 "Deoxys (Defense)" [:psychic])
  (600 "Deoxys (Speed)" [:psychic])
  (318 "Turtwig" [:grass])
  (405 "Grotle" [:grass])
  (525 "Torterra" [:grass :ground])
  (309 "Chimchar" [:fire])
  (405 "Monferno" [:fighting :fire])
  (534 "Infernape" [:fighting :fire])
  (314 "Piplup" [:water])
  (405 "Prinplup" [:water])
  (530 "Empoleon" [:steel :water])
  (245 "Starly" [:flying :normal])
  (340 "Staravia" [:flying :normal])
  (485 "Staraptor" [:flying :normal])
  (250 "Bidoof" [:normal])
  (410 "Bibarel" [:normal :water])
  (194 "Kricketot" [:bug])
  (384 "Kricketune" [:bug])
  (263 "Shinx" [:electric])
  (363 "Luxio" [:electric])
  (523 "Luxray" [:electric])
  (280 "Budew" [:grass :poison])
  (515 "Roserade" [:grass :poison])
  (350 "Cranidos" [:rock])
  (495 "Rampardos" [:rock])
  (350 "Shieldon" [:rock :steel])
  (495 "Bastiodon" [:rock :steel])
  (224 "Burmy" [:bug])
  (424 "Wormadam (Plant)" [:bug :grass])
  (424 "Wormadam (Sandy)" [:bug :ground])
  (424 "Wormadam (Trash)" [:bug :steel])
  (424 "Mothim" [:bug :flying])
  (244 "Combee" [:bug :flying])
  (474 "Vespiquen" [:bug :flying])
  (405 "Pachirisu" [:electric])
  (330 "Buizel" [:water])
  (495 "Floatzel" [:water])
  (275 "Cherubi" [:grass])
  (450 "Cherrim" [:grass])
  (325 "Shellos" [:water])
  (475 "Gastrodon" [:ground :water])
  (482 "Ambipom" [:normal])
  (348 "Drifloon" [:flying :ghost])
  (498 "Drifblim" [:flying :ghost])
  (350 "Buneary" [:normal])
  (480 "Lopunny" [:normal])
  (580 "Mega Lopunny" [:fighting :normal])
  (495 "Mismagius" [:ghost])
  (505 "Honchkrow" [:dark :flying])
  (310 "Glameow" [:normal])
  (452 "Purugly" [:normal])
  (285 "Chingling" [:psychic])
  (329 "Stunky" [:dark :poison])
  (479 "Skuntank" [:dark :poison])
  (300 "Bronzor" [:psychic :steel])
  (500 "Bronzong" [:psychic :steel])
  (290 "Bonsly" [:rock])
  (310 "Mime Jr." [:fairy :psychic])
  (220 "Happiny" [:normal])
  (411 "Chatot" [:flying :normal])
  (485 "Spiritomb" [:dark :ghost])
  (300 "Gible" [:dragon :ground])
  (410 "Gabite" [:dragon :ground])
  (600 "Garchomp" [:dragon :ground])
  (700 "Mega Garchomp" [:dragon :ground])
  (390 "Munchlax" [:normal])
  (285 "Riolu" [:fighting])
  (525 "Lucario" [:fighting :steel])
  (625 "Mega Lucario" [:fighting :steel])
  (330 "Hippopotas" [:ground])
  (525 "Hippowdon" [:ground])
  (330 "Skorupi" [:bug :poison])
  (500 "Drapion" [:dark :poison])
  (300 "Croagunk" [:fighting :poison])
  (490 "Toxicroak" [:fighting :poison])
  (454 "Carnivine" [:grass])
  (330 "Finneon" [:water])
  (460 "Lumineon" [:water])
  (345 "Mantyke" [:flying :water])
  (334 "Snover" [:grass :ice])
  (494 "Abomasnow" [:grass :ice])
  (596 "Mega Abomasnow" [:grass :ice])
  (510 "Weavile" [:dark :ice])
  (535 "Magnezone" [:electric :steel])
  (515 "Lickilicky" [:normal])
  (535 "Rhyperior" [:ground :rock])
  (535 "Tangrowth" [:grass])
  (540 "Electivire" [:electric])
  (540 "Magmortar" [:fire])
  (545 "Togekiss" [:fairy :flying])
  (515 "Yanmega" [:bug :flying])
  (525 "Leafeon" [:grass])
  (525 "Glaceon" [:ice])
  (510 "Gliscor" [:flying :ground])
  (530 "Mamoswine" [:ground :ice])
  (535 "Porygon-Z" [:normal])
  (518 "Gallade" [:fighting :psychic])
  (618 "Mega Gallade" [:fighting :psychic])
  (525 "Probopass" [:rock :steel])
  (525 "Dusknoir" [:ghost])
  (480 "Froslass" [:ghost :ice])
  (440 "Rotom" [:electric :ghost])
  (520 "Rotom (Heat)" [:electric :fire])
  (520 "Rotom (Wash)" [:electric :water])
  (520 "Rotom (Frost)" [:electric :ice])
  (520 "Rotom (Fan)" [:electric :flying])
  (520 "Rotom (Mow)" [:electric :grass])
  (580 "Uxie" [:psychic])
  (580 "Mesprit" [:psychic])
  (580 "Azelf" [:psychic])
  (680 "Dialga" [:dragon :steel])
  (680 "Palkia" [:dragon :water])
  (600 "Heatran" [:fire :steel])
  (670 "Regigigas" [:normal])
  (680 "Giratina" [:dragon :ghost])
  (680 "Giratina (Origin)" [:dragon :ghost])
  (600 "Cresselia" [:psychic])
  (480 "Phione" [:water])
  (600 "Manaphy" [:water])
  (600 "Darkrai" [:dark])
  (600 "Shaymin" [:grass])
  (600 "Shaymin (Sky)" [:flying :grass])
  (720 "Arceus" [:normal])
  (600 "Victini" [:fire :psychic])
  (308 "Snivy" [:grass])
  (413 "Servine" [:grass])
  (528 "Serperior" [:grass])
  (308 "Tepig" [:fire])
  (418 "Pignite" [:fighting :fire])
  (528 "Emboar" [:fighting :fire])
  (308 "Oshawott" [:water])
  (413 "Dewott" [:water])
  (528 "Samurott" [:water])
  (255 "Patrat" [:normal])
  (420 "Watchog" [:normal])
  (275 "Lillipup" [:normal])
  (370 "Herdier" [:normal])
  (500 "Stoutland" [:normal])
  (281 "Purrloin" [:dark])
  (446 "Liepard" [:dark])
  (316 "Pansage" [:grass])
  (498 "Simisage" [:grass])
  (316 "Pansear" [:fire])
  (498 "Simisear" [:fire])
  (316 "Panpour" [:water])
  (498 "Simipour" [:water])
  (292 "Munna" [:psychic])
  (487 "Musharna" [:psychic])
  (264 "Pidove" [:flying :normal])
  (358 "Tranquill" [:flying :normal])
  (488 "Unfezant" [:flying :normal])
  (295 "Blitzle" [:electric])
  (497 "Zebstrika" [:electric])
  (280 "Roggenrola" [:rock])
  (390 "Boldore" [:rock])
  (515 "Gigalith" [:rock])
  (313 "Woobat" [:flying :psychic])
  (425 "Swoobat" [:flying :psychic])
  (328 "Drilbur" [:ground])
  (508 "Excadrill" [:ground :steel])
  (445 "Audino" [:normal])
  (545 "Mega Audino" [:fairy :normal])
  (305 "Timburr" [:fighting])
  (405 "Gurdurr" [:fighting])
  (505 "Conkeldurr" [:fighting])
  (294 "Tympole" [:water])
  (384 "Palpitoad" [:ground :water])
  (509 "Seismitoad" [:ground :water])
  (465 "Throh" [:fighting])
  (465 "Sawk" [:fighting])
  (310 "Sewaddle" [:bug :grass])
  (380 "Swadloon" [:bug :grass])
  (500 "Leavanny" [:bug :grass])
  (260 "Venipede" [:bug :poison])
  (360 "Whirlipede" [:bug :poison])
  (485 "Scolipede" [:bug :poison])
  (280 "Cottonee" [:fairy :grass])
  (480 "Whimsicott" [:fairy :grass])
  (280 "Petilil" [:grass])
  (480 "Lilligant" [:grass])
  (460 "Basculin" [:water])
  (292 "Sandile" [:dark :ground])
  (351 "Krokorok" [:dark :ground])
  (519 "Krookodile" [:dark :ground])
  (315 "Darumaka" [:fire])
  (480 "Darmanitan" [:fire])
  (540 "Darmanitan (Zen)" [:fire :psychic])
  (461 "Maractus" [:grass])
  (325 "Dwebble" [:bug :rock])
  (475 "Crustle" [:bug :rock])
  (348 "Scraggy" [:dark :fighting])
  (488 "Scrafty" [:dark :fighting])
  (490 "Sigilyph" [:flying :psychic])
  (303 "Yamask" [:ghost])
  (483 "Cofagrigus" [:ghost])
  (355 "Tirtouga" [:rock :water])
  (495 "Carracosta" [:rock :water])
  (401 "Archen" [:flying :rock])
  (567 "Archeops" [:flying :rock])
  (359 "Trubbish" [:poison])
  (474 "Garbodor" [:poison])
  (330 "Zorua" [:dark])
  (510 "Zoroark" [:dark])
  (300 "Minccino" [:normal])
  (470 "Cinccino" [:normal])
  (290 "Gothita" [:psychic])
  (390 "Gothorita" [:psychic])
  (490 "Gothitelle" [:psychic])
  (290 "Solosis" [:psychic])
  (370 "Duosion" [:psychic])
  (490 "Reuniclus" [:psychic])
  (305 "Ducklett" [:flying :water])
  (473 "Swanna" [:flying :water])
  (305 "Vanillite" [:ice])
  (395 "Vanillish" [:ice])
  (535 "Vanilluxe" [:ice])
  (335 "Deerling" [:grass :normal])
  (475 "Sawsbuck" [:grass :normal])
  (428 "Emolga" [:electric :flying])
  (315 "Karrablast" [:bug])
  (495 "Escavalier" [:bug :steel])
  (294 "Foongus" [:grass :poison])
  (464 "Amoonguss" [:grass :poison])
  (335 "Frillish" [:ghost :water])
  (480 "Jellicent" [:ghost :water])
  (470 "Alomomola" [:water])
  (319 "Joltik" [:bug :electric])
  (472 "Galvantula" [:bug :electric])
  (305 "Ferroseed" [:grass :steel])
  (489 "Ferrothorn" [:grass :steel])
  (300 "Klink" [:steel])
  (440 "Klang" [:steel])
  (520 "Klinklang" [:steel])
  (275 "Tynamo" [:electric])
  (405 "Eelektrik" [:electric])
  (515 "Eelektross" [:electric])
  (335 "Elgyem" [:psychic])
  (485 "Beheeyem" [:psychic])
  (275 "Litwick" [:fire :ghost])
  (370 "Lampent" [:fire :ghost])
  (520 "Chandelure" [:fire :ghost])
  (320 "Axew" [:dragon])
  (410 "Fraxure" [:dragon])
  (540 "Haxorus" [:dragon])
  (305 "Cubchoo" [:ice])
  (485 "Beartic" [:ice])
  (485 "Cryogonal" [:ice])
  (305 "Shelmet" [:bug])
  (495 "Accelgor" [:bug])
  (471 "Stunfisk" [:electric :ground])
  (350 "Mienfoo" [:fighting])
  (510 "Mienshao" [:fighting])
  (485 "Druddigon" [:dragon])
  (303 "Golett" [:ghost :ground])
  (483 "Golurk" [:ghost :ground])
  (340 "Pawniard" [:dark :steel])
  (490 "Bisharp" [:dark :steel])
  (490 "Bouffalant" [:normal])
  (350 "Rufflet" [:flying :normal])
  (510 "Braviary" [:flying :normal])
  (370 "Vullaby" [:dark :flying])
  (510 "Mandibuzz" [:dark :flying])
  (484 "Heatmor" [:fire])
  (484 "Durant" [:bug :steel])
  (295 "Deino" [:dark :dragon])
  (420 "Zweilous" [:dark :dragon])
  (600 "Hydreigon" [:dark :dragon])
  (360 "Larvesta" [:bug :fire])
  (550 "Volcarona" [:bug :fire])
  (580 "Cobalion" [:fighting :steel])
  (580 "Terrakion" [:fighting :rock])
  (580 "Virizion" [:fighting :grass])
  (580 "Tornadus" [:flying])
  (580 "Tornadus (Therian)" [:flying])
  (580 "Thundurus" [:electric :flying])
  (580 "Thundurus (Therian)" [:electric :flying])
  (680 "Reshiram" [:dragon :fire])
  (680 "Zekrom" [:dragon :electric])
  (600 "Landorus" [:flying :ground])
  (600 "Landorus (Therian)" [:flying :ground])
  (660 "Kyurem" [:dragon :ice])
  (700 "Kyurem (Black)" [:dragon :ice])
  (700 "Kyurem (White)" [:dragon :ice])
  (580 "Keldeo" [:fighting :water])
  (600 "Meloetta" [:normal :psychic])
  (600 "Meloetta (Pirouette)" [:fighting :normal])
  (600 "Genesect" [:bug :steel])
  (313 "Chespin" [:grass])
  (405 "Quilladin" [:grass])
  (530 "Chesnaught" [:fighting :grass])
  (307 "Fennekin" [:fire])
  (409 "Braixen" [:fire])
  (534 "Delphox" [:fire :psychic])
  (314 "Froakie" [:water])
  (405 "Frogadier" [:water])
  (530 "Greninja" [:dark :water])
  (237 "Bunnelby" [:normal])
  (423 "Diggersby" [:ground :normal])
  (278 "Fletchling" [:flying :normal])
  (382 "Fletchinder" [:fire :flying])
  (499 "Talonflame" [:fire :flying])
  (200 "Scatterbug" [:bug])
  (213 "Spewpa" [:bug])
  (411 "Vivillon" [:bug :flying])
  (369 "Litleo" [:fire :normal])
  (507 "Pyroar" [:fire :normal])
  (303 "Flabébé" [:fairy])
  (371 "Floette" [:fairy])
  (551 "Floette (Eternal)" [:fairy])
  (552 "Florges" [:fairy])
  (350 "Skiddo" [:grass])
  (531 "Gogoat" [:grass])
  (348 "Pancham" [:fighting])
  (495 "Pangoro" [:dark :fighting])
  (472 "Furfrou" [:normal])
  (355 "Espurr" [:psychic])
  (466 "Meowstic" [:psychic])
  (466 "Meowstic Female" [:psychic])
  (325 "Honedge" [:ghost :steel])
  (448 "Doublade" [:ghost :steel])
  (520 "Aegislash" [:ghost :steel])
  (520 "Aegislash (Blade)" [:ghost :steel])
  (341 "Spritzee" [:fairy])
  (462 "Aromatisse" [:fairy])
  (341 "Swirlix" [:fairy])
  (480 "Slurpuff" [:fairy])
  (288 "Inkay" [:dark :psychic])
  (482 "Malamar" [:dark :psychic])
  (306 "Binacle" [:rock :water])
  (500 "Barbaracle" [:rock :water])
  (320 "Skrelp" [:poison :water])
  (494 "Dragalge" [:dragon :poison])
  (330 "Clauncher" [:water])
  (500 "Clawitzer" [:water])
  (289 "Helioptile" [:electric :normal])
  (481 "Heliolisk" [:electric :normal])
  (362 "Tyrunt" [:dragon :rock])
  (521 "Tyrantrum" [:dragon :rock])
  (362 "Amaura" [:ice :rock])
  (521 "Aurorus" [:ice :rock])
  (525 "Sylveon" [:fairy])
  (500 "Hawlucha" [:fighting :flying])
  (431 "Dedenne" [:electric :fairy])
  (500 "Carbink" [:fairy :rock])
  (300 "Goomy" [:dragon])
  (452 "Sliggoo" [:dragon])
  (600 "Goodra" [:dragon])
  (470 "Klefki" [:fairy :steel])
  (309 "Phantump" [:ghost :grass])
  (474 "Trevenant" [:ghost :grass])
  (335 "Pumpkaboo" [:ghost :grass])
  (335 "Pumpkaboo (Small)" [:ghost :grass])
  (335 "Pumpkaboo (Large)" [:ghost :grass])
  (335 "Pumpkaboo (Super)" [:ghost :grass])
  (494 "Gourgeist" [:ghost :grass])
  (494 "Gourgeist (Small)" [:ghost :grass])
  (494 "Gourgeist (Large)" [:ghost :grass])
  (494 "Gourgeist (Super)" [:ghost :grass])
  (304 "Bergmite" [:ice])
  (514 "Avalugg" [:ice])
  (245 "Noibat" [:dragon :flying])
  (535 "Noivern" [:dragon :flying])
  (680 "Xerneas" [:fairy])
  (680 "Yveltal" [:dark :flying])
  (600 "Zygarde" [:dragon :ground])
  (600 "Diancie" [:fairy :rock])
  (700 "Mega Diancie" [:fairy :rock])
  (600 "Hoopa" [:ghost :psychic])
  (680 "Hoopa (Unbound)" [:dark :psychic])
  (600 "Volcanion" [:fire :water]))
  )

;total stats, pokemon name, types.
(def viipokemon
'(
(320 "Rowlet"[:grass,:flying])
(420 "Dartrix"[:grass,:flying])
(530 "Decidueye"[:grass,:ghost])
(530 "Incineroar"[:fire,:dark])
(530 "Primarina"[:water,:fairy])
(265 "Pikipek"[:normal,:flying])
(355 "Trumbeak"[:normal,:flying])
(485 "Toucannon"[:normal,:flying])
(400 "Charjabug"[:bug,:electric])
(500 "Vikavolt"[:bug,:electric])
(478 "Crabominable"[:fighting,:ice])
(476 "Oricorio"[:fire,:flying])
(304 "Cutiefly"[:bug,:fairy])
(464 "Ribombee"[:bug,:fairy])
(305 "Mareanie"[:poison,:water])
(495 "Toxapex"[:poison,:water])
(269 "Dewpider"[:water,:bug])
(454 "Araquanid"[:water,:bug])
(285 "Morelull"[:grass,:fairy])
(405 "Shiinotic"[:grass,:fairy])
(320 "Salandit"[:poison,:fire])
(480 "Salazzle"[:poison,:fire])
(340 "Stufful"[:normal,:fighting])
(500 "Bewear"[:normal,:fighting])
(490 "Oranguru"[:normal,:psychic])
(230 "Wimpod"[:bug,:water])
(530 "Golisopod"[:bug,:water])
(320 "Sandygast"[:ghost,:ground])
(480 "Palossand"[:ghost,:ground])
(440 "Minior (Meteor Form)"[:rock,:flying])
(500 "Minior (Core)"[:rock,:flying])
(485 "Turtonator"[:fire,:dragon])
(435 "Togedemaru"[:electric,:steel])
(476 "Mimikyu"[:ghost,:fairy])
(475 "Bruxish"[:water,:psychic])
(485 "Drampa"[:normal,:dragon])
(517 "Dhelmise"[:ghost,:grass])
(420 "Hakamo-o"[:dragon,:fighting])
(600 "Kommo-o"[:dragon,:fighting])
(570 "Tapu Koko"[:electric,:fairy])
(570 "Tapu Lele"[:psychic,:fairy])
(570 "Tapu Bulu"[:grass,:fairy])
(570 "Tapu Fini"[:water,:fairy])
(680 "Solgaleo"[:psychic,:steel])
(680 "Lunala"[:psychic,:ghost])
(570 "Nihilego"[:rock,:poison])
(570 "Buzzwole"[:bug,:fighting])
(570 "Pheromosa"[:bug,:fighting])
(570 "Celesteela"[:steel,:flying])
(570 "Kartana"[:grass,:steel])
(570 "Guzzlord"[:dark,:dragon])
(600 "Magearna"[:steel,:fairy])
(600 "Marshadow"[:fighting,:ghost])
(320 "Litten"[:fire])
(420 "Torracat"[:fire])
(320 "Popplio"[:water])
(420 "Brionne"[:water])
(253 "Yungoos"[:normal])
(418 "Gumshoos"[:normal])
(300 "Grubbin"[:bug])
(338 "Crabrawler"[:fighting])
(280 "Rockruff"[:rock])
(487 "Lycanroc (Midday Form)"[:rock])
(487 "Lycanroc (Midnight Form)"[:rock])
(175 "Wishiwashi (Solo Form)"[:water])
(620 "Wishiwashi (School Form)"[:water])
(385 "Mudbray"[:ground])
(500 "Mudsdale"[:ground])
(250 "Fomantis"[:grass])
(480 "Lurantis"[:grass])
(210 "Bounsweet"[:grass])
(290 "Steenee"[:grass])
(510 "Tsareena"[:grass])
(485 "Comfey"[:fairy])
(490 "Passimian"[:fighting])
(410 "Pyukumuku"[:water])
(534 "Type: Null"[:normal])
(570 "Silvally"[:normal])
(480 "Komala"[:normal])
(300 "Jangmo-o"[:dragon])
(200 "Cosmog"[:psychic])
(400 "Cosmoem"[:psychic])
(570 "Xurkitree"[:electric])
(600 "Necrozma"[:psychic])))

(def pokemon-name-type-and-value
  (concat pokemon-name-type-and-value-i-to-vi viipokemon)
  )

(defn remove-a-pokemon [pokemon-seq pokemon]
  (filter (fn[x] (not (.contains (nth x 1) pokemon))) pokemon-seq))

(defn filter-pokemon-seq[names]
  (loop [names names
         pokemon-seq pokemon-name-type-and-value]
    (if (empty? names)
      pokemon-seq
      (recur (pop names)
             (remove-a-pokemon pokemon-seq (peek names)) ))))

(defn retrieve-a-pokemon [seq pokemon]
  (filter (fn[x] (.contains (nth x 1) pokemon)) seq)
)

(defn retrieve-pokemon-seq[names]
  (loop [names names pokemon-seq '()]
    (if (empty? names)
      pokemon-seq
      (recur (pop names)
             (concat (retrieve-a-pokemon pokemon-name-type-and-value (peek names))
                     pokemon-seq) ))))

(defn sort-pokemon-by-value [pokemon]
    (sort-by first > pokemon))


(defn sort-pokemon-by-type [pokemon]
    (sort-by last pokemon))

(def partition-pokemon-into-types
  (partition-by last (sort-pokemon-by-type pokemon-name-type-and-value)))

(def best-pokemon-for-each-type
   (map #(first (sort-pokemon-by-value %1)) partition-pokemon-into-types)
)

(def sorted-best-pokemon-for-each-type
  (sort-pokemon-by-value best-pokemon-for-each-type))

(defn sorted-best-pokemon-of-pred [pred]
  (filter pred sorted-best-pokemon-for-each-type)
  )

(defn single-type-search-pred [type]
  #(some (fn[x](= type x)) (last %))
  )

(defn sorted-best-pokemon-of-specific-type [type]
  (first (sorted-best-pokemon-of-pred (single-type-search-pred type)))
  )

(defn sorted-best-pokemon-of-specific-types [type1 type2]
  (first (sorted-best-pokemon-of-pred (every-pred (single-type-search-pred type1)
                                           (single-type-search-pred type2))
  ))
  )

(defn sorted-best-pokemon-for-specified-combo-search [types]
  (remove nil? (map #(apply sorted-best-pokemon-of-specific-types %) (combo/combinations types 2)))
  )

(defn evaluate-real-pokemon-combination [real-pokemon-combo]
(let [types-included (set (flatten (map last real-pokemon-combo)))]
  [
   [
    (count types-included)
    (apply + (map first real-pokemon-combo))
   ]
   (map second real-pokemon-combo)
   types-included
   ]
  )
  )

(defn all-possible-ability-outcomes [types]
  (let [real-pokemon-combinations (combo/combinations (sorted-best-pokemon-for-specified-combo-search types) (int (Math/ceil (/ (count types) 2))))]
    (reverse (sort-by first compare (map evaluate-real-pokemon-combination real-pokemon-combinations)))
  )
  )

(defn best-ability-combo [types]
  (first (all-possible-ability-outcomes types ))
)

(def best-ten-types
  '((:fighting :poison :ground :rock :bug :ghost :fire :grass :ice :fairy)
 (:fighting :poison :ground :rock :bug :fire :grass :ice :dark :fairy)
 (:fighting :ground :rock :bug :ghost :steel :fire :grass :ice :fairy)
 (:fighting :ground :rock :bug :steel :fire :grass :ice :dark :fairy)))

(def abilities-per-typelist
  (map best-ability-combo best-ten-types)
)

(def best-twelve-types 
  '(
     (:fighting :flying :poison :ground :rock :bug :ghost :steel :fire :grass :ice :fairy)
     (:fighting :flying :poison :ground :rock :bug :ghost :fire :water :grass :ice :fairy)
     (:fighting :flying :poison :ground :rock :bug :ghost :fire :grass :electric :ice :fairy)
     (:fighting :flying :poison :ground :rock :bug :ghost :fire :grass :psychic :ice :fairy)
    ))

(def abilities-per-typelist-twelve
  (map best-ability-combo best-twelve-types)
)

;(reverse (sort-by first compare (map best-ability-combo best-ten-types)))

;([[10 3266]
;  ("Mega Heracross"
;   "Primal Groudon"
;   "Mega Tyranitar"
;   "Magearna"
;   "Mega Abomasnow")
;  #{:fire :ground :bug :dark :fighting :grass :fairy :ice :rock :steel}]
; [[10 3266]
;  ("Marshadow" "Primal Groudon" "Mega Diancie" "Mega Scizor" "Mega Abomasnow")
;  #{:fire :ground :bug :fighting :grass :fairy :ghost :ice :rock :steel}]
; [[10 3266]
;  ("Mega Heracross"
;   "Mega Gengar"
;   "Primal Groudon"
;   "Mega Diancie"
;   "Mega Abomasnow")
;  #{:fire :ground :bug :fighting :grass :fairy :ghost :ice :rock :poison}]
; [[10 3205]
;  ("Mega Heracross" "Mega Venusaur" "Primal Groudon" "Mega Diancie" "Weavile")
;  #{:fire :ground :bug :dark :fighting :grass :fairy :ice :rock :poison}])

(defn print-best-combos [types]
  (pprint (take 2 (all-possible-ability-outcomes types ))
        )
  )
