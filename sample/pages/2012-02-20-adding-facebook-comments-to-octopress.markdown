---
layout: post
title: "Adding Facebook Comments To Octopress"
date: 2012-02-20 18:46
comments: true
categories: [ octopress, facebook, blogging, comments, hacking, technology ] 
description: "A quick description of how to add facebook comments to an octopress blog"
keywords: "octopress comments, comments, octopress, blogging, hacking, technology, facebook, octopress facebook comments"
---
Today I decided to have a go at adding comments to my blog.  Octopress comes with built in support for comments using [disqus](http://disqus.com), but I wanted to try and do something a little different.  Facebook comments seemed like a reasonable alternative - there's no additional login required, all posts have to be backed by a facebook profile and it's fairly easy to setup.  I also like the option to reduce visibility of comments - in this mode, comments will only be viewable by the poster, an their friends.  Sounds like a fairly good way to cut down on crap.

In an attempt to save myself a little learning time I had a look on google for a guide to setting up facebook comments on Octopress, but had no luck finding anything.  So, I'm going to walkthrough the (fairly straightforward) process here, in case anyone else wants to try it out.

<!-- more -->

The first thing you'll need to do, is [register a facebook app](https://developers.facebook.com/apps) for your blog.  Once you've done this, facebook should give you an app id.  To make things easier should you need to change the app id, it's good to store this id in a variable in your octopress configuration file.  So, in _config.yml, add something like this

``` yaml
# Facebook comments
facebook_appid: 222612811167194
```


Next, you'll need to include the facebook javascript API in your page.  Assuming you're using the facebook like functionality built into octopress this has mostly been dealt with, with one slight modification needed - the default octopress code doesn't contain your app id.  So, open up `source/_includes/facebook_like.html` and update the line beginning with `js.src=` to this:

``` javascript
js.src = "//connect.facebook.net/en_US/all.js#appId={{ site.facebook_appid }}&xfbml=1";
```

Now that you've added the javascript API into your page, you need to add the actual comments to your pages.  Create a file `source/_includes/post/facebook_comments.html` and insert the following:

{% literal %}
```  html
<noscript>Please enable JavaScript to view the comments powered by facebook</a></noscript>
<div 
  class="fb-comments" 
  data-href="{{ site.url }}{{ page.url }}" 
  data-num-posts="2"
  data-width="470"
  data-colorscheme="dark" ></div>
```
{% endliteral %}

You can tweak the colorscheme, width and number of posts used to your liking.  This file will not be included in your Octopress posts by default, you'll need to add it in yourself.  Octopress pages &amp; posts are built using the `post.html` and `page.html` files in `source/_layouts`.  So, open them up and add in the following after (or before) the disqus section:

{% literal %}
```
{% if site.facebook_appid and page.comments == true %}
  <section>
    <h1>Comments</h1>
    <div id="facebook_comments" aria-live="polite">
      {% include post/facebook_comments.html %}
    </div>
  </section>
{% endif %}
```
{% endliteral %}

That should be all you need to do to get the basic display &amp; adding of comments working, but it'd be good to enable the moderation functionality that facebook provides.  To do this, you need to list the moderators in the head of each of the pages themselves.  Octopress provides an easy way to do this in the `source/includes/custom/head.html` file.  Open it up, and add in this code:

{% literal %}
``` html
{% if site.facebook_appid %}
<meta property="fb:app_id" content="{{ site.facebook_appid }}" />
{% endif %}
```
{% endliteral %}

This should allow moderation by any of the admin users of your facebok app.  And that's all there is to it.

Unfortunately the facebook comment feed is not themeable beyond selecting the dark/light theme, which isn't great.  Probably ok for sites using more conventional colours, but neither option goes perfectly with mine.  At this point I don't think that's a deal breaker, so I'll see how things go.

Any comments, feel free to leave them in the new comment box.
