## scalac jigsaw

Experimental module to support the Scala compiler's enforcement
of [JEP-261 Modules](http://openjdk.java.net/jeps/261)

The hypothesis is that we can use code in the Java 9 library
to construct and interrogate the module graph, rather than
reimplementing this logic in scalac.

Assuming this is possible, we'd like to create a facade
over this to let us link it into the compiler that itself
must still build with Java 8. When the compiler is only
run with Java 8, module support would be disabled.
