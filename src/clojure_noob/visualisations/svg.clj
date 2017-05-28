(ns clojure-noob.visualisations.svg
  (:require [clojure.string :as s])
  (:refer-clojure :exclude [min max]))

(defn latlng->point
  "Conver Lat and Lon to comma separated string"
  [latlng]
  (str (:lat latlng) ", " (:lng latlng)))

(defn points
  [locations]
  (s/join " " (->> locations (map latlng->point))))

(defn comparator-over-maps
  [comparator-fn keys]
  (fn [maps]
    (zipmap keys
            (map (fn [key]
                   (apply comparator-fn (map key maps))) keys))))