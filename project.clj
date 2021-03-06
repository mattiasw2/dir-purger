(defproject dir-purger "0.2.1"
  :description "Keep directories clean of old files."
  :url "https://github.com/mattiasw2/dir-purger"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}
  :dependencies [
                 [org.clojure/clojure "1.7.0"]
                 [prismatic/schema "1.0.3"]
                 ;;[org.clojure/core.match "0.3.0-alpha4"]
                 ;; clojure logging
                 ;; https://github.com/ptaoussanis/timbre
                 [com.taoensso/timbre "4.1.4"]
                 ;; pandect is a fast and easy-to-use Message Digest, Checksum, HMAC and Signature library for Clojure.
                 ;; https://github.com/xsc/pandect
                 ;; [pandect "0.5.4"]

                 ;; for Windows service
                 [commons-daemon/commons-daemon "1.0.15"]
                 ]
  :main ^:skip-aot dir-purger.core
  :target-path "target/%s"
  :profiles {:uberjar {:aot :all}
             ;; without this, nrepl 0.2.10 will be used and cider wants 0.2.12
             :repl
             {
              :dependencies
              [
               [org.clojure/tools.nrepl "0.2.12"]
               ;; frm .lein: spyscope: #spy/p #spy/d #spy/t
               ;; (take 20 (repeat #spy/p (+ 1 2 3)))
               [spyscope "0.1.5"]
               ]}
             }

  ;; :uberjar-name "dir-purger.jar"


  :plugins [
            ;;[cider/cider-nrepl "0.10.0"]
            [cider/cider-nrepl "0.10.0" :exclusions [org.clojure/tools.nrepl]]
            
            ;;[lein-environ "1.0.0"]
            ;; Pretty-print a representation of the project map.
            ;; https://github.com/technomancy/leiningen/tree/master/lein-pprint
            ;; lein pprint
            ;;[lein-pprint "1.1.1"]
            
            ;; lint for clojure
            ;;[lein-kibit "0.1.2"]
            ;; another lint for clojure
            ;;[jonase/eastwood "0.2.3"]
            ]


  )
