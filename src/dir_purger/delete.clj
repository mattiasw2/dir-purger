(ns dir-purger.delete
   (:require 
     [clojure.java.io :as io]
     )
   (:gen-class))


(defn delete-file
  "Delete file f. Raise an exception if it fails unless silently is true."
  [f & [silently]]
  (or (.delete (io/as-file f))
      silently
      (throw (java.io.IOException. (str "Couldn't delete " f)))))

;; (defn delete-if-empty 
;;   "Deletes the given directory if it contains no files or subdirectories." 
;;   [directory]
;;   (when-not (empty? (file-seq directory))
;;     (. directory delete)
;;     true))

(defn delete-file-recursively
  "Delete file f. If it's a directory, recursively delete all its contents.
Raise an exception if any deletion fails unless silently is true."
  [f & [silently]]
  (let [f (io/as-file f)]
    (if (.isDirectory f)
      (do
        (doseq [child (.listFiles f)]
          ;;(println (str "recurse " child))
          (delete-file-recursively child silently)
          )
        ;; no, do not delete empty directories, but if I want, I can do it here
        )
      (do
        ;;(println (str "file    " f))
        (delete-file f silently)
        ))
    ))
