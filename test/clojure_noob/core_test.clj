(ns clojure-noob.core-test
  (:gen-class)
  (:require [clojure.test :refer :all]
            [clojure-noob.experiment :refer :all]))

(deftest empty-applications-and-non-empty-evaluations
  " Regardless of what is there in evaluations and jobs entity,
  if there are no applications in the application entity, there will be no output"
  (def applications [])

  (def evaluations
    {:status "takethisone" :job 17 :application 1 :evaluator 3})

  (def jobs
    [{:id 17 :invited_evaluators [1 2 3]}])

  (def testoutput {})

  (testing "if the core application works fine or not"
    (is (= (pending-evaluations applications  evaluations jobs) testoutput))))

(deftest test-when-application-not-exist-in-evaluations
  "The evaluations are empty. Hence the applications that are not in evaluations should be evaluated.
  However, if the corresponding jobs/evaluators are not present in job entity, they won't be in output"
  (def applications
    [{:id 1 :status "finalised" :job 1 :applicant "afsal"}
     {:id 2 :status "finalised" :job 2 :applicant "thaj"}])

  (def evaluations
    [])

  (def jobs
    [{:id 1 :invited_evaluators [1 2 3]}])

  (def testoutput
    {1 [{:application 1, :evaluator 1, :job 1, :applicant "afsal"}],
     2 [{:application 1, :evaluator 2, :job 1, :applicant "afsal"}],
     3 [{:application 1, :evaluator 3, :job 1, :applicant "afsal"}]})
  
  (testing "if the core application works fine or not"
    (is (= (pending-evaluations applications  evaluations jobs) testoutput))))

(deftest test-main-execution
  "This test case handles multiple tests"
  (def applications
    [{}
     {:id 1 :status "finalised" :job 17 :applicant "hameesh"}
     {:id 2 :status "finalised" :job 10 :applicant "afsal"}
     {:id 3 :status "finalised" :job 11 :applicant "thaj"}
     {:id 4 :status "finalised" :job 12 :applicant "anotherapplicant"}
     {:id 5 :status "finalised" :job 13 :applicant "mybro"}
     {:id 6 :status "finalised" :job 20 :applicant "hello"}])


  (def evaluations
    [{}
     {:status "takethisone" :job 17 :application 1 :evaluator 3}
     {:status "finalised" :job 10 :application 2 :evaluator 1}
     {:status "finalised" :job 17 :application 1 :evaluator 1}
     {:status "finalised" :job 17 :application 1 :evaluator 2}
     {:status "takethisone" :job 17 :application 1 :evaluator 7}
     ;; evaluator 8 is not listed in the invited evaluators and hence this evaluator won't be considered
     {:status "application-wont-consider-this-guy" :job 17 :application 1 :evaluator 8}
     {:status "takethisone" :job 13 :application 5 :evaluator 9}
     {:status "takethisone" :job 11 :application 3 :evaluator 4}
     {:status "takethisone" :job 20 :application 7}])

  (def jobs
    [{}
     {:id 17 :invited_evaluators [1 2 3]}
     {:id 11 :invited_evaluators [1 2 4]}
     {:id 10 :invited_evaluators [3 2 4]}
     {:id 12 :invited_evaluators [4 5 7]}
     {:id 13 :invited_evaluators [9 10]}
     {:id 20 :invited_evaluators [11, 12]}])

  (def testoutput
    {7 [{:application 4, :evaluator 7, :job 12, :applicant "anotherapplicant"}],
     1 [{:application 3, :evaluator 1, :job 11, :applicant "thaj"}],
     4 [{:application 3, :evaluator 4, :job 11, :applicant "thaj"}
        {:application 2, :evaluator 4, :job 10, :applicant "afsal"}
        {:application 4, :evaluator 4, :job 12, :applicant "anotherapplicant"}],
     3 [{:application 1, :evaluator 3, :job 17, :applicant "hameesh"}
        {:application 2, :evaluator 3, :job 10, :applicant "afsal"}],
     12 [{:application 6, :evaluator 12, :job 20, :applicant "hello"}],
     2 [{:application 3, :evaluator 2, :job 11, :applicant "thaj"}
        {:application 2, :evaluator 2, :job 10, :applicant "afsal"}],
     11 [{:application 6, :evaluator 11, :job 20, :applicant "hello"}],
     9 [{:application 5, :evaluator 9, :job 13, :applicant "mybro"}],
     5 [{:application 4, :evaluator 5, :job 12, :applicant "anotherapplicant"}],
     10 [{:application 5, :evaluator 10, :job 13, :applicant "mybro"}]})

  (testing "if the core application works fine or not"
    (is (= (pending-evaluations applications  evaluations jobs) testoutput))))