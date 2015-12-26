(defproject poketype "0.0.1-SNAPSHOT"
  :description "Cool new project to do things and stuff"
  :dependencies [[org.clojure/clojure "1.4.0"], 
                 [org.clojure/math.combinatorics "0.1.1"]]
  :profiles {:dev {:dependencies [[midje "1.5.1"][org.clojure/tools.trace "0.7.5"]]}}
            :main poketype.core)

  
