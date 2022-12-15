(ns tp-clojure-tdl.core
  (:gen-class)
  (:require [clojure.java.io :as io])
  (:require [clojure.string :as string])
  (:require [clojure.core.async
             :refer [>! <! put! go-loop chan close!]])
   (:require [tp-clojure-tdl.backtracking 
              :refer :all ])
)

(def line (atom {:value ""}))
(def total-solved (atom 0))
(def output-filename (atom "salida.txt"))

(def channel_create_file (chan) )

(def channel_resolve_data (chan) )

(def  thread_1
  (future
    (go-loop []
      (when-some [x (<! channel_resolve_data)]
        (>! channel_create_file x)
        (recur)))
    ))

(def  thread_2
  (future
    ( go-loop []
      (when-some [x (<! channel_create_file)]
        (spit @output-filename (str x "\n") :append true)
        (recur)))
    ))

(defn file-exists? [filename]
  (if (.exists (io/file filename)) true false)
)

(defn replace-in-str [line to-be-replaced replacement]
  (clojure.string/replace line
                          (re-pattern (if (< 1 (count to-be-replaced))
                                        to-be-replaced
                                        (str "[" to-be-replaced "]")))
                          replacement))

(defn clear-line [line]
  (let [new-line (replace-in-str line "." "0")]
    (apply str (re-seq (re-pattern "[\\d]") new-line))
    )
  )

(defn grid-from-line [line]
  (let [new-line (clear-line line)]
    (if (= 81 (count new-line))
      ; Convert to int all chars (that we already know they are numbers)
      (let [seq-num (map #(Character/digit % 10) (seq new-line))]
        ; Split the string into vectors of 9 elems (each line), and then generates the "matrix" or grid
        (into (vector) (map #(into (vector) %) (partition 9 seq-num))))
      nil)
    )
  )

(defn grid-to-line [grid]
  (reduce #(str %1 "|" %2) (map (partial apply str) grid)))

(defn format-sudoku-line [arr]
  (let [line (mapv #(if (zero? %) " " %) arr)]
  (vec (flatten (reduce #(vector %1 '| %2) (partition 3 line) )))))

(defn print-grid [grid]
  (loop [i 0]
    (when (< i N)
      (when (and (zero? (mod i 3)) (pos? i)) (println (vec (repeat 11 '-))))
      (println (format-sudoku-line (grid i)))
      (recur (inc i)))))


(defn remove-file-extension [filename]
  (replace-in-str filename (first (re-find #"(\.[a-zA-Z0-9]+)$" filename)) ""))
(defn process-file-v2 [filename]
  (swap! output-filename (partial str (remove-file-extension filename)))
  (with-open [rdr (io/reader filename)]
    (doseq [line_ (line-seq rdr)]
      (swap! line assoc :value line_)
      (let [grid (grid-from-line (get @line :value))]
        (if (empty? grid)
          (do
            (println (apply str (repeat 25 '*)))
            (printf "La línea %s no pertenece a un sudoku válido%n" (get @line :value)) (flush)
            (println (apply str (repeat 25 '*))))
          (let [grid-solved (sudoku-solver grid)]
            (if (nil? grid-solved)
              (do
                (println (apply str (repeat 25 '*)))
                (printf "La línea %s no pertenece a un sudoku válido%n" (get @line :value)) (flush)
                (println (apply str (repeat 25 '*))))
              (do
                ; console output
                (println (apply str (repeat 25 '*))) ; separator
                (println "Sudoku original:")
                (print-grid grid)
                (println "Sudoku resuelto:")
                (print-grid grid-solved)
                (println (apply str (repeat 25 '*))) ; separator
                ; write in file
                (put! channel_resolve_data (grid-to-line grid-solved))
                ; increment counter
                (swap! total-solved inc)
                )
              )
            )
          )))))

(defn close-app [state]
  (when (= state true)
    (println "Cerrando Aplicacion")
    (close! channel_create_file)
    (close! channel_resolve_data)
    (future-cancel thread_1)
    (future-cancel thread_2)
    (shutdown-agents)
    (println "Fin"))
  )

(defn init-service []
  (println "Ingrese la ruta del Archivo o escriba SALIR para terminar:")
  (try
    (let [line (clojure.string/trim (read-line))]
      (cond
        (empty? line) (do (println "ERROR: no se ingresó nada.") (init-service))
        (= (clojure.string/upper-case line) "SALIR") (do (print "Muchas gracias, vuelva pronto!") (flush))
        (file-exists? line) (do (process-file-v2 line) (printf "Cantidad de sudokus resueltos: %d%n" @total-solved))
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
  (close-app true)
)