---
layout: post
title: "Discovering Cucumber"
date: 2012-09-17 21:20
comments: true
categories: coding technology testing tools bdd cucumber
description: "A story of discovery BDD, Cucumber & RSPec-Expectations"
---

A few months ago I was having a browse of some of the jobs on stackoverflow
careers and I came across a job posting for Amazon.  I wasn't intending on
applying for the job, but I decided to have a look and see what sort of skills
they were looking for.  I was a little surprised to find that there were some
terms in there that I couldn't even begin to guess what they meant.  BDD?
Cucumber?  What are these things?

<!-- more -->

Of course, living in the age of the internet, I didn't have to remain ignorant
for long.  A quick Google led me to the
[Wikipedia for BDD](http://en.wikipedia.org/wiki/Behavior-driven_development)
and more importantly to the web page for [cucumber](http://cukes.info/).
After having a quick read of the examples on the main page for cucumber I was
very intrigued (if you don't know what it is already, have a read for yourself).

At my work we tend to write out test specifications for a feature before
we actually write any code, and then later go on to write the tests based on
the test specs.  Doing things this way has never really felt right to me
though.  I certainly understand the need for solid testing and can see value in
having all your tests planned out before you write a drop of code; However since
the specs are little more than a document describing the test code it feels a
lot like I'm just duplicating information.  And duplication isn't good.

So the idea that with cucumber I can write out tests in plain English and have
actual code tests run based off that is immensely appealing.

My only problem at this stage - cucumber is Ruby, and none of my projects are
written in Ruby (in fact, I don't even know Ruby - but that's not really an
issue).  However a lot of my recent personal projects have been web projects,
and it doesn't really matter what language I test websites with, as most of the
testing can be done without using actual code interfaces (or if actual
interfacing is needed, it can easily be done through HTTP).  Thus, I decided to
try it out for high level testing on some of my web projects.

My initial impressions are very favourable - it's incredibly simple to define
tests first and it no longer feels like I'm duplicating work by writing out
test specs.  I also really like the
[RSpec-Expectations](https://www.relishapp.com/rspec/rspec-expectations/docs)
library that Ruby has, allowing you to write expectations out in almost
English - so much better than the assertion style expectations I'm used to.

Compare `assertEqual(1, 2)` with `1.should eq(2)`.  Not the
best example perhaps, but when you combine this with tools like
[Capybara](http://jnicklas.github.com/capybara/) and get to write expectations
like `field.should have_content(text)` it starts to feel very powerful.  

So, after this brief experiment I'm really rather impressed with Cucumber,
RSpec-Expectations and even Ruby itself - I've even found myself wishing for
RSpec like functionality in all the other languages I work with (more on that
in a future post).  Hopefully in the future I can find a way to integrate
Cucumber or a similar tool into the development processes at work, and
finally do away with redundant test specs.

    this.something = somethingElse('hello', 'blah');
    this.something = somethingElse('hello', 'blah');
    this.something = somethingElse('hello', 'blah');
