(ns clojure-noob.core
  (:gen-class))

(defn -main
  "I don't do a whole lot ... yet."
  [& args]
  (println "I am a little teapot"))

(defn train
  []
  (println "Chooo Choo"))

(defn simple-fun []
  (if false
    (do
      (println "my name is afsal")
      "my second name is thaj")

    (do
      (println "My name is not afsal")
      "My second name is not afsal")))

(defn simple-when []
  (when true
    "My name is afsal and here it ends, i don't have an else statement"
    )
  )

(def namedthisparameter [1 2 3 43 5])

(defn error-msg
  [severity]
  (str "OH GOD! ITS A DISASTER WE ARE "
       (if (= severity :mild)
         "SAVED"
         "DOOOOOOOOOOOOOOOOOOOOOOOOOOOOMMMMED!!!"
         )))



(def value "value is defined as value! Hehe")

(def name "Chewbacca")
(str "\"Uggllglglglglglglglll\" - " name)
;; => "Uggllglglglglglglglll" - Chewbacca

(def myhashmap {:a 1 :b 2})

(get myhashmap :a)

(get {:a "afsal" :b "sydney city"} :c "unicorns?")

(get-in {:id 1 :value {:name "afsal" :age 27}} [:value :name])

;Another way to look up a value in a map is to treat the map like a function with the key as its argument:
({:a "Afsal" :b "Thaj"} :a)

;;Keywords can be used as functions that look up the corresponding value in a data structure. For example, you can look up :a in a map:

(:a {:a "afsal" :b "thaj"})

;; that is equivalent to

(get {:a "Afsal" :b "Thaj"} :a)

(get {:a "Afsal" :b "Thaj"} :c "this is the default value")

;; hence a keyword can be considered as a default value
(:c {:a "Afsal" :b "Thaj"} "this is the default value")

;;vector
(get [1 2 3] 0)

;;Vector can consist of any type
(get [1 {:a "Afsal"} 2] 1)

;;create vectors with the vector function
(vector 1 2 3 4 5)

;;conj function to add additional elements into the vector
(conj [1 2 3] 4)


(nth '(:a :b :c) 2)

(nth '(1 2 3 4) 3)

(conj '(1 2 3 4) 5)

(conj [1 2 3 4] 5)

(set [1 2 3 2 2 2 2])

(contains? #{:a :b} :a)

(:a #{:a :b})

(:a #{:a "b"})

(get #{:a :b} :a)

(get #{"a" "b"} "a")

(defn multi-arity
  ;; 3-arity arguments and body
  ([first-arg second-arg third-arg]
   (println first-arg second-arg third-arg))
  ;; 2-arity arguments and body
  ([first-arg second-arg]
   (println first-arg second-arg))
  ;; 1-arity arguments and body
  ([first-arg]
   (println first-arg)))


(defn x-chop
  "Describe the kind of chop you're inflicting on someone"
  ([name chop-type]
   (println (str "I " chop-type " chop " name "! Take that!")))
  ([name]
   (println (str x-chop name "karate"))))


(defn something-takes-one [oneparameter]
  "This function takes just one parameter"
  (str "hi this handles just one parameter and that is " oneparameter))

(defn something-takes-multiple
  [& parameters]
  (map something-takes-one parameters))

;; variable inputs
(something-takes-multiple "afsal" "thaj" "this" "is" "multiple")

(defn favorite-thingss [nname & other-pparameters]
  (println (str "this is amazing " nname " " (clojure.string/join ", " other-pparameters))))


;;destructuring
(defn announce-map [{lat :lat lng :lng}]
  (println (str "latitude is " lat " and longitude is " lng)))

(announce-map {:lat 125 :lng 41})

;;look at the below function
(:a {:a "afsal" :b "thaj"})

(defn receive-treasure-function [{lat :lat}]
  (println lat))


(defn receive-another-function [{:keys [lat lng] :as treasure-location}]
  (println (str lat))
  (println (str lng))
  ;; this should work - wtf
  (str "steership!" treasure-location)

  )

(receive-another-function {:lat "afsal" :lng "thaj"})

(receive-another-function {:lat 1 :lng 2})

((fn [arg] (println arg)) "afsal")


(map (fn [x] (str "yea m" x)) [1 2 3 4 5])


(def i-named-function (fn [x] (str "this is " x)))

(i-named-function 1)

(#(identity %&) 1 "afsal" :afsal :yip "blarg")

;;closure

(defn this-returns-a-function [some-arg]
  #(mod % some-arg))

(def new-function (this-returns-a-function 2))

(new-function 2)

(loop [iteration 0]
  (println (str "Iteration " iteration))
  (if (>= iteration 3)
    (println "goodbye")
    (recur (inc iteration))))

;; Simple Loops
(loop [iteration 0]
  (println (str "Iteration" iteration))
  (if (> iteration 3)
    "Good Bye"
    (recur (inc iteration))))

(loop [iteration 0 iteration2 0 iteration3 0]
  (println (str "Iterations " iteration " " iteration2 " " iteration3))
  (if (and (> iteration 3) (> iteration2 3) (> iteration3 3))
    "It is done...Please come out of the loop"
    (recur (inc iteration) (inc iteration2) (inc iteration3))))

(loop [iterations []]
  (def xa (to-array iterations))
  (if (>= (alength xa) 3)
    iterations
    (recur (into iterations (set [1 1 1 1 1 1 1 1 1 1 1 1])))))


(into [] (set [:a :a]))
