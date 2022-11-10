;; In action_on_file.clj

(defn validate_file [doc]
  (if (not-empty doc)
    true
    false))

(defn read_File [] 
  (loop [] 
   ;; ((println "Ingrese la ruta del Archivo:")
     ;;(def document (read-line))
        ;;(def validate (validate_file document))
        ;;(if (true? validate)
        ;;    (println (document))
        ;;)
     ;;)
  )
)