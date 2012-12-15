(ns nona.config-test
  (:use clojure.test
        nona.config))

(use-fixtures :each 
              (fn [f]
              (try
                (f)
                (finally
                  (reset! @#'nona.config/config @#'nona.config/defaults))
                )))

(deftest get-config-test
  (testing "get-config"
    (testing "should return set values"
      (set-config :tasty "oranges")
      (set-config {:nasty "pears"})
      (is (= (get-config :tasty) "oranges"))
      (is (= (get-config :nasty) "pears"))
      )
    (testing "should return nil if unset"
      (is (= (get-config :none) nil))
      )))

(deftest load-config-test
  (testing "load-config"
    (testing "should merge with default"
      (with-redefs [slurp (constantly "{}")]
        (is (= (load-config "file") @#'nona.config/defaults))
        ))
    (testing "should override defaults"
      (with-redefs [slurp (constantly "{:dest-dir \"something\"}")]
        (is (= (:dest-dir (load-config "file")) "something"))
      ))))

(deftest get-layout-test
  (testing "get-layout"
    (testing "should return layouts"
      (set-config :layouts {"layout1" "blah1" "layout2" "blah2"})
      (is (= (get-layout "layout1") "blah1"))
      (is (= (get-layout "layout2") "blah2"))
    (testing "should return nil if not there"
      (is (= (get-layout "missing") nil))
      ))))

