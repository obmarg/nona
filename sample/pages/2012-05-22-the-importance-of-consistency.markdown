---
layout: post
title: "The Importance of Consistency"
date: 2012-05-22 21:54
comments: true
categories: [ "coding", "best-practices", "technology" ] 
description: "A rant about consistency"
published: true

---

Sometime in the past couple of weeks I posted a quick tweet complaining about inconsistencies in other people's code.  I had a hard time condensing everything I wanted to say into one (or even a few) 160 character block(s), so eventually just gave up and posted a quick moan.  However I felt compelled to elaborate on my thoughts a little bit, so here I go.

Consistency. It's one of the most important, perhaps _the_ most important aspect of modern software development.  Consistency in code helps other programmers (or your future self) better understand unfamiliar areas of a system.  Similar benefits can be obtained from consistent system design;  Someone unfamiliar with a particular part of a system may be able to make some assumptions about how it works based on their previous experiences.  Even a user interface benefits hugely from a consistent look, feel & behaviour.

In this post, I primarily want to talk about coding consistency, though I'll maybe touch on a few other bits and pieces.

<!-- more -->

### What is consistency?

There's a lot of different aspects to coding consistency - from simple things like using the same variable names to identify the same data in different parts of a system;  Larger undertakings like using consistent design patterns throughout the architecture of an application; Ensuring that broadly similar functions behave in broadly similar ways also comes into it.

The popular [DRY](http://en.wikipedia.org/wiki/Don't_repeat_yourself) principle in software development is  amongst other things enforcing consistency - if every piece of data in a system has a single representation, then    that piece of data is guaranteed to be consistent across the whole system.

So, why do I think consistency is so important?  As mentioned above it makes code easier to follow, but it doesn't end there.   If code is written in a consistent manner it makes identifying similar or duplicated functionality easier, and allows for vastly simplified refactoring.  If the patterns for using functions or classes are broadly similar then there is less need to read through (possibly non-existent) documentation.  Communicating also becomes a much easier task if everyone is speaking the same domain language.

Of course there are times when consistency might not be appropriate.  For example, you shouldn't fall in to the trap of re-using an unsuitable design pattern just because it is used elsewhere.  You know what they say about [hammers](http://en.wikipedia.org/wiki/Law_of_the_instrument).

### Inconsistency

Rather than go on about consistency for another few paragraphs I'd like to go over a few examples of inconsistency, to illustrate some of the downsides of not keeping things consistent in the first place.

Firstly there's the example that inspired this blog post.

{% codeblock lang:cpp %}
void CreateServiceOne( string serviceUrl, string uniqueId )
{...}

void CreateServiceTwo( string deviceId, string serviceUrl )
{…}

void DoCreate( string cameraId )
{
	CreateServiceOne( "http://blah", cameraId );
	CreateServiceTwo( cameraId, "http://blah" );
}

{% endcodeblock %}

Obviously this isn't the exact code, but it's a rough summary of the two major problems I had with that particular piece of code.  

The first of those problems, was the name(s) of the cameraId variable - as it made it's way through several different layers of code, it went through 3 different names.  Perhaps not the biggest issue ever, but it did mean I had to spend an extra 5 minutes or so making absolutely sure that the deviceId at level 3 was indeed the same bit of data as the cameraId at level 1.

And of course the second problem was the order of the arguments to the `CreateServiceX` functions.  These functions did basically the same job with the same data, but for different services.  But for some unclear (and undocumented) reason they all took a variation of different arguments in different orders.  Again, not the biggest deal, but it resulted in a little extra effort was needed to check that the functions were in fact doing the same thing and that common functionality could be refactored out.

Now, these are fairly minor issues (probably caused by slightly rushed work, or just from a selection of different developers working in the area), but that doesn't mean that inconsistencies like this aren't a problem.

I found it quite interesting to read through [eevee's massive PHP rant](http://me.veekun.com/blog/2012/04/09/php-a-fractal-of-bad-design/) that was going around recently.  Though I can imagine it's unpopular with a lot of PHP users, I think it has a number of good points.

Much of the article seems to be dealing with inconsistencies:  functions that take the same arguments but in different orders;  Functions that do several different things depending on several configuration values;   Operators that behave completely differently under different circumstances.  A lot of these issues are again fairly minor, but when taken as a whole they really do add up to a lot of difficulties.

Even worse in a system like PHP that is used by millions (I assume) worldwide, these inconsistencies become incredibly hard to fix.  Sure, the actual fixes are potentially fairly easy, but you run the risk of breaking thousands of websites that relied on the old inconsistent behaviour.

Now most systems are never likely to see the levels of deployment that PHP does, but that doesn't make this stuff any less important.  Even changes to an API with a single user could end up subtly broken by some "minor" change.

So, hopefully somewhere in this elongated moan I have convinced some of you of the importance of consistency.  If you already agreed with me then great, and if not please feel free to tell me why in the comments.
