# dir-purger

Keep directories clean of old files.

Mostly an exercise in a long-running clojure programming on Windows.

## Installation

Should be run as a service or similar. On windows, you will need this

http://www.apache.org/dist/commons/daemon/binaries/windows/

Most likely version 1.0.15

## Usage

Create a file dir-purger.config by editing dir-purger.config.SAMPLE

Place it in the same folder as the .jar file, or if you are compiling, in the top dir of the project or above.

Now, run using

    $ java -jar dir-purger-0.1.0-standalone.jar 

## Options

The .config file contains the following fields

:dirs which is a vector of directory names

:pause-minutes is only used in daemon mode NIY

:trial true doesn't delete any file, it just list them

## Examples

...

### Bugs

...

### Any Other Sections
### That You Think
### Might be Useful

## License

Copyright © 2015 FIXME

Distributed under the Eclipse Public License either version 1.0 or (at
your option) any later version.
