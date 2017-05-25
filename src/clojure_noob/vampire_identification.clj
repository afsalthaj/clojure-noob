(ns clojure-noob.core
  (:gen-class))

(def filename "suspects.csv")

(def vamp-keys [:name :glitter-index])

(defn str->int
  [str]
  (Integer. str))

(def conversions {:name          identity
                  :glitter-index str->int})

(defn convert
  [vamp-key value]
  ((get conversions vamp-key) value))

;;parse the file
(defn parse
  "Convert a CSV into rows of columns"
  [string]
  (map #(clojure.string/split % #",")
       (clojure.string/split string #"\n")))


(parse (slurp filename))

(defn mapify
  "Return a seq of maps like {:name \"Edward Cullen\" :glitter-index 10}"
  [rows]
  (map (fn [unmapped-row]
         (reduce (fn [row-map [vamp-key value]]
                   (assoc row-map vamp-key (convert vamp-key value)))
                 {}
                 (map vector vamp-keys unmapped-row)))
       rows))


(first (mapify (parse (slurp filename))))
; => {:glitter-index 10, :name "Edward Cullen"}

(defn glitter-filter
  [minimum-glitter records]
  (filter #(>= (:glitter-index %) minimum-glitter) records))

(glitter-filter 3 (mapify (parse (slurp filename))))
({:name "Edward Cullen", :glitter-index 10}
  {:name "Jacob Black", :glitter-index 3}
  {:name "Carlisle Cullen", :glitter-index 6})
