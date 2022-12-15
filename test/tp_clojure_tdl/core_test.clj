(ns tp-clojure-tdl.core-test
  (:require [clojure.test :refer :all]
            [tp-clojure-tdl.core :refer :all]))

(deftest remove-file-extension-test
  (testing "Remove file extension, letting only the name of the file"
    (is (= "hola" (remove-file-extension "hola.txt")))
    (is (= "hola" (remove-file-extension "hola.json")))
    ))

(deftest replace-in-str-test
  (testing "replace-in-str replace a substring for a given new string"
    (is (= "HÑÑa" (replace-in-str "Hola" "ol" "ÑÑ")))
    (is (= "Hola" (replace-in-str "Hola" "pa" "ÑÑ")))
    (is (= "167083000" (replace-in-str "167.83..." "." "0")))
    )
  )

(deftest clear-line-test
  (testing "clear special chars and let only numbers"
    (is (= "167083000300050008080000400000020000005000700600030002020007960906000200040002003"
           (clear-line "  167.83...|3...5...8|.8....4..|....2....|..5...7..|6...3...2|.2...796.|9.6...2..|.4...2..3  ")))
    ))

(deftest grid-from-line-test
  (testing "Generates a grid from a string containing 81 positions. If it can't returns nil"
    (is (nil? (grid-from-line "lalala")))
    (is (= [[1 6 7 0 8 3 0 0 0] [3 0 0 0 5 0 0 0 8] [0 8 0 0 0 0 4 0 0]
            [0 0 0 0 2 0 0 0 0] [0 0 5 0 0 0 7 0 0] [6 0 0 0 3 0 0 0 2]
            [0 2 0 0 0 7 9 6 0] [9 0 6 0 0 0 2 0 0] [0 4 0 0 0 2 0 0 3]]
           (grid-from-line "167.83...|3...5...8|.8....4..|....2....|..5...7..|6...3...2|.2...796.|9.6...2..|.4...2..3")))
    )
  )

(deftest grid-to-line-text
  (testing "Returns a line from a matriz, separating rows with a pipe ('|')"
    (let [grid [[1 6 7 0 8 3 0 0 0] [3 0 0 0 5 0 0 0 8] [0 8 0 0 0 0 4 0 0]
                 [0 0 0 0 2 0 0 0 0] [0 0 5 0 0 0 7 0 0] [6 0 0 0 3 0 0 0 2]
                 [0 2 0 0 0 7 9 6 0] [9 0 6 0 0 0 2 0 0] [0 4 0 0 0 2 0 0 3]]]
      (is (= "167083000|300050008|080000400|000020000|005000700|600030002|020007960|906000200|040002003"
             (grid-to-line grid)))
      )))