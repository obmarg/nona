(ns nona.render-test
  (:require nona.files)
  (:use midje.sweet
        nona.render))

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

(fact
  "get-page-data should handle page.content"
  (@#'nona.render/get-page-data test-page [:page :content]) => '("Something")
  )

(fact
  "get-page-data should handle page.layout"
  (@#'nona.render/get-page-data test-page [:page :layout]) => "layout"
  )

(fact
  "get-page-data should return empty string for missing data"
  (@#'nona.render/get-page-data test-page [:page :something]) => ""
  )

(fact
  "get-page-data should return empty string for non page data"
  (@#'nona.render/get-page-data test-page [:something :layout]) => ""
  )

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
