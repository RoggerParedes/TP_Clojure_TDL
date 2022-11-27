(ns tp-clojure-tdl.backtracking)

(defn get-col [grid col]
  (map #(get % col) grid))

(defn get-row [grid row]
  (get grid row))

(defn is-in [v elem]
  (if (some #{elem} v) true false)
  )

(defn get-square [grid row col]
  (let[i-row (* 3 (quot row 3)), i-col (* 3 (quot col 3))]
    (vec (flatten (mapv #(subvec % i-col (+ i-col 3)) (subvec grid  i-row (+ i-row 3))))
         ))
  )
  (defn print_grid [grid]
    (run! println grid)
  )

;should return true or false
(defn find_empty_location [grid l]
  (for [row (range 9)]
    (for [col (range 9)]
      (when (== (get-in grid [row col]) 0) 
        (def l (assoc l 0 row 1 col))))))

;should return true or false
(defn used_in_box [grid row col num]
  (for [i (range 3)]
    (for [j (range 3)]
      (when (== (get-in grid [(+ i row) (+ j col)]) num)
        (def used true)))))

(defn used_in_row [grid row num]
  (some #{num} (get grid row)))

;should return true or false
(defn used_in_col [grid col num]
  {:pre [(def used (vector)) 
         (for [row (range 9)]
           (conj used (== (get-in grid [row col]) num)))]}
  (used))

(defn check_location_is_safe [grid row col num]
  (and (not (used_in_row grid row num))
       (not (used_in_col grid col num))
       (not (used_in_box grid (- row (mod row 3)) (- col (mod col 3)) num))))



(defn solve_sudoku[grid]
  ; l keeps record of the row and col in find_empty_location
  ((def l [0 0])
   ;if there is no unassaigned location, it ends
   (not (find_empty_location grid l))

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


