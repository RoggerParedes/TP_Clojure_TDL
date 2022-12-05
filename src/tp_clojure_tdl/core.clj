(ns tp-clojure-tdl.core
  (:gen-class)
  (:require [clojure.java.io :as io])
  (:require [clojure.string :as string])
  (:require [clojure.core.async
             :as a
             :refer [>! <! put! go-loop chan]])
   (:require [tp-clojure-tdl.backtracking 
              :refer :all ])
)

(defn validate_extension_file [name_file]
    ( let  [extension_file 
      (second (re-find #"(\.[a-zA-Z0-9]+)$" name_file))]
     ( if (= ".json" extension_file)
      true
      false
    )))

(defn is_empty_name [doc]
  ( if (not-empty doc)
    true
    false
  )
)

(def chan_create_file (chan) )

(def chan_resolve_data (chan) )

(def  thread_1
  (future 
    ( go-loop []
     (let [x (<! chan_resolve_data)]
       ;(Thread/sleep 1000) 
       (>! chan_create_file x)
       (recur))
     )))

(def  thread_2
  (future
    ( go-loop [] 
     (let [x (<! chan_create_file)] 
       ;(Thread/sleep 1000) 
       (spit "solutions.txt" (str x "\n") :append true)
       (recur))
     ))
  )

(defn file_exists [name_file]
  (if (= true (.exists (io/file name_file)) ) true false)
)

(def line (atom {:value ""}))
(def matrix (atom {:value []}))

(defn remove_character_ [a b c] 
  (swap! c assoc :value (string/replace (@c :value) a b)))
   
(defn remove_character
  "array_of_character_to_remove:Vector{:character character :newCharacter newCharacter} 
   atom_string:{:value value}"
  [array_of_character_to_remove atom_string]
  (doseq [res array_of_character_to_remove]
    (remove_character_ (get res :character) 
                       (get res :newCharacter) atom_string)))

(def vector_array (vector 
         {:character "]" :newCharacter ""} 
         {:character "[" :newCharacter ""}))

(defn replace-in-str [line to-be-replaced replacement]
  (clojure.string/replace line
                          (re-pattern (if (< 1 (count to-be-replaced))
                                        to-be-replaced
                                        (str "[" to-be-replaced "]")
                                        ))
                          replacement))

(defn clear-line [line]
  (let [new-line (replace-in-str line "." "0")]
    (apply str (re-seq (re-pattern "[\\d]") new-line))
    )
  )

(defn grid-from-line [line]
  (let [new-line (clear-line line)]
    (if (= 81 (count new-line))
      (let [seq-num (map #(Character/digit % 10) (seq new-line))]
        (swap! matrix assoc :value (into (vector) (map #(into (vector) %) (partition 9 seq-num)))))
      nil)
    )
  )

(defn read_line [nameFile]
  (with-open [rdr (io/reader nameFile)]
    (doseq [line_ (line-seq rdr)]
      (swap! line assoc :value line_)
      (remove_character vector_array line)
      (grid-from-line  (get @line :value))
      (put! chan_resolve_data  (sudoku-solver (get @matrix :value)))
    )
  )
)

(defn init_service [] 
  
    ( loop []
        (println "Ingrese la ruta del Archivo:")
        (let [document (read-line) validate (is_empty_name document)] 
            (if (true? (and validate (file_exists document)))
              (read_line document)
              (do (println "Ruta invalida") (recur))
            )
        )
    )
)

(defn -main
  [& args]
  (init_service)
)
;;/home/rogger/Documents/tp_clojure_tdl/doc/prueba.txt

;;(defn -main
  ;;[& args]
  ;;(println "Inicio") 
   ;;(put! chan_resolve_data "hola2")
   ;;(println "Fin")
;;)

;;(-main)