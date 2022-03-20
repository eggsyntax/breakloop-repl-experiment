# breakloop-repl

Ultimately a failed proof-of-concept; I only had a bit of time to spend on it & was encountering some indeterminate behavior I didn't manage to solve quickly enough.

I'd still ultimately like to find a way to have the full Common Lisp-style breakloop in Clojure, but it's challenging. As far as I can see, the more obvious route (custom REPL behavior) fails due to lack of continuations on the JVM ([Project Loom](https://cr.openjdk.java.net/~rpressler/loom/Loom-Proposal.html) may solve this at some point). This was an attempt to finesse that by `try`ing each expression and drop into a REPL if an error was encountered. This wouldn't have been a complete solution by any means even if I'd gotten it working, since any side effects in the code would have potentially have already been executed by the time the REPL starts, but it seemed worth trying the experiment.

## Usage

FIXME

## License

Copyright © 2021 FIXME

This program and the accompanying materials are made available under the
terms of the Eclipse Public License 2.0 which is available at
http://www.eclipse.org/legal/epl-2.0.

This Source Code may also be made available under the following Secondary
Licenses when the conditions for such availability set forth in the Eclipse
Public License, v. 2.0 are satisfied: GNU General Public License as published by
the Free Software Foundation, either version 2 of the License, or (at your
option) any later version, with the GNU Classpath Exception which is available
at https://www.gnu.org/software/classpath/license.html.
