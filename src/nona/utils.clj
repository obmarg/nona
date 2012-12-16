(ns nona.utils)

(defn concat-vec-in-map
  "Updates a vector inside a map by concatenating it with v
   Creates a vector if there isn't one"
  [v m k]
  (assoc m k (vec (concat (m k) v)))
  )

(defn dup-vector-keys
  "This function takes a map that may have vectors for keys
   and uses each of the individual members of the vector
   as keys in the top level collection.

   So {[:one :two] :something} becomes {:one :something
                                        :two :something}
   
   Designed to be composed with group-by when the the keyfn
   may return a vector"
  [m]
  {:pre [(associative? m)]}
  (let [f (fn [m k v]
            (if (sequential? k)
              (reduce (partial concat-vec-in-map v) m k)
              (concat-vec-in-map v m k)
              ))]
    (reduce-kv f {} m)
    ))

(def group-by-single-key (comp dup-vector-keys group-by))
