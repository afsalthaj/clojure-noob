(ns clojure-noob.core
  (:gen-class))

(reduce + [1 2 3 4])

;; this is different from the reduce function that you see
;; in languages like scala, where the concept is more like folding
;; with an initial value. Hence you can see the below functionality
;; in functions like `foldLeft` / `foldRight` in Scala.
(reduce + 15 [1 2 3 4])

;; now please refer to the `better-symmetric-body-parts`
