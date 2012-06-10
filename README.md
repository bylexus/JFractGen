JFractGen
=========

A Julia/Mandelbrot Fractal generator written in Java/Swing.

This is just a weekend / Hobby project of me. At the moment the code and output is
in a very early stage, let's call it "0.1-alpha", if already. Give me some time :-)

Main class: ch.alexi.fractgen.Main

See also: https://github.com/bylexus/JS-fractgen

Today's already implemented features
------------------------------------
- Fractal settings presets for 'nice' mandelbrot locations
- color palette presets
- uses chosable nr of Workers, for using multiple CPUs for calculating
- dive deeper by click/rubberband zoom
- history
- dynamic color palette change (without re-calc)
- PNG export
- presets defined as JSON data in properties file


Planned / future features
-------------------------
- JSON export of presets
- define user-presets in json config file
- graphic export/conversion
- multiply color palette
- more presets
- drag-move
- smooth colors

NOTE(s)
-------
This code contains the Forms layout classes from http://jgoodies.com

