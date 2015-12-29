(ns dir-purger.config-file
  (:require
   [clojure.java.io :as io]
   )
  (:gen-class)
  )

(defn this-jar?
  "utility function to get the name of jar in which this function is invoked
  Used like this: (println (this-jar mw.config-file))"
  [& [ns]]
  (try
    (-> (or ns (class *ns*))
        .getProtectionDomain .getCodeSource .getLocation .getPath)
    (catch Exception e nil)))

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
      (let [jar-path (this-jar? nil)] ;; dir-purger.config-file)]
        (if jar-path
          (find-and-slurp-internal filename 50 (str (.getParent (java.io.File. jar-path)) "/"))
          (throw (Exception. (str "Not found: " filename))))))))

