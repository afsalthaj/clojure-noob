(ns clojure-noob.core-test
  (:require [clojure.test :refer :all]
            [clojure-noob.experiment :refer :all]))

(deftest evaluation-nv-test
  (testing "if a particular evaluation is nv"
    (is (= (evaluation-nv? (first evaluations)) false))))