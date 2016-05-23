(defproject poketype "0.0.1-SNAPSHOT"
  :description "Cool new project to do things and stuff"
  :dependencies [[org.clojure/clojure "1.7.0"] 
                 [org.clojure/math.combinatorics "0.1.1"]
                 [net.mikera/core.matrix "0.47.1"]
                 [yesql "0.5.3"]
                 [com.rpl/specter "0.10.0"]
                 [mysql/mysql-connector-java "5.1.32"]]
  :profiles {:dev {:dependencies [[midje "1.5.1"][org.clojure/tools.trace "0.7.5"]]}}
            :main poketype.core)

  
