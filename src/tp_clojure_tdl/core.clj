(ns tp-clojure-tdl.core
  (:gen-class)
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

(defn validate_file [doc]
  ( if (not-empty doc)
    true
    false
  )
)

(defn init_service [] 
  ( do
    ( loop []
      ( do
        (println "Ingrese la ruta del Archivo:")
        (def document (read-line))
        (def validate (validate_file document))
        ( if (true? validate)
          (println document)
          (recur)
        )
      )
    )
  )
)

(defn -main
  [& args]
  (init_service)
)

