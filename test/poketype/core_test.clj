(ns poketype.core-test
  (:use midje.sweet)
  (:use [poketype.core]))
(background (around :facts (let [normal (keyword->type-index :normal)
                                 dark (keyword->type-index :dark) 
                                 amount-of-single-types-in-current-gen 18 
                                 amount-of-active-types-in-current-gen 133] ?form)))

(facts "about type effectiveness"
(fact "There are 18 types."
      
      (get-in type-effectiveness-attack-defence [normal normal]) => 1
      (get-in type-effectiveness-attack-defence [dark dark]) => (/ 1 2)
      (get-in type-effectiveness-attack-defence [dark dark]) => 1/2
      (count (get type-effectiveness-attack-defence normal)) => amount-of-single-types-in-current-gen
      (count (get type-effectiveness-attack-defence-dual normal)) => amount-of-active-types-in-current-gen
      (count (first (select-loadout dark))) => amount-of-active-types-in-current-gen
       )

(fact "select loadout works"
      (select-loadout normal) => '([1 1 1 1 1 1/2 1 0 1/2 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1/2 0 1 1 1/2 1/2 0 1 1 1/2 1 1 1 1 1/2 0 1 1 1/2 1/2 0 1/2 1 1 1/2 0 1 1 1/2 1 1 1 1 1 1 1 1 1 1 1/2 1 1/2 1 1 1 1 1 1 0 1 1/2 1 1 1 1 1 1/2 1 1/2 1 1 1 1 1 1 1 1/2 0 1 1 1/2 1 0 0 0N 1 1 1 1/2 0 1 1 1 1 1 1 1 1 1 1 1 1/2 0 1 1 1/2 1 1/2 1/2 1/4 1/2 1/2 1 1 1 0 1 1])
;      (select-loadout normal) => [[1, 1, 1, 1, 1, 1/2, 1, 0, 1/2, 1, 1, 1, 1, 1, 1, 1, 1, 1]]
;      (select-loadout dark rock) => [[1 1/2 1 1 1 1 1 2 1 1 1 1 1 2 1 1 1/2 1/2],[1, 1/2, 2, 1, 1/2, 1, 2, 1, 1/2, 2, 1, 1, 1, 1, 2, 1, 1, 1]]     
      )
       
(fact "loadout-max-effectiveness-array works"
      (loadout-max-effectiveness-array normal dark) => '(1 1 1 1 1 1 1 2 1 1 1 1 1 2 1 1 1 1 1 1 1 1 1 2 1 1 2 1 1 1 2 4 2 1N 2 1N 1 2 1 1 2 1 1 1 1 2 1 2 1 1 2 1 1 1 1 1 1 1 1 1 1 1 2 1 1/2 1 1 1 1 1 1 1 1 2 1 1 1 1 1 1N 1 1/2 1 1/2 1 1 1 1 1 2 1 1 2 1 1 1 1 2 1N 2 1 1 2 1 2 1 1 1 1 1 1 1 1 1 2 1 1 2 1 1 1 1 1 1/2 1 1/2 1/2 1 1 1 2 1 1)
      )

(fact "get-score-of-loadout"
      (loadout-total-effectiveness [2 1]) => 3
      highest-score-with-all-types => 344    
      )

       )
(facts "about pokemon search from data source"
(fact "There are 18 types."
      
      (get-in type-effectiveness-attack-defence [normal normal]) => 1
      (get-in type-effectiveness-attack-defence [dark dark]) => (/ 1 2)
      (get-in type-effectiveness-attack-defence [dark dark]) => 1/2
      (count (get type-effectiveness-attack-defence normal)) => amount-of-single-types-in-current-gen
      (count (get type-effectiveness-attack-defence-dual normal)) => amount-of-active-types-in-current-gen
      (count (first (select-loadout dark))) => amount-of-active-types-in-current-gen
       )

(fact "pokemon names can be extracted."
      (count data-partitioned-by-pokemon-names) => 803
      (select-types-from-flat-pokemon-data (take 2 (pokemon-with-type-and-stats))) => [:grass :poison]
      (first pokemon-name-type-and-value) => '(318 "Bulbasaur" [:grass :poison])

      (pokemon-value (first (pokemon-with-type-and-stats))) => 318
      (first (sort-pokemon-by-value pokemon-name-type-and-value)) => 
                                    '(780 "Mega Mewtwo X" [:fighting :psychic])
       
      (count (set (map last pokemon-name-type-and-value))) => 133
      (count partition-pokemon-into-types) => 133
      (count best-pokemon-for-each-type) => 133
      (first sorted-best-pokemon-for-each-type) => '(780 "Mega Mewtwo X" #{:psychic :fighting})

      (sorted-best-pokemon-of-specific-type :ghost) => '(680 "Giratina" [:dragon :ghost])
      (sorted-best-pokemon-of-specific-types :grass :poison) => '(625 "Mega Venusaur" [:grass :poison])

      (sorted-best-pokemon-for-specified-combo-search '(:fight :ground :rock :ice)) => 
       '((535 "Rhyperior" [:ground :rock]) 
        (530 "Mamoswine" [:ground :ice]) 
        (521 "Aurorus" [:ice :rock]))

      (evaluate-real-pokemon-combination '((535 "Rhyperior" [:ground :rock]) (530 "Mamoswine" [:ground :ice]))) =>
                 '([3 1065] ("Rhyperior" "Mamoswine") #{:ground :ice :rock})
(all-possible-ability-outcomes '(:fight :ground :rock :ice)) => '([[3 1065] ("Rhyperior" "Mamoswine") #{:ground :ice :rock}] [[3 1056] ("Rhyperior" "Aurorus") #{:ground :ice :rock}] [[3 1051] ("Mamoswine" "Aurorus") #{:ground :ice :rock}])
      )
       )
