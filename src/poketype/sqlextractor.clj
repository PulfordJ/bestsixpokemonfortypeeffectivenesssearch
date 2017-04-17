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

(def pokemon-name-type-and-value-from-database
  (map 
    #(list (pokemon-value (first %1))
           (:pokemon_name (first %1))
           (select-types-from-flat-pokemon-data %1)) 
    data-partitioned-by-pokemon-names)
  )
