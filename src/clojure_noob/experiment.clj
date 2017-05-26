(ns clojure-noob.experiment
  (:gen-class)
  (:require [clojure.set :refer :all]))

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

(defn finalised-application?
  "Finalised Application"
  [application]
  (= (:status application) "finalised"))

(defn get-finalised-applications
  "Finalised Applications"
  [applications]
  (-> (->>  applications (filter finalised-application?))))

(defn get-completed-evaluations
  "Get all the completed evaluations that are either started or completed"
  [evaluations]
  (-> (->> evaluations (filter not-to-be-evaluated?)) (into [])))

(defn all-uncompleted-evaluations
  "target all finalised applications with pending evaluations"
  [applications]
  (-> (->> applications (get-finalised-applications)) (into [])))

(defn merge-job-application
  "Merge a job with various instances in applications sequence\n"
  [applications job]
  (->> applications
       (map #(clojure.set/rename-keys % {:id :application}))
       (map #(if (= (:job %) (:id job)) (conj % job) %))
       (filter :invited_evaluators )))

(defn get-job-application-records
  "Get the complete list of job and application record"
  [jobs uncompletedevaluations-as-applications]
  (reduce (fn [collated-job-applications job]
            (into collated-job-applications
                  (merge-job-application uncompletedevaluations-as-applications job))) [] jobs))

(defn- explode-job-application-evaluation-row
  "Explode a row of record that connects job, application and evaluation "
  [row-of-application-job-record]
  (def evaluator-list (:invited_evaluators row-of-application-job-record))
  (reduce (fn [exploded-record evaluator]
            (into exploded-record
                  [{:application (:application row-of-application-job-record)
                    :evaluator evaluator
                    :job (:job row-of-application-job-record)
                    :applicant (:applicant row-of-application-job-record)}])) [] evaluator-list))

(defn- explode-evaluators-in-job-application-records
  "Explode the values of invitor_list into multiple rows"
  [application-job-records]
  (->
    (->> application-job-records
         (map #(explode-job-application-evaluation-row %))
         (flatten)
    (into []))))

(defn group-data-for-evaluators
  "Get all the evaluations to be done by a particular evaluator"
  [exploded-evaluator-data]
  (group-by :evaluator exploded-evaluator-data))

(defn evaluator-completed-evaluation?
  [completed-evaluations intermediate-result]
  (some #(and
           (= (:application intermediate-result) (:application %))
           (= (:evaluator intermediate-result) (:evaluator %))
           (= (:job intermediate-result) (:job %)))
        completed-evaluations))

(def evaluator-not-completed-evaluation? (complement evaluator-completed-evaluation?))

;; Final invocation
(defn pending-evaluations
  [applications evaluations jobs]
  (->>
    (all-uncompleted-evaluations applications)
    (get-job-application-records jobs)
    (explode-evaluators-in-job-application-records)
    (filter #(evaluator-not-completed-evaluation? (get-completed-evaluations evaluations) %))
    (group-data-for-evaluators)
    ))