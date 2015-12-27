(ns poketype.core
  (:require [clojure.math.combinatorics :as combo ]
            [clojure.core.reducers :as r])
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
  (range 0 (count (get type-effectiveness-attack-defence 0)))
  )

(def type-combos 
  (combo/combinations all-attack-types 6))

(def type-combos-vector 
  (map (fn [x] (apply vector x)) type-combos)
  )

(defn transpose [m]
  (apply mapv vector m))

(def type-effectiveness-defence-attack
  (transpose type-effectiveness-attack-defence)
  )

(defn max-effective-indexes [index] 
  (apply max-key second 
         (map-indexed vector 
                      (get type-effectiveness-defence-attack index))))


(defn select-loadout [& type-vector]
  (map (fn [x] (get type-effectiveness-attack-defence x)) type-vector)
  )

(defn loadout-max-effectiveness-array [& type-vector] 
  (r/foldcat (r/map (fn [x] (apply max x)) (transpose (apply select-loadout type-vector)))) 
  )

(defn loadout-total-effectiveness [& type-vector] 
  (r/fold + (apply loadout-max-effectiveness-array type-vector))
  )

(defn loadouts-total-effectiveness [loadouts]
  (r/map 
    (fn [x] (apply loadout-total-effectiveness x)) 
    loadouts) 
  )

(defn loadouts->key-val-loadouts-effectiveness-array [loadouts]
  (r/map (fn[x] (let [max-arr (apply loadout-max-effectiveness-array x)] [x  [max-arr (apply loadout-total-effectiveness x)] ])) loadouts)
  )

(defn loadouts-effectiveness-result->total [result]
  (get result 1))

(defn loadouts-map-string [loadoutsMapping] 
  (map (fn[x] [(map type-index->keyword (get x 0)) (get x 1)] ) loadoutsMapping)
  )

(defn -main []
  
   (pprint (loadouts-map-string
             (into [] (r/filter (fn [x] (>= (get (get x 1) 1) 35)) 
                     (loadouts->key-val-loadouts-effectiveness-array type-combos-vector)))))(shutdown-agents)
   )
