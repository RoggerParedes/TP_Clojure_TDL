(defproject tp_clojure_tdl "0.1.0-SNAPSHOT"
  :description "Sudoku Solver"
  :url "https://github.com/RoggerParedes/tp_clojure_tdl"
  :license {:name "EPL-2.0 OR GPL-2.0-or-later WITH Classpath-exception-2.0"
            :url "https://www.eclipse.org/legal/epl-2.0/"}
  :dependencies [[org.clojure/clojure "1.11.1"]
                 [org.clojure/core.async "1.6.673"]]
  :main ^:skip-aot tp-clojure-tdl.core
  :target-path "target/%s"
  :profiles {:uberjar {:aot :all
                       :jvm-opts ["-Dclojure.compiler.direct-linking=true"]}})
