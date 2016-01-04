(ns dir-purger.delete
  (:require
   [schema.core :as s]
   [clojure.java.io :as io]
   )
  (:gen-class))

(do (set! *warn-on-reflection* true)(schema.core/set-fn-validation! true))

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
  [file :- java.io.File trial :- s/Bool toplevel? :- s/Bool]
  (when (.exists file)
    (if (.isDirectory file)
      (do
        ;; .listFiles creates File object, .list just names, to
        ;; see if we can handle dir with 305700 files
        (doseq [child (.list file)]
          (delete-file-recursively (java.io.File. file ^String child) trial false)
          )
        ;; delete empty directories
        (when-not toplevel?
          (when (empty? (.list file))
            (when (older-than-days 1 file) (.delete file))))
        )
      (delete-file file trial)
      )
    ))
