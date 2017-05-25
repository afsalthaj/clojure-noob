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
;;he sequence abstraction is about operating on members individually, whereas the collection abstraction is about the data structure as a whole. For example, the collection functions count, empty?, and every? aren’t about any individual element; they’re about the whole:

(map identity {:sunlight-reaction "Glitter!"})

(into {} (map identity {:sunlight-reaction "Glitter!"}))

(map identity ["a" "b" "c"])

(into #{} (map identity ["a" "a" "b" "b" "c"]))

;;f into were asked to describe its strengths at a job interview, it would say, “I’m great at taking two collections and adding all the elements from the second to the first.”

;; clojure-noob.core> []
;; []
;; clojure-noob.core> (into [] [1 2 3 4 5])
;; [1 2 3 4 5]
;; clojure-noob.core> (into [1] [1 2 3 4 5])
;; [1 1 2 3 4 5]
;; clojure-noob.core> (conj [1] [1])
;; [1 [1]]

;;Notice that the number 1 is passed as a scalar (singular, non-collection) value, whereas into’s second argument must be a collection.

;; chained operation sequence
(filter #(> % 2) (map #(+ % 2) [1 2 3 4]))

;; is equivalent to
(->> [1 2 3 4] (map #(+ % 2)))
(filter #(> % 2))

(defn my-conj
  [target & additions]
  (into target additions))

(my-conj [0] 1 2 3)
; => [0 1 2 3]

;; clojure-noob.core> (conj [1 2 3 4] 5)
;; [1 2 3 4 5]
;; clojure-noob.core> (conj [1] 2 3 4 5)
;; [1 2 3 4 5]
;; (apply conj [1] [2 2 3 4])
;; [1 2 2 3 4]
;; clojure-noob.core> 
(def add10 (partial + 10))


(defn lousy-logger
  [log-level message]
  (condp = log-level
    :warn (clojure.string/lower-case message)
    :emergency (clojure.string/upper-case message)))

(def warn (partial lousy-logger :warn))

(warn "Red light ahead")
; => "red light ahead"

(defn vampire? [&restvalue] (true))

(def vampires [{:10 "afsal"} {:20 "thaj"}])

(defn vampire-related-details
  [integervalue]
  (get vampires integervalue))


;; complement
(defn identify-humans
  [social-security-numbers]
  (filter #(not (vampire? %))
          (map vampire-related-details social-security-numbers)))

(def not-vampire? (complement vampire?))

(defn identify-humans
  [social-security-numbers]
  (filter not-vampire?
          (map vampire-related-details social-security-numbers)))

;; apply function
(defn some-fun-taking-rest [restvalues]
  (println restvalues))


(defn idontknowmuchaboutargs [noideawhatthisis]
  (apply max noideawhatthisis))

(idontknowmuchaboutargs [1 2 3])


;;memoize

(defn sleepy-identity
  "Returns the given value after 1 second"
  [x]
  (Thread/sleep 1000)
  x)
(sleepy-identity "Mr. Fantastico")
; => "Mr. Fantastico" after 1 second

(sleepy-identity "Mr. Fantastico")
; => "Mr. Fantastico" after 1 second

(def memo-sleepy-identity (memoize sleepy-identity))

(memo-sleepy-identity "Mr. Fantastico")
; => "Mr. Fantastico" after 1 second

(memo-sleepy-identity "Mr. Fantastico")
; => "Mr. Fantastico" immediately



