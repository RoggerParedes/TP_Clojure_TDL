(ns tp-clojure-tdl.backtracking-test
  (:require [clojure.test :refer :all]
            [tp-clojure-tdl.backtracking :refer :all]))

(def grid [[3 0 6 5 0 8 4 0 0]
           [5 2 0 0 0 0 0 0 0]
           [0 8 7 0 0 0 0 3 1]
           [0 0 3 0 1 0 0 8 0]
           [9 0 0 8 6 3 0 0 5]
           [0 5 0 0 9 0 6 0 0]
           [1 3 0 0 0 0 2 5 0]
           [0 0 0 0 0 0 0 7 4]
           [0 0 5 2 0 6 3 0 0]])

(deftest get-row-test
  (testing "Returns the nth row (starting from 0)"
    (is (= [3 0 6 5 0 8 4 0 0] (get-row grid 0)))
    (is (= [0 0 5 2 0 6 3 0 0] (get-row grid 8)))
    )
  )

(deftest get-col-test
  (testing "Returns the nth col (starting from 0)"
    (is (= [3 5 0 0 9 0 1 0 0] (get-col grid 0)))
    (is (= [8 0 0 0 3 0 0 0 6] (get-col grid 5)))
    )
  )

(deftest get-square-test
  (testing "Returns the elements of the square corresponding to a given row and column"
    (is (= [3 0 6 5 2 0 0 8 7] (get-square grid 0 2)))
    (is (= [5 0 8 0 0 0 0 0 0] (get-square grid 2 4)))
    (is (= [2 5 0 0 7 4 3 0 0] (get-square grid 8 8)))
    )
  )

(deftest is-in-test
  (testing "Checks if an element is in an array"
    (is (true? (is-in [1 2 3 5] 5)))
    (is (false? (is-in [1 2 3 5] 4)))
    )
  )

(deftest is-in-row-test
  (testing "Checks if a num is in certain row"
    ;row 1: [5 2 0 0 0 0 0 0 0]
    (is (true? (is-in-row grid 1 2)))
    (is (false? (is-in-row grid 1 3)))
    )
  )

(deftest is-in-col-test
  (testing "Checks if a num is in certain column"
    ;col 0:  [3 5 0 0 9 0 1 0 0]
    (is (true? (is-in-col grid 0 9)))
    (is (false? (is-in-col grid 0 8)))
    )
  )

(deftest is-in-col-test
  (testing "Checks if a num is in certain column"
    ;last square :[ 2 5 0
    ;               0 7 4
    ;               3 0 0]
    (is (true? (is-in-square grid 8 8 4)))
    (is (false? (is-in-square grid 8 8 1)))
    )
  )

(deftest replace-element-test
  (testing "Replace the element placed in grid[row][col] for the given value"
    ;row 2: [0 8 7 0 0 0 0 3 1]
    (is (=  [0 8 7 0 9 0 0 3 1] (get (replace-element grid 2 4 9) 2)))
    (is (=  [1 8 7 0 0 0 0 3 1] (get (replace-element grid 2 0 1) 2)))
    ))

(deftest is-safe?-test
  (testing "Returns true if the number is a candidate (not in square, row or column)"
    (comment [[3 0 6 5 0 8 4 0 0]
              [5 2 0 0 0 0 0 0 0]
              [0 8 7 0 0 0 0 3 1]
              [0 0 3 0 1 0 0 8 0]
              [9 0 0 8 6 3 0 0 5]
              [0 5 0 0 9 0 6 0 0]
              [1 3 0 0 0 0 2 5 0]
              [0 0 0 0 0 0 0 7 4]
              [0 0 5 2 0 6 3 0 0]])
    (is (true? (is-safe? grid 0 1 9)))  ; candidate
    (is (false? (is-safe? grid 0 1 3))) ; number in the same row
    (is (false? (is-safe? grid 0 1 2))) ; number in the same column
    (is (false? (is-safe? grid 0 1 7))) ; number in the same square
    ))