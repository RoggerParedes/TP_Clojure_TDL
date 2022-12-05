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
(defn backtracking-sudoku-solver [grid row col]
  (
    cond
    ; We have reached the 8th row and 9th column, so it's finished!
    (and (= row (- N 1)) (= col N)) grid
    ; If column value becomes 9 we move to next row, and column start from 0
    (= col N) (backtracking-sudoku-solver grid (inc row) 0)
    ;  if the current position already contains value >0, we go for next column
    (pos? (get-element grid row col)) (backtracking-sudoku-solver grid row (inc col))
    :else
    ; Recursive function to test every posible value, starting from 1
    ((fn try-number [num]
       (cond
         ; if num > N, then this solution isn't viable
         (> num N) nil
         ; if is a viable to insert num in position, tries that path.
         ; If the path is a dead end, it backtracks and try the next number
         ; If is not posible to insert num, starts again with the next number
         (is-safe? grid row col num) (
                                       let [result (backtracking-sudoku-solver
                                                     (replace-element grid row col num) row col)]
                                       (if (nil? result) (try-number (inc num)) result))
         :else
         (try-number (inc num))
         )
       ) 1)
    )
  )


(defn sudoku-solver
  ([grid]
   (if (empty? grid) "Error: grid is empty"
     (let [solution (sudoku-solver grid 0 0)]
       (if (nil? solution) "Error: not solvable" solution)
       )
     )
   )
  ([grid row col]
   (
     cond
     ; We have reached the 8th row and 9th column, so it's finished!
     (and (= row (- N 1)) (= col N)) grid
     ; If column value becomes 9 we move to next row, and column start from 0
     (= col N) (backtracking-sudoku-solver grid (inc row) 0)
     ;  if the current position already contains value >0, we go for next column
     (pos? (get-element grid row col)) (backtracking-sudoku-solver grid row (inc col))
     :else
     ; Recursive function to test every posible value, starting from 1
     ((fn try-number [num]
        (cond
          ; if num > N, then this solution isn't viable
          (> num N) nil
          ; if is a viable to insert num in position, tries that path.
          ; If the path is a dead end, it backtracks and try the next number
          ; If is not posible to insert num, starts again with the next number
          (is-safe? grid row col num) (
                                        let [result (sudoku-solver (replace-element grid row col num) row col)]
                                        (if (nil? result) (try-number (inc num)) result))
          :else
          (try-number (inc num))
          )
        ) 1)
     )
   )
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


