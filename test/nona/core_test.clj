(ns nona.core-test
  (:use midje.sweet
        nona.core))

(def sample-posts
  #{{:name "p1"
     :metadata {:title "One" :date "2010-10-05" :categories ["tech"]}}
    {:name "p2"
     :metadata {:title "Two" :date "2010-12-05" :categories ["tech" "cars"]}}
    {:name "p3"
     :metadata {:title "Three" :date "2011-10-05" :categories ["sausages"]}}
    })

; gen-index-pages tests
; provided is proving to be a pain in the arse AGAIN.
; Almost tempted to just use the clojure.test for these ones...
(future-fact
  "gen-index-pages handles sorting by title"
  (gen-index-pages {:order :title} sample-posts) => "page"
  (provided
    (#'nona.core/index-page 
        {:order :title} 
        nil 
        (as-checker (fn [x y xs] 
                 (print "in checker")
                 (= (map :name) '("p1" "p3" "p2"))))
        ) => "page"
    ))
(future-fact
  "gen-index-pages handles sorting by date"
  (gen-index-pages {:order :date} sample-posts) => "page"
  (provided
    (#'nona.core/index-page 
        {:order :date} 
        nil 
        (checker [xs] (= (map :name xs) '("p1" "p2" "p3")))
        ) => "page"
    ))
; Temporary not amazing tests, until I can figure out provided:
(fact
  "gen-index-pages generates a single page when no grouping"
  (gen-index-pages {:order :date} sample-posts) => '("page")
  (provided
    (#'nona.core/index-page 
        {:order :date} 
        nil 
        anything
        ) => "page" :times 1
    ))
(fact
  "gen-index-pages generates a page per group when grouping"
  (gen-index-pages {:order :date
                    :page-grouping :categories} 
                   sample-posts) => '("page", "page", "page")
  (provided
    (#'nona.core/index-page 
        {:order :date :page-grouping :categories} 
        "tech" 
        anything
        ) => "page" :times 1
    (#'nona.core/index-page 
        {:order :date :page-grouping :categories} 
        "cars" 
        anything
        ) => "page" :times 1
    (#'nona.core/index-page 
        {:order :date :page-grouping :categories} 
        "sausages" 
        anything
        ) => "page" :times 1
    ))

; index-page tests
(fact
  "index-page generates grouped pages ok"
  (@#'nona.core/index-page 
       {:title "Title: %s"
        :path "/blog/"
        :layout ...layout...}
       "tech"
       ...posts...) => {:dest-path "/blog/tech/index.html"
                        :title "Title: tech"
                        :posts ...posts...
                        :layout ...layout...}
  )
(fact
  "index-page generates non-grouped pages ok"
  (@#'nona.core/index-page 
       {:title "Archives"
        :path ""
        :layout ...layout...}
       nil
       ...posts...) => {:dest-path "/index.html"
                        :title "Archives"
                        :posts ...posts...
                        :layout ...layout...}
  )

; TODO: Write tests of page-from-post
