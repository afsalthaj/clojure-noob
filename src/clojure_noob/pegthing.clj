(ns clojure-noob.pegthing
  (:require [clojure.tools.namespace.repl :refer [refresh]])
  (:gen-class))

;; Advanced concepts

;; A few simple examples to start with
;; lazy sequence of triangular numbers
;; a few more algorithmic functions
(defn tri*
  "Generates lazy sequence of triangular numbers"
  ([] (tri* 0 1))
  ([sum n]
   (let [new-sum (+ sum n)]
     (cons new-sum (lazy-seq (tri* new-sum (inc n)))))))

(def tri (tri*))

(defn triangular?
  "Checks if a given number is a triangular number"
  [n]
  (= n (last (take-while #(>= n %) tri))))


(defn row-tri
  "Triangular number at the end of a row"
  [n]
  (last (take n tri)))


(defn row-num
  "Returns row number the position belongs to: pos 1 in row 1,
  positions 2 and 3 in row 2, etc"
  [pos]
  (inc (count (take-while #(> pos %) tri))))

(defn connect
  "Form a mutual connection between two positions"
  [board max-pos pos neighbor destination]
  (if (<= destination max-pos)
    (reduce (fn [new-board [p1 p2]]
              (assoc-in new-board [p1 :connections p2] neighbor))
            board
            [[pos destination] [destination pos]])
    board))

;; about namespaces
(ns-name *ns*)


;; symbols
;; For now, all you need to know is that when you give Clojure a symbol like map,
;; it finds the corresponding var in the current namespace, gets a shelf address,
;; and retrieves an object from that shelf for you—in this case, the function that map refers to.
;; If you want to just use the symbol itself, and not the thing it refers to, you have to quote it.
;; Quoting any Clojure form tells Clojure not to evaluate it but to treat it as data.
;; The next few examples show what happens when you quote a form.

(map inc [1 2]) ;;=> both map an inc are symbols in clojure. Symbols are data types with in clojure


;; the functions such as `defn` 

(def somefun (fn [arg1 arg2] (+ arg1 arg2)))

;; can be written as 

(defn somefun [arg1 arg2] (+ arg1 arg2))

;; namespaces will be having maps. For example consider the below code
(def new-function "afsal thaj")
;; By defining this function you are updating the maps with a new association, that is between
;; `new-function` and var. The namespace will staore `afsal thaj`, get the address of the shelf and store it in a var.

somefun
;; return the var (#user/new-function)

;; so to repeat, the namespace will be a set of maps, consisting of association between
;; object names and the var, where the address of the implementation of the object is stored.
;; So in the above case the map will be:
;; my-fun #'clojure-noob.core/my-fun

;;This process is called interning a var. You can interact with a namespace’s map
;; of symbols-to-interned-vars using ns-interns. Here’s how you’d get a map of interned vars:

;;
;;(get (ns-interns *ns*) 'my-fun)
;;
;;user=> (count (ns-imports *ns*))
;;;;=> 96
;;
;;user=> (count (ns-interns *ns*))
;;;;=> 2
;;
;;user=> (count (ns-refers *ns*))
;;;;=> 590
;;
;;user=> (+ *1 *2 *3)
;;;;=> 688
;;
;;(count (ns-map *ns*))

(def great-books ["my book" "some other book"])

(deref #'clojure-noob.core/great-books)
; => ["East of Eden" "The Glass Bead Game"]
;; This is like telling Clojure, “Get the shelf number from the var, go to that shelf number, grab what’s on it,
;; and give it to me!”

great-books
; => ["East of Eden" "The Glass Bead Game"]
;;This is like telling Clojure, “Retrieve the var associated with great-books and deref that bad Jackson.”

;; in summary
;; given a symbol -> find the corresponding var in the map of
;; ns (ns-interns if they are internal functions) -> get the shelf number from the var -> go to the
;; shelf and retrieve the actual data/value

;; If you define a def with the same functon name, then the var has been updated with the address of the new vector.
;; It’s like you used white-out on the address on a card in the card catalog and then wrote a new address. The result
;; is that you can no longer ask Clojure to find the first vector. This is referred to as a name collision. Chaos! Anarchy!

;; switching namespace
;; some-name is a symbol, and we tell clojure it is a symbol.
;; clojure would create namespace with the name "some-name"
(in-ns 'some-name)

(comment
  ;; refering other namespaces in current namespace
  (clojure.core/refer 'cheese.taxonomy)

  ;; another example of reference

  (clojure.core/refer 'cheese.taxonomy :only ['bries])

  ;;Calls ns-interns on the cheese.taxonomy namespace
  ;;Merges that with the ns-map of the current namespace
  ;;Makes the result the new ns-map of the current namespace

  cheese.analysis=> (clojure.core/refer 'cheese.taxonomy :exclude ['bries])
  heese.analysis=> (clojure.core/refer 'cheese.taxonomy :rename {'bries 'yummy-bries})

  ;;there are more on namespaces in http:// www.braveclojure.com / organization/


  ;;important thing to note:
  ;;Dashes in namespace names correspond to underscores in the file­system.
  ;; So the-divine-cheese-code is mapped to the_divine_cheese_code on the filesystem.

  ;; An example of `require `and `refer `(ns the-divine-cheese-code.core)
  ;; Ensure that the SVG code is evaluated

  (require 'the-divine-cheese-code.visualization.svg)
  ;;Refer the namespace so that you don't have to use the
  ;;fully qualified name to reference svg.clj functions
  (refer 'the-divine-cheese-code.visualization.svg)

  ;;This shows that file svg.clj.clj exists in the directory visualization in the `the-divine-cheese-code `directory. This
  ;;directory exists in the root folder which is `src `when you create a lein project.
  ;;not that it is all symbol. It says, find the file that corresponds to this symbol using the
  ;; rules described in “The Relationship Between File Paths and Namespace Names” on page 133. In this case,
  ;; Clojure finds src / the_divine_cheese_code/visualization/svg.clj.clj.

  ;;require also lets you alias a namespace when you require it, using :as or alias. This:

  (require '[thenamespacedirectory.filenamewithoutclj :as svg])
  "In this case, you may call the defns and defs in the namespace as `svg.clj/something `
  Clojure provides another shortcut. Instead of calling require and refer separately,
  the function use does both. It’s frowned upon to use use in production code,
  but it’s handy when you’re experimenting in the REPL and you want to quickly get your hands on some functions.
  For example, this: "

  == Another example=====
  (require 'the-divine-cheese-code.visualization.svg)
  (refer 'the-divine-cheese-code.visualization.svg)
  (alias 'svg 'the-divine-cheese-code.visualization.svg)

  "is similar to"

  (use '[the-divine-cheese-code.visualization.svg :as svg])

  = (svg/some-fun) (some-fun)
  )


(comment
  "===============The NS macro==================
One useful task ns does is refer the clojure.core namespace by default.
That’s why you can call println from within the-divine-cheese-code.core without using the fully
qualified name, clojure.core/println"

  (ns the-divine-cheese-code.core
    (:refer-clojure :exclude [println]))

  is similat to

  (in-ns 'the-divine-cheese-code.core)
  (refer 'clojure.core :exclude ['println])

  (ns the-divine-cheese-code.core
    (:require the-divine-cheese-code.visualization.svg))

  (in-ns 'the-divine-cheese-code.core)
  (require 'the-divine-cheese-code.visualization.svg)

  (ns the-divine-cheese-code.core
    (:require [the-divine-cheese-code.visualization.svg :as svg]
              [clojure.java.browse :as browse]))

  ;; is equivalent to
  (in-ns 'the-divine-cheese-code.core)
  (require ['the-divine-cheese-code.visualization.svg :as 'svg])
  (require ['clojure.java.browse :as 'browse])

  ;;However, one difference between the (:require) reference and the require function
  ;; is that the reference also allows you to refer names. This:

  (ns the-divine-cheese-code.core
    (:require [the-divine-cheese-code.visualization.svg :refer [points]]))

  (in-ns 'the-divine-cheese-code.core)
  (require 'the-divine-cheese-code.visualization.svg)
  (refer 'the-divine-cheese-code.visualization.svg :only ['points])


  ;; BETTER NOT TO USE `USE`

  (in-ns 'the-divine-cheese-code.core)
  (use 'clojure.java.browse)
  (use 'clojure.java.io)

  ;; is similar to
  (ns the-divine-cheese-code.core
    (:use [clojure.java browse io]))


  )