(ns nona.posts-test
  (:require [markdown.core])
  (:use midje.sweet
        nona.posts
        [nona.files :only (relative-path)]
        [clojure.java.io :only (file)]
        ))

; make-post tests
; Yet another one I can't get provided to work properly
; with...
(future-fact
  "make-post makes a post from a file and some input"
  (make-post (file "file.md") "data") => {:name "file"
                                          :content ...content...
                                          :src-path ...relpath...
                                          :metadata "metadata"}
  (provided
    (#'relative-path (file "file.md")) => ...relpath...
    (#'nona.posts/split-metadata "data") => ["metadata" "data"]
    (#'nona.posts/transform-data "data") => ...content...
    ))

; get-render-context tests
(fact
  "get-render-context uses metadata and content"
  (get-render-context {:content "content"
                          :name "something"
                          :metadata {:one "one" :two "two"}
                          }) => {:content "content"
                                 :one "one"
                                 :two "two"}
  )

; split-metadata tests
(fact
  "split-metadata extracts metadata from yaml header"
  (@#'nona.posts/split-metadata 
       "---\ntest: something\n---\ncontent") => [{:test "something"}
                                                 "content"]
  )
(fact
  "split-metadata just returns data if no yaml header"
  (@#'nona.posts/split-metadata "content") => [nil "content"]
  )

; filename-metadata tests
(fact
  "filename-metadata extracts a date and title"
  (@#'nona.posts/filename-metadata 
       "2010-10-20-a-post-by-me") => {:title "a post by me"
                                      :date "2010-10-20"}
  )
(fact
  "filename-metadata only extracts date if it's there"
  (@#'nona.posts/filename-metadata 
       "a-post-by-me") => {:title "a post by me"}
  )

; transform-data tests
(fact
  "transform-data transforms markdown"
  (@#'nona.posts/transform-data ".md" "data") => "output"
  (provided
    (#'markdown.core/md-to-html-string "data") => "output"
    ))
(fact
  "transform-data just returns ntml"
  (@#'nona.posts/transform-data ".html" "data") => "data"
  )
