(ns clojure-noob.core
  (:gen-class))
(def sample-applications
  [{:id 1 :status "finalised" :job 17 :applicant "hameesh"}
   {:id 2 :status "finalised" :job 10 :applicant "afsal"}
   {:id 3 :status "finalised" :job 11 :applicant "thaj"}])


(def sample-evaluations
  [{:status "takethisone" :job 17 :application 1 :evaluator 3}
   {:status "finalised" :job 10 :application 2 :evaluator 1}
   {:status "finalised" :job 17 :application 1 :evaluator 1}
   {:status "finalised" :job 17 :application 1 :evaluator 2}
   {:status "takethisone" :job 11 :application 3 :evaluator 4}])

(def sample-jobs
  [{:id 17 :invited_evaluators [1 2 3]}
   {:id 11 :invited_evaluators [1 2 4]}
   {:id 10 :invited_evaluators [3 2 4]}])

(def expected
  { 3 [{:application 1, :evaluator 3, :job 17, :applicant "hameesh"}],
   1 [{:application 3, :evaluator 1, :job 11, :applicant "thaj"}],
   2 [{:application 3, :evaluator 2, :job 11, :applicant "thaj"}]
   4 [{:application 3, :evaluator 4, :job 11, :applicant "thaj"}]})

(defn evaluation-nv?
  "started but not completed evaluation\n"
  [evaluation]
  (= (:status evaluation) "nv"))

(defn evaluation-complete?
  "is evaluatioon complete?"
  [evaluation]
  (= (:status evaluation) "finalised"))

(defn is-application-in-evaluations?
  [finalised-application evaluations]
  (some #(=(:id finalised-application)(:application %)) evaluations))

(def is-application-not-in-evaluations? (complement is-application-in-evaluations?))

(defn jobid->jobs->evaluators
  [jobs jobid]
  (into #{} (:invited_evaluators (first(filter #(= (:id %) jobid) jobs)))))

(defn application->evaluations->evaluators[application evaluations]
  (into #{} (map :evaluator (filter #(=(:application %) (:id application)) evaluations))))

(defn not-to-be-evaluated?
  "is evaluation not to be evaluated?"
  [evaluation]
  (or (evaluation-nv? evaluation) (evaluation-complete? evaluation)))

(def to-be-evaluated?
  "Is this evaluation record to be evaluated? (complement of not-to-be-evaluated?) "
  (complement not-to-be-evaluated?))

(defn is-finalised-application-to-be-evaluated?
  "Given a finalised application and a set of evaluations, verify if the application should be evaluated?"
  [evaluations jobs finalised-application ]
  ;; they needn't be in a single loop and make things complicated as they are chained by an or condition
  (or (is-application-not-in-evaluations? finalised-application evaluations)
      (some #(and (=(:id finalised-application)(:application %)) (to-be-evaluated? %)) evaluations)
      (not (=(jobid->jobs->evaluators jobs (:job finalised-application))(application->evaluations->evaluators finalised-application evaluations)))))

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

(defn all-uncompleted-evaluations
  "target all finalised applications with pending evaluations"
  [applications evaluations jobs]
  (->
    (->> applications
         (get-finalised-applications)
         (filter #(is-finalised-application-to-be-evaluated? evaluations jobs %)))
    (flatten)
    (into [])))

(defn merge-job-application
  "Merge a job with various instances in applications sequence\n"
  [applications job]
  (def id->application (map #(clojure.set/rename-keys % {:id :application}) applications))
  (->> id->application (map #(if (= (:job %) (:id job)) (conj % job) %))
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
  (def evaluator-list (:invited_evaluators row-of-application-job-record))
  (reduce (fn [exploded-record evaluator]
            (into exploded-record
                  [{:application (:application row-of-application-job-record)
                    :evaluator evaluator
                    :job (:job row-of-application-job-record)
                    :applicant (:applicant row-of-application-job-record)}])) [] evaluator-list))

(defn- explode-evaluators-in-job-application-records
  "Explode the values of invitor_list into multiple rows"
  ;; TODO If more instances of this functionality comes up, we may need to abstract it further.
  [application-job-records]
  (into [] (flatten(map #(explode-job-application-evaluation-row %) application-job-records))))

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
    (all-uncompleted-evaluations applications evaluations jobs)
    (get-job-application-records jobs)
    (explode-evaluators-in-job-application-records)
    (filter #(evaluator-not-completed-evaluation? (get-completed-evaluations evaluations) %))
    (group-data-for-evaluators)
    ))