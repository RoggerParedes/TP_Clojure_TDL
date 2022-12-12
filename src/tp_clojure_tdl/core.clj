(ns tp-clojure-tdl.core
  (:gen-class)
  (:require [clojure.java.io :as io])
  (:require [clojure.string :as string])
  (:require [clojure.core.async
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

(def channel_create_file (chan) )

(def channel_resolve_data (chan) )

(def  thread_1
  (future 
    ( go-loop []
     (let [x (<! channel_resolve_data)]
       ;(Thread/sleep 1000) 
       (>! channel_create_file x)
       (recur))
     )))

(def  thread_2
  (future
    ( go-loop [] 
     (let [x (<! channel_create_file)] 
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

(defn format-sudoku-line [arr]
  (let [line (mapv #(if (zero? %) " " %) arr)]
    (vec (flatten (conj (subvec line  0 3) '| (subvec line  3 6) '| (subvec line  6 9))))))

(defn print-grid [grid channel]
  (loop [i 0]
    (when (< i N)
      (when (and (zero? (mod i 3)) (pos? i)) 
        (let [separator (vec (repeat 11 '-))]
          (println separator)
          (put! channel separator)))
      (println (format-sudoku-line (grid i)))
      (put! channel (format-sudoku-line (grid i)))
      (recur (inc i)))))

(defn process-file [nameFile]
  (with-open [rdr (io/reader nameFile)]
    (doseq [line_ (line-seq rdr)]
      (swap! line assoc :value line_)
      (remove_character vector_array line)
      (grid-from-line (get @line :value))
      (let [resolution (sudoku-solver (get @matrix :value))]
        (print-grid resolution channel_resolve_data)
        (let [separator (vec (repeat 15 '*))] 
          (println separator)
          (put! channel_resolve_data separator)))
      ;)
    )
  )
)

(defn init-service []
  (println "Ingrese la ruta del Archivo o escriba SALIR para terminar:")
  (try
    (let [line (clojure.string/trim (read-line))]
      (prn "linea: " line)
      (cond
        (empty? line) (do (println "ERROR: no se ingresó nada.") (init-service))
        (= (clojure.string/upper-case line) "SALIR") (do (print "Muchas gracias, vuelva prontos!") (flush))
        (file_exists line) (do (process-file line))
        :else
        (do (println "Ruta invalida") (init-service))
        ))
    (catch Exception e
      (println "ERROR ->" (clojure.string/trim
                            (clojure.string/upper-case
                              (let [msg-err (get (Throwable->map e) :cause)]
                                (if (nil? msg-err) "desconocido" msg-err)))))
      (init-service)))
  )


(defn -main
  [& args]
  (init-service)
  (System/exit 0)
)