(ns tp-clojure-tdl.core
  (:gen-class)
  (:require [clojure.java.io :as io])
)

(defn validate_extension_file [name_file]
  ( do
    ( def extension_file 
      (second (re-find #"(\.[a-zA-Z0-9]+)$" name_file))
    )
    ( if (= ".json" extension_file)
      true
      false
    )
  )  
)

(defn is_empty_name [doc]
  ( if (not-empty doc)
    true
    false
  )
)

(defn file_exists [name_file]
  (if (= true (.exists (io/file name_file)) ) true false)
)

(defn read_line [nameFile]
  (with-open [rdr (clojure.java.io/reader nameFile)]
    (doseq [line (line-seq rdr)]
      (println line)
    )
  )
)

(defn init_service [] 
  ( do
    ( loop []
      ( do
        (println "Ingrese la ruta del Archivo:")
        (def document (read-line))
        (def validate (is_empty_name document))
        ;;( if (true? validate)
        ( if (true? (and validate (file_exists document)))
          (read_line document)
          (do (println "Ruta invalida") (recur))
        )
      )
    )
  )
)

(defn -main
  [& args]
  (init_service)
)
;;/home/rogger/Documents/tp_clojure_tdl/doc/prueba.txt