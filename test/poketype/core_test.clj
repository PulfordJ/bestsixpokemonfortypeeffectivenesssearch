(ns poketype.core-test
  (:use midje.sweet)
  (:use [poketype.core]))

(facts "about type effectiveness"
(fact "There are 17 types."
      (get-in type-effectiveness-attack-defence [normal normal]) => 1
      (get-in type-effectiveness-attack-defence [dark dark]) => (/ 1 2)
      (get-in type-effectiveness-attack-defence [dark dark]) => 1/2
      (count (get type-effectiveness-attack-defence normal)) => 17)

(fact "The most effective type against normal is fight."
      (max-effective-indexes normal) => [1 2]
      (max-effective-indexes normal) => [1 2]
      
      )

(fact "select loadout works"
      (select-loadout normal) => '([1, 1, 1, 1, 1, 1/2, 1, 0, 1/2, 1, 1, 1, 1, 1, 1, 1, 1])
      (select-loadout normal) => [[1, 1, 1, 1, 1, 1/2, 1, 0, 1/2, 1, 1, 1, 1, 1, 1, 1, 1]]
      (select-loadout dark rock) => [[1, 1/2, 1, 1, 1, 1, 1, 2, 1/2, 1, 1, 1, 1, 2, 1, 1, 1/2 ],[1, 1/2, 2, 1, 1/2, 1, 2, 1, 1/2, 2, 1, 1, 1, 1, 2, 1, 1]]
      
      )
       
(fact "loadout-max-effectiveness-array works"
      (loadout-max-effectiveness-array normal fight) => '(2 1 1 1 1 2 1 0 2 1 1 1 1 1 2 1 2)
      ;;(loadouts-max-effectiveness-arrays [[0 1] [0]]) => [[2 1 1 1 1 2 1 0 2 1 1 1 1 1 2 1 2] [1 1 1 1 1 1/2 1 0 1/2 1 1 1 1 1 1 1 1]]
      )

(fact "get-score-of-loadout"
      (loadout-total-effectiveness normal fight) => 21
      )

       )
