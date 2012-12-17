(ns nona.render-test
  (:require nona.files)
  (:use midje.sweet
        nona.render))
; TODO: check if using nona.files rather than requiring
;       fixes my mocking problems...

(fact "create-templates returns several templates"
      (create-templates
        {:one "file1" :two "file2"}) => {:one "temp1" :two "temp2"}
      (provided
        (#'nona.render/create-template "file1") => "temp1"
        (#'nona.render/create-template "file2") => "temp2"
        ))

(def test-template
  "<html>
    <body>
     <div id='content'>
       Blah blah blah Bloo bloo bloo
       <div id='container'>
         Hello!<span class='content'></span>Goodbye!
       </div>
       Something
     </div>
    </body>
   </html>")

(def test-page
  {:name "Nona"
   :content "Something"
   :metadata {:layout "layout"}
   })

(def test-layouts
  {:one {:template "template.html"
         :snippet :.container
         :insertpoint :.content}})

(def expected-output
  #".*<div id='content'>\s+Hello!<span class='content'>Something</span>Goodbye!</div>.*"
  )

; This next one always fails (seemingly the provided just
; isn't working, as it still tries to load a file)
; Would be nice to get it working somehow...
(future-fact "the templates from create-templates work properly"
      (let [templates (create-templates test-layouts)]
        (apply str ((:one templates) test-page)) => expected-output
        )
      (provided
        (get-template-file "template.html") => (java.io.StringReader test-template)
        ))

; TODO: Write tests of render-page

;
; handle-data-text tests
;

(defn page-data-fn 
  "An empty function to mock out"
  [keywords])

(def test-node {:attrs {:data-text "first.second"}})

(fact
  "handle-data-text should split out keywords and return page data"
  (let [f (@#'nona.render/handle-data-text #'page-data-fn)]
    (f test-node) => (assoc test-node :content "woo")
    )
  (provided
    (page-data-fn [:first :second]) => "woo"
    ))

;
; attr-matches tests
;

(fact
  "attr-matches returns empty if no attrs"
  (@#'nona.render/attr-matches #"" {}) => ()
  )
(fact
  "attr-matches returns empty if no attrs"
  (@#'nona.render/attr-matches #"" {:attrs {}}) => ()
  )
(fact
  "attr-matches returns empty if no matching attrs"
  (@#'nona.render/attr-matches #"" {:attrs {:something ""}}) => ()
  )
(fact
  "attr-matches returns list of matching attrs"
  (@#'nona.render/attr-matches 
       #"^data-.*"
       {:attrs {:data-x "" :data-y "" :link ""}}) => '(:data-y :data-x)
  )
(fact
  "attr-matches rejects non-regexp arguments"
  (@#'nona.render/attr-matches "" {}) => (throws java.lang.AssertionError)
  )

;
; split-keywords tests
;

(fact
  "split-keywords returns a vector of keywords"
  (@#'nona.render/split-keywords "one.two.three") => [:one :two :three]
  )
(fact
  "split-keywords returns a vector of one keyword if no dots"
  (@#'nona.render/split-keywords "one") => [:one]
  )
