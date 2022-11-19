(ns tp-clojure-tdl.core-test
  (:require [clojure.test :refer :all]
            [tp-clojure-tdl.core :refer :all]))

(deftest a-test
  (testing "FIXME, I fail."
    (is (= 1 1))))

(deftest b-test
  (testing "If the Inut file is not empty then return true")
  (is (= true (is_empty_name "data")))
)

(deftest c-test
  (testing "If the Inut file is empty then return False")
  (is (= false (is_empty_name "")))
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

(deftest f-test
  (testing "If file not exists then return false"
    (is (= false (file_exists "prueba2.txt")))
  )  
)


(deftest g-test
  (testing "If file exists then return true"
    (is (= true (file_exists "/home/rogger/prueba.txt")))
  )  
)

(deftest h-test
  (testing "It verify that the file not exists and not equal to true"
    (is (not= true (file_exists "pruebsa.txt")))
  ) 
)

(deftest i-test
  (testing "I want to remove the character '[' and ']'"
    (let [line (atom {:value "casa[]"})
          myVect (vector 
         {:character "]" :newCharacter ""} 
         {:character "[" :newCharacter ""}
        )] (do
             (remove_character myVect line) 
             (is (= "casa" (get @line :value)))))
  ) 
)