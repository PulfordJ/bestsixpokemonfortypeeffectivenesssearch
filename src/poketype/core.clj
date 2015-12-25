(ns poketype.core
  (:require [clojure.math.combinatorics :as combo ])
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
   ]
   )

(def type-effectiveness-attack-defence
  [
   [1, 1, 1, 1, 1, 1/2, 1, 0, 1/2, 1, 1, 1, 1, 1, 1, 1, 1],
   [2, 1, 1/2, 1/2, 1, 2, 1/2, 0, 2, 1, 1, 1, 1, 1/2, 2, 1, 2],
   [1, 2, 1, 1, 1, 1/2, 2, 1, 1/2, 1, 1, 2, 1/2, 1, 1, 1, 1],
   [1, 1, 1, 1/2, 1/2, 1/2, 1, 1/2, 0, 1, 1, 2, 1, 1, 1, 1, 1],
   [1, 1, 0, 2, 1, 2, 1/2, 1, 2, 2, 1, 1/2, 2, 1, 1, 1, 1],
   [1, 1/2, 2, 1, 1/2, 1, 2, 1, 1/2, 2, 1, 1, 1, 1, 2, 1, 1],
   [1, 1/2, 1/2, 1/2, 1, 1, 1, 1/2, 1/2, 1/2, 1, 2, 1, 2, 1, 1, 2],
   [0, 1, 1, 1, 1, 1, 1, 2, 1/2, 1, 1, 1, 1, 2, 1, 1, 1/2],
   [1, 1, 1, 1, 1, 2, 1, 1, 1/2, 1/2, 1/2, 1, 1/2, 1, 2, 1, 1],
   [1, 1, 1, 1, 1, 1/2, 2, 1, 2, 1/2, 1/2, 2, 1, 1, 2, 1/2, 1],
   [1, 1, 1, 1, 2, 2, 1, 1, 1, 2, 1/2, 1/2, 1, 1, 1, 1/2, 1],
   [1, 1, 1/2, 1/2, 2, 2, 1/2, 1, 1/2, 1/2, 2, 1/2, 1, 1, 1, 1/2, 1],
   [1, 1, 2, 1, 0, 1, 1, 1, 1, 1, 2, 1/2, 1/2, 1, 1, 1/2, 1],
   [1, 2, 1, 2, 1, 1, 1, 1, 1/2, 1, 1, 1, 1, 1/2, 1, 1, 0],
   [1, 1, 2, 1, 2, 1, 1, 1, 1/2, 1/2, 1/2, 2, 1, 1, 1/2, 2, 1],
   [1, 1, 1, 1, 1, 1, 1, 1, 1/2, 1, 1, 1, 1, 1, 1, 2, 1],
   [1, 1/2, 1, 1, 1, 1, 1, 2, 1/2, 1, 1, 1, 1, 2, 1, 1, 1/2 ]]
  )

(def all-types 
  (range 0 (count (get type-effectiveness-attack-defence 0)))
  )

(def type-combos 
  (combo/combinations all-types 6))

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
  (map (fn [x] (apply max x)) (transpose (apply select-loadout type-vector))) 
  )

(defn loadout-total-effectiveness [& type-vector] 
(apply + (apply loadout-max-effectiveness-array type-vector))
  )

(defn loadouts-total-effectiveness [loadouts]
  (map 
    (fn [x] (apply loadout-total-effectiveness x)) 
       loadouts) 
)

(defn loadouts-max-effectiveness-arrays [loadouts]
  (map 
    (fn [x] [(apply loadout-max-effectiveness-array x)
      (apply loadout-total-effectiveness x)]) 
       loadouts) 
  ) 


(defn loadouts->key-val-loadouts-effectiveness-array [loadouts]
  (zipmap loadouts (loadouts-max-effectiveness-arrays loadouts)
                    )
  )

(defn loadouts-effectiveness-result->total [result]
(get result 1))

(defn loadouts->sorted-key-val-loadouts-effectiveness-array [loadouts]
 (let [non-sorted-results 
       (loadouts->key-val-loadouts-effectiveness-array loadouts) ]
 (into (sorted-map-by (fn [key1 key2]
                           (compare 
                             [(get (get non-sorted-results key2) 1) key2]
                             [(get (get non-sorted-results key1) 1) key1] )))
        non-sorted-results)))

(defn loadouts-key-val-entry-string [key-val-entry]
   (conj 
     (vector (map type-index->keyword (key key-val-entry)))
     (val key-val-entry))
  )

(defn loadouts-map-string [loadoutsMapping] 
  (map loadouts-key-val-entry-string loadoutsMapping)  
  )

(defn -main []
  (pprint (loadouts-map-string
    (filter (fn [x] (== 34 (get (val x) 1))) (loadouts->sorted-key-val-loadouts-effectiveness-array type-combos-vector)))))

;; (pprint (filter (fn [x] (== 34 (get (val x) 1))) (loadouts->sorted-key-val-loadouts-effectiveness-array type-combos-vector)))
