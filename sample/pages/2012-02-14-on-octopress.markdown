---
layout: post
title: "On Octopress"
date: 2012-02-14 23:58
comments: true
categories: [technology, blogging, octopress]
---

So, this is a blog.  Clearly.  Currently not the most exciting of blogs, but
hey - got to start somewhere.  I've chosen to host my blog using
[Octopress](http://octopress.org), and thought I'd write a bit about my
experiences with setting it up, and some of the tools I've decided to use with
it.

Octopress is billed as _"A blogging framework for hackers"_, so as you might
imagine is not really an ideal choice if you're looking for point and click
simplicity.  Configuration and posting is entirely done through editing and
creating text files, which octopress processes to generate static HTML for
publishing.  Not great for anyone looking for a nice GUI to manage their blog,
but also nothing overly complicated.

The text based nature of octopress also brings a number of benefits: 

* Easy backup and version control using git 
* Very easy deployment via git or rsync
* Next to no server load
* No need to setup MySQL, PHP etc. on server

The environment setup for octopress is brief, which is exactly what you want -
a few commands and you're up and running.  Unfortunately one of those commands
involves compiling ruby.  This doesn't add difficulty, as all the usual
configure/make/make install is handled behind the scenes for you. However it
does add a bit more time than I'd have liked. On the slow atom processor in my
server, it probably took about 20 minutes to compile - hardly ideal for an
impulsive test of some software.

After the initial environment setup it's just a case of cloning the octopress
git repository, and changing a few configuration and theming files to your
liking.

By default octopress uses [Markdown](http://en.wikipedia.org/wiki/Markdown) for
formatting, so I've chosen to use [MarkPad](http://code52.org/DownmarkerWPF/)
for editing my blog files.  MarkPad is a nice simple open source WYSIWYG editor
for markdown.  It also seems to support jekyll format files, which are used
behind the scenes by octopress.  It's not entirely suited to using with
octopress - it doesn't seem to generate the same file names as octopress
expects, and the date format in the headers is different, however I'm
planning on looking into ways to improve that.

All in all I'm quite happy with octopress for now, hopefully I actually find
enough topics to write about that I'll use it regularly.
