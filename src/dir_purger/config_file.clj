(ns dir-purger.config-file
  (:require
   [clojure.java.io :as io]
   )
  (:gen-class)
  )

(do
  ;;(set! *warn-on-reflection* true)
  (schema.core/set-fn-validation! true))

(defn this-jar?
  "utility function to get the name of jar in which this function is invoked
  Used like this: (println (this-jar mw.config-file))"
  [& [ns]]
  (let [^clojure.lang.Namespace ns2 (or ns (class *ns*))]
    (try
      (-> ns2
          .getProtectionDomain .getCodeSource .getLocation .getPath)
      (catch Exception e nil))))

(defn find-and-slurp-internal
  "Search and slurp for file in this dir, and all parents until found. Throw exception if not found. Max 50 levels"
  ([filename] (find-and-slurp-internal filename 50 ""))
  ([filename level prefix]
   ;; in order to allow :post
   ;; (loop [filename filename level level prefix prefix]
   (if (< level 0) 
     (throw (Exception. (str "Not found: " filename)))
     (if (.exists (clojure.java.io/as-file (str prefix filename)))
       (slurp (str prefix filename))
       (recur filename (- level 1) (str "../" prefix))))))

;;; Look at http://www.mkyong.com/java/java-cron-job-to-run-a-jar-file/
;;; to see how jar file can be scheduled easily
(defn find-and-slurp
  "Search in this dir, and if not found, search in position of jar file (for cron) or any of its parents"
  [filename]
  (try
    (find-and-slurp-internal filename)
    (catch Exception e
      ;; type hints can both be set in let/arg or as you see further down in the call
      (let [^String jar-path (this-jar? nil)] ;; dir-purger.config-file)]
        (if jar-path
          (find-and-slurp-internal filename 50 (str (.getParent (java.io.File. ^String jar-path)) "/"))
          (throw (Exception. (str "Not found: " filename))))))))

