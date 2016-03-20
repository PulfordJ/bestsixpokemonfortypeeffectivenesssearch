(ns poketype.core
  (:require [clojure.math.combinatorics :as combo ]
            [clojure.core.reducers :as r]
            )
  (:use clojure.pprint )
  )

;;; This is an incorrect implementation, such as might be written by
;;; someone who was used to a Lisp in which an empty list is equal to
;;; nil.
(defn first-element [sequence default]
  (if (empty? sequence)
    default
    (first sequence)))

(def normal 0)
(def fight 1)
(def flying 2)
(def poison 3)
(def ground 4)
(def rock 5)
(def bug 6)
(def ghost 7)
(def steel 8)
(def fire 9)
(def water 10)
(def grass 11)
(def electric 12)
(def psychic 13)
(def ice 14)
(def dragon 15)
(def dark 16)

(def type-index->keyword 
  [:normal
   :fight
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
            :fight 
            :ground 
            :flying 
            :psychic 
            :fairy]
   :fire [
          :water
          :electric
          :fight
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
           :fight
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
           :fight
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
   :fight [:poison
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

(defn loadouts-effectiveness-result->total [result]
  (get result 1))

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
  (map highest-score (range 1 18)))


(defn score-cutoff [amount-of-types-in-loadout] 
  (highest-score amount-of-types-in-loadout) )

(defn get-and-print-loadouts [amount-of-types-in-loadout]
  (let [score-bar (score-cutoff amount-of-types-in-loadout)](binding [*print-right-margin* 100]  
    (pprint (loadouts-map-string
      (take 4 (sort #(compare (get (get %2 1) 1) (get (get %1 1) 1))
              (into []  
                                 (loadouts->key-val-loadouts-effectiveness-array (type-combos-vector amount-of-types-in-loadout)))))))))
  
  )

(defn recursive-1-to-n-loadout-checks [n] 
  (if (> n 1) 
     (recursive-1-to-n-loadout-checks (dec n))
     )(get-and-print-loadouts n))

(defn -main []
  (recursive-1-to-n-loadout-checks 12) 
  )
