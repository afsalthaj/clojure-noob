(ns clojure-noob.divine-cheese-code.concurrency)

(defmacro wait
  [timeout body]
  `(do (Thread/sleep ~timeout) ~body))

(defn testconcurr 
  [] 
  (time (let [saying3 (promise)]
          (future (deliver saying3 (wait 4000 "Cheerio")))
          (let [saying2 (promise)]
            (future (deliver saying2 (wait 1000 "Pip Pip!!")))
            (let [saying1 (promise)]
              (future (deliver saying1 (wait 2000 "Ello, gove na")))
              (println @saying1))
            (println @saying2))
          (println @saying3))))
