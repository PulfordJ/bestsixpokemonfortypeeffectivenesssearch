(ns poketype.sqlextractor
  (:require [clojure.math.combinatorics :as combo ]
            [clojure.core.reducers :as r]
            [clojure.string :as str]
            [com.rpl.specter :as specter]
            [yesql.core :refer [defqueries]]
            )
  (:use clojure.pprint )
  )
;To setup in MariaDB:
;MariaDB [(none)]> create database pokemon_db2
;MariaDB [(none)]> use pokemon_db2
;MariaDB [pokemon_db2]> show tables
;MariaDB [pokemon_db2]> source Pokemon.sql
;MariaDB [pokemon_db2]> source Type.sql
;MariaDB [pokemon_db2]> source PKMNType.sql
; Define a database connection spec. (This is standard clojure.java.jdbc.)
(def db-spec {:classname "com.mysql.jdbc.Driver"
              :subprotocol "mysql"
              :subname "//localhost:3306/pokemon_db2"
              :user "root"
              :password "root"})

; Import the SQL query as a function.
(defqueries "resource/findpokemontypesandstats.sql"
  {:connection db-spec})

(def data-partitioned-by-pokemon-names
  (partition-by :pokemon_name (pokemon-with-type-and-stats))
  )


(defn select-types-from-flat-pokemon-data [pokemon-type-data]
         (apply vector
         (map keyword
         (sort
         (map str/lower-case 
              (specter/select [specter/ALL :type_name] pokemon-type-data))
                  )
                )
                )
  )

(defn pokemon-value [pokemon-value-data]
  (apply +
         ((
           juxt 
           :base_hp :base_atk :base_def :base_spa :base_spd :base_spe) 
          pokemon-value-data)
         ))

(def pokemon-name-type-and-value-i-to-vi
  (map 
    #(list (pokemon-value (first %1))
           (:pokemon_name (first %1))
           (select-types-from-flat-pokemon-data %1)) 
    data-partitioned-by-pokemon-names)
  )
;total states, pokemon name, types.
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

(defn print-best-combos [types]
  (pprint (take 2 (all-possible-ability-outcomes types ))
        )
  )
