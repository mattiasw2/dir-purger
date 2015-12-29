(ns dir-purger.delete
  (:require
   [schema.core :as s]
   [clojure.java.io :as io]
   )
  (:gen-class))

;; long diff = new Date().getTime() - file.lastModified();

;; if (diff > x * 24 * 60 * 60 * 1000) {
;;     file.delete();
;; }
;; Which deletes files older than x (an int) days.

(s/defn older-than-days :- s/Bool
  "Return true if File object older than days days"
  [days :- s/Num file :- java.io.File]
  (let [diff (- (.getTime (java.util.Date.)) (.lastModified file))]
    (> diff (* days 24 60 60 1000))))
  

(s/defn delete-file 
  "Delete file f."
  [file :- java.io.File trial :- s/Bool]
  (when (older-than-days 1 file)
    (if trial
      (println (str "will be deleted: " file))
      (.delete file))))


;; (defn delete-if-empty 
;;   "Deletes the given directory if it contains no files or subdirectories." 
;;   [directory]
;;   (when-not (empty? (file-seq directory))
;;     (. directory delete)
;;     true))

(s/defn delete-file-recursively
  "Delete file f. If it's a directory, recursively delete all its contents.
   Only list files to delete unless trial is false."
  [f :- s/Str trial :- s/Bool]
  (let [file (io/as-file f)]
    (if (.isDirectory file)
      (do
        (doseq [child (.listFiles file)]
          (delete-file-recursively child trial)
          )
        ;; no, do not delete empty directories, but if I want, I can do it here
        )
      (delete-file file trial)
      )
    ))
