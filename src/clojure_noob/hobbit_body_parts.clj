(ns clojure-noob.core
  (:gen-class))

;; binding the sequence of hashmaps to assym-hobbit-body-parts
(def assym-hobbit-body-parts [{:name "head" :size 3}
                              {:name "lef-eye" :size 1}
                              {:name "left-ear" :size 1}
                              {:name "mouth" :size 1}
                              {:name "nose" :size 1}
                              {:name "neck" :size 2}
                              {:name "left-shoulder" :size 3}
                              {:name "left-upper-arm" :size 3}
                              {:name "chest" :size 10}
                              {:name "back" :size 10}
                              {:name "left-forearm" :size 3}
                              {:name "abdomen" :size 6}
                              {:name "left-kidney" :size 1}
                              {:name "left-hand" :size 2}
                              {:name "left-knee" :size 2}
                              {:name "left-thigh" :size 4}
                              {:name "left-lower-leg" :size 3}
                              {:name "left-achilles" :size 1}
                              {:name "left-foot" :size 2}])

;; replace a string which is a value of a hashmap part
(defn replace-left-right [part]
  (def replaced-name (clojure.string/replace (:name part) #"^left-" "right-" ))
  {:name replaced-name :size (:size part)})

;; execute this command to see what happens
(replace-left-right {:name "left-data" :size 1})  

;; add more maps into the sequence where the new map will just replace the
;; word "left" from the values of the maps and add it as it is into the map
;; resulting in `finalbodyparts`
(defn symmetrize-body-part 
"Expects a sequence of maps of body parts with :name and :size"
[asym-body-parts]
  (loop [remaining-body-parts asym-body-parts 
         finalbodyparts []]
   (if (empty? remaining-body-parts)
      finalbodyparts
      (let [[first-body-part & rem-body-parts] remaining-body-parts]
           (recur rem-body-parts 
                (into finalbodyparts 
                     (set [first-body-part 
                          (replace-left-right first-body-part)])))))))

;; uncomment below code and run
(println (clojure.string/join "\n " (symmetrize-body-part assym-hobbit-body-parts)))
