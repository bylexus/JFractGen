JFractGen
=========

(c) 2012-2020 Alexander Schenkel, https://www.alexi.ch alex@alexi.ch

A Julia/Mandelbrot Fractal generator written in Java/Swing.

This is just a weekend / Hobby project of me. It is not at all a state-of-the-art
Fractal calculator, but based on my naïve implementation. Works at the precision of a standard
Java Double (so smalles precision is about +/-4,9E-324, which is pretty much already).

This project does not need you to understand the Maths behind it. It is just to SHOW
you the beauty of math.

Build
-----

You need Maven and JDK >= 1.8 to build the tool.

Build with:

* mvn validate
* mvn package

Run
----

* mvn exec:exec

Main class: ch.alexi.jfractgen.Main

See also an HTML5 version of this project: https://github.com/bylexus/JS-fractgen

Today's already implemented features
------------------------------------
- Fractal settings presets for 'nice' mandelbrot/julia locations
- color palette presets
- define user-presets in json config file (<user.home>/.jfractcalc/presets.json)
- uses chosable nr of Workers, for using multiple CPUs for calculating
- dive deeper by click/rubberband zoom, zoom out
- drag-move the fractal viewport
- smooth colors / hard edges
- history
- dynamic color palette change (without re-calc)
- multiply color palette
- PNG export
- presets defined as JSON data in properties file
- JSON export/import of presets
- Transparency support in color schemes


Planned / future features
-------------------------
- create own color palettes
- fixed repeat pattern: repeat after 256 steps
- flip-flop color repeating: when repeating, repeat the palette forward-backward, instead of flipping back
- graphic bulk export/conversion/downsampling
- preferences:
  - nr of workers / CPU dependant
  - max. Nr of history entries
- aspect-ratio presets
- rubber band should respect actual image aspect ratio



NOTE(s)
-------
This code contains the following 3rd-party libraries:
- the Forms layout classes from http://jgoodies.com
- the macify library from http://simplericity.org/macify/
- the Silk icon set from http://www.famfamfam.com/lab/icons/silk/

