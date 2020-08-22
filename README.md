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

```
$ mvn validate
$ mvn package
```

Run
----

with maven:

```
$ mvn exec:exec
```

pre-built JAR:

```
$ java -Xmx1024M -jar jfractgen-0.9.jar
# Or use the shell script:
$ ./jfractgen.sh
```

**Main class**: ch.alexi.jfractgen.Main

See also an HTML5 version of this project: https://github.com/bylexus/JS-fractgen

Today's already implemented features
------------------------------------
- Fractal settings presets for 'nice' mandelbrot/julia locations
- color palette presets
- define user-presets in json config file ([user.home]/.jfractcalc/presets.json)
- uses chosable nr of Workers, for using multiple CPUs for calculating
- dive deeper by click/rubberband zoom, zoom out
- drag-move the fractal viewport
- smooth colors / hard edges
- multiple color palette models:
  - fixed repeat pattern: repeat after n steps
  - dynamic color palette: stretch color palette to match iterations
- history / undo
- dynamic color palette change (without re-calc)
- multiple color palettes, multiple color apply strategies
- PNG export
- presets defined as JSON data in properties file
- JSON export/import of presets
- Transparency support in color schemes
- Super-configurable: All relevant (and not so relevant) Fractal and color parameters can manually be set.
- preferences:
  - nr of workers / CPU dependant
  - max. Nr of history entries
  - Background color


Planned / future features
-------------------------
- create own color palettes
- graphic bulk export/conversion/downsampling
- aspect-ratio presets



NOTE(s)
-------
This code contains the following 3rd-party libraries:
- the Forms layout classes from http://jgoodies.com
- the macify library from http://simplericity.org/macify/
- the Silk icon set from http://www.famfamfam.com/lab/icons/silk/

