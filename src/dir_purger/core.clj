(ns dir-purger.core
 (:require 
  [dir-purger.delete :as myfile]
  [dir-purger.mw1 :as mw1]
  [clojure.java.io :as io]
  )
   
  (:import [org.apache.commons.daemon Daemon DaemonContext])
  (:gen-class
   :implements [org.apache.commons.daemon.Daemon]))

;;; read about procrun for windows, which is needed
;;; http://www.rkn.io/2014/02/06/clojure-cookbook-daemons/
;;; http://commons.apache.org/proper/commons-daemon/procrun.html

(def Config
  "The parameters used to identify your CDN77 account
   https://client.cdn77.com/support/api/version/2.0/data#Prefetch"
  ;;{:pause-minutes 60 :dirs ["C:\\temp\\pricing-core\\"] :trial true})
  (read-string (mw1/find-and-slurp "dir-purger.config")))


;;; A crude approximation of your application's state.
(def state (atom {}))

(defn work
  "Called every :pause-minutes"
  []
  ;; (vec to force execution
  (vec                                  
   (for [dir (:dirs Config)]
    ;; todo: how to abort even quicker?
    (when (:running @state)
      (myfile/delete-file-recursively dir (:trial Config)))
    )))
  
;;; the NTService/daemon call-backs
(defn init [args]
  (println (str "Starting " Config))
  (swap! state assoc :running true))

(defn start []
  (while (:running @state)
    (work)
    ;; todo: this sleep should be short, since otherwise, we cannot stop the daemon
    ;; todo: so I will need a counter too, :pause-minutes * 60
    (Thread/sleep 1000)))

(defn stop []
  (swap! state assoc :running false))

;; Daemon implementation

(defn -init [this ^DaemonContext context]
  (init (.getArguments context)))

(defn -start [this]
  (future (start)))

(defn -stop [this]
  (stop))

;; Enable command-line invocation
(defn -main [& args]
  (init args)
  (work)
  (println (str "Finished "))
  )
