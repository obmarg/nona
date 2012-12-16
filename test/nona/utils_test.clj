(ns nona.utils-test
  (:use midje.sweet
        nona.utils))

(fact
  "concat-vec-in-map concatenates a vector in a map"
  (concat-vec-in-map [3 4] {:one [1 2]} :one) => {:one [1 2 3 4]}
  )
(fact
  "concat-vec-in-map creates a vector if one doesn't exist"
  (concat-vec-in-map [1] {} :one) => {:one [1]}
  )

(fact
  "dup-vector-keys does nothing if no vector keys"
  (dup-vector-keys {:one ["one"] :two ["two"]}) => {:one ["one"] :two ["two"]}
  )
(fact
  "dup-vector-keys duplicates if vector keys"
  (dup-vector-keys
  {[:one :two] ["x"] :three ["3"] [:four :five] ["y"]}) => {:one ["x"]
                                                              :two ["x"]
                                                              :three ["3"]
                                                              :four ["y"]
                                                              :five ["y"]}
  )
(fact
  "dup-vector-keys doesn't overwrite existing keys" 
  (dup-vector-keys
    {[:one :two] ["x"] :one ["y"]}) => {:one ["y" "x"]
                                        :two ["x"]}
  )
(fact
  "dup-vector-keys merges vectors (rather than nesting)"
  (dup-vector-keys
    {[:one :two] ["x" "y"] :one ["z"]}) => {:one ["z" "x" "y"]
                                           :two ["x" "y"]}
  )
(fact
  "dup-vector-keys rejects non-maps"
  (dup-vector-keys "something") => (throws java.lang.AssertionError)
  )
