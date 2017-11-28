# COSC331Proj2
Project 2 for Networking
Requirements:
- Must use TCP protocol
- Server must allow client to choose from a list of (at minimum 3) files
- Client must get/save file in its entirety
- Implemented in Java 7
- Transfer between 2 physical computers (no localhost only)
- Compilation on the command line (you cannot demo with IDE)
- Version Control on Github. You must commit after each method or class change.
Instructions:
Make sure the FileServer is running before starting the ClientSide.

Upon running the ClientSide, the client machine will automatically request a
connection to the server. The server will allow the connection and then send
a count of how many files are available as well as their names to the client.
The client receives the names printed to their console and may then input one
of the files to request it from the server. If a correct file name is sent to
the server, then the server will send the requested file. If anything other
than a valid file name is received, the server will send an error message
printed to the client's console. If the file name was valid, the client will
save the requested file with the proper file name to the folder it is in. After
the file is saved the client terminates the connection. The server remains
running and awaits another connection. Only one client may connect and request
a single file at a time.
