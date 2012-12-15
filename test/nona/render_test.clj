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
   :content "Something"})

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

