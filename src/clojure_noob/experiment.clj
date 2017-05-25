(ns clojure-noob.core
  (:gen-class))

;; sample applicants
(def applications
  [{:id 1 :status "finalised" :job 17 :applicant "hameesh"} 
   {:id 2 :status "finalised" :job 10 :applicant "afsal"}
   {:id 3 :status "finalised" :job 11 :applicant "thaj"}])

;; sample evaluations
(def evaluations 
  [{:status "takethisone" :job 17 :application 1} 
   {:status "finalised" :job 10 :application 2}
   {:status "takethisone" :job 11 :application 3}])

;; sample jobs
(def jobs
  [{:id 17 :invited_evaluators [1 2 3]} 
   {:id 11 :invited_evaluators [1 2 3]}
   {:id 10 :invited_evaluators [3 2 4]}]) 

;; started but not completed evaluation
(defn evaluation-nv?
  [evaluation]
  (= (:status evaluation) "nv"))

;; evaluatioon complete
(defn evaluation-complete?
  [evaluation]
  (= (:status evaluation) "finalised"))

;; is this evaluation record not to be evaluated.
(defn not-to-be-evaluated?
  [evaluation]
  (or (evaluation-nv? evaluation) (evaluation-complete? evaluation)))
 
;; is this evaluation record to be evaluated? (complement of not-to-be-evaluated?) 
(def to-be-evaluated? (complement not-to-be-evaluated?))  

;; finalised application
(defn finalised-application?
  [application]
  (= (:status application) "finalised"))

;; finalised applications
(defn get-finalised-applications 
  [applications]
  (into [] (filter finalised-application? applications)))

;; completed or started evaluations/ evaluations that are not pending
(defn get-completed-evaluations 
  [evaluations]
  (into [] (filter not-to-be-evaluated? evaluations)))

;; applicant ids whose evaluations are not pending
(defn get-applicants-completed-evaluations 
  [evaluations] 
  (into [] (map :application (get-completed-evaluations evaluations))))

;; if finalised application is completed evaluation
(defn application-in-evaluation-completed-list?
  [evaluations application] 
  (some #(= (:id application) %) (get-applicants-completed-evaluations evaluations)))

;; if a finalised application is having a pending evaluation
(def application-not-in-complete-evaluation-list? (complement application-in-evaluation-completed-list?))

;; target all finalised applications with pending evaluations
(defn all-uncompleted-evaluations
  [applications evaluations]
  (->> applications (get-finalised-applications) (filter (partial application-not-in-complete-evaluation-list? evaluations))))

;; final function call
(def firstresult (into [] (all-uncompleted-evaluations applications evaluations)))

;; merge a job with various instances in applications sequence
(defn merge-job-application
  [applications job]
  (->> applications (map #(if (= (:job %)(:id job))(conj % job) %)) 
                    (filter #(:invited_evaluators %))))

;; get the complete list of job and application recor
(defn get-job-application-records
  [jobs uncompletedevaluations-as-applications]
  (reduce (fn[collated-job-applications job]
   (into collated-job-applications 
        (merge-job-application uncompletedevaluations-as-applications job))) [] jobs))

(defn explode-evaluators-in-job-application-records
 [application-job-records]

)
