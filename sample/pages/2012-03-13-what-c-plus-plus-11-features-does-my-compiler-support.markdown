---
layout: post
title: "What C++11 features does my compiler support?"
date: 2012-03-13 12:18
comments: true
categories: [ technology, c++, c++11, webdev, projects ]
description: "About the C++11 compiler support matrix, a website to allow easier discovery of c++11 compiler support"
keywords: "c++, c++11, compiler support, compiler, features, c++11 features"
---

I've recently become obsessed with C++11, the new standard for the C++
language, that
[massively](http://akrzemi1.wordpress.com/2011/08/11/move-constructor/)
[improves](http://insanecoding.blogspot.com/2010/03/c-201x-variadic-templates.html)
certain areas of the language, and brings all kinds of
[modern](http://www.georgeflanagin.com/c++11/auto.php)
[features](http://www.softwarequalityconnection.com/2011/06/the-biggest-changes-in-c11-and-why-you-should-care/)
that [other](http://www.python.org/)
[languages](http://en.wikipedia.org/wiki/C_Sharp_%28programming_language%29)
have enjoyed for ages.  At present C++11 support is a bit patchy - no one
compiler supports all of the standard, and most of them have chosen to
implement different bits first.

When working in C++, I make use of about 3 compilers - the visual c++ 2010
compiler, and 3 different versions of GCC.  Which of course means that I've got
4 different C++11 feature sets to bear in mind.  This isn't an easy task so I
regularly have to loop up the supported feature tables before I actually use a
feature.  I'm also thinking about switching some of my projects over to use
clang (at least on linux), which should complicate things further.

<!-- more -->

Looking up the feature tables is fine, but it's really not great to have to
look at 2 (or 3) separate pages each time you want to use a new feature. Not to
mention that cross-referencing the tables is a pain if you want to know exactly
which features are available to you.  The 
[apache C++0x support](http://wiki.apache.org/stdcxx/C%2B%2B0xCompilerSupport) 
table goes some way towards lessening this pain, but I feel it still leaves some
room for improvement, it's still not particularly easy to cross-reference 
support, or tell at a glance what's supported on your compiler version.

So, I spent a couple of nights throwing together some simple code to parse the
various C++11 support tables and some code to generate a nice, easily scannable
and filterable table based off the data.  I have decided to name it the 
[C++11 Compiler Support Matrix](http://cpp11.grambo.me.uk).  It's not 100% 
accurate at present (mostly due to un-machine friendly tables), but that's 
something I'm looking to improve over the next few days/weeks.  It also 
currently only has support for GCC, Clang and Visual Studio, but I may expand 
the range in the future.

The code used to generate the website is available on
[github](https://github.com/obmarg/cpp11csm) so if anyone wants to submit a
bug report or pull request then feel free.

The tables it's information are based on can be found 
[here for MSVC](http://blogs.msdn.com/b/vcblog/archive/2011/09/12/10209291.aspx)
, [here for GCC](http://gcc.gnu.org/projects/cxx0x.html) and 
[here for Clang](http://clang.llvm.org/cxx_status.html).  Kudos to the Clang
developers for making a nice easy to parse table. 
