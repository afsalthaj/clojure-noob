(ns clojure-noob.experiment
  (:gen-class))

;; sample applicants
(def applications
  [{:id 1 :status "finalised" :job 17 :applicant "hameesh"}
   {:id 2 :status "finalised" :job 10 :applicant "afsal"}
   {:id 3 :status "finalised" :job 11 :applicant "thaj"}])

;; sample evaluations
(def evaluations
  [{:status "takethisone" :job 17 :application 1 :evaluator 3}
   {:status "finalised" :job 10 :application 2 :evaluator 1}
   {:status "finalised" :job 17 :application 1 :evaluator 1}
   {:status "finalised" :job 17 :application 1 :evaluator 2}
   {:status "takethisone" :job 11 :application 3}])

;; sample jobs
(def jobs
  [{:id 17 :invited_evaluators [1 2 3]}
   {:id 11 :invited_evaluators [1 2]}
   {:id 10 :invited_evaluators [3 2 4]}])

(defn evaluation-nv?
  "started but not completed evaluation\n"
  [evaluation]
  (= (:status evaluation) "nv"))

(defn evaluation-complete?
  "is evaluatioon complete?"
  [evaluation]
  (= (:status evaluation) "finalised"))

(defn not-to-be-evaluated?
  "is evaluation not to be evaluated?"
  [evaluation]
  (or (evaluation-nv? evaluation) (evaluation-complete? evaluation)))

;; is this evaluation record to be evaluated? (complement of not-to-be-evaluated?) 
(def to-be-evaluated? (complement not-to-be-evaluated?))

(defn finalised-application?
  "Finalised Application"
  [application]
  (= (:status application) "finalised"))

(defn get-finalised-applications
  "Finalised Applications"
  [applications]
  (into [] (filter finalised-application? applications)))

(defn get-completed-evaluations
  "Get all the completed evaluations that are either started or completed"
  [evaluations]
  (into [] (filter not-to-be-evaluated? evaluations)))

(defn get-applicants-completed-evaluations
  "Get all the applicants who are in the completed-evaluations registry"
  [evaluations]
  (into [] (map :application (get-completed-evaluations evaluations)) ))

(defn application-in-evaluation-completed-list?
  "If finalised application has completed evaluation"
  [evaluations application]
  (some #(= (:id application) %) (get-applicants-completed-evaluations evaluations)))

(def application-not-in-complete-evaluation-list?
  "If finalised application is not their in started/completed evaluation list"
  (complement application-in-evaluation-completed-list?))

(defn all-uncompleted-evaluations
  "target all finalised applications with pending evaluations"
  [applications evaluations]
  (into [] (->> applications (get-finalised-applications)
       (filter (partial application-not-in-complete-evaluation-list? evaluations)))))

(def firstresult
  "First result"
  ;;TODO For testing, will be removed
  (into [] (all-uncompleted-evaluations applications evaluations)))

(defn merge-job-application
  "Merge a job with various instances in applications sequence\n"
  [applications job]
  (->> applications (map #(if (= (:job %) (:id job)) (conj % job) %))
       (filter #(:invited_evaluators %))))

(defn get-job-application-records
  "Get the complete list of job and application record"
  ;;TODO: This can be a basic join fucntionality that can be abstracted out later on
  [jobs uncompletedevaluations-as-applications]
  (reduce (fn [collated-job-applications job]
            (into collated-job-applications
                  (merge-job-application uncompletedevaluations-as-applications job))) [] jobs))

(defn- explode-job-application-evaluation-row
  "Explode a row of record that connects job, application and evaluation "
  [row-of-application-job-record]
  (println row-of-application-job-record)
  (def evaluator-list (:invited_evaluators row-of-application-job-record))
  (reduce (fn [exploded-record evaluator]
            (into exploded-record
                  [{:evaluator evaluator
                    :job (:job row-of-application-job-record)
                    :applicant (:applicant row-of-application-job-record)}])) [] evaluator-list))

(defn- explode-evaluators-in-job-application-records
  "Explode the values of invitor_list into multiple rows"
  ;; TODO If more instances of this functionality comes up, we may need to abstract it further.
  [application-job-records]
  (flatten(map #(explode-job-application-evaluation-row %) application-job-records)))

(defn group-data-for-evaluators
  "Get all the evaluations to be done by a particular evaluator"
  [exploded-evaluator-data]
  (group-by :evaluator exploded-evaluator-data))

;; Final invocation
(defn pending-evaluations
  [applications evaluations jobs]
  (->>
    (all-uncompleted-evaluations applications evaluations)
    (get-job-application-records jobs)
    (explode-evaluators-in-job-application-records)
    (group-data-for-evaluators)
    ))