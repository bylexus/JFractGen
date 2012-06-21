JFractGen
=========

(c) 2012 Alexander Schenkel, http://www.alexi.ch alex@alexi.ch

A Julia/Mandelbrot Fractal generator written in Java/Swing.

This is just a weekend / Hobby project of me. At the moment the code and output is
in a very early stage, let's call it "0.1-alpha", if already. Give me some time :-)

This project does not need you to understand the Maths behind it. It is just to SHOW
you the beauty of math.

Main class: ch.alexi.fractgen.Main

Run: "java -jar JFractGen.jar"

See also: https://github.com/bylexus/JS-fractgen

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


Planned / future features
-------------------------
- JSON export/import of presets
- create own color palettes
- graphic bulk export/conversion/downsampling



NOTE(s)
-------
This code contains the following 3rd-party libraries:
- the Forms layout classes from http://jgoodies.com
- the macify library from http://simplericity.org/macify/
- the Silk icon set from http://www.famfamfam.com/lab/icons/silk/

