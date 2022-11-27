(ns tp-clojure-tdl.backtracking)

(def N 9)
(defn get-col [grid col]
  (map #(get % col) grid))

(defn get-row [grid row]
  (get grid row))

(defn get-square [grid row col]
  (let[i-row (* 3 (quot row 3)), i-col (* 3 (quot col 3))]
    (vec (flatten (mapv #(subvec % i-col (+ i-col 3)) (subvec grid  i-row (+ i-row 3))))
         ))
  )

(defn is-in [v elem]
  (if (some #{elem} v) true false)
  )

(defn get-element [grid row col]
  ((grid row) col))

(defn print_grid [grid]
    (run! println grid))

;should return true or false
(defn find_empty_location [grid l]
  (for [row (range 9)]
    (for [col (range 9)]
      (when (== (get-in grid [row col]) 0) 
        (def l (assoc l 0 row 1 col))))))

(defn is-in-row [grid row num]
  (is-in (get-row grid row) num))

(defn is-in-col [grid col num]
  (is-in (get-col grid col) num))

(defn is-in-square [grid row col num]
  (is-in (get-square grid row col) num))

(defn replace-element [grid row col num]
  (vec (map-indexed (fn [idx, itm] (if (= idx row) (assoc itm col num) itm)) grid))
  )

(defn is-safe? [grid row col num]
  (not (or
         (is-in-row grid row num)
         (is-in-col grid col num)
         (is-in-square grid row col num)
         )
       ))


(defn get-element [grid row col]
  ((grid row) col))
(defn backtracking-sudoku-solver[grid row col]
  (
    cond
    ; último casillero! devuelvo la grilla ya completa
    (and (= row (- N 1)) (= col N)) grid
    ; última columna? cambio de línea
    (= col N) (backtracking-sudoku-solver grid (inc row) 0)
    ; casillero no vacío? busco siguiente libre
    :else
    "nada"




    )


  )


(defn sudoku-solver [grid]
  (backtracking-sudoku-solver grid 0 0)
  ; l keeps record of the row and col in find_empty_location
  ;((def l [0 0])
   ;if there is no unassaigned location, it ends
   ;(not (find_empty_location grid l))

   ;row = l [0]
   ;col = l[1]

   ;for num in range(1, 10):
		
		;if(check_location_is_safe(arr,
		;				row, col, num)):
   
		;	arr[row][col]= num

		;	if(solve_sudoku(arr)):
		;		return True

		;	arr[row][col] = 0
				
	;return False
   )


(comment
  (def grid [[3 0 6 5 0 8 4 0 0]
             [5 2 0 0 0 0 0 0 0]
             [0 8 7 0 0 0 0 3 1]
             [0 0 3 0 1 0 0 8 0]
             [9 0 0 8 6 3 0 0 5]
             [0 5 0 0 9 0 6 0 0]
             [1 3 0 0 0 0 2 5 0]
             [0 0 0 0 0 0 0 7 4]
             [0 0 5 2 0 6 3 0 0]])
  )


