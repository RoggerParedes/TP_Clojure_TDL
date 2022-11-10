(ns tp-clojure-tdl.core-test
  (:require [clojure.test :refer :all]
            [tp-clojure-tdl.core :refer :all]))

(deftest a-test
  (testing "FIXME, I fail."
    (is (= 1 1))))

(deftest b-test
  (testing "If the Inut file is not empty then return true")
  (is (= true (validate_file "data")))
)

(deftest c-test
  (testing "If the Inut file is empty then return False")
  (is (= false (validate_file "")))
)
    
(deftest d-test
  (testing "If we receive file with extension .json then, validate_extension_file return True"
    (is (= true (validate_extension_file "arch.json")))
  )
)

(deftest e-test
  (testing "If we receive file without extension .json then, validate_extension_file return False"
    (is (= false (validate_extension_file "arch.html")))
  )
)