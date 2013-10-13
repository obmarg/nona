{:layouts {"blog-index" {:template "template.html"
                         :snippet :#blog-index-snippet
                         :insertpoint :#blog-index}
           "index" {:template "template.html"
                    :snippet :#index-snippet
                    :insertpoint :#blog-index}
           "post" {:template "template.html"
                   :snippet :#.hentry
                   :insertpoint :#blog-index}
           }}
