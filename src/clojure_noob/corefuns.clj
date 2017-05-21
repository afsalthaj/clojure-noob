(ns clojure-noob.core
  (:gen-class))

(reduce + [1 2 3 4])

;; this is different from the reduce function that you see
;; in languages like scala, where the concept is more like folding
;; with an initial value. Hence you can see the below functionality
;; in functions like `foldLeft` / `foldRight` in Scala.
(reduce + 15 [1 2 3 4])

;; now please refer to the `better-symmetric-body-parts`

;; one use of reduce function where a map is sent as
;; a sequence
(reduce (fn [new-map [key val]]
          (do (println (assoc new-map key (inc val)))
              (assoc new-map key (inc val))))
        {}
        {:max 30 :min 10})

(def food-journal
  [{:month 1 :day 1 :human 5.3 :critter 2.3}
   {:month 1 :day 2 :human 5.1 :critter 2.0}
   {:month 2 :day 1 :human 4.9 :critter 2.1}
   {:month 2 :day 2 :human 5.0 :critter 2.5}
   {:month 3 :day 1 :human 4.2 :critter 3.3}
   {:month 3 :day 2 :human 4.0 :critter 3.8}
   {:month 4 :day 1 :human 3.7 :critter 3.9}
   {:month 4 :day 2 :human 3.7 :critter 3.6}])

;;some function
(some #(> (:critter %) 5) food-journal)

(some #(> (:critter %) 3) food-journal)

;;The reason is that the anonymous function #(> (:critter %) 3) returns true or false. Here’s how you could return the entry:

(some #(and (> (:critter %) 3) %) food-journal)

;;simple count function
(count "aaaa")


;;sort

(sort [3 2 1])

(sort-by count ["aaaa" "bb" "c"])

(sort ["aaaa" "bb" "c"])

(repeat "na")

(take 8 (repeat "na"))

(take 3 (repeatedly (fn [] (rand-int 10))))


(defn even-numbers
  ([] (even-numbers 0))
  ([n] (cons n (lazy-seq (even-numbers (+ n 2))))))

(take 10 (even-numbers))

;; The Collection Abstraction
;; The collection abstraction is closely related to the sequence abstraction. All of Clojure’s core data structures—vectors, maps, lists, and sets—take part in both abstractions.


